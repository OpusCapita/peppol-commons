package com.opuscapita.peppol.commons.auth;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class AuthorizationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationService.class);

    @Value("${service-client.username:''}")
    private String username;
    @Value("${service-client.password:''}")
    private String password;
    @Value("${service-client.client-key:''}")
    private String clientKey;
    @Value("${service-client.client-secret:''}")
    private String clientSecret;

    @Value("${peppol.auth.url}")
    private String host;
    @Value("${peppol.auth.port}")
    private String port;

    private RestTemplate restTemplate;
    private EhCacheCacheManager cacheManager;

    @Autowired
    public AuthorizationService(RestTemplateBuilder restTemplateBuilder, EhCacheCacheManager cacheManager) {
        this.restTemplate = restTemplateBuilder.build();
        this.cacheManager = cacheManager;
    }

    public void setAuthorizationHeader(HttpHeaders headers) {
        AuthorizationResponse result = getTokenDetails(username);
        if (result == null) {
            result = updateTokenDetails(username);
        }
        headers.set("X-User-Id-Token", result.getId_token());
    }

    /**
     * Fetches the access_token and token_type from the auth service
     * using service-client credentials from consul config repository
     * <p>
     * Note that access token is being cached and may not be fetched.
     *
     * @return authorization header like "Bearer MzA4MjkyZWItZTQyOC00Y2EzLTgxOWUtOGNhODlkNjI"
     */
    public String getAuthorizationHeader() {
        AuthorizationResponse result = getTokenDetails(username);
        return result.getAuthorizationHeader();
    }

    /**
     * Fetches the id_token from the auth service
     * <p>
     * Note that this is only for direct accesses skipping kong.
     *
     * @return X-User-Id-Token
     */
    public String getUserIdTokenHeader() {
        AuthorizationResponse result = getTokenDetails(username);
        return result.getId_token();
    }

    public AuthorizationResponse getTokenDetails(String serviceName) {
        try {
            return cacheManager.getCache("auth.token").get(serviceName, AuthorizationResponse.class);
        } catch (Exception e) {
            throw new AuthServiceException("Auth token should be fetched from cache: " + serviceName);
        }
    }

    @CachePut("auth.token")
    public AuthorizationResponse updateTokenDetails(String serviceName) {
        logger.debug("Token details requested from auth service");
        checkRequiredConfigParameters(serviceName);

        String endpoint = getEndpoint("/auth/token");
        List<NameValuePair> queryParams = new ArrayList<>();
        queryParams.add(new BasicNameValuePair("username", serviceName));
        queryParams.add(new BasicNameValuePair("password", password));
        queryParams.add(new BasicNameValuePair("grant_type", "password"));
        queryParams.add(new BasicNameValuePair("scope", "id email phone userInfo roles"));
        String body = URLEncodedUtils.format(queryParams, Charset.defaultCharset());
        logger.debug("Fetching token from endpoint: " + endpoint);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", setAuthorizationHeader());
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        logger.debug("Setting http Authorization headers");

        try {
            ResponseEntity<AuthorizationResponse> result = restTemplate.exchange(endpoint, HttpMethod.POST, entity, AuthorizationResponse.class);
            logger.debug("Token details fetched successfully from auth service");
            return result.getBody();
        } catch (Exception e) {
            throw new AuthServiceException(e.getMessage());
        }
    }

    private void checkRequiredConfigParameters(String serviceName) {
        if (StringUtils.isBlank(serviceName)) {
            throw new AuthServiceException("Auth service could not be called: Empty field \"username\".");
        }
        if (StringUtils.isBlank(password)) {
            throw new AuthServiceException("Auth service could not be called: Empty field \"password\".");
        }
        if (StringUtils.isBlank(clientKey)) {
            throw new AuthServiceException("Auth service could not be called: Empty field \"clientKey\".");
        }
        if (StringUtils.isBlank(clientSecret)) {
            throw new AuthServiceException("Auth service could not be called: Empty field \"clientSecret\".");
        }
    }

    private String setAuthorizationHeader() {
        String auth = clientKey + ":" + clientSecret;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.defaultCharset()));
        return "Basic " + new String(encodedAuth);
    }

    private String getEndpoint(String url) {
        return UriComponentsBuilder
                .fromUriString("http://" + host)
                .port(port)
                .path(url)
                .toUriString();
    }
}

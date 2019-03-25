package com.opuscapita.peppol.commons.storage.blob;

import com.opuscapita.peppol.commons.auth.AuthorizationService;
import com.opuscapita.peppol.commons.storage.StorageException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@SuppressWarnings("WeakerAccess")
public class BlobServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(BlobServiceClient.class);

    @Value("${peppol.storage.blob.url}")
    private String host;
    @Value("${peppol.storage.blob.port}")
    private String port;

    @Value("${peppol.auth.tenant.id}")
    private String tenant;

    private RestTemplate restTemplate;
    private AuthorizationService authService;

    public BlobServiceClient(AuthorizationService authService, RestTemplateBuilder restTemplateBuilder) {
        this.authService = authService;
        this.restTemplate = restTemplateBuilder.build();
    }

    public List<BlobServiceResponse> listFolder(String folder) throws StorageException {
        logger.info("File list requested from blob service for folder: " + folder);
        try {
            ResponseEntity<BlobServiceResponse[]> result = get(folder, BlobServiceResponse[].class);
            logger.info("File list fetched successfully from blob service for folder: " + folder);
            return new ArrayList<>(Arrays.asList(result.getBody()));
        } catch (Exception e) {
            throw new StorageException("Error occurred while trying to read the file list from blob service.", e);
        }
    }

    public InputStream getFile(String path) throws StorageException {
        logger.info("File requested from blob service for path: " + path);
        try {
            ResponseEntity<String> result = get(path, String.class);
            logger.info("File fetched successfully from blob service for path: " + path);
            return new ByteArrayInputStream(result.getBody().getBytes());
        } catch (Exception e) {
            throw new StorageException("Error occurred while trying to read the file from blob service.", e);
        }
    }

    private <T> ResponseEntity<T> get(String path, Class<T> type) throws StorageException {
        String endpoint = getEndpoint(path);
        logger.debug("Reading file from endpoint: " + endpoint);

        HttpHeaders headers = new HttpHeaders();
        authService.setAuthorizationHeader(headers);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        logger.debug("Setting http headers content type to application json");

        return restTemplate.exchange(endpoint, HttpMethod.GET, entity, type);
    }

    public BlobServiceResponse putFile(InputStream data, String path) throws StorageException {
        logger.info("File storage requested from blob service to path: " + path);
        String endpoint = getEndpoint(path);
        logger.debug("Putting file to endpoint: " + endpoint);

        HttpHeaders headers = new HttpHeaders();
        authService.setAuthorizationHeader(headers);
        headers.set("Transfer-Encoding", "chunked");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        HttpEntity<Resource> entity = new HttpEntity<>(new InputStreamResource(data), headers);
        logger.debug("Wrapped and set the request body as input stream");

        try {
            ResponseEntity<BlobServiceResponse> result = restTemplate.exchange(endpoint, HttpMethod.PUT, entity, BlobServiceResponse.class);
            logger.info("File stored successfully to blob service path: " + path);
            return result.getBody();
        } catch (Exception e) {
            throw new StorageException("Error occurred while trying to put the file to blob service", e);
        }
    }

    public String moveFile(String currentPath, String destinationPath) throws StorageException {
        logger.info("File move requested from blob service for file: " + currentPath + " to folder: " + destinationPath);
        String endpoint = getEndpoint("/move" + currentPath);
        logger.debug("Moving file from endpoint: " + endpoint);

        HttpHeaders headers = new HttpHeaders();
        authService.setAuthorizationHeader(headers);
        headers.setContentType(MediaType.APPLICATION_JSON);
        String wrappedDestination = "{\"dstPath\":\"" + destinationPath + "\"}";
        HttpEntity<String> entity = new HttpEntity<>(wrappedDestination, headers);
        logger.debug("Wrapped and set the request body as string");

        try {
            ResponseEntity<String> result = restTemplate.exchange(endpoint, HttpMethod.PUT, entity, String.class);
            logger.info("File moved successfully in blob service to path: " + destinationPath);
            return destinationPath;
        } catch (Exception e) {
            throw new StorageException("Error occurred while trying to move the file in blob service", e);
        }
    }

    public void remove(String path) throws StorageException {
        logger.info("File remove requested from blob service for path: " + path);
        String endpoint = getEndpoint(path);
        logger.debug("Removing file from endpoint: " + endpoint);

        HttpHeaders headers = new HttpHeaders();
        authService.setAuthorizationHeader(headers);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        logger.debug("Wrapped and set the request body as string");

        try {
            restTemplate.exchange(endpoint, HttpMethod.DELETE, entity, Void.class);
            logger.info("File(s) removed successfully from blob service for path: " + path);
        } catch (Exception e) {
            throw new StorageException("Error occurred while trying to remove the file in blob service", e);
        }
    }

    public boolean isExists(String path) throws StorageException {
        logger.info("File exists check requested from blob service for path: " + path);
        String endpoint = getEndpoint(path);
        logger.debug("Checking file at endpoint: " + endpoint);

        HttpHeaders headers = new HttpHeaders();
        authService.setAuthorizationHeader(headers);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        logger.debug("Wrapped and set the request body as string");

        try {
            ResponseEntity<BlobServiceResponse> result = restTemplate.exchange(endpoint, HttpMethod.HEAD, entity, BlobServiceResponse.class);
            return path.equals(result.getBody().getPath());
        } catch (Exception e) {
            return false;
        }
    }

    private String getEndpoint(String path) throws StorageException {
        if (StringUtils.isBlank(tenant)) {
            throw new StorageException("Blob service cannot be used: Missing configuration \"peppol.auth.tenant.id\".");
        }

        return UriComponentsBuilder
                .fromUriString("http://" + host)
                .port(port)
                .path("/api/" + tenant + "/files" + path)
                .queryParam("inline", "true")
                .queryParam("createMissing", "true")
                .queryParam("recursive", "true")
                .toUriString();
    }

}
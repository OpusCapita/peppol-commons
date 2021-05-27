package com.opuscapita.peppol.commons.eventing.jsd;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import java.net.*;
import java.io.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.opuscapita.peppol.commons.eventing.TicketContentFormatter;

@Component
@ConditionalOnProperty(name = "snc.enabled", havingValue = "true", matchIfMissing = true)
public class JsdREST implements Jsd {

    private static final Logger logger = LoggerFactory.getLogger(JsdREST.class);

    private final JsdConfiguration configuration;

    @Autowired
    public JsdREST(JsdConfiguration configuration) {
        this.configuration = configuration;
    }

    public void insert(JsdEntity entity) throws IOException {

        byte[] postData = entity.getAsJSON().getBytes("utf-8");
        this.postRequest(postData);
    }

    @SuppressWarnings("Duplicates")
    private void postRequest(byte[] postData) throws IOException {
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        //CredentialsProvider credsProvider2 = new BasicCredentialsProvider();

        logger.info("Create ticket on url " + this.configuration.getHttpUrl() + " with user " + this.configuration.getHttpUsername() );

        URL aURL = new URL(this.configuration.getHttpUrl());
        String scope = aURL.getAuthority();
        String host = aURL.getHost();
        String protocol = aURL.getProtocol();
        Integer port = aURL.getPort();

        if( port == -1 ) {
          if( protocol.equals("https")) {
            port = 443;
          }
          else {
            port = 80; //not secure
            logger.warn( this.configuration.getHttpUrl() + " is not secure, try using https" );
          }
        }

        credsProvider.setCredentials(new AuthScope(
                        new HttpHost(scope)),
                new UsernamePasswordCredentials(this.configuration.getHttpUsername(), this.configuration.getHttpPassword()));

        try {

            HttpHost targetHost = new HttpHost( host , port, protocol );

            AuthCache authCache = new BasicAuthCache();
            authCache.put(targetHost, new BasicScheme());

            HttpClientContext context = HttpClientContext.create();
            context.setCredentialsProvider(credsProvider);
            context.setAuthCache(authCache);

            CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).useSystemProperties().build();

            Throwable exp1 = null;

            try {
                HttpPost httpPost = new HttpPost(this.configuration.getHttpUrl());
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-Type", "application/json");
                HttpEntity entity = new ByteArrayEntity(postData);
                httpPost.setEntity(entity);

                CloseableHttpResponse response = httpClient.execute(httpPost, context);
                Throwable exp2 = null;

                try {
                    String responseBody = EntityUtils.toString(response.getEntity());

                    if (!this.isSuccessfulResponse(responseBody)) {
                        logger.error("Failed to insert ticket. More details here: " +
                              response.getStatusLine().getStatusCode() + " " +
                              responseBody
                              );

                        throw new IOException("Failed to insert ticket: " + responseBody);
                    }
                    else {
                      logger.info("Inserted ticket, details: " +
                            response.getStatusLine().getStatusCode() + " " +
                            responseBody
                            );
                    }
                } catch (Throwable exception) {
                    exp2 = exception;
                    throw exception;
                } finally {
                    if (response != null) {
                        if (exp2 != null) {
                            try {
                                response.close();
                            } catch (Throwable exception) {
                                exp2.addSuppressed(exception);
                            }
                        } else {
                            response.close();
                        }
                    }
                }
            } catch (Throwable exception) {
                exp1 = exception;
                throw exception;
            } finally {
                if (httpClient != null) {
                    if (exp1 != null) {
                        try {
                            httpClient.close();
                        } catch (Throwable exception) {
                            exp1.addSuppressed(exception);
                        }
                    } else {
                        httpClient.close();
                    }
                }
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            throw new IOException(exception);
        }
    }

    private boolean isSuccessfulResponse(String responseBody) {
        JsonElement jsonResponse = (JsonElement) (new Gson()).fromJson(responseBody, JsonElement.class);
        JsonObject result = jsonResponse.getAsJsonObject();
        if (result == null) {
            return false;
        } else {

            JsonPrimitive key = result.getAsJsonPrimitive("key");
            JsonPrimitive id = result.getAsJsonPrimitive("id");
            return key != null && id != null;
        }
    }
}

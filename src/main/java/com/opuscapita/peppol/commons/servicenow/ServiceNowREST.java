package com.opuscapita.peppol.commons.servicenow;

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
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

@SuppressWarnings({"Duplicates", "WeakerAccess"})
public class ServiceNowREST implements ServiceNow {

    private static final Logger logger = LoggerFactory.getLogger(ServiceNowREST.class);

    private static String HOST = "lion.itella.net";

    private final ServiceNowConfiguration configuration;

    public ServiceNowREST(ServiceNowConfiguration configuration) {
        this.configuration = configuration;
    }

    public void insert(SncEntity entity) throws IOException {
        entity.setBsc(this.configuration.getSncBsc());
        entity.setBusinessGroup(this.configuration.getSncBusinessGroup());
        entity.setFrom(this.configuration.getSncFrom() + HOST);
        byte[] postData = (new Gson()).toJson(entity).getBytes("utf-8");
        this.postRequest(postData);
    }

    protected void postRequest(byte[] postData) throws IOException {
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(new AuthScope(
                        new HttpHost(this.configuration.getHostName())),
                new UsernamePasswordCredentials(this.configuration.getHttpUsername(), this.configuration.getHttpPassword()));

        try {
            CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).useSystemProperties().build();
            Throwable exp1 = null;

            try {
                HttpPost httpPost = new HttpPost(this.configuration.getHttpUrl());
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-Type", "application/json");
                HttpEntity entity = new ByteArrayEntity(postData);
                httpPost.setEntity(entity);

                CloseableHttpResponse response = httpClient.execute(httpPost);
                Throwable exp2 = null;

                try {
                    String responseBody = EntityUtils.toString(response.getEntity());
                    Header locationHeader = response.getFirstHeader("Location");
                    String location = locationHeader != null ? locationHeader.getValue() : "";
                    if (!this.isSuccessfulResponse(responseBody)) {
                        logger.error("Failed to insert incident. More details here: " + location);
                        logger.error(responseBody);
                        throw new IOException("Failed to insert incident: " + responseBody);
                    }

                    logger.info("Incident inserted. More details here: " + location);
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

    protected boolean isSuccessfulResponse(String responseBody) {
        JsonElement jsonResponse = (JsonElement) (new Gson()).fromJson(responseBody, JsonElement.class);
        JsonObject result = jsonResponse.getAsJsonObject().getAsJsonObject("result");
        if (result == null) {
            return false;
        } else {
            JsonPrimitive importState = result.getAsJsonPrimitive("sys_import_state");
            return importState != null && "inserted".equals(importState.getAsString());
        }
    }

    static {
        try {
            HOST = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException var1) {
            var1.printStackTrace();
        }
    }
}

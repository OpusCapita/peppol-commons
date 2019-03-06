package com.opuscapita.peppol.commons.eventing.servicenow;

import java.net.URI;
import java.net.URISyntaxException;

public class ServiceNowConfiguration {

    private String httpUrl;
    private String httpUsername;
    private String httpPassword;
    private String sncBsc;
    private String sncFrom;
    private String sncBusinessGroup;
    private String hostName;

    public ServiceNowConfiguration(String httpUrl, String httpUsername, String httpPassword, String sncBsc, String sncFrom, String sncBusinessGroup) {
        this.httpUrl = httpUrl;
        this.httpUsername = httpUsername;
        this.httpPassword = httpPassword;
        this.sncBsc = sncBsc;
        this.sncFrom = sncFrom;
        this.sncBusinessGroup = sncBusinessGroup;

        try {
            URI uri = new URI(httpUrl);
            this.hostName = uri.getHost();
        } catch (URISyntaxException var8) {
            this.hostName = "instance.service-now.com";
            var8.printStackTrace();
        }
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public String getHttpUsername() {
        return httpUsername;
    }

    public String getHttpPassword() {
        return httpPassword;
    }

    public String getSncBsc() {
        return sncBsc;
    }

    public String getSncFrom() {
        return sncFrom;
    }

    public String getSncBusinessGroup() {
        return sncBusinessGroup;
    }

    public String getHostName() {
        return hostName;
    }
}

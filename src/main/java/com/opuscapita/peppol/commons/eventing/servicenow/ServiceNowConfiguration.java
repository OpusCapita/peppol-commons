package com.opuscapita.peppol.commons.eventing.servicenow;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class ServiceNowConfiguration {

    @Value("${snc.rest.url}")
    private String httpUrl;

    @Value("${snc.rest.username}")
    private String httpUsername;

    @Value("${snc.rest.password}")
    private String httpPassword;

    @Value("${snc.bsc}")
    private String sncBsc;

    @Value("${snc.from}")
    private String sncFrom;

    @Value("${snc.businessGroup}")
    private String sncBusinessGroup;

    private String hostName;

    public ServiceNowConfiguration() {
        try {
            this.hostName = new URI(httpUrl).getHost();
        } catch (Exception e) {
            this.hostName = "instance.service-now.com";
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

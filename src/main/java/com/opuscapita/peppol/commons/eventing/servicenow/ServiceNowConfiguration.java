package com.opuscapita.peppol.commons.eventing.servicenow;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceNowConfiguration {

    @Value("${snc.rest.url:localhost}")
    private String httpUrl;

    @Value("${snc.rest.scope:localhost}")
    private String httpScope;

    @Value("${snc.rest.username:test}")
    private String httpUsername;

    @Value("${snc.rest.password:test}")
    private String httpPassword;

    @Value("${snc.bsc:test}")
    private String sncBsc;

    @Value("${snc.from:peppol@opuscapita.com}")
    private String sncFrom;

    @Value("${snc.businessGroup:test}")
    private String sncBusinessGroup;

    public String getHttpUrl() {
        return httpUrl;
    }

    public String getHttpScope() {
        return httpScope;
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
}
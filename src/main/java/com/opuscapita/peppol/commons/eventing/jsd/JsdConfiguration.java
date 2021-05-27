package com.opuscapita.peppol.commons.eventing.jsd;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JsdConfiguration {

    @Value("${snc.rest.url:localhost}")
    private String httpUrl;

    @Value("${snc.rest.username:test}")
    private String httpUsername;

    @Value("${snc.rest.password:test}")
    private String httpPassword;


    public void setTestValues(  String url, String usr, String pass ) {
      this.httpUrl=url;
      this.httpUsername=usr;
      this.httpPassword=pass;
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


}

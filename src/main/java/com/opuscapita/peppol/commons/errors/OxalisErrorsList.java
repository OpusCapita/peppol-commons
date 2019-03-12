package com.opuscapita.peppol.commons.errors;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "peppol.common.errors.oxalis")
public class OxalisErrorsList {
    
    private List<OxalisError> list;

    public List<OxalisError> getList() {
        return list;
    }

    public void setList(List<OxalisError> oxalisErrors) {
        this.list = oxalisErrors;
    }
}
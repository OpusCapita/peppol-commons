package com.opuscapita.peppol.commons.template;

public class CommonRestResponse {

    private String message;

    public CommonRestResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

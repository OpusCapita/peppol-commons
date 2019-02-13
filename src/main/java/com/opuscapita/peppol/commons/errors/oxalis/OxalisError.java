package com.opuscapita.peppol.commons.errors.oxalis;

public class OxalisError {

    private String mask;
    private SendingErrors type;

    public OxalisError() {
    }

    public OxalisError(SendingErrors type, String mask) {
        this.type = type;
        this.mask = mask;
    }

    public SendingErrors getType() {
        return type;
    }

    public void setType(SendingErrors type) {
        this.type = type;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }
}

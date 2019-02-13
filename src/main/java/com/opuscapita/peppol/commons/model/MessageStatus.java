package com.opuscapita.peppol.commons.model;

public enum MessageStatus {

    sent("sent"),
    failed("failed"),
    invalid("invalid"),
    resolved("resolved"),
    processing("processing"),
    reprocessed("reprocessed");

    private final String status;

    MessageStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.status;
    }
}

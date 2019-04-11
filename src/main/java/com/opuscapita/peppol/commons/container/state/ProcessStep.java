package com.opuscapita.peppol.commons.container.state;

public enum ProcessStep {
    INBOUND,
    PROCESSOR,
    VALIDATOR,
    OUTBOUND,
    NETWORK,
    REPROCESSOR,

    WEB,
    REST,
    TEST,
    UNKNOWN;
}

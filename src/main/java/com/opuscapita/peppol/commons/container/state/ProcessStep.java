package com.opuscapita.peppol.commons.container.state;

public enum ProcessStep {
    INBOUND,
    PROCESS,
    ROUTING,
    VALIDATION,
    OUTBOUND,
    NETWORK,
    REPROCESS,

    WEB,
    REST,
    TEST,
    UNKNOWN;
}

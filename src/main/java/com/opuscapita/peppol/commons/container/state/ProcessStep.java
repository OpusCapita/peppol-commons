package com.opuscapita.peppol.commons.container.state;

public enum ProcessStep {
    INBOUND,
    PREPROCESS,
    ROUTING,
    VALIDATION,
    OUTBOUND,
    FILE_TO_MQ,
    MQ_TO_FILE,
    REPROCESS,

    WEB,
    REST,
    TEST,
    UNKNOWN;
}

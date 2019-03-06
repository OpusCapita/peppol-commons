package com.opuscapita.peppol.commons.container.state;

public enum ProcessType {

    WEB,
    REST,
    TEST,
    UNKNOWN,

    OUT_FILE_TO_MQ,
    OUT_PREPROCESS,
    OUT_ROUTING,
    OUT_VALIDATION,
    OUT_OUTBOUND,
    OUT_REPROCESS,
    OUT_PEPPOL_RETRY,
    OUT_TEST,

    IN_INBOUND,
    IN_PREPROCESS,
    IN_ROUTING,
    IN_VALIDATION,
    IN_MQ_TO_FILE,
    IN_REPROCESS,
    IN_TEST;
}

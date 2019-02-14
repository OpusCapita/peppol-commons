package com.opuscapita.peppol.commons.servicenow;

import java.io.IOException;

public interface ServiceNow {
    void insert(SncEntity sncEntity) throws IOException;
}

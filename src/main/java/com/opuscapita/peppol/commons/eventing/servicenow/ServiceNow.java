package com.opuscapita.peppol.commons.eventing.servicenow;

import java.io.IOException;

public interface ServiceNow {

    void insert(SncEntity sncEntity) throws IOException;

}

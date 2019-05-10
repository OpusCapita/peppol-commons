package com.opuscapita.peppol.commons.eventing.servicenow;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "snc.enabled", havingValue = "false")
public class DisabledServiceNowRest implements ServiceNow {

    @Override
    public void insert(SncEntity sncEntity) {
        // do nothing.
    }
}

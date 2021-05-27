package com.opuscapita.peppol.commons.eventing.jsd;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "snc.enabled", havingValue = "false")
public class DisabledJsdRest implements Jsd {

    @Override
    public void insert(JsdEntity JsdEntity) {
        // do nothing.
    }
}

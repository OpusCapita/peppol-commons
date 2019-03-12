package com.opuscapita.peppol.commons.eventing;

import com.opuscapita.peppol.commons.container.ContainerMessage;

public interface EventReporter {

    void reportStatus(ContainerMessage cm);

}

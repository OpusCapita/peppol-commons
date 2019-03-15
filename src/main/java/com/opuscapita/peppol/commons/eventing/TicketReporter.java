package com.opuscapita.peppol.commons.eventing;

import com.opuscapita.peppol.commons.container.ContainerMessage;

public interface TicketReporter {

    void reportWithContainerMessage(ContainerMessage cm, Throwable e, String shortDescription);

    void reportWithContainerMessage(ContainerMessage cm, Throwable e, String shortDescription, String additionalDetails);

    void reportWithoutContainerMessage(String customerId, String fileName, Throwable e, String shortDescription);

    void reportWithoutContainerMessage(String customerId, String fileName, Throwable e, String shortDescription, String additionalDetails);
}

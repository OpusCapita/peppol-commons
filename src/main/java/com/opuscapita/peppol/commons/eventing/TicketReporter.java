package com.opuscapita.peppol.commons.eventing;

import com.opuscapita.peppol.commons.container.ContainerMessage;

public interface TicketReporter {

    void reportWithContainerMessage(ContainerMessage cm, Throwable e, String shortDescription);

    void reportWithContainerMessage(ContainerMessage cm, Throwable e, String shortDescription, String additionalDetails);

    void reportWithoutContainerMessage(String customerId, Throwable e, String shortDescription, String correlationId, String fileName);

    void reportWithoutContainerMessage(String customerId, Throwable e, String shortDescription, String correlationId, String fileName, String additionalDetails);
}

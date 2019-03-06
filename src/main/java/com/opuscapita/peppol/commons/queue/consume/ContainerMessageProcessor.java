package com.opuscapita.peppol.commons.queue.consume;

import com.opuscapita.peppol.commons.container.ContainerMessage;
import org.jetbrains.annotations.NotNull;

public interface ContainerMessageProcessor {

    void process(@NotNull ContainerMessage cm);

    void setContainerMessageConsumer(ContainerMessageConsumer consumer);
}

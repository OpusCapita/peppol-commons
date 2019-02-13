package com.opuscapita.peppol.commons.template;

import com.opuscapita.peppol.commons.container.ContainerMessage;
import org.jetbrains.annotations.NotNull;

public interface ContainerMessageProcessor {

    void process(@NotNull ContainerMessage cm);

    default void setContainerMessageConsumer(ContainerMessageConsumer controller) {
    }
}

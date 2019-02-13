package com.opuscapita.peppol.commons.template;

import com.opuscapita.peppol.commons.container.ContainerMessage;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ContainerMessageConsumer {

    void consume(@NotNull ContainerMessage cm) throws Exception;

}

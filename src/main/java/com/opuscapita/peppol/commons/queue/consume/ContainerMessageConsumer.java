package com.opuscapita.peppol.commons.queue.consume;

import com.opuscapita.peppol.commons.container.ContainerMessage;
import org.jetbrains.annotations.NotNull;

public interface ContainerMessageConsumer {

    void consume(@NotNull ContainerMessage cm) throws Exception;

}

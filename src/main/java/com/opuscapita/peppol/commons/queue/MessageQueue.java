package com.opuscapita.peppol.commons.queue;

import com.opuscapita.peppol.commons.container.ContainerMessage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Common interface for all communications with MQ.
 */
public interface MessageQueue {
    /**
     * <p>Connect and send a string message.
     * Connection string format is:</p>
     * <code>
     *     queue_name:parameter1=value1,parameter2,parameter3=value3
     * </code>
     * <p>Where known parameters are:</p>
     * <ul>
     *     <li>exchange=name - name of the exchange to use</li>
     *     <li>x-delay=n - will put a header to the message that is recognizable by delayed queue, delays message for n milliseconds</li>
     * </ul>
     * <p>Without parameters simply represents the name of the queue to send to.</p>
     *
     * @param connectionString basically queue name with required additional info
     * @param message container message itself
     * @throws IOException conversion exception
     * @throws TimeoutException connection exception
     */
    void convertAndSend(@NotNull String connectionString, @NotNull ContainerMessage message) throws IOException, TimeoutException;

}

package com.opuscapita.peppol.commons.queue.consume;

import com.opuscapita.peppol.commons.container.ContainerMessage;
import com.opuscapita.peppol.commons.container.ContainerMessageSerializer;
import com.opuscapita.peppol.commons.eventing.jsd.JsdTicketReporter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Receives message from MQ and sends it to processing with all necessary conversions and error handling.
 */
@Component
public class CommonMessageReceiver {

    private final static Logger logger = LoggerFactory.getLogger(CommonMessageReceiver.class);

    private final JsdTicketReporter errorHandler;
    private final CommonMessageProcessor processor;
    private final ContainerMessageSerializer serializer;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    public CommonMessageReceiver(@NotNull JsdTicketReporter errorHandler,
                                 @NotNull CommonMessageProcessor processor,
                                 @NotNull ContainerMessageSerializer serializer) {
        this.processor = processor;
        this.serializer = serializer;
        this.errorHandler = errorHandler;
    }

    public synchronized void receiveMessage(@NotNull byte[] bytes) {
        logger.debug("Message received as bytes array, assuming JSON");
        receiveMessage(new String(bytes));
    }

    public synchronized void receiveMessage(@NotNull String message) {
        logger.debug("Received string message, assuming JSON");
        receiveMessage(jsonToContainerMessage(message));
    }

    public synchronized void receiveMessage(@Nullable ContainerMessage cm) {
        if (cm == null) {
            logger.warn("Container message is null, exiting");
            reportError(new IllegalArgumentException("Container message is null"), "Container message is null");
        } else {
            //logger.debug("Message received, file id: " + cm.getFileName());
            processor.process(cm);
        }
    }

    private ContainerMessage jsonToContainerMessage(@NotNull String json) {
        try {
            return serializer.fromJson(json);
        } catch (Exception e) {
            logger.warn("Failed to deserialize received message: " + e.getMessage());
            reportError(e, json);
        }
        return null;
    }

    private void reportError(@NotNull Throwable e, @Nullable String message) {
        logger.warn("Reporting error: " + message + ", exception message: " + e.getMessage());
        try {
            errorHandler.reportWithoutContainerMessage(null, null, e,
                    "Failed to deserialize received message", "Deserialization failed for message: '" + message + "'");
        } catch (Exception weird) {
            logger.error("Deserialization failed for message: '" + message + "', ERROR: " + e.getMessage());
            logger.error("Reporting to SNC failed", weird);
        }
        logger.info("Container message is unavailable, cannot report to monitoring");
        throw new AmqpRejectAndDontRequeueException("Deserialization failed", e);
    }

    public void setContainerMessageConsumer(@NotNull ContainerMessageConsumer consumer) {
        this.processor.setContainerMessageConsumer(consumer);
    }

}

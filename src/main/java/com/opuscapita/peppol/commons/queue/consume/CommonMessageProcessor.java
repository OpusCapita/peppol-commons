package com.opuscapita.peppol.commons.queue.consume;

import com.opuscapita.peppol.commons.container.ContainerMessage;
import com.opuscapita.peppol.commons.eventing.OcEventReporter;
import com.opuscapita.peppol.commons.eventing.SncTicketReporter;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Sends message to custom consumer and takes responsibility of error handling and events reporting.
 */
@Component
@SuppressWarnings("Duplicates")
@ConditionalOnProperty(name = "peppol.common.async.processing.enabled", havingValue = "false", matchIfMissing = true)
public class CommonMessageProcessor implements ContainerMessageProcessor {

    private final static Logger logger = LoggerFactory.getLogger(CommonMessageProcessor.class);

    private final OcEventReporter eventReporter;
    private final SncTicketReporter ticketReporter;

    private ContainerMessageConsumer containerMessageConsumer;

    @Autowired
    public CommonMessageProcessor(OcEventReporter eventReporter, SncTicketReporter ticketReporter) {
        this.eventReporter = eventReporter;
        this.ticketReporter = ticketReporter;
    }

    public void setContainerMessageConsumer(@NotNull ContainerMessageConsumer consumer) {
        this.containerMessageConsumer = consumer;
    }

    @Override
    public void process(@NotNull ContainerMessage cm) {
        try {
            logger.info("Processing message: " + cm.toLog());
            containerMessageConsumer.consume(cm);
        } catch (Exception e) {
            reportError(cm, e);
            throw new AmqpRejectAndDontRequeueException(e.getMessage(), e);
        }

        eventReporter.reportStatus(cm);
    }

    private void reportError(ContainerMessage cm, Throwable e) {
        try {
            String shortDescription = StringUtils.isNotBlank(e.getMessage()) ? e.getMessage() : "Incident, UnknownError";
            logger.warn("Message processing failed for " + cm.toLog() + " with error: " + e.getMessage());
            ticketReporter.reportWithContainerMessage(cm, e, shortDescription);
        } catch (Exception weird) {
            logger.error("Reporting to ServiceNow threw exception: ", weird);
        }

        try {
            cm.setProcessingException(e.getMessage());
            eventReporter.reportStatus(cm);
        } catch (Exception weird) {
            logger.error("Failed to report error using event reporter", weird);
        }
    }

}

package com.opuscapita.peppol.commons.eventing;

import com.opuscapita.peppol.commons.container.ContainerMessage;
import com.opuscapita.peppol.commons.queue.MessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OcEventReporter implements EventReporter {

    private static final Logger logger = LoggerFactory.getLogger(OcEventReporter.class);

    @Value("${peppol.eventing.queue.in.name:eventing}")
    private String reportDestination;

    private final MessageQueue messageQueue;
    private final TicketReporter ticketReporter;

    @Autowired
    public OcEventReporter(MessageQueue messageQueue, TicketReporter ticketReporter) {
        this.messageQueue = messageQueue;
        this.ticketReporter = ticketReporter;
    }

    @Override
    public void reportStatus(ContainerMessage cm) {
        try {
            messageQueue.convertAndSend(reportDestination, cm);
        } catch (Exception e) {
            failedToProcess(cm, e);
        }
    }

    private void failedToProcess(ContainerMessage cm, Throwable e) {
        String shortDescription = "OcEventReporter failed to report the status of the message: " + cm.getFileName();
        logger.warn("Reporting an issue to ServiceNow: " + shortDescription);
        try {
            ticketReporter.reportWithContainerMessage(cm, e, shortDescription);
        } catch (Exception exception) {
            logger.error("Failed to report issue to ServiceNow: ", exception);
        }
    }

}

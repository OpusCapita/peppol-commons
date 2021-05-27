package com.opuscapita.peppol.commons.eventing.jsd;

import com.opuscapita.peppol.commons.container.ContainerMessage;
import com.opuscapita.peppol.commons.eventing.jsd.Jsd;
import com.opuscapita.peppol.commons.eventing.jsd.JsdEntity;
import com.opuscapita.peppol.commons.eventing.TicketReporter;
import com.opuscapita.peppol.commons.eventing.TicketContentFormatter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JsdTicketReporter implements TicketReporter {

    private static final Logger logger = LoggerFactory.getLogger(JsdTicketReporter.class);

    private final Jsd client;

    @Autowired
    public JsdTicketReporter(Jsd client) {
        this.client = client;
    }

    @Override
    public void reportWithContainerMessage(ContainerMessage cm, Throwable e, String shortDescription) {
        reportWithContainerMessage(cm, e, shortDescription, null);
    }

    @Override
    public void reportWithContainerMessage(ContainerMessage cm, Throwable e, String shortDescription, String additionalDetails) {
        createTicketFromContainerMessage(cm, e, shortDescription, additionalDetails);
    }

    @Override
    public void reportWithoutContainerMessage(String customerId, String fileName, Throwable e, String shortDescription) {
        reportWithoutContainerMessage(customerId, fileName, e, shortDescription, null);
    }

    @Override
    public void reportWithoutContainerMessage(String customerId, String fileName, Throwable e, String shortDescription, String additionalDetails) {
        createTicketWithoutContainerMessage(customerId, fileName, e, shortDescription, additionalDetails);
    }

    private void createTicketFromContainerMessage(ContainerMessage cm, Throwable e, String shortDescription, String additionalDetails) {
        String detailedDescription = TicketContentFormatter.getErrorDescription(cm, e, additionalDetails);
        createTicket(shortDescription, detailedDescription, cm.getCustomerId(), cm.getFileName());
    }

    private void createTicketWithoutContainerMessage(String customerId, String fileName, Throwable e, String shortDescription, String additionalDetails) {
        fileName = StringUtils.isBlank(fileName) ? "N/A" : fileName;
        customerId = StringUtils.isBlank(customerId) ? "N/A" : customerId;
        String detailedDescription = TicketContentFormatter.getErrorDescription(customerId, fileName, e, additionalDetails);
        createTicket(shortDescription, detailedDescription, customerId, fileName);
    }


    private void createTicket(String shortDescription, String detailedDescription, String customerId, String fileName) {
        try {
            JsdEntity ticket = new JsdEntity(shortDescription, detailedDescription, customerId);
            client.insert(ticket);

            logger.info("Jsd ticket created for " + fileName + " about " + shortDescription);
        } catch (Exception e) {
            logger.error("Failed to create JSD ticket for customer: " + customerId + ", file: " + fileName +
                    " about " + shortDescription + " with data: " + detailedDescription, e);
        }
    }
}

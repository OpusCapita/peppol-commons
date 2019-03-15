package com.opuscapita.peppol.commons.eventing;

import com.opuscapita.peppol.commons.container.ContainerMessage;
import com.opuscapita.peppol.commons.eventing.servicenow.ServiceNow;
import com.opuscapita.peppol.commons.eventing.servicenow.SncEntity;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class SncTicketReporter implements TicketReporter {

    private static final Logger logger = LoggerFactory.getLogger(SncTicketReporter.class);

    private final ServiceNow client;

    @Autowired
    public SncTicketReporter(ServiceNow client) {
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
            SncEntity ticket = new SncEntity(shortDescription, detailedDescription, customerId);
            client.insert(ticket);

            logger.info("ServiceNow ticket created for " + fileName + " about " + shortDescription);
        } catch (Exception e) {
            logger.error("Failed to create SNC ticket for customer: " + customerId + ", file: " + fileName +
                    " about " + shortDescription + " with data: " + detailedDescription, e);
        }
    }

}

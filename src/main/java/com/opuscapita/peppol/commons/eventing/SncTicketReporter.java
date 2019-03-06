package com.opuscapita.peppol.commons.eventing;

import com.opuscapita.peppol.commons.container.ContainerMessage;
import com.opuscapita.peppol.commons.container.ContainerMessageSerializer;
import com.opuscapita.peppol.commons.eventing.servicenow.ServiceNow;
import com.opuscapita.peppol.commons.eventing.servicenow.SncEntity;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Primary
@Component
public class SncTicketReporter implements TicketReporter {

    private static final Logger logger = LoggerFactory.getLogger(SncTicketReporter.class);

    private final ServiceNow client;
    private final ContainerMessageSerializer serializer;

    @Autowired
    public SncTicketReporter(ServiceNow client, ContainerMessageSerializer serializer) {
        this.client = client;
        this.serializer = serializer;
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
    public void reportWithoutContainerMessage(String customerId, Throwable e, String shortDescription, String correlationId, String fileName) {
        reportWithoutContainerMessage(customerId, e, shortDescription, correlationId, fileName, null);
    }

    @Override
    public void reportWithoutContainerMessage(String customerId, Throwable e, String shortDescription, String correlationId, String fileName, String additionalDetails) {
        createTicketWithoutContainerMessage(customerId, e, fileName, shortDescription, correlationId, additionalDetails);
    }

    private void createTicketFromContainerMessage(ContainerMessage cm, Throwable e, String shortDescription, String additionalDetails) {
        String correlationId = cm.getMetadata().getTransmissionId() + cm.getCurrentStatus();
        String detailedDescription = TicketContentFormatter.getErrorDescription(cm, e, additionalDetails, serializer);

        createTicket(shortDescription, detailedDescription, correlationId, cm.getCustomerId(), cm.getFileName());
    }

    private void createTicketWithoutContainerMessage(String customerId, Throwable e, String fileName, String shortDescription, String correlationId, String additionalDetails) {
        fileName = fileName == null ? "N/A" : fileName;
        String exceptionMessage = TicketContentFormatter.exceptionMessageToString(e);
        String detailedDescription = TicketContentFormatter.getErrorDescription(customerId, e, fileName, additionalDetails);

        if (StringUtils.isBlank(correlationId)) {
            correlationId = fileName;
            if (StringUtils.isNotBlank(exceptionMessage)) {
                correlationId += exceptionMessage;
            }
        }

        createTicket(shortDescription, detailedDescription, correlationIdDigest(correlationId), customerId, fileName);
    }


    private void createTicket(String shortDescription, String detailedDescription, String correlationId, String customerId, String fileName) {
        if (StringUtils.isBlank(customerId)) {
            customerId = "N/A";
        }

        try {
            SncEntity ticket = new SncEntity(shortDescription, detailedDescription, correlationIdDigest(correlationId), customerId, 0);
            client.insert(ticket);

            logger.info("ServiceNow ticket created for " + fileName + " about " + shortDescription);
        } catch (Exception e) {
            logger.error("Failed to create SNC ticket for customer: " + customerId + ", file: " + fileName +
                    " about " + shortDescription + " with data: " + detailedDescription, e);
        }
    }

    private String correlationIdDigest(String correlationId) {
        try {
            correlationId = Hex.encodeHexString(MessageDigest.getInstance("SHA-1").digest(correlationId.getBytes()));
        } catch (NoSuchAlgorithmException e1) {
            logger.error("Failed to create SHA-1 has of correlation id");
            e1.printStackTrace();
        }
        return correlationId;
    }
}
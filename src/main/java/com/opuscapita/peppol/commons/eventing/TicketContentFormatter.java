package com.opuscapita.peppol.commons.eventing;

import com.opuscapita.peppol.commons.container.ContainerMessage;
import com.opuscapita.peppol.commons.container.ContainerMessageSerializer;
import com.opuscapita.peppol.commons.container.document.DocumentError;
import com.opuscapita.peppol.commons.container.document.DocumentWarning;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.stream.Collectors;

/**
 * Creates informational message out of container message for failed documents.
 */
public class TicketContentFormatter {

    static String getErrorDescription(ContainerMessage cm, Throwable e, String additionalDetails, ContainerMessageSerializer serializer) {
        String detailedDescription = "Failed to process message";

        detailedDescription += "\nFile name: " + cm.getFileName();

        if (StringUtils.isNotBlank(cm.getCustomerId())) {
            detailedDescription += "\nCustomerID: " + cm.getCustomerId();
        }

        if (e != null && StringUtils.isNotBlank(e.getMessage())) {
            detailedDescription += "\nError message: " + e.getMessage();
        }

        if (additionalDetails != null) {
            detailedDescription += "\nDetails: " + additionalDetails;
        }

        if (cm.getDocumentInfo() != null && cm.getDocumentInfo().getWarnings().size() > 0) {
            detailedDescription += "\nDocument warnings: " +
                    cm.getDocumentInfo().getWarnings().stream().map(DocumentWarning::toString).collect(Collectors.joining("\n\t"));
        }
        if (cm.getDocumentInfo() != null && cm.getDocumentInfo().getErrors().size() > 0) {
            detailedDescription += "\nDocument errors: " +
                    cm.getDocumentInfo().getErrors().stream().map(DocumentError::toString).collect(Collectors.joining("\n\t"));
        }

        detailedDescription += "\nLast processing status: " + cm.getCurrentStatus();

        String processingException = cm.getProcessingException();
        if (StringUtils.isNotBlank(processingException)) {
            detailedDescription += "\nProcessing exception message: " + processingException;
        }

        String exceptionMessage = exceptionMessageToString(e);
        if (exceptionMessage != null) {
            detailedDescription += "\nPlatform exception message: " + exceptionMessage;
        }

        String json = serializer.toJson(cm).replaceAll("\\{|\\}|\\\"|\\'", "");
        detailedDescription += "\nMessage content: \n" + json + "\n";

        if (e != null) {
            detailedDescription += "\n\nPlatform exception: " + ExceptionUtils.getStackTrace(e) + "\n";
        }

        return detailedDescription;
    }

    static String getErrorDescription(String customerId, Throwable e, String fileName, String additionalDetails) {
        String detailedDescription = "Failed to process message";

        if (fileName != null) {
            detailedDescription += "\nFile name: " + fileName;
        }

        if (StringUtils.isNotBlank(customerId)) {
            detailedDescription += "\nCustomerID: " + customerId;
        }

        if (e != null && StringUtils.isNotBlank(e.getMessage())) {
            detailedDescription += "\nError message: " + e.getMessage();
        }

        String exceptionMessage = exceptionMessageToString(e);
        if (exceptionMessage != null) {
            detailedDescription += "\nPlatform exception message: " + exceptionMessage;
        }

        if (e != null) {
            detailedDescription += "\n\nPlatform exception: " + ExceptionUtils.getStackTrace(e) + "\n";
        }

        if (additionalDetails != null) {
            detailedDescription += "\n\nAdditional details: " + additionalDetails + "\n";
        }

        return detailedDescription;
    }

    static String exceptionMessageToString(Throwable e) {
        if (e == null) {
            return null;
        }
        if (StringUtils.isBlank(e.getMessage())) {
            return null;
        }
        return e.getMessage();
    }
}

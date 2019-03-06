package com.opuscapita.peppol.commons.container;

import com.google.gson.annotations.Since;
import com.opuscapita.peppol.commons.container.metadata.DocumentInfo;
import com.opuscapita.peppol.commons.container.state.Endpoint;
import com.opuscapita.peppol.commons.container.state.Route;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class ContainerMessage implements Serializable {

    private static final long serialVersionUID = -5450780856722626102L;

    @Since(1.0) private String fileName;
    @Since(1.0) private String originalFileName = "";

    @Since(1.0) private Route route;
    @Since(1.0) private String currentStatus;
    @Since(1.0) private Endpoint currentEndpoint;
    @Since(1.0) private Endpoint sourceEndpoint;

    @Since(1.0) private String processingException;
    @Since(1.0) private PeppolMessageMetadata metadata;

    @Since(1.0) private DocumentInfo documentInfo;

    public ContainerMessage() {
    }

    public ContainerMessage(@NotNull String fileName, @NotNull Endpoint source) {
        this.setFileName(fileName);
        this.setSourceEndpoint(source);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOriginalFileName() {
        if (StringUtils.isBlank(originalFileName)) {
            return FilenameUtils.getBaseName(getFileName());
        }
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = FilenameUtils.getBaseName(originalFileName);
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public String popRoute() {
        if (getRoute() == null) {
            return null;
        }
        return getRoute().pop();
    }

    public Endpoint getSourceEndpoint() {
        return sourceEndpoint;
    }

    public void setSourceEndpoint(Endpoint sourceEndpoint) {
        this.sourceEndpoint = sourceEndpoint;
    }

    public boolean isInbound() {
        return getSourceEndpoint().isInbound();
    }

    public boolean isOutbound() {
        return !this.isInbound();
    }

    public void setStatus(Endpoint endpoint, String status) {
        this.setCurrentEndpoint(endpoint);
        this.setCurrentStatus(status);
    }

    public Endpoint getCurrentEndpoint() {
        return currentEndpoint;
    }

    public void setCurrentEndpoint(Endpoint currentEndpoint) {
        this.currentEndpoint = currentEndpoint;
    }

    public String getCurrentStatus() {
        if (StringUtils.isBlank(currentStatus)) {
            return "unknown";
        }
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getProcessingException() {
        return processingException;
    }

    public void setProcessingException(String processingException) {
        this.processingException = processingException;
    }

    public PeppolMessageMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(PeppolMessageMetadata metadata) {
        this.metadata = metadata;
    }

    public String getCustomerId() {
        if (getMetadata() == null) {
            return null;
        }
        return isInbound() ? getMetadata().getRecipientId() : getMetadata().getSenderId();
    }

    public ApInfo getApInfo() {
        if (metadata == null) {
            return null;
        }
        return ApInfo.parseFromCommonName(isInbound() ? metadata.getSendingAccessPoint() : metadata.getReceivingAccessPoint());
    }

    public DocumentInfo getDocumentInfo() {
        return documentInfo;
    }

    public void setDocumentInfo(DocumentInfo documentInfo) {
        this.documentInfo = documentInfo;
    }

    public String toLog() {
        String result = "[file: {filename}, customer: {customer}, status: {status}, endpoint: {endpoint}]";
        result = result.replace("{filename}", "{" + getFileName() + "}");

        String customerId = getCustomerId();
        result = result.replace("{customer}", "{" + (StringUtils.isBlank(customerId) ? "unknown" : customerId) + "}");
        result = result.replace("{status}", "{" + (StringUtils.isBlank(getCurrentStatus()) ? "unknown" : getCurrentStatus()) + "}");
        result = result.replace("{endpoint}", "{" + (getCurrentEndpoint() == null ? "unknown" : getCurrentEndpoint().getType()) + "}");

        return result;
    }
}

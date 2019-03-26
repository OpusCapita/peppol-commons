package com.opuscapita.peppol.commons.container;

import com.google.gson.annotations.Since;
import com.opuscapita.peppol.commons.container.metadata.AccessPointInfo;
import com.opuscapita.peppol.commons.container.metadata.PeppolMessageMetadata;
import com.opuscapita.peppol.commons.container.state.Endpoint;
import com.opuscapita.peppol.commons.container.state.ProcessStep;
import com.opuscapita.peppol.commons.container.state.Route;
import com.opuscapita.peppol.commons.container.state.log.DocumentLog;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

public class ContainerMessage implements Serializable {

    private static final long serialVersionUID = -5450780856722626102L;

    @Since(1.0) private String fileName;
    @Since(1.0) private String originalFileName = "";

    @Since(1.0) private Route route;
    @Since(1.0) private PeppolMessageMetadata metadata;
    @Since(1.0) private ContainerMessageHistory history;

    public ContainerMessage() {
        this.history = new ContainerMessageHistory(Endpoint.TEST);
    }

    public ContainerMessage(String fileName, Endpoint endpoint) {
        this.fileName = fileName;
        this.history = new ContainerMessageHistory(endpoint);
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

    public ContainerMessageHistory getHistory() {
        return history;
    }

    public Endpoint getEndpoint() {
        return history.getEndpoint();
    }

    public boolean isInbound() {
        return getEndpoint().isInbound();
    }

    public boolean isOutbound() {
        return !this.isInbound();
    }

    public void setStep(ProcessStep step) {
        this.getEndpoint().setStep(step);
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

    public AccessPointInfo getApInfo() {
        if (metadata == null) {
            return null;
        }
        return AccessPointInfo.parseFromCommonName(isInbound() ? metadata.getSendingAccessPoint() : metadata.getReceivingAccessPoint());
    }

    public String toKibana() {
        String result = "[file: {filename}, status: {status}, endpoint: {endpoint}]";
        result = result.replace("{filename}", "{" + getFileName() + "}");

        DocumentLog log = history.getLastInfoLog();
        result = result.replace("{status}", "{" + (log == null ? "unknown" : log.getMessage()) + "}");
        Endpoint endpoint = getEndpoint();
        result = result.replace("{endpoint}", "{" + (endpoint == null ? "unknown" : endpoint.getStepWithFlow()) + "}");

        return result;
    }

}

package com.opuscapita.peppol.commons.container;

import com.google.gson.annotations.Since;
import com.opuscapita.peppol.commons.container.metadata.AccessPointInfo;
import com.opuscapita.peppol.commons.container.metadata.ContainerMessageMetadata;
import com.opuscapita.peppol.commons.container.state.ProcessFlow;
import com.opuscapita.peppol.commons.container.state.ProcessStep;
import com.opuscapita.peppol.commons.container.state.Route;
import com.opuscapita.peppol.commons.container.state.Source;

import java.io.Serializable;

public class ContainerMessage implements Serializable {

    private static final long serialVersionUID = -5450780856722626102L;

    @Since(1.0) private String fileName;

    @Since(1.0) private Route route;
    @Since(1.0) private Source source;
    @Since(1.0) private ContainerMessageHistory history;
    @Since(1.0) private ContainerMessageMetadata metadata;

    public ContainerMessage(String fileName) {
        this(fileName, Source.UNKNOWN);
    }

    public ContainerMessage(String fileName, Source source) {
        this(fileName, source, ProcessStep.UNKNOWN);
    }

    public ContainerMessage(String fileName, Source source, ProcessStep step) {
        this.fileName = fileName;
        this.source = source;
        this.history = new ContainerMessageHistory(step);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public ProcessStep getStep() {
        return getHistory().getStep();
    }

    public void setStep(ProcessStep step) {
        this.history.setStep(step);
    }

    public ProcessFlow getFlow() {
        return Source.NETWORK.equals(source) ? ProcessFlow.IN : ProcessFlow.OUT;
    }

    public String getStepWithFlow() {
        return getFlow().name() + "_" + getStep().name();
    }

    public boolean isInbound() {
        return Source.NETWORK.equals(source);
    }

    public boolean isOutbound() {
        return !this.isInbound();
    }

    public ContainerMessageHistory getHistory() {
        return history;
    }

    public ContainerMessageMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(ContainerMessageMetadata metadata) {
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
        String result = "[file: {filename}, messageId: {messageId}, transmissionId: {transmissionId}]";
        result = result.replace("{filename}", "{" + getFileName() + "}");
        result = result.replace("{messageId}", "{" + (metadata == null ? "-" : metadata.getMessageId()) + "}");
        result = result.replace("{transmissionId}", "{" + (metadata == null ? "-" : metadata.getTransmissionId()) + "}");
        return result;
    }

}

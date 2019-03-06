package com.opuscapita.peppol.commons.container.metadata;

import com.google.gson.annotations.Since;
import com.opuscapita.peppol.commons.container.PeppolMessageMetadata;
import com.opuscapita.peppol.commons.container.ApInfo;
import com.opuscapita.peppol.commons.container.state.Endpoint;
import com.opuscapita.peppol.commons.container.state.Route;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public class ProcessingInfo implements Serializable {

    private static final long serialVersionUID = -556566093311452295L;

    @Since(1.0) private Endpoint source;
    @Since(1.0) private Route route;
    @Since(1.0) private Endpoint currentEndpoint;
    @Since(1.0) private String currentStatus;
    @Since(1.0) private String processingException;
    @Since(1.0) private String originalSource;
    @Since(1.0) private PeppolMessageMetadata peppolMessageMetadata;

    public ProcessingInfo(@NotNull Endpoint source) {
        this.source = source;
    }

    @Nullable
    public Route getRoute() {
        return route;
    }

    public ProcessingInfo setRoute(@Nullable Route route) {
        this.route = route;
        return this;
    }

    public boolean isInbound() {
        return source.isInbound();
    }

    public Endpoint getSource() {
        return source;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    @SuppressWarnings("UnusedReturnValue")
    public ProcessingInfo setCurrentStatus(@NotNull Endpoint endpoint, @NotNull String status) {
        this.currentEndpoint = endpoint;
        this.currentStatus = status;
        return this;
    }

    @Nullable
    public String getProcessingException() {
        return processingException;
    }

    public void setProcessingException(@Nullable String processingException) {
        this.processingException = processingException;
    }

    public String getTransactionId() {
        PeppolMessageMetadata metadata = getPeppolMessageMetadata();
        if (metadata == null) {
            return null;
        }
        return metadata.getTransmissionId();
    }

    public Endpoint getCurrentEndpoint() {
        return currentEndpoint == null ? source : currentEndpoint;
    }

    public ApInfo getApInfo() {
        PeppolMessageMetadata metadata = getPeppolMessageMetadata();
        if (metadata == null) {
            return null;
        }
        return ApInfo.parseFromCommonName(isInbound() ? metadata.getSendingAccessPoint() : metadata.getReceivingAccessPoint());
    }

    @NotNull
    public String getOriginalSource() {
        if (StringUtils.isBlank(originalSource)) {
            return source.getName();
        }
        return originalSource;
    }

    public void setOriginalSource(String originalSource) {
        this.originalSource = originalSource;
    }

    public PeppolMessageMetadata getPeppolMessageMetadata() {
        return peppolMessageMetadata;
    }

    public void setPeppolMessageMetadata(PeppolMessageMetadata peppolMessageMetadata) {
        this.peppolMessageMetadata = peppolMessageMetadata;
    }
}

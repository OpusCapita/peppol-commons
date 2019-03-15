package com.opuscapita.peppol.commons.container;

import com.google.gson.annotations.Since;
import com.opuscapita.peppol.commons.container.state.Endpoint;
import com.opuscapita.peppol.commons.container.state.log.DocumentErrorType;
import com.opuscapita.peppol.commons.container.state.log.DocumentLog;
import com.opuscapita.peppol.commons.container.state.log.DocumentLogLevel;
import com.opuscapita.peppol.commons.container.state.log.DocumentValidationError;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ContainerMessageHistory implements Serializable {

    private static final long serialVersionUID = -9016219790420895460L;

    @Since(1.0)
    private Endpoint endpoint;
    @Since(1.0)
    private List<DocumentLog> logs;

    ContainerMessageHistory(Endpoint endpoint) {
        this.endpoint = endpoint;
        this.logs = new ArrayList<>();
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public List<DocumentLog> getLogs() {
        return logs;
    }

    public void addLog(DocumentLog log) {
        log.setSource(endpoint.getStep());
        this.logs.add(log);
    }

    public void addInfo(String message) {
        this.addLog(new DocumentLog(message, DocumentLogLevel.INFO));
    }

    public void addWarning(String message) {
        this.addLog(new DocumentLog(message, DocumentLogLevel.WARNING));
    }

    public void addError(String message) {
        this.addLog(new DocumentLog(message, DocumentLogLevel.ERROR, DocumentErrorType.PROCESSING_ERROR));
    }

    public void addValidationError(String message) {
        this.addLog(new DocumentLog(message, DocumentLogLevel.ERROR, DocumentErrorType.VALIDATION_ERROR));
    }

    public void addValidationError(DocumentValidationError error) {
        DocumentLog log = new DocumentLog(error.toString(), DocumentLogLevel.ERROR, DocumentErrorType.VALIDATION_ERROR);
        log.setValidationError(error);
        this.addLog(log);
    }

    public void addValidationWarning(DocumentValidationError error) {
        DocumentLog log = new DocumentLog(error.toString(), DocumentLogLevel.WARNING, DocumentErrorType.VALIDATION_ERROR);
        log.setValidationError(error);
        this.addLog(log);
    }

    public boolean hasError() {
        return this.logs.stream().anyMatch(log -> DocumentLogLevel.ERROR.equals(log.getLevel()));
    }

    public DocumentLog getLastLog() {
        if (logs.isEmpty()) {
            return null;
        }
        return logs.get(logs.size() - 1);
    }

    public DocumentLog getLastLog(DocumentLogLevel level) {
        if (logs.isEmpty()) {
            return null;
        }

        for (int i = logs.size() - 1; i >= 0; i--) {
            DocumentLog log = logs.get(i);
            if (level.equals(log.getLevel())) {
                return log;
            }
        }
        return null;
    }

    public DocumentLog getLastInfoLog() {
        return getLastLog(DocumentLogLevel.INFO);
    }

    public DocumentLog getLastErrorLog() {
        return getLastLog(DocumentLogLevel.ERROR);
    }

}

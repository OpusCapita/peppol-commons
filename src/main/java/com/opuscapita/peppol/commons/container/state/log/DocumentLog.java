package com.opuscapita.peppol.commons.container.state.log;

import com.google.gson.annotations.Since;
import com.opuscapita.peppol.commons.container.state.ProcessStep;

import java.io.Serializable;

public class DocumentLog implements Serializable {

    private static final long serialVersionUID = -4776734668982849414L;

    @Since(1.0)
    private String message;
    @Since(1.0)
    private DocumentLogLevel level;
    @Since(1.0)
    private DocumentErrorType errorType;
    @Since(1.0)
    private DocumentValidationError validationError;

    @Since(1.0)
    private ProcessStep source;

    public DocumentLog(String message, DocumentLogLevel level) {
        this.message = message;
    }

    public DocumentLog(String message, DocumentLogLevel level, DocumentErrorType errorType) {
        this(message, level);
        this.errorType = errorType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DocumentLogLevel getLevel() {
        return level;
    }

    public void setLevel(DocumentLogLevel level) {
        this.level = level;
    }

    public DocumentErrorType getErrorType() {
        return errorType;
    }

    public void setErrorType(DocumentErrorType errorType) {
        this.errorType = errorType;
    }

    public ProcessStep getSource() {
        return source;
    }

    public void setSource(ProcessStep source) {
        this.source = source;
    }


    public DocumentValidationError getValidationError() {
        return validationError;
    }

    public void setValidationError(DocumentValidationError validationError) {
        this.validationError = validationError;
    }

    @Override
    public String toString() {
        String type = DocumentLogLevel.ERROR.equals(level) ? errorType.name() : level.name();
        return String.format("%s from %s: %s", type, source, message);
    }
}

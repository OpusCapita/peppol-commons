package com.opuscapita.peppol.commons.validation;

import com.opuscapita.peppol.commons.container.ContainerMessage;
import com.opuscapita.peppol.commons.container.document.DocumentError;
import com.opuscapita.peppol.commons.container.document.DocumentWarning;
import com.opuscapita.peppol.commons.container.state.Endpoint;
import com.opuscapita.peppol.commons.container.state.ProcessType;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ValidationResult implements Serializable {

    private static final long serialVersionUID = 7905183684035939619L;

    private List<DocumentError> errors = new ArrayList<>();
    private List<DocumentWarning> warnings = new ArrayList<>();

    @NotNull
    public static ValidationResult fromContainerMessage(@NotNull ContainerMessage cm) {
        Endpoint endpoint = cm.getCurrentEndpoint() == null ?
                new Endpoint("unknown", ProcessType.UNKNOWN) : cm.getCurrentEndpoint();

        ValidationResult result = new ValidationResult();
        if (cm.getDocumentInfo() == null) {
            result.errors.add(new DocumentError(endpoint, "No document information in message"));
        } else {
            result.errors.addAll(cm.getDocumentInfo().getErrors());
            result.warnings.addAll(cm.getDocumentInfo().getWarnings());
        }

        return result;
    }

    public boolean isPassed() {
        return errors.isEmpty();
    }

    public void addError(DocumentError error) {
        errors.add(error);
    }

    public void addWarning(DocumentWarning documentWarning) {
        warnings.add(documentWarning);
    }

    public List<DocumentError> getErrors() {
        return errors;
    }

    public List<DocumentWarning> getWarnings() {
        return warnings;
    }

    public @NotNull
    String getErrorsString() {
        return errors.stream()
                .map(DocumentError::toString)
                .collect(Collectors.joining("; "));
    }
}

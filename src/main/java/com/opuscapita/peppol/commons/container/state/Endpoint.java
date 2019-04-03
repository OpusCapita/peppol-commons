package com.opuscapita.peppol.commons.container.state;

import com.google.gson.annotations.Since;

import java.io.Serializable;

/**
 * Single endpoint of the module/process/whatever in a whole route.
 * Each endpoint has own name (service name) and type.
 */
public class Endpoint implements Serializable {

    private static final long serialVersionUID = -8305356325208895381L;

    public static final Endpoint TEST = new Endpoint(Source.UNKNOWN, ProcessStep.TEST);

    @Since(1.0) private final Source source;
    @Since(1.0) private ProcessStep step;

    public Endpoint(Source source) {
        this(source, ProcessStep.UNKNOWN);
    }

    public Endpoint(Source source, ProcessStep step) {
        this.step = step;
        this.source = source;
    }

    public Source getSource() {
        return source;
    }

    public ProcessFlow getFlow() {
        return Source.NETWORK.equals(source) ? ProcessFlow.IN : ProcessFlow.OUT;
    }

    public ProcessStep getStep() {
        return step;
    }

    public String getStepWithFlow() {
        return getFlow().name() + "_" + step.name();
    }

    public void setStep(ProcessStep step) {
        this.step = step;
    }

    public boolean isInbound() {
        return Source.NETWORK.equals(source);
    }

    public boolean isTerminal() {
        return ProcessStep.NETWORK.equals(step);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Endpoint)) return false;

        Endpoint endpoint = (Endpoint) o;
        return source.equals(endpoint.source) && step.equals(endpoint.step);
    }

    @Override
    public int hashCode() {
        int result = source.hashCode();
        result = 31 * result + step.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Service " + step + " (source:" + source + ")";
    }

}

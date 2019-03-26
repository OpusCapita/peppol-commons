package com.opuscapita.peppol.commons.container.state;

import com.google.gson.annotations.Since;

import java.io.Serializable;

/**
 * Single endpoint of the module/process/whatever in a whole route.
 * Each endpoint has own name (service name) and type.
 */
public class Endpoint implements Serializable {

    private static final long serialVersionUID = -8305356325208895381L;

    public static final Endpoint TEST = new Endpoint(Source.UNKNOWN, ProcessFlow.IN, ProcessStep.TEST);

    @Since(1.0) private final Source source;
    @Since(1.0) private final ProcessFlow flow;
    @Since(1.0) private ProcessStep step;

    public Endpoint(Source source, ProcessFlow flow) {
        this(source, flow, ProcessStep.UNKNOWN);
    }

    public Endpoint(Source source, ProcessFlow flow, ProcessStep step) {
        this.flow = flow;
        this.source = source;
        this.step = step;
    }

    public Source getSource() {
        return source;
    }

    public ProcessFlow getFlow() {
        return flow;
    }

    public ProcessStep getStep() {
        return step;
    }

    public String getStepWithFlow() {
        return flow.name() + "_" + step.name();
    }

    public void setStep(ProcessStep step) {
        this.step = step;
    }

    public boolean isInbound() {
        return ProcessFlow.IN.equals(flow);
    }

    public boolean isTerminal() {
        return ProcessStep.OUTBOUND.equals(step) || ProcessStep.MQ_TO_FILE.equals(step);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Endpoint)) return false;

        Endpoint endpoint = (Endpoint) o;
        return source.equals(endpoint.source) && step.equals(endpoint.step) && flow.equals(endpoint.flow);
    }

    @Override
    public int hashCode() {
        int result = source.hashCode();
        result = 31 * result + step.hashCode();
        result = 31 * result + flow.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Service " + step + " (direction:" + flow + ")";
    }

}

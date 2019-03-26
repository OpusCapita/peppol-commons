package com.opuscapita.peppol.commons.container.state;

import com.google.gson.annotations.Since;

import java.io.Serializable;

public class Route implements Serializable {

    private static final long serialVersionUID = -9123055794300438134L;

    @Since(1.0) private String mask;
    @Since(1.0) private String source;
    @Since(1.0) private String destination;
    @Since(1.0) private String description;
    @Since(1.0) private int retry = 0;
    @Since(1.0) private int delay = 0;
    @Since(1.0) private int current = 0;

    public Route() {
    }

    public Route(Route other) {
        this.destination = other.getDestination();
        this.description = other.getDescription();
        this.mask = other.getMask();
        this.source = other.getSource();
        this.retry = other.getRetry();
        this.delay = other.getDelay();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int incrementAndGetCurrent() {
        return ++current;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(description);
        if (mask != null) {
            result.append(" (").append(mask).append(") ");
        }
        result.append("[ ").append(source).append(" ");
        result.append(destination);
        return result + "]";
    }

}

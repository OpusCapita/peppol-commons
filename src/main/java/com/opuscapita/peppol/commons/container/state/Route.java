package com.opuscapita.peppol.commons.container.state;

import com.google.gson.annotations.Since;

import java.io.Serializable;

public class Route implements Serializable {

    private static final long serialVersionUID = -9123055794300438134L;

    @Since(1.1) private Source destination;
    @Since(1.0) private int retry = 0;
    @Since(1.0) private int delay = 0;
    @Since(1.0) private int current = -1;

    public Route() {
    }

    public Route(Source destination) {
        this.destination = destination;
    }

    public void initiate(int retry, int delay) {
        this.retry = retry;
        this.delay = delay;
        this.current = 0;
    }

    public Source getDestination() {
        return destination;
    }

    public void setDestination(Source destination) {
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

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int incrementAndGetCurrent() {
        return ++current;
    }

    @Override
    public String toString() {
        return destination + " [" + current + "]";
    }

}

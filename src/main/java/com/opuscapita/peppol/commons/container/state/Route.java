package com.opuscapita.peppol.commons.container.state;

import com.google.gson.annotations.Since;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Route implements Serializable {

    private static final long serialVersionUID = -9123055794300438134L;

    @Since(1.0) private String description;
    @Since(1.0) private String mask;
    @Since(1.0) private String source;
    @Since(1.0) private int current = 0;
    @Since(1.0) private List<String> endpoints = new ArrayList<>();

    public Route() {
    }

    public Route(Route other) {
        this.endpoints = other.getEndpoints();
        this.description = other.getDescription();
        this.mask = other.getMask();
        this.source = other.getSource();
    }

    public String pop() {
        if (current >= endpoints.size()) {
            return null;
        }
        return endpoints.get(current++);
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

    public List<String> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(List<String> endpoints) {
        this.endpoints = endpoints;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(description);
        if (mask != null) {
            result.append(" (").append(mask).append(") ");
        }
        result.append("[ ").append(source).append(" ");
        for (String endpoint : endpoints) {
            result.append(endpoint).append(" ");
        }
        return result + "]";
    }

}

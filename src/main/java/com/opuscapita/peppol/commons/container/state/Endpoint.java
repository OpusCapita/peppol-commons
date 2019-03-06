package com.opuscapita.peppol.commons.container.state;

import com.google.gson.annotations.Since;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Single endpoint of the module/process/whatever in a whole route.
 * Each endpoint has own name (service name) and type.
 */
public class Endpoint implements Serializable {

    public static final Endpoint TEST = new Endpoint("test", ProcessType.TEST);

    @Since(1.0) private final String name;
    @Since(1.0) private final ProcessType type;

    public Endpoint(@NotNull String name, @NotNull ProcessType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public ProcessType getType() {
        return type;
    }

    public boolean isInbound() {
        return type.toString().startsWith("IN_");
    }

    public boolean isTerminal() {
        switch (type) {
            case OUT_OUTBOUND:
            case IN_MQ_TO_FILE:
                return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Endpoint)) return false;

        Endpoint endpoint = (Endpoint) o;

        return name.equals(endpoint.name) && type == endpoint.type;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Service " + name + " (type:" + type + ")";
    }
}

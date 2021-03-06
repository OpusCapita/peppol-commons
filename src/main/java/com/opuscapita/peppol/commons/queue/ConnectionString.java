package com.opuscapita.peppol.commons.queue;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Splits connection string to parts if necessary.
 * More information on connection string format here: {@link MessageQueue}
 */
public class ConnectionString {

    public static final String QUEUE_SEPARATOR = ":";
    public static final String VALUE_SEPARATOR = "=";
    public static final String EXCHANGE = "exchange";
    private static final String PARAMETER_SEPARATOR = ",";
    private static final String DELAY = "x-delay";

    private final String it;

    ConnectionString(@NotNull String connectionString) {
        if (StringUtils.isBlank(connectionString)) {
            throw new IllegalArgumentException("Connection string must not be empty");
        }
        this.it = connectionString;
    }

    @NotNull
    public String getQueue() {
        if (it.contains(QUEUE_SEPARATOR)) {
            return it.substring(0, it.indexOf(":"));
        }
        return it;
    }

    @Nullable
    public String getExchange() {
        return getValue(EXCHANGE);
    }

    int getXDelay() {
        String value = getValue(DELAY);
        return value == null ? 0 : Integer.parseInt(value);
    }

    @Nullable
    private String rest() {
        if (it.contains(QUEUE_SEPARATOR) && !(it.indexOf(QUEUE_SEPARATOR) == it.length())) {
            return it.substring(it.indexOf(QUEUE_SEPARATOR) + 1);
        }
        return null;
    }

    @Nullable
    private String getValue(@NotNull String name) {
        String rest = rest();
        if (rest == null) {
            return null;
        }

        String[] params = StringUtils.split(rest, PARAMETER_SEPARATOR);
        for (String part : params) {
            String[] parts = StringUtils.split(part, VALUE_SEPARATOR);
            if (name.equals(parts[0])) {
                if (parts.length == 1) {
                    return "";
                }
                return parts[1];
            }
        }

        return null;
    }
}

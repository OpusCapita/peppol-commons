package com.opuscapita.peppol.commons.storage;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class EmptyFileException extends IOException {

    EmptyFileException(@NotNull String message) {
        super(message);
    }

}

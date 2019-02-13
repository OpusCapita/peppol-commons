package com.opuscapita.peppol.commons.validation;

import com.opuscapita.peppol.commons.container.ContainerMessage;
import org.jetbrains.annotations.NotNull;

public interface BasicValidator {

    @NotNull
    ContainerMessage validate(@NotNull ContainerMessage cm, @NotNull byte[] data);

}

package com.opuscapita.peppol.commons.container.metadata;

import no.difi.oxalis.api.lang.OxalisContentException;
import no.difi.vefa.peppol.common.model.Header;
import no.difi.vefa.peppol.sbdh.SbdReader;
import no.difi.vefa.peppol.sbdh.lang.SbdhException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class MetadataExtractor {

    private static final Logger logger = LoggerFactory.getLogger(MetadataExtractor.class);

    private final DocumentHeaderParser headerParser;

    @Autowired
    public MetadataExtractor(@Nullable DocumentHeaderParser headerParser) {
        this.headerParser = headerParser;
    }

    public PeppolMessageMetadata extract(@NotNull InputStream content) throws Exception {
        try {
            Header header = SbdReader.newInstance(content).getHeader();
            if (header == null) {
                throw new SbdhException("Could not extract SBD header, file might not be wrapped.");
            }

            return PeppolMessageMetadata.create(new OcTransmissionResult(header));

        } catch (Exception exception) {
            if (exception instanceof SbdhException) {
                logger.debug(exception.getMessage());
                return extractFromPayload(content);
            }
            throw exception;
        }
    }


    private PeppolMessageMetadata extractFromPayload(@NotNull InputStream payload) throws Exception {
        Header header = headerParser.parse(payload);
        if (header == null) {
            throw new OxalisContentException("Could not extract metadata from payload.");
        }

        return PeppolMessageMetadata.create(new OcTransmissionResult(header));
    }

}
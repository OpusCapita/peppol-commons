package com.opuscapita.peppol.commons.container.metadata;

import com.opuscapita.peppol.commons.container.metadata.parser.DocumentHeaderParser;
import no.difi.vefa.peppol.common.model.Header;
import no.difi.vefa.peppol.sbdh.SbdReader;
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
    public MetadataExtractor(DocumentHeaderParser headerParser) {
        this.headerParser = headerParser;
    }

    public ContainerMessageMetadata extract(InputStream content) {
        try {
            Header header = SbdReader.newInstance(content).getHeader();
            if (header == null) {
                return null;
            }
            return ContainerMessageMetadata.create(new OcTransmissionResult(header));

        } catch (Exception e) {
            logger.error("MetadataExtractor encountered exception: " + e.getMessage());
            return null;
        }
    }

    public ContainerMessageMetadata extractFromPayload(InputStream payload) {
        try {
            Header header = headerParser.parse(payload);
            if (header == null) {
                return null;
            }
            return ContainerMessageMetadata.create(new OcTransmissionResult(header));

        } catch (Exception e) {
            logger.error("MetadataExtractor encountered exception: " + e.getMessage());
            return null;
        }
    }

    public ContainerBusinessMetadata extractBusinessMetadata(InputStream payload) {
        try {
            return headerParser.businessParse(payload);

        } catch (Exception e) {
            logger.error("MetadataExtractor encountered exception: " + e.getMessage());
            return new ContainerBusinessMetadata();
        }
    }

}
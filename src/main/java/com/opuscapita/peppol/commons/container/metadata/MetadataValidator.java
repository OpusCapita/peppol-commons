package com.opuscapita.peppol.commons.container.metadata;

import com.opuscapita.peppol.commons.container.ContainerMessage;
import com.opuscapita.peppol.commons.storage.Storage;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MetadataValidator {

    private static final Logger logger = LoggerFactory.getLogger(MetadataValidator.class);

    private Storage storage;
    private MetadataExtractor metadataExtractor;

    @Autowired
    public MetadataValidator(Storage storage, MetadataExtractor metadataExtractor) {
        this.storage = storage;
        this.metadataExtractor = metadataExtractor;
    }

    public void validate(@NotNull ContainerMessage cm) {
        ContainerMessageMetadata metadata = cm.getMetadata();

        if (metadata == null) {
            logger.debug("No metadata info found in the message: " + cm.getFileName());
            try {
                metadata = extractMetadata(cm);

                if (metadata == null) {
                    cm.getHistory().addError("Could not extract the header from file");
                    return;
                }
            } catch (Exception e) {
                logger.error("Could not extract the metadata from file: " + cm.getFileName(), e);
                cm.getHistory().addError(e.getMessage());
                return;
            }
        }

        List<String> missingFields = new ArrayList<>();
        if (StringUtils.isBlank(metadata.getSenderId())) {
            missingFields.add("senderId");
        }
        if (StringUtils.isBlank(metadata.getRecipientId())) {
            missingFields.add("receiverId");
        }
        if (StringUtils.isBlank(metadata.getDocumentTypeIdentifier())) {
            missingFields.add("documentTypeIdentifier");
        }
        if (StringUtils.isBlank(metadata.getProfileTypeIdentifier())) {
            missingFields.add("profileTypeIdentifier");
        }

        if (!missingFields.isEmpty()) {
            String tmp = missingFields.stream().collect(Collectors.joining(", "));
            cm.getHistory().addError("Missing metadata information [" + tmp + "]");
        }
    }

    private ContainerMessageMetadata extractMetadata(@NotNull ContainerMessage cm) throws Exception {
        logger.debug("I will try to extract metadata from payload");

        try (InputStream content = storage.get(cm.getFileName())) {
            ContainerMessageMetadata metadata = metadataExtractor.extract(content);
            cm.setMetadata(metadata);
            return metadata;
        }
    }

}

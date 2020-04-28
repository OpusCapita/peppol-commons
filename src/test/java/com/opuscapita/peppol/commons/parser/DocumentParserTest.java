package com.opuscapita.peppol.commons.parser;

import com.opuscapita.peppol.commons.container.metadata.ContainerBusinessMetadata;
import com.opuscapita.peppol.commons.container.metadata.MetadataExtractor;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertNotNull;

@Ignore
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@EnableAutoConfiguration
public class DocumentParserTest {

    @Autowired
    private MetadataExtractor extractor;

    @Test
    public void extractBusinessMetadata() throws IOException {
        File file = ResourceUtils.getFile("classpath:test-materials/sample-beast.xml");

        try (InputStream inputStream = new FileInputStream(file)) {
            ContainerBusinessMetadata metadata = extractor.extractBusinessMetadata(inputStream);
            assertNotNull(metadata);
            assertNotNull(metadata.getIssueDate());
            assertNotNull(metadata.getSenderName());
        }
    }

}

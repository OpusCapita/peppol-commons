package com.opuscapita.peppol.commons.container.metadata.parser;

import com.opuscapita.peppol.commons.container.metadata.ContainerBusinessMetadata;
import no.difi.oxalis.api.lang.OxalisContentException;
import no.difi.oxalis.sniffer.PeppolStandardBusinessHeader;
import no.difi.oxalis.sniffer.document.HardCodedNamespaceResolver;
import no.difi.vefa.peppol.common.model.Header;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;

/**
 * Copied from oxalis "no.difi.oxalis.sniffer.document.NoSbdhParser"
 * <p>
 * Parses UBL based documents, which are not wrapped within an SBDH
 * extracting data and creating a Header.
 */
@Component
public class DocumentHeaderParser {

    private final DocumentBuilderFactory documentBuilderFactory;

    public DocumentHeaderParser() {
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);

        try {
            documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException("Unable to configure DOM parser for secure processing.", e);
        }
    }

    /**
     * Parses and extracts the data needed to create a PeppolStandardBusinessHeader object. The inputstream supplied
     * should not be wrapped in an SBDH.
     *
     * @param inputStream UBL XML data without an SBDH.
     * @return an instance of Header populated with data from the UBL XML document.
     * @throws OxalisContentException invalid file content
     */
    public Header parse(InputStream inputStream) throws OxalisContentException {
        return originalParse(inputStream).toVefa();
    }

    /**
     * Parses and extracts the data needed to create a PeppolStandardBusinessHeader object. The inputstream supplied
     * should not be wrapped in an SBDH.
     *
     * @param inputStream UBL XML data without an SBDH.
     * @return an instance of PeppolStandardBusinessHeader populated with data from the UBL XML document.
     * @throws OxalisContentException invalid file content
     */
    private PeppolStandardBusinessHeader originalParse(InputStream inputStream) throws OxalisContentException {
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);

            XPath xPath = XPathFactory.newInstance().newXPath();
            xPath.setNamespaceContext(new HardCodedNamespaceResolver());

            PeppolStandardBusinessHeader sbdh = PeppolStandardBusinessHeader.createPeppolStandardBusinessHeaderWithNewDate();

            DocumentPayloadParser parser = new DocumentPayloadParser(document, xPath);
            if (parser.canParse()) {
                sbdh.setDocumentTypeIdentifier(parser.fetchDocumentTypeId().toVefa());
                sbdh.setProfileTypeIdentifier(parser.fetchProcessTypeId());
                sbdh.setSenderId(parser.getSender());
                sbdh.setRecipientId(parser.getReceiver());
            }
            return sbdh;
        } catch (Exception e) {
            throw new OxalisContentException("Unable to parseOld document " + e.getMessage(), e);
        }
    }

    public ContainerBusinessMetadata businessParse(InputStream inputStream) throws OxalisContentException {
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);

            XPath xPath = XPathFactory.newInstance().newXPath();
            xPath.setNamespaceContext(new HardCodedNamespaceResolver());

            ContainerBusinessMetadata metadata = new ContainerBusinessMetadata();

            DocumentPayloadParser parser = new DocumentPayloadParser(document, xPath);
            metadata.setDocumentId(parser.fetchId());
            metadata.setIssueDate(parser.fetchIssueDate());
            metadata.setIssueTime(parser.fetchIssueTime());
            metadata.setSenderName(parser.getSenderName());
            metadata.setReceiverName(parser.getReceiverName());
            return metadata;
        } catch (Exception e) {
            throw new OxalisContentException("Unable to parse business metadata " + e.getMessage(), e);
        }
    }
}

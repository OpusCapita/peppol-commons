package com.opuscapita.peppol.commons.container.metadata.parser;

import no.difi.oxalis.sniffer.identifier.CustomizationIdentifier;
import no.difi.oxalis.sniffer.identifier.PeppolDocumentTypeId;
import no.difi.vefa.peppol.common.model.ParticipantIdentifier;
import no.difi.vefa.peppol.common.model.ProcessIdentifier;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

@SuppressWarnings("WeakerAccess")
public class DocumentPayloadParser {

    private final XPath xPath;
    private final Document document;
    private final DocumentParticipantParser participantParser;

    public DocumentPayloadParser(Document document, XPath xPath) {
        this.xPath = xPath;
        this.document = document;
        this.participantParser = new DocumentParticipantParser(document, xPath);
    }

    public String localName() {
        return document.getDocumentElement().getLocalName();
    }

    public String rootNameSpace() {
        return document.getDocumentElement().getNamespaceURI();
    }

    public String ublVersion() {
        String version = retriveValueForXpath("//cbc:UBLVersionID");
        return StringUtils.isNotBlank(version) ? version : "2.1"; //maybe?
    }

    public boolean canParse() {
        return ("" + rootNameSpace()).startsWith("urn:oasis:names:specification:ubl:schema:xsd:");
    }

    public CustomizationIdentifier fetchCustomizationId() {
        String value = retriveValueForXpath("//cbc:CustomizationID");
        return CustomizationIdentifier.valueOf(value);
    }

    public ProcessIdentifier fetchProcessTypeId() {
        String value = retriveValueForXpath("//cbc:ProfileID");
        return ProcessIdentifier.of(value);
    }

    public PeppolDocumentTypeId fetchDocumentTypeId() {
        CustomizationIdentifier customizationIdentifier = fetchCustomizationId();
        return new PeppolDocumentTypeId(rootNameSpace(), localName(), customizationIdentifier, ublVersion());
    }

    public ParticipantIdentifier getSender() {
        return participantParser.getSender();
    }

    public ParticipantIdentifier getReceiver() {
        return participantParser.getReceiver();
    }

    public String retriveValueForXpath(String s) {
        try {
            String value = xPath.evaluate(s, document);
            if (value == null) {
                throw new IllegalStateException("Unable to find value for Xpath expr " + s);
            }
            return value.trim();
        } catch (XPathExpressionException e) {
            throw new IllegalStateException("Unable to evaluate " + s + "; " + e.getMessage(), e);
        }
    }

}

package com.opuscapita.peppol.commons.container.metadata.parser;

import no.difi.oxalis.sniffer.identifier.CustomizationIdentifier;
import no.difi.oxalis.sniffer.identifier.PeppolDocumentTypeId;
import no.difi.vefa.peppol.common.model.ProcessIdentifier;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;

import javax.xml.xpath.XPath;

public class DocumentPayloadParser extends DocumentParticipantParser {

    DocumentPayloadParser(Document document, XPath xPath) {
        super(document, xPath);
    }

    private String rootNameSpace() {
        return document.getDocumentElement().getNamespaceURI();
    }

    boolean canParse() {
        return ("" + rootNameSpace()).startsWith("urn:oasis:names:specification:ubl:schema:xsd:");
    }

    ProcessIdentifier fetchProcessTypeId() {
        String value = retriveValueForXpath("//cbc:ProfileID");
        return ProcessIdentifier.of(value);
    }

    PeppolDocumentTypeId fetchDocumentTypeId() {
        CustomizationIdentifier customizationIdentifier = fetchCustomizationId();
        return new PeppolDocumentTypeId(rootNameSpace(), localName(), customizationIdentifier, ublVersion());
    }

    private CustomizationIdentifier fetchCustomizationId() {
        String value = retriveValueForXpath("//cbc:CustomizationID");
        return CustomizationIdentifier.valueOf(value);
    }

    private String ublVersion() {
        String version = retriveValueForXpath("//cbc:UBLVersionID");
        return StringUtils.isNotBlank(version) ? version : "2.1"; //maybe?
    }

    String fetchId() {
        try {
            return retriveValueForXpath("//cbc:ID");
        } catch (Exception e) {
            return null;
        }
    }

    String fetchIssueDate() {
        try {
            return retriveValueForXpath("//cbc:IssueDate");
        } catch (Exception e) {
            return null;
        }
    }

    String fetchIssueTime() {
        try {
            return retriveValueForXpath("//cbc:IssueTime");
        } catch (Exception e) {
            return null;
        }
    }

}

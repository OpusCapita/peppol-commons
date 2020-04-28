package com.opuscapita.peppol.commons.container.metadata.parser;

import no.difi.oxalis.sniffer.identifier.CustomizationIdentifier;
import no.difi.oxalis.sniffer.identifier.PeppolDocumentTypeId;
import no.difi.vefa.peppol.common.model.ProcessIdentifier;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;

import javax.xml.xpath.XPath;
import java.util.ArrayList;
import java.util.List;

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
        String version = null;
        try {
            version = retriveValueForXpath("//cbc:UBLVersionID");
        } catch (Exception ignored) {
        }

        if (StringUtils.isBlank(version)) {
            try {
                version = document.getElementsByTagName("UBLVersionID").item(0).getTextContent();
            } catch (Exception ignored) {
            }
        }

        return StringUtils.isNotBlank(version) ? version : "2.1"; //maybe?
    }

    String fetchId() {
        String id = null;
        try {
            id = retriveValueForXpath("//cbc:ID");
        } catch (Exception ignored) {
        }

        if (StringUtils.isBlank(id)) {
            try {
                id = document.getElementsByTagName("ID").item(0).getTextContent();
            } catch (Exception ignored) {
            }
        }

        // 162: BEAst Order (not ubl)
        if (StringUtils.isBlank(id)) {
            try {
                id = retriveValueForXpath("//OrderHeader/OrderNumber");
            } catch (Exception ignored) {
            }
        }

        return id;
    }

    String fetchIssueDate() {
        List<String> paths = new ArrayList<>();
        paths.add("//cbc:IssueDate");
        paths.add("//IssueDate");
        paths.add("//OrderHeader/OrderDateAndTime");
        return checkPathsString(paths);
    }

    String fetchIssueTime() {
        List<String> paths = new ArrayList<>();
        paths.add("//cbc:IssueTime");
        paths.add("//IssueTime");
        return checkPathsString(paths);
    }

}

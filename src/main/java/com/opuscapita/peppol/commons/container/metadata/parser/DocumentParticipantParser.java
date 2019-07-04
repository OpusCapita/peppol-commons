package com.opuscapita.peppol.commons.container.metadata.parser;

import no.difi.oxalis.sniffer.identifier.ParticipantId;
import no.difi.oxalis.sniffer.identifier.SchemeId;
import no.difi.vefa.peppol.common.model.ParticipantIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
class DocumentParticipantParser {

    private static final Logger logger = LoggerFactory.getLogger(DocumentParticipantParser.class);

    protected final XPath xPath;
    protected final Document document;

    DocumentParticipantParser(Document document, XPath xPath) {
        this.xPath = xPath;
        this.document = document;
    }

    protected String localName() {
        String localName = document.getDocumentElement().getLocalName();
        if ("StandardBusinessDocument".equals(localName)) {
            localName = document.getDocumentElement().getChildNodes().item(1).getLocalName();
        }
        return localName;
    }

    ParticipantIdentifier getSender() {
        List<String> paths = new ArrayList<>();

        String type = localName();
        if ("DespatchAdvice".equalsIgnoreCase(type)) {
            paths.add("//cac:DespatchSupplierParty/cac:Party/cbc:EndpointID");
            paths.add("//cac:DespatchSupplierParty/cac:Party/cac:PartyIdentification/cbc:ID");
        }
        if ("Catalogue".equalsIgnoreCase(type)) {
            paths.add("//cac:ProviderParty/cbc:EndpointID");
            paths.add("//cac:ProviderParty/cac:PartyIdentification/cbc:ID");
            paths.add("//cac:SellerSupplierParty/cac:Party/cbc:EndpointID");
            paths.add("//cac:SellerSupplierParty/cac:Party/cac:PartyIdentification/cbc:ID");
        }
        if ("Invoice".equalsIgnoreCase(type) || "CreditNote".equalsIgnoreCase(type) || "Reminder".equalsIgnoreCase(type)) {
            paths.add("//cac:AccountingSupplierParty/cac:Party/cbc:EndpointID");
            paths.add("//cac:AccountingSupplierParty/cac:Party/cac:PartyLegalEntity/cbc:CompanyID");
            paths.add("//cac:AccountingSupplierParty/cac:Party/cac:PartyIdentification/cbc:ID");
            paths.add("//cac:SellerParty/cac:Party/cbc:EndpointID");
        }
        if ("Order".equalsIgnoreCase(type)) {
            paths.add("//cac:BuyerCustomerParty/cac:Party/cbc:EndpointID");
            paths.add("//cac:BuyerCustomerParty/cac:Party/cac:PartyIdentification/cbc:ID");
        }
        if ("OrderResponse".equalsIgnoreCase(type) || "OrderResponseSimple".equalsIgnoreCase(type)) {
            paths.add("//cac:SellerSupplierParty/cac:Party/cbc:EndpointID");
            paths.add("//cac:SellerSupplierParty/cac:Party/cac:PartyIdentification/cbc:ID");
        }
        if ("ApplicationResponse".equalsIgnoreCase(type)) {
            paths.add("//cac:SenderParty/cbc:EndpointID");
            paths.add("//cac:SenderParty/cac:PartyIdentification/cbc:ID");
        }

        return checkPaths(paths);
    }

    String getSenderName() {
        List<String> paths = new ArrayList<>();

        String type = localName();
        if ("DespatchAdvice".equalsIgnoreCase(type)) {
            paths.add("//cac:DespatchSupplierParty/cac:Party/cac:PartyName/cbc:Name");
        }
        if ("Catalogue".equalsIgnoreCase(type)) {
            paths.add("//cac:ProviderParty/cac:PartyName/cbc:Name");
            paths.add("//cac:SellerSupplierParty/cac:Party/cac:PartyName/cbc:Name");
        }
        if ("Invoice".equalsIgnoreCase(type) || "CreditNote".equalsIgnoreCase(type) || "Reminder".equalsIgnoreCase(type)) {
            paths.add("//cac:AccountingSupplierParty/cac:Party/cac:PartyName/cbc:Name");
            paths.add("//cac:SellerParty/cac:Party/cac:PartyName/cbc:Name");
        }
        if ("Order".equalsIgnoreCase(type)) {
            paths.add("//cac:BuyerCustomerParty/cac:Party/cac:PartyName/cbc:Name");
        }
        if ("OrderResponse".equalsIgnoreCase(type) || "OrderResponseSimple".equalsIgnoreCase(type)) {
            paths.add("//cac:SellerSupplierParty/cac:Party/cac:PartyName/cbc:Name");
        }
        if ("ApplicationResponse".equalsIgnoreCase(type)) {
            paths.add("//cac:SenderParty/cac:PartyName/cbc:Name");
        }

        return checkPathsString(paths);
    }

    ParticipantIdentifier getReceiver() {
        List<String> paths = new ArrayList<>();

        String type = localName();
        if ("DespatchAdvice".equalsIgnoreCase(type)) {
            paths.add("//cac:DeliveryCustomerParty/cac:Party/cbc:EndpointID");
            paths.add("//cac:DeliveryCustomerParty/cac:Party/cac:PartyIdentification/cbc:ID");
        }
        if ("Catalogue".equalsIgnoreCase(type)) {
            paths.add("//cac:ReceiverParty/cbc:EndpointID");
            paths.add("//cac:ReceiverParty/cac:PartyIdentification/cbc:ID");
            paths.add("//cac:BuyerCustomerParty/cac:Party/cbc:EndpointID");
            paths.add("//cac:BuyerCustomerParty/cac:Party/cac:PartyIdentification/cbc:ID");
        }
        if ("Invoice".equalsIgnoreCase(type) || "CreditNote".equalsIgnoreCase(type) || "Reminder".equalsIgnoreCase(type)) {
            paths.add("//cac:AccountingCustomerParty/cac:Party/cbc:EndpointID");
            paths.add("//cac:AccountingCustomerParty/cac:Party/cac:PartyLegalEntity/cbc:CompanyID");
            paths.add("//cac:AccountingCustomerParty/cac:Party/cac:PartyIdentification/cbc:ID");
            paths.add("//cac:BuyerParty/cac:Party/cbc:EndpointID");
        }
        if ("Order".equalsIgnoreCase(type)) {
            paths.add("//cac:SellerSupplierParty/cac:Party/cbc:EndpointID");
            paths.add("//cac:SellerSupplierParty/cac:Party/cac:PartyIdentification/cbc:ID");
        }
        if ("OrderResponse".equalsIgnoreCase(type) || "OrderResponseSimple".equalsIgnoreCase(type)) {
            paths.add("//cac:BuyerCustomerParty/cac:Party/cbc:EndpointID");
            paths.add("//cac:BuyerCustomerParty/cac:Party/cac:PartyIdentification/cbc:ID");
        }
        if ("ApplicationResponse".equalsIgnoreCase(type)) {
            paths.add("//cac:ReceiverParty/cbc:EndpointID");
            paths.add("//cac:ReceiverParty/cac:PartyIdentification/cbc:ID");
        }

        return checkPaths(paths);
    }

    String getReceiverName() {
        List<String> paths = new ArrayList<>();

        String type = localName();
        if ("DespatchAdvice".equalsIgnoreCase(type)) {
            paths.add("//cac:DeliveryCustomerParty/cac:Party/cac:PartyName/cbc:Name");
        }
        if ("Catalogue".equalsIgnoreCase(type)) {
            paths.add("//cac:ReceiverParty/cac:PartyName/cbc:Name");
            paths.add("//cac:BuyerCustomerParty/cac:Party/cac:PartyName/cbc:Name");
        }
        if ("Invoice".equalsIgnoreCase(type) || "CreditNote".equalsIgnoreCase(type) || "Reminder".equalsIgnoreCase(type)) {
            paths.add("//cac:AccountingCustomerParty/cac:Party/cac:PartyName/cbc:Name");
            paths.add("//cac:BuyerParty/cac:Party/cac:PartyName/cbc:Name");
        }
        if ("Order".equalsIgnoreCase(type)) {
            paths.add("//cac:SellerSupplierParty/cac:Party/cac:PartyName/cbc:Name");
        }
        if ("OrderResponse".equalsIgnoreCase(type) || "OrderResponseSimple".equalsIgnoreCase(type)) {
            paths.add("//cac:BuyerCustomerParty/cac:Party/cac:PartyName/cbc:Name");
        }
        if ("ApplicationResponse".equalsIgnoreCase(type)) {
            paths.add("//cac:ReceiverParty/cac:PartyName/cbc:Name");
        }

        return checkPathsString(paths);
    }

    protected ParticipantIdentifier checkPaths(List<String> paths) {
        for (String path : paths) {
            try {
                ParticipantIdentifier result = participantId(path);
                if (result != null) {
                    return result;
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    protected String checkPathsString(List<String> paths) {
        for (String path : paths) {
            try {
                return retriveValueForXpath(path);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    private ParticipantIdentifier participantId(String xPathExpr) {
        Element element;
        try {
            element = retrieveElementForXpath(xPathExpr);
        } catch (Exception ex) {
            return null;
        }

        ParticipantId ret;
        String companyId = element.getFirstChild().getNodeValue().trim();
        String schemeIdTextValue = element.getAttribute("schemeID").trim();

        // check if we already have a valid participant 9908:987654321
        if (ParticipantId.isValidParticipantIdentifierPattern(companyId)) {
            if (schemeIdTextValue.length() == 0) {
                // we accept participants with icd prefix if schemeId is missing ...
                ret = new ParticipantId(companyId);
            } else {
                // ... or when given schemeId matches the icd code stat eg NO:VAT matches 9908 from 9908:987654321
                if (companyId.startsWith(getIcdPrefix(schemeIdTextValue) + ":")) {
                    ret = new ParticipantId(companyId);
                } else {
                    logger.error(String.format(
                            "ParticipantId at '%s' is illegal, schemeId '%s' and icd code prefix of '%s' does not match",
                            xPathExpr, schemeIdTextValue, companyId));
                    return null;
                }
            }
        } else {
            // try to add the given icd prefix to the participant id
            companyId = String.format("%s:%s", getIcdPrefix(schemeIdTextValue), companyId);
            if (!ParticipantId.isValidParticipantIdentifierPattern(companyId)) {
                logger.error(String.format("ParticipantId syntax at '%s' evaluates to '%s' and is invalid", xPathExpr, companyId));
                return null;
            }
            ret = new ParticipantId(companyId);
        }
        return ret.toVefa();
    }

    private String getIcdPrefix(String schemeIdTextValue) {
        try {
            // schemaId as ICD identifier e.g. "ES:VAT"
            return SchemeId.parse(schemeIdTextValue).getCode();
        } catch (IllegalArgumentException ignored) {
            // schemaId as ICD code e.g. "9908"
            return SchemeId.fromISO6523(schemeIdTextValue).getCode();
        }
    }

    private Element retrieveElementForXpath(String s) {
        try {
            Element element = (Element) xPath.evaluate(s, document, XPathConstants.NODE);
            if (element == null) {
                throw new IllegalStateException("No element in XPath: " + s);
            }
            return element;
        } catch (XPathExpressionException e) {
            throw new IllegalStateException("Unable to evaluate " + s + "; " + e.getMessage(), e);
        }
    }

    protected String retriveValueForXpath(String s) {
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

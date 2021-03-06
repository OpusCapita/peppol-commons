package com.opuscapita.peppol.commons.container.xml;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.validation.SchemaFactory;

@Configuration
public class XmlParserConfigurator {

    @Bean
    @Lazy
    @ConditionalOnMissingBean
    public SAXParserFactory saxParserFactory() {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        return factory;
    }

    @Bean
    @Lazy
    @ConditionalOnMissingBean
    public TransformerFactory transformerFactory() {
        return TransformerFactory.newInstance();
    }

    @Bean
    @Lazy
    @ConditionalOnMissingBean
    public XMLInputFactory xmlInputFactory() {
        return XMLInputFactory.newFactory();
    }

    @Bean
    @Lazy
    @ConditionalOnMissingBean
    public SchemaFactory schemaFactory() {
        return SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    }
}
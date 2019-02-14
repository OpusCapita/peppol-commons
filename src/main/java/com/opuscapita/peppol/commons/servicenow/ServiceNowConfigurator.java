package com.opuscapita.peppol.commons.servicenow;

import com.opuscapita.peppol.commons.container.ContainerMessageSerializer;
import com.opuscapita.peppol.commons.errors.ErrorHandler;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;

@Configuration
public class ServiceNowConfigurator {

    @Bean
    @Lazy
    @ConditionalOnMissingBean
    public ErrorHandler errorHandler(@NotNull ServiceNow serviceNowRest, @NotNull ContainerMessageSerializer serializer) {
        return new ErrorHandler(serviceNowRest, serializer);
    }

    @Bean
    @Lazy
    @ConditionalOnMissingBean
    public ServiceNowConfiguration serviceNowConfiguration(@NotNull Environment environment) {
        return new ServiceNowConfiguration(
                environment.getProperty("snc.rest.url"),
                environment.getProperty("snc.rest.username"),
                environment.getProperty("snc.rest.password"),
                environment.getProperty("snc.bsc"),
                environment.getProperty("snc.from"),
                environment.getProperty("snc.businessGroup"));
    }

    @Bean
    @Lazy
    @ConditionalOnMissingBean
    public ServiceNow serviceNowRest(@NotNull Environment environment) {
        return new ServiceNowREST(serviceNowConfiguration(environment));
    }
}
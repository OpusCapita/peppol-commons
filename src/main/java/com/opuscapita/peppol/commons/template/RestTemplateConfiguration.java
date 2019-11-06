package com.opuscapita.peppol.commons.template;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {

    private final Integer TIMEOUT = 10000;

    @Bean
    @LoadBalanced
    @Qualifier("peppol")
    public RestTemplate restTemplate() throws Exception {
        return new RestTemplate();
    }

//    @Bean
//    @LoadBalanced
//    @Qualifier("peppol")
//    public RestTemplate restTemplate() throws Exception {
//        CloseableHttpClient httpClient = HttpClientBuilder.create()
//                .setConnectionManager(getConnectionManager())
//                .setDefaultRequestConfig(getRequestConfig())
//                .build();
//
//        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
//        requestFactory.setHttpClient(httpClient);
//        return new RestTemplate(requestFactory);
//    }

    private RequestConfig getRequestConfig() {
        return RequestConfig.custom()
                .setConnectionRequestTimeout(TIMEOUT)
                .setConnectTimeout(TIMEOUT)
                .setSocketTimeout(TIMEOUT)
                .build();
    }

    private PoolingHttpClientConnectionManager getConnectionManager() {
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        manager.setMaxTotal(100);
        manager.setDefaultMaxPerRoute(100);
        return manager;
    }
}

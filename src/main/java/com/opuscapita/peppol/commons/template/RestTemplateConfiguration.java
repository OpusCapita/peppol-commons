package com.opuscapita.peppol.commons.template;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

@Configuration
public class RestTemplateConfiguration {

    private final Integer TIMEOUT = 10000;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Bean
    @LoadBalanced
    @Qualifier("peppol")
    public RestTemplate restTemplate() throws Exception {
        restTemplateBuilder.messageConverters(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return restTemplateBuilder.build();
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

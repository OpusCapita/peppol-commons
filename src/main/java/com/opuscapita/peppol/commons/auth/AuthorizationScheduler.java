package com.opuscapita.peppol.commons.auth;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class AuthorizationScheduler {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationService.class);

    @Value("${service-client.username:''}")
    private String username;

    private AuthorizationService service;

    @Autowired
    public AuthorizationScheduler(AuthorizationService service) {
        this.service = service;
    }

    @Scheduled(fixedRateString = "${auth-cache.schedule:3480000}")
    public void updateCache() {
        logger.debug("AuthorizationScheduler updating auth.token cache");
        if (StringUtils.isNotBlank(username)) {
            service.updateTokenDetails(username);
        }
    }
}

package com.opuscapita.peppol.commons.auth;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xml.sax.ErrorHandler;

import static org.junit.Assert.assertNotNull;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@EnableAutoConfiguration
public class AuthorizationServiceTest {

    @Autowired
    private AuthorizationService service;

    @MockBean
    private ErrorHandler errorHandler;

    @Test
    @Ignore
    public void getTokenTest() {
        String auth = service.getAuthorizationHeader();
        assertNotNull(auth);
    }
}

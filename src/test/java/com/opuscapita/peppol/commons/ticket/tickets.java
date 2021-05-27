package com.opuscapita.peppol.commons.ticket;

import org.junit.Test;
//import org.junit.BeforeEach;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;
import java.io.*;

import com.opuscapita.peppol.commons.eventing.jsd.JsdTicketReporter;
import com.opuscapita.peppol.commons.eventing.jsd.JsdREST;
import com.opuscapita.peppol.commons.eventing.jsd.JsdConfiguration;

public class tickets {

    public void setUp() throws Exception {
        System.out.println("Start tests");
    }

    @Test
    public void testTicket()  {
        JsdConfiguration cfg = new JsdConfiguration();

        cfg.setTestValues(
          "https://customerportal-dev.opuscapita.com/rest/api/2/issue",
          "peppol",
          "Ysk2MWK826A2Fk3h" );

        JsdREST client = new JsdREST( cfg );
        JsdTicketReporter jr = new JsdTicketReporter( client );
        System.out.println("Start testTicket");
        jr.reportWithoutContainerMessage("testit", "filename.xml", new Exception("it failed"), "Could not run");
        System.out.println("After");
    }

}

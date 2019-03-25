package com.opuscapita.peppol.commons.storage.blob;

import com.opuscapita.peppol.commons.storage.StorageException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@EnableAutoConfiguration
public class BlobStorageTest {

    @Autowired
    private BlobStorage storage;

    @Before
    public void testClean() throws StorageException {
        storage.remove("/private/peppol/");
    }

    @Test
    @Ignore
    public void testPutMoveAndList() throws StorageException {
        String content = "akjmgalp2";
        String folder = "/private/peppol/hot/dev2/";
        String destFolder = "/private/peppol/cold/dev2/";
        String filename = "test2.xml";

        InputStream inputStream = new ByteArrayInputStream(content.getBytes());
        String putResponse = storage.putToCustom(inputStream, folder, filename);
        assertEquals(folder + filename, putResponse);

        String moveResponse = storage.moveToCustom(putResponse, destFolder);
        assertEquals(destFolder + filename, moveResponse);

        List<String> listResponse1 = storage.check(folder);
        assertTrue(listResponse1.isEmpty());

        List<String> listResponse2 = storage.check(destFolder);
        assertTrue(listResponse2.contains(moveResponse));

        storage.remove(moveResponse);

        List<String> listResponse3 = storage.check(destFolder);
        assertTrue(listResponse3.isEmpty());
    }

}

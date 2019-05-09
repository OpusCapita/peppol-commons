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
        String content = "akjmgalp1";
        String folder = "/private/peppol/hot/dev1/";
        String destFolder = "/private/peppol/cold/dev1/";
        String filename = "test1.xml";

        InputStream inputStream = new ByteArrayInputStream(content.getBytes());
        String putResponse = storage.put(inputStream, folder, filename);
        assertEquals(folder + filename, putResponse);

        String moveResponse = storage.move(putResponse, destFolder);
        assertEquals(destFolder + filename, moveResponse);

        List<String> listResponse1 = storage.list(folder);
        assertTrue(listResponse1.isEmpty());

        List<String> listResponse2 = storage.list(destFolder);
        assertTrue(listResponse2.contains(moveResponse));

        storage.remove(moveResponse);

        List<String> listResponse3 = storage.list(destFolder);
        assertTrue(listResponse3.isEmpty());
    }

    @Test
    @Ignore
    public void testFilenameUniqueification() throws StorageException {
        String content = "akjmgalp2";
        String folder = "/private/peppol/hot/dev2/";
        String filename1 = "test2.xml";
        String filename2 = "test2_0.xml";
        String filename3 = "test2_1.xml";

        InputStream inputStream = new ByteArrayInputStream(content.getBytes());
        String putResponse = storage.put(inputStream, folder, filename1);
        assertEquals(folder + filename1, putResponse);

        InputStream inputStream2 = new ByteArrayInputStream(content.getBytes());
        String putResponse2 = storage.put(inputStream2, folder, filename1);
        assertEquals(folder + filename2, putResponse2);

        InputStream inputStream3 = new ByteArrayInputStream(content.getBytes());
        String putResponse3 = storage.put(inputStream3, folder, filename1);
        assertEquals(folder + filename3, putResponse3);
    }

}

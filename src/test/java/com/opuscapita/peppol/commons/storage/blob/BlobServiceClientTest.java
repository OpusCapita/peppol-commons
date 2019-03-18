package com.opuscapita.peppol.commons.storage.blob;

import com.opuscapita.peppol.commons.storage.StorageException;
import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@EnableAutoConfiguration
public class BlobServiceClientTest {

    @Autowired
    private BlobServiceClient client;

    @Test
    @Ignore
    public void testPutGetAndList() throws IOException {
        String content = "akjmgalp1";
        String folder = "/peppol/hot/dev1/";
        String filename = "test1.xml";

        InputStream inputStream = new ByteArrayInputStream(content.getBytes());
        BlobServiceResponse putResponse = client.putFile(inputStream, folder + filename);
        assertEquals(folder + filename, putResponse.getPath());

        InputStream outputStream = client.getFile(folder + filename);
        String result = IOUtils.toString(outputStream, Charset.defaultCharset());
        assertEquals(content, result);

        List<BlobServiceResponse> listResponse = client.listFolder(folder);
        assertTrue(listResponse.stream().anyMatch(r -> r.getName().equals(filename)));
    }

    @Test
    @Ignore
    public void testPutMoveAndList() throws StorageException {
        String content = "akjmgalp2";
        String folder = "/peppol/hot/dev2/";
        String destFolder = "/peppol/cold/dev2/";
        String filename = "test2.xml";

        InputStream inputStream = new ByteArrayInputStream(content.getBytes());
        BlobServiceResponse putResponse = client.putFile(inputStream, folder + filename);
        assertEquals(folder + filename, putResponse.getPath());

        BlobServiceResponse moveResponse = client.moveFile(putResponse.getPath(), destFolder);
        assertEquals(destFolder + filename, moveResponse.getPath());

        List<BlobServiceResponse> listResponse1 = client.listFolder(folder);
        assertTrue(listResponse1.isEmpty());

        List<BlobServiceResponse> listResponse2 = client.listFolder(destFolder);
        assertTrue(listResponse2.stream().anyMatch(r -> r.getName().equals(filename)));
    }

    @Test
    @Ignore
    public void testPutRemoveAndList() throws StorageException {
        String content = "akjmgalp3";
        String folder = "/peppol/hot/dev3/";
        String filename = "test3.xml";

        InputStream inputStream = new ByteArrayInputStream(content.getBytes());
        BlobServiceResponse putResponse = client.putFile(inputStream, folder + filename);
        assertEquals(folder + filename, putResponse.getPath());

        client.remove("/peppol/");

        List<BlobServiceResponse> listResponse = client.listFolder(folder);
        assertTrue(listResponse.isEmpty());
    }

}

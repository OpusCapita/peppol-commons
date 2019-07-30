package com.opuscapita.peppol.commons.storage.blob;

import com.opuscapita.peppol.commons.storage.Storage;
import com.opuscapita.peppol.commons.storage.StorageException;
import com.opuscapita.peppol.commons.storage.StorageUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("Duplicates")
@Primary
@Component
public class BlobStorage implements Storage {

    @Value("${peppol.storage.blob.hot:hot}")
    private String hotFolder;

    @Value("${peppol.storage.blob.cold:cold}")
    private String coldFolder;

    private BlobServiceClient client;

    public BlobStorage(BlobServiceClient client) {
        this.client = client;
    }

    @Override
    public InputStream get(String path) throws StorageException {
        return client.getFile(path);
    }

    @Override
    public Long size(String path) throws StorageException {
        return client.size(path);
    }

    @Override
    public List<String> list(String folder) throws StorageException {
        return client.listFolder(folder).stream().map(BlobServiceResponse::getPath).collect(Collectors.toList());
    }

    @Override
    public void update(InputStream content, String path) throws StorageException {
        client.putFile(content, path);
    }

    @Override
    public String put(InputStream content, String folder, String filename) throws StorageException {
        String path = StorageUtils.uniqueifyFilename(folder + filename, this);
        return client.putFile(content, path).getPath();
    }

    @Override
    public String move(String path, String folder) throws StorageException {
        String dest = folder + FilenameUtils.getName(path);
        String uniq = StorageUtils.uniqueifyFilename(dest, this);
        return client.moveFile(path, uniq);
    }

    @Override
    public void remove(String path) throws StorageException {
        client.remove(path);
    }

    @Override
    public boolean exists(String path) throws StorageException {
        return client.isExists(path);
    }
}

package com.opuscapita.peppol.commons.storage.blob;

import com.opuscapita.peppol.commons.storage.Storage;
import com.opuscapita.peppol.commons.storage.StorageException;
import com.opuscapita.peppol.commons.storage.StorageUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<String> check(String folder) throws StorageException {
        return client.listFolder(folder).stream().map(BlobServiceResponse::getPath).collect(Collectors.toList());
    }

    @Override
    public String putToCustom(InputStream content, String folder, String filename) throws StorageException {
        return client.putFile(content, folder + filename).getPath();
    }

    @Override
    public String putToTemporary(InputStream content, String filename) throws StorageException {
        String path = StorageUtils.createDailyPath(hotFolder, filename);
        return client.putFile(content, path).getPath();
    }

    @Override
    public String putToPermanent(InputStream content, String filename, String senderId, String receiverId) throws StorageException {
        String path = StorageUtils.createUserPath(coldFolder, filename, senderId, receiverId);
        return client.putFile(content, path).getPath();
    }

    @Override
    public String moveToCustom(String path, String folder) throws StorageException {
        return client.moveFile(path, folder).getPath();
    }

    @Override
    public String moveToTemporary(String path) throws StorageException {
        String folder = StorageUtils.createDailyPath(hotFolder, "");
        return client.moveFile(path, folder).getPath();
    }

    @Override
    public String moveToPermanent(String path, String senderId, String receiverId) throws StorageException {
        String folder = StorageUtils.createUserPath(coldFolder, "", senderId, receiverId);
        return client.moveFile(path, folder).getPath();
    }

    @Override
    public void remove(String path) throws StorageException {
        client.remove(path);
    }

}

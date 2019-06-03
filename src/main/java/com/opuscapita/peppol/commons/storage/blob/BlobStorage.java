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
import java.util.concurrent.atomic.AtomicInteger;
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
        return retryGet(path);
    }

    @Override
    public List<String> list(String folder) throws StorageException {
        return client.listFolder(folder).stream().map(BlobServiceResponse::getPath).collect(Collectors.toList());
    }

    @Override
    public void update(InputStream content, String path) throws StorageException {
        retryPut(content, path);
    }

    @Override
    public String put(InputStream content, String folder, String filename) throws StorageException {
        String path = StorageUtils.uniqueifyFilename(folder + filename, this);
        return retryPut(content, path).getPath();
    }

    @Override
    public String move(String path, String folder) throws StorageException {
        String dest = folder + FilenameUtils.getName(path);
        String uniq = StorageUtils.uniqueifyFilename(dest, this);
        return retryMove(path, uniq);
    }

    @Override
    public void remove(String path) throws StorageException {
        client.remove(path);
    }

    @Override
    public boolean exists(String path) throws StorageException {
        return retryExists(path);
    }

    @Deprecated
    public String putToTemporary(InputStream content, String filename) throws StorageException {
        String path = StorageUtils.createDailyPath(hotFolder, filename);
        String uniq = StorageUtils.uniqueifyFilename(path, this);
        return retryPut(content, uniq).getPath();
    }

    @Deprecated
    public String putToPermanent(InputStream content, String filename, String senderId, String receiverId) throws StorageException {
        String path = StorageUtils.createUserPath(coldFolder, filename, senderId, receiverId);
        String uniq = StorageUtils.uniqueifyFilename(path, this);
        return retryPut(content, uniq).getPath();
    }

    @Deprecated
    public String moveToTemporary(String path) throws StorageException {
        String dest = StorageUtils.createDailyPath(hotFolder, FilenameUtils.getName(path));
        String uniq = StorageUtils.uniqueifyFilename(dest, this);
        return retryMove(path, uniq);
    }

    @Deprecated
    public String moveToPermanent(String path, String senderId, String receiverId) throws StorageException {
        String dest = StorageUtils.createUserPath(coldFolder, FilenameUtils.getName(path), senderId, receiverId);
        String uniq = StorageUtils.uniqueifyFilename(dest, this);
        return retryMove(path, uniq);
    }

    private InputStream retryGet(String path) throws StorageException {
        AtomicInteger i = new AtomicInteger(0);
        StorageException exception;
        do {
            try {
                return client.getFile(path);
            } catch (StorageException e) {
                exception = e;
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ignored) {
                }
            }
        } while (i.incrementAndGet() < 5);

        throw exception;
    }

    private BlobServiceResponse retryPut(InputStream data, String path) throws StorageException {
        AtomicInteger i = new AtomicInteger(0);
        StorageException exception;
        do {
            try {
                return client.putFile(data, path);
            } catch (StorageException e) {
                exception = e;
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ignored) {
                }
            }
        } while (i.incrementAndGet() < 5);

        throw exception;
    }

    private String retryMove(String src, String dst) throws StorageException {
        AtomicInteger i = new AtomicInteger(0);
        StorageException exception;
        do {
            try {
                return client.moveFile(src, dst);
            } catch (StorageException e) {
                exception = e;
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ignored) {
                }
            }
        } while (i.incrementAndGet() < 5);

        throw exception;
    }

    private boolean retryExists(String path) throws StorageException {
        AtomicInteger i = new AtomicInteger(0);
        StorageException exception;
        do {
            try {
                return client.isExists(path);
            } catch (StorageException e) {
                exception = e;
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ignored) {
                }
            }
        } while (i.incrementAndGet() < 5);

        throw exception;
    }

}

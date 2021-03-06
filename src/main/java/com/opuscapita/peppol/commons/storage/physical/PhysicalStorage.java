package com.opuscapita.peppol.commons.storage.physical;

import com.opuscapita.peppol.commons.storage.Storage;
import com.opuscapita.peppol.commons.storage.StorageException;
import com.opuscapita.peppol.commons.storage.StorageUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PhysicalStorage implements Storage {

    @Value("${peppol.storage.blob.hot:hot}")
    private String hotFolder;

    @Value("${peppol.storage.blob.cold:cold}")
    private String coldFolder;

    @Override
    public InputStream get(String path) throws StorageException {
        try {
            return new FileInputStream(path);
        } catch (IOException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public Long size(String path) throws StorageException {
        try {
            return new File(path).length();
        } catch (Exception e) {
            return 0L;
        }
    }

    @Override
    public List<String> list(String folder) throws StorageException {
        try {
            File[] files = new File(folder).listFiles();
            List<File> list = new ArrayList<>(Arrays.asList(files));
            return list.stream().filter(File::isFile).map(File::getAbsolutePath).collect(Collectors.toList());
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void update(InputStream content, String path) throws StorageException {
        File file = new File(path);
        try {
            FileUtils.copyInputStreamToFile(content, file);
        } catch (IOException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public String put(InputStream content, String folder, String filename) throws StorageException {
        String uniq = StorageUtils.uniqueifyFilename(folder + filename, this);
        File file = new File(uniq);
        try {
            FileUtils.copyInputStreamToFile(content, file);
            return file.getAbsolutePath();
        } catch (IOException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public String move(String path, String folder) throws StorageException {
        File source = new File(path);
        String uniq = StorageUtils.uniqueifyFilename(folder + source.getName(), this);
        File dest = new File(uniq);
        try {
            FileUtils.moveFile(source, dest);
            return dest.getAbsolutePath();
        } catch (IOException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void remove(String path) throws StorageException {
        File file = new File(path);
        Boolean result = FileUtils.deleteQuietly(file);
        if (!result) {
            throw new StorageException("Error occurred while removing the file: " + path);
        }
    }

    @Override
    public boolean exists(String path) throws StorageException {
        return new File(path).exists();
    }
}

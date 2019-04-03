package com.opuscapita.peppol.commons.storage;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StorageUtils {

    public static final String FILE_SEPARATOR = "/";

    public static String createDailyPath(String parent, String filename) {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        return parent + FILE_SEPARATOR + date + FILE_SEPARATOR + filename;
    }

    public static String createUserPath(String parent, String filename, String senderId, String receiverId) {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        return parent + FILE_SEPARATOR + normalizeUserId(senderId) + FILE_SEPARATOR + normalizeUserId(receiverId) + FILE_SEPARATOR + date + FILE_SEPARATOR + filename;
    }

    public static String normalizeUserId(String userId) {
        userId = StringUtils.isBlank(userId) ? "unknown" : userId;
        return userId.replaceAll("[^a-zA-Z0-9.-]", "_");
    }

    public static String uniqueifyFilename(String path, Storage storage) throws StorageException {
        if (!storage.exists(path)) {
            return path;
        }

        String pathName = FilenameUtils.getFullPath(path);
        String filename = FilenameUtils.getName(path);
        String basename = FilenameUtils.getBaseName(filename);
        String extension = FilenameUtils.getExtension(filename);
        extension = StringUtils.isBlank(extension) ? "" : "." + extension;

        String check = pathName + basename + "_0" + extension;
        for (int i = 1; storage.exists(check); i++) {
            check = pathName + basename + "_" + i + extension;
        }
        return check;
    }

}

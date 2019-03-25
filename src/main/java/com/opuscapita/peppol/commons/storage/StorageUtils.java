package com.opuscapita.peppol.commons.storage;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StorageUtils {

    public static String createDailyPath(String parent, String filename) {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        return parent + File.separator + date + File.separator + filename;
    }

    public static String createUserPath(String parent, String filename, String senderId, String receiverId) {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        return parent + File.separator + normalizeUserId(senderId) + File.separator + normalizeUserId(receiverId) + File.separator + date + File.separator + filename;
    }

    public static String normalizeUserId(String userId) {
        userId = StringUtils.isBlank(userId) ? "unknown" : userId;
        return userId.replaceAll("[^a-zA-Z0-9.-]", "_");
    }

    public static String uniqueifyFilename(String path, Storage storage) throws StorageException {
        if (!storage.exists(path)) {
            return path;
        }

        String pathName = FilenameUtils.getPath(path);
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

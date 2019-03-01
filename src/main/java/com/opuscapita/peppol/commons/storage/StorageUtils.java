package com.opuscapita.peppol.commons.storage;

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
}

package com.opuscapita.peppol.commons.storage.blob;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

public class BlobServiceResponse {

    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    private String name;
    private String location;
    private String path;
    private Integer size;
    private Boolean isFile;
    private Boolean isDirectory;
    private Date lastModified;

    public static BlobServiceResponse fromJson(String json) {
        return gson.fromJson(json, BlobServiceResponse.class);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Boolean getFile() {
        return isFile;
    }

    public void setFile(Boolean file) {
        isFile = file;
    }

    public Boolean getDirectory() {
        return isDirectory;
    }

    public void setDirectory(Boolean directory) {
        isDirectory = directory;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
}

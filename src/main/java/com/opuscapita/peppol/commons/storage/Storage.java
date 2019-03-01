package com.opuscapita.peppol.commons.storage;

import java.io.InputStream;
import java.util.List;

public interface Storage {

    /**
     * Returns the file content as input stream
     *
     * @param path full path of the file ex: "/peppol/hot/20190223/test.xml"
     */
    InputStream get(String path) throws StorageException;

    /**
     * Checks the folder and returns the full path of the files in that folder as a string list
     *
     * @param folder the folder name to check, has to end with a slash ex: "/peppol/in/a2a/"
     */
    List<String> check(String folder) throws StorageException;

    /**
     * Puts file to the given folder, with the given filename
     *
     * @param content  the file content as input stream
     * @param folder   the folder to put the file, has to end with a slash ex: "/peppol/in/xib/"
     * @param filename the filename ex: "test.xml"
     * @return the final full path of the file ex: "/peppol/in/xib/test.xml"
     */
    String putToCustom(InputStream content, String folder, String filename) throws StorageException;

    /**
     * Puts file to the short-term storage folder, with the given filename
     *
     * @param content  the file content as input stream
     * @param filename the filename ex: "test.xml"
     * @return the final full path of the file ex: "/peppol/hot/20199223/test.xml"
     */
    String putToTemporary(InputStream content, String filename) throws StorageException;

    /**
     * Puts file to the long-term storage folder, with the given filename
     *
     * @param content  the file content as input stream
     * @param filename the filename ex: "test.xml"
     * @return the final full path of the file ex: "/peppol/cold/9908_987987987/0007_232100032/20199223/test.xml"
     */
    String putToPermanent(InputStream content, String filename, String senderId, String receiverId) throws StorageException;

    /**
     * Moves file to the given folder
     *
     * @param path   the current full path of the file ex: "/peppol/cold/9908_987987987/0007_232100032/20199223/test.xml"
     * @param folder the folder to put the file, has to end with a slash ex: "/peppol/out/xib/"
     * @return the final full path of the file ex: "/peppol/out/xib/test.xml"
     */
    String moveToCustom(String path, String folder) throws StorageException;

    /**
     * Moves file to the short-term storage folder
     *
     * @param path the current full path of the file ex: "/peppol/in/a2a/test.xml"
     * @return the final full path of the file ex: "/peppol/hot/20199223/test.xml"
     */
    String moveToTemporary(String path) throws StorageException;

    /**
     * Moves file to the long-term storage folder
     *
     * @param path the current full path of the file ex: "/peppol/hot/20199223/test.xml"
     * @return the final full path of the file ex: "/peppol/cold/9908_987987987/0007_232100032/20199223/test.xml"
     */
    String moveToPermanent(String path, String senderId, String receiverId) throws StorageException;

    /**
     * Removes a file or a directory, note that if a directory is passed as parameter, it deletes recursively
     *
     * @param path the full path of the file or the directory ex: "/peppol/hot/20199223/test.xml"
     */
    void remove(String path) throws StorageException;

}

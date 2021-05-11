package uk.co.ankeetpatel.encryptedfilesystem.efsserver.services;

public interface S3Services {
    /**
     *
     * @param keyName
     */
    public void downloadFile(String keyName);

    /**
     *
     * @param keyName
     * @param uploadFilePath
     */
    public void uploadFile(String keyName, String uploadFilePath);
}
package uk.co.ankeetpatel.encryptedfilesystem.efsserver.security.services;

public interface S3Services {
    public void downloadFile(String keyName);
    public void uploadFile(String keyName, String uploadFilePath);
}
package uk.co.ankeetpatel.encryptedfilesystem.efsserver.Exception;

public class NoAccessToFileException extends Exception {

    public NoAccessToFileException() {
        super("User does not have access to this file.");
    }

    public NoAccessToFileException(String s) {
        super(s);
    }

}

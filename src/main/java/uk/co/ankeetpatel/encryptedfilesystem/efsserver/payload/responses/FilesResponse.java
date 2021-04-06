package uk.co.ankeetpatel.encryptedfilesystem.efsserver.payload.responses;

import uk.co.ankeetpatel.encryptedfilesystem.efsserver.models.File;

import java.util.ArrayList;

public class FilesResponse {

    private File file;

    private ArrayList<Integer> permissions;

    public FilesResponse(File file, ArrayList<Integer> permissions) {
        this.file = file;
        this.permissions = permissions;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public ArrayList<Integer> getPermissions() {
        return permissions;
    }

    public void setPermissions(ArrayList<Integer> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return "FilesResponse{" +
                "file=" + file +
                ", permissions=" + permissions +
                '}';
    }
}

package uk.co.ankeetpatel.encryptedfilesystem.efsserver.payload.responses;

import uk.co.ankeetpatel.encryptedfilesystem.efsserver.models.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AllowedFilesResponse {

    private ArrayList<FilesResponse> filesResponse;

    public AllowedFilesResponse(ArrayList<FilesResponse> filesResponse) {
        this.filesResponse = filesResponse;
    }

    public ArrayList<FilesResponse> getFilesResponse() {
        return filesResponse;
    }

    public void setFilesResponse(ArrayList<FilesResponse> filesResponse) {
        this.filesResponse = filesResponse;
    }
}

package uk.co.ankeetpatel.encryptedfilesystem.efsserver.payload.responses;

import org.json.simple.JSONArray;

import java.util.ArrayList;

public class DownloadResponse {

    private Long id;
    private JSONArray file;
    private String fileName;

    public DownloadResponse(Long id, ArrayList<byte[]> file, String fileName) {
        this.id = id;
        this.fileName = fileName;

        JSONArray JSONarr = new JSONArray();
        for (byte[] arr : file) {
            JSONArray jsa = new JSONArray();
            for (int i = 0; i < arr.length; i++) {
                jsa.add(arr[i]);
            }
            JSONarr.add(jsa);
        }
        this.file = JSONarr;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ArrayList<byte[]> getFile() {
        return file;
    }

    public void setFile(ArrayList<byte[]> file) {
        JSONArray JSONarr = new JSONArray();
        for (byte[] arr : file) {
            JSONArray jsa = new JSONArray();
            for (int i = 0; i < arr.length; i++) {
                jsa.add(arr[i]);
            }
            JSONarr.add(jsa);
        }
        this.file = JSONarr;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}

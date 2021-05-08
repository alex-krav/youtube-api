package com.youtube.uploadvideo.pojo;

public class UploadResponse {

    private final long status;
    private final String message;

    public UploadResponse(long status, String message) {
        this.status = status;
        this.message = message;
    }

    public long getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}

package com.youtube.uploadvideo.model;

public class UploadResponse {

    private final long status;
    private final String message;
    private final String videoId;

    public UploadResponse(long status, String message, String videoId) {
        this.status = status;
        this.message = message;
        this.videoId = videoId;
    }

    public long getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getVideoId() {
        return videoId;
    }
}

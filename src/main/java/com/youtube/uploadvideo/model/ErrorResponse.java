package com.youtube.uploadvideo.model;

public class ErrorResponse {

    private final long status;
    private final String message;

    public ErrorResponse(long status, String message) {
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

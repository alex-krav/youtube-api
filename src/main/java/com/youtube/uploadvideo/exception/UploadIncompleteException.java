package com.youtube.uploadvideo.exception;

import com.youtube.uploadvideo.model.UploadStatus;
import org.springframework.http.HttpStatus;

public class UploadIncompleteException extends RuntimeException {

    private final String videoId;

    public UploadIncompleteException(String videoId) {
        super(videoId);
        this.videoId = videoId;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getUploadStatus() {
        return UploadStatus.IN_PROGRESS.value();
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.ACCEPTED;
    }
}

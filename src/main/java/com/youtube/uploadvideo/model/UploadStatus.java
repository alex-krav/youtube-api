package com.youtube.uploadvideo.model;

public enum UploadStatus {
    IN_PROGRESS("In progress"),
    COMPLETE("Complete"),
    FAILED("Failed");

    private String value;

    UploadStatus(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public UploadStatus getByValue(String value) {
        for (UploadStatus v : values()) {
            if (v.value().equals(value)) {
                return v;
            }
        }
        return null;
    }
}

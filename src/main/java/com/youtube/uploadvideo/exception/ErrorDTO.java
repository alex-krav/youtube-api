package com.youtube.uploadvideo.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorDTO {
    public int code;
    public String status;
    public String message;
}

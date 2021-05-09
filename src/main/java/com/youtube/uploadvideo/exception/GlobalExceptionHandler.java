package com.youtube.uploadvideo.exception;

import com.youtube.uploadvideo.storage.StorageFileNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(VideoNotFoundException.class)
    public ResponseEntity<ErrorDTO> generateVideoNotFoundException(VideoNotFoundException ex) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage(ex.getMessage());
        errorDTO.setStatus(String.valueOf(ex.getStatus().value()));
        errorDTO.setTime(new Date().toString());

        return new ResponseEntity<>(errorDTO, ex.getStatus());
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<ErrorDTO> generateNotAuthorizedException(NotAuthorizedException ex) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage(ex.getMessage());
        errorDTO.setStatus(String.valueOf(ex.getStatus().value()));
        errorDTO.setTime(new Date().toString());

        return new ResponseEntity<>(errorDTO, ex.getStatus());
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<ErrorDTO> generateServiceNotAvailableException(StorageFileNotFoundException ex) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage("Service not available");
        errorDTO.setStatus(String.valueOf(HttpStatus.SERVICE_UNAVAILABLE.value()));
        errorDTO.setTime(new Date().toString());

        return new ResponseEntity<>(errorDTO, HttpStatus.SERVICE_UNAVAILABLE);
    }
}

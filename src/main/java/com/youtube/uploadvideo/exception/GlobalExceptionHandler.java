package com.youtube.uploadvideo.exception;

import com.youtube.uploadvideo.model.StatusDTO;
import com.youtube.uploadvideo.storage.StorageFileNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(VideoNotFoundException.class)
    public ResponseEntity<ErrorDTO> generateVideoNotFoundException(VideoNotFoundException ex) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage("Requested resource is not found. Provide existing resource ID.");
        errorDTO.setStatus("Not Found");
        errorDTO.setCode(ex.getStatus().value());

        return new ResponseEntity<>(errorDTO, ex.getStatus());
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<ErrorDTO> generateNotAuthorizedException(NotAuthorizedException ex) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage("User not found. Provide valid OAuth token.");
        errorDTO.setStatus("Unauthorized");
        errorDTO.setCode(ex.getStatus().value());

        return new ResponseEntity<>(errorDTO, ex.getStatus());
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<ErrorDTO> generateServiceNotAvailableException(StorageFileNotFoundException ex) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage("Service currently not available. Try again later.");
        errorDTO.setStatus("Service Unavailable");
        errorDTO.setCode(HttpStatus.SERVICE_UNAVAILABLE.value());

        return new ResponseEntity<>(errorDTO, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(UploadIncompleteException.class)
    public ResponseEntity<StatusDTO> generateUploadIncompleteException(UploadIncompleteException ex) {
        StatusDTO statusDTO = new StatusDTO(ex.getUploadStatus(), ex.getVideoId());

        return new ResponseEntity<>(statusDTO, ex.getHttpStatus());
    }
}

package com.youtube.uploadvideo;

import com.youtube.uploadvideo.pojo.UploadResponse;
import com.youtube.uploadvideo.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class RestApiController {

    @Autowired
    private StorageService storageService;

    private static final String template = "Filename: %s, token: %s";

    @PostMapping("/api/v1/video")
    public UploadResponse greeting(@RequestHeader("Authorization") String token,
                                   @RequestParam("file") MultipartFile file) {
        storageService.store(file);
        return new UploadResponse(200, String.format(template, file.getOriginalFilename(), token));
    }
}

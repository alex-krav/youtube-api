package com.youtube.uploadvideo;

import com.youtube.uploadvideo.model.*;
import com.youtube.uploadvideo.repository.UserRepository;
import com.youtube.uploadvideo.repository.VideoRepository;
import com.youtube.uploadvideo.storage.StorageService;
import com.youtube.uploadvideo.utils.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class RestApiController {

    @Autowired
    private StorageService storageService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VideoRepository videoRepository;

    @PostMapping("/api/v1/video")
    public Object greeting(@RequestHeader("Authorization") String token,
                           @RequestParam("file") MultipartFile file,
                           HttpServletResponse response) {
        User user = userRepository.findByToken(token);
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return new ErrorResponse(401, "Not Authorized");
        }

        storageService.store(file);

        Video video = new Video();
        video.setUser(user);
        video.setVideoId(RandomString.generator().generate(10));
        video.setFileName(file.getOriginalFilename());
        video.setUploadStatus(UploadStatus.IN_PROGRESS.value());
        videoRepository.save(video);

        return new UploadResponse(200, UploadStatus.IN_PROGRESS.value(), video.getVideoId());
    }
}

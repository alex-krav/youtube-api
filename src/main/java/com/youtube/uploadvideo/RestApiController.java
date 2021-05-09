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
    public Object upload(@RequestHeader("Authorization") String token,
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

    @GetMapping("/api/v1/video/{videoId}/status")
    public Object status(@RequestHeader("Authorization") String token,
                           @PathVariable("videoId") String videoId,
                           HttpServletResponse response) {
        User user = userRepository.findByToken(token);
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return new ErrorResponse(401, "Not Authorized");
        }

        Video video = videoRepository.findByVideoId(videoId);
        if (video == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return new ErrorResponse(404, "Not Found");
        }

        if (!video.getUser().equals(user)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return new ErrorResponse(401, "Not Authorized");
        }

        if (UploadStatus.getByValue(video.getUploadStatus()).equals(UploadStatus.IN_PROGRESS)) {
            video.setUploadStatus(UploadStatus.COMPLETE.value());
            videoRepository.save(video);
        }

        return new UploadResponse(200, video.getUploadStatus(), video.getVideoId());
    }
}

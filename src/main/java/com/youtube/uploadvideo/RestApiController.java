package com.youtube.uploadvideo;

import com.youtube.uploadvideo.exception.NotAuthorizedException;
import com.youtube.uploadvideo.exception.VideoNotFoundException;
import com.youtube.uploadvideo.model.StatusDTO;
import com.youtube.uploadvideo.model.UploadStatus;
import com.youtube.uploadvideo.model.User;
import com.youtube.uploadvideo.model.Video;
import com.youtube.uploadvideo.repository.UserRepository;
import com.youtube.uploadvideo.repository.VideoRepository;
import com.youtube.uploadvideo.storage.StorageFileNotFoundException;
import com.youtube.uploadvideo.storage.StorageService;
import com.youtube.uploadvideo.storage.VideoStreamService;
import com.youtube.uploadvideo.utils.RandomString;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/api/v1")
public class RestApiController {

    @Autowired
    private StorageService storageService;
    @Autowired
    private VideoStreamService videoStreamService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VideoRepository videoRepository;

    @PostMapping("/videos")
    public ResponseEntity<StatusDTO> upload(@RequestHeader("Authorization") String token,
                                            @RequestPart("video_file") FilePart file) {
        User user = userRepository.findByToken(token);
        if (user == null) {
            throw new NotAuthorizedException("User not authorized");
        }

        storageService.store(file);

        Video video = new Video();
        video.setUser(user);
        video.setVideoId(RandomString.generator().generate(10));
        video.setFileName(file.filename());
        video.setUploadStatus(UploadStatus.IN_PROGRESS.value());
        videoRepository.save(video);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new StatusDTO(video.getUploadStatus(), video.getVideoId()));
    }

    @GetMapping("/videos/{video-id}/status")
    public ResponseEntity<StatusDTO> status(@RequestHeader("Authorization") String token,
                                            @PathVariable("video-id") String videoId) {
        User user = userRepository.findByToken(token);
        if (user == null) {
            throw new NotAuthorizedException("User not authorized");
        }

        Video video = videoRepository.findByVideoId(videoId);
        if (video == null) {
            throw new VideoNotFoundException("Video not found");
        }

        if (!video.getUser().equals(user)) {
            throw new NotAuthorizedException("User not authorized");
        }

        String uploadStatus = video.getUploadStatus();
        if (UploadStatus.getByValue(uploadStatus).equals(UploadStatus.IN_PROGRESS)) {
            video.setUploadStatus(UploadStatus.COMPLETE.value());
            videoRepository.save(video);
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(new StatusDTO(uploadStatus, video.getVideoId()));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new StatusDTO(uploadStatus, video.getVideoId()));
    }

    @GetMapping("/videos/{video-id}")
    public Mono<ResponseEntity<byte[]>> streamVideo(ServerHttpResponse serverHttpResponse,
                                                    @RequestHeader(value = "Range", required = false) String httpRangeList,
                                                    @PathVariable("video-id") String videoId,
                                                    @RequestParam(value = "height", required = false) String height,
                                                    @RequestParam(value = "time", required = false) String time,
                                                    @RequestParam(value = "speed", required = false) String speed,
                                                    @RequestParam(value = "subtitles", required = false) String subtitles) {
        Video video = videoRepository.findByVideoId(videoId);
        if (video == null) {
            throw new VideoNotFoundException("Video not found");
        }

        String uploadStatus = video.getUploadStatus();
        if (UploadStatus.getByValue(uploadStatus).equals(UploadStatus.IN_PROGRESS)) {
            video.setUploadStatus(UploadStatus.COMPLETE.value());
            videoRepository.save(video);
            throw new StorageFileNotFoundException("File not available");
        }

        RetryPolicy<Resource> retryPolicy = new RetryPolicy<>();
        retryPolicy.withMaxRetries(3);
        retryPolicy.withDelay(Duration.ofSeconds(5));
        retryPolicy.handle(StorageFileNotFoundException.class);
        Resource resource = Failsafe.with(retryPolicy).get(() -> storageService.loadAsResource(video.getFileName()));

        String resolvedFileName = storageService.resolvedFilename(video.getFileName());
        String mediaType = MediaTypeFactory.getMediaType(resource).get().toString();

        return Mono.just(videoStreamService.prepareContent(resolvedFileName, mediaType, httpRangeList));
    }
}

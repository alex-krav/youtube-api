package com.youtube.uploadvideo.repository;

import com.youtube.uploadvideo.model.Video;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface VideoRepository extends Repository<Video, Integer> {

    @Query("SELECT video FROM Video video WHERE video.videoId =:videoId")
    @Transactional(readOnly = true)
    Video findByVideoId(@Param("videoId") String videoId);

    void save(Video video);
}

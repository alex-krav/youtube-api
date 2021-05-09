package com.youtube.uploadvideo.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(name = "channel_id")
    @NotEmpty
    private String channel;

    @Column(name = "token")
    @NotEmpty
    private String token;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Video> videos;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private List<Video> getVideosInternal() {
        if (this.videos == null) {
            this.videos = new ArrayList<>();
        }
        return this.videos;
    }

    private void setVideosInternal(List<Video> videos) {
        this.videos = videos;
    }

    public List<Video> getVideos() {
        return Collections.unmodifiableList(getVideosInternal());
    }

    public void addVideo(Video video) {
        getVideosInternal().add(video);
        video.setUser(this);
    }

    public Video getVideo(String videoId) {
        for (Video video : getVideosInternal()) {
            if (video.getVideoId().equals(videoId)) {
                return video;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getId().equals(user.getId());
    }
}

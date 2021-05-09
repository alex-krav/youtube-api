package com.youtube.uploadvideo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StatusDTO {

    private final String status;
    private final String videoId;
}

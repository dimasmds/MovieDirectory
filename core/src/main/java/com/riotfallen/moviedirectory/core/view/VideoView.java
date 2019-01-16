package com.riotfallen.moviedirectory.core.view;

import com.riotfallen.moviedirectory.core.model.video.Video;

public interface VideoView {
    void showVideoData(Video video);
    void showVideoNotFound(String message);
    void showVideoError(String message);
}

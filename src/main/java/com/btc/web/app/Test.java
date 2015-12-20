package com.btc.web.app;

import com.btc.web.service.YouTubeService;

/**
 * Created by Chris on 12/5/15.
 */
public class Test {

    private static final String SAMPLE_VIDEO_FILENAME = "sample-video.mp4";

    public static void main(String[] args) {
        System.out.println("Uploading: " + SAMPLE_VIDEO_FILENAME);
        YouTubeService youTubeService = new YouTubeService();
        youTubeService.uploadVideo(Test.class.getResourceAsStream("/sample-video.mp4"));

    }
}
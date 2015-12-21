package com.btc.web.service;

import lombok.Data;

/**
 * Created by Chris on 12/20/15.
 */
@Data
public class WidgetFeedReturn {
    int playlistCount;
    String videoTitle;
    String videoJoke;
    String videoThumbnail;
    String videoId;
    String id;
    boolean success;
    String message;
}
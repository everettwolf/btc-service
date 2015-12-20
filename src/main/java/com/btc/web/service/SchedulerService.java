package com.btc.web.service;

import com.btc.web.web.WebServiceController;
import org.quartz.DisallowConcurrentExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Created by Chris on 12/19/15.
 */
@Service
@DisallowConcurrentExecution
public class SchedulerService {

    @Value("${playlist.widget}")
    private String playlistWidget;

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private WebServiceController ws;

    private final static Logger logger = LoggerFactory.getLogger(SchedulerService.class);

    @Scheduled(cron = "0 0 0 1/1 * ? *") //Midnight daily
    public void updateWidget() throws Exception {

        if (playlistService.insertPlaylistItem(playlistWidget, playlistService.getVideoIdFromWidgetFeed())) {
            ws.updatePlaylists();
        }

        logger.info("Widget updated");
    }
}

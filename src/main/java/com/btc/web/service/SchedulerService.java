package com.btc.web.service;

import com.btc.web.notification.EmailAlertNotification;
import com.btc.web.web.WebServiceController;
import com.google.common.collect.Maps;
import org.quartz.DisallowConcurrentExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by Chris on 12/19/15.
 */
@Service
@DisallowConcurrentExecution
public class SchedulerService {

    @Value("${playlist.widget}")
    private String playlistWidget;

    @Value("${scheduler.enabled}")
    private boolean schedulerEnabled;

    @Value("${scheduler.email.enabled}")
    private boolean schedulerEmailEnabled;

    @Value("${scheduler.cron}")
    private String cronSchedule;

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private WebServiceController ws;

    @Autowired
    private EmailAlertNotification emailAlertNotification;

    private final static Logger logger = LoggerFactory.getLogger(SchedulerService.class);

    @Scheduled(cron = "${scheduler.cron}")
    public void updateWidget() throws Exception {
        if (schedulerEnabled) {

            WidgetFeedReturn widgetFeedReturn = playlistService.getWidgetFeedInfo();
            if (widgetFeedReturn.getPlaylistCount() > 0
                    && playlistService.insertPlaylistItem(playlistWidget, widgetFeedReturn.getVideoId())
                    && playlistService.deletePlaylistItem(widgetFeedReturn.getId())) {
                //ws.updateAllPlaylists();
                ws.updatePlaylist(playlistWidget);
                widgetFeedReturn.setSuccess(true);
                widgetFeedReturn.setMessage(new StringBuilder()
                        .append("Widget updated with '")
                        .append(widgetFeedReturn.getVideoTitle())
                        .append("', '")
                        .append(widgetFeedReturn.getVideoJoke())
                        .append("'<br>")
                        .append("There are ")
                        .append(widgetFeedReturn.getPlaylistCount() - 1)
                        .append(" videos left to draw from.").toString());
                logger.info("Widget updated");
            } else {
                widgetFeedReturn.setSuccess(false);
                widgetFeedReturn.setMessage("WIDGET WAS NOT UPDATED, PLEASE CHECK YOUR FEED");
                logger.error("Widget was not updated");
            }
            sendWidgetUpdateAlert(widgetFeedReturn);
        }
    }

    private void sendWidgetUpdateAlert(WidgetFeedReturn widgetFeedReturn) throws Exception {

        if (!schedulerEmailEnabled) return;

        Map<String, Object> model = Maps.newHashMap();

        model.put("alertMessage", widgetFeedReturn.getMessage());

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm a");
        model.put("alertDate", sdf.format(new Date()));

        SimpleDateFormat day = new SimpleDateFormat("MM/dd/yyyy");
        model.put("day", day.format(new Date()));
        model.put("serviceName", "Beyond the Comics");
        model.put("alertTitle", "Beyond the Comics Alert");
        model.put("footerMessage", "THIS IS AN AUTOMATED MAIL MESSAGE. PLEASE DO NOT REPLY.");

        if (widgetFeedReturn.isSuccess()) {
            model.put("videoThumbnail", widgetFeedReturn.getVideoThumbnail());
        }

        Map<String, String> imageResources = Maps.newHashMap();
        imageResources.put("btc_logo", "btc_logo.gif");


        try {
            emailAlertNotification.send(model, null, imageResources);
        } catch (MessagingException e) {
            logger.error(e.getMessage());
        }
    }
}

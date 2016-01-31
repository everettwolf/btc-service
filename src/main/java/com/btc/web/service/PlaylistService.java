package com.btc.web.service;

import com.btc.web.model.Playlist;
import com.btc.web.model.repository.PlaylistRepository;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Chris on 11/14/15.
 */
@Service
public class PlaylistService {

    private static Logger logger = LoggerFactory.getLogger(PlaylistService.class);

    @Autowired
    private PlaylistRepository playlistRepository;

    private YouTube youtube;

    @Value("${playlist.widget_feed}")
    private String playlistWidgetFeed;

    @Value("${yt.key}")
    private String youTubeKey;

    @Value("${yt.channel}")
    private String youTubeChannel;

    @Data
    public class TalentAndDate {
        String talent = "Unknown";
        String date = "1/1/1900";
    }

    @PostConstruct
    private void init() throws Exception {
        List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube");

        logger.info("Getting the YouTube Authorization credentials.");
        Credential credential = YouTubeAuth.authorize(scopes, "playlistupdates");
        youtube = new YouTube
                .Builder(YouTubeAuth.HTTP_TRANSPORT, YouTubeAuth.JSON_FACTORY, credential)
                .setApplicationName("Beyond the Comics")
                .build();
        logger.info("Successfully retrieved the YouTube Authorization credentials.");
    }

    @Cacheable(value = "playlists", key = "#playlistId")
    public Playlist findByPlaylistId(String playlistId) {
        logger.info("Playlist cached: {}", playlistId);
        Playlist playlist = playlistRepository.findByPlaylistId(playlistId);
        return playlist;
    }

    @CacheEvict(value = "playlists", key = "#playlist.playlistId")
    public Playlist save(Playlist playlist) {
        logger.info("Playlist evicted: {}", playlist.getPlaylistId());
        return playlistRepository.save(playlist);
    }

    @CacheEvict(value = "playlists", allEntries = true)
    public void evictAllPlaylists() {
        logger.info("All playlists evicted.");
    }

    @CacheEvict(value = "playlists", key = "#playlistId")
    public void evictPlaylist(String playlistId) {
        logger.info("Playlist {} evicted", playlistId);
    }

    public void deleteByPlaylistId(String playlistId) {
        playlistRepository.deleteByPlaylistId(playlistId);
    }

    public boolean insertPlaylistItem(String playlistId, String videoId) throws Exception {

        try {

            YouTube.Videos.List listVideosRequest = youtube.videos().list("snippet").setId(videoId);
            VideoListResponse videoListResponse = listVideosRequest.execute();

            List<Video> videoList = videoListResponse.getItems();
            if (videoList.isEmpty()) {
                logger.error("Can't find a video with ID {}", videoId);
                return false;
            }
            Video video = videoList.get(0);

            VideoRecordingDetails videoRecordingDetails = video.getRecordingDetails();
            if (videoRecordingDetails == null) {
                videoRecordingDetails = new VideoRecordingDetails();
            }
            Date date = Calendar.getInstance().getTime();

            videoRecordingDetails.setRecordingDate(new DateTime(Calendar.getInstance().getTime(), TimeZone.getTimeZone("America/Los_Angeles")));
            video.setRecordingDetails(videoRecordingDetails);

            YouTube.Videos.Update updateVideosRequest = youtube.videos().update("snippet,recordingDetails", video);
            Video videoResponse = updateVideosRequest.execute();
            logger.info("Video date set to: {}", videoResponse.getRecordingDetails().getRecordingDate());

            ResourceId resourceId = new ResourceId();
            resourceId.setKind("youtube#video");
            resourceId.setVideoId(videoId);

            PlaylistItemSnippet playlistItemSnippet = new PlaylistItemSnippet();
            playlistItemSnippet.setTitle("First video in the test playlist");
            playlistItemSnippet.setPlaylistId(playlistId);
            playlistItemSnippet.setResourceId(resourceId);

            PlaylistItem playlistItem = new PlaylistItem();
            playlistItem.setSnippet(playlistItemSnippet);

            YouTube.PlaylistItems.Insert playlistItemsInsertCommand = youtube.playlistItems().insert("snippet,contentDetails", playlistItem);
            PlaylistItem returnedPlaylistItem = playlistItemsInsertCommand.execute();

            logger.info("New widget item: {}", returnedPlaylistItem.getSnippet().getTitle());

            return true;
        } catch (Exception e) {
            logger.error("Error updating widget {}", e.getMessage());

            return false;
        }
    }

    public boolean deletePlaylistItem(String id) throws Exception {

        try {
            YouTube.PlaylistItems.Delete playlistItemDeleteCommand = youtube.playlistItems().delete(id);
            playlistItemDeleteCommand.execute();

            logger.info("Deleted playlist item: {}", id);

            return true;
        } catch (Exception e) {
            logger.error("Error updating widget {}", e.getMessage());

            return false;
        }
    }

    public WidgetFeedReturn getWidgetFeedInfo() {

        WidgetFeedReturn widgetFeedReturn = new WidgetFeedReturn();
        JsonArray widgetFeedJsonArray = getPlaylistItemsByPlaylistId(playlistWidgetFeed)
                .getAsJsonArray("items");

        widgetFeedReturn.setPlaylistCount(widgetFeedJsonArray.size());

        if (widgetFeedJsonArray.size() > 0) {
            widgetFeedReturn.setId(widgetFeedJsonArray
                    .get(0)
                    .getAsJsonObject()
                    .get("id")
                    .getAsString());
            JsonObject snippet = widgetFeedJsonArray
                    .get(0)
                    .getAsJsonObject()
                    .get("snippet")
                    .getAsJsonObject();
            widgetFeedReturn.setVideoId(snippet
                    .get("resourceId")
                    .getAsJsonObject()
                    .get("videoId")
                    .getAsString());
            widgetFeedReturn.setVideoTitle(snippet
                    .get("title")
                    .getAsString());
            widgetFeedReturn.setVideoJoke(snippet
                    .get("description")
                    .getAsString());
            widgetFeedReturn.setVideoThumbnail(snippet
                    .get("thumbnails")
                    .getAsJsonObject()
                    .get("medium")
                    .getAsJsonObject()
                    .get("url")
                    .getAsString());
        }
        return widgetFeedReturn;
    }

    public JsonObject getAllPlaylistsForChannel() {

        String uri = "https://www.googleapis.com/youtube/v3/playlists?part=snippet&channelId=" + youTubeChannel + "&maxResults=50&key=" + youTubeKey;
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);

        return new JsonParser().parse(result).getAsJsonObject();
    }

    public JsonObject getPlaylistItemsByPlaylistId(String gridId) {

        String uri = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=50&playlistId=" + gridId + "&key=" + youTubeKey;
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);

        return new JsonParser().parse(result).getAsJsonObject();
    }

    public TalentAndDate getTalentAndDateByVideoId(String videoId) {

        String uri = "https://www.googleapis.com/youtube/v3/videos?part=snippet,recordingDetails&id=" + videoId + "&maxResults=50&key=" + youTubeKey;
        RestTemplate restTemplate = new RestTemplate();

        try {
            String result = restTemplate.getForObject(uri, String.class);

            JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
            JsonArray jsonArray = jsonObject.getAsJsonArray("items");

            TalentAndDate talentAndDate = new TalentAndDate();
            talentAndDate.setTalent(jsonArray.get(0).getAsJsonObject()
                    .get("snippet")
                    .getAsJsonObject()
                    .get("tags")
                    .getAsJsonArray()
                    .get(0)
                    .getAsString());
            talentAndDate.setDate(jsonArray.get(0).getAsJsonObject()
                    .get("recordingDetails")
                    .getAsJsonObject()
                    .get("recordingDate")
                    .getAsString());

            return talentAndDate;

        } catch (Exception e) {
            return new TalentAndDate();
        }
    }

}

package com.btc.web.service;

import com.btc.web.model.Playlist;
import com.btc.web.model.repository.PlaylistRepository;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemSnippet;
import com.google.api.services.youtube.model.ResourceId;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;

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

    @PostConstruct
    private void init() throws Exception {
        List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube");

        Credential credential = Auth.authorize(scopes, "playlistupdates");
        youtube = new YouTube
                .Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential)
                .setApplicationName("Beyond the Comics")
                .build();
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

    public void deleteByPlaylistId(String playlistId) {
        playlistRepository.deleteByPlaylistId(playlistId);
    }

    public boolean insertPlaylistItem(String playlistId, String videoId) throws Exception {

        try {
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

    public String getTalentByVideoId(String videoId) {

        String uri = "https://www.googleapis.com/youtube/v3/videos?part=snippet&id=" + videoId + "&maxResults=50&key=" + youTubeKey;
        RestTemplate restTemplate = new RestTemplate();

        try {
            String result = restTemplate.getForObject(uri, String.class);

            JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
            JsonArray jsonArray = jsonObject.getAsJsonArray("items");

            return jsonArray.get(0).getAsJsonObject()
                    .get("snippet")
                    .getAsJsonObject()
                    .get("tags")
                    .getAsJsonArray()
                    .get(0)
                    .getAsString();
        } catch (Exception e) {
            return "Unknown";
        }
    }

}

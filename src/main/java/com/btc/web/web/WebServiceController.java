package com.btc.web.web;

import com.btc.web.model.ComicsGrid;
import com.btc.web.model.Playlist;
import com.btc.web.service.PlaylistService;
import com.btc.web.service.SchedulerService;
import com.btc.web.service.TemplateService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mobile.device.Device;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

//import it.sauronsoftware.jave.*;


/**
 * Created by Chris on 1/8/15.
 */
@RequestMapping("/ws")
@RestController
public class WebServiceController {

    private static Logger logger = LoggerFactory.getLogger(WebServiceController.class);

    @JsonAutoDetect(fieldVisibility = ANY)
    static class DataBean {

        String json;

        public DataBean(String str) {
            this.json = str;
        }
    }

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private SchedulerService schedulerService;

    @RequestMapping(value = "updateWidget", produces = MediaType.APPLICATION_JSON_VALUE)
    public DataBean updateWidget() {
        try {
            schedulerService.updateWidget();
            return new DataBean("{'updated':'true'}");
        } catch (Exception e) {
            return new DataBean("{'updated': 'false', 'error': " + e.getMessage() + "}");
        }
    }

    @RequestMapping(value = "evictAllPlaylists", produces = MediaType.APPLICATION_JSON_VALUE)
    public DataBean evictAllPlaylists() {
        playlistService.evictAllPlaylists();
        return new DataBean("{'evicted':'true'}");
    }

    @RequestMapping(value = "evictPlaylist/{playlistId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public DataBean evictPlaylist(@PathVariable(value = "playlistId") String playlistId) {
        playlistService.evictPlaylist(playlistId);
        return new DataBean("{'evicted':'true', 'playlist' : " + playlistId + "}");
    }

    @RequestMapping(value = "getTemplate", produces = MediaType.APPLICATION_JSON_VALUE)
    public DataBean getTemplate(@RequestParam("templateType") TemplateService.TemplateType templateType) throws Exception {
        return new DataBean(templateService.getTemplate(templateType));
    }

    @RequestMapping(value = "getPlaylistProps/{playlistId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DataBean getPlaylistProps(@PathVariable(value = "playlistId") String playlistId) throws Exception {
        return new DataBean(playlistService.findByPlaylistId(playlistId).getPlaylistJSON());
    }

    @Transactional
    @RequestMapping(value = "updatePlaylist/{playlistId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void updatePlaylist(@PathVariable(value = "playlistId") String playlistId) throws Exception {

        BiMap<String, String> playlistMap = getPlaylistMap();

        List<ComicsGrid> comicsGridList = new ArrayList<>();
        JsonObject jsonObject = playlistService.getPlaylistItemsByPlaylistId(playlistId);
        JsonArray jsonArray = jsonObject.getAsJsonArray("items");

        for (int j = 0; j < jsonArray.size(); j++) {

            ComicsGrid comicsGrid = new ComicsGrid();

            JsonObject item = jsonArray.get(j).getAsJsonObject();

            logger.info("Grid item: {}", item.toString());

            JsonObject snippet = item.get("snippet").getAsJsonObject();

            String comic = snippet.get("description").getAsString();
            String joke = snippet.get("title").getAsString();
            String publishedAt = snippet.get("publishedAt").getAsString();

            publishedAt = Date.from(ZonedDateTime.parse(publishedAt).toInstant()).toString();

            String thumb = snippet.get("thumbnails")
                    .getAsJsonObject()
                    .get("medium")
                    .getAsJsonObject()
                    .get("url").getAsString();

            String videoId = snippet.get("resourceId")
                    .getAsJsonObject()
                    .get("videoId")
                    .getAsString();

            PlaylistService.TalentAndDate talentAndDate = playlistService.getTalentAndDateByVideoId(videoId);

            comicsGrid.setPlaylistId(playlistMap.get(comic));
            comicsGrid.setComic(comic);
            comicsGrid.setJoke(joke);
            comicsGrid.setThumb(thumb);
            comicsGrid.setVideoId(videoId);
            comicsGrid.setTalent(talentAndDate.getTalent());
            comicsGrid.setPublishedAt(talentAndDate.getDate() == null ? publishedAt : talentAndDate.getDate());
            comicsGridList.add(comicsGrid);
        }

        Playlist playlist = playlistService.findByPlaylistId(playlistId);
        if (playlist != null) playlistService.deleteByPlaylistId(playlistId);

        playlist = new Playlist();
        playlist.setPlaylistId(playlistId);
        playlist.setPlaylistName(playlistMap.inverse().get(playlistId));
        playlist.setPlaylistJSON(new Gson().toJson(comicsGridList));
        playlistService.save(playlist);
    }

    @Transactional
    @RequestMapping(value = "updateAllPlaylists", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateAllPlaylists() throws Exception {

        Map<String, String> playlistMap = getPlaylistMap();

        for (Map.Entry<String, String> entry : playlistMap.entrySet()) {
            updatePlaylist(entry.getValue());
        }

    }

    private BiMap<String, String> getPlaylistMap() {

        BiMap<String, String> playlistMap = HashBiMap.create();
        JsonObject playlistJsonObject = playlistService.getAllPlaylistsForChannel();
        JsonArray playlistJsonArray = playlistJsonObject.getAsJsonArray("items");

        for (int i = 0; i < playlistJsonArray.size(); i++) {
            JsonObject playlistItem = playlistJsonArray
                    .get(i)
                    .getAsJsonObject();

            playlistMap.put(
                    playlistItem
                            .get("snippet")
                            .getAsJsonObject()
                            .get("title")
                            .getAsString(),
                    playlistItem
                            .get("id")
                            .getAsString()

            );
        }
        return playlistMap;
    }

    @RequestMapping("/detect-device")
    public
    @ResponseBody
    String detectDevice(Device device) {
        String deviceType = "unknown";
        if (device.isNormal()) {
            deviceType = "normal";
        } else if (device.isMobile()) {
            deviceType = "mobile";
        } else if (device.isTablet()) {
            deviceType = "tablet";
        }
        return "Hello " + deviceType + " browser!";
    }

}

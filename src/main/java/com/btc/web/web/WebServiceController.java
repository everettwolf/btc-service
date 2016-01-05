package com.btc.web.web;

import com.btc.web.model.ComicsGrid;
import com.btc.web.model.Playlist;
import com.btc.web.service.PlaylistService;
import com.btc.web.service.TemplateService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
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

    @RequestMapping(value = "getTemplate", produces = MediaType.APPLICATION_JSON_VALUE)
    public DataBean getTemplate(@RequestParam("templateType") TemplateService.TemplateType templateType) throws Exception {
        return new DataBean(templateService.getTemplate(templateType));
    }

    @RequestMapping(value = "getPlaylistProps/{playlistId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DataBean getPlaylistProps(@PathVariable(value = "playlistId") String playlistId) throws Exception {
        return new DataBean(playlistService.findByPlaylistId(playlistId).getPlaylistJSON());
    }

    @Transactional
    @RequestMapping(value = "updatePlaylists", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void updatePlaylists() throws Exception {

        List<ComicsGrid> comicsGridList;
        Map<String, String> playlistMap = new HashMap<>();
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

        for (Map.Entry<String, String> entry : playlistMap.entrySet()) {

            comicsGridList = new ArrayList<>();
            JsonObject jsonObject = playlistService.getPlaylistItemsByPlaylistId(entry.getValue());
            JsonArray jsonArray = jsonObject.getAsJsonArray("items");

            for (int j = 0; j < jsonArray.size(); j++) {

                ComicsGrid comicsGrid = new ComicsGrid();

                JsonObject item = jsonArray.get(j).getAsJsonObject();

                logger.info("Grid item: {}", item.toString());

                JsonObject snippet = item.get("snippet").getAsJsonObject();

                String comic = snippet.get("title").getAsString();
                String joke = snippet.get("description").getAsString();

                String thumb = snippet.get("thumbnails")
                        .getAsJsonObject()
                        .get("medium")
                        .getAsJsonObject()
                        .get("url").getAsString();

                String videoId = snippet.get("resourceId")
                        .getAsJsonObject()
                        .get("videoId")
                        .getAsString();

                String talent = playlistService.getTalentByVideoId(videoId);

                comicsGrid.setPlaylistId(playlistMap.get(comic));
                comicsGrid.setComic(comic);
                comicsGrid.setJoke(joke);
                comicsGrid.setThumb(thumb);
                comicsGrid.setVideoId(videoId);
                comicsGrid.setTalent(talent);
                comicsGridList.add(comicsGrid);
            }

            Playlist playlist = playlistService.findByPlaylistId(entry.getValue());
            if (playlist != null) playlistService.deleteByPlaylistId(entry.getValue());

            playlist = new Playlist();
            playlist.setPlaylistId(entry.getValue());
            playlist.setPlaylistName(entry.getKey());
            playlist.setPlaylistJSON(new Gson().toJson(comicsGridList));
            playlistService.save(playlist);

        }

    }

}

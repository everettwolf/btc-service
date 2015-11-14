package com.btc.web.web;

import com.btc.web.model.ComicsGrid;
import com.btc.web.model.Playlist;
import com.btc.web.service.PlaylistService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.sauronsoftware.jave.*;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;


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

     @RequestMapping(value = "/uploadVideo", method = RequestMethod.POST, consumes = {"multipart/*"}, produces = MediaType.APPLICATION_JSON_VALUE)
     public String uploadVideo(@RequestPart("video") MultipartFile video) {

          try {
               File sourceFile = convert(video);
               Encoder encoder = new Encoder();
               MultimediaInfo multimediaInfo = encoder.getInfo(sourceFile);
               VideoInfo videoInfo = multimediaInfo.getVideo();

               String format = multimediaInfo.getFormat();
               AudioInfo audioInfo = multimediaInfo.getAudio();


               File targetFile = new File("/Users/Chris/beesting.mp4");
               AudioAttributes audioAttributes = new AudioAttributes();
               audioAttributes.setCodec("libvorbis");
               VideoAttributes videoAttributes = new VideoAttributes();
               videoAttributes.setCodec("mpeg4");
               videoAttributes.setTag("DIVX");
               videoAttributes.setBitRate(new Integer(160000));
               videoAttributes.setFrameRate(new Integer(30));
               EncodingAttributes attrs = new EncodingAttributes();
               attrs.setFormat("mpegvideo");
               attrs.setAudioAttributes(audioAttributes);
               attrs.setVideoAttributes(videoAttributes);
               encoder.encode(sourceFile, targetFile, attrs);
               return "good";
          } catch (Exception e) {
               // throw (new RuntimeException("problem uploading file"));
               return "bad";
          }

     }

     @RequestMapping(value = "getGridTemplate", produces = MediaType.APPLICATION_JSON_VALUE)
     public DataBean getGridTemplate() throws Exception {

          Resource headerTemplate = new ClassPathResource("grid_assets/header.html");
          StringWriter header = new StringWriter();
          IOUtils.copy(headerTemplate.getInputStream(), header, Charsets.UTF_8);

          Resource gridTemplate = new ClassPathResource("grid_assets/grid_template.html");
          StringWriter content = new StringWriter();
          IOUtils.copy(gridTemplate.getInputStream(), content, Charsets.UTF_8);

          Resource footerTemplate = new ClassPathResource("grid_assets/footer.html");
          StringWriter footer = new StringWriter();
          IOUtils.copy(footerTemplate.getInputStream(), footer, Charsets.UTF_8);

          String returnJson = header.toString() + content.toString() + footer.toString();
          return new DataBean(returnJson);
     }

     @RequestMapping(value = "getPlayerTemplate", produces = MediaType.APPLICATION_JSON_VALUE)
     public DataBean getPlayerTemplate() throws Exception {

          Resource headerTemplate = new ClassPathResource("grid_assets/header.html");
          StringWriter header = new StringWriter();
          IOUtils.copy(headerTemplate.getInputStream(), header, Charsets.UTF_8);

          Resource playerTemplate = new ClassPathResource("grid_assets/player_template.html");
          StringWriter content = new StringWriter();
          IOUtils.copy(playerTemplate.getInputStream(), content, Charsets.UTF_8);

          Resource footerTemplate = new ClassPathResource("grid_assets/footer.html");
          StringWriter footer = new StringWriter();
          IOUtils.copy(footerTemplate.getInputStream(), footer, Charsets.UTF_8);

          String returnJson = header.toString() + content.toString() + footer.toString();
          return new DataBean(returnJson);
     }

     @RequestMapping(value = "getGridJson", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
     public DataBean getGridJson() throws Exception {

          return new DataBean(playlistService.findByPlaylistId("PLLVtQiMiCJeG_tNuuHw_3ACvXohGh-XGv").getPlaylistJSON());
     }

     @RequestMapping(value = "getFoo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
     public void getFoo() throws Exception {

          List<ComicsGrid> comicsGridList;
          Map<String, String> playlistMap = new HashMap<>();
          JsonObject playlistJsonObject = getAllPlaylistsForChannel();
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
               JsonObject jsonObject = getPlaylistItemsByPlaylistId(entry.getValue());
               JsonArray jsonArray = jsonObject.getAsJsonArray("items");

               for (int j = 0; j < jsonArray.size(); j++) {

                    ComicsGrid comicsGrid = new ComicsGrid();

                    JsonObject item = jsonArray.get(j).getAsJsonObject();

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

                    String talent = getTalentByVideoId(videoId);

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

     private JsonObject getAllPlaylistsForChannel() {

          String uri = "https://www.googleapis.com/youtube/v3/playlists?part=snippet&channelId=UCrw5nc-6MP632Mx_-3_O4OQ&maxResults=50&key=AIzaSyDCloQetmWP3BgQ7q-1m9K_hEVdyY2qorw";
          RestTemplate restTemplate = new RestTemplate();
          String result = restTemplate.getForObject(uri, String.class);

          return new JsonParser().parse(result).getAsJsonObject();
     }

     private JsonObject getPlaylistItemsByPlaylistId(String gridId) {
          String uri = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=50&playlistId=" + gridId + "&key=AIzaSyDCloQetmWP3BgQ7q-1m9K_hEVdyY2qorw";
          RestTemplate restTemplate = new RestTemplate();
          String result = restTemplate.getForObject(uri, String.class);

          return new JsonParser().parse(result).getAsJsonObject();
     }

     private String getTalentByVideoId(String videoId) {
          String uri = "https://www.googleapis.com/youtube/v3/videos?part=snippet&id=" + videoId + "&maxResults=50&key=AIzaSyDCloQetmWP3BgQ7q-1m9K_hEVdyY2qorw";
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

     private File convert(MultipartFile file) throws Exception {
          File convFile = new File(file.getOriginalFilename());
          convFile.createNewFile();
          FileOutputStream fos = new FileOutputStream(convFile);
          fos.write(file.getBytes());
          fos.close();
          return convFile;
     }

}

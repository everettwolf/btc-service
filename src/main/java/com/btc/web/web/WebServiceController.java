package com.btc.web.web;

import com.btc.web.model.ComicsGrid;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.deploy.util.StringUtils;
import it.sauronsoftware.jave.*;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

          Resource gridJson = new ClassPathResource("grid_assets/you_tube_grid.json");
          StringWriter writer = new StringWriter();
          IOUtils.copy(gridJson.getInputStream(), writer, Charsets.UTF_8);
          String returnJson = writer.toString();
          return new DataBean(returnJson);
     }

     @RequestMapping(value = "getFoo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
     public List<ComicsGrid> getFoo() throws Exception {

          String gridId = "";
          List<String> videoIds = new ArrayList<>();
          Map<String, String> playlists = new HashMap<>();
          List<ComicsGrid> comicsGridList = new ArrayList<>();

          String uri;
          RestTemplate restTemplate;
          String result;
          JsonObject jsonObject;
          JsonArray jsonArray;

          uri = "https://www.googleapis.com/youtube/v3/playlists?part=snippet&channelId=UCrw5nc-6MP632Mx_-3_O4OQ&maxResults=50&key=AIzaSyDCloQetmWP3BgQ7q-1m9K_hEVdyY2qorw";
          restTemplate = new RestTemplate();
          result = restTemplate.getForObject(uri, String.class);

          jsonObject = new JsonParser().parse(result).getAsJsonObject();
          jsonArray = jsonObject.getAsJsonArray("items");

          for (int i = 0; i < jsonArray.size(); i++) {
               JsonObject item = jsonArray.get(i).getAsJsonObject();
               String playlistId = item.get("id").getAsString();

               JsonObject snippet = item.get("snippet").getAsJsonObject();
               String playlistTitle = snippet.get("title").getAsString();
               if (playlistTitle.toLowerCase().equals("grid")) gridId = playlistId;

               playlists.put(playlistTitle, playlistId);

          }

          System.out.println(gridId);
          System.out.println(playlists);

          uri = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=50&playlistId=" + gridId + "&key=AIzaSyDCloQetmWP3BgQ7q-1m9K_hEVdyY2qorw";
          restTemplate = new RestTemplate();
          result = restTemplate.getForObject(uri, String.class);

          jsonObject = new JsonParser().parse(result).getAsJsonObject();
          jsonArray = jsonObject.getAsJsonArray("items");

          for (int i = 0; i < jsonArray.size(); i++) {

               ComicsGrid comicsGrid = new ComicsGrid();

               JsonObject item = jsonArray.get(i).getAsJsonObject();

               JsonObject snippet = item.get("snippet").getAsJsonObject();

               String playlistId = playlists.get(snippet.get("title").getAsString());
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

               videoIds.add(videoId);

               comicsGrid.setPlaylistId(playlistId);
               comicsGrid.setComic(comic);
               comicsGrid.setJoke(joke);
               comicsGrid.setThumb(thumb);
               comicsGrid.setVideoId(videoId);
               comicsGridList.add(comicsGrid);
               System.out.println(playlistId);
               System.out.println(comic);
               System.out.println(joke);
               System.out.println(thumb);
               System.out.println(videoId + "\n\n");

          }

          uri = "https://www.googleapis.com/youtube/v3/videos?part=snippet&id=" + StringUtils.join(videoIds, ",") + "&maxResults=50&key=AIzaSyDCloQetmWP3BgQ7q-1m9K_hEVdyY2qorw";
          restTemplate = new RestTemplate();
          result = restTemplate.getForObject(uri, String.class);

          jsonObject = new JsonParser().parse(result).getAsJsonObject();
          jsonArray = jsonObject.getAsJsonArray("items");

          for (int i = 0; i < jsonArray.size(); i++) {
               String videoId = jsonArray.get(i).getAsJsonObject()
                    .get("id")
                    .getAsString();

               String talent = jsonArray.get(i).getAsJsonObject()
                    .get("snippet")
                    .getAsJsonObject()
                    .get("tags")
                    .getAsJsonArray()
                    .get(0)
                    .getAsString();

               for (ComicsGrid comicsGrid : comicsGridList) {
                    if (comicsGrid.getVideoId().equals(videoId)) {
                         comicsGrid.setTalent(talent);
                         break;
                    }

               }
          }

          return comicsGridList;
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

//package com.btc.web.app;
//
//import com.btc.web.model.ComicsGrid;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import com.sun.deploy.util.StringUtils;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by Chris on 11/8/15.
// */
//public class YouTube {
//     public static void main(String[] args) {
//
//          String gridId = "";
//          List<String> videoIds = new ArrayList<>();
//          Map<String, String> playlists = new HashMap<>();
//          List<ComicsGrid> comicsGridList = new ArrayList<>();
//
//          String uri;
//          RestTemplate restTemplate;
//          String result;
//          JsonObject jsonObject;
//          JsonArray jsonArray;
//
//          uri = "https://www.googleapis.com/youtube/v3/playlists?part=snippet&channelId=UCrw5nc-6MP632Mx_-3_O4OQ&maxResults=50&key=AIzaSyDCloQetmWP3BgQ7q-1m9K_hEVdyY2qorw";
//          restTemplate = new RestTemplate();
//          result = restTemplate.getForObject(uri, String.class);
//
//          jsonObject = new JsonParser().parse(result).getAsJsonObject();
//          jsonArray = jsonObject.getAsJsonArray("items");
//
//          for (int i = 0; i < jsonArray.size(); i++) {
//               JsonObject item = jsonArray.get(i).getAsJsonObject();
//               String playlistId = item.get("id").getAsString();
//
//               JsonObject snippet = item.get("snippet").getAsJsonObject();
//               String playlistTitle = snippet.get("title").getAsString();
//               if (playlistTitle.toLowerCase().equals("grid")) gridId = playlistId;
//
//               playlists.put(playlistTitle, playlistId);
//
//          }
//
//          System.out.println(gridId);
//          System.out.println(playlists);
//
//          uri = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=50&playlistId=" + gridId + "&key=AIzaSyDCloQetmWP3BgQ7q-1m9K_hEVdyY2qorw";
//          restTemplate = new RestTemplate();
//          result = restTemplate.getForObject(uri, String.class);
//
//          jsonObject = new JsonParser().parse(result).getAsJsonObject();
//          jsonArray = jsonObject.getAsJsonArray("items");
//
//          for (int i = 0; i < jsonArray.size(); i++) {
//
//               ComicsGrid comicsGrid = new ComicsGrid();
//
//               JsonObject item = jsonArray.get(i).getAsJsonObject();
//
//               JsonObject snippet = item.get("snippet").getAsJsonObject();
//
//               String playlistId = playlists.get(snippet.get("title").getAsString());
//               String comic = snippet.get("title").getAsString();
//               String joke = snippet.get("description").getAsString();
//
//               String thumb = snippet.get("thumbnails")
//                    .getAsJsonObject()
//                    .get("medium")
//                    .getAsJsonObject()
//                    .get("url").getAsString();
//
//               String videoId = snippet.get("resourceId")
//                    .getAsJsonObject()
//                    .get("videoId")
//                    .getAsString();
//
//               videoIds.add(videoId);
//
//               comicsGrid.setPlaylistId(playlistId);
//               comicsGrid.setComic(comic);
//               comicsGrid.setJoke(joke);
//               comicsGrid.setThumb(thumb);
//               comicsGrid.setVideoId(videoId);
//               comicsGridList.add(comicsGrid);
//               System.out.println(playlistId);
//               System.out.println(comic);
//               System.out.println(joke);
//               System.out.println(thumb);
//               System.out.println(videoId + "\n\n");
//
//          }
//
//          uri = "https://www.googleapis.com/youtube/v3/videos?part=snippet&id=" + StringUtils.join(videoIds, ",") + "&maxResults=50&key=AIzaSyDCloQetmWP3BgQ7q-1m9K_hEVdyY2qorw";
//          restTemplate = new RestTemplate();
//          result = restTemplate.getForObject(uri, String.class);
//
//          jsonObject = new JsonParser().parse(result).getAsJsonObject();
//          jsonArray = jsonObject.getAsJsonArray("items");
//
//          for (int i = 0; i < jsonArray.size(); i++) {
//               String videoId = jsonArray.get(i).getAsJsonObject()
//                    .get("id")
//                    .getAsString();
//
//               String talent = jsonArray.get(i).getAsJsonObject()
//                    .get("snippet")
//                    .getAsJsonObject()
//                    .get("tags")
//                    .getAsJsonArray()
//                    .get(0)
//                    .getAsString();
//
//               for (ComicsGrid comicsGrid : comicsGridList) {
//                    if (comicsGrid.getVideoId().equals(videoId)) {
//                         comicsGrid.setTalent(talent);
//                         break;
//                    }
//
//               }
//          }
//
//
//          System.out.println(result);
//
//
//     }
//
//}

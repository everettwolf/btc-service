package com.btc.web.app;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Data;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Chris on 12/19/15.
 */
public class UpdateFromFeed {

    public static void main(String[] args) throws Exception {
        String videoId = "";
        JsonArray widgetFeedJsonArray = getPlaylistItemsByPlaylistId("PLLVtQiMiCJeHFYM4L6Lu68dSdSoQdwCak")
                .getAsJsonArray("items");

        if (widgetFeedJsonArray.size() > 0) {
            videoId = widgetFeedJsonArray
                    .get(0)
                    .getAsJsonObject()
                    .get("snippet")
                    .getAsJsonObject()
                    .get("resourceId")
                    .getAsJsonObject()
                    .get("videoId")
                    .getAsString();
        }

        updatePlaylistWithVideo("PLLVtQiMiCJeEcXlTmAuiY8T_0gWCTxyws", "1z7im3MTFuU");
        System.out.println(videoId);
//        JsonArray widgetJsonArray = getPlaylistItemsByPlaylistId("PLLVtQiMiCJeEcXlTmAuiY8T_0gWCTxyws")
//                .getAsJsonArray("items");


    }

    private static JsonObject getPlaylistItemsByPlaylistId(String gridId) {
        String uri = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=50&playlistId=" + gridId + "&key=AIzaSyDCloQetmWP3BgQ7q-1m9K_hEVdyY2qorw";
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);

        return new JsonParser().parse(result).getAsJsonObject();
    }

    @Data
    static class ResourceId {
        private String videoId;
        private String kind;
    }

    @Data
    static class Snippet {
        private ResourceId resourceId;
        private String playlistId;
    }

    @Data
    static class RequestBody {
        private Snippet snippet;
    }

    private static JsonObject updatePlaylistWithVideo(String playlistId, String videoId) throws Exception {

//        String uri = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&key=AIzaSyDCloQetmWP3BgQ7q-1m9K_hEVdyY2qorw";
//        HttpClient httpClient = new DefaultHttpClient();
//        Gson gson = new Gson();
//        HttpPost post = new HttpPost(uri);
//
//        ResourceId resourceId = new ResourceId();
//        resourceId.setVideoId("1z7im3MTFuU");
//        resourceId.setKind("youtube#video");
//
//        Snippet snippet = new Snippet();
//        snippet.setResourceId(resourceId);
//        snippet.setPlaylistId("PLLVtQiMiCJeEcXlTmAuiY8T_0gWCTxyws");
//
//        RequestBody requestBody = new RequestBody();
//        requestBody.setSnippet(snippet);
//
//        System.out.println(gson.toJson(requestBody));
//
//        StringEntity stringEntity = new StringEntity(gson.toJson(requestBody));
//
//        post.setEntity(stringEntity);
//        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
//
//        HttpResponse response = httpClient.execute(post);
//        return null;
        //return new JsonParser().parse(result).getAsJsonObject();
//        String uri = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&key=AIzaSyDCloQetmWP3BgQ7q-1m9K_hEVdyY2qorw";
        String uri = "https://www.googleapis.com/youtube/v3/playlistItems";
        Gson gson = new Gson();
        RestTemplate restTemplate = new RestTemplate();

        ResourceId resourceId = new ResourceId();
        resourceId.setVideoId("1z7im3MTFuU");
        resourceId.setKind("youtube#video");

        Snippet snippet = new Snippet();
        snippet.setResourceId(resourceId);
        snippet.setPlaylistId("PLLVtQiMiCJeEcXlTmAuiY8T_0gWCTxyws");

        RequestBody requestBody = new RequestBody();
        requestBody.setSnippet(snippet);

        MultiValueMap<String, String> mvm = new LinkedMultiValueMap<String, String>();
        mvm.add("part", "snippet");
        mvm.add("key", "AIzaSyDCloQetmWP3BgQ7q-1m9K_hEVdyY2qorw");
        mvm.add("body", gson.toJson(requestBody));

        System.out.println(gson.toJson(requestBody));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(mvm, headers);

        //HttpEntity<String> entity = new HttpEntity<String>(gson.toJson(requestBody), headers);
        //String response = restTemplate.postForObject(uri, entity, String.class);
        Object response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
        System.out.println(response);
        return null;
    }
}

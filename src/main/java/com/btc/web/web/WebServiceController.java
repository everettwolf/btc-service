package com.btc.web.web;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;

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

     private File convert(MultipartFile file) throws Exception {
          File convFile = new File(file.getOriginalFilename());
          convFile.createNewFile();
          FileOutputStream fos = new FileOutputStream(convFile);
          fos.write(file.getBytes());
          fos.close();
          return convFile;
     }

}

package com.btc.web.service;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.StringWriter;

/**
 * Created by Chris on 11/18/15.
 */

@Service
public class TemplateService {

     private static Logger logger = LoggerFactory.getLogger(TemplateService.class);

     public enum TemplateType {
          GRID("grid_assets/header.html", "grid_assets/grid_template.html", "grid_assets/footer.html"),
          PLAYER("grid_assets/header.html", "grid_assets/player_template.html", "grid_assets/footer.html");

          private final String header;
          private final String content;
          private final String footer;

          TemplateType(String header, String content, String footer) {
               this.header = header;
               this.content = content;
               this.footer = footer;

          }

          private String getHeader() {
               return header;
          }

          private String getContent() {
               return content;
          }

          private String getFooter() {
               return footer;
          }
     }

     @Cacheable(value = "templates")
     public String getTemplate(TemplateType templateType) throws Exception {

          logger.info("Getting template for {}", templateType.toString());

          Resource headerTemplate = new ClassPathResource(templateType.getHeader());
          StringWriter header = new StringWriter();
          IOUtils.copy(headerTemplate.getInputStream(), header, Charsets.UTF_8);

          Resource gridTemplate = new ClassPathResource(templateType.getContent());
          StringWriter content = new StringWriter();
          IOUtils.copy(gridTemplate.getInputStream(), content, Charsets.UTF_8);

          Resource footerTemplate = new ClassPathResource(templateType.getFooter());
          StringWriter footer = new StringWriter();
          IOUtils.copy(footerTemplate.getInputStream(), footer, Charsets.UTF_8);

          return header.toString() + content.toString() + footer.toString();
     }
}

package com.btc.web;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

import java.util.Collections;

/**
 * Created by Chris on 9/12/14.
 */
@EnableJpaRepositories
@EnableScheduling
@SpringBootApplication
public class Application {

     @Value("${db.auto}")
     private boolean dbAuto;

     @ControllerAdvice
     static class JsonpAdvice extends AbstractJsonpResponseBodyAdvice {
          public JsonpAdvice() {
               super("callback");
          }
     }

     @Bean
     public HttpMessageConverters customConverters() {
          return new HttpMessageConverters(false, Collections.<HttpMessageConverter<?>>singleton(new MappingJackson2HttpMessageConverter()));
     }

     public static void main(String[] args) throws Exception {
          SpringApplication application = new SpringApplication(Application.class);
          application.run();
     }

}

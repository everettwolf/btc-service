package com.btc.web;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

import java.util.Collections;

/**
 * Created by Chris on 9/12/14.
 */
@Configuration
@EnableAutoConfiguration
@EnableJpaRepositories
@SpringBootApplication
@ComponentScan(value = "com.btc.web")
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

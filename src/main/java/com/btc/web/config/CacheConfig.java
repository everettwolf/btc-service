package com.btc.web.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 11/13/15.
 */
@Configuration
@EnableCaching
public class CacheConfig {

     @Bean
     public CacheManager cacheManager() {
          ConcurrentMapCacheManager concurrentMapCacheManager = new ConcurrentMapCacheManager();

          List<String> caches = new ArrayList<>();
          caches.add("playlists");
          caches.add("templates");

          concurrentMapCacheManager.setCacheNames(caches);
          return concurrentMapCacheManager;
     }
}

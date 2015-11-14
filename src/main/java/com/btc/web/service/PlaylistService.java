package com.btc.web.service;

import com.btc.web.model.Playlist;
import com.btc.web.model.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Created by Chris on 11/14/15.
 */
@Service
public class PlaylistService {

     @Autowired
     private PlaylistRepository playlistRepository;

     @Cacheable(value = "playlists", key = "#playlistId")
     public Playlist findByPlaylistId(String playlistId) {
          Playlist playlist = playlistRepository.findByPlaylistId(playlistId);
          return playlist;
     }

     public void deleteByPlaylistId(String playlistId) {
          playlistRepository.deleteByPlaylistId(playlistId);
     }

     @CacheEvict(value = "playlists", key = "#playlist.playlistId")
     public Playlist save(Playlist playlist) {
          return playlistRepository.save(playlist);
     }
}

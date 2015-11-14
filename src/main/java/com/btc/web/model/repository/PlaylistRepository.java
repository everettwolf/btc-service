package com.btc.web.model.repository;

import com.btc.web.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

     Playlist findByPlaylistId(String playlistId);

     void deleteByPlaylistId(String playlistId);

}

package com.btc.web.model.repository;

import com.btc.web.model.Playlists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PlaylistRepository extends JpaRepository<Playlists, Long> {
     Playlists findByPlaylistId(String playlistId);
}

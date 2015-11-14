package com.btc.web.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by Eric on 10/22/14.
 */
@Data
@Entity
@Table(name = "playlist")
public class Playlist implements Serializable {

     private static final long serialVersionUID = 1L;

     @Id
     @Column(name = "id")
     @SequenceGenerator(name = "btc_id_generator", sequenceName = "BTC_ID_SEQUENCE", allocationSize = 2)
     @GeneratedValue(strategy = GenerationType.AUTO, generator = "btc_id_generator")
     private Long id;

     @NotEmpty
     @Size(max = 100)
     @Column(name = "playlist_name")
     private String playlistName;

     @NotEmpty
     @Size(max = 100)
     @Column(name = "playlist_id")
     private String playlistId;

     @Lob
     @Column(name = "playlist_json")
     private String playlistJSON;

}

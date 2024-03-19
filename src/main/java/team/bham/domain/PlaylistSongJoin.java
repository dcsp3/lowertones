package team.bham.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PlaylistSongJoin.
 */
@Entity
@Table(name = "playlist_song_table")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlaylistSongJoin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "song_order_index", nullable = false)
    private Integer songOrderIndex;

    @NotNull
    @Column(name = "song_date_added", nullable = false)
    private LocalDate songDateAdded;

    @ManyToOne
    @JsonIgnoreProperties(value = { "appUser", "playlistSongJoins" }, allowSetters = true)
    private Playlist playlist;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "spotifyGenreEntities", "musicbrainzGenreEntities", "contributors", "album", "playlistSongJoins", "songArtistJoins" },
        allowSetters = true
    )
    private Song song;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PlaylistSongJoin id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSongOrderIndex() {
        return this.songOrderIndex;
    }

    public PlaylistSongJoin songOrderIndex(Integer songOrderIndex) {
        this.setSongOrderIndex(songOrderIndex);
        return this;
    }

    public void setSongOrderIndex(Integer songOrderIndex) {
        this.songOrderIndex = songOrderIndex;
    }

    public LocalDate getSongDateAdded() {
        return this.songDateAdded;
    }

    public PlaylistSongJoin songDateAdded(LocalDate songDateAdded) {
        this.setSongDateAdded(songDateAdded);
        return this;
    }

    public void setSongDateAdded(LocalDate songDateAdded) {
        this.songDateAdded = songDateAdded;
    }

    public Playlist getPlaylist() {
        return this.playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public PlaylistSongJoin playlist(Playlist playlist) {
        this.setPlaylist(playlist);
        return this;
    }

    public Song getSong() {
        return this.song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public PlaylistSongJoin song(Song song) {
        this.setSong(song);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlaylistSongJoin)) {
            return false;
        }
        return id != null && id.equals(((PlaylistSongJoin) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlaylistSongJoin{" +
            "id=" + getId() +
            ", songOrderIndex=" + getSongOrderIndex() +
            ", songDateAdded='" + getSongDateAdded() + "'" +
            "}";
    }
}

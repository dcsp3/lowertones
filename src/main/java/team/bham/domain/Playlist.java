package team.bham.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Playlist.
 */
@Entity
@Table(name = "playlist_table")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Playlist implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date_added_to_db", nullable = false)
    private LocalDate dateAddedToDB;

    @NotNull
    @Column(name = "date_last_modified", nullable = false)
    private LocalDate dateLastModified;

    @NotNull
    @Column(name = "playlist_spotify_id", nullable = false)
    private String playlistSpotifyID;

    @NotNull
    @Column(name = "playlist_name", nullable = false)
    private String playlistName;

    @NotNull
    @Column(name = "playlist_snapshot_id", nullable = false)
    private String playlistSnapshotID;

    @Column(name = "playlist_image_large")
    private String playlistImageLarge;

    @Column(name = "playlist_image_medium")
    private String playlistImageMedium;

    @Column(name = "playlist_image_small")
    private String playlistImageSmall;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "playlists" }, allowSetters = true)
    private AppUser appUser;

    @OneToMany(mappedBy = "playlist")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "playlist", "song" }, allowSetters = true)
    private Set<PlaylistSongJoin> playlistSongJoins = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Playlist id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateAddedToDB() {
        return this.dateAddedToDB;
    }

    public Playlist dateAddedToDB(LocalDate dateAddedToDB) {
        this.setDateAddedToDB(dateAddedToDB);
        return this;
    }

    public void setDateAddedToDB(LocalDate dateAddedToDB) {
        this.dateAddedToDB = dateAddedToDB;
    }

    public LocalDate getDateLastModified() {
        return this.dateLastModified;
    }

    public Playlist dateLastModified(LocalDate dateLastModified) {
        this.setDateLastModified(dateLastModified);
        return this;
    }

    public void setDateLastModified(LocalDate dateLastModified) {
        this.dateLastModified = dateLastModified;
    }

    public String getPlaylistSpotifyID() {
        return this.playlistSpotifyID;
    }

    public Playlist playlistSpotifyID(String playlistSpotifyID) {
        this.setPlaylistSpotifyID(playlistSpotifyID);
        return this;
    }

    public void setPlaylistSpotifyID(String playlistSpotifyID) {
        this.playlistSpotifyID = playlistSpotifyID;
    }

    public String getPlaylistName() {
        return this.playlistName;
    }

    public Playlist playlistName(String playlistName) {
        this.setPlaylistName(playlistName);
        return this;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public String getPlaylistSnapshotID() {
        return this.playlistSnapshotID;
    }

    public Playlist playlistSnapshotID(String playlistSnapshotID) {
        this.setPlaylistSnapshotID(playlistSnapshotID);
        return this;
    }

    public void setPlaylistSnapshotID(String playlistSnapshotID) {
        this.playlistSnapshotID = playlistSnapshotID;
    }

    public String getPlaylistImageLarge() {
        return this.playlistImageLarge;
    }

    public Playlist playlistImageLarge(String playlistImageLarge) {
        this.setPlaylistImageLarge(playlistImageLarge);
        return this;
    }

    public void setPlaylistImageLarge(String playlistImageLarge) {
        this.playlistImageLarge = playlistImageLarge;
    }

    public String getPlaylistImageMedium() {
        return this.playlistImageMedium;
    }

    public Playlist playlistImageMedium(String playlistImageMedium) {
        this.setPlaylistImageMedium(playlistImageMedium);
        return this;
    }

    public void setPlaylistImageMedium(String playlistImageMedium) {
        this.playlistImageMedium = playlistImageMedium;
    }

    public String getPlaylistImageSmall() {
        return this.playlistImageSmall;
    }

    public Playlist playlistImageSmall(String playlistImageSmall) {
        this.setPlaylistImageSmall(playlistImageSmall);
        return this;
    }

    public void setPlaylistImageSmall(String playlistImageSmall) {
        this.playlistImageSmall = playlistImageSmall;
    }

    public AppUser getAppUser() {
        return this.appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Playlist appUser(AppUser appUser) {
        this.setAppUser(appUser);
        return this;
    }

    public Set<PlaylistSongJoin> getPlaylistSongJoins() {
        return this.playlistSongJoins;
    }

    public void setPlaylistSongJoins(Set<PlaylistSongJoin> playlistSongJoins) {
        if (this.playlistSongJoins != null) {
            this.playlistSongJoins.forEach(i -> i.setPlaylist(null));
        }
        if (playlistSongJoins != null) {
            playlistSongJoins.forEach(i -> i.setPlaylist(this));
        }
        this.playlistSongJoins = playlistSongJoins;
    }

    public Playlist playlistSongJoins(Set<PlaylistSongJoin> playlistSongJoins) {
        this.setPlaylistSongJoins(playlistSongJoins);
        return this;
    }

    public Playlist addPlaylistSongJoin(PlaylistSongJoin playlistSongJoin) {
        this.playlistSongJoins.add(playlistSongJoin);
        playlistSongJoin.setPlaylist(this);
        return this;
    }

    public Playlist removePlaylistSongJoin(PlaylistSongJoin playlistSongJoin) {
        this.playlistSongJoins.remove(playlistSongJoin);
        playlistSongJoin.setPlaylist(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Playlist)) {
            return false;
        }
        return id != null && id.equals(((Playlist) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Playlist{" +
            "id=" + getId() +
            ", dateAddedToDB='" + getDateAddedToDB() + "'" +
            ", dateLastModified='" + getDateLastModified() + "'" +
            ", playlistSpotifyID='" + getPlaylistSpotifyID() + "'" +
            ", playlistName='" + getPlaylistName() + "'" +
            ", playlistSnapshotID='" + getPlaylistSnapshotID() + "'" +
            ", playlistImageLarge='" + getPlaylistImageLarge() + "'" +
            ", playlistImageMedium='" + getPlaylistImageMedium() + "'" +
            ", playlistImageSmall='" + getPlaylistImageSmall() + "'" +
            "}";
    }
}

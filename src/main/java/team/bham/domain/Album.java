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
import team.bham.domain.enumeration.AlbumType;
import team.bham.domain.enumeration.ReleaseDatePrecision;

/**
 * A Album.
 */
@Entity
@Table(name = "album_table")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Album implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "album_spotify_id", nullable = false)
    private String albumSpotifyID;

    @NotNull
    @Column(name = "album_name", nullable = false)
    private String albumName;

    @NotNull
    @Column(name = "album_cover_art", nullable = false)
    private String albumCoverArt;

    @NotNull
    @Column(name = "album_release_date", nullable = false)
    private LocalDate albumReleaseDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "release_date_precision", nullable = false)
    private ReleaseDatePrecision releaseDatePrecision;

    @NotNull
    @Column(name = "album_popularity", nullable = false)
    private Integer albumPopularity;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "album_type", nullable = false)
    private AlbumType albumType;

    @Column(name = "spotify_album_upc")
    private String spotifyAlbumUPC;

    @Column(name = "spotify_album_ean")
    private String spotifyAlbumEAN;

    @Column(name = "spotify_album_isrc")
    private String spotifyAlbumISRC;

    @NotNull
    @Column(name = "date_added_to_db", nullable = false)
    private LocalDate dateAddedToDB;

    @NotNull
    @Column(name = "date_last_modified", nullable = false)
    private LocalDate dateLastModified;

    @NotNull
    @Column(name = "musicbrainz_metadata_added", nullable = false)
    private Boolean musicbrainzMetadataAdded;

    @Column(name = "musicbrainz_id")
    private String musicbrainzID;

    @OneToMany(mappedBy = "album")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "spotifyGenreEntities", "musicbrainzGenreEntities", "contributors", "album", "playlistSongJoins", "songArtistJoins" },
        allowSetters = true
    )
    private Set<Song> songs = new HashSet<>();

    @OneToMany(mappedBy = "album")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "song", "album", "mainArtist" }, allowSetters = true)
    private Set<SpotifyGenreEntity> spotifyGenreEntities = new HashSet<>();

    @OneToMany(mappedBy = "album")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "song", "album", "mainArtist" }, allowSetters = true)
    private Set<MusicbrainzGenreEntity> musicbrainzGenreEntities = new HashSet<>();

    @ManyToMany(mappedBy = "albums")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "relatedArtists", "spotifyGenreEntities", "musicbrainzGenreEntities", "albums", "songArtistJoins" },
        allowSetters = true
    )
    private Set<MainArtist> mainArtists = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Album id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlbumSpotifyID() {
        return this.albumSpotifyID;
    }

    public Album albumSpotifyID(String albumSpotifyID) {
        this.setAlbumSpotifyID(albumSpotifyID);
        return this;
    }

    public void setAlbumSpotifyID(String albumSpotifyID) {
        this.albumSpotifyID = albumSpotifyID;
    }

    public String getAlbumName() {
        return this.albumName;
    }

    public Album albumName(String albumName) {
        this.setAlbumName(albumName);
        return this;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumCoverArt() {
        return this.albumCoverArt;
    }

    public Album albumCoverArt(String albumCoverArt) {
        this.setAlbumCoverArt(albumCoverArt);
        return this;
    }

    public void setAlbumCoverArt(String albumCoverArt) {
        this.albumCoverArt = albumCoverArt;
    }

    public LocalDate getAlbumReleaseDate() {
        return this.albumReleaseDate;
    }

    public Album albumReleaseDate(LocalDate albumReleaseDate) {
        this.setAlbumReleaseDate(albumReleaseDate);
        return this;
    }

    public void setAlbumReleaseDate(LocalDate albumReleaseDate) {
        this.albumReleaseDate = albumReleaseDate;
    }

    public ReleaseDatePrecision getReleaseDatePrecision() {
        return this.releaseDatePrecision;
    }

    public Album releaseDatePrecision(ReleaseDatePrecision releaseDatePrecision) {
        this.setReleaseDatePrecision(releaseDatePrecision);
        return this;
    }

    public void setReleaseDatePrecision(ReleaseDatePrecision releaseDatePrecision) {
        this.releaseDatePrecision = releaseDatePrecision;
    }

    public Integer getAlbumPopularity() {
        return this.albumPopularity;
    }

    public Album albumPopularity(Integer albumPopularity) {
        this.setAlbumPopularity(albumPopularity);
        return this;
    }

    public void setAlbumPopularity(Integer albumPopularity) {
        this.albumPopularity = albumPopularity;
    }

    public AlbumType getAlbumType() {
        return this.albumType;
    }

    public Album albumType(AlbumType albumType) {
        this.setAlbumType(albumType);
        return this;
    }

    public void setAlbumType(AlbumType albumType) {
        this.albumType = albumType;
    }

    public String getSpotifyAlbumUPC() {
        return this.spotifyAlbumUPC;
    }

    public Album spotifyAlbumUPC(String spotifyAlbumUPC) {
        this.setSpotifyAlbumUPC(spotifyAlbumUPC);
        return this;
    }

    public void setSpotifyAlbumUPC(String spotifyAlbumUPC) {
        this.spotifyAlbumUPC = spotifyAlbumUPC;
    }

    public String getSpotifyAlbumEAN() {
        return this.spotifyAlbumEAN;
    }

    public Album spotifyAlbumEAN(String spotifyAlbumEAN) {
        this.setSpotifyAlbumEAN(spotifyAlbumEAN);
        return this;
    }

    public void setSpotifyAlbumEAN(String spotifyAlbumEAN) {
        this.spotifyAlbumEAN = spotifyAlbumEAN;
    }

    public String getSpotifyAlbumISRC() {
        return this.spotifyAlbumISRC;
    }

    public Album spotifyAlbumISRC(String spotifyAlbumISRC) {
        this.setSpotifyAlbumISRC(spotifyAlbumISRC);
        return this;
    }

    public void setSpotifyAlbumISRC(String spotifyAlbumISRC) {
        this.spotifyAlbumISRC = spotifyAlbumISRC;
    }

    public LocalDate getDateAddedToDB() {
        return this.dateAddedToDB;
    }

    public Album dateAddedToDB(LocalDate dateAddedToDB) {
        this.setDateAddedToDB(dateAddedToDB);
        return this;
    }

    public void setDateAddedToDB(LocalDate dateAddedToDB) {
        this.dateAddedToDB = dateAddedToDB;
    }

    public LocalDate getDateLastModified() {
        return this.dateLastModified;
    }

    public Album dateLastModified(LocalDate dateLastModified) {
        this.setDateLastModified(dateLastModified);
        return this;
    }

    public void setDateLastModified(LocalDate dateLastModified) {
        this.dateLastModified = dateLastModified;
    }

    public Boolean getMusicbrainzMetadataAdded() {
        return this.musicbrainzMetadataAdded;
    }

    public Album musicbrainzMetadataAdded(Boolean musicbrainzMetadataAdded) {
        this.setMusicbrainzMetadataAdded(musicbrainzMetadataAdded);
        return this;
    }

    public void setMusicbrainzMetadataAdded(Boolean musicbrainzMetadataAdded) {
        this.musicbrainzMetadataAdded = musicbrainzMetadataAdded;
    }

    public String getMusicbrainzID() {
        return this.musicbrainzID;
    }

    public Album musicbrainzID(String musicbrainzID) {
        this.setMusicbrainzID(musicbrainzID);
        return this;
    }

    public void setMusicbrainzID(String musicbrainzID) {
        this.musicbrainzID = musicbrainzID;
    }

    public Set<Song> getSongs() {
        return this.songs;
    }

    public void setSongs(Set<Song> songs) {
        if (this.songs != null) {
            this.songs.forEach(i -> i.setAlbum(null));
        }
        if (songs != null) {
            songs.forEach(i -> i.setAlbum(this));
        }
        this.songs = songs;
    }

    public Album songs(Set<Song> songs) {
        this.setSongs(songs);
        return this;
    }

    public Album addSong(Song song) {
        this.songs.add(song);
        song.setAlbum(this);
        return this;
    }

    public Album removeSong(Song song) {
        this.songs.remove(song);
        song.setAlbum(null);
        return this;
    }

    public Set<SpotifyGenreEntity> getSpotifyGenreEntities() {
        return this.spotifyGenreEntities;
    }

    public void setSpotifyGenreEntities(Set<SpotifyGenreEntity> spotifyGenreEntities) {
        if (this.spotifyGenreEntities != null) {
            this.spotifyGenreEntities.forEach(i -> i.setAlbum(null));
        }
        if (spotifyGenreEntities != null) {
            spotifyGenreEntities.forEach(i -> i.setAlbum(this));
        }
        this.spotifyGenreEntities = spotifyGenreEntities;
    }

    public Album spotifyGenreEntities(Set<SpotifyGenreEntity> spotifyGenreEntities) {
        this.setSpotifyGenreEntities(spotifyGenreEntities);
        return this;
    }

    public Album addSpotifyGenreEntity(SpotifyGenreEntity spotifyGenreEntity) {
        this.spotifyGenreEntities.add(spotifyGenreEntity);
        spotifyGenreEntity.setAlbum(this);
        return this;
    }

    public Album removeSpotifyGenreEntity(SpotifyGenreEntity spotifyGenreEntity) {
        this.spotifyGenreEntities.remove(spotifyGenreEntity);
        spotifyGenreEntity.setAlbum(null);
        return this;
    }

    public Set<MusicbrainzGenreEntity> getMusicbrainzGenreEntities() {
        return this.musicbrainzGenreEntities;
    }

    public void setMusicbrainzGenreEntities(Set<MusicbrainzGenreEntity> musicbrainzGenreEntities) {
        if (this.musicbrainzGenreEntities != null) {
            this.musicbrainzGenreEntities.forEach(i -> i.setAlbum(null));
        }
        if (musicbrainzGenreEntities != null) {
            musicbrainzGenreEntities.forEach(i -> i.setAlbum(this));
        }
        this.musicbrainzGenreEntities = musicbrainzGenreEntities;
    }

    public Album musicbrainzGenreEntities(Set<MusicbrainzGenreEntity> musicbrainzGenreEntities) {
        this.setMusicbrainzGenreEntities(musicbrainzGenreEntities);
        return this;
    }

    public Album addMusicbrainzGenreEntity(MusicbrainzGenreEntity musicbrainzGenreEntity) {
        this.musicbrainzGenreEntities.add(musicbrainzGenreEntity);
        musicbrainzGenreEntity.setAlbum(this);
        return this;
    }

    public Album removeMusicbrainzGenreEntity(MusicbrainzGenreEntity musicbrainzGenreEntity) {
        this.musicbrainzGenreEntities.remove(musicbrainzGenreEntity);
        musicbrainzGenreEntity.setAlbum(null);
        return this;
    }

    public Set<MainArtist> getMainArtists() {
        return this.mainArtists;
    }

    public void setMainArtists(Set<MainArtist> mainArtists) {
        if (this.mainArtists != null) {
            this.mainArtists.forEach(i -> i.removeAlbum(this));
        }
        if (mainArtists != null) {
            mainArtists.forEach(i -> i.addAlbum(this));
        }
        this.mainArtists = mainArtists;
    }

    public Album mainArtists(Set<MainArtist> mainArtists) {
        this.setMainArtists(mainArtists);
        return this;
    }

    public Album addMainArtist(MainArtist mainArtist) {
        this.mainArtists.add(mainArtist);
        mainArtist.getAlbums().add(this);
        return this;
    }

    public Album removeMainArtist(MainArtist mainArtist) {
        this.mainArtists.remove(mainArtist);
        mainArtist.getAlbums().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Album)) {
            return false;
        }
        return id != null && id.equals(((Album) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Album{" +
            "id=" + getId() +
            ", albumSpotifyID='" + getAlbumSpotifyID() + "'" +
            ", albumName='" + getAlbumName() + "'" +
            ", albumCoverArt='" + getAlbumCoverArt() + "'" +
            ", albumReleaseDate='" + getAlbumReleaseDate() + "'" +
            ", releaseDatePrecision='" + getReleaseDatePrecision() + "'" +
            ", albumPopularity=" + getAlbumPopularity() +
            ", albumType='" + getAlbumType() + "'" +
            ", spotifyAlbumUPC='" + getSpotifyAlbumUPC() + "'" +
            ", spotifyAlbumEAN='" + getSpotifyAlbumEAN() + "'" +
            ", spotifyAlbumISRC='" + getSpotifyAlbumISRC() + "'" +
            ", dateAddedToDB='" + getDateAddedToDB() + "'" +
            ", dateLastModified='" + getDateLastModified() + "'" +
            ", musicbrainzMetadataAdded='" + getMusicbrainzMetadataAdded() + "'" +
            ", musicbrainzID='" + getMusicbrainzID() + "'" +
            "}";
    }
}

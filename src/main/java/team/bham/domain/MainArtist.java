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
 * A MainArtist.
 */
@Entity
@Table(name = "artists_table")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MainArtist implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "artist_spotify_id", nullable = false)
    private String artistSpotifyID;

    @NotNull
    @Column(name = "artist_name", nullable = false)
    private String artistName;

    @NotNull
    @Column(name = "artist_popularity", nullable = false)
    private Integer artistPopularity;

    @Column(name = "artist_image_small")
    private String artistImageSmall;

    @Column(name = "artist_image_medium")
    private String artistImageMedium;

    @Column(name = "artist_image_large")
    private String artistImageLarge;

    @Column(name = "artist_followers")
    private Integer artistFollowers;

    @Column(name = "date_added_to_db")
    private LocalDate dateAddedToDB;

    @Column(name = "date_last_modified")
    private LocalDate dateLastModified;

    @Column(name = "musicbrainz_id")
    private String musicbrainzID;

    @JsonIgnoreProperties(value = { "mainArtist" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private RelatedArtists relatedArtists;

    @OneToMany(mappedBy = "mainArtist")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "song", "album", "mainArtist" }, allowSetters = true)
    private Set<SpotifyGenreEntity> spotifyGenreEntities = new HashSet<>();

    @OneToMany(mappedBy = "mainArtist")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "song", "album", "mainArtist" }, allowSetters = true)
    private Set<MusicbrainzGenreEntity> musicbrainzGenreEntities = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_artists_table__album",
        joinColumns = @JoinColumn(name = "artists_table_id"),
        inverseJoinColumns = @JoinColumn(name = "album_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "songs", "spotifyGenreEntities", "musicbrainzGenreEntities", "mainArtists" }, allowSetters = true)
    private Set<Album> albums = new HashSet<>();

    @OneToMany(mappedBy = "mainArtist")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "song", "mainArtist" }, allowSetters = true)
    private Set<SongArtistJoin> songArtistJoins = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MainArtist id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArtistSpotifyID() {
        return this.artistSpotifyID;
    }

    public MainArtist artistSpotifyID(String artistSpotifyID) {
        this.setArtistSpotifyID(artistSpotifyID);
        return this;
    }

    public void setArtistSpotifyID(String artistSpotifyID) {
        this.artistSpotifyID = artistSpotifyID;
    }

    public String getArtistName() {
        return this.artistName;
    }

    public MainArtist artistName(String artistName) {
        this.setArtistName(artistName);
        return this;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public Integer getArtistPopularity() {
        return this.artistPopularity;
    }

    public MainArtist artistPopularity(Integer artistPopularity) {
        this.setArtistPopularity(artistPopularity);
        return this;
    }

    public void setArtistPopularity(Integer artistPopularity) {
        this.artistPopularity = artistPopularity;
    }

    public String getArtistImageSmall() {
        return this.artistImageSmall;
    }

    public MainArtist artistImageSmall(String artistImageSmall) {
        this.setArtistImageSmall(artistImageSmall);
        return this;
    }

    public void setArtistImageSmall(String artistImageSmall) {
        this.artistImageSmall = artistImageSmall;
    }

    public String getArtistImageMedium() {
        return this.artistImageMedium;
    }

    public MainArtist artistImageMedium(String artistImageMedium) {
        this.setArtistImageMedium(artistImageMedium);
        return this;
    }

    public void setArtistImageMedium(String artistImageMedium) {
        this.artistImageMedium = artistImageMedium;
    }

    public String getArtistImageLarge() {
        return this.artistImageLarge;
    }

    public MainArtist artistImageLarge(String artistImageLarge) {
        this.setArtistImageLarge(artistImageLarge);
        return this;
    }

    public void setArtistImageLarge(String artistImageLarge) {
        this.artistImageLarge = artistImageLarge;
    }

    public Integer getArtistFollowers() {
        return this.artistFollowers;
    }

    public MainArtist artistFollowers(Integer artistFollowers) {
        this.setArtistFollowers(artistFollowers);
        return this;
    }

    public void setArtistFollowers(Integer artistFollowers) {
        this.artistFollowers = artistFollowers;
    }

    public LocalDate getDateAddedToDB() {
        return this.dateAddedToDB;
    }

    public MainArtist dateAddedToDB(LocalDate dateAddedToDB) {
        this.setDateAddedToDB(dateAddedToDB);
        return this;
    }

    public void setDateAddedToDB(LocalDate dateAddedToDB) {
        this.dateAddedToDB = dateAddedToDB;
    }

    public LocalDate getDateLastModified() {
        return this.dateLastModified;
    }

    public MainArtist dateLastModified(LocalDate dateLastModified) {
        this.setDateLastModified(dateLastModified);
        return this;
    }

    public void setDateLastModified(LocalDate dateLastModified) {
        this.dateLastModified = dateLastModified;
    }

    public String getMusicbrainzID() {
        return this.musicbrainzID;
    }

    public MainArtist musicbrainzID(String musicbrainzID) {
        this.setMusicbrainzID(musicbrainzID);
        return this;
    }

    public void setMusicbrainzID(String musicbrainzID) {
        this.musicbrainzID = musicbrainzID;
    }

    public RelatedArtists getRelatedArtists() {
        return this.relatedArtists;
    }

    public void setRelatedArtists(RelatedArtists relatedArtists) {
        this.relatedArtists = relatedArtists;
    }

    public MainArtist relatedArtists(RelatedArtists relatedArtists) {
        this.setRelatedArtists(relatedArtists);
        return this;
    }

    public Set<SpotifyGenreEntity> getSpotifyGenreEntities() {
        return this.spotifyGenreEntities;
    }

    public void setSpotifyGenreEntities(Set<SpotifyGenreEntity> spotifyGenreEntities) {
        if (this.spotifyGenreEntities != null) {
            this.spotifyGenreEntities.forEach(i -> i.setMainArtist(null));
        }
        if (spotifyGenreEntities != null) {
            spotifyGenreEntities.forEach(i -> i.setMainArtist(this));
        }
        this.spotifyGenreEntities = spotifyGenreEntities;
    }

    public MainArtist spotifyGenreEntities(Set<SpotifyGenreEntity> spotifyGenreEntities) {
        this.setSpotifyGenreEntities(spotifyGenreEntities);
        return this;
    }

    public MainArtist addSpotifyGenreEntity(SpotifyGenreEntity spotifyGenreEntity) {
        this.spotifyGenreEntities.add(spotifyGenreEntity);
        spotifyGenreEntity.setMainArtist(this);
        return this;
    }

    public MainArtist removeSpotifyGenreEntity(SpotifyGenreEntity spotifyGenreEntity) {
        this.spotifyGenreEntities.remove(spotifyGenreEntity);
        spotifyGenreEntity.setMainArtist(null);
        return this;
    }

    public Set<MusicbrainzGenreEntity> getMusicbrainzGenreEntities() {
        return this.musicbrainzGenreEntities;
    }

    public void setMusicbrainzGenreEntities(Set<MusicbrainzGenreEntity> musicbrainzGenreEntities) {
        if (this.musicbrainzGenreEntities != null) {
            this.musicbrainzGenreEntities.forEach(i -> i.setMainArtist(null));
        }
        if (musicbrainzGenreEntities != null) {
            musicbrainzGenreEntities.forEach(i -> i.setMainArtist(this));
        }
        this.musicbrainzGenreEntities = musicbrainzGenreEntities;
    }

    public MainArtist musicbrainzGenreEntities(Set<MusicbrainzGenreEntity> musicbrainzGenreEntities) {
        this.setMusicbrainzGenreEntities(musicbrainzGenreEntities);
        return this;
    }

    public MainArtist addMusicbrainzGenreEntity(MusicbrainzGenreEntity musicbrainzGenreEntity) {
        this.musicbrainzGenreEntities.add(musicbrainzGenreEntity);
        musicbrainzGenreEntity.setMainArtist(this);
        return this;
    }

    public MainArtist removeMusicbrainzGenreEntity(MusicbrainzGenreEntity musicbrainzGenreEntity) {
        this.musicbrainzGenreEntities.remove(musicbrainzGenreEntity);
        musicbrainzGenreEntity.setMainArtist(null);
        return this;
    }

    public Set<Album> getAlbums() {
        return this.albums;
    }

    public void setAlbums(Set<Album> albums) {
        this.albums = albums;
    }

    public MainArtist albums(Set<Album> albums) {
        this.setAlbums(albums);
        return this;
    }

    public MainArtist addAlbum(Album album) {
        this.albums.add(album);
        album.getMainArtists().add(this);
        return this;
    }

    public MainArtist removeAlbum(Album album) {
        this.albums.remove(album);
        album.getMainArtists().remove(this);
        return this;
    }

    public Set<SongArtistJoin> getSongArtistJoins() {
        return this.songArtistJoins;
    }

    public void setSongArtistJoins(Set<SongArtistJoin> songArtistJoins) {
        if (this.songArtistJoins != null) {
            this.songArtistJoins.forEach(i -> i.setMainArtist(null));
        }
        if (songArtistJoins != null) {
            songArtistJoins.forEach(i -> i.setMainArtist(this));
        }
        this.songArtistJoins = songArtistJoins;
    }

    public MainArtist songArtistJoins(Set<SongArtistJoin> songArtistJoins) {
        this.setSongArtistJoins(songArtistJoins);
        return this;
    }

    public MainArtist addSongArtistJoin(SongArtistJoin songArtistJoin) {
        this.songArtistJoins.add(songArtistJoin);
        songArtistJoin.setMainArtist(this);
        return this;
    }

    public MainArtist removeSongArtistJoin(SongArtistJoin songArtistJoin) {
        this.songArtistJoins.remove(songArtistJoin);
        songArtistJoin.setMainArtist(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MainArtist)) {
            return false;
        }
        return id != null && id.equals(((MainArtist) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MainArtist{" +
            "id=" + getId() +
            ", artistSpotifyID='" + getArtistSpotifyID() + "'" +
            ", artistName='" + getArtistName() + "'" +
            ", artistPopularity=" + getArtistPopularity() +
            ", artistImageSmall='" + getArtistImageSmall() + "'" +
            ", artistImageMedium='" + getArtistImageMedium() + "'" +
            ", artistImageLarge='" + getArtistImageLarge() + "'" +
            ", artistFollowers=" + getArtistFollowers() +
            ", dateAddedToDB='" + getDateAddedToDB() + "'" +
            ", dateLastModified='" + getDateLastModified() + "'" +
            ", musicbrainzID='" + getMusicbrainzID() + "'" +
            "}";
    }
}

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

/**
 * A Song.
 */
@Entity
@Table(name = "song_table")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Song implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "song_spotify_id", nullable = false)
    private String songSpotifyID;

    @NotNull
    @Column(name = "song_title", nullable = false)
    private String songTitle;

    @NotNull
    @Column(name = "song_duration", nullable = false)
    private Integer songDuration;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "song_album_type", nullable = false)
    private AlbumType songAlbumType;

    @NotNull
    @Column(name = "song_album_id", nullable = false)
    private String songAlbumID;

    @NotNull
    @Column(name = "song_explicit", nullable = false)
    private Boolean songExplicit;

    @NotNull
    @Column(name = "song_popularity", nullable = false)
    private Integer songPopularity;

    @Column(name = "song_preview_url")
    private String songPreviewURL;

    @NotNull
    @Column(name = "song_track_features_added", nullable = false)
    private Boolean songTrackFeaturesAdded;

    @Column(name = "song_acousticness")
    private Float songAcousticness;

    @Column(name = "song_danceability")
    private Float songDanceability;

    @Column(name = "song_energy")
    private Float songEnergy;

    @Column(name = "song_instrumentalness")
    private Float songInstrumentalness;

    @Column(name = "song_liveness")
    private Float songLiveness;

    @Column(name = "song_loudness")
    private Float songLoudness;

    @Column(name = "song_speechiness")
    private Float songSpeechiness;

    @Column(name = "song_tempo")
    private Float songTempo;

    @Column(name = "song_valence")
    private Float songValence;

    @Column(name = "song_key")
    private Integer songKey;

    @Column(name = "song_time_signature")
    private Integer songTimeSignature;

    @NotNull
    @Column(name = "song_date_added_to_db", nullable = false)
    private LocalDate songDateAddedToDB;

    @NotNull
    @Column(name = "song_date_last_modified", nullable = false)
    private LocalDate songDateLastModified;

    @OneToMany(mappedBy = "song")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "song" }, allowSetters = true)
    private Set<Contributor> contributors = new HashSet<>();

    @OneToMany(mappedBy = "song")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "song", "album", "mainArtist" }, allowSetters = true)
    private Set<SpotifyGenreEntity> spotifyGenreEntities = new HashSet<>();

    @OneToMany(mappedBy = "song")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "song", "album", "mainArtist" }, allowSetters = true)
    private Set<MusicbrainzGenreEntity> musicbrainzGenreEntities = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "songs", "spotifyGenreEntities", "musicbrainzGenreEntities", "mainArtist" }, allowSetters = true)
    private Album album;

    @OneToMany(mappedBy = "song")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "playlist", "song" }, allowSetters = true)
    private Set<PlaylistSongJoin> playlistSongJoins = new HashSet<>();

    @OneToMany(mappedBy = "song")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "song", "mainArtist" }, allowSetters = true)
    private Set<SongArtistJoin> songArtistJoins = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Song id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSongSpotifyID() {
        return this.songSpotifyID;
    }

    public Song songSpotifyID(String songSpotifyID) {
        this.setSongSpotifyID(songSpotifyID);
        return this;
    }

    public void setSongSpotifyID(String songSpotifyID) {
        this.songSpotifyID = songSpotifyID;
    }

    public String getSongTitle() {
        return this.songTitle;
    }

    public Song songTitle(String songTitle) {
        this.setSongTitle(songTitle);
        return this;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public Integer getSongDuration() {
        return this.songDuration;
    }

    public Song songDuration(Integer songDuration) {
        this.setSongDuration(songDuration);
        return this;
    }

    public void setSongDuration(Integer songDuration) {
        this.songDuration = songDuration;
    }

    public AlbumType getSongAlbumType() {
        return this.songAlbumType;
    }

    public Song songAlbumType(AlbumType songAlbumType) {
        this.setSongAlbumType(songAlbumType);
        return this;
    }

    public void setSongAlbumType(AlbumType songAlbumType) {
        this.songAlbumType = songAlbumType;
    }

    public String getSongAlbumID() {
        return this.songAlbumID;
    }

    public Song songAlbumID(String songAlbumID) {
        this.setSongAlbumID(songAlbumID);
        return this;
    }

    public void setSongAlbumID(String songAlbumID) {
        this.songAlbumID = songAlbumID;
    }

    public Boolean getSongExplicit() {
        return this.songExplicit;
    }

    public Song songExplicit(Boolean songExplicit) {
        this.setSongExplicit(songExplicit);
        return this;
    }

    public void setSongExplicit(Boolean songExplicit) {
        this.songExplicit = songExplicit;
    }

    public Integer getSongPopularity() {
        return this.songPopularity;
    }

    public Song songPopularity(Integer songPopularity) {
        this.setSongPopularity(songPopularity);
        return this;
    }

    public void setSongPopularity(Integer songPopularity) {
        this.songPopularity = songPopularity;
    }

    public String getSongPreviewURL() {
        return this.songPreviewURL;
    }

    public Song songPreviewURL(String songPreviewURL) {
        this.setSongPreviewURL(songPreviewURL);
        return this;
    }

    public void setSongPreviewURL(String songPreviewURL) {
        this.songPreviewURL = songPreviewURL;
    }

    public Boolean getSongTrackFeaturesAdded() {
        return this.songTrackFeaturesAdded;
    }

    public Song songTrackFeaturesAdded(Boolean songTrackFeaturesAdded) {
        this.setSongTrackFeaturesAdded(songTrackFeaturesAdded);
        return this;
    }

    public void setSongTrackFeaturesAdded(Boolean songTrackFeaturesAdded) {
        this.songTrackFeaturesAdded = songTrackFeaturesAdded;
    }

    public Float getSongAcousticness() {
        return this.songAcousticness;
    }

    public Song songAcousticness(Float songAcousticness) {
        this.setSongAcousticness(songAcousticness);
        return this;
    }

    public void setSongAcousticness(Float songAcousticness) {
        this.songAcousticness = songAcousticness;
    }

    public Float getSongDanceability() {
        return this.songDanceability;
    }

    public Song songDanceability(Float songDanceability) {
        this.setSongDanceability(songDanceability);
        return this;
    }

    public void setSongDanceability(Float songDanceability) {
        this.songDanceability = songDanceability;
    }

    public Float getSongEnergy() {
        return this.songEnergy;
    }

    public Song songEnergy(Float songEnergy) {
        this.setSongEnergy(songEnergy);
        return this;
    }

    public void setSongEnergy(Float songEnergy) {
        this.songEnergy = songEnergy;
    }

    public Float getSongInstrumentalness() {
        return this.songInstrumentalness;
    }

    public Song songInstrumentalness(Float songInstrumentalness) {
        this.setSongInstrumentalness(songInstrumentalness);
        return this;
    }

    public void setSongInstrumentalness(Float songInstrumentalness) {
        this.songInstrumentalness = songInstrumentalness;
    }

    public Float getSongLiveness() {
        return this.songLiveness;
    }

    public Song songLiveness(Float songLiveness) {
        this.setSongLiveness(songLiveness);
        return this;
    }

    public void setSongLiveness(Float songLiveness) {
        this.songLiveness = songLiveness;
    }

    public Float getSongLoudness() {
        return this.songLoudness;
    }

    public Song songLoudness(Float songLoudness) {
        this.setSongLoudness(songLoudness);
        return this;
    }

    public void setSongLoudness(Float songLoudness) {
        this.songLoudness = songLoudness;
    }

    public Float getSongSpeechiness() {
        return this.songSpeechiness;
    }

    public Song songSpeechiness(Float songSpeechiness) {
        this.setSongSpeechiness(songSpeechiness);
        return this;
    }

    public void setSongSpeechiness(Float songSpeechiness) {
        this.songSpeechiness = songSpeechiness;
    }

    public Float getSongTempo() {
        return this.songTempo;
    }

    public Song songTempo(Float songTempo) {
        this.setSongTempo(songTempo);
        return this;
    }

    public void setSongTempo(Float songTempo) {
        this.songTempo = songTempo;
    }

    public Float getSongValence() {
        return this.songValence;
    }

    public Song songValence(Float songValence) {
        this.setSongValence(songValence);
        return this;
    }

    public void setSongValence(Float songValence) {
        this.songValence = songValence;
    }

    public Integer getSongKey() {
        return this.songKey;
    }

    public Song songKey(Integer songKey) {
        this.setSongKey(songKey);
        return this;
    }

    public void setSongKey(Integer songKey) {
        this.songKey = songKey;
    }

    public Integer getSongTimeSignature() {
        return this.songTimeSignature;
    }

    public Song songTimeSignature(Integer songTimeSignature) {
        this.setSongTimeSignature(songTimeSignature);
        return this;
    }

    public void setSongTimeSignature(Integer songTimeSignature) {
        this.songTimeSignature = songTimeSignature;
    }

    public LocalDate getSongDateAddedToDB() {
        return this.songDateAddedToDB;
    }

    public Song songDateAddedToDB(LocalDate songDateAddedToDB) {
        this.setSongDateAddedToDB(songDateAddedToDB);
        return this;
    }

    public void setSongDateAddedToDB(LocalDate songDateAddedToDB) {
        this.songDateAddedToDB = songDateAddedToDB;
    }

    public LocalDate getSongDateLastModified() {
        return this.songDateLastModified;
    }

    public Song songDateLastModified(LocalDate songDateLastModified) {
        this.setSongDateLastModified(songDateLastModified);
        return this;
    }

    public void setSongDateLastModified(LocalDate songDateLastModified) {
        this.songDateLastModified = songDateLastModified;
    }

    public Set<Contributor> getContributors() {
        return this.contributors;
    }

    public void setContributors(Set<Contributor> contributors) {
        if (this.contributors != null) {
            this.contributors.forEach(i -> i.setSong(null));
        }
        if (contributors != null) {
            contributors.forEach(i -> i.setSong(this));
        }
        this.contributors = contributors;
    }

    public Song contributors(Set<Contributor> contributors) {
        this.setContributors(contributors);
        return this;
    }

    public Song addContributor(Contributor contributor) {
        this.contributors.add(contributor);
        contributor.setSong(this);
        return this;
    }

    public Song removeContributor(Contributor contributor) {
        this.contributors.remove(contributor);
        contributor.setSong(null);
        return this;
    }

    public Set<SpotifyGenreEntity> getSpotifyGenreEntities() {
        return this.spotifyGenreEntities;
    }

    public void setSpotifyGenreEntities(Set<SpotifyGenreEntity> spotifyGenreEntities) {
        if (this.spotifyGenreEntities != null) {
            this.spotifyGenreEntities.forEach(i -> i.setSong(null));
        }
        if (spotifyGenreEntities != null) {
            spotifyGenreEntities.forEach(i -> i.setSong(this));
        }
        this.spotifyGenreEntities = spotifyGenreEntities;
    }

    public Song spotifyGenreEntities(Set<SpotifyGenreEntity> spotifyGenreEntities) {
        this.setSpotifyGenreEntities(spotifyGenreEntities);
        return this;
    }

    public Song addSpotifyGenreEntity(SpotifyGenreEntity spotifyGenreEntity) {
        this.spotifyGenreEntities.add(spotifyGenreEntity);
        spotifyGenreEntity.setSong(this);
        return this;
    }

    public Song removeSpotifyGenreEntity(SpotifyGenreEntity spotifyGenreEntity) {
        this.spotifyGenreEntities.remove(spotifyGenreEntity);
        spotifyGenreEntity.setSong(null);
        return this;
    }

    public Set<MusicbrainzGenreEntity> getMusicbrainzGenreEntities() {
        return this.musicbrainzGenreEntities;
    }

    public void setMusicbrainzGenreEntities(Set<MusicbrainzGenreEntity> musicbrainzGenreEntities) {
        if (this.musicbrainzGenreEntities != null) {
            this.musicbrainzGenreEntities.forEach(i -> i.setSong(null));
        }
        if (musicbrainzGenreEntities != null) {
            musicbrainzGenreEntities.forEach(i -> i.setSong(this));
        }
        this.musicbrainzGenreEntities = musicbrainzGenreEntities;
    }

    public Song musicbrainzGenreEntities(Set<MusicbrainzGenreEntity> musicbrainzGenreEntities) {
        this.setMusicbrainzGenreEntities(musicbrainzGenreEntities);
        return this;
    }

    public Song addMusicbrainzGenreEntity(MusicbrainzGenreEntity musicbrainzGenreEntity) {
        this.musicbrainzGenreEntities.add(musicbrainzGenreEntity);
        musicbrainzGenreEntity.setSong(this);
        return this;
    }

    public Song removeMusicbrainzGenreEntity(MusicbrainzGenreEntity musicbrainzGenreEntity) {
        this.musicbrainzGenreEntities.remove(musicbrainzGenreEntity);
        musicbrainzGenreEntity.setSong(null);
        return this;
    }

    public Album getAlbum() {
        return this.album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Song album(Album album) {
        this.setAlbum(album);
        return this;
    }

    public Set<PlaylistSongJoin> getPlaylistSongJoins() {
        return this.playlistSongJoins;
    }

    public void setPlaylistSongJoins(Set<PlaylistSongJoin> playlistSongJoins) {
        if (this.playlistSongJoins != null) {
            this.playlistSongJoins.forEach(i -> i.setSong(null));
        }
        if (playlistSongJoins != null) {
            playlistSongJoins.forEach(i -> i.setSong(this));
        }
        this.playlistSongJoins = playlistSongJoins;
    }

    public Song playlistSongJoins(Set<PlaylistSongJoin> playlistSongJoins) {
        this.setPlaylistSongJoins(playlistSongJoins);
        return this;
    }

    public Song addPlaylistSongJoin(PlaylistSongJoin playlistSongJoin) {
        this.playlistSongJoins.add(playlistSongJoin);
        playlistSongJoin.setSong(this);
        return this;
    }

    public Song removePlaylistSongJoin(PlaylistSongJoin playlistSongJoin) {
        this.playlistSongJoins.remove(playlistSongJoin);
        playlistSongJoin.setSong(null);
        return this;
    }

    public Set<SongArtistJoin> getSongArtistJoins() {
        return this.songArtistJoins;
    }

    public void setSongArtistJoins(Set<SongArtistJoin> songArtistJoins) {
        if (this.songArtistJoins != null) {
            this.songArtistJoins.forEach(i -> i.setSong(null));
        }
        if (songArtistJoins != null) {
            songArtistJoins.forEach(i -> i.setSong(this));
        }
        this.songArtistJoins = songArtistJoins;
    }

    public Song songArtistJoins(Set<SongArtistJoin> songArtistJoins) {
        this.setSongArtistJoins(songArtistJoins);
        return this;
    }

    public Song addSongArtistJoin(SongArtistJoin songArtistJoin) {
        this.songArtistJoins.add(songArtistJoin);
        songArtistJoin.setSong(this);
        return this;
    }

    public Song removeSongArtistJoin(SongArtistJoin songArtistJoin) {
        this.songArtistJoins.remove(songArtistJoin);
        songArtistJoin.setSong(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Song)) {
            return false;
        }
        return id != null && id.equals(((Song) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Song{" +
            "id=" + getId() +
            ", songSpotifyID='" + getSongSpotifyID() + "'" +
            ", songTitle='" + getSongTitle() + "'" +
            ", songDuration=" + getSongDuration() +
            ", songAlbumType='" + getSongAlbumType() + "'" +
            ", songAlbumID='" + getSongAlbumID() + "'" +
            ", songExplicit='" + getSongExplicit() + "'" +
            ", songPopularity=" + getSongPopularity() +
            ", songPreviewURL='" + getSongPreviewURL() + "'" +
            ", songTrackFeaturesAdded='" + getSongTrackFeaturesAdded() + "'" +
            ", songAcousticness=" + getSongAcousticness() +
            ", songDanceability=" + getSongDanceability() +
            ", songEnergy=" + getSongEnergy() +
            ", songInstrumentalness=" + getSongInstrumentalness() +
            ", songLiveness=" + getSongLiveness() +
            ", songLoudness=" + getSongLoudness() +
            ", songSpeechiness=" + getSongSpeechiness() +
            ", songTempo=" + getSongTempo() +
            ", songValence=" + getSongValence() +
            ", songKey=" + getSongKey() +
            ", songTimeSignature=" + getSongTimeSignature() +
            ", songDateAddedToDB='" + getSongDateAddedToDB() + "'" +
            ", songDateLastModified='" + getSongDateLastModified() + "'" +
            "}";
    }
}

package team.bham.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MusicbrainzGenreEntity.
 */
@Entity
@Table(name = "musicbrainz_genre_entity")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MusicbrainzGenreEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "musicbrainz_genre", nullable = false)
    private String musicbrainzGenre;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "contributors", "spotifyGenreEntities", "musicbrainzGenreEntities", "album", "playlistSongJoins", "songArtistJoins" },
        allowSetters = true
    )
    private Song song;

    @ManyToOne
    @JsonIgnoreProperties(value = { "songs", "spotifyGenreEntities", "musicbrainzGenreEntities", "mainArtist" }, allowSetters = true)
    private Album album;

    @ManyToOne
    @JsonIgnoreProperties(value = { "albums", "spotifyGenreEntities", "musicbrainzGenreEntities", "songArtistJoins" }, allowSetters = true)
    private MainArtist mainArtist;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MusicbrainzGenreEntity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMusicbrainzGenre() {
        return this.musicbrainzGenre;
    }

    public MusicbrainzGenreEntity musicbrainzGenre(String musicbrainzGenre) {
        this.setMusicbrainzGenre(musicbrainzGenre);
        return this;
    }

    public void setMusicbrainzGenre(String musicbrainzGenre) {
        this.musicbrainzGenre = musicbrainzGenre;
    }

    public Song getSong() {
        return this.song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public MusicbrainzGenreEntity song(Song song) {
        this.setSong(song);
        return this;
    }

    public Album getAlbum() {
        return this.album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public MusicbrainzGenreEntity album(Album album) {
        this.setAlbum(album);
        return this;
    }

    public MainArtist getMainArtist() {
        return this.mainArtist;
    }

    public void setMainArtist(MainArtist mainArtist) {
        this.mainArtist = mainArtist;
    }

    public MusicbrainzGenreEntity mainArtist(MainArtist mainArtist) {
        this.setMainArtist(mainArtist);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MusicbrainzGenreEntity)) {
            return false;
        }
        return id != null && id.equals(((MusicbrainzGenreEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MusicbrainzGenreEntity{" +
            "id=" + getId() +
            ", musicbrainzGenre='" + getMusicbrainzGenre() + "'" +
            "}";
    }
}

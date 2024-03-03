package team.bham.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SongArtistJoin.
 */
@Entity
@Table(name = "song_artist_table")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SongArtistJoin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "top_track_index")
    private Integer topTrackIndex;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "contributors", "spotifyGenreEntities", "musicbrainzGenreEntities", "album", "playlistSongJoins", "songArtistJoins" },
        allowSetters = true
    )
    private Song song;

    @ManyToOne
    @JsonIgnoreProperties(value = { "albums", "spotifyGenreEntities", "musicbrainzGenreEntities", "songArtistJoins" }, allowSetters = true)
    private MainArtist mainArtist;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SongArtistJoin id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTopTrackIndex() {
        return this.topTrackIndex;
    }

    public SongArtistJoin topTrackIndex(Integer topTrackIndex) {
        this.setTopTrackIndex(topTrackIndex);
        return this;
    }

    public void setTopTrackIndex(Integer topTrackIndex) {
        this.topTrackIndex = topTrackIndex;
    }

    public Song getSong() {
        return this.song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public SongArtistJoin song(Song song) {
        this.setSong(song);
        return this;
    }

    public MainArtist getMainArtist() {
        return this.mainArtist;
    }

    public void setMainArtist(MainArtist mainArtist) {
        this.mainArtist = mainArtist;
    }

    public SongArtistJoin mainArtist(MainArtist mainArtist) {
        this.setMainArtist(mainArtist);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SongArtistJoin)) {
            return false;
        }
        return id != null && id.equals(((SongArtistJoin) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SongArtistJoin{" +
            "id=" + getId() +
            ", topTrackIndex=" + getTopTrackIndex() +
            "}";
    }
}

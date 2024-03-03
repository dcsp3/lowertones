package team.bham.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Contributor.
 */
@Entity
@Table(name = "contributor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Contributor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "role")
    private String role;

    @Column(name = "instrument")
    private String instrument;

    @Column(name = "musicbrainz_id")
    private String musicbrainzID;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "contributors", "spotifyGenreEntities", "musicbrainzGenreEntities", "album", "playlistSongJoins", "songArtistJoins" },
        allowSetters = true
    )
    private Song song;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Contributor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Contributor name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return this.role;
    }

    public Contributor role(String role) {
        this.setRole(role);
        return this;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getInstrument() {
        return this.instrument;
    }

    public Contributor instrument(String instrument) {
        this.setInstrument(instrument);
        return this;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getMusicbrainzID() {
        return this.musicbrainzID;
    }

    public Contributor musicbrainzID(String musicbrainzID) {
        this.setMusicbrainzID(musicbrainzID);
        return this;
    }

    public void setMusicbrainzID(String musicbrainzID) {
        this.musicbrainzID = musicbrainzID;
    }

    public Song getSong() {
        return this.song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public Contributor song(Song song) {
        this.setSong(song);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contributor)) {
            return false;
        }
        return id != null && id.equals(((Contributor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Contributor{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", role='" + getRole() + "'" +
            ", instrument='" + getInstrument() + "'" +
            ", musicbrainzID='" + getMusicbrainzID() + "'" +
            "}";
    }
}

package team.bham.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MusicBrainzSongAttribution.
 */
@Entity
@Table(name = "music_brainz_song_attribution")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MusicBrainzSongAttribution implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "recording_mbid")
    private String recordingMBID;

    @Column(name = "recording_title")
    private String recordingTitle;

    @Column(name = "song_main_artist_name")
    private String songMainArtistName;

    @Column(name = "song_main_artist_id")
    private Integer songMainArtistID;

    @Column(name = "song_contributor_mbid")
    private String songContributorMBID;

    @Column(name = "song_contributor_name")
    private String songContributorName;

    @Column(name = "song_contributor_role")
    private String songContributorRole;

    @Column(name = "song_contributor_instrument")
    private String songContributorInstrument;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MusicBrainzSongAttribution id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecordingMBID() {
        return this.recordingMBID;
    }

    public MusicBrainzSongAttribution recordingMBID(String recordingMBID) {
        this.setRecordingMBID(recordingMBID);
        return this;
    }

    public void setRecordingMBID(String recordingMBID) {
        this.recordingMBID = recordingMBID;
    }

    public String getRecordingTitle() {
        return this.recordingTitle;
    }

    public MusicBrainzSongAttribution recordingTitle(String recordingTitle) {
        this.setRecordingTitle(recordingTitle);
        return this;
    }

    public void setRecordingTitle(String recordingTitle) {
        this.recordingTitle = recordingTitle;
    }

    public String getSongMainArtistName() {
        return this.songMainArtistName;
    }

    public MusicBrainzSongAttribution songMainArtistName(String songMainArtistName) {
        this.setSongMainArtistName(songMainArtistName);
        return this;
    }

    public void setSongMainArtistName(String songMainArtistName) {
        this.songMainArtistName = songMainArtistName;
    }

    public Integer getSongMainArtistID() {
        return this.songMainArtistID;
    }

    public MusicBrainzSongAttribution songMainArtistID(Integer songMainArtistID) {
        this.setSongMainArtistID(songMainArtistID);
        return this;
    }

    public void setSongMainArtistID(Integer songMainArtistID) {
        this.songMainArtistID = songMainArtistID;
    }

    public String getSongContributorMBID() {
        return this.songContributorMBID;
    }

    public MusicBrainzSongAttribution songContributorMBID(String songContributorMBID) {
        this.setSongContributorMBID(songContributorMBID);
        return this;
    }

    public void setSongContributorMBID(String songContributorMBID) {
        this.songContributorMBID = songContributorMBID;
    }

    public String getSongContributorName() {
        return this.songContributorName;
    }

    public MusicBrainzSongAttribution songContributorName(String songContributorName) {
        this.setSongContributorName(songContributorName);
        return this;
    }

    public void setSongContributorName(String songContributorName) {
        this.songContributorName = songContributorName;
    }

    public String getSongContributorRole() {
        return this.songContributorRole;
    }

    public MusicBrainzSongAttribution songContributorRole(String songContributorRole) {
        this.setSongContributorRole(songContributorRole);
        return this;
    }

    public void setSongContributorRole(String songContributorRole) {
        this.songContributorRole = songContributorRole;
    }

    public String getSongContributorInstrument() {
        return this.songContributorInstrument;
    }

    public MusicBrainzSongAttribution songContributorInstrument(String songContributorInstrument) {
        this.setSongContributorInstrument(songContributorInstrument);
        return this;
    }

    public void setSongContributorInstrument(String songContributorInstrument) {
        this.songContributorInstrument = songContributorInstrument;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MusicBrainzSongAttribution)) {
            return false;
        }
        return id != null && id.equals(((MusicBrainzSongAttribution) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MusicBrainzSongAttribution{" +
            "id=" + getId() +
            ", recordingMBID='" + getRecordingMBID() + "'" +
            ", recordingTitle='" + getRecordingTitle() + "'" +
            ", songMainArtistName='" + getSongMainArtistName() + "'" +
            ", songMainArtistID=" + getSongMainArtistID() +
            ", songContributorMBID='" + getSongContributorMBID() + "'" +
            ", songContributorName='" + getSongContributorName() + "'" +
            ", songContributorRole='" + getSongContributorRole() + "'" +
            ", songContributorInstrument='" + getSongContributorInstrument() + "'" +
            "}";
    }
}

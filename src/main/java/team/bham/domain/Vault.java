package team.bham.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Vault.
 */
@Entity
@Table(name = "vault")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Vault implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "source_playlist_id")
    private String sourcePlaylistID;

    @Column(name = "playlist_name")
    private String playlistName;

    @Column(name = "result_playlist_id")
    private String resultPlaylistID;

    @Column(name = "frequency")
    private String frequency;

    @Column(name = "type")
    private String type;

    @Column(name = "playlist_cover_url")
    private String playlistCoverURL;

    @Column(name = "playlist_snapshot_id")
    private String playlistSnapshotID;

    @Column(name = "date_last_updated")
    private LocalDate dateLastUpdated;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vault id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return this.userId;
    }

    public Vault userId(Long userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSourcePlaylistID() {
        return this.sourcePlaylistID;
    }

    public Vault sourcePlaylistID(String sourcePlaylistID) {
        this.setSourcePlaylistID(sourcePlaylistID);
        return this;
    }

    public void setSourcePlaylistID(String sourcePlaylistID) {
        this.sourcePlaylistID = sourcePlaylistID;
    }

    public String getPlaylistName() {
        return this.playlistName;
    }

    public Vault playlistName(String playlistName) {
        this.setPlaylistName(playlistName);
        return this;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public String getResultPlaylistID() {
        return this.resultPlaylistID;
    }

    public Vault resultPlaylistID(String resultPlaylistID) {
        this.setResultPlaylistID(resultPlaylistID);
        return this;
    }

    public void setResultPlaylistID(String resultPlaylistID) {
        this.resultPlaylistID = resultPlaylistID;
    }

    public String getFrequency() {
        return this.frequency;
    }

    public Vault frequency(String frequency) {
        this.setFrequency(frequency);
        return this;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getType() {
        return this.type;
    }

    public Vault type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlaylistCoverURL() {
        return this.playlistCoverURL;
    }

    public Vault playlistCoverURL(String playlistCoverURL) {
        this.setPlaylistCoverURL(playlistCoverURL);
        return this;
    }

    public void setPlaylistCoverURL(String playlistCoverURL) {
        this.playlistCoverURL = playlistCoverURL;
    }

    public String getPlaylistSnapshotID() {
        return this.playlistSnapshotID;
    }

    public Vault playlistSnapshotID(String playlistSnapshotID) {
        this.setPlaylistSnapshotID(playlistSnapshotID);
        return this;
    }

    public void setPlaylistSnapshotID(String playlistSnapshotID) {
        this.playlistSnapshotID = playlistSnapshotID;
    }

    public LocalDate getDateLastUpdated() {
        return this.dateLastUpdated;
    }

    public Vault dateLastUpdated(LocalDate dateLastUpdated) {
        this.setDateLastUpdated(dateLastUpdated);
        return this;
    }

    public void setDateLastUpdated(LocalDate dateLastUpdated) {
        this.dateLastUpdated = dateLastUpdated;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vault)) {
            return false;
        }
        return id != null && id.equals(((Vault) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vault{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", sourcePlaylistID='" + getSourcePlaylistID() + "'" +
            ", playlistName='" + getPlaylistName() + "'" +
            ", resultPlaylistID='" + getResultPlaylistID() + "'" +
            ", frequency='" + getFrequency() + "'" +
            ", type='" + getType() + "'" +
            ", playlistCoverURL='" + getPlaylistCoverURL() + "'" +
            ", playlistSnapshotID='" + getPlaylistSnapshotID() + "'" +
            ", dateLastUpdated='" + getDateLastUpdated() + "'" +
            "}";
    }
}

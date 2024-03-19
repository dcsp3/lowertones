package team.bham.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RelatedArtists.
 */
@Entity
@Table(name = "related_artists")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RelatedArtists implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "main_artist_sptfy_id", nullable = false)
    private String mainArtistSptfyID;

    @NotNull
    @Column(name = "related_artist_spotify_id_1", nullable = false)
    private String relatedArtistSpotifyID1;

    @NotNull
    @Column(name = "related_artist_spotify_id_2", nullable = false)
    private String relatedArtistSpotifyID2;

    @NotNull
    @Column(name = "related_artist_spotify_id_3", nullable = false)
    private String relatedArtistSpotifyID3;

    @NotNull
    @Column(name = "related_artist_spotify_id_4", nullable = false)
    private String relatedArtistSpotifyID4;

    @NotNull
    @Column(name = "related_artist_spotify_id_5", nullable = false)
    private String relatedArtistSpotifyID5;

    @NotNull
    @Column(name = "related_artist_spotify_id_6", nullable = false)
    private String relatedArtistSpotifyID6;

    @NotNull
    @Column(name = "related_artist_spotify_id_7", nullable = false)
    private String relatedArtistSpotifyID7;

    @NotNull
    @Column(name = "related_artist_spotify_id_8", nullable = false)
    private String relatedArtistSpotifyID8;

    @NotNull
    @Column(name = "related_artist_spotify_id_9", nullable = false)
    private String relatedArtistSpotifyID9;

    @NotNull
    @Column(name = "related_artist_spotify_id_10", nullable = false)
    private String relatedArtistSpotifyID10;

    @NotNull
    @Column(name = "related_artist_spotify_id_11", nullable = false)
    private String relatedArtistSpotifyID11;

    @NotNull
    @Column(name = "related_artist_spotify_id_12", nullable = false)
    private String relatedArtistSpotifyID12;

    @NotNull
    @Column(name = "related_artist_spotify_id_13", nullable = false)
    private String relatedArtistSpotifyID13;

    @NotNull
    @Column(name = "related_artist_spotify_id_14", nullable = false)
    private String relatedArtistSpotifyID14;

    @NotNull
    @Column(name = "related_artist_spotify_id_15", nullable = false)
    private String relatedArtistSpotifyID15;

    @NotNull
    @Column(name = "related_artist_spotify_id_16", nullable = false)
    private String relatedArtistSpotifyID16;

    @NotNull
    @Column(name = "related_artist_spotify_id_17", nullable = false)
    private String relatedArtistSpotifyID17;

    @NotNull
    @Column(name = "related_artist_spotify_id_18", nullable = false)
    private String relatedArtistSpotifyID18;

    @NotNull
    @Column(name = "related_artist_spotify_id_19", nullable = false)
    private String relatedArtistSpotifyID19;

    @NotNull
    @Column(name = "related_artist_spotify_id_20", nullable = false)
    private String relatedArtistSpotifyID20;

    @JsonIgnoreProperties(
        value = { "relatedArtists", "albums", "spotifyGenreEntities", "musicbrainzGenreEntities", "songArtistJoins" },
        allowSetters = true
    )
    @OneToOne(mappedBy = "relatedArtists")
    private MainArtist mainArtist;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RelatedArtists id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMainArtistSptfyID() {
        return this.mainArtistSptfyID;
    }

    public RelatedArtists mainArtistSptfyID(String mainArtistSptfyID) {
        this.setMainArtistSptfyID(mainArtistSptfyID);
        return this;
    }

    public void setMainArtistSptfyID(String mainArtistSptfyID) {
        this.mainArtistSptfyID = mainArtistSptfyID;
    }

    public String getRelatedArtistSpotifyID1() {
        return this.relatedArtistSpotifyID1;
    }

    public RelatedArtists relatedArtistSpotifyID1(String relatedArtistSpotifyID1) {
        this.setRelatedArtistSpotifyID1(relatedArtistSpotifyID1);
        return this;
    }

    public void setRelatedArtistSpotifyID1(String relatedArtistSpotifyID1) {
        this.relatedArtistSpotifyID1 = relatedArtistSpotifyID1;
    }

    public String getRelatedArtistSpotifyID2() {
        return this.relatedArtistSpotifyID2;
    }

    public RelatedArtists relatedArtistSpotifyID2(String relatedArtistSpotifyID2) {
        this.setRelatedArtistSpotifyID2(relatedArtistSpotifyID2);
        return this;
    }

    public void setRelatedArtistSpotifyID2(String relatedArtistSpotifyID2) {
        this.relatedArtistSpotifyID2 = relatedArtistSpotifyID2;
    }

    public String getRelatedArtistSpotifyID3() {
        return this.relatedArtistSpotifyID3;
    }

    public RelatedArtists relatedArtistSpotifyID3(String relatedArtistSpotifyID3) {
        this.setRelatedArtistSpotifyID3(relatedArtistSpotifyID3);
        return this;
    }

    public void setRelatedArtistSpotifyID3(String relatedArtistSpotifyID3) {
        this.relatedArtistSpotifyID3 = relatedArtistSpotifyID3;
    }

    public String getRelatedArtistSpotifyID4() {
        return this.relatedArtistSpotifyID4;
    }

    public RelatedArtists relatedArtistSpotifyID4(String relatedArtistSpotifyID4) {
        this.setRelatedArtistSpotifyID4(relatedArtistSpotifyID4);
        return this;
    }

    public void setRelatedArtistSpotifyID4(String relatedArtistSpotifyID4) {
        this.relatedArtistSpotifyID4 = relatedArtistSpotifyID4;
    }

    public String getRelatedArtistSpotifyID5() {
        return this.relatedArtistSpotifyID5;
    }

    public RelatedArtists relatedArtistSpotifyID5(String relatedArtistSpotifyID5) {
        this.setRelatedArtistSpotifyID5(relatedArtistSpotifyID5);
        return this;
    }

    public void setRelatedArtistSpotifyID5(String relatedArtistSpotifyID5) {
        this.relatedArtistSpotifyID5 = relatedArtistSpotifyID5;
    }

    public String getRelatedArtistSpotifyID6() {
        return this.relatedArtistSpotifyID6;
    }

    public RelatedArtists relatedArtistSpotifyID6(String relatedArtistSpotifyID6) {
        this.setRelatedArtistSpotifyID6(relatedArtistSpotifyID6);
        return this;
    }

    public void setRelatedArtistSpotifyID6(String relatedArtistSpotifyID6) {
        this.relatedArtistSpotifyID6 = relatedArtistSpotifyID6;
    }

    public String getRelatedArtistSpotifyID7() {
        return this.relatedArtistSpotifyID7;
    }

    public RelatedArtists relatedArtistSpotifyID7(String relatedArtistSpotifyID7) {
        this.setRelatedArtistSpotifyID7(relatedArtistSpotifyID7);
        return this;
    }

    public void setRelatedArtistSpotifyID7(String relatedArtistSpotifyID7) {
        this.relatedArtistSpotifyID7 = relatedArtistSpotifyID7;
    }

    public String getRelatedArtistSpotifyID8() {
        return this.relatedArtistSpotifyID8;
    }

    public RelatedArtists relatedArtistSpotifyID8(String relatedArtistSpotifyID8) {
        this.setRelatedArtistSpotifyID8(relatedArtistSpotifyID8);
        return this;
    }

    public void setRelatedArtistSpotifyID8(String relatedArtistSpotifyID8) {
        this.relatedArtistSpotifyID8 = relatedArtistSpotifyID8;
    }

    public String getRelatedArtistSpotifyID9() {
        return this.relatedArtistSpotifyID9;
    }

    public RelatedArtists relatedArtistSpotifyID9(String relatedArtistSpotifyID9) {
        this.setRelatedArtistSpotifyID9(relatedArtistSpotifyID9);
        return this;
    }

    public void setRelatedArtistSpotifyID9(String relatedArtistSpotifyID9) {
        this.relatedArtistSpotifyID9 = relatedArtistSpotifyID9;
    }

    public String getRelatedArtistSpotifyID10() {
        return this.relatedArtistSpotifyID10;
    }

    public RelatedArtists relatedArtistSpotifyID10(String relatedArtistSpotifyID10) {
        this.setRelatedArtistSpotifyID10(relatedArtistSpotifyID10);
        return this;
    }

    public void setRelatedArtistSpotifyID10(String relatedArtistSpotifyID10) {
        this.relatedArtistSpotifyID10 = relatedArtistSpotifyID10;
    }

    public String getRelatedArtistSpotifyID11() {
        return this.relatedArtistSpotifyID11;
    }

    public RelatedArtists relatedArtistSpotifyID11(String relatedArtistSpotifyID11) {
        this.setRelatedArtistSpotifyID11(relatedArtistSpotifyID11);
        return this;
    }

    public void setRelatedArtistSpotifyID11(String relatedArtistSpotifyID11) {
        this.relatedArtistSpotifyID11 = relatedArtistSpotifyID11;
    }

    public String getRelatedArtistSpotifyID12() {
        return this.relatedArtistSpotifyID12;
    }

    public RelatedArtists relatedArtistSpotifyID12(String relatedArtistSpotifyID12) {
        this.setRelatedArtistSpotifyID12(relatedArtistSpotifyID12);
        return this;
    }

    public void setRelatedArtistSpotifyID12(String relatedArtistSpotifyID12) {
        this.relatedArtistSpotifyID12 = relatedArtistSpotifyID12;
    }

    public String getRelatedArtistSpotifyID13() {
        return this.relatedArtistSpotifyID13;
    }

    public RelatedArtists relatedArtistSpotifyID13(String relatedArtistSpotifyID13) {
        this.setRelatedArtistSpotifyID13(relatedArtistSpotifyID13);
        return this;
    }

    public void setRelatedArtistSpotifyID13(String relatedArtistSpotifyID13) {
        this.relatedArtistSpotifyID13 = relatedArtistSpotifyID13;
    }

    public String getRelatedArtistSpotifyID14() {
        return this.relatedArtistSpotifyID14;
    }

    public RelatedArtists relatedArtistSpotifyID14(String relatedArtistSpotifyID14) {
        this.setRelatedArtistSpotifyID14(relatedArtistSpotifyID14);
        return this;
    }

    public void setRelatedArtistSpotifyID14(String relatedArtistSpotifyID14) {
        this.relatedArtistSpotifyID14 = relatedArtistSpotifyID14;
    }

    public String getRelatedArtistSpotifyID15() {
        return this.relatedArtistSpotifyID15;
    }

    public RelatedArtists relatedArtistSpotifyID15(String relatedArtistSpotifyID15) {
        this.setRelatedArtistSpotifyID15(relatedArtistSpotifyID15);
        return this;
    }

    public void setRelatedArtistSpotifyID15(String relatedArtistSpotifyID15) {
        this.relatedArtistSpotifyID15 = relatedArtistSpotifyID15;
    }

    public String getRelatedArtistSpotifyID16() {
        return this.relatedArtistSpotifyID16;
    }

    public RelatedArtists relatedArtistSpotifyID16(String relatedArtistSpotifyID16) {
        this.setRelatedArtistSpotifyID16(relatedArtistSpotifyID16);
        return this;
    }

    public void setRelatedArtistSpotifyID16(String relatedArtistSpotifyID16) {
        this.relatedArtistSpotifyID16 = relatedArtistSpotifyID16;
    }

    public String getRelatedArtistSpotifyID17() {
        return this.relatedArtistSpotifyID17;
    }

    public RelatedArtists relatedArtistSpotifyID17(String relatedArtistSpotifyID17) {
        this.setRelatedArtistSpotifyID17(relatedArtistSpotifyID17);
        return this;
    }

    public void setRelatedArtistSpotifyID17(String relatedArtistSpotifyID17) {
        this.relatedArtistSpotifyID17 = relatedArtistSpotifyID17;
    }

    public String getRelatedArtistSpotifyID18() {
        return this.relatedArtistSpotifyID18;
    }

    public RelatedArtists relatedArtistSpotifyID18(String relatedArtistSpotifyID18) {
        this.setRelatedArtistSpotifyID18(relatedArtistSpotifyID18);
        return this;
    }

    public void setRelatedArtistSpotifyID18(String relatedArtistSpotifyID18) {
        this.relatedArtistSpotifyID18 = relatedArtistSpotifyID18;
    }

    public String getRelatedArtistSpotifyID19() {
        return this.relatedArtistSpotifyID19;
    }

    public RelatedArtists relatedArtistSpotifyID19(String relatedArtistSpotifyID19) {
        this.setRelatedArtistSpotifyID19(relatedArtistSpotifyID19);
        return this;
    }

    public void setRelatedArtistSpotifyID19(String relatedArtistSpotifyID19) {
        this.relatedArtistSpotifyID19 = relatedArtistSpotifyID19;
    }

    public String getRelatedArtistSpotifyID20() {
        return this.relatedArtistSpotifyID20;
    }

    public RelatedArtists relatedArtistSpotifyID20(String relatedArtistSpotifyID20) {
        this.setRelatedArtistSpotifyID20(relatedArtistSpotifyID20);
        return this;
    }

    public void setRelatedArtistSpotifyID20(String relatedArtistSpotifyID20) {
        this.relatedArtistSpotifyID20 = relatedArtistSpotifyID20;
    }

    public MainArtist getMainArtist() {
        return this.mainArtist;
    }

    public void setMainArtist(MainArtist mainArtist) {
        if (this.mainArtist != null) {
            this.mainArtist.setRelatedArtists(null);
        }
        if (mainArtist != null) {
            mainArtist.setRelatedArtists(this);
        }
        this.mainArtist = mainArtist;
    }

    public RelatedArtists mainArtist(MainArtist mainArtist) {
        this.setMainArtist(mainArtist);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RelatedArtists)) {
            return false;
        }
        return id != null && id.equals(((RelatedArtists) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RelatedArtists{" +
            "id=" + getId() +
            ", mainArtistSptfyID='" + getMainArtistSptfyID() + "'" +
            ", relatedArtistSpotifyID1='" + getRelatedArtistSpotifyID1() + "'" +
            ", relatedArtistSpotifyID2='" + getRelatedArtistSpotifyID2() + "'" +
            ", relatedArtistSpotifyID3='" + getRelatedArtistSpotifyID3() + "'" +
            ", relatedArtistSpotifyID4='" + getRelatedArtistSpotifyID4() + "'" +
            ", relatedArtistSpotifyID5='" + getRelatedArtistSpotifyID5() + "'" +
            ", relatedArtistSpotifyID6='" + getRelatedArtistSpotifyID6() + "'" +
            ", relatedArtistSpotifyID7='" + getRelatedArtistSpotifyID7() + "'" +
            ", relatedArtistSpotifyID8='" + getRelatedArtistSpotifyID8() + "'" +
            ", relatedArtistSpotifyID9='" + getRelatedArtistSpotifyID9() + "'" +
            ", relatedArtistSpotifyID10='" + getRelatedArtistSpotifyID10() + "'" +
            ", relatedArtistSpotifyID11='" + getRelatedArtistSpotifyID11() + "'" +
            ", relatedArtistSpotifyID12='" + getRelatedArtistSpotifyID12() + "'" +
            ", relatedArtistSpotifyID13='" + getRelatedArtistSpotifyID13() + "'" +
            ", relatedArtistSpotifyID14='" + getRelatedArtistSpotifyID14() + "'" +
            ", relatedArtistSpotifyID15='" + getRelatedArtistSpotifyID15() + "'" +
            ", relatedArtistSpotifyID16='" + getRelatedArtistSpotifyID16() + "'" +
            ", relatedArtistSpotifyID17='" + getRelatedArtistSpotifyID17() + "'" +
            ", relatedArtistSpotifyID18='" + getRelatedArtistSpotifyID18() + "'" +
            ", relatedArtistSpotifyID19='" + getRelatedArtistSpotifyID19() + "'" +
            ", relatedArtistSpotifyID20='" + getRelatedArtistSpotifyID20() + "'" +
            "}";
    }
}

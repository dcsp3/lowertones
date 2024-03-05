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
 * A AppUser.
 */
@Entity
@Table(name = "app_user_table")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "spotify_user_id", nullable = false)
    private String spotifyUserID;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Size(max = 1000)
    @Column(name = "spotify_refresh_token", length = 1000)
    private String spotifyRefreshToken;

    @Size(max = 1000)
    @Column(name = "spotify_auth_token", length = 1000)
    private String spotifyAuthToken;

    @NotNull
    @Column(name = "last_login_date", nullable = false)
    private LocalDate lastLoginDate;

    @NotNull
    @Column(name = "discover_weekly_buffer_settings", nullable = false)
    private Integer discoverWeeklyBufferSettings;

    @Column(name = "discover_weekly_buffer_playlist_id")
    private String discoverWeeklyBufferPlaylistID;

    @NotNull
    @Column(name = "dark_mode", nullable = false)
    private Boolean darkMode;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "appUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appUser", "playlistSongJoins" }, allowSetters = true)
    private Set<Playlist> playlists = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AppUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpotifyUserID() {
        return this.spotifyUserID;
    }

    public AppUser spotifyUserID(String spotifyUserID) {
        this.setSpotifyUserID(spotifyUserID);
        return this;
    }

    public void setSpotifyUserID(String spotifyUserID) {
        this.spotifyUserID = spotifyUserID;
    }

    public String getName() {
        return this.name;
    }

    public AppUser name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public AppUser email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpotifyRefreshToken() {
        return this.spotifyRefreshToken;
    }

    public AppUser spotifyRefreshToken(String spotifyRefreshToken) {
        this.setSpotifyRefreshToken(spotifyRefreshToken);
        return this;
    }

    public void setSpotifyRefreshToken(String spotifyRefreshToken) {
        this.spotifyRefreshToken = spotifyRefreshToken;
    }

    public String getSpotifyAuthToken() {
        return this.spotifyAuthToken;
    }

    public AppUser spotifyAuthToken(String spotifyAuthToken) {
        this.setSpotifyAuthToken(spotifyAuthToken);
        return this;
    }

    public void setSpotifyAuthToken(String spotifyAuthToken) {
        this.spotifyAuthToken = spotifyAuthToken;
    }

    public LocalDate getLastLoginDate() {
        return this.lastLoginDate;
    }

    public AppUser lastLoginDate(LocalDate lastLoginDate) {
        this.setLastLoginDate(lastLoginDate);
        return this;
    }

    public void setLastLoginDate(LocalDate lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public Integer getDiscoverWeeklyBufferSettings() {
        return this.discoverWeeklyBufferSettings;
    }

    public AppUser discoverWeeklyBufferSettings(Integer discoverWeeklyBufferSettings) {
        this.setDiscoverWeeklyBufferSettings(discoverWeeklyBufferSettings);
        return this;
    }

    public void setDiscoverWeeklyBufferSettings(Integer discoverWeeklyBufferSettings) {
        this.discoverWeeklyBufferSettings = discoverWeeklyBufferSettings;
    }

    public String getDiscoverWeeklyBufferPlaylistID() {
        return this.discoverWeeklyBufferPlaylistID;
    }

    public AppUser discoverWeeklyBufferPlaylistID(String discoverWeeklyBufferPlaylistID) {
        this.setDiscoverWeeklyBufferPlaylistID(discoverWeeklyBufferPlaylistID);
        return this;
    }

    public void setDiscoverWeeklyBufferPlaylistID(String discoverWeeklyBufferPlaylistID) {
        this.discoverWeeklyBufferPlaylistID = discoverWeeklyBufferPlaylistID;
    }

    public Boolean getDarkMode() {
        return this.darkMode;
    }

    public AppUser darkMode(Boolean darkMode) {
        this.setDarkMode(darkMode);
        return this;
    }

    public void setDarkMode(Boolean darkMode) {
        this.darkMode = darkMode;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AppUser user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Playlist> getPlaylists() {
        return this.playlists;
    }

    public void setPlaylists(Set<Playlist> playlists) {
        if (this.playlists != null) {
            this.playlists.forEach(i -> i.setAppUser(null));
        }
        if (playlists != null) {
            playlists.forEach(i -> i.setAppUser(this));
        }
        this.playlists = playlists;
    }

    public AppUser playlists(Set<Playlist> playlists) {
        this.setPlaylists(playlists);
        return this;
    }

    public AppUser addPlaylist(Playlist playlist) {
        this.playlists.add(playlist);
        playlist.setAppUser(this);
        return this;
    }

    public AppUser removePlaylist(Playlist playlist) {
        this.playlists.remove(playlist);
        playlist.setAppUser(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppUser)) {
            return false;
        }
        return id != null && id.equals(((AppUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppUser{" +
            "id=" + getId() +
            ", spotifyUserID='" + getSpotifyUserID() + "'" +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", spotifyRefreshToken='" + getSpotifyRefreshToken() + "'" +
            ", spotifyAuthToken='" + getSpotifyAuthToken() + "'" +
            ", lastLoginDate='" + getLastLoginDate() + "'" +
            ", discoverWeeklyBufferSettings=" + getDiscoverWeeklyBufferSettings() +
            ", discoverWeeklyBufferPlaylistID='" + getDiscoverWeeklyBufferPlaylistID() + "'" +
            ", darkMode='" + getDarkMode() + "'" +
            "}";
    }
}

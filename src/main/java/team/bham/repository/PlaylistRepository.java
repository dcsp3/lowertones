package team.bham.repository;

import java.util.ArrayList;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import team.bham.domain.Playlist;

/**
 * Spring Data JPA repository for the Playlist entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    @Query("SELECT p FROM Playlist p WHERE p.playlistSpotifyID = ?1")
    Playlist findPlaylistBySpotifyId(String spotifyId);

    @Query("SELECT p FROM Playlist p WHERE p.appUser.id = ?1")
    ArrayList<Playlist> findPlaylistsByAppUserID(long appUserID);
}

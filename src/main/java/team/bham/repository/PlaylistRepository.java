package team.bham.repository;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
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
    Set<Playlist> findPlaylistsByAppUserID(long appUserID);

    @EntityGraph(
        attributePaths = {
            "playlistSongJoins",
            "playlistSongJoins.song",
            "playlistSongJoins.song.songArtistJoins",
            "playlistSongJoins.song.songArtistJoins.mainArtist",
            "playlistSongJoins.song.songArtistJoins.mainArtist.spotifyGenreEntities",
        }
    )
    Optional<Playlist> findById(Long id);

    @Query(
        "SELECT COUNT(DISTINCT song.id) FROM Playlist p " +
        "JOIN p.playlistSongJoins psj " +
        "JOIN psj.song song " +
        "JOIN song.songArtistJoins saj " +
        "JOIN saj.mainArtist artist " +
        "WHERE artist.artistSpotifyID = :artistSpotifyId " +
        "AND p.appUser.id = :userId"
    )
    int countSongsByArtistInUserLibrary(@Param("userId") Long userId, @Param("artistSpotifyId") String artistSpotifyId);
}

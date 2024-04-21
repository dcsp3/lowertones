package team.bham.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team.bham.domain.PlaylistSongJoin;
import team.bham.domain.Song;

/**
 * Spring Data JPA repository for the PlaylistSongJoin entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlaylistSongJoinRepository extends JpaRepository<PlaylistSongJoin, Long> {
    @Query("SELECT ps.song FROM PlaylistSongJoin ps WHERE ps.playlist.id = :playlistId")
    List<Song> findSongsByPlaylistId(@Param("playlistId") Long playlistId);

    @Query(
        "SELECT g.spotifyGenre AS genre, COUNT(g) AS genreCount " +
        "FROM PlaylistSongJoin psj " +
        "JOIN psj.song s " +
        "JOIN s.songArtistJoins saj " +
        "JOIN saj.mainArtist ma " +
        "JOIN ma.spotifyGenreEntities g " +
        "WHERE psj.playlist.id = :playlistId " +
        "GROUP BY g.spotifyGenre " +
        "ORDER BY genreCount DESC"
    )
    List<Object[]> findMainGenreByPlaylistIdUsingArtists(@Param("playlistId") Long playlistId);

    @Query(
        "SELECT COUNT(psj) " +
        "FROM PlaylistSongJoin psj " +
        "JOIN psj.song s " +
        "JOIN s.songArtistJoins saj " +
        "JOIN saj.mainArtist ma " +
        "WHERE ma.artistSpotifyID = :artistSpotifyId AND psj.playlist.id = :playlistId"
    )
    int countSongsByArtistInPlaylist(@Param("artistSpotifyId") String artistSpotifyId, @Param("playlistId") Long playlistId);
}

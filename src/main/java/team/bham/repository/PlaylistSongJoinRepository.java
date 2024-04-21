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
        "SELECT g.spotifyGenre AS genre, COUNT(g) AS count FROM PlaylistSongJoin psj " +
        "JOIN psj.song s " +
        "JOIN s.spotifyGenreEntities g " +
        "WHERE psj.playlist.id = :playlistId " +
        "GROUP BY g.spotifyGenre " +
        "ORDER BY COUNT(g) DESC"
    )
    List<Object[]> findMainGenreByPlaylistId(@Param("playlistId") Long playlistId);
}

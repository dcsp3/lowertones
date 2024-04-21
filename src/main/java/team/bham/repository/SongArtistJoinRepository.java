package team.bham.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team.bham.domain.MainArtist;
import team.bham.domain.Song;
import team.bham.domain.SongArtistJoin;

/**
 * Spring Data JPA repository for the SongArtistJoin entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SongArtistJoinRepository extends JpaRepository<SongArtistJoin, Long> {
    @Query(
        "SELECT a FROM MainArtist a JOIN a.songArtistJoins saj JOIN saj.song s JOIN s.playlistSongJoins psj JOIN psj.playlist p WHERE p.id = :playlistId ORDER BY a.artistPopularity DESC"
    )
    Page<MainArtist> findMostPopularArtistByPlaylistId(@Param("playlistId") Long playlistId, Pageable pageable);

    @Query(
        "SELECT sa.song FROM SongArtistJoin sa JOIN sa.song.playlistSongJoins psj WHERE psj.playlist.id = :playlistId AND sa.mainArtist.id = :artistId AND sa.song.songPopularity = (SELECT MAX(s.songPopularity) FROM Song s JOIN s.playlistSongJoins psj2 WHERE psj2.playlist.id = :playlistId AND EXISTS (SELECT saj FROM SongArtistJoin saj WHERE saj.song.id = s.id AND saj.mainArtist.id = :artistId))"
    )
    Song findTopTrackByPopularArtistInPlaylist(@Param("artistId") Long artistId, @Param("playlistId") Long playlistId);
}

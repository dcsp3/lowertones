package team.bham.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team.bham.domain.MainArtist;
import team.bham.domain.Song;
import team.bham.repository.SongWithArtistName;

/**
 * Spring Data JPA repository for the Song entity.
 *
 * When extending this class, extend SongRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */

@Repository
public interface SongRepository extends SongRepositoryWithBagRelationships, JpaRepository<Song, Long> {
    default Optional<Song> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Song> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Song> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }

    @Query("SELECT s FROM Song s WHERE s.songSpotifyID IN :spotifyIds")
    List<Song> findBySongSpotifyIDIn(@Param("spotifyIds") List<String> spotifyIds);

    @Query("SELECT s FROM Song s WHERE s.songSpotifyID = ?1")
    Song findSongBySpotifyId(String spotifyId);

    @Query(
        value = "SELECT S.SONG_SPOTIFY_ID as songSpotifyId, S.SONG_TITLE as songTitle, S.SONG_DURATION as songDuration, S.SONG_EXPLICIT as songExplicit, S.SONG_POPULARITY as songPopularity, S.SONG_ACOUSTICNESS as songAcousticness, S.SONG_DANCEABILITY as songDanceability, S.SONG_ENERGY as songEnergy, S.SONG_INSTRUMENTALNESS as songInstrumentalness, S.SONG_LIVENESS as songLiveness, S.SONG_LOUDNESS as songLoudness, S.SONG_SPEECHINESS as songSpeechiness, S.SONG_TEMPO as songTempo, S.SONG_VALENCE as songValence, AR.ARTIST_NAME as artistName, A.ALBUM_RELEASE_DATE as albumReleaseDate " +
        "FROM SONG_TABLE S " +
        "JOIN PLAYLIST_SONG_TABLE PSJ ON S.ID = PSJ.SONG_ID " +
        "JOIN PLAYLIST_TABLE P ON PSJ.PLAYLIST_ID = P.ID " +
        "JOIN ALBUM_TABLE A ON S.SONG_ALBUM_ID = A.ALBUM_SPOTIFY_ID " +
        "JOIN REL_ARTISTS_TABLE__ALBUM RATA ON A.ID = RATA.ALBUM_ID " +
        "JOIN ARTISTS_TABLE AR ON RATA.ARTISTS_TABLE_ID = AR.ID " +
        "WHERE P.PLAYLIST_SPOTIFY_ID = :playlistId AND (S.SONG_TITLE <> '' AND S.SONG_DURATION <> 0) " +
        "UNION " +
        "SELECT S.SONG_SPOTIFY_ID as songSpotifyId, S.SONG_TITLE as songTitle, S.SONG_DURATION as songDuration, S.SONG_EXPLICIT as songExplicit, S.SONG_POPULARITY as songPopularity, S.SONG_ACOUSTICNESS as songAcousticness, S.SONG_DANCEABILITY as songDanceability, S.SONG_ENERGY as songEnergy, S.SONG_INSTRUMENTALNESS as songInstrumentalness, S.SONG_LIVENESS as songLiveness, S.SONG_LOUDNESS as songLoudness, S.SONG_SPEECHINESS as songSpeechiness, S.SONG_TEMPO as songTempo, S.SONG_VALENCE as songValence, AR.ARTIST_NAME as artistName, A.ALBUM_RELEASE_DATE as albumReleaseDate " +
        "FROM SONG_TABLE S " +
        "JOIN ALBUM_TABLE A ON S.SONG_ALBUM_ID = A.ALBUM_SPOTIFY_ID " +
        "JOIN PLAYLIST_SONG_TABLE PSJ ON S.ID = PSJ.SONG_ID " +
        "JOIN PLAYLIST_TABLE P ON PSJ.PLAYLIST_ID = P.ID " +
        "JOIN SONG_ARTIST_TABLE SA ON S.ID = SA.SONG_ID " +
        "JOIN ARTISTS_TABLE AR ON SA.MAIN_ARTIST_ID = AR.ID " +
        "WHERE P.PLAYLIST_SPOTIFY_ID = :playlistId AND (S.SONG_TITLE <> '' AND S.SONG_DURATION <> 0);",
        nativeQuery = true
    )
    List<SongWithArtistName> findSongsByPlaylistId(@Param("playlistId") String playlistId);

    @Query(
        value = "SELECT S.SONG_SPOTIFY_ID as songSpotifyId, C.NAME as contributorName, C.ROLE as contributorRole, C.INSTRUMENT as contributorInstrument " +
        "FROM SONG_TABLE S " +
        "JOIN PLAYLIST_SONG_TABLE PSJ ON S.ID = PSJ.SONG_ID " +
        "JOIN PLAYLIST_TABLE P ON PSJ.PLAYLIST_ID = P.ID " +
        "JOIN REL_SONG_TABLE__CONTRIBUTOR RSTC ON S.ID = RSTC.SONG_TABLE_ID " +
        "JOIN CONTRIBUTOR C ON RSTC.CONTRIBUTOR_ID = C.ID " +
        "WHERE P.PLAYLIST_SPOTIFY_ID = :playlistId AND (S.SONG_TITLE <> '' AND S.SONG_DURATION <> 0);",
        nativeQuery = true
    )
    List<SongWithCollaborators> findSongsCollaboratorsByPlaylistId(@Param("playlistId") String playlistId);

    @Query(
        value = "SELECT S.SONG_SPOTIFY_ID as songSpotifyId, S.SONG_TITLE as songTitle, S.SONG_DURATION as songDuration, S.SONG_EXPLICIT as songExplicit, S.SONG_POPULARITY as songPopularity, S.SONG_ACOUSTICNESS as songAcousticness, S.SONG_DANCEABILITY as songDanceability, S.SONG_ENERGY as songEnergy, S.SONG_INSTRUMENTALNESS as songInstrumentalness, S.SONG_LIVENESS as songLiveness, S.SONG_LOUDNESS as songLoudness, S.SONG_SPEECHINESS as songSpeechiness, S.SONG_TEMPO as songTempo, S.SONG_VALENCE as songValence, AR.ARTIST_NAME as artistName, A.ALBUM_RELEASE_DATE as albumReleaseDate " +
        "FROM SONG_TABLE S " +
        "JOIN PLAYLIST_SONG_TABLE PSJ ON S.ID = PSJ.SONG_ID " +
        "JOIN PLAYLIST_TABLE P ON PSJ.PLAYLIST_ID = P.ID " +
        "JOIN ALBUM_TABLE A ON S.SONG_ALBUM_ID = A.ALBUM_SPOTIFY_ID " +
        "JOIN REL_ARTISTS_TABLE__ALBUM RATA ON A.ID = RATA.ALBUM_ID " +
        "JOIN ARTISTS_TABLE AR ON RATA.ARTISTS_TABLE_ID = AR.ID " +
        "WHERE P.PLAYLIST_SPOTIFY_ID = :playlistId AND (S.SONG_TITLE <> '' AND S.SONG_DURATION <> 0) " +
        "UNION " +
        "SELECT S.SONG_SPOTIFY_ID as songSpotifyId, S.SONG_TITLE as songTitle, S.SONG_DURATION as songDuration, S.SONG_EXPLICIT as songExplicit, S.SONG_POPULARITY as songPopularity, S.SONG_ACOUSTICNESS as songAcousticness, S.SONG_DANCEABILITY as songDanceability, S.SONG_ENERGY as songEnergy, S.SONG_INSTRUMENTALNESS as songInstrumentalness, S.SONG_LIVENESS as songLiveness, S.SONG_LOUDNESS as songLoudness, S.SONG_SPEECHINESS as songSpeechiness, S.SONG_TEMPO as songTempo, S.SONG_VALENCE as songValence, AR.ARTIST_NAME as artistName, A.ALBUM_RELEASE_DATE as albumReleaseDate " +
        "FROM SONG_TABLE S " +
        "JOIN ALBUM_TABLE A ON S.SONG_ALBUM_ID = A.ALBUM_SPOTIFY_ID " +
        "JOIN PLAYLIST_SONG_TABLE PSJ ON S.ID = PSJ.SONG_ID " +
        "JOIN PLAYLIST_TABLE P ON PSJ.PLAYLIST_ID = P.ID " +
        "JOIN SONG_ARTIST_TABLE SA ON S.ID = SA.SONG_ID " +
        "JOIN ARTISTS_TABLE AR ON SA.MAIN_ARTIST_ID = AR.ID " +
        "WHERE P.PLAYLIST_SPOTIFY_ID = :playlistId AND (S.SONG_TITLE <> '' AND S.SONG_DURATION <> 0);",
        nativeQuery = true
    )
    List<SongWithArtistName> findSongsByLowertonesLibrary(@Param("playlistId") String whereQuery);
}

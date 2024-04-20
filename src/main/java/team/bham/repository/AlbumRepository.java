package team.bham.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import team.bham.domain.Album;

/**
 * Spring Data JPA repository for the Album entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    @Query("SELECT a FROM Album a WHERE a.albumSpotifyID = ?1")
    public Album findAlbumBySpotifyId(String spotifyId);

    List<Album> findByAlbumSpotifyIDIn(List<String> albumSpotifyIDs);
}

package team.bham.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team.bham.domain.MainArtist;
import team.bham.service.dto.NetworkDTO;

/**
 * Spring Data JPA repository for the MainArtist entity.
 *
 * When extending this class, extend MainArtistRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface MainArtistRepository extends MainArtistRepositoryWithBagRelationships, JpaRepository<MainArtist, Long> {
    default Optional<MainArtist> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<MainArtist> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<MainArtist> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }

    List<MainArtist> findByArtistSpotifyIDIn(List<String> artistSpotifyIDs);

    @Query("SELECT a FROM MainArtist a WHERE a.artistSpotifyID = ?1")
    MainArtist findArtistBySpotifyId(String spotifyId);

    @Query(
        "SELECT a " +
        "FROM MainArtist a " +
        "JOIN a.songArtistJoins saj " +
        "JOIN saj.song s " +
        "JOIN s.playlistSongJoins psj " +
        "WHERE psj.playlist.id = :playlistId " +
        "GROUP BY a.id, a.artistName, a.artistSpotifyID, a.artistImageSmall, a.artistImageMedium, a.artistImageLarge"
    )
    List<MainArtist> findArtistDetailsByPlaylistId(@Param("playlistId") Long playlistId);
}

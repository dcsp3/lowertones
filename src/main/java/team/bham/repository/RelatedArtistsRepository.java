package team.bham.repository;

import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team.bham.domain.RelatedArtists;

/**
 * Spring Data JPA repository for the RelatedArtists entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RelatedArtistsRepository extends JpaRepository<RelatedArtists, Long> {
    @Query("SELECT ra FROM RelatedArtists ra WHERE ra.mainArtistSptfyID IN :artistSpotifyIDs")
    List<RelatedArtists> findRelatedArtistsByMainArtistSpotifyIDs(@Param("artistSpotifyIDs") Set<String> artistSpotifyIDs);
}

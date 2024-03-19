package team.bham.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import team.bham.domain.RelatedArtists;

/**
 * Spring Data JPA repository for the RelatedArtists entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RelatedArtistsRepository extends JpaRepository<RelatedArtists, Long> {}

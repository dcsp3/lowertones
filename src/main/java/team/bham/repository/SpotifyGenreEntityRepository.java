package team.bham.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import team.bham.domain.SpotifyGenreEntity;

/**
 * Spring Data JPA repository for the SpotifyGenreEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpotifyGenreEntityRepository extends JpaRepository<SpotifyGenreEntity, Long> {}

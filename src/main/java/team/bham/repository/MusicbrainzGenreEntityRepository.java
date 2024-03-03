package team.bham.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import team.bham.domain.MusicbrainzGenreEntity;

/**
 * Spring Data JPA repository for the MusicbrainzGenreEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MusicbrainzGenreEntityRepository extends JpaRepository<MusicbrainzGenreEntity, Long> {}

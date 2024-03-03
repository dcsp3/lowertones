package team.bham.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import team.bham.domain.MusicBrainzSongAttribution;

/**
 * Spring Data JPA repository for the MusicBrainzSongAttribution entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MusicBrainzSongAttributionRepository extends JpaRepository<MusicBrainzSongAttribution, Long> {}

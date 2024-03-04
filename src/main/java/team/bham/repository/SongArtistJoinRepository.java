package team.bham.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import team.bham.domain.SongArtistJoin;

/**
 * Spring Data JPA repository for the SongArtistJoin entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SongArtistJoinRepository extends JpaRepository<SongArtistJoin, Long> {}

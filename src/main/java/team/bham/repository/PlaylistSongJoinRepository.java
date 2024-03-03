package team.bham.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import team.bham.domain.PlaylistSongJoin;

/**
 * Spring Data JPA repository for the PlaylistSongJoin entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlaylistSongJoinRepository extends JpaRepository<PlaylistSongJoin, Long> {}

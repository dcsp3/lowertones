package team.bham.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import team.bham.domain.MainArtist;

/**
 * Spring Data JPA repository for the MainArtist entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MainArtistRepository extends JpaRepository<MainArtist, Long> {}

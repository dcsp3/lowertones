package team.bham.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import team.bham.domain.SpotifyExchangeCode;

/**
 * Spring Data JPA repository for the SpotifyExchangeCode entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpotifyExchangeCodeRepository extends JpaRepository<SpotifyExchangeCode, Long> {}

package team.bham.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import team.bham.domain.Vault;

/**
 * Spring Data JPA repository for the Vault entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VaultRepository extends JpaRepository<Vault, Long> {}

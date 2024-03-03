package team.bham.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import team.bham.domain.Contributor;

/**
 * Spring Data JPA repository for the Contributor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContributorRepository extends JpaRepository<Contributor, Long> {}

package team.bham.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team.bham.domain.Contributor;
import team.bham.service.dto.RecappedRequest;

/**
 * Spring Data JPA repository for the Contributor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContributorRepository extends JpaRepository<Contributor, Long> {
    @Query("SELECT c, COUNT(c) FROM Contributor c " + "JOIN c.songs s " + "WHERE s.id IN :songIds AND c.role = :role " + "GROUP BY c")
    List<Object[]> countContributorsByRoleAndSongIds(@Param("role") String role, @Param("songIds") List<Long> songIds);

    @Query("SELECT DISTINCT c FROM Contributor c JOIN c.songs s WHERE s.id IN :songIds")
    List<Contributor> findContributorsBySongIds(@Param("songIds") List<Long> songIds);
}

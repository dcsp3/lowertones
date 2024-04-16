package team.bham.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team.bham.domain.User;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    String USERS_BY_LOGIN_CACHE = "usersByLogin";

    String USERS_BY_EMAIL_CACHE = "usersByEmail";
    Optional<User> findOneByActivationKey(String activationKey);
    List<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);
    Optional<User> findOneByResetKey(String resetKey);
    Optional<User> findOneByEmailIgnoreCase(String email);
    Optional<User> findOneByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
    Optional<User> findOneWithAuthoritiesByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
    Optional<User> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    Page<User> findAllByIdNotNullAndActivatedIsTrue(Pageable pageable);

    default void deleteUserWithDependencies(Long userId) {
        deletePlaylistSongJoinsByUserId(userId);
        deletePlaylistsByUserId(userId);
        deleteUserById(userId);
    }

    @Transactional
    @Modifying
    @Query("DELETE FROM PlaylistSongJoin psj WHERE psj.playlist.id IN (SELECT p.id FROM Playlist p WHERE p.appUser.id = ?1)")
    void deletePlaylistSongJoinsByUserId(Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Playlist p WHERE p.appUser.id = ?1")
    void deletePlaylistsByUserId(Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM AppUser a WHERE a.id = ?1")
    void deleteUserById(Long userId);
}

package team.bham.service;

import java.util.*;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.bham.domain.AppUser;
import team.bham.domain.User;
import team.bham.repository.AppUserRepository;
import team.bham.repository.UserRepository;

@Service
public class PreferencesService {

    private final UserRepository userRepository;
    private final AppUserRepository appUserRepository;
    private final AppUserService appUserService;
    private final CacheManager cacheManager;

    public PreferencesService(
        AppUserRepository appUserRepository,
        UserRepository userRepository,
        AppUserService appUserService,
        CacheManager cacheManager
    ) {
        this.appUserRepository = appUserRepository;
        this.userRepository = userRepository;
        this.appUserService = appUserService;
        this.cacheManager = cacheManager;
    }

    @Transactional
    public AppUser getAppUser(Authentication authentication) {
        User user = userRepository.findOneByLogin(authentication.getName()).get();
        Optional<AppUser> appUser = appUserRepository.findByUserId(user.getId());
        if (appUser.isPresent()) return appUser.get(); else return null;
    }

    public void updateEmail(String login, String email) {
        userRepository
            .findOneByLogin(login)
            .ifPresent(user -> {
                user.setEmail(email);
                userRepository.save(user);
            });
    }

    public void deleteCurrentUser(String login) {
        appUserService.deleteUser(login);
        userRepository
            .findOneByLogin(login)
            .ifPresent(user -> {
                userRepository.delete(user);
                this.clearUserCaches(user);
            });
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        if (user.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
        }
    }
}

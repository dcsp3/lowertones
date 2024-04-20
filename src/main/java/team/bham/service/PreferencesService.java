package team.bham.service;

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

    public PreferencesService(AppUserRepository appUserRepository, UserRepository userRepository) {
        this.appUserRepository = appUserRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public AppUser getAppUser(Authentication authentication) {
        User user = userRepository.findOneByLogin(authentication.getName()).get();
        return appUserRepository.findByUserId(user.getId()).get();
    }
}

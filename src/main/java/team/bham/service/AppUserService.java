package team.bham.service;

import org.springframework.stereotype.Service;
import team.bham.domain.AppUser;
import team.bham.domain.User;
import team.bham.repository.AppUserRepository;
import team.bham.repository.PlaylistRepository;
import team.bham.repository.UserRepository;

@Service
public class AppUserService {

    private final UserRepository userRepository;
    private final AppUserRepository appUserRepository;

    public AppUserService(UserRepository userRepository, AppUserRepository appUserRepository, PlaylistRepository playlistRepository) {
        this.userRepository = userRepository;
        this.appUserRepository = appUserRepository;
    }

    public void deleteUser(String user) {
        AppUser appUser = appUserRepository
            .findByUserLogin(user)
            .orElseThrow(() -> new RuntimeException("User not found with login: " + user));
        userRepository.deleteUserWithDependencies(appUser.getId());
    }

    public AppUser resolveAppUser(String username) {
        User user = userRepository
            .findOneByLogin(username)
            .orElseThrow(() -> new RuntimeException("User not found with login: " + username));
        return appUserRepository
            .findByUserId(user.getId())
            .orElseThrow(() -> new RuntimeException("AppUser not found for user id: " + user.getId()));
    }
}

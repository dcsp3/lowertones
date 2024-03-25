package team.bham.service;

import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import team.bham.domain.AppUser;
import team.bham.domain.User;
import team.bham.repository.AppUserRepository;
import team.bham.repository.UserRepository;
import team.bham.service.SpotifyAPIWrapperService;

@Service
public class VisualisationsService {

    private SpotifyAPIWrapperService spotifyAPIWrapperService;
    private final UserRepository userRepository;
    private final AppUserRepository appUserRepository;

    public VisualisationsService(
        SpotifyAPIWrapperService spotifyAPIWrapperService,
        UserRepository userRepository,
        AppUserRepository appUserRepository
    ) {
        this.spotifyAPIWrapperService = spotifyAPIWrapperService;
        this.userRepository = userRepository;
        this.appUserRepository = appUserRepository;
    }

    public JSONObject getShortTermArtists(Authentication authentication) {
        User user = userRepository.findOneByLogin(authentication.getName()).get();
        AppUser appUser = appUserRepository.findByUserId(user.getId()).get();

        return spotifyAPIWrapperService.getCurrentUserShortTermTopArtists(appUser);
    }
}

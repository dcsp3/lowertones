package team.bham.service;

import java.util.List;
import java.util.Optional;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import team.bham.domain.AppUser;
import team.bham.repository.AppUserRepository;
import team.bham.service.SpotifyAPIWrapperService;

@Service
@Transactional
public class DiscoverWeeklyService {

    private final SpotifyAPIWrapperService spotifyAPIWrapperService;
    private final AppUserRepository appUserRepository;

    public DiscoverWeeklyService(SpotifyAPIWrapperService spotifyAPIWrapperService, AppUserRepository appUserRepository) {
        this.spotifyAPIWrapperService = spotifyAPIWrapperService;
        this.appUserRepository = appUserRepository;
    }
    /*
    public void updateDiscoverWeeklyPlaylist() {
        // Fetch the current authenticated user
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<AppUser> optionalAppUser = appUserRepository.findOneByUsername(currentUsername);
        AppUser currentUser = optionalAppUser.get();
        spotifyAPIWrapperService.refreshAccessToken(currentUser);
        String spotifyUserId = currentUser.getSpotifyUserId();

    }*/

}

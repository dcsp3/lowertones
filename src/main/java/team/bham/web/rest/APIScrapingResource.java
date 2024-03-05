package team.bham.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import team.bham.domain.AppUser;
import team.bham.domain.User;
import team.bham.repository.AppUserRepository;
import team.bham.repository.UserRepository;
import team.bham.service.SpotifyAPIWrapperService;
import team.bham.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api")
public class APIScrapingResource {

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserRepository userRepository;
    private final AppUserRepository appUserRepository;
    private final SpotifyAPIWrapperService apiWrapper;

    //todo
    public APIScrapingResource(
        UserRepository userRepository,
        AppUserRepository appUserRepository,
        SpotifyAPIWrapperService apiWrapperService
    ) {
        this.userRepository = userRepository;
        this.appUserRepository = appUserRepository;
        this.apiWrapper = apiWrapperService;
    }

    @PostMapping("/scrape")
    public ResponseEntity<Boolean> ScrapeUserPlaylists() {
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping("/test-get-user-details")
    public ResponseEntity<JSONObject> getUserID(Authentication authentication) {
        AppUser appUser = resolveAppUser(authentication.getName());
        return new ResponseEntity<>(apiWrapper.getUserDetails(appUser), HttpStatus.OK);
    }

    private AppUser resolveAppUser(String name) {
        User user = userRepository.findOneByLogin(name).get();
        return appUserRepository.findByUserId(user.getId()).get();
    }
}

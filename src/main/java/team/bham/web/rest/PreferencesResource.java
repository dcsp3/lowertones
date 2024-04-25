package team.bham.web.rest;

import javax.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.bham.config.Constants;
import team.bham.domain.AppUser;
import team.bham.security.jwt.TokenProvider;
import team.bham.service.PreferencesService;
import team.bham.service.UserService;
import tech.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class PreferencesResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);
    private final PreferencesService preferencesService;
    private AppUser appUser;
    private final UserService userService;
    private final TokenProvider tokenProvider;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public PreferencesResource(PreferencesService preferencesService, UserService userService, TokenProvider tokenProvider) {
        this.preferencesService = preferencesService;
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/account/preferences")
    public ResponseEntity<AppUser> getAppUser(Authentication authentication) {
        appUser = preferencesService.getAppUser(authentication);
        return ResponseEntity.ok(appUser);
    }

    @PostMapping("/account/preferences/highContrast")
    public ResponseEntity<Boolean> getHighContrast(Authentication authentication) {
        appUser = preferencesService.getAppUser(authentication);
        return ResponseEntity.ok(appUser.getHighContrastMode());
    }

    @PostMapping("/account/preferences/textSize")
    public ResponseEntity<Number> getTextSize(Authentication authentication) {
        appUser = preferencesService.getAppUser(authentication);
        return ResponseEntity.ok(appUser.getTextSize());
    }

    @PostMapping("/account/preferences/{login}")
    public ResponseEntity<Void> signOutAllDevices(
        @PathVariable @Pattern(regexp = Constants.LOGIN_REGEX) String login,
        Authentication authentication
    ) {
        log.debug("REST request to invalidate tokens of User: {}", login);
        tokenProvider.invalidateAllTokensForUser(login);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/account/preferences/{login}")
    public ResponseEntity<Void> deleteCurrentUser(
        @PathVariable @Pattern(regexp = Constants.LOGIN_REGEX) String login,
        Authentication authentication
    ) {
        log.debug("REST request to delete User: {}", login);
        userService.deleteUser(login);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createAlert(applicationName, "A user is deleted with identifier " + login, login))
            .build();
    }
}

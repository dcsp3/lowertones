package team.bham.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.bham.domain.AppUser;
import team.bham.service.PreferencesService;

@RestController
@RequestMapping("/api")
public class PreferencesResource {

    private final PreferencesService PreferencesService;
    private AppUser appUser;

    public PreferencesResource(PreferencesService PreferencesService) {
        this.PreferencesService = PreferencesService;
    }

    @PostMapping("/account/preferences")
    public ResponseEntity<AppUser> getAppUser(Authentication authentication) {
        appUser = PreferencesService.getAppUser(authentication);
        return ResponseEntity.ok(appUser);
    }
}

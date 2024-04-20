package team.bham.web.rest;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.bham.service.NetworkService;

@RestController
@RequestMapping("/api")
public class NetworkResource {

    private final NetworkService networkService;

    public NetworkResource(NetworkService networkService) {
        this.networkService = networkService;
    }

    @GetMapping("/top-artists-short-term")
    public ResponseEntity<String> getShortTermTopArtists(Authentication authentication) {
        return networkService.getShortTermTopArtists(authentication);
    }

    @GetMapping("/top-artists-medium-term")
    public ResponseEntity<String> getMediumTermTopArtists(Authentication authentication) {
        return networkService.getMediumTermTopArtists(authentication);
    }

    @GetMapping("/top-artists-long-term")
    public ResponseEntity<String> getLongTermTopArtists(Authentication authentication) {
        return networkService.getLongTermTopArtists(authentication);
    }

    @GetMapping("/user-playlists")
    public ResponseEntity<List<String>> getUserPlaylistNames(Authentication authentication) {
        return networkService.getUserPlaylistNames(authentication);
    }
}

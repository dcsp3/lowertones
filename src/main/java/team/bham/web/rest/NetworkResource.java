package team.bham.web.rest;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.bham.service.NetworkService;
import team.bham.service.dto.NetworkDTO;

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
    public ResponseEntity<List<Map<String, Object>>> getUserPlaylistNames(Authentication authentication) {
        return networkService.getUserPlaylistNames(authentication);
    }

    @GetMapping("/playlists/{playlistId}/items")
    public ResponseEntity<NetworkDTO> getPlaylistItems(@PathVariable Long playlistId, Authentication authentication) {
        return networkService.getPlaylistItems(playlistId, authentication);
    }

    @GetMapping("/playlist/{playlistId}/stats")
    public ResponseEntity<String> getPlaylistData(@PathVariable Long playlistId, Authentication authentication) {
        return networkService.getPlaylistStats(playlistId, authentication);
    }
}

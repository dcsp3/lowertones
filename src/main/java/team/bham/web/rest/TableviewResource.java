package team.bham.web.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestController;
import team.bham.domain.AppUser;
import team.bham.repository.AppUserRepository;
import team.bham.repository.UserRepository;
import team.bham.service.APIWrapper.*;
import team.bham.service.APIWrapper.Enums.*;
import team.bham.service.SpotifyAPIWrapperService;
import team.bham.service.TableviewService;
import team.bham.service.UserService;
import team.bham.service.dto.PlaylistExportDTO;

@RestController
@RequestMapping("/api")
public class TableviewResource {

    private final TableviewService tableviewService;
    private final UserService userService;
    private final SpotifyAPIWrapperService apiWrapper;

    public TableviewResource(TableviewService tableviewService, UserService userService, SpotifyAPIWrapperService apiWrapperService) {
        this.tableviewService = tableviewService;
        this.userService = userService;
        this.apiWrapper = apiWrapperService;
    }

    @GetMapping("/top-playlist")
    public ResponseEntity<SpotifyPlaylist> getPlaylistTracks(Authentication authentication) {
        AppUser appUser = userService.resolveAppUser(authentication.getName());
        ArrayList<SpotifySimplifiedPlaylist> playlistIds = apiWrapper.getCurrentUserPlaylists(appUser).getData();
        SpotifyPlaylist p = apiWrapper.getPlaylistDetails(appUser, playlistIds.get(0).getSpotifyId()).getData();
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    @GetMapping("/tableview-user-playlists")
    public ResponseEntity<List<Map<String, Object>>> getUserPlaylistNames(Authentication authentication) {
        return tableviewService.getUserPlaylistNames(authentication);
    }

    @GetMapping("/tableview-playlist-songs")
    public ResponseEntity<List<Map<String, Object>>> getUserPlaylistSongs(
        @RequestParam("playlistId") String playlistId,
        Authentication authentication
    ) {
        return tableviewService.getUserPlaylistSongs(playlistId, authentication);
    }

    @PostMapping("/tableview-export-playlist")
    public ResponseEntity<Boolean> exportToSpotify(Authentication authentication, @RequestBody PlaylistExportDTO playlistExportDTO) {
        System.out.println(
            "Playlist name: " + playlistExportDTO.getName() + ", Songs: " + playlistExportDTO.getSongSpotifyIds().toString()
        );

        AppUser appUser = userService.resolveAppUser(authentication.getName());
        JSONObject createResponse = this.apiWrapper.createPlaylist(appUser, playlistExportDTO.getName()).getData();
        String playlistId = createResponse.getString("id");
        this.apiWrapper.addItemsToPlaylist(appUser, playlistId, playlistExportDTO.getSongSpotifyIds());

        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}

package team.bham.web.rest;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.bham.domain.AppUser;
import team.bham.repository.AppUserRepository;
import team.bham.repository.UserRepository;
import team.bham.service.APIWrapper.*;
import team.bham.service.APIWrapper.Enums.*;
import team.bham.service.SpotifyAPIWrapperService;
import team.bham.service.TableviewService;
import team.bham.service.UserService;

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
        JSONObject playlistInfo = apiWrapper.getCurrentUserPlaylists(appUser).getData();
        JSONArray playlistItems = playlistInfo.getJSONArray("items");
        JSONObject firstPlaylist = playlistItems.getJSONObject(0);
        SpotifyPlaylist p = apiWrapper.getPlaylistDetails(appUser, firstPlaylist.getString("id")).getData();
        return new ResponseEntity<>(p, HttpStatus.OK);
        //JSONObject trackInfo = apiWrapper.getPlaylistTracks(appUser, firstPlaylist.getString("id")).getData();
        //return new ResponseEntity<>(trackInfo.toString(), HttpStatus.OK);
    }
}

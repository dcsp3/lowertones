package team.bham.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.text.StyledEditorKit.BoldAction;
import org.json.JSONArray;
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
import team.bham.service.APIWrapper.Enums.SpotifyTimeRange;
import team.bham.service.APIWrapper.SpotifyAPIResponse;
import team.bham.service.SpotifyAPIWrapperService;
import team.bham.service.UserService;
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
    private final UserService userService;
    private final SpotifyAPIWrapperService apiWrapper;

    //todo
    public APIScrapingResource(
        UserRepository userRepository,
        AppUserRepository appUserRepository,
        UserService userService,
        SpotifyAPIWrapperService apiWrapperService
    ) {
        this.userRepository = userRepository;
        this.appUserRepository = appUserRepository;
        this.userService = userService;
        this.apiWrapper = apiWrapperService;
    }

    @PostMapping("/scrape")
    public ResponseEntity<Boolean> ScrapeUserPlaylists() {
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping("/get-user-details")
    public ResponseEntity<String> getUserID(Authentication authentication) {
        AppUser appUser = userService.resolveAppUser(authentication.getName());
        SpotifyAPIResponse<JSONObject> response = apiWrapper.getUserDetails(appUser);
        if (response.getSuccess()) {
            return new ResponseEntity<>(response.getData().toString(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //huge bodge job - should be removed in the near future
    //No longer used by tableview, own service and resource has been made
    @GetMapping("/playlist-tracks")
    public ResponseEntity<String> getPlaylistTracks(Authentication authentication) {
        //handle errors here...
        AppUser appUser = userService.resolveAppUser(authentication.getName());
        JSONObject playlistInfo = apiWrapper.getCurrentUserPlaylists(appUser).getData();
        JSONArray playlistItems = playlistInfo.getJSONArray("items");

        JSONArray tracks = new JSONArray();
        for (int i = 0; i < playlistItems.length(); i++) {
            JSONObject playlist = playlistItems.getJSONObject(i);
            //tracks.add(apiWrapper.getPlaylistTracks(appUser, playlist.getString("id")).getData());
            tracks.put(apiWrapper.getPlaylistTracks(appUser, playlist.getString("id")).getData());
        }

        return new ResponseEntity<>(tracks.toString(), HttpStatus.OK);
        /*JSONObject firstPlaylist = playlistItems.getJSONObject(0);
        JSONObject trackInfo = apiWrapper.getPlaylistTracks(appUser, firstPlaylist.getString("id")).getData();
        return new ResponseEntity<>(trackInfo.toString(), HttpStatus.OK);*/
    }

    @GetMapping("/top-artists")
    public ResponseEntity<String> getCurrentUserTopArtists(Authentication authentication) {
        AppUser appUser = userService.resolveAppUser(authentication.getName());
        if (appUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        SpotifyTimeRange short_term = SpotifyTimeRange.SHORT_TERM;
        SpotifyTimeRange medium_term = SpotifyTimeRange.MEDIUM_TERM;
        SpotifyTimeRange long_term = SpotifyTimeRange.LONG_TERM;

        try {
            SpotifyAPIResponse<JSONObject> shortTermArtists = apiWrapper.getCurrentUserTopArtists(appUser, short_term);
            SpotifyAPIResponse<JSONObject> mediumTermArtists = apiWrapper.getCurrentUserTopArtists(appUser, medium_term);
            SpotifyAPIResponse<JSONObject> longTermArtists = apiWrapper.getCurrentUserTopArtists(appUser, long_term);

            JSONObject result = new JSONObject();
            result.put("shortTerm", shortTermArtists.getData());
            result.put("mediumTerm", mediumTermArtists.getData());
            result.put("longTerm", longTermArtists.getData());

            return new ResponseEntity<>(result.toString(), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve artist data");
        }
    }

    @GetMapping("/is-spotify-linked")
    public ResponseEntity<Boolean> isSpotifyLinked(Authentication authentication) {
        AppUser appUser = userService.resolveAppUser(authentication.getName());
        if (appUser == null) {
            // AppUser not found, handle accordingly
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //no refresh - not linked
        if (appUser.getSpotifyRefreshToken() == null || appUser.getSpotifyRefreshToken().isEmpty()) {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }

        //attempt dummy API call
        SpotifyAPIResponse<JSONObject> userDetails = apiWrapper.getUserDetails(appUser);

        //call failing (i.e. success=false) implies user revoked access
        //(or rate limit, need to handle that)
        return new ResponseEntity<>(userDetails.getSuccess(), HttpStatus.OK);
    }
}

package team.bham.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import team.bham.domain.*;
import team.bham.domain.enumeration.AlbumType;
import team.bham.domain.enumeration.ReleaseDatePrecision;
import team.bham.domain.enumeration.ScrapingProgress;
import team.bham.repository.*;
import team.bham.service.APIScrapingService;
import team.bham.service.APIWrapper.*;
import team.bham.service.APIWrapper.Enums.*;
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
    private final AlbumRepository albumRepository;
    private final MainArtistRepository mainArtistRepository;
    private final PlaylistRepository playlistRepository;
    private final PlaylistSongJoinRepository playlistSongJoinRepository;
    private final SongArtistJoinRepository songArtistJoinRepository;
    private final SongRepository songRepository;
    private final UserService userService;
    private final SpotifyAPIWrapperService apiWrapper;
    private final APIScrapingService apiScrapingService;

    //todo
    public APIScrapingResource(
        UserRepository userRepository,
        AppUserRepository appUserRepository,
        UserService userService,
        SpotifyAPIWrapperService apiWrapperService,
        AlbumRepository albumRepository,
        MainArtistRepository mainArtistRepository,
        PlaylistRepository playlistRepository,
        PlaylistSongJoinRepository playlistSongJoinRepository,
        SongArtistJoinRepository songArtistJoinRepository,
        SongRepository songRepository,
        APIScrapingService apiScrapingService
    ) {
        this.userRepository = userRepository;
        this.appUserRepository = appUserRepository;
        this.userService = userService;
        this.apiWrapper = apiWrapperService;
        this.albumRepository = albumRepository;
        this.mainArtistRepository = mainArtistRepository;
        this.playlistRepository = playlistRepository;
        this.playlistSongJoinRepository = playlistSongJoinRepository;
        this.songArtistJoinRepository = songArtistJoinRepository;
        this.songRepository = songRepository;
        this.apiScrapingService = apiScrapingService;
    }

    @Transactional
    @PostMapping("/scrape")
    public ResponseEntity<Boolean> ScrapeUserPlaylists(Authentication authentication) {
        //AppUser appUser = userService.resolveAppUser(authentication.getName());

        CompletableFuture<Void> future = apiScrapingService.beginScrapeTask(authentication.getName());

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping("/scrape-progress")
    public ResponseEntity<ScrapingProgress> getScrapeProgress(Authentication authentication) {
        return new ResponseEntity<>(apiScrapingService.getScrapingProgress(authentication.getName()), HttpStatus.OK);
    }

    @Transactional
    @GetMapping("/has-scraped")
    public ResponseEntity<Boolean> userHasScraped(Authentication authentication) {
        try {
            AppUser appUser = userService.resolveAppUser(authentication.getName());
            return new ResponseEntity<>(appUser.getPlaylists().size() > 0, HttpStatus.OK);
        } catch (Exception e) {
            //appuser doesn't exist
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }

    @Transactional
    @PostMapping("/test")
    public ResponseEntity<Boolean> test(Authentication authentication) {
        AppUser appUser = userService.resolveAppUser(authentication.getName());
        apiScrapingService.test(appUser);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping("/get-user-details")
    public ResponseEntity<SpotifyUser> getUserID(Authentication authentication) {
        AppUser appUser = userService.resolveAppUser(authentication.getName());
        SpotifyAPIResponse<SpotifyUser> response = apiWrapper.getUserDetails(appUser);
        if (response.getSuccess()) {
            return new ResponseEntity<>(response.getData(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-user-playlists")
    public ResponseEntity<ArrayList<SpotifySimplifiedPlaylist>> getUserPlaylists(Authentication authentication) {
        AppUser appUser = userService.resolveAppUser(authentication.getName());
        SpotifyAPIResponse<ArrayList<SpotifySimplifiedPlaylist>> response = apiWrapper.getCurrentUserPlaylists(appUser);
        if (response.getSuccess()) {
            return new ResponseEntity<>(response.getData(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
        AppUser appUser = null;
        try {
            appUser = userService.resolveAppUser(authentication.getName());
        } catch (Exception e) {
            //whoops (default admin/user), let's fix that

            appUser = new AppUser();
            User user = userRepository.findOneByLogin(authentication.getName()).get();
            appUser.setUser(user);
            appUser.setDiscoverWeeklyBufferPlaylistID(new String());
            appUser.setDiscoverWeeklyBufferSettings(0);
            appUser.setEmail(user.getEmail());
            appUser.setName("test");
            appUser.setSpotifyUserID(new String());
            appUser.setLastLoginDate(LocalDate.now());
            appUser.setHighContrastMode(false);
            appUser.setTextSize(0);
            appUserRepository.save(appUser);
        }

        //no refresh - not linked
        if (appUser.getSpotifyRefreshToken() == null || appUser.getSpotifyRefreshToken().isEmpty()) {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }

        //attempt dummy API call
        SpotifyAPIResponse<SpotifyUser> userDetails = apiWrapper.getUserDetails(appUser);

        //call failing (i.e. success=false) implies user revoked access
        //(or rate limit, need to handle that)
        return new ResponseEntity<>(userDetails.getSuccess(), HttpStatus.OK);
    }
}

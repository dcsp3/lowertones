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

    @GetMapping("/get-user-details")
    public ResponseEntity<String> getUserID(Authentication authentication) {
        AppUser appUser = resolveAppUser(authentication.getName());
        SpotifyAPIResponse response = apiWrapper.getUserDetails(appUser);
        if (response.getSuccess()) {
            return new ResponseEntity<>(response.getData().toString(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //huge bodge job - should be removed in the near future
    @GetMapping("/playlist-tracks")
    public ResponseEntity<String> getPlaylistTracks(Authentication authentication) {
        //handle errors here...
        AppUser appUser = resolveAppUser(authentication.getName());
        JSONObject playlistInfo = apiWrapper.getCurrentUserPlaylists(appUser).getData();
        JSONArray playlistItems = playlistInfo.getJSONArray("items");
        JSONObject firstPlaylist = playlistItems.getJSONObject(0);
        JSONObject trackInfo = apiWrapper.getPlaylistTracks(appUser, firstPlaylist.getString("id")).getData();
        return new ResponseEntity<>(trackInfo.toString(), HttpStatus.OK);
    }

    @GetMapping("/top-artists-short-term")
    public ResponseEntity<String> getShortTermTopArtists(Authentication authentication) {
        AppUser appUser = resolveAppUser(authentication.getName());
        if (appUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        SpotifyTimeRange short_term = SpotifyTimeRange.SHORT_TERM;

        SpotifyAPIResponse shortTermArtists = apiWrapper.getCurrentUserTopArtists(appUser, short_term);
        JSONArray artists = shortTermArtists.getData().getJSONArray("items");

        JSONObject result = new JSONObject();
        JSONArray graphDataArray = new JSONArray(); // Create a JSONArray to hold all artist info

        int min_distance = 100;
        int max_distance = 600;

        int totalPopularity = 0;

        for (int count = 0; count < artists.length(); count++) {
            JSONObject item = artists.getJSONObject(count);
            totalPopularity += item.getInt("popularity"); // Assuming "popularity" is the key for artist popularity
            // Calculate the distance based on rank
            double distance = min_distance + ((double) (max_distance - min_distance) / artists.length()) * (count + 1);

            JSONObject artistInfo = new JSONObject(); // Use JSONObject to hold each artist's info
            artistInfo.put("distance", distance);
            artistInfo.put("name", item.getString("name"));
            artistInfo.put("id", item.getString("id"));

            JSONArray genresArray = item.getJSONArray("genres");
            List<String> genresList = genresArray.toList().stream().map(Object::toString).collect(Collectors.toList());
            artistInfo.put("genres", genresList);

            if (item.getJSONArray("images").length() > 0) { // Ensure there's at least one image
                artistInfo.put("imageUrl", item.getJSONArray("images").getJSONObject(0).getString("url"));
            } else {
                artistInfo.put("imageUrl", JSONObject.NULL); // Handle case where there's no image
            }

            graphDataArray.put(artistInfo); // Add this artist's info to the array
        }

        double averagePopularity = artists.length() > 0 ? (double) totalPopularity / artists.length() : 0;

        String tasteCategory;

        if (averagePopularity < 20) {
            tasteCategory = "Underground";
        } else if (averagePopularity < 40) {
            tasteCategory = "Rising";
        } else if (averagePopularity < 60) {
            tasteCategory = "Cool";
        } else if (averagePopularity < 80) {
            tasteCategory = "Trending";
        } else if (averagePopularity < 90) {
            tasteCategory = "Mainstream";
        } else {
            tasteCategory = "Superstar";
        }

        // Prepare stats object
        JSONObject stats = new JSONObject();

        if (artists.length() > 0) {
            // Assuming the first artist is the top artist
            JSONObject topArtist = artists.getJSONObject(0);
            stats.put("topArtistName", topArtist.getString("name"));
            stats.put("topArtistImage", topArtist.getJSONArray("images").getJSONObject(0).getString("url"));

            // Calculate the top genre
            Map<String, Integer> genreCounts = new HashMap<>();
            for (int i = 0; i < artists.length(); i++) {
                JSONArray genres = artists.getJSONObject(i).getJSONArray("genres");
                for (int j = 0; j < genres.length(); j++) {
                    String genre = genres.getString(j);
                    genreCounts.put(genre, genreCounts.getOrDefault(genre, 0) + 1);
                }
            }
            String topGenre = Collections.max(genreCounts.entrySet(), Map.Entry.comparingByValue()).getKey();
            stats.put("topGenre", topGenre);

            // Update stats object
            stats.put("averagePopularity", String.format("%.2f%%", averagePopularity));
            stats.put("tasteCategory", tasteCategory);
        }

        result.put("graphData", graphDataArray);
        result.put("stats", stats);

        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }

    @GetMapping("/top-artists-medium-term")
    public ResponseEntity<String> getMediumtTermTopArtists(Authentication authentication) {
        AppUser appUser = resolveAppUser(authentication.getName());
        if (appUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        SpotifyTimeRange medium_term = SpotifyTimeRange.MEDIUM_TERM;

        SpotifyAPIResponse mediumTermArtists = apiWrapper.getCurrentUserTopArtists(appUser, medium_term);
        JSONArray artists = mediumTermArtists.getData().getJSONArray("items");

        JSONObject result = new JSONObject();
        JSONArray graphDataArray = new JSONArray(); // Create a JSONArray to hold all artist info

        int min_distance = 100;
        int max_distance = 600;

        int totalPopularity = 0;

        for (int count = 0; count < artists.length(); count++) {
            JSONObject item = artists.getJSONObject(count);
            totalPopularity += item.getInt("popularity"); // Assuming "popularity" is the key for artist popularity
            // Calculate the distance based on rank
            double distance = min_distance + ((double) (max_distance - min_distance) / artists.length()) * (count + 1);

            JSONObject artistInfo = new JSONObject(); // Use JSONObject to hold each artist's info
            artistInfo.put("distance", distance);
            artistInfo.put("name", item.getString("name"));
            artistInfo.put("id", item.getString("id"));

            JSONArray genresArray = item.getJSONArray("genres");
            List<String> genresList = genresArray.toList().stream().map(Object::toString).collect(Collectors.toList());
            artistInfo.put("genres", genresList);

            if (item.getJSONArray("images").length() > 0) { // Ensure there's at least one image
                artistInfo.put("imageUrl", item.getJSONArray("images").getJSONObject(0).getString("url"));
            } else {
                artistInfo.put("imageUrl", JSONObject.NULL); // Handle case where there's no image
            }

            graphDataArray.put(artistInfo); // Add this artist's info to the array
        }

        double averagePopularity = artists.length() > 0 ? (double) totalPopularity / artists.length() : 0;

        String tasteCategory;

        if (averagePopularity < 20) {
            tasteCategory = "Underground";
        } else if (averagePopularity < 40) {
            tasteCategory = "Rising";
        } else if (averagePopularity < 60) {
            tasteCategory = "Cool";
        } else if (averagePopularity < 80) {
            tasteCategory = "Trending";
        } else if (averagePopularity < 90) {
            tasteCategory = "Mainstream";
        } else {
            tasteCategory = "Superstar";
        }

        // Prepare stats object
        JSONObject stats = new JSONObject();

        if (artists.length() > 0) {
            // Assuming the first artist is the top artist
            JSONObject topArtist = artists.getJSONObject(0);
            stats.put("topArtistName", topArtist.getString("name"));
            stats.put("topArtistImage", topArtist.getJSONArray("images").getJSONObject(0).getString("url"));

            // Calculate the top genre
            Map<String, Integer> genreCounts = new HashMap<>();
            for (int i = 0; i < artists.length(); i++) {
                JSONArray genres = artists.getJSONObject(i).getJSONArray("genres");
                for (int j = 0; j < genres.length(); j++) {
                    String genre = genres.getString(j);
                    genreCounts.put(genre, genreCounts.getOrDefault(genre, 0) + 1);
                }
            }
            String topGenre = Collections.max(genreCounts.entrySet(), Map.Entry.comparingByValue()).getKey();
            stats.put("topGenre", topGenre);

            // Update stats object
            stats.put("averagePopularity", String.format("%.2f%%", averagePopularity));
            stats.put("tasteCategory", tasteCategory);
        }

        result.put("graphData", graphDataArray);
        result.put("stats", stats);

        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }

    @GetMapping("/top-artists-long-term")
    public ResponseEntity<String> getLongTermTopArtists(Authentication authentication) {
        AppUser appUser = resolveAppUser(authentication.getName());
        if (appUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        SpotifyTimeRange long_term = SpotifyTimeRange.LONG_TERM;

        SpotifyAPIResponse longTermArtists = apiWrapper.getCurrentUserTopArtists(appUser, long_term);
        JSONArray artists = longTermArtists.getData().getJSONArray("items");

        JSONObject result = new JSONObject();
        JSONArray graphDataArray = new JSONArray(); // Create a JSONArray to hold all artist info

        int min_distance = 100;
        int max_distance = 600;

        int totalPopularity = 0;

        for (int count = 0; count < artists.length(); count++) {
            JSONObject item = artists.getJSONObject(count);
            totalPopularity += item.getInt("popularity"); // Assuming "popularity" is the key for artist popularity
            // Calculate the distance based on rank
            double distance = min_distance + ((double) (max_distance - min_distance) / artists.length()) * (count + 1);

            JSONObject artistInfo = new JSONObject(); // Use JSONObject to hold each artist's info
            artistInfo.put("distance", distance);
            artistInfo.put("name", item.getString("name"));
            artistInfo.put("id", item.getString("id"));

            JSONArray genresArray = item.getJSONArray("genres");
            List<String> genresList = genresArray.toList().stream().map(Object::toString).collect(Collectors.toList());
            artistInfo.put("genres", genresList);

            if (item.getJSONArray("images").length() > 0) { // Ensure there's at least one image
                artistInfo.put("imageUrl", item.getJSONArray("images").getJSONObject(0).getString("url"));
            } else {
                artistInfo.put("imageUrl", JSONObject.NULL); // Handle case where there's no image
            }

            graphDataArray.put(artistInfo); // Add this artist's info to the array
        }

        double averagePopularity = artists.length() > 0 ? (double) totalPopularity / artists.length() : 0;

        String tasteCategory;

        if (averagePopularity < 20) {
            tasteCategory = "Underground🤘";
        } else if (averagePopularity < 40) {
            tasteCategory = "Rising📈";
        } else if (averagePopularity < 60) {
            tasteCategory = "Cool😎";
        } else if (averagePopularity < 80) {
            tasteCategory = "Trending🔥";
        } else if (averagePopularity < 90) {
            tasteCategory = "Mainstream🌍";
        } else {
            tasteCategory = "Superstar⭐";
        }

        // Prepare stats object
        JSONObject stats = new JSONObject();

        if (artists.length() > 0) {
            // Assuming the first artist is the top artist
            JSONObject topArtist = artists.getJSONObject(0);
            stats.put("topArtistName", topArtist.getString("name"));
            stats.put("topArtistImage", topArtist.getJSONArray("images").getJSONObject(0).getString("url"));

            // Calculate the top genre
            Map<String, Integer> genreCounts = new HashMap<>();
            for (int i = 0; i < artists.length(); i++) {
                JSONArray genres = artists.getJSONObject(i).getJSONArray("genres");
                for (int j = 0; j < genres.length(); j++) {
                    String genre = genres.getString(j);
                    genreCounts.put(genre, genreCounts.getOrDefault(genre, 0) + 1);
                }
            }
            String topGenre = Collections.max(genreCounts.entrySet(), Map.Entry.comparingByValue()).getKey();
            stats.put("topGenre", topGenre);

            // Update stats object
            stats.put("averagePopularity", String.format("%.2f%%", averagePopularity));
            stats.put("tasteCategory", tasteCategory);
        }

        result.put("graphData", graphDataArray);
        result.put("stats", stats);

        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }

    /*

    @GetMapping("/top-artists-medium-term")
    public ResponseEntity<String> getMediumTermTopArtists(Authentication authentication) {
        AppUser appUser = resolveAppUser(authentication.getName());
        if (appUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        
        SpotifyTimeRange medium_term = SpotifyTimeRange.MEDIUM_TERM;


        JSONObject topArtists = apiWrapper.getCurrentUserTopArtists(appUser, SpotifyTimeRange.MEDIUM_TERM).getData();
        JSONArray artists = topArtists.getJSONArray("items");

        List<Object> result = new ArrayList<>();
        int min_distance = 100;
        int max_distance = 600;

        for (int count = 0; count < artists.length(); count++) {
            JSONObject item = artists.getJSONObject(count);
            // Calculate the distance based on rank
            double distance = min_distance + ((double) (max_distance - min_distance) / artists.length()) * (count + 1);

            List<Object> artistInfo = new ArrayList<>();
            artistInfo.add(distance);
            artistInfo.add(item.getString("name"));
            artistInfo.add(item.getString("id"));

            JSONArray genresArray = item.getJSONArray("genres");
            List<String> genresList = genresArray.toList().stream().map(Object::toString).collect(Collectors.toList());
            artistInfo.add(genresList);

            JSONArray images = item.getJSONArray("images");
            artistInfo.add(images.getJSONObject(0).getString("url"));

            result.add(artistInfo);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/top-artists-long-term")
    public ResponseEntity<String> getLongTermTopArtists(Authentication authentication) {
        AppUser appUser = resolveAppUser(authentication.getName());
        if (appUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        
        SpotifyTimeRange long_term = SpotifyTimeRange.LONG_TERM;


        JSONObject topArtists = apiWrapper.getCurrentUserTopArtists(appUser, SpotifyTimeRange.LONG_TERM).getData();
        JSONArray artists = topArtists.getJSONArray("items");

        List<Object> result = new ArrayList<>();
        int min_distance = 100;
        int max_distance = 600;

        for (int count = 0; count < artists.length(); count++) {
            JSONObject item = artists.getJSONObject(count);
            // Calculate the distance based on rank
            double distance = min_distance + ((double) (max_distance - min_distance) / artists.length()) * (count + 1);

            List<Object> artistInfo = new ArrayList<>();
            artistInfo.add(distance);
            artistInfo.add(item.getString("name"));
            artistInfo.add(item.getString("id"));

            JSONArray genresArray = item.getJSONArray("genres");
            List<String> genresList = genresArray.toList().stream().map(Object::toString).collect(Collectors.toList());
            artistInfo.add(genresList);

            JSONArray images = item.getJSONArray("images");
            artistInfo.add(images.getJSONObject(0).getString("url"));

            result.add(artistInfo);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
  */

    @GetMapping("/top-artists")
    public ResponseEntity<String> getCurrentUserTopArtists(Authentication authentication) {
        AppUser appUser = resolveAppUser(authentication.getName());
        if (appUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        SpotifyTimeRange short_term = SpotifyTimeRange.SHORT_TERM;
        SpotifyTimeRange medium_term = SpotifyTimeRange.MEDIUM_TERM;
        SpotifyTimeRange long_term = SpotifyTimeRange.LONG_TERM;

        try {
            SpotifyAPIResponse shortTermArtists = apiWrapper.getCurrentUserTopArtists(appUser, short_term);
            SpotifyAPIResponse mediumTermArtists = apiWrapper.getCurrentUserTopArtists(appUser, medium_term);
            SpotifyAPIResponse longTermArtists = apiWrapper.getCurrentUserTopArtists(appUser, long_term);

            JSONObject result = new JSONObject();
            result.put("shortTerm", shortTermArtists.getData());
            result.put("mediumTerm", mediumTermArtists.getData());
            result.put("longTerm", longTermArtists.getData());

            return new ResponseEntity<>(result.toString(), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve artist data");
        }
    }

    private AppUser resolveAppUser(String name) {
        User user = userRepository.findOneByLogin(name).get();

        return appUserRepository.findByUserId(user.getId()).get();
    }

    @GetMapping("/is-spotify-linked")
    public ResponseEntity<Boolean> isSpotifyLinked(Authentication authentication) {
        AppUser appUser = resolveAppUser(authentication.getName());
        if (appUser == null) {
            // AppUser not found, handle accordingly
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //no refresh - not linked
        if (appUser.getSpotifyRefreshToken() == null || appUser.getSpotifyRefreshToken().isEmpty()) {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }

        //attempt dummy API call
        SpotifyAPIResponse userDetails = apiWrapper.getUserDetails(appUser);

        //call failing (i.e. success=false) implies user revoked access
        //(or rate limit, need to handle that)
        return new ResponseEntity<>(userDetails.getSuccess(), HttpStatus.OK);
    }
}

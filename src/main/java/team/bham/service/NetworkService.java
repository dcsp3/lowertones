package team.bham.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import team.bham.domain.AppUser;
import team.bham.service.APIWrapper.Enums.SpotifyTimeRange;
import team.bham.service.APIWrapper.SpotifyAPIResponse;

@Service
public class NetworkService {

    private final SpotifyAPIWrapperService apiWrapper;
    private final UserService userService;

    public NetworkService(SpotifyAPIWrapperService apiWrapper, UserService userService) {
        this.apiWrapper = apiWrapper;
        this.userService = userService;
    }

    //todo: add helper methods to avoid duplication (for stats)

    public ResponseEntity<String> getShortTermTopArtists(Authentication authentication) {
        AppUser appUser = userService.resolveAppUser(authentication.getName());
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

    public ResponseEntity<String> getMediumTermTopArtists(Authentication authentication) {
        AppUser appUser = userService.resolveAppUser(authentication.getName());
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

    public ResponseEntity<String> getLongTermTopArtists(Authentication authentication) {
        AppUser appUser = userService.resolveAppUser(authentication.getName());
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
}

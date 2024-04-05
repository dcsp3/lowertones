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

    private JSONArray extractArtistDetails(JSONArray artists) {
        JSONArray graphDataArray = new JSONArray();
        int min_distance = 100;
        int max_distance = 600;

        for (int count = 0; count < artists.length(); count++) {
            JSONObject item = artists.getJSONObject(count);
            double distance = min_distance + ((double) (max_distance - min_distance) / artists.length()) * (count + 1);

            JSONObject artistInfo = new JSONObject();
            artistInfo.put("distance", distance);
            artistInfo.put("name", item.getString("name"));
            artistInfo.put("id", item.getString("id"));

            JSONArray genresArray = item.getJSONArray("genres");
            List<String> genresList = genresArray.toList().stream().map(Object::toString).collect(Collectors.toList());
            artistInfo.put("genres", genresList);

            if (item.getJSONArray("images").length() > 0) {
                artistInfo.put("imageUrl", item.getJSONArray("images").getJSONObject(0).getString("url"));
            } else {
                artistInfo.put("imageUrl", JSONObject.NULL);
            }

            graphDataArray.put(artistInfo);
        }
        return graphDataArray;
    }

    private JSONObject calculateStats(JSONArray artists) {
        JSONObject stats = new JSONObject();
        int totalPopularity = 0;
        Map<String, Integer> genreCounts = new HashMap<>();

        for (int i = 0; i < artists.length(); i++) {
            JSONObject artist = artists.getJSONObject(i);
            totalPopularity += artist.getInt("popularity");

            JSONArray genres = artist.getJSONArray("genres");
            for (int j = 0; j < genres.length(); j++) {
                String genre = genres.getString(j);
                genreCounts.put(genre, genreCounts.getOrDefault(genre, 0) + 1);
            }
        }

        double averagePopularity = artists.length() > 0 ? (double) totalPopularity / artists.length() : 0;
        String topGenre = Collections.max(genreCounts.entrySet(), Map.Entry.comparingByValue()).getKey();
        String tasteCategory = calculateTasteCategory(averagePopularity);

        if (artists.length() > 0) {
            JSONObject topArtist = artists.getJSONObject(0);
            stats.put("topArtistName", topArtist.getString("name"));
            stats.put("topArtistImage", topArtist.getJSONArray("images").getJSONObject(0).getString("url"));
        }

        stats.put("topGenre", topGenre);
        stats.put("averagePopularity", String.format("%.2f%%", averagePopularity));
        stats.put("tasteCategory", tasteCategory);

        return stats;
    }

    private String calculateTasteCategory(double averagePopularity) {
        if (averagePopularity < 20) return "Underground🤘";
        if (averagePopularity < 40) return "Rising📈";
        if (averagePopularity < 60) return "Cool😎";
        if (averagePopularity < 80) return "Trending🔥";
        if (averagePopularity < 90) return "Mainstream🌍";
        return "Superstar⭐";
    }

    public ResponseEntity<String> getShortTermTopArtists(Authentication authentication) {
        AppUser appUser = userService.resolveAppUser(authentication.getName());
        if (appUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        SpotifyTimeRange short_term = SpotifyTimeRange.SHORT_TERM;
        JSONObject shortTermArtists = apiWrapper.getCurrentUserTopArtists(appUser, short_term).getData();
        JSONArray artists = shortTermArtists.getJSONArray("items");

        JSONObject result = new JSONObject();
        result.put("graphData", extractArtistDetails(artists));
        result.put("stats", calculateStats(artists));

        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }

    public ResponseEntity<String> getMediumTermTopArtists(Authentication authentication) {
        AppUser appUser = userService.resolveAppUser(authentication.getName());
        if (appUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        SpotifyTimeRange medium_term = SpotifyTimeRange.MEDIUM_TERM;
        JSONObject mediumTermArtists = apiWrapper.getCurrentUserTopArtists(appUser, medium_term).getData();
        JSONArray artists = mediumTermArtists.getJSONArray("items");

        JSONObject result = new JSONObject();
        result.put("graphData", extractArtistDetails(artists));
        result.put("stats", calculateStats(artists));

        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }

    public ResponseEntity<String> getLongTermTopArtists(Authentication authentication) {
        AppUser appUser = userService.resolveAppUser(authentication.getName());
        if (appUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        SpotifyTimeRange long_term = SpotifyTimeRange.LONG_TERM;
        JSONObject longTermArtists = apiWrapper.getCurrentUserTopArtists(appUser, long_term).getData();
        JSONArray artists = longTermArtists.getJSONArray("items");

        JSONObject result = new JSONObject();
        result.put("graphData", extractArtistDetails(artists));
        result.put("stats", calculateStats(artists));

        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }
}

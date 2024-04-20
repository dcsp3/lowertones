package team.bham.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.bham.domain.AppUser;
import team.bham.domain.MainArtist;
import team.bham.domain.Playlist;
import team.bham.domain.PlaylistSongJoin;
import team.bham.domain.Song;
import team.bham.domain.SongArtistJoin;
import team.bham.repository.PlaylistRepository;
import team.bham.service.APIWrapper.Enums.SpotifyTimeRange;
import team.bham.service.APIWrapper.SpotifyAPIResponse;
import team.bham.service.dto.NetworkDTO;

@Service
public class NetworkService {

    private final SpotifyAPIWrapperService apiWrapper;
    private final UserService userService;
    private final UtilService utilService;

    public NetworkService(SpotifyAPIWrapperService apiWrapper, UserService userService, UtilService utilService) {
        this.apiWrapper = apiWrapper;
        this.userService = userService;
        this.utilService = utilService;
    }

    private JSONArray extractArtistDetails(JSONArray artists, AppUser user) {
        JSONArray graphDataArray = new JSONArray();
        int min_distance = 100;
        int max_distance = 600;

        for (int count = 0; count < artists.length(); count++) {
            JSONObject item = artists.getJSONObject(count);
            double distance = min_distance + ((double) (max_distance - min_distance) / artists.length()) * (count + 1);

            String artistSpotifyId = item.getString("id");
            int songCount = countSongsByArtistInLibrary(user, artistSpotifyId);

            JSONObject artistInfo = new JSONObject();
            artistInfo.put("distance", distance);
            artistInfo.put("name", item.getString("name"));
            artistInfo.put("id", artistSpotifyId);
            artistInfo.put("songsInLibrary", songCount);

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

    private JSONObject getTopTrackByTopArtist(JSONObject topArtist, AppUser user, SpotifyTimeRange timeRange) {
        JSONObject topTrack = new JSONObject();
        SpotifyAPIResponse<JSONObject> response = apiWrapper.getCurrentUserTopTracks(user, timeRange);

        JSONArray tracks = response.getData().getJSONArray("items");
        for (int i = 0; i < tracks.length(); i++) {
            JSONObject track = tracks.getJSONObject(i);
            JSONArray artists = track.getJSONArray("artists");
            for (int j = 0; j < artists.length(); j++) {
                JSONObject trackArtist = artists.getJSONObject(j);
                if (trackArtist.getString("id").equals(topArtist.getString("id"))) {
                    topTrack.put("trackName", track.getString("name"));
                    topTrack.put("previewUrl", track.getString("preview_url"));
                    return topTrack;
                }
            }
        }

        topTrack.put("trackName", "");
        topTrack.put("previewUrl", "");
        return topTrack;
    }

    private JSONObject calculateStats(JSONArray artists, AppUser user, SpotifyTimeRange timeRange) {
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

        Map<String, Object> tasteCategoryDetails = calculateTasteCategory(averagePopularity);

        if (artists.length() > 0) {
            JSONObject topArtist = artists.getJSONObject(0);
            stats.put("topArtistName", topArtist.getString("name"));
            stats.put("topArtistImage", topArtist.getJSONArray("images").getJSONObject(0).getString("url"));

            JSONObject topTrackByTopArtist = getTopTrackByTopArtist(topArtist, user, timeRange);
            if (topTrackByTopArtist != null) {
                stats.put("topTrackByTopArtist", topTrackByTopArtist);
            }
        }

        stats.put("topGenre", topGenre);
        stats.put("averagePopularity", String.format("%.2f%%", averagePopularity));
        stats.put("tasteCategory", tasteCategoryDetails);

        return stats;
    }

    private Map<String, Object> calculateTasteCategory(double averagePopularity) {
        Map<String, Object> categoryDetails = new HashMap<>();

        if (averagePopularity < 20) {
            categoryDetails.put("name", "Underground🤘");
            categoryDetails.put("colorDark", "#2c3e50");
            categoryDetails.put("colorLight", "#34495e");
        } else if (averagePopularity < 40) {
            categoryDetails.put("name", "Rising📈");
            categoryDetails.put("colorDark", "#27ae60");
            categoryDetails.put("colorLight", "#2ecc71");
        } else if (averagePopularity < 60) {
            categoryDetails.put("name", "Cool😎");
            categoryDetails.put("colorDark", "#2980b9");
            categoryDetails.put("colorLight", "#3498db");
        } else if (averagePopularity < 80) {
            categoryDetails.put("name", "Trending🔥");
            categoryDetails.put("colorDark", "#d35400");
            categoryDetails.put("colorLight", "#e67e22");
        } else if (averagePopularity < 90) {
            categoryDetails.put("name", "Mainstream🌍");
            categoryDetails.put("colorDark", "#c0392b");
            categoryDetails.put("colorLight", "#e74c3c");
        } else {
            categoryDetails.put("name", "Superstar⭐");
            categoryDetails.put("colorDark", "#f1c40f");
            categoryDetails.put("colorLight", "#f39c12");
        }

        return categoryDetails;
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
        result.put("graphData", extractArtistDetails(artists, appUser)); // Pass user to method
        result.put("stats", calculateStats(artists, appUser, short_term));

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
        result.put("graphData", extractArtistDetails(artists, appUser));
        result.put("stats", calculateStats(artists, appUser, medium_term));

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
        result.put("graphData", extractArtistDetails(artists, appUser));
        result.put("stats", calculateStats(artists, appUser, long_term));

        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }

    public int countSongsByArtistInLibrary(AppUser appUser, String artistSpotifyId) {
        if (appUser == null) {
            return 0;
        }

        return utilService.countSongsByArtistInLibrary(appUser, artistSpotifyId);
    }

    public ResponseEntity<List<Map<String, Object>>> getUserPlaylistNames(Authentication authentication) {
        AppUser appUser = userService.resolveAppUser(authentication.getName());
        if (appUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<Playlist> playlists = utilService.getUserPlaylists(appUser);

        List<Map<String, Object>> playlistInfo = playlists
            .stream()
            .map(playlist -> {
                Map<String, Object> info = new HashMap<>();
                info.put("id", playlist.getId());
                info.put("name", playlist.getPlaylistName());
                return info;
            })
            .collect(Collectors.toList());

        return ResponseEntity.ok().body(playlistInfo);
    }

    @Transactional
    public ResponseEntity<NetworkDTO> getPlaylistItems(Long playlistId, Authentication authentication) {
        AppUser appUser = userService.resolveAppUser(authentication.getName());
        if (appUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Playlist playlist = utilService.getPlaylistById(playlistId);
        if (playlist == null || !playlist.getAppUser().equals(appUser)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        NetworkDTO networkDTO = new NetworkDTO(playlist.getId(), playlist.getPlaylistName());
        networkDTO.setSongDetails(new ArrayList<>());

        for (PlaylistSongJoin join : playlist.getPlaylistSongJoins()) {
            Song song = join.getSong();
            List<NetworkDTO.ArtistDetails> artistDetailsList = new ArrayList<>();

            for (SongArtistJoin songArtistJoin : song.getSongArtistJoins()) {
                MainArtist artist = songArtistJoin.getMainArtist();
                List<String> genres = Collections.emptyList();

                NetworkDTO.ArtistDetails artistDetails = new NetworkDTO.ArtistDetails(
                    artist.getId(),
                    artist.getArtistName(),
                    genres,
                    artist.getArtistImageLarge(),
                    0
                );
                artistDetailsList.add(artistDetails);
            }

            NetworkDTO.SongDetails songDetails = new NetworkDTO.SongDetails(song.getId(), song.getSongTitle());
            songDetails.setArtists(artistDetailsList);
            networkDTO.getSongDetails().add(songDetails);
        }

        return ResponseEntity.ok(networkDTO);
    }
}

package team.bham.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.bham.domain.AppUser;
import team.bham.domain.MainArtist;
import team.bham.domain.Playlist;
import team.bham.domain.PlaylistSongJoin;
import team.bham.domain.RelatedArtists;
import team.bham.domain.Song;
import team.bham.domain.SongArtistJoin;
import team.bham.domain.SpotifyGenreEntity;
import team.bham.repository.MainArtistRepository;
import team.bham.repository.PlaylistRepository;
import team.bham.repository.PlaylistSongJoinRepository;
import team.bham.repository.RelatedArtistsRepository;
import team.bham.repository.SongArtistJoinRepository;
import team.bham.service.APIWrapper.Enums.SpotifyTimeRange;
import team.bham.service.APIWrapper.SpotifyAPIResponse;
import team.bham.service.dto.NetworkDTO;

@Service
public class NetworkService {

    private final SpotifyAPIWrapperService apiWrapper;
    private final UserService userService;
    private final UtilService utilService;

    @Autowired
    private SongArtistJoinRepository songArtistJoinRepository;

    @Autowired
    private PlaylistSongJoinRepository playlistSongJoinRepository;

    @Autowired
    private MainArtistRepository mainArtistRepository;

    @Autowired
    private RelatedArtistsRepository relatedArtistsRepository;

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
            categoryDetails.put("name", "Underground👤");
            categoryDetails.put("colorDark", "#2c3e50");
            categoryDetails.put("colorLight", "#34495e");
        } else if (averagePopularity < 40) {
            categoryDetails.put("name", "Niche🤘");
            categoryDetails.put("colorDark", "#27ae60");
            categoryDetails.put("colorLight", "#2ecc71");
        } else if (averagePopularity < 60) {
            categoryDetails.put("name", "Emerging🚀");
            categoryDetails.put("colorDark", "#2980b9");
            categoryDetails.put("colorLight", "#3498db");
        } else if (averagePopularity < 80) {
            categoryDetails.put("name", "Popular✨");
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

    private List<String> extractSpotifyIDs(JSONArray artists) {
        List<String> spotifyIDs = new ArrayList<>();
        for (int i = 0; i < artists.length(); i++) {
            spotifyIDs.add(artists.getJSONObject(i).getString("id"));
        }
        return spotifyIDs;
    }

    // awful way of doing it but cant use DB query because of the RelatedArtist type def :(
    private Map<String, Set<String>> constructRelatedArtistsMap(List<RelatedArtists> relatedArtistsList, Set<String> artistSpotifyIdsSet) {
        Map<String, Set<String>> relatedArtistsMap = new HashMap<>();
        for (RelatedArtists ra : relatedArtistsList) {
            String mainArtistId = ra.getMainArtistSptfyID();
            Set<String> relatedIds = new HashSet<>();

            relatedIds.add(ra.getRelatedArtistSpotifyID1());
            relatedIds.add(ra.getRelatedArtistSpotifyID2());
            relatedIds.add(ra.getRelatedArtistSpotifyID3());
            relatedIds.add(ra.getRelatedArtistSpotifyID4());
            relatedIds.add(ra.getRelatedArtistSpotifyID5());
            relatedIds.add(ra.getRelatedArtistSpotifyID6());
            relatedIds.add(ra.getRelatedArtistSpotifyID7());
            relatedIds.add(ra.getRelatedArtistSpotifyID8());
            relatedIds.add(ra.getRelatedArtistSpotifyID9());
            relatedIds.add(ra.getRelatedArtistSpotifyID10());
            relatedIds.add(ra.getRelatedArtistSpotifyID11());
            relatedIds.add(ra.getRelatedArtistSpotifyID12());
            relatedIds.add(ra.getRelatedArtistSpotifyID13());
            relatedIds.add(ra.getRelatedArtistSpotifyID14());
            relatedIds.add(ra.getRelatedArtistSpotifyID15());
            relatedIds.add(ra.getRelatedArtistSpotifyID16());
            relatedIds.add(ra.getRelatedArtistSpotifyID17());
            relatedIds.add(ra.getRelatedArtistSpotifyID18());
            relatedIds.add(ra.getRelatedArtistSpotifyID19());
            relatedIds.add(ra.getRelatedArtistSpotifyID20());

            relatedIds.removeIf(relatedId -> relatedId == null || !artistSpotifyIdsSet.contains(relatedId));
            relatedArtistsMap.put(mainArtistId, relatedIds);
        }
        return relatedArtistsMap;
    }

    private JSONObject convertMapToJson(Map<String, Set<String>> relatedArtistsMap) {
        JSONObject jsonMap = new JSONObject();
        relatedArtistsMap.forEach((key, value) -> jsonMap.put(key, new JSONArray(value)));
        return jsonMap;
    }

    public ResponseEntity<String> getShortTermTopArtists(Authentication authentication) {
        AppUser appUser = userService.resolveAppUser(authentication.getName());
        if (appUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        SpotifyTimeRange short_term = SpotifyTimeRange.SHORT_TERM;
        JSONObject shortTermArtists = apiWrapper.getCurrentUserTopArtists(appUser, short_term).getData();
        JSONArray artists = shortTermArtists.getJSONArray("items");

        List<String> artistSpotifyIds = extractSpotifyIDs(artists);
        Set<String> artistSpotifyIdsSet = new HashSet<>(artistSpotifyIds);

        List<RelatedArtists> relatedArtistsList = relatedArtistsRepository.findRelatedArtistsByMainArtistSpotifyIDs(artistSpotifyIdsSet);
        Map<String, Set<String>> relatedArtistsMap = constructRelatedArtistsMap(relatedArtistsList, artistSpotifyIdsSet);

        JSONObject result = new JSONObject();
        result.put("graphData", extractArtistDetails(artists, appUser));
        result.put("stats", calculateStats(artists, appUser, short_term));
        result.put("relatedArtists", convertMapToJson(relatedArtistsMap));

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

        List<String> artistSpotifyIds = extractSpotifyIDs(artists);
        Set<String> artistSpotifyIdsSet = new HashSet<>(artistSpotifyIds);

        List<RelatedArtists> relatedArtistsList = relatedArtistsRepository.findRelatedArtistsByMainArtistSpotifyIDs(artistSpotifyIdsSet);
        Map<String, Set<String>> relatedArtistsMap = constructRelatedArtistsMap(relatedArtistsList, artistSpotifyIdsSet);

        JSONObject result = new JSONObject();
        result.put("graphData", extractArtistDetails(artists, appUser));
        result.put("stats", calculateStats(artists, appUser, medium_term));
        result.put("relatedArtists", convertMapToJson(relatedArtistsMap));
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

        List<String> artistSpotifyIds = extractSpotifyIDs(artists);
        Set<String> artistSpotifyIdsSet = new HashSet<>(artistSpotifyIds);

        List<RelatedArtists> relatedArtistsList = relatedArtistsRepository.findRelatedArtistsByMainArtistSpotifyIDs(artistSpotifyIdsSet);
        Map<String, Set<String>> relatedArtistsMap = constructRelatedArtistsMap(relatedArtistsList, artistSpotifyIdsSet);

        JSONObject result = new JSONObject();
        result.put("graphData", extractArtistDetails(artists, appUser));
        result.put("stats", calculateStats(artists, appUser, long_term));
        result.put("relatedArtists", convertMapToJson(relatedArtistsMap));

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

    public JSONObject getArtistsDetailsFromPlaylist(AppUser appUser, Long playlistId) {
        List<MainArtist> artists = mainArtistRepository.findArtistDetailsByPlaylistId(playlistId);

        double minDistance = 100.0;
        double maxDistance = 600.0;

        int totalSongs = playlistSongJoinRepository.findSongsByPlaylistId(playlistId).size();

        JSONArray jsonArtistsArray = new JSONArray();

        for (MainArtist artist : artists) {
            int songsByArtist = playlistSongJoinRepository.countSongsByArtistInPlaylist(artist.getArtistSpotifyID(), playlistId);

            double distance = calculateDistance(songsByArtist, totalSongs, minDistance, maxDistance);

            JSONObject jsonArtist = new JSONObject();
            jsonArtist.put("id", artist.getArtistSpotifyID());
            jsonArtist.put("name", artist.getArtistName());
            jsonArtist.put("genres", mapGenres(artist.getSpotifyGenreEntities()));
            jsonArtist.put("imageUrl", artist.getArtistImageLarge() == null ? JSONObject.NULL : artist.getArtistImageLarge());
            jsonArtist.put("distance", distance);
            jsonArtist.put("songsInLibrary", countSongsByArtistInLibrary(appUser, artist.getArtistSpotifyID()));

            jsonArtistsArray.put(jsonArtist);
        }

        JSONObject graphData = new JSONObject();
        graphData.put("artists", jsonArtistsArray);
        return graphData;
    }

    public JSONObject calculatePlaylistStats(Long playlistId) {
        JSONObject stats = new JSONObject();

        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "artistPopularity"));
        Page<MainArtist> mostPopularArtistsPage = songArtistJoinRepository.findMostPopularArtistByPlaylistId(playlistId, pageable);

        if (!mostPopularArtistsPage.isEmpty()) {
            MainArtist mostPopularArtist = mostPopularArtistsPage.getContent().get(0);
            Long artistId = mostPopularArtist.getId();

            stats.put("topArtistName", mostPopularArtist.getArtistName());
            stats.put("topArtistImage", mostPopularArtist.getArtistImageLarge());

            Song topTrack = songArtistJoinRepository.findTopTrackByPopularArtistInPlaylist(artistId, playlistId);
            if (topTrack != null) {
                JSONObject topTrackByTopArtist = new JSONObject();
                topTrackByTopArtist.put("previewUrl", topTrack.getSongPreviewURL());
                topTrackByTopArtist.put("trackName", topTrack.getSongTitle());
                stats.put("topTrackByTopArtist", topTrackByTopArtist);
            }
        }

        List<Object[]> genreData = playlistSongJoinRepository.findMainGenreByPlaylistIdUsingArtists(playlistId);
        String mainGenre = genreData
            .stream()
            .max(Comparator.comparingLong(o -> (Long) o[1]))
            .map(result -> (String) result[0])
            .orElse("Unknown");
        stats.put("topGenre", mainGenre);

        List<Song> songs = playlistSongJoinRepository.findSongsByPlaylistId(playlistId);
        double averagePopularity = songs.stream().mapToInt(Song::getSongPopularity).average().orElse(0);
        stats.put("averagePopularity", String.format("%.2f%%", averagePopularity));

        Map<String, Object> tasteCategoryDetails = calculateTasteCategory(averagePopularity);
        stats.put("tasteCategory", tasteCategoryDetails);

        return stats;
    }

    private JSONArray mapGenres(Set<SpotifyGenreEntity> genreEntities) {
        JSONArray jsonGenresArray = new JSONArray();
        for (SpotifyGenreEntity genreEntity : genreEntities) {
            jsonGenresArray.put(genreEntity.getSpotifyGenre());
        }
        return jsonGenresArray;
    }

    private double calculateDistance(int songsByArtist, int totalSongs, double minDistance, double maxDistance) {
        double distanceRatio = (double) songsByArtist / totalSongs;
        return minDistance + (maxDistance - minDistance) * distanceRatio;
    }

    @Transactional
    public ResponseEntity<String> getPlaylistData(Long playlistId, Authentication authentication) {
        AppUser appUser = userService.resolveAppUser(authentication.getName());
        if (appUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        try {
            JSONObject stats = calculatePlaylistStats(playlistId);
            JSONObject graphData = getArtistsDetailsFromPlaylist(appUser, playlistId);

            JSONArray artistsArray = graphData.getJSONArray("artists");
            Set<String> artistSpotifyIds = new HashSet<>();
            for (int i = 0; i < artistsArray.length(); i++) {
                JSONObject artist = artistsArray.getJSONObject(i);
                artistSpotifyIds.add(artist.getString("id"));
            }

            List<RelatedArtists> relatedArtistsList = relatedArtistsRepository.findRelatedArtistsByMainArtistSpotifyIDs(artistSpotifyIds);
            Map<String, Set<String>> relatedArtistsMap = constructRelatedArtistsMap(relatedArtistsList, artistSpotifyIds);
            JSONObject relatedArtistsJson = convertMapToJson(relatedArtistsMap);

            JSONObject result = new JSONObject();
            result.put("stats", stats);
            result.put("graphData", graphData);
            result.put("relatedArtists", relatedArtistsJson);

            return new ResponseEntity<>(result.toString(), HttpStatus.OK);
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the playlist stats");
        }
    }
}

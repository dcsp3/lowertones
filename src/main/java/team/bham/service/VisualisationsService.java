package team.bham.service;

import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import team.bham.domain.*;
import team.bham.repository.AppUserRepository;
import team.bham.repository.UserRepository;
import team.bham.service.APIWrapper.Enums.SpotifyTimeRange;
import team.bham.service.APIWrapper.SpotifyAPIResponse;
import team.bham.service.SpotifyAPIWrapperService;
import team.bham.service.UtilService;
import team.bham.service.dto.VisualisationsDTO;

@Service
public class VisualisationsService {

    private SpotifyAPIWrapperService spotifyAPIWrapperService;
    private final UserRepository userRepository;
    private final AppUserRepository appUserRepository;

    private final UtilService utilService;

    public VisualisationsService(
        SpotifyAPIWrapperService spotifyAPIWrapperService,
        UserRepository userRepository,
        AppUserRepository appUserRepository,
        UtilService utilService
    ) {
        this.spotifyAPIWrapperService = spotifyAPIWrapperService;
        this.userRepository = userRepository;
        this.appUserRepository = appUserRepository;
        this.utilService = utilService;
    }

    public VisualisationsDTO top5GenresInPlaylist(Authentication authentication, String playlistId) {
        VisualisationsDTO dto = new VisualisationsDTO();
        List<Song> playlistSongs = utilService.getPlaylistSongs(playlistId);
        List<Album> playlistAlbums = null;
        for (int i = 0; i < playlistSongs.size(); i++) {
            playlistAlbums.add(playlistSongs.get(i).getAlbum());
        }
        List<MainArtist> playlistArtists = null;
        for (int i = 0; i < playlistAlbums.size(); i++) {
            playlistAlbums.add(playlistSongs.get(i).getAlbum());
        }
        List<SpotifyGenreEntity> playlistGenreEntities = null;
        for (int i = 0; i < playlistArtists.size(); i++) {
            playlistGenreEntities.addAll(playlistArtists.get(i).getSpotifyGenreEntities());
        }
        List<String> playlistGenres = null;
        for (int i = 0; i < playlistGenreEntities.size(); i++) {
            playlistGenres.add(playlistGenreEntities.get(i).getSpotifyGenre());
        }
        System.out.println("playlistGenres" + playlistGenres);
        List<String> topFive = findTopFiveFrequentStrings(playlistGenres);

        int numOfSongs = playlistSongs.size();

        dto.setTopGenre1Name(topFive.get(0));
        int topGenre1Count = Collections.frequency(playlistGenres, topFive.get(0));
        dto.setTopGenre1Percent(topGenre1Count / numOfSongs);

        dto.setTopGenre2Name(topFive.get(1));
        int topGenre2Count = Collections.frequency(playlistGenres, topFive.get(1));
        dto.setTopGenre2Percent(topGenre2Count / numOfSongs);

        dto.setTopGenre3Name(topFive.get(2));
        int topGenre3Count = Collections.frequency(playlistGenres, topFive.get(2));
        dto.setTopGenre3Percent(topGenre3Count / numOfSongs);

        dto.setTopGenre4Name(topFive.get(3));
        int topGenre4Count = Collections.frequency(playlistGenres, topFive.get(0));
        dto.setTopGenre4Percent(topGenre4Count / numOfSongs);

        dto.setTopGenre5Name(topFive.get(4));
        int topGenre5Count = Collections.frequency(playlistGenres, topFive.get(0));
        dto.setTopGenre5Percent(topGenre5Count / numOfSongs);

        // still need to set the DTO with top 5 artists and their counts in the playlist
        // would I return that data in a different DTO or different function?

        return dto;
    }

    // find top 5 genres method
    public static List<String> findTopFiveFrequentStrings(List<String> inputList) {
        Map<String, Integer> occurrences = new HashMap<>();

        // Count occurrences of each string
        for (String str : inputList) {
            occurrences.put(str, occurrences.getOrDefault(str, 0) + 1);
        }

        // Sort the map entries by value (occurrences)
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(occurrences.entrySet());
        sortedEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        // Extract top 5 most frequent strings
        List<String> topFive = new ArrayList<>();
        int count = 0;
        for (Map.Entry<String, Integer> entry : sortedEntries) {
            topFive.add(entry.getKey());
            count++;
            if (count == 5) break;
        }

        return topFive;
    }

    public VisualisationsDTO getShortTermArtists(Authentication authentication) {
        VisualisationsDTO dto = new VisualisationsDTO();

        User user = userRepository.findOneByLogin(authentication.getName()).get();
        AppUser appUser = appUserRepository.findByUserId(user.getId()).get();

        JSONArray topArtists = spotifyAPIWrapperService
            .getCurrentUserTopArtists(appUser, SpotifyTimeRange.SHORT_TERM)
            .getData()
            .getJSONArray("items");

        int count = Math.min(5, topArtists.length());

        for (int i = 0; i < count; i++) {
            JSONObject artist = topArtists.getJSONObject(i);
            String name = artist.getString("name");

            switch (i) {
                case 0:
                    dto.setTopArtist1Name(name);
                    break;
                case 1:
                    dto.setTopArtist2Name(name);
                    break;
                case 2:
                    dto.setTopArtist3Name(name);
                    break;
                case 3:
                    dto.setTopArtist4Name(name);
                    break;
                case 4:
                    dto.setTopArtist5Name(name);
                    break;
                default:
                    break;
            }
        }

        return dto;
    }
}

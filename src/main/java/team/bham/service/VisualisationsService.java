package team.bham.service;

import java.util.*;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
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

    @Transactional
    public VisualisationsDTO getVisualisations(Authentication authentication, String playlistId) {
        VisualisationsDTO dto = new VisualisationsDTO();
        User user = userRepository.findOneByLogin(authentication.getName()).get();
        AppUser appUser = appUserRepository.findByUserId(user.getId()).get();
        List<Song> playlistSongs = utilService.getPlaylistSongs(appUser, playlistId);
        List<Album> playlistAlbums = new ArrayList<>();
        for (int i = 0; i < playlistSongs.size(); i++) {
            playlistAlbums.add(playlistSongs.get(i).getAlbum());
        }
        List<MainArtist> playlistArtists = new ArrayList<>();
        for (int i = 0; i < playlistAlbums.size(); i++) {
            playlistArtists.addAll(playlistAlbums.get(i).getMainArtists());
        }
        List<SpotifyGenreEntity> playlistGenreEntities = new ArrayList<>();
        for (int i = 0; i < playlistArtists.size(); i++) {
            playlistGenreEntities.addAll(playlistArtists.get(i).getSpotifyGenreEntities());
        }
        List<String> playlistGenres = new ArrayList<>();
        for (int i = 0; i < playlistGenreEntities.size(); i++) {
            playlistGenres.add(playlistGenreEntities.get(i).getSpotifyGenre());
        }
        Map<String, Integer> topFive = findTopFiveFrequentStrings(playlistGenres);

        int numOfSongs = playlistSongs.size();
        topFive.forEach((k, v) -> System.out.println("Key: " + k + ", Value: " + v));

        List<Map.Entry<String, Integer>> entries = new ArrayList<>(topFive.entrySet());
        for (int i = 0; i < entries.size(); i++) {
            String genreName = entries.get(i).getKey();
            int genreCount = entries.get(i).getValue();
            float genrePercent = ((float) genreCount / (float) numOfSongs) * 100;
            switch (i) {
                case 0:
                    dto.setTopGenre1Name(genreName);
                    dto.setTopGenre1Percent((int) genrePercent);
                    break;
                case 1:
                    dto.setTopGenre2Name(genreName);
                    dto.setTopGenre2Percent((int) genrePercent);
                    break;
                case 2:
                    dto.setTopGenre3Name(genreName);
                    dto.setTopGenre3Percent((int) genrePercent);
                    break;
                case 3:
                    dto.setTopGenre4Name(genreName);
                    dto.setTopGenre4Percent((int) genrePercent);
                    break;
                case 4:
                    dto.setTopGenre5Name(genreName);
                    dto.setTopGenre5Percent((int) genrePercent);
                    break;
            }
        }

        List<Map.Entry<String, Integer>> topArtists = findTopNArtistFrequencies(playlistArtists, 5);
        for (int i = 0; i < topArtists.size(); i++) {
            String artistName = topArtists.get(i).getKey();
            int artistCount = topArtists.get(i).getValue();
            switch (i) {
                case 0:
                    dto.setTopArtist1Name(artistName);
                    dto.setTopArtist1Count(artistCount);
                    break;
                case 1:
                    dto.setTopArtist2Name(artistName);
                    dto.setTopArtist2Count(artistCount);
                    break;
                case 2:
                    dto.setTopArtist3Name(artistName);
                    dto.setTopArtist3Count(artistCount);
                    break;
                case 3:
                    dto.setTopArtist4Name(artistName);
                    dto.setTopArtist4Count(artistCount);
                    break;
                case 4:
                    dto.setTopArtist5Name(artistName);
                    dto.setTopArtist5Count(artistCount);
                    break;
            }
        }

        float Avgpopularity = 0;
        float AvgDanceability = 0;
        float AvgEnergy = 0;
        float AvgAcousticness = 0;
        float AvgTempo = 0;
        int numOfNonEmptySongs = 0;

        for (int i = 0; i < playlistSongs.size(); i++) {
            Song song = playlistSongs.get(i);
            if (song != null && song.getSongAcousticness() != null && song.getSongAcousticness() != 0) {
                AvgAcousticness += song.getSongAcousticness();
                AvgDanceability += song.getSongDanceability();
                AvgEnergy += song.getSongEnergy();
                Avgpopularity += song.getSongPopularity();
                AvgTempo += song.getSongTempo();
                numOfNonEmptySongs++;
            }
        }

        float avgAcousticnessFloat = (float) AvgAcousticness / (float) numOfNonEmptySongs;
        dto.setAvgAcousticness(avgAcousticnessFloat);
        System.out.println("AvgAcousticness: " + AvgAcousticness / numOfNonEmptySongs);
        System.out.println(dto.getAvgAcousticness());

        float avgDanceabilityFloat = (float) AvgDanceability / (float) numOfNonEmptySongs;
        dto.setAvgDanceability(avgDanceabilityFloat);
        System.out.println("AvgDanceability: " + AvgDanceability / numOfNonEmptySongs);
        System.out.println(dto.getAvgDanceability());

        float avgEnergyFloat = (float) AvgEnergy / (float) numOfNonEmptySongs;
        dto.setAvgEnergy(avgEnergyFloat);
        System.out.println("AvgEnergy: " + AvgEnergy / numOfNonEmptySongs);
        System.out.println(dto.getAvgEnergy());

        float avgpopularityFloat = (float) Avgpopularity / (float) numOfNonEmptySongs;
        dto.setAvgpopularity(avgpopularityFloat);
        System.out.println("Avgpopularity: " + Avgpopularity / numOfNonEmptySongs);
        System.out.println(dto.getAvgpopularity());

        float avgTempoFloat = (float) AvgTempo / (float) numOfNonEmptySongs;
        dto.setAvgTempo(avgTempoFloat);
        System.out.println("AvgTempo: " + AvgTempo / numOfNonEmptySongs);
        System.out.println(dto.getAvgTempo());

        dto.setNumOfSongs(numOfSongs);
        System.out.println("NumOfSongs: " + numOfSongs);
        System.out.println(dto.getNumOfSongs());

        return dto;
    }

    public static List<Map.Entry<String, Integer>> findTopNArtistFrequencies(List<MainArtist> artists, int topN) {
        Map<String, Integer> frequencyMap = new HashMap<>();

        for (MainArtist artist : artists) {
            String name = artist.getArtistName();
            frequencyMap.put(name, frequencyMap.getOrDefault(name, 0) + 1);
        }

        return frequencyMap
            .entrySet()
            .stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(topN)
            .collect(Collectors.toList());
    }

    // find top 5 genres method
    public static Map<String, Integer> findTopFiveFrequentStrings(List<String> inputList) {
        Map<String, Integer> occurrences = new HashMap<>();

        for (String str : inputList) {
            occurrences.put(str, occurrences.getOrDefault(str, 0) + 1);
        }

        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(occurrences.entrySet());
        sortedEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        Map<String, Integer> topFive = new LinkedHashMap<>();
        int count = 0;
        for (Map.Entry<String, Integer> entry : sortedEntries) {
            topFive.put(entry.getKey(), entry.getValue());
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

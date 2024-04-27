package team.bham.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.List;
import java.util.Map;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.json.JSONArray;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.bham.domain.Album;
import team.bham.domain.AppUser;
import team.bham.domain.AppUser;
import team.bham.domain.MainArtist;
import team.bham.domain.Playlist;
import team.bham.domain.PlaylistSongJoin;
import team.bham.domain.RelatedArtists;
import team.bham.domain.Song;
import team.bham.domain.SongArtistJoin;
import team.bham.domain.SpotifyGenreEntity;
import team.bham.repository.AlbumRepository;
import team.bham.repository.MainArtistRepository;
import team.bham.repository.MainArtistRepository;
import team.bham.repository.PlaylistRepository;
import team.bham.repository.PlaylistRepository;
import team.bham.repository.PlaylistSongJoinRepository;
import team.bham.repository.RelatedArtistsRepository;
import team.bham.repository.SongArtistJoinRepository;
import team.bham.repository.SongRepository;
import team.bham.repository.SongWithArtistName;
import team.bham.repository.SongWithCollaborators;
import team.bham.service.APIWrapper.Enums.SpotifyTimeRange;
import team.bham.service.APIWrapper.Enums.SpotifyTimeRange;
import team.bham.service.APIWrapper.SpotifyAPIResponse;
import team.bham.service.APIWrapper.SpotifyAPIResponse;
import team.bham.service.dto.NetworkDTO;
import team.bham.service.dto.QueryParams;

@Service
public class TableviewService {

    private final SpotifyAPIWrapperService apiWrapper;
    private final UserService userService;
    private final UtilService utilService;

    private final PlaylistRepository playlistRepository;
    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;

    public TableviewService(
        SpotifyAPIWrapperService apiWrapper,
        UserService userService,
        UtilService utilService,
        PlaylistRepository playlistRepository,
        SongRepository songRepository,
        AlbumRepository albumRepository
    ) {
        this.apiWrapper = apiWrapper;
        this.userService = userService;
        this.utilService = utilService;
        this.playlistRepository = playlistRepository;
        this.songRepository = songRepository;
        this.albumRepository = albumRepository;
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
                info.put("id", playlist.getPlaylistSpotifyID());
                info.put("name", playlist.getPlaylistName());
                info.put("imgSmall", playlist.getPlaylistImageSmall());
                info.put("imgMedium", playlist.getPlaylistImageMedium());
                info.put("imgLarge", playlist.getPlaylistImageLarge());

                return info;
            })
            .collect(Collectors.toList());

        return ResponseEntity.ok().body(playlistInfo);
    }

    @Transactional
    public List<Song> findSongsByPlaylistId(AppUser user, String playlistId) {
        Set<Song> playlistSongsSet = new HashSet<>();
        Set<Playlist> playlists = user.getPlaylists();
        Playlist userPlaylist = playlists
            .stream()
            .filter(playlist -> playlist.getPlaylistSpotifyID().equals(playlistId))
            .findFirst()
            .orElse(null);
        Set<PlaylistSongJoin> playlistSongJoins = new HashSet<>();
        playlistSongJoins.addAll(userPlaylist.getPlaylistSongJoins());
        for (PlaylistSongJoin playlistSongJoin : playlistSongJoins) {
            playlistSongsSet.add(playlistSongJoin.getSong());
        }
        List<Song> entireLibrarySongsList = new ArrayList<>(playlistSongsSet);
        return entireLibrarySongsList;
    }

    // this is what I'm working on at the moment
    @Transactional
    public ResponseEntity<List<Map<String, Object>>> getUserPlaylistSongs(String playlistId, Authentication authentication) {
        AppUser appUser = userService.resolveAppUser(authentication.getName());
        if (appUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<SongWithArtistName> songs;
        List<SongWithCollaborators> songsCollaborators;

        if (playlistId.equals("entireLibrary")) {
            songs = songRepository.findSongsByUserId(appUser.getId());
            songsCollaborators = songRepository.findSongsCollaboratorsByUserId(appUser.getId());
        } else {
            songs = songRepository.findSongsByPlaylistId(playlistId);
            songsCollaborators = songRepository.findSongsCollaboratorsByPlaylistId(playlistId);
        }

        // Group collaborators by spotifyId
        Map<String, List<SongWithCollaborators>> collaboratorMap = songsCollaborators
            .stream()
            .collect(Collectors.groupingBy(SongWithCollaborators::getSongSpotifyId));

        List<Map<String, Object>> songInfo = songs
            .stream()
            .map(song -> {
                Map<String, Object> info = new HashMap<>();
                info.put("spotifyId", song.getSongSpotifyId());
                info.put("title", song.getSongTitle());
                info.put("artist", song.getArtistName());
                info.put("length", song.getSongDuration());
                info.put("explicit", song.getSongExplicit());
                info.put("popularity", song.getSongPopularity());
                info.put("release", song.getAlbumReleaseDate());
                info.put("acousticness", song.getSongAcousticness());
                info.put("danceability", song.getSongDanceability());
                info.put("instrumentalness", song.getSongInstrumentalness());
                info.put("energy", song.getSongEnergy());
                info.put("liveness", song.getSongLiveness());
                info.put("loudness", song.getSongLoudness());
                info.put("speechiness", song.getSongSpeechiness());
                info.put("valence", song.getSongValence());
                info.put("tempo", song.getSongTempo());

                if (collaboratorMap.containsKey(song.getSongSpotifyId())) {
                    List<SongWithCollaborators> collaborators = collaboratorMap.get(song.getSongSpotifyId());
                    List<String> names = collaborators.stream().map(SongWithCollaborators::getContributorName).collect(Collectors.toList());
                    List<String> roles = collaborators.stream().map(SongWithCollaborators::getContributorRole).collect(Collectors.toList());
                    List<String> instruments = collaborators
                        .stream()
                        .map(SongWithCollaborators::getContributorInstrument)
                        .collect(Collectors.toList());

                    info.put("contributorNames", names);
                    info.put("contributorRoles", roles);
                    info.put("contributorInstruments", instruments);
                    info.put("contributor", true);

                    System.out.println("\n\n\n\n\n\n\n");
                    System.out.println(names);
                    System.out.println("\n\n\n\n\n\n\n");
                } else {
                    info.put("contributorNames", new String[0]);
                    info.put("contributorRoles", new String[0]);
                    info.put("contributorInstruments", new String[0]);
                    info.put("contributor", false);
                }

                return info;
            })
            .collect(Collectors.toList());

        return ResponseEntity.ok().body(songInfo);
    }

    public static String toSqlList(String[] countries) {
        return Arrays
            .stream(countries)
            .map(country -> "'" + country.replace("'", "''") + "'") // Handles single quotes in country names by escaping them.
            .collect(Collectors.joining(", ", "(", ")"));
    }

    @Transactional
    public ResponseEntity<List<Map<String, Object>>> getLowertonesSongs(QueryParams queryParams) {
        System.out.println("\n\n\n\n\n\n");
        System.out.println(toSqlList(queryParams.getArtistChips()));
        System.out.println("\n\n\n\n\n\n");

        System.out.println("\n\n\n\n\n\n");
        System.out.println(queryParams);
        System.out.println("\n\n\n\n\n\n");

        String queryString = "";

        List<SongWithArtistName> songs = songRepository.findSongsByLowertonesLibrary(
            queryParams.getSearchQuery(),
            queryParams.getMinDuration(),
            queryParams.getMaxDuration(),
            queryParams.getSelectedExplicitness(),
            queryParams.getMinPopularity(),
            queryParams.getMaxPopularity(),
            queryParams.getMinAcousticness(),
            queryParams.getMaxAcousticness(),
            queryParams.getMinDanceability(),
            queryParams.getMaxDanceability(),
            queryParams.getMinInstrumentalness(),
            queryParams.getMaxInstrumentalness(),
            queryParams.getMinEnergy(),
            queryParams.getMaxEnergy(),
            queryParams.getMinLiveness(),
            queryParams.getMaxLiveness(),
            queryParams.getMinLoudness(),
            queryParams.getMaxLoudness(),
            queryParams.getMinSpeechiness(),
            queryParams.getMaxSpeechiness(),
            queryParams.getMinValence(),
            queryParams.getMaxValence()
        );

        System.out.println("\n\n\n\n\n\n");
        System.out.println("WHAT'S THE BIG IDEA");
        System.out.println("\n\n\n\n\n\n");
        //List<SongWithCollaborators> songsCollaborators = songRepository.findSongsCollaboratorsByPlaylistId(queryString);

        // Group collaborators by spotifyId
        // Map<String, List<SongWithCollaborators>> collaboratorMap = songsCollaborators
        //     .stream()
        //     .collect(Collectors.groupingBy(SongWithCollaborators::getSongSpotifyId));

        List<Map<String, Object>> songInfo = songs
            .stream()
            .map(song -> {
                Map<String, Object> info = new HashMap<>();
                info.put("spotifyId", song.getSongSpotifyId());
                info.put("title", song.getSongTitle());
                info.put("artist", song.getArtistName());
                info.put("length", song.getSongDuration());
                info.put("explicit", song.getSongExplicit());
                info.put("popularity", song.getSongPopularity());
                info.put("release", song.getAlbumReleaseDate());
                info.put("acousticness", song.getSongAcousticness());
                info.put("danceability", song.getSongDanceability());
                info.put("instrumentalness", song.getSongInstrumentalness());
                info.put("energy", song.getSongEnergy());
                info.put("liveness", song.getSongLiveness());
                info.put("loudness", song.getSongLoudness());
                info.put("speechiness", song.getSongSpeechiness());
                info.put("valence", song.getSongValence());
                info.put("tempo", song.getSongTempo());

                info.put("contributorNames", new String[0]);
                info.put("contributorRoles", new String[0]);
                info.put("contributorInstruments", new String[0]);
                info.put("contributor", false);

                return info;
            })
            .collect(Collectors.toList());

        return ResponseEntity.ok().body(songInfo);
    }
}

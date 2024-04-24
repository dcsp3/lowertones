package team.bham.service;

import java.util.ArrayList;
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
import team.bham.domain.AppUser;
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
import team.bham.service.APIWrapper.Enums.SpotifyTimeRange;
import team.bham.service.APIWrapper.SpotifyAPIResponse;
import team.bham.service.APIWrapper.SpotifyAPIResponse;
import team.bham.service.dto.NetworkDTO;

@Service
public class TableviewService {

    private final SpotifyAPIWrapperService apiWrapper;
    private final UserService userService;
    private final UtilService utilService;

    public TableviewService(SpotifyAPIWrapperService apiWrapper, UserService userService, UtilService utilService) {
        this.apiWrapper = apiWrapper;
        this.userService = userService;
        this.utilService = utilService;
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

    //this is what I'm working on at the moment
    @Transactional
    public ResponseEntity<List<Map<String, Object>>> getUserPlaylistSongs(String playlistId, Authentication authentication) {
        AppUser appUser = userService.resolveAppUser(authentication.getName());
        if (appUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        System.out.println("\n\n\n\n\n\n\n");
        System.out.println("is this thing on");
        System.out.println(playlistId);
        System.out.println("\n\n\n\n\n\n\n");

        List<Song> songs = utilService.getPlaylistSongs(playlistId);

        System.out.println("\n\n\n\n\n\n\n");
        System.out.println("does this thing work");
        System.out.println(playlistId);
        System.out.println("\n\n\n\n\n\n\n");

        List<Map<String, Object>> songInfo = songs
            .stream()
            .map(song -> {
                Map<String, Object> info = new HashMap<>();
                System.out.println("\n here's text" + song.getSongTitle());
                info.put("title", song.getSongTitle());
                //info.put("artist", song.getSongArtist());
                //info.put("contributor", song.());
                info.put("length", song.getSongDuration());
                info.put("explicit", song.getSongExplicit());
                info.put("popularity", song.getSongPopularity());
                //info.put("release", song.getSong());
                info.put("acousticness", song.getSongAcousticness());
                info.put("danceability", song.getSongDanceability());
                info.put("instrumentalness", song.getSongInstrumentalness());
                info.put("energy", song.getSongEnergy());
                info.put("liveness", song.getSongLiveness());
                info.put("loudness", song.getSongLoudness());
                info.put("speechiness", song.getSongSpeechiness());
                info.put("valence", song.getSongValence());
                info.put("tempo", song.getSongTempo());

                return info;
            })
            .collect(Collectors.toList());

        return ResponseEntity.ok().body(songInfo);
    }
}

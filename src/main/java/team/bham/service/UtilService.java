package team.bham.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import team.bham.domain.AppUser;
import team.bham.domain.MainArtist;
import team.bham.domain.Playlist;
import team.bham.domain.PlaylistSongJoin;
import team.bham.domain.Song;
import team.bham.domain.SongArtistJoin;
import team.bham.repository.PlaylistRepository;
import team.bham.repository.SongRepository;
import team.bham.service.APIScrapingService;
import team.bham.service.APIWrapper.Enums.SpotifyTimeRange;
import team.bham.service.APIWrapper.SpotifyAPIResponse;
import team.bham.service.APIWrapper.SpotifyTrack;
import team.bham.service.SpotifyAPIWrapperService;
import team.bham.web.rest.APIScrapingResource;

@Service
public class UtilService {

    private final PlaylistRepository playlistRepository;
    private final SpotifyAPIWrapperService spotifyAPIWrapperService;
    private final SongRepository songRepository;
    private final APIScrapingResource apiScrapingResource;
    private final APIScrapingService apiScrapingService;

    @Autowired
    public UtilService(
        APIScrapingService apiScrapingService,
        PlaylistRepository playlistRepository,
        SpotifyAPIWrapperService spotifyAPIWrapperService,
        SongRepository songRepository,
        APIScrapingResource apiScrapingResource
    ) {
        this.apiScrapingService = apiScrapingService;
        this.playlistRepository = playlistRepository;
        this.spotifyAPIWrapperService = spotifyAPIWrapperService;
        this.songRepository = songRepository;
        this.apiScrapingResource = apiScrapingResource;
    }

    @Transactional
    public ArrayList<Playlist> getUserPlaylists(AppUser user) {
        long appUserId = user.getId();
        ArrayList<Playlist> userPlaylists = new ArrayList<>(playlistRepository.findPlaylistsByAppUserID(appUserId));
        return userPlaylists;
    }

    @Transactional
    public Playlist getPlaylistById(Long id) {
        return playlistRepository.findById(id).orElse(null);
    }

    @Transactional
    public int countSongsByArtistInLibrary(AppUser appUser, String artistSpotifyId) {
        long appUserId = appUser.getId();
        return playlistRepository.countSongsByArtistInUserLibrary(appUserId, artistSpotifyId);
    }

    @Transactional
    public List<Song> getEntireLibrarySongs(AppUser user) {
        long appUserId = user.getId();
        Set<Song> entireLibrarySongsSet = new HashSet<>();
        ArrayList<Playlist> userPlaylists = new ArrayList<>(playlistRepository.findPlaylistsByAppUserID(appUserId));
        Set<PlaylistSongJoin> playlistSongJoins = new HashSet<>();
        for (int i = 0; i < userPlaylists.size(); i++) {
            playlistSongJoins.addAll(userPlaylists.get(i).getPlaylistSongJoins());
        }
        for (PlaylistSongJoin playlistSongJoin : playlistSongJoins) {
            entireLibrarySongsSet.add(playlistSongJoin.getSong());
        }
        List<Song> entireLibrarySongsList = new ArrayList<>(entireLibrarySongsSet);
        return entireLibrarySongsList;
    }

    @Transactional
    public List<Song> getPlaylistSongsInTimeframe(AppUser user, String playlistId, LocalDate startDate, LocalDate endDate) {
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
            if (playlistSongJoin.getSongDateAdded().isAfter(startDate) && playlistSongJoin.getSongDateAdded().isBefore(endDate)) {
                playlistSongsSet.add(playlistSongJoin.getSong());
            }
        }
        List<Song> entireLibrarySongsList = new ArrayList<>(playlistSongsSet);
        return entireLibrarySongsList;
    }

    @Transactional
    public List<Song> getPlaylistSongs(String playlistId) {
        Set<Song> playlistSongsSet = new HashSet<>();
        Playlist userPlaylist = playlistRepository.findPlaylistBySpotifyId(playlistId);
        Set<PlaylistSongJoin> playlistSongJoins = new HashSet<>();
        playlistSongJoins.addAll(userPlaylist.getPlaylistSongJoins());
        for (PlaylistSongJoin playlistSongJoin : playlistSongJoins) {
            playlistSongsSet.add(playlistSongJoin.getSong());
        }
        List<Song> entireLibrarySongsList = new ArrayList<>(playlistSongsSet);
        return entireLibrarySongsList;
    }

    @Transactional
    public List<Song> getEntireLibrarySongsAddedInTimeframe(AppUser user, LocalDate startDate, LocalDate endDate) {
        long appUserId = user.getId();
        Set<Song> entireLibrarySongsSet = new HashSet<>();
        ArrayList<Playlist> userPlaylists = new ArrayList<>(playlistRepository.findPlaylistsByAppUserID(appUserId));
        Set<PlaylistSongJoin> playlistSongJoins = new HashSet<>();
        for (int i = 0; i < userPlaylists.size(); i++) {
            playlistSongJoins.addAll(userPlaylists.get(i).getPlaylistSongJoins());
        }
        for (PlaylistSongJoin playlistSongJoin : playlistSongJoins) {
            if (playlistSongJoin.getSongDateAdded().isAfter(startDate) && playlistSongJoin.getSongDateAdded().isBefore(endDate)) {
                entireLibrarySongsSet.add(playlistSongJoin.getSong());
            }
        }
        List<Song> entireLibrarySongsList = new ArrayList<>(entireLibrarySongsSet);
        return entireLibrarySongsList;
    }

    @Transactional
    public List<Song> getUserTopSongs(AppUser user, SpotifyTimeRange timeRange) {
        /*
        List<Song> topSongs = new ArrayList<>();
        SpotifyAPIResponse<JSONObject> topSongsJson = spotifyAPIWrapperService.getCurrentUserTopTracks(user, timeRange);
        for (int i = 0; topSongsJson.getData().getJSONArray("items").length() > i; i++) {
            String currID = topSongsJson.getData().getJSONArray("items").getJSONObject(i).getString("id");
            if (songRepository.findSongBySpotifyId(currID) != null) {
                Song song = songRepository.findSongBySpotifyId(currID);
                topSongs.add(song);
                System.out.println("Song added to list: " + song.getSongTitle());
            } else {
                // Song not found - add song
                // causes lots of exceptions currently
                SpotifyTrack track = new SpotifyTrack();
                //NEED TO ADD THE REST OF THE TRACK INFO
                //apiScrapingResource.storeTrack(track);
            }
        } 
        */

        SpotifyAPIResponse<JSONObject> topSongsJson = spotifyAPIWrapperService.getCurrentUserTopTracks(user, timeRange);
        List<Song> list = apiScrapingService.storeSongsFromTopSongs(topSongsJson, user);
        return list;
    }

    @Transactional
    public List<MainArtist> getUserTopArtists(AppUser user, SpotifyTimeRange timeRange) {
        SpotifyAPIResponse<JSONObject> topArtistsJson = spotifyAPIWrapperService.getCurrentUserTopArtists(user, timeRange);
        List<MainArtist> list = apiScrapingService.storeArtistsFromTopArtists(topArtistsJson, user);
        return list;
    }

    @Transactional
    public Set<MainArtist> getMainArtistsFromSongs(List<Song> songs) {
        Set<MainArtist> mainArtists = new HashSet<>();
        for (int i = 0; i < songs.size(); i++) {
            try {
                Set<SongArtistJoin> joins = songs.get(i).getSongArtistJoins();
                mainArtists.addAll(joins.stream().map(SongArtistJoin::getMainArtist).collect(Collectors.toSet()));
            } catch (Exception e) {
                System.out.println("Error getting main artists from songs");
            }
        }
        return mainArtists;
    }
}

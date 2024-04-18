package team.bham.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.bham.domain.AppUser;
import team.bham.domain.Playlist;
import team.bham.domain.PlaylistSongJoin;
import team.bham.domain.Song;
import team.bham.repository.PlaylistRepository;
import team.bham.service.APIWrapper.Enums.SpotifyTimeRange;

@Service
public class UtilService {

    private final PlaylistRepository playlistRepository;

    @Autowired
    public UtilService(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    @Transactional
    public List<Song> getEntireLibrarySongs(AppUser user) {
        long appUserId = user.getId();
        Set<Song> entireLibrarySongsSet = new HashSet<>();
        ArrayList<Playlist> userPlaylists = playlistRepository.findPlaylistsByAppUserID(appUserId);
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
    public List<Song> getEntireLibrarySongsAddedInTimeframe(AppUser user, SpotifyTimeRange timeRange) {
        long appUserId = user.getId();
        Set<Song> entireLibrarySongsSet = new HashSet<>();
        ArrayList<Playlist> userPlaylists = playlistRepository.findPlaylistsByAppUserID(appUserId);
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
}

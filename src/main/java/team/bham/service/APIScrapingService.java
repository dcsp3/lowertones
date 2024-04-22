package team.bham.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Collections;
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
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Collectors;
import javax.swing.text.StyledEditorKit.BoldAction;
import org.json.JSONArray;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import team.bham.domain.*;
import team.bham.domain.enumeration.AlbumType;
import team.bham.domain.enumeration.ReleaseDatePrecision;
import team.bham.repository.*;
import team.bham.service.APIWrapper.*;
import team.bham.service.APIWrapper.Enums.*;
import team.bham.service.SpotifyAPIWrapperService;
import team.bham.service.UserService;
import team.bham.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

@Service
@Transactional
public class APIScrapingService {

    private final UserRepository userRepository;
    private final AppUserRepository appUserRepository;
    private final AlbumRepository albumRepository;
    private final MainArtistRepository mainArtistRepository;
    private final PlaylistRepository playlistRepository;
    private final PlaylistSongJoinRepository playlistSongJoinRepository;
    private final SongArtistJoinRepository songArtistJoinRepository;
    private final SongRepository songRepository;
    private final UserService userService;
    private final SpotifyAPIWrapperService apiWrapper;

    public APIScrapingService(
        UserRepository userRepository,
        AppUserRepository appUserRepository,
        AlbumRepository albumRepository,
        MainArtistRepository mainArtistRepository,
        PlaylistRepository playlistRepository,
        PlaylistSongJoinRepository playlistSongJoinRepository,
        SongArtistJoinRepository songArtistJoinRepository,
        SongRepository songRepository,
        UserService userService,
        SpotifyAPIWrapperService apiWrapper
    ) {
        this.userRepository = userRepository;
        this.appUserRepository = appUserRepository;
        this.albumRepository = albumRepository;
        this.mainArtistRepository = mainArtistRepository;
        this.playlistRepository = playlistRepository;
        this.playlistSongJoinRepository = playlistSongJoinRepository;
        this.songArtistJoinRepository = songArtistJoinRepository;
        this.songRepository = songRepository;
        this.userService = userService;
        this.apiWrapper = apiWrapper;
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CompletableFuture<Void> beginScrapeTask(String username) {
        AppUser appUser = userService.resolveAppUser(username);
        scrape(appUser);
        return CompletableFuture.completedFuture(null);
    }

    @Transactional
    public void scrape(AppUser appUser) {
        ArrayList<SpotifySimplifiedPlaylist> playlistIds = apiWrapper.getCurrentUserPlaylists(appUser).getData();
        Set<Playlist> userPlaylists = playlistRepository.findPlaylistsByAppUserID(appUser.getId());
        //scrape playlist info, create playlist object, store in repo and add to appuser (store appuser again), ...
        for (int i = 0; i < playlistIds.size(); i++) {
            //check if we've already scraped this playlist
            SpotifySimplifiedPlaylist curSimplePlaylist = playlistIds.get(i);
            Playlist playlist = userPlaylists
                .stream()
                .filter(p -> p.getPlaylistSpotifyID().equals(curSimplePlaylist.getSpotifyId()))
                .findFirst()
                .orElse(null);
            if (playlist != null) {
                //snapshot id same - skip
                if (playlist.getPlaylistSnapshotID().equals(playlistIds.get(i).getSnapshotId())) {
                    continue;
                }
                //otherwise - maybe just delete all playlist-song joins???? todo
            } else playlist = new Playlist();

            //grab full playlist details from simple playlist id
            SpotifyPlaylist curPlaylist = apiWrapper.getPlaylistDetails(appUser, curSimplePlaylist.getSpotifyId()).getData();

            //get song ids
            ArrayList<String> songSpotifyIds = curPlaylist
                .getTracks()
                .stream()
                .map(SpotifyTrack::getId)
                .collect(Collectors.toCollection(ArrayList::new));
            List<String> albumSpotifyIds = curPlaylist
                .getTracks()
                .stream()
                .map(t -> t.getAlbum().getSpotifyId())
                .collect(Collectors.toList());
            List<String> artistSpotifyIds = curPlaylist
                .getTracks()
                .stream()
                .map(t -> t.getArtist().getSpotifyId())
                .collect(Collectors.toList());
            List<Song> existingSongs = songRepository.findBySongSpotifyIDIn(songSpotifyIds);
            List<MainArtist> existingArtists = mainArtistRepository.findByArtistSpotifyIDIn(artistSpotifyIds);
            List<Album> existingAlbums = albumRepository.findByAlbumSpotifyIDIn(albumSpotifyIds);

            playlist.setAppUser(appUser);
            playlist.setDateAddedToDB(LocalDate.now());
            playlist.setDateLastModified(LocalDate.now());
            playlist.setPlaylistSpotifyID(curPlaylist.getPlaylistId());
            playlist.setPlaylistSnapshotID(curPlaylist.getSnapshotId());
            playlist.setPlaylistName(curPlaylist.getName());
            playlist.setPlaylistImageSmall(curPlaylist.getPlaylistImageSmall());
            playlist.setPlaylistImageMedium(curPlaylist.getPlaylistImageMedium());
            playlist.setPlaylistImageLarge(curPlaylist.getPlaylistImageLarge());
            playlistRepository.save(playlist);

            ArrayList<SpotifyTrack> tracks = curPlaylist.getTracks();
            Set<SpotifyTrack> trackSet = new HashSet<>(tracks);
            for (SpotifyTrack track : trackSet) {
                MainArtist a = getArtistScraped(track.getArtist().getSpotifyId(), existingArtists);
                if (a == null) {
                    a = storeArtist(track.getArtist());
                    existingArtists.add(a);
                }
                Album album = getAlbumScraped(track.getAlbum().getSpotifyId(), existingAlbums);
                if (album == null) {
                    album = storeAlbum(track.getAlbum());
                    existingAlbums.add(album);
                }

                //check if song already exists. if not, store in db and create song-artist join
                //songs already in db should always have song-artist join already
                Song s = getSongScraped(track.getId(), existingSongs);
                if (s == null) {
                    s = storeTrack(track, album);
                    existingSongs.add(s);

                    SongArtistJoin songArtistJoin = new SongArtistJoin();
                    songArtistJoin.setSong(s);
                    songArtistJoin.setMainArtist(a);
                    songArtistJoin.setTopTrackIndex(0); //????
                    songArtistJoinRepository.save(songArtistJoin);
                }

                PlaylistSongJoin playlistSongJoin = new PlaylistSongJoin();
                playlistSongJoin.setPlaylist(playlist);
                playlistSongJoin.setSong(s);
                playlistSongJoin.setSongOrderIndex(i); //tracks in SpotifyPlaylist obj *should* be in correct order
                playlistSongJoin.setSongDateAdded(LocalDate.now());
                playlistSongJoinRepository.save(playlistSongJoin);
                //todo: genre stuff.
            }
        }
    }

    @Transactional
    public Album storeAlbum(SpotifyAlbum album) {
        Album a = new Album();
        a.setAlbumName(album.getName());
        a.setAlbumSpotifyID(album.getSpotifyId());
        a.setAlbumCoverArt(album.getCoverArtURL());
        a.setAlbumReleaseDate(album.getReleaseDate());
        switch (album.getReleasePrecision()) {
            case YEAR:
                a.setReleaseDatePrecision(ReleaseDatePrecision.YEAR);
                break;
            case MONTH:
                a.setReleaseDatePrecision(ReleaseDatePrecision.MONTH);
                break;
            case DAY:
                a.setReleaseDatePrecision(ReleaseDatePrecision.DAY);
                break;
        }
        a.setAlbumPopularity(album.getPopularity());
        switch (album.getAlbumType()) {
            case "album":
                a.setAlbumType(AlbumType.ALBUM);
                break;
            case "single":
                a.setAlbumType(AlbumType.SINGLE);
                break;
            case "compilation":
                a.setAlbumType(AlbumType.COMPILATION);
                break;
        }
        a.setDateAddedToDB(LocalDate.now());
        a.setDateLastModified(LocalDate.now());
        a.setMusicbrainzMetadataAdded(false);

        albumRepository.save(a);
        return a;
    }

    @Transactional
    public MainArtist storeArtist(SpotifyArtist artist) {
        MainArtist mainArtist = new MainArtist();
        mainArtist.setArtistSpotifyID(artist.getSpotifyId());
        mainArtist.setArtistName(artist.getName());
        mainArtist.setArtistPopularity(artist.getPopularity());
        if (artist.getImages() != null) {
            switch (artist.getImages().size()) {
                case 0:
                    break;
                case 1:
                    mainArtist.setArtistImageLarge(artist.getImages().get(0).getUrl());
                    break;
                case 2:
                    mainArtist.setArtistImageLarge(artist.getImages().get(0).getUrl());
                    mainArtist.setArtistImageMedium(artist.getImages().get(1).getUrl());
                    break;
                default:
                    mainArtist.setArtistImageLarge(artist.getImages().get(0).getUrl());
                    mainArtist.setArtistImageMedium(artist.getImages().get(1).getUrl());
                    mainArtist.setArtistImageSmall(artist.getImages().get(2).getUrl());
                    break;
            }
        }

        //todo: images
        mainArtistRepository.save(mainArtist);
        return mainArtist;
    }

    @Transactional
    public void test(AppUser appUser) {
        //grab full playlist details from simple playlist id
        SpotifyPlaylist curPlaylist = apiWrapper.getPlaylistDetails(appUser, "48DDkoj8vW5JJl1W8vISmC").getData();

        //get song ids
        ArrayList<String> songSpotifyIds = curPlaylist
            .getTracks()
            .stream()
            .map(SpotifyTrack::getId)
            .collect(Collectors.toCollection(ArrayList::new));
        List<String> albumSpotifyIds = curPlaylist.getTracks().stream().map(t -> t.getAlbum().getSpotifyId()).collect(Collectors.toList());
        List<String> artistSpotifyIds = curPlaylist
            .getTracks()
            .stream()
            .map(t -> t.getArtist().getSpotifyId())
            .collect(Collectors.toList());
        List<Song> existingSongs = songRepository.findBySongSpotifyIDIn(songSpotifyIds);
        List<MainArtist> existingArtists = mainArtistRepository.findByArtistSpotifyIDIn(artistSpotifyIds);
        List<Album> existingAlbums = albumRepository.findByAlbumSpotifyIDIn(albumSpotifyIds);

        Playlist playlist = new Playlist();

        playlist.setAppUser(appUser);
        playlist.setDateAddedToDB(LocalDate.now());
        playlist.setDateLastModified(LocalDate.now());
        playlist.setPlaylistSpotifyID(curPlaylist.getPlaylistId());
        playlist.setPlaylistSnapshotID(curPlaylist.getSnapshotId());
        playlist.setPlaylistName(curPlaylist.getName());
        playlist.setPlaylistImageSmall(curPlaylist.getPlaylistImageSmall());
        playlist.setPlaylistImageMedium(curPlaylist.getPlaylistImageMedium());
        playlist.setPlaylistImageLarge(curPlaylist.getPlaylistImageLarge());
        playlistRepository.save(playlist);

        ArrayList<SpotifyTrack> tracks = curPlaylist.getTracks();
        for (int j = 0; j < tracks.size(); j++) {
            MainArtist a = getArtistScraped(tracks.get(j).getArtist().getSpotifyId(), existingArtists);
            if (a == null) {
                a = storeArtist(tracks.get(j).getArtist());
            }
            Album album = getAlbumScraped(tracks.get(j).getAlbum().getSpotifyId(), existingAlbums);
            if (album == null) {
                album = storeAlbum(tracks.get(j).getAlbum());
            }

            //check if song already exists. if not, store in db and create song-artist join
            //songs already in db should always have song-artist join already
            Song s = getSongScraped(tracks.get(j).getId(), existingSongs);
            if (s == null) {
                s = storeTrack(tracks.get(j), album);

                SongArtistJoin songArtistJoin = new SongArtistJoin();
                songArtistJoin.setSong(s);
                songArtistJoin.setMainArtist(a);
                songArtistJoin.setTopTrackIndex(0); //????
                songArtistJoinRepository.save(songArtistJoin);
            }

            PlaylistSongJoin playlistSongJoin = new PlaylistSongJoin();
            playlistSongJoin.setPlaylist(playlist);
            playlistSongJoin.setSong(s);
            playlistSongJoin.setSongOrderIndex(0); //tracks in SpotifyPlaylist obj *should* be in correct order
            playlistSongJoin.setSongDateAdded(LocalDate.now());
            playlistSongJoinRepository.save(playlistSongJoin);
            //todo: genre stuff.
        }
    }

    @Transactional
    public Song storeTrack(SpotifyTrack track, Album album) {
        //Song s = songRepository.findSongBySpotifyId(track.getId());
        // if (s != null) {
        //    return s;
        // }

        Song song = new Song();
        song.setSongSpotifyID(track.getId());
        song.setSongTitle(track.getName());
        song.setSongAlbumID(track.getAlbum().getSpotifyId());
        song.setSongExplicit(track.getExplicit());
        song.setSongDuration(track.getDuration());
        song.setSongAlbumType(AlbumType.ALBUM);
        song.setSongPopularity(track.getPopularity());
        song.setSongDateAddedToDB(LocalDate.now());
        song.setSongDateLastModified(LocalDate.now());
        song.setSongPreviewURL(track.getPreviewUrl());
        song.setSongTrackFeaturesAdded(false);
        //awful, todo: remove
        if (track.getAudioFeatures() != null) {
            song.setSongTrackFeaturesAdded(true);
            song.setSongAcousticness(track.getAudioFeatures().getAcousticness());
            song.setSongDanceability(track.getAudioFeatures().getDanceability());
            song.setSongEnergy(track.getAudioFeatures().getEnergy());
            song.setSongInstrumentalness(track.getAudioFeatures().getInstrumentalness());
            song.setSongLiveness(track.getAudioFeatures().getLiveness());
            song.setSongLoudness(track.getAudioFeatures().getLoudness());
            song.setSongSpeechiness(track.getAudioFeatures().getSpeechiness());
            song.setSongTempo(track.getAudioFeatures().getTempo());
            song.setSongValence(track.getAudioFeatures().getValence());
            song.setSongKey(track.getAudioFeatures().getKey());
            song.setSongTimeSignature(track.getAudioFeatures().getTimeSignature());
        }
        song.setAlbum(album);
        songRepository.save(song);
        return song;
    }

    private Song getSongScraped(String spotifyId, List<Song> curSongs) {
        for (Song song : curSongs) {
            if (song.getSongSpotifyID().equals(spotifyId)) return song;
        }

        return null;
    }

    private MainArtist getArtistScraped(String spotifyId, List<MainArtist> curArtists) {
        for (MainArtist artist : curArtists) {
            if (artist.getArtistSpotifyID().equals(spotifyId)) return artist;
        }

        return null;
    }

    private Album getAlbumScraped(String spotifyId, List<Album> curAlbums) {
        for (Album album : curAlbums) {
            if (album.getAlbumSpotifyID().equals(spotifyId)) return album;
        }

        return null;
    }
}

package team.bham.service;

import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import liquibase.integration.commandline.Main;
import org.mapstruct.control.MappingControl.Use;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.bham.domain.AppUser;
import team.bham.domain.Contributor;
import team.bham.domain.MainArtist;
import team.bham.domain.Playlist;
import team.bham.domain.PlaylistSongJoin;
import team.bham.domain.Song;
import team.bham.domain.SongArtistJoin;
import team.bham.domain.User;
import team.bham.repository.AppUserRepository;
import team.bham.repository.ContributorRepository;
import team.bham.repository.MainArtistRepository;
import team.bham.repository.PlaylistRepository;
import team.bham.repository.SongRepository;
import team.bham.repository.UserRepository;
import team.bham.service.APIWrapper.Enums.SpotifyTimeRange;
import team.bham.service.UtilService;
import team.bham.service.dto.RecappedDTO;
import team.bham.service.dto.RecappedRequest;

@Service
public class RecappedService {

    private final UserRepository userRepository;
    private final AppUserRepository appUserRepository;
    private final PlaylistRepository playlistRepository;
    private final MainArtistRepository mainArtistRepository;
    private final ContributorRepository contributorRepository;
    private final SongRepository songRepository;
    private final UtilService utilService;

    public RecappedService(
        AppUserRepository appUserRepository,
        UserRepository userRepository,
        PlaylistRepository playlistRepository,
        MainArtistRepository mainArtistRepository,
        ContributorRepository contributorRepository,
        SongRepository songRepository,
        UtilService utilService
    ) {
        this.appUserRepository = appUserRepository;
        this.userRepository = userRepository;
        this.playlistRepository = playlistRepository;
        this.mainArtistRepository = mainArtistRepository;
        this.contributorRepository = contributorRepository;
        this.songRepository = songRepository;
        this.utilService = utilService;
    }

    @Transactional(readOnly = true)
    private Map<Contributor, Long> countContributorsByRoleAndSongs(String role, List<Song> songs) {
        List<Long> songIds = songs.stream().map(Song::getId).collect(Collectors.toList());
        List<Object[]> results = contributorRepository.countContributorsByRoleAndSongIds(role, songIds);
        Map<Contributor, Long> contributorCountMap = new HashMap<>();
        for (Object[] result : results) {
            contributorCountMap.put((Contributor) result[0], (Long) result[1]);
        }
        return contributorCountMap;
    }

    @Transactional
    public RecappedDTO calculateRecappedInfo(RecappedRequest request, Authentication authentication) {
        RecappedDTO dto = new RecappedDTO();
        User user = userRepository.findOneByLogin(authentication.getName()).get();
        AppUser appUser = appUserRepository.findByUserId(user.getId()).get();
        long appUserId = appUser.getId();
        List<Song> songs = new ArrayList<>();
        int duration = 0;
        SpotifyTimeRange timeRange = null;
        String requestTimeframe = request.getTimeframe();
        //case statement to set timeRange
        switch (requestTimeframe) {
            case "LAST_MONTH":
                timeRange = SpotifyTimeRange.SHORT_TERM;
                break;
            case "LAST_6_MONTHS":
                timeRange = SpotifyTimeRange.MEDIUM_TERM;
                break;
            case "LAST_FEW_YEARS":
                timeRange = SpotifyTimeRange.LONG_TERM;
                break;
            default:
                break;
        }

        //get song list based on request
        if (request.isScanEntireLibrary()) {
            songs = utilService.getEntireLibrarySongsAddedInTimeframe(appUser, timeRange);
            System.out.println("scan entire library /n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n/n");
            System.out.println("songs: " + songs.size());
        } else if (request.isScanTopSongs()) {
            songs = utilService.getUserTopSongs(appUser, timeRange);
            System.out.println("scan Top Songs");
            System.out.println("songs: " + songs.size());
        } else if (request.isScanSpecificPlaylist()) {
            songs = utilService.getPlaylistSongs(request.getPlaylistId());
            System.out.println("scan Playlist");
            System.out.println("songs: " + songs.size());
        } else {
            // No songs to scan
            return dto;
        }

        //get main artists from songs
        Set<MainArtist> mainArtists = utilService.getMainArtistsFromSongs(songs);

        //get total duration of songs in milliseconds
        for (int i = 0; i < songs.size(); i++) {
            System.out.println("song duration scanning: " + songs.get(i).getSongTitle() + " + duration: " + songs.get(i).getSongDuration());
            System.out.println("current i is: " + i);
            duration = duration + songs.get(i).getSongDuration();
        }

        // 3. Get Contributors for each song, count the number of occurrences, and save the top 5
        Map<Contributor, Long> topContributors = countContributorsByRoleAndSongs(request.getMusicianType().name(), songs);

        // Sort the map by value and pick the top 5 contributors
        List<Map.Entry<Contributor, Long>> sortedContributors = topContributors
            .entrySet()
            .stream()
            .sorted(Map.Entry.<Contributor, Long>comparingByValue().reversed())
            .limit(5)
            .collect(Collectors.toList());

        // 4. For the top contributor, check for a Spotify image or an album cover
        //String imageUrl = getContributorImageUrl(sortedContributors.get(0).getKey());

        // 5. Get 2 other album covers for top songs by the top contributor
        //List<String> additionalAlbumCovers = getAdditionalAlbumCovers(sortedContributors.get(0).getKey(), songs);

        // 6. Construct the DTO with the gathered information
        //constructRecappedDTO(dto, sortedContributors, imageUrl, additionalAlbumCovers);

        System.out.println("duration: " + duration);
        System.out.println("mainArtists: " + mainArtists.size());

        dto.setTotalSongs(songs.size());
        dto.setTotalDuration(duration / 60000);
        dto.setTotalArtists(mainArtists.size());
        dto.setTotalContributors(100);
        dto.setTopUnder1kName("1kartist");
        dto.setTopUnder1kImage("https://i.scdn.co/image/ab6761610000e5ebae4a51ded0c9a8b75278f5eb");
        dto.setTopUnder10kName("10kartist");
        dto.setTopUnder10kImage("https://i.scdn.co/image/ab6761610000e5ebae4a51ded0c9a8b75278f5eb");
        dto.setTopUnder100kName("100kartist");
        dto.setTopUnder100kImage("https://i.scdn.co/image/ab6761610000e5ebae4a51ded0c9a8b75278f5eb");
        dto.setNumOneArtistName("Kenny Beats");
        dto.setNumOneAristNumSongs(1344);
        dto.setNumTwoArtistName("KaytranadaKaytranadaKaytranadaKaytranada");
        dto.setNumTwoAristNumSongs(100);
        dto.setNumThreeArtistName("Kanye West");
        dto.setNumThreeAristNumSongs(90);
        dto.setNumFourArtistName("Boi-1da");
        dto.setNumFourAristNumSongs(80);
        dto.setNumFiveArtistName("JPEGMAFIA");
        dto.setNumFiveAristNumSongs(70);
        dto.setNumOneArtistImage("https://i.scdn.co/image/ab6761610000e5ebae4a51ded0c9a8b75278f5eb");
        dto.setNumTwoArtistImage("https://i.scdn.co/image/ab67616100005174069ff978752054a7e015daab");
        dto.setNumThreeArtistImage("https://i.scdn.co/image/ab67616100005174069ff978752054a7e015daab");
        dto.setNumFourArtistImage("https://i.scdn.co/image/ab67616100005174069ff978752054a7e015daab");
        dto.setNumFiveArtistImage("https://i.scdn.co/image/ab67616100005174069ff978752054a7e015daab");
        dto.setNumOneFirstCoverImg("https://i.scdn.co/image/ab67616d0000b2735c2bbb4d4a66a70310705a26");
        dto.setNumOneFirstSongTitle("Leonard");
        dto.setNumOneFirstSongMainArtist("Kenny Beatseasedae");
        dto.setNumOneSecondCoverImg("https://i.scdn.co/image/ab67616d0000b273922a12ba0b5a66f034dc9959");
        dto.setNumOneSecondSongTitle("Lay_Up.m4a");
        dto.setNumOneSecondSongMainArtist("Denzel Curry");

        try {
            TimeUnit.SECONDS.sleep(0);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dto;
    }
    // Add additional service methods as needed
}

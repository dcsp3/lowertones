package team.bham.service;

import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.mapstruct.control.MappingControl.Use;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.bham.domain.AppUser;
import team.bham.domain.Contributor;
import team.bham.domain.Song;
import team.bham.domain.User;
import team.bham.repository.AppUserRepository;
import team.bham.repository.ContributorRepository;
import team.bham.repository.MainArtistRepository;
import team.bham.repository.PlaylistRepository;
import team.bham.repository.SongRepository;
import team.bham.repository.UserRepository;
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

    public RecappedService(
        AppUserRepository appUserRepository,
        UserRepository userRepository,
        PlaylistRepository playlistRepository,
        MainArtistRepository mainArtistRepository,
        ContributorRepository contributorRepository,
        SongRepository songRepository
    ) {
        this.appUserRepository = appUserRepository;
        this.userRepository = userRepository;
        this.playlistRepository = playlistRepository;
        this.mainArtistRepository = mainArtistRepository;
        this.contributorRepository = contributorRepository;
        this.songRepository = songRepository;
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

    private List<Song> getUserTopSongs(AppUser user, String dateRange) {
        List<Song> topSongs = new ArrayList<>();
        //MAKE CALLS TO USERS TOP SONGS FOR PERIOD
        /*
        if (dateRange.equals("LAST_MONTH")) {

        }
        else if (dateRange.equals("LAST_6_MONTHs")) {

        }
        else if (dateRange.equals("LAST_FEW_YEARS")) { 

        } else {

        }
        */
        return topSongs;
    }

    private List<Song> getPlaylistSongs(AppUser user, String playlistId) {
        List<Song> playlistSongs = new ArrayList<>();

        //MAKE CALLS TO USERS PLAYLIST SONGS
        return playlistSongs;
    }

    private List<Song> getEntireLibrarySongs(AppUser user) {
        List<Song> entireLibrarySongs = new ArrayList<>();
        //MAKE CALLS TO USERS ENTIRE LIBRARY SONGS
        return entireLibrarySongs;
    }

    public RecappedDTO calculateRecappedInfo(RecappedRequest request, Authentication authentication) {
        // Placeholder for logic to generate RecappedDTO based on the request
        RecappedDTO dto = new RecappedDTO();
        User user = userRepository.findOneByLogin(authentication.getName()).get();
        AppUser appUser = appUserRepository.findByUserId(user.getId()).get();
        List<Song> songs = new ArrayList<>();

        if (request.isScanEntireLibrary()) {
            songs = getEntireLibrarySongs(appUser);
        } else if (request.isScanTopSongs()) {
            songs = getUserTopSongs(appUser, request.getDateRange());
        } else if (request.isScanSpecificPlaylist()) {
            songs = getPlaylistSongs(appUser, request.getPlaylistId());
        } else {
            // No songs to scan
            return dto;
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
        dto.setNumOneArtistName("Kenny Beats");
        dto.setNumOneAristNumSongs(134);
        dto.setNumTwoArtistName("Kaytranada");
        dto.setNumTwoAristNumSongs(100);
        dto.setNumThreeArtistName("Kanye West");
        dto.setNumThreeAristNumSongs(90);
        dto.setNumFourArtistName("Boi-1da");
        dto.setNumFourAristNumSongs(80);
        dto.setNumFiveArtistName("JPEGMAFIA");
        dto.setNumFiveAristNumSongs(70);
        dto.setNumOneHeroImg("https://i.scdn.co/image/ab6761610000e5ebae4a51ded0c9a8b75278f5eb");
        dto.setNumOneFirstCoverImg("https://i.scdn.co/image/ab67616d0000b2735c2bbb4d4a66a70310705a26");
        dto.setNumOneFirstSongTitle("Leonard");
        dto.setNumOneFirstSongMainArtist("Kenny Beats");
        dto.setNumOneSecondCoverImg("https://i.scdn.co/image/ab67616d0000b273922a12ba0b5a66f034dc9959");
        dto.setNumOneSecondSongTitle("Lay_Up.m4a");
        dto.setNumOneSecondSongMainArtist("Denzel Curry");

        try {
            TimeUnit.SECONDS.sleep(20);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dto;
    }
    // Add additional service methods as needed
}

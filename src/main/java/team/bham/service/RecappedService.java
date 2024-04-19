package team.bham.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.bham.domain.AppUser;
import team.bham.domain.Contributor;
import team.bham.domain.MainArtist;
import team.bham.domain.Song;
import team.bham.domain.User;
import team.bham.repository.AppUserRepository;
import team.bham.repository.ContributorRepository;
import team.bham.repository.MainArtistRepository;
import team.bham.repository.PlaylistRepository;
import team.bham.repository.SongRepository;
import team.bham.repository.UserRepository;
import team.bham.service.APIWrapper.Enums.SpotifySearchType;
import team.bham.service.APIWrapper.Enums.SpotifyTimeRange;
import team.bham.service.APIWrapper.SpotifyAPIResponse;
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
    private final SpotifyAPIWrapperService spotifyAPIWrapperService;

    public RecappedService(
        AppUserRepository appUserRepository,
        UserRepository userRepository,
        PlaylistRepository playlistRepository,
        MainArtistRepository mainArtistRepository,
        ContributorRepository contributorRepository,
        SongRepository songRepository,
        UtilService utilService,
        SpotifyAPIWrapperService spotifyAPIWrapperService
    ) {
        this.appUserRepository = appUserRepository;
        this.userRepository = userRepository;
        this.playlistRepository = playlistRepository;
        this.mainArtistRepository = mainArtistRepository;
        this.contributorRepository = contributorRepository;
        this.songRepository = songRepository;
        this.utilService = utilService;
        this.spotifyAPIWrapperService = spotifyAPIWrapperService;
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
    private String getContributorImageUrl(String name, AppUser user) {
        JSONObject response = spotifyAPIWrapperService.search(name, SpotifySearchType.ARTIST, user);
        Integer thresholdRatio = 95;
        Integer thresholdPartialRatio = 90;
        JSONArray artists = response.getJSONObject("artists").getJSONArray("items");
        for (int i = 0; i < artists.length(); i++) {
            JSONObject artist = artists.getJSONObject(i);
            String artistName = artist.getString("name");
            int similarity = FuzzySearch.ratio(name, artistName);
            if (similarity > thresholdRatio) {
                JSONArray images = artist.getJSONArray("images");
                if (images.length() > 0) {
                    return images.getJSONObject(0).getString("url");
                }
            }
        }
        for (int i = 0; i < artists.length(); i++) {
            JSONObject artist = artists.getJSONObject(i);
            String artistName = artist.getString("name");
            int similarity = FuzzySearch.partialRatio(name, artistName);
            if (similarity > thresholdPartialRatio) {
                JSONArray images = artist.getJSONArray("images");
                if (images.length() > 0) {
                    return images.getJSONObject(0).getString("url");
                }
            }
        }
        return null;
    }

    @Transactional
    public RecappedDTO calculateRecappedInfo(RecappedRequest request, Authentication authentication) {
        // 1. Prepare formatting, handle request, instantiate objects
        RecappedDTO dto = new RecappedDTO();
        User user = userRepository.findOneByLogin(authentication.getName()).get();
        AppUser appUser = appUserRepository.findByUserId(user.getId()).get();
        long appUserId = appUser.getId();
        List<Song> songs = new ArrayList<>();
        int duration = 0;
        SpotifyTimeRange timeRange = null;
        LocalDate startDate = null;
        LocalDate endDate = null;
        String requestTimeframe = request.getTimeframe();

        // case statement to set timeRange
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
                if (requestTimeframe.matches("\\d{4}-\\d{2}-\\d{2} - \\d{4}-\\d{2}-\\d{2}")) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    try {
                        String[] dates = requestTimeframe.split(" - ");
                        System.out.println("dates- start: " + dates[0] + " and end: " + dates[1]);
                        startDate = LocalDate.parse(dates[0], formatter);
                        endDate = LocalDate.parse(dates[1], formatter);
                    } catch (DateTimeParseException e) {
                        System.err.println("Invalid date format: " + e.getMessage());
                    }
                } else {
                    System.out.println("timeframe is here      :" + request.getTimeframe());
                    System.err.println("Unknown timeframe format");
                }
                break;
        }
        System.out.println("scantype is here      :" + request.getScanType());

        // 2. Get song list, artist list, duration based on request
        if (request.getScanType().equals("entireLibrary")) {
            songs = utilService.getEntireLibrarySongsAddedInTimeframe(appUser, startDate, endDate);
            System.out.println("scan entire library");
            System.out.println("songs: " + songs.size());
        } else if (request.getScanType().equals("topSongs")) {
            songs = utilService.getUserTopSongs(appUser, timeRange);
            System.out.println("scan Top Songs");
            System.out.println("songs: " + songs.size());
        } else if (request.getScanType().length() == 22) {
            songs = utilService.getPlaylistSongsInTimeframe(request.getScanType(), startDate, endDate);
            System.out.println("scan Playlist");
            System.out.println("songs: " + songs.size());
        } else {
            // No songs to scan
            System.err.println("Unknown scan type, maybe playlistID is invalid");
            return dto;
        }
        dto.setTotalSongs(songs.size());

        // get main artists from songs
        Set<MainArtist> mainArtists = utilService.getMainArtistsFromSongs(songs);
        dto.setTotalArtists(mainArtists.size());

        // get total duration of songs in milliseconds
        for (int i = 0; i < songs.size(); i++) {
            System.out.println("song duration scanning: " + songs.get(i).getSongTitle() + " + duration: " + songs.get(i).getSongDuration());
            System.out.println("current i is: " + i);
            duration = duration + songs.get(i).getSongDuration();
        }
        dto.setTotalDuration(duration / 60000);

        // 3. Get Contributors for each song, count the number of occurrences, and save
        // the top 5
        Map<Contributor, Long> topContributors = countContributorsByRoleAndSongs(request.getMusicianType().name(), songs);

        // Sort the map by value and pick the top 5 contributors
        List<Map.Entry<Contributor, Long>> sortedContributors = topContributors
            .entrySet()
            .stream()
            .sorted(Map.Entry.<Contributor, Long>comparingByValue().reversed())
            .limit(5)
            .collect(Collectors.toList());

        // 4. For the top contributors, check for a Spotify image or an album cover

        //TEST USAGE
        System.out.println("KENNY BEATS IMG" + getContributorImageUrl("Kenny Beats", appUser));

        //String numOneImageUrl = getContributorImageUrl(sortedContributors.get(0).getKey().getName(), appUser);
        //String numTwoImageUrl = getContributorImageUrl(sortedContributors.get(1).getKey().getName(), appUser);
        //String numThreeImageUrl = getContributorImageUrl(sortedContributors.get(2).getKey().getName(), appUser);
        //String numFourImageUrl = getContributorImageUrl(sortedContributors.get(3).getKey().getName(), appUser);
        //String numFiveImageUrl = getContributorImageUrl(sortedContributors.get(4).getKey().getName(), appUser);

        // 5. Get 2 other album covers for top songs by the top contributor
        //List<String> additionalAlbumCovers = getAdditionalAlbumCovers(sortedContributors.get(0).getKey(), songs);

        // List<String> additionalAlbumCovers =
        // getAdditionalAlbumCovers(sortedContributors.get(0).getKey(), songs);

        // 6. Get top under 1k, 10k, 100k followers artists
        System.out.println("start date: " + startDate);
        System.out.println("end date: " + endDate);

        SpotifyTimeRange closestTimeRange;
        if (timeRange == null) {
            LocalDate diff = endDate.minusDays(startDate.toEpochDay());
            //CASE SWITCH TO FIND CLOSEST TIME RANGE TO DIFF
            if (diff.toEpochDay() < 60) {
                closestTimeRange = SpotifyTimeRange.SHORT_TERM;
            } else if (diff.toEpochDay() < 235) {
                closestTimeRange = SpotifyTimeRange.MEDIUM_TERM;
            } else {
                closestTimeRange = SpotifyTimeRange.LONG_TERM;
            }
        } else {
            closestTimeRange = timeRange;
        }
        SpotifyAPIResponse<JSONObject> topArtistsFromTimeRange = spotifyAPIWrapperService.getCurrentUserTopArtists(
            appUser,
            closestTimeRange
        );
        String topArtistUnder1kFollowersID = null;
        String topArtistUnder10kFollowersID = null;
        String topArtistUnder100kFollowersID = null;
        JSONArray items = topArtistsFromTimeRange.getData().getJSONArray("items");
        for (int i = 0; i < items.length(); i++) {
            JSONObject artist = items.getJSONObject(i);
            JSONObject followers = artist.getJSONObject("followers");
            int followerCount = followers.getInt("total");
            String artistId = artist.getString("id");

            if (topArtistUnder1kFollowersID == null && followerCount < 1000) {
                topArtistUnder1kFollowersID = artistId;
            }
            if (topArtistUnder10kFollowersID == null && followerCount < 10000) {
                topArtistUnder10kFollowersID = artistId;
            }
            if (topArtistUnder100kFollowersID == null && followerCount < 100000) {
                topArtistUnder100kFollowersID = artistId;
            }
            if (topArtistUnder1kFollowersID != null && topArtistUnder10kFollowersID != null && topArtistUnder100kFollowersID != null) {
                break;
            }
        }
        /* THIS WILL ONLY WORK WHEN GETTING THE TOP ARTISTS ADDS TO THE DATABASE
        MainArtist topUnder1k = mainArtistRepository.findArtistBySpotifyId(topArtistUnder1kFollowersID);
        MainArtist topUnder10k = mainArtistRepository.findArtistBySpotifyId(topArtistUnder10kFollowersID);
        MainArtist topUnder100k = mainArtistRepository.findArtistBySpotifyId(topArtistUnder100kFollowersID);
        dto.setTopUnder1kName(topUnder1k.getArtistName());
        dto.setTopUnder10kName(topUnder10k.getArtistName());
        dto.setTopUnder100kName(topUnder100k.getArtistName());
        dto.setTopUnder1kImage(topUnder1k.getArtistImageLarge());
        dto.setTopUnder10kImage(topUnder10k.getArtistImageLarge());
        dto.setTopUnder100kImage(topUnder100k.getArtistImageLarge());
        */

        // Construct the DTO with the gathered information
        // constructRecappedDTO(dto, sortedContributors, imageUrl,
        // additionalAlbumCovers);

        System.out.println("duration: " + duration);
        System.out.println("mainArtists: " + mainArtists.size());

        dto.setTotalContributors(100);

        dto.setTopUnder1kName("1kartist");
        dto.setTopUnder1kImage("https://i.scdn.co/image/ab6761610000e5ebae4a51ded0c9a8b75278f5eb");
        dto.setTopUnder10kName("10kartist");
        dto.setTopUnder10kImage("https://i.scdn.co/image/ab6761610000e5ebae4a51ded0c9a8b75278f5eb");
        dto.setTopUnder100kName("100kartist");
        dto.setTopUnder100kImage("https://i.scdn.co/image/ab67616100005174069ff978752054a7e015daab");

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

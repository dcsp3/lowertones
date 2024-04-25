package team.bham.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
        System.out.println("songIds: " + songIds);
        List<Contributor> allContributors = contributorRepository.findContributorsBySongIds(songIds);
        List<Contributor> filteredContributorsByRole = allContributors
            .stream()
            .filter(c -> c.getRole() != null && (c.getRole().equalsIgnoreCase(role) || c.getRole().contains(role)))
            .collect(Collectors.toList());
        Map<String, List<Contributor>> groupedById = filteredContributorsByRole
            .stream()
            .collect(Collectors.groupingBy(Contributor::getMusicbrainzID));
        Map<Contributor, Long> contributorCountMap = new HashMap<>();
        for (Map.Entry<String, List<Contributor>> entry : groupedById.entrySet()) {
            List<Contributor> contributors = entry.getValue();
            Contributor representative = contributors.get(0); // pick the first contributor as representative
            long count = contributors.size();
            contributorCountMap.put(representative, count);
        }
        return contributorCountMap;
    }

    @Transactional
    private String getContributorImageUrl(String name, AppUser user) {
        JSONObject response = spotifyAPIWrapperService.search(name, SpotifySearchType.ARTIST, user);
        System.out.println("response: " + response);
        Integer thresholdRatio = 95;
        Integer thresholdPartialRatio = 90;
        JSONArray artists = response.getJSONObject("artists").getJSONArray("items");
        List<JSONObject> exactMatches = new ArrayList<>();
        List<JSONObject> partialMatches = new ArrayList<>();

        for (int i = 0; i < artists.length(); i++) {
            JSONObject artist = artists.getJSONObject(i);
            System.out.println("artistJSON: " + artist);
            String artistName = artist.getString("name");
            System.out.println("artistName: " + artistName);
            int fullMatchRatio = FuzzySearch.ratio(name, artistName);
            int partialMatchRatio = FuzzySearch.partialRatio(name, artistName);

            if (fullMatchRatio > thresholdRatio) {
                exactMatches.add(artist);
            } else if (partialMatchRatio > thresholdPartialRatio) {
                partialMatches.add(artist);
            }
        }

        JSONObject bestExactMatch = exactMatches
            .stream()
            .max(Comparator.comparingInt(artist -> artist.getJSONObject("followers").getInt("total")))
            .orElse(null);
        if (bestExactMatch != null) {
            JSONArray images = bestExactMatch.getJSONArray("images");
            if (images.length() > 0) {
                return images.getJSONObject(0).getString("url");
            }
        }

        JSONObject bestPartialMatch = partialMatches
            .stream()
            .max(Comparator.comparingInt(artist -> artist.getJSONObject("followers").getInt("total")))
            .orElse(null);
        if (bestPartialMatch != null) {
            JSONArray images = bestPartialMatch.getJSONArray("images");
            if (images.length() > 0) {
                return images.getJSONObject(0).getString("url");
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
        long duration = 0;
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
                    System.out.println("timeframe is here:" + request.getTimeframe());
                    System.err.println("Unknown timeframe format");
                }
                break;
        }
        System.out.println("scantype is here:" + request.getScanType());

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
            System.out.println("current duration is: " + duration);
            System.out.println("current i is: " + i);
            duration = duration + songs.get(i).getSongDuration();
        }
        duration = duration / 60000;
        dto.setTotalDuration(((int) duration));

        // 3. Get Contributors for each song, count the number of occurrences, and save the top 5
        Map<Contributor, Long> topContributors = countContributorsByRoleAndSongs(request.getMusicianType().name(), songs);
        dto.setTotalContributors(topContributors.size());

        System.out.println("topContributors here: " + topContributors);

        // Sort the map by value and pick the top 5 contributors
        Map<Contributor, Long> top5Contributors = topContributors
            .entrySet()
            .stream()
            .sorted(Map.Entry.<Contributor, Long>comparingByValue().reversed())
            .limit(5)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        System.out.println("top5Contributors here: " + top5Contributors);

        int count = 1;
        String[] names = new String[5];
        int[] numSongs = new int[5];
        for (Map.Entry<Contributor, Long> entry : top5Contributors.entrySet()) {
            Contributor contributor = entry.getKey();
            String name = contributor.getName();
            names[count - 1] = name;
            numSongs[count - 1] = entry.getValue().intValue();
            count++;
        }
        dto.setNumOneArtistName(names[0]);
        dto.setNumTwoArtistName(names[1]);
        dto.setNumThreeArtistName(names[2]);
        dto.setNumFourArtistName(names[3]);
        dto.setNumFiveArtistName(names[4]);
        dto.setNumOneAristNumSongs(numSongs[0]);
        dto.setNumTwoAristNumSongs(numSongs[1]);
        dto.setNumThreeAristNumSongs(numSongs[2]);
        dto.setNumFourAristNumSongs(numSongs[3]);
        dto.setNumFiveAristNumSongs(numSongs[4]);

        // 4. For the top contributors, check for a Spotify image or an album cover

        String[] images = new String[5];
        System.out.println("names here: " + names[0] + ", " + names[1] + ", " + names[2] + ", " + names[3] + ", " + names[4]);
        for (int i = 0; i < 5; i++) {
            images[i] = getContributorImageUrl(names[i], appUser);
        }

        for (int i = 0; i < 5; i++) {
            if (images[i] == null) {
                //TO DO - SET IMAGE TO TOP ALBUM COVER
                images[i] = "https://i.scdn.co/image/ab6761610000e5ebae4a51ded0c9a8b75278f5eb";
            }
        }
        dto.setNumOneArtistImage(images[0]);
        dto.setNumTwoArtistImage(images[1]);
        dto.setNumThreeArtistImage(images[2]);
        dto.setNumFourArtistImage(images[3]);
        dto.setNumFiveArtistImage(images[4]);

        System.out.println("images here: " + images[0] + images[1] + images[2] + images[3] + images[4]);
        System.out.println("names here: " + names[0] + names[1] + names[2] + names[3] + names[4]);

        //TEST USAGE
        System.out.println("KENNY BEATS IMG" + getContributorImageUrl("Kenny Beats", appUser));

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

        List<MainArtist> topArtists = utilService.getUserTopArtists(appUser, closestTimeRange);

        String topArtistUnder1kFollowersID = null;
        String topArtistUnder10kFollowersID = null;
        String topArtistUnder100kFollowersID = null;
        if (topArtists != null) {
            for (int i = 0; i < topArtists.size(); i++) {
                if (topArtists.get(i) == null) {
                    System.out.println("topArtists.get(i) is null for artist: " + topArtists.get(i).getArtistName());
                    continue;
                }
                if (topArtists.get(i).getArtistFollowers() == null) {
                    System.out.println("topArtists.get(i).getArtistFollowers() is null for artist: " + topArtists.get(i).getArtistName());
                    continue;
                }
                int followerCount = topArtists.get(i).getArtistFollowers();
                String artistId = topArtists.get(i).getArtistSpotifyID();

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
            MainArtist topUnder1k = mainArtistRepository.findArtistBySpotifyId(topArtistUnder1kFollowersID);
            MainArtist topUnder10k = mainArtistRepository.findArtistBySpotifyId(topArtistUnder10kFollowersID);
            MainArtist topUnder100k = mainArtistRepository.findArtistBySpotifyId(topArtistUnder100kFollowersID);
            if (topUnder1k != null) {
                dto.setTopUnder1kName(topUnder1k.getArtistName());
                dto.setTopUnder1kImage(topUnder1k.getArtistImageLarge());
            }
            if (topUnder10k != null) {
                dto.setTopUnder10kName(topUnder10k.getArtistName());
                dto.setTopUnder10kImage(topUnder10k.getArtistImageLarge());
            }
            if (topUnder100k != null) {
                dto.setTopUnder100kName(topUnder100k.getArtistName());
                dto.setTopUnder100kImage(topUnder100k.getArtistImageLarge());
            }
        }

        System.out.println("duration: " + duration);
        System.out.println("mainArtists: " + mainArtists.size());

        //TEST DATA

        /*
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
        */

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

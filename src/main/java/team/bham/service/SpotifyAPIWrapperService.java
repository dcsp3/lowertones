package team.bham.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import team.bham.domain.AppUser;
import team.bham.repository.AppUserRepository;
import team.bham.service.APIWrapper.*;
import team.bham.service.APIWrapper.Enums.*;

@Service
@Transactional
public class SpotifyAPIWrapperService {

    private final Logger log = LoggerFactory.getLogger(SpotifyAPIWrapperService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${spotify.client.id}")
    private String clientId;

    @Value("${spotify.client.secret}")
    private String clientSecret;

    private AppUserRepository appUserRepository;

    public SpotifyAPIWrapperService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public SpotifyAPIResponse<SpotifyUser> getUserDetails(AppUser user) {
        String endpoint = "https://api.spotify.com/v1/me";
        SpotifyAPIResponse<SpotifyUser> res = new SpotifyAPIResponse<>();
        SpotifyAPIResponse<JSONObject> userDataResponse = APICall(HttpMethod.GET, endpoint, user);
        if (!userDataResponse.getSuccess()) {
            res.setSuccess(false);
            res.setStatus(userDataResponse.getStatus());
            return res;
        }
        JSONObject userData = userDataResponse.getData();
        SpotifyUser spotifyUser = new SpotifyUser();
        spotifyUser.setDisplayName(userData.getString("display_name"));
        spotifyUser.setEmail(userData.getString("email"));
        spotifyUser.setSpotifyId(userData.getString("id"));

        JSONArray images = userData.getJSONArray("images");
        for (int i = 0; i < images.length(); i++) {
            JSONObject imageJSON = images.getJSONObject(i);
            SpotifyImage image = new SpotifyImage();
            image.setUrl(imageJSON.getString("url"));
            image.setWidth(imageJSON.getInt("width"));
            image.setHeight(imageJSON.getInt("height"));
            spotifyUser.addImage(image);
        }

        res.setSuccess(true);
        res.setStatus(userDataResponse.getStatus());
        res.setData(spotifyUser);
        return res;
    }

    public JSONObject search(String query, SpotifySearchType type, AppUser user) {
        String endpoint = "https://api.spotify.com/v1/search?q=" + query + "&type=" + type.label;
        JSONObject response = APICall(HttpMethod.GET, endpoint, user).getData();
        return response;
    }

    public SpotifyAPIResponse<ArrayList<SpotifySimplifiedPlaylist>> getCurrentUserPlaylists(AppUser user) {
        String endpoint = "https://api.spotify.com/v1/me/playlists?limit=50";
        JSONObject response = APICall(HttpMethod.GET, endpoint, user).getData();
        JSONArray playlistEntriesJSON = response.getJSONArray("items");
        ArrayList<SpotifySimplifiedPlaylist> playlists = new ArrayList<SpotifySimplifiedPlaylist>();
        int totalPlaylists = response.getInt("total");
        for (int i = 0; i < totalPlaylists; i += 50) {
            for (int j = 0; j < playlistEntriesJSON.length(); j++) {
                JSONObject playlistEntry = playlistEntriesJSON.getJSONObject(j);
                playlists.add(
                    new SpotifySimplifiedPlaylist(
                        playlistEntry.getString("name"),
                        playlistEntry.getString("id"),
                        playlistEntry.getString("snapshot_id")
                    )
                );
            }
            if (!response.isNull("next")) {
                String nextPageURL = response.getString("next");
                response = APICall(HttpMethod.GET, nextPageURL, user).getData();
                playlistEntriesJSON = response.getJSONArray("items");
            }
        }

        SpotifyAPIResponse<ArrayList<SpotifySimplifiedPlaylist>> res = new SpotifyAPIResponse<>();
        res.setSuccess(true);
        res.setData(playlists);
        return res;
    }

    public SpotifyAPIResponse<JSONObject> getPlaylistTracks(AppUser user, String playlistId) {
        String endpoint = "https://api.spotify.com/v1/playlists/" + playlistId + "/tracks";
        return APICall(HttpMethod.GET, endpoint, user);
    }

    public SpotifyAPIResponse<SpotifyPlaylist> getPlaylistDetails(AppUser user, String playlistId) {
        String endpoint = "https://api.spotify.com/v1/playlists/" + playlistId;

        JSONObject playlistJSON = APICall(HttpMethod.GET, endpoint, user).getData();
        SpotifyPlaylist playlist = new SpotifyPlaylist();
        playlist.setPlaylistId(playlistJSON.getString("id"));
        playlist.setSnapshotId(playlistJSON.getString("snapshot_id"));
        playlist.setName(playlistJSON.getString("name"));

        JSONObject trackInfo = playlistJSON.getJSONObject("tracks");

        //String nextPageURL = trackInfo.isNull("next") ? null : trackInfo.getString("next");

        ArrayList<String> trackIds = new ArrayList<>();

        Boolean getNextPage = true;
        while (getNextPage) {
            JSONArray tracks = trackInfo.getJSONArray("items");

            for (int i = 0; i < tracks.length(); i++) {
                JSONObject trackJSON = ((JSONObject) tracks.get(i)).getJSONObject("track");
                //todo: handle episodes separately
                if (trackJSON.getString("type").equals("track")) {
                    SpotifyTrack track = genTrackFromJSON(trackJSON);
                    playlist.addTrack(track);
                    trackIds.add(track.getId());
                }
            }

            getNextPage = !trackInfo.isNull("next");
            if (getNextPage) {
                String nextPageURL = trackInfo.getString("next");
                trackInfo = APICall(HttpMethod.GET, nextPageURL, user).getData();
            }
        }

        //grab track audio features
        ArrayList<SpotifyTrackAudioFeatures> audioFeatures = getTrackAudioFeatures(trackIds, user).getData();
        for (int i = 0; i < audioFeatures.size(); i++) {
            playlist.getTracks().get(i).setAudioFeatures(audioFeatures.get(i));
        }

        SpotifyAPIResponse<SpotifyPlaylist> res = new SpotifyAPIResponse<>();
        res.setData(playlist);
        res.setSuccess(true);
        return res;
    }

    public SpotifyAPIResponse<ArrayList<SpotifyTrackAudioFeatures>> getTrackAudioFeatures(ArrayList<String> trackIds, AppUser user) {
        String endpoint = "https://api.spotify.com/v1/audio-features?ids=";

        //split into batches of 100 ids
        ArrayList<String> batches = new ArrayList<>();
        String curBatch = "";
        for (int i = 0; i < trackIds.size(); i++) {
            curBatch += trackIds.get(i);
            if (i > 0 && ((i % 100) == 99)) {
                batches.add(curBatch);
                curBatch = "";
            } else if (i != trackIds.size() - 1) {
                curBatch += ",";
            }
        }
        if (curBatch.length() > 0) {
            batches.add(curBatch);
        }
        ArrayList<SpotifyTrackAudioFeatures> audioFeaturesList = new ArrayList<>();
        for (int i = 0; i < batches.size(); i++) {
            String curEndpoint = endpoint + batches.get(i);
            JSONObject response = APICall(HttpMethod.GET, curEndpoint, user).getData();
            JSONArray audioFeaturesListJSON = response.getJSONArray("audio_features");

            for (int j = 0; j < audioFeaturesListJSON.length(); j++) {
                JSONObject audioFeaturesJSON = audioFeaturesListJSON.getJSONObject(j);
                SpotifyTrackAudioFeatures audioFeatures = new SpotifyTrackAudioFeatures();
                audioFeatures.setAcousticness(audioFeaturesJSON.getFloat("acousticness"));
                audioFeatures.setDanceability(audioFeaturesJSON.getFloat("danceability"));
                audioFeatures.setEnergy(audioFeaturesJSON.getFloat("energy"));
                audioFeatures.setInstrumentalness(audioFeaturesJSON.getFloat("instrumentalness"));
                audioFeatures.setKey(audioFeaturesJSON.getInt("key"));
                audioFeatures.setLiveness(audioFeaturesJSON.getFloat("liveness"));
                audioFeatures.setLoudness(audioFeaturesJSON.getFloat("loudness"));
                audioFeatures.setMode(audioFeaturesJSON.getInt("mode"));
                audioFeatures.setSpeechiness(audioFeaturesJSON.getFloat("speechiness"));
                audioFeatures.setTempo(audioFeaturesJSON.getFloat("tempo"));
                audioFeatures.setTimeSignature(audioFeaturesJSON.getInt("time_signature"));
                audioFeatures.setValence(audioFeaturesJSON.getFloat("valence"));
                audioFeaturesList.add(audioFeatures);
            }
        }

        SpotifyAPIResponse<ArrayList<SpotifyTrackAudioFeatures>> res = new SpotifyAPIResponse<>();
        res.setSuccess(true);
        res.setData(audioFeaturesList);
        return res;
    }

    public SpotifyAPIResponse<JSONObject> getCurrentUserTopTracks(AppUser user, SpotifyTimeRange timeRange) {
        String endpoint = "https://api.spotify.com/v1/me/top/tracks?offset=0&limit=50&time_range=" + timeRange.label;
        return APICall(HttpMethod.GET, endpoint, user);
    }

    public SpotifyAPIResponse<JSONObject> getCurrentUserTopArtists(AppUser user, SpotifyTimeRange timeRange) {
        String endpoint = "https://api.spotify.com/v1/me/top/artists?offset=0&limit=35&time_range=" + timeRange.label;
        return APICall(HttpMethod.GET, endpoint, user);
    }

    private SpotifyAPIResponse<JSONObject> APICall(HttpMethod method, String endpoint, AppUser user) {
        SpotifyAPIResponse<JSONObject> apiResponse = new SpotifyAPIResponse<>();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + user.getSpotifyAuthToken());
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange(endpoint, method, entity, String.class);
        } catch (HttpStatusCodeException e) {
            HttpStatus status = e.getStatusCode();

            if (status != HttpStatus.UNAUTHORIZED) {
                apiResponse.setSuccess(false);
                return apiResponse;
            }

            if (!refreshAccessToken(user)) {
                //refresh failed.. hmm
                apiResponse.setSuccess(false);
                return apiResponse;
            }
            headers.set("Authorization", "Bearer " + user.getSpotifyAuthToken());
            entity = new HttpEntity<>(headers);
            response = restTemplate.exchange(endpoint, method, entity, String.class);
        }

        apiResponse.setData(new JSONObject(response.getBody()));
        apiResponse.setSuccess(true);
        return apiResponse;
    }

    //todo: thoroughly test this
    private boolean refreshAccessToken(AppUser user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, clientSecret);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "refresh_token");
        requestBody.add("refresh_token", user.getSpotifyRefreshToken());

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                "https://accounts.spotify.com/api/token",
                HttpMethod.POST,
                requestEntity,
                String.class
            );
            JSONObject jsonData = new JSONObject(responseEntity.getBody());

            String accessToken = jsonData.getString("access_token");

            user.setSpotifyAuthToken(accessToken);
            appUserRepository.save(user);
            return true;
        } catch (HttpStatusCodeException e) {
            return false;
        }
    }

    private SpotifyTrack genTrackFromJSON(JSONObject trackJSON) {
        SpotifyTrack track = new SpotifyTrack();

        //misc track info (name,...)
        //todo: preview url, etc., check obj type
        track.setName(trackJSON.getString("name"));
        track.setDuration(trackJSON.getInt("duration_ms"));
        track.setExplicit(trackJSON.getBoolean("explicit"));
        track.setPopularity(trackJSON.getInt("popularity"));
        if (trackJSON.has("preview_url") && !trackJSON.isNull("preview_url")) track.setPreviewUrl(trackJSON.getString("preview_url"));
        track.setId(trackJSON.getString("id"));

        //album
        JSONObject albumJSON = trackJSON.getJSONObject("album");
        SpotifyAlbum album = new SpotifyAlbum();
        album.setAlbumType(albumJSON.getString("album_type"));
        album.setNumTracks(albumJSON.getInt("total_tracks"));
        album.setSpotifyId(albumJSON.getString("id"));
        album.setName(albumJSON.getString("name"));
        // album.setReleaseDate(albumJSON.getString("release_date"));
        String releaseDateUnformatted = albumJSON.getString("release_date");

        //hacky, but LocalDate expects month/day to be set.
        switch (albumJSON.getString("release_date_precision")) {
            case "year":
                album.setReleasePrecision(SpotifyReleasePrecision.YEAR);
                releaseDateUnformatted += "-01-01";
                break;
            case "month":
                album.setReleasePrecision(SpotifyReleasePrecision.MONTH);
                releaseDateUnformatted += "-01";
                break;
            case "day":
                album.setReleasePrecision(SpotifyReleasePrecision.DAY);
                break;
        }
        album.setReleaseDate(LocalDate.parse(releaseDateUnformatted, DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        JSONArray albumArt = albumJSON.getJSONArray("images");
        if (albumArt.length() > 0) {
            album.setCoverArtURL(albumArt.getJSONObject(0).getString("url"));
        } else {
            //hacky
            album.setCoverArtURL("");
        }
        //album.setCoverArtURL(albumJSON.getJSONArray("images").getJSONObject(0).getString("url"));

        track.setAlbum(album);

        //only care about main artist
        JSONObject mainArtistJSON = trackJSON.getJSONArray("artists").getJSONObject(0);
        SpotifyArtist mainArtist = new SpotifyArtist();
        mainArtist.setName(mainArtistJSON.getString("name"));
        mainArtist.setSpotifyId(mainArtistJSON.getString("id"));

        //popularity doesn't seem to exist for some artists... wtf?
        int artistPopularity = (mainArtistJSON.has("popularity") ? mainArtistJSON.getInt("popularity") : 0);
        mainArtist.setPopularity(artistPopularity);

        if (mainArtistJSON.has("images")) {
            JSONArray artistImages = mainArtistJSON.getJSONArray("images");
            for (int i = 0; i < artistImages.length(); i++) {
                JSONObject imageJSON = artistImages.getJSONObject(i);
                SpotifyImage image = new SpotifyImage();
                image.setUrl(imageJSON.getString("url"));
                image.setWidth(imageJSON.getInt("width"));
                image.setHeight(imageJSON.getInt("height"));
                mainArtist.addImage(image);
            }
        }

        track.setMainArtist(mainArtist);

        return track;
    }
}

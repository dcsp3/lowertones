package team.bham.service;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        SpotifyAPIResponse<JSONObject> response = APICall(HttpMethod.GET, endpoint, user);

        if (!response.getSuccess()) {
            return new SpotifyAPIResponse<ArrayList<SpotifySimplifiedPlaylist>>(false, HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
        JSONObject responseJSON = response.getData();
        System.out.println("PLALTIST RESPONSE: " + response.toString());
        JSONArray playlistEntriesJSON = responseJSON.getJSONArray("items");
        ArrayList<SpotifySimplifiedPlaylist> playlists = new ArrayList<SpotifySimplifiedPlaylist>();
        int totalPlaylists = responseJSON.getInt("total");
        for (int i = 0; i < totalPlaylists; i += 50) {
            for (int j = 0; j < playlistEntriesJSON.length(); j++) {
                JSONObject playlistEntry = playlistEntriesJSON.getJSONObject(j);
                SpotifySimplifiedPlaylist curPlaylist = new SpotifySimplifiedPlaylist();
                curPlaylist.setName(playlistEntry.getString("name"));
                curPlaylist.setSpotifyId(playlistEntry.getString("id"));
                curPlaylist.setSnapshotId(playlistEntry.getString("snapshot_id"));
                /*if (playlistEntry.has("images")) {
                    if (!playlistEntry.isNull("images")) {
                        JSONArray images = playlistEntry.getJSONArray("images");
                        for (int k = 0; k < images.length(); k++) {
                            JSONObject imageJSON = images.getJSONObject(k);
                            SpotifyImage image = new SpotifyImage();
                            if (imageJSON.has("width") && !imageJSON.isNull("width") && imageJSON.optInt("width", -1) != -1) {
                                image.setWidth(imageJSON.getInt("width"));
                            } else {
                                image.setWidth(0);
                            }
                            if (imageJSON.has("height") && !imageJSON.isNull("height") && imageJSON.optInt("height", -1) != -1) {
                                image.setHeight(imageJSON.getInt("height"));
                            } else {
                                image.setHeight(0);
                            }
                            image.setUrl(imageJSON.getString("url"));
                            curPlaylist.addImage(image);
                        }
                    }
                }*/
                playlists.add(curPlaylist);
            }
            if (!responseJSON.isNull("next")) {
                String nextPageURL = responseJSON.getString("next");
                response = APICall(HttpMethod.GET, nextPageURL, user);
                if (!response.getSuccess()) {
                    return new SpotifyAPIResponse<ArrayList<SpotifySimplifiedPlaylist>>(false, HttpStatus.INTERNAL_SERVER_ERROR, null);
                }
                responseJSON = response.getData();
                playlistEntriesJSON = responseJSON.getJSONArray("items");
            }
        }

        SpotifyAPIResponse<ArrayList<SpotifySimplifiedPlaylist>> res = new SpotifyAPIResponse<>();
        res.setSuccess(true);
        res.setData(playlists);
        return res;
    }

    public SpotifyAPIResponse<JSONObject> createPlaylist(AppUser user, String playlistName) {
        String endpoint = "https://api.spotify.com/v1/users/" + user.getSpotifyUserID() + "/playlists";

        JSONObject body = new JSONObject();
        body.put("name", playlistName);
        return APICall(HttpMethod.POST, endpoint, body, user);
    }

    public SpotifyAPIResponse<JSONObject> addItemsToPlaylist(AppUser user, String playlistId, ArrayList<String> playlistIds) {
        String endpoint = "https://api.spotify.com/v1/playlists/" + playlistId + "/tracks";

        JSONObject body = new JSONObject();
        JSONArray uris = new JSONArray();

        // uris.put("spotify:track:7sVbKoBdhXtYCEOO6qC1SN");
        // uris.put("spotify:track:7lsepdNR69a3PS9pWMNICd");
        // todo: these should be batched.
        for (int i = 0; i < playlistIds.size(); i++) {
            uris.put("spotify:track:" + playlistIds.get(i));
        }

        body.put("uris", uris);

        return APICall(HttpMethod.POST, endpoint, body, user);
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

        JSONArray images = playlistJSON.optJSONArray("images");
        playlist.setPlaylistImageSmall("");
        playlist.setPlaylistImageMedium("");
        playlist.setPlaylistImageLarge("");
        if (images != null && images.length() > 0) {
            playlist.setPlaylistImageLarge(images.length() > 0 ? images.getJSONObject(0).optString("url", "") : "");
            playlist.setPlaylistImageMedium(images.length() > 1 ? images.getJSONObject(1).optString("url", "") : "");
            playlist.setPlaylistImageSmall(images.length() > 2 ? images.getJSONObject(2).optString("url", "") : "");
        } else {
            // Log or handle the scenario where no images are available
            System.out.println("No images available for this playlist.");
        }

        JSONObject trackInfo = playlistJSON.getJSONObject("tracks");

        // String nextPageURL = trackInfo.isNull("next") ? null :
        // trackInfo.getString("next");

        ArrayList<String> trackIds = new ArrayList<>();
        ArrayList<String> artistIds = new ArrayList<>();

        Boolean getNextPage = true;
        while (getNextPage) {
            JSONArray tracks = new JSONArray();
            try {
                tracks = trackInfo.getJSONArray("items");
            } catch (Exception e) {
                // some playlists are incredibly cursed
                System.out.println("What the fuck is this playlist?? " + trackInfo.toString());
                SpotifyAPIResponse<SpotifyPlaylist> res = new SpotifyAPIResponse<>();
                res.setData(playlist);
                res.setSuccess(true);
                return res;
            }

            for (int i = 0; i < tracks.length(); i++) {
                JSONObject trackJSON = null;
                try {
                    trackJSON = ((JSONObject) tracks.get(i)).getJSONObject("track");
                    String addedDateString = ((JSONObject) tracks.get(i)).getString("added_at");
                    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                    LocalDate dateAdded = null;
                    try {
                        ZonedDateTime zonedDateTime = ZonedDateTime.parse(addedDateString, formatter);
                        dateAdded = zonedDateTime.toLocalDate();
                    } catch (DateTimeParseException e) {
                        System.err.println("Error parsing date: " + addedDateString);
                        e.printStackTrace();
                        continue;
                    }
                    if (trackJSON.getString("type").equals("track")) {
                        SpotifyTrack track = genTrackFromJSON(trackJSON);
                        track.setDateAdded(dateAdded);
                        playlist.addTrack(track);
                        trackIds.add(track.getId());
                        artistIds.add(track.getArtist().getSpotifyId());
                    }
                } catch (Exception e) {
                    continue;
                }
                // todo: handle episodes separately
            }

            getNextPage = !trackInfo.isNull("next");
            if (getNextPage) {
                String nextPageURL = trackInfo.getString("next");
                trackInfo = APICall(HttpMethod.GET, nextPageURL, user).getData();
            }
        }

        // grab track audio features
        SpotifyAPIResponse<ArrayList<SpotifyTrackAudioFeatures>> audioFeaturesResponse = getTrackAudioFeatures(trackIds, user);
        if (audioFeaturesResponse.getSuccess()) {
            ArrayList<SpotifyTrackAudioFeatures> audioFeatures = audioFeaturesResponse.getData();
            for (int i = 0; i < audioFeatures.size(); i++) {
                playlist.getTracks().get(i).setAudioFeatures(audioFeatures.get(i));
            }
        }

        // TESTING STUFF, IGNORE

        // grab ext. artist info
        // SpotifyAPIResponse<ArrayList<SpotifyArtist>> artistInfoResponse =
        // getArtistInfo(artistIds, user);
        // if(artistInfoResponse.getSuccess()) {
        // ArrayList<SpotifyArtist> artistInfo = artistInfoResponse.getData();
        // for(int i = 0; i < artistInfo.size(); i++) {
        // playlist.getTracks().get(i).setMainArtist(artistInfo.get(i));
        // }
        // }
        //lol

        SpotifyAPIResponse<SpotifyPlaylist> res = new SpotifyAPIResponse<>();
        res.setData(playlist);
        res.setSuccess(true);
        return res;
    }

    public SpotifyAPIResponse<ArrayList<SpotifyTrack>> getTrackInfo(ArrayList<String> trackIds, AppUser user) {
        String endpoint = "https://api.spotify.com/v1/tracks/";
        ArrayList<String> batches = batchSpotifyIDs(trackIds, 50); //docs say 50, don't really trust that
        ArrayList<SpotifyTrack> tracks = new ArrayList<>();
        for (int i = 0; i < batches.size(); i++) {
            String curEndpoint = endpoint + batches.get(i);
            SpotifyAPIResponse<JSONObject> response = APICall(HttpMethod.GET, curEndpoint, user);
            //todo: apiresponse errors
            JSONObject responseJSON = response.getData();
            JSONArray tracksJSON = responseJSON.getJSONArray("tracks");
            for (int j = 0; j < tracksJSON.length(); j++) {
                JSONObject trackJSON = tracksJSON.getJSONObject(j);
                SpotifyTrack track = genTrackFromJSON(trackJSON);
                tracks.add(track);
            }
        }

        return new SpotifyAPIResponse<ArrayList<SpotifyTrack>>(true, HttpStatus.OK, tracks);
    }

    public SpotifyAPIResponse<ArrayList<SpotifyAlbum>> getAlbumInfo(ArrayList<String> albumIds, AppUser user) {
        String endpoint = "https://api.spotify.com/v1/albums?ids=";
        ArrayList<String> batches = batchSpotifyIDs(albumIds, 20);
        ArrayList<SpotifyAlbum> albums = new ArrayList<>();
        for (int i = 0; i < batches.size(); i++) {
            String curEndpoint = endpoint + batches.get(i);
            SpotifyAPIResponse<JSONObject> response = APICall(HttpMethod.GET, curEndpoint, user);
            // todo: propagate apiresponse errors down
            JSONObject responseJSON = response.getData();
            JSONArray albumsJSON = responseJSON.getJSONArray("albums");
            for (int j = 0; j < albumsJSON.length(); j++) {
                JSONObject albumJSON = albumsJSON.getJSONObject(j);
                SpotifyAlbum album = new SpotifyAlbum();
                album.setAlbumType(albumJSON.getString("album_type"));
                album.setSpotifyId(albumJSON.getString("id"));
                album.setName(albumJSON.getString("name"));
                album.setNumTracks(albumJSON.getInt("total_tracks"));
                album.setPopularity(albumJSON.optInt("popularity", 0));
                String releaseDateUnformatted = albumJSON.getString("release_date");
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
                try {
                    album.setReleaseDate(LocalDate.parse(releaseDateUnformatted, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                } catch (Exception e) {
                    // fuck
                    album.setReleaseDate(LocalDate.now());
                }
                JSONArray albumArt = albumJSON.getJSONArray("images");
                if (albumArt.length() > 0) {
                    album.setCoverArtURL(albumArt.getJSONObject(0).getString("url"));
                } else {
                    // hacky
                    album.setCoverArtURL("");
                }

                albums.add(album);
            }
        }

        return new SpotifyAPIResponse<ArrayList<SpotifyAlbum>>(true, HttpStatus.OK, albums);
    }

    public SpotifyAPIResponse<ArrayList<SpotifyArtist>> getArtistInfo(ArrayList<String> artistIds, AppUser user) {
        String endpoint = "https://api.spotify.com/v1/artists?ids=";
        ArrayList<String> batches = batchSpotifyIDs(artistIds, 50);
        ArrayList<SpotifyArtist> artists = new ArrayList<>();
        for (int i = 0; i < batches.size(); i++) {
            String curEndpoint = endpoint + batches.get(i);
            SpotifyAPIResponse<JSONObject> response = APICall(HttpMethod.GET, curEndpoint, user);
            JSONObject responseJSON = response.getData();

            JSONArray artistsJSON = responseJSON.getJSONArray("artists");
            for (int j = 0; j < artistsJSON.length(); j++) {
                JSONObject artistJSON = artistsJSON.getJSONObject(j);
                SpotifyArtist artist = new SpotifyArtist();
                artist.setName(artistJSON.getString("name"));
                artist.setSpotifyId(artistJSON.getString("id"));
                artist.setPopularity(artistJSON.optInt("popularity", 0));
                if (artistJSON.has("images")) {
                    JSONArray artistImages = artistJSON.getJSONArray("images");
                    for (int k = 0; k < artistImages.length(); k++) {
                        JSONObject imageJSON = artistImages.getJSONObject(k);
                        SpotifyImage image = new SpotifyImage();
                        image.setUrl(imageJSON.getString("url"));
                        image.setWidth(imageJSON.getInt("width"));
                        image.setHeight(imageJSON.getInt("height"));
                        artist.addImage(image);
                    }
                }

                artists.add(artist);
            }
        }

        return new SpotifyAPIResponse<ArrayList<SpotifyArtist>>(true, HttpStatus.OK, artists);
    }

    public SpotifyAPIResponse<ArrayList<SpotifyTrackAudioFeatures>> getTrackAudioFeatures(ArrayList<String> trackIds, AppUser user) {
        String endpoint = "https://api.spotify.com/v1/audio-features?ids=";

        // split into batches of 100 ids
        ArrayList<String> batches = batchSpotifyIDs(trackIds, 100);
        ArrayList<SpotifyTrackAudioFeatures> audioFeaturesList = new ArrayList<>();
        for (int i = 0; i < batches.size(); i++) {
            String curEndpoint = endpoint + batches.get(i);
            SpotifyAPIResponse<JSONObject> response = APICall(HttpMethod.GET, curEndpoint, user);
            // audio features seems kinda broken. random 429s
            if (!response.getSuccess()) {
                return new SpotifyAPIResponse<ArrayList<SpotifyTrackAudioFeatures>>(false, HttpStatus.INTERNAL_SERVER_ERROR, null);
            }
            JSONObject responseData = response.getData();
            JSONArray audioFeaturesListJSON = null;
            audioFeaturesListJSON = responseData.getJSONArray("audio_features");

            for (int j = 0; j < audioFeaturesListJSON.length(); j++) {
                try {
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
                } catch (Exception e) {
                    audioFeaturesList.add(null);
                }
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
        for (int i = 0; i < 2; i++) {
            try {
                response = restTemplate.exchange(endpoint, method, entity, String.class);
                apiResponse.setData(new JSONObject(response.getBody()));
                apiResponse.setSuccess(true);
                return apiResponse;
            } catch (HttpStatusCodeException e) {
                HttpStatus status = e.getStatusCode();
                if (status == HttpStatus.TOO_MANY_REQUESTS) {
                    try {
                        Thread.sleep((long) (Math.pow(2, i) * 1000));
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    continue;
                } else if (status == HttpStatus.UNAUTHORIZED) {
                    if (!refreshAccessToken(user)) {
                        // wtf?
                        apiResponse.setSuccess(false);
                        return apiResponse;
                    } else {
                        // set new access token in request headers
                        headers = new HttpHeaders();
                        headers.set("Authorization", "Bearer " + user.getSpotifyAuthToken());
                        entity = new HttpEntity<>(headers);
                    }
                } else {
                    System.out.println("uhhh" + entity.getHeaders().toString() + " " + endpoint);
                    throw new RuntimeException("bruh: " + e.getStatusCode() + " " + e.getResponseBodyAsString());
                    // apiResponse.setSuccess(false);
                    // apiResponse.setStatus(status);
                    // return apiResponse;
                }
            }
        }

        // failed: timeout
        apiResponse.setSuccess(false);
        return apiResponse;
    }

    // fml... so much code duplication
    private SpotifyAPIResponse<JSONObject> APICall(HttpMethod method, String endpoint, JSONObject body, AppUser user) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + user.getSpotifyAuthToken());
        SpotifyAPIResponse<JSONObject> apiResponse = new SpotifyAPIResponse<>();

        HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange(endpoint, method, entity, String.class);
            apiResponse.setData(new JSONObject(response.getBody()));
            apiResponse.setSuccess(true);
            return apiResponse;
        } catch (HttpStatusCodeException e) {
            HttpStatus status = e.getStatusCode();
            // todo: all this (refresh etc.... lazy)
            throw new RuntimeException("bruh: " + status + " " + e.getResponseBodyAsString());
        }
    }

    // todo: thoroughly test this
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

        // misc track info (name,...)
        // todo: preview url, etc., check obj type
        track.setName(trackJSON.getString("name"));
        track.setDuration(trackJSON.getInt("duration_ms"));
        track.setExplicit(trackJSON.getBoolean("explicit"));
        track.setPopularity(trackJSON.getInt("popularity"));
        if (trackJSON.has("preview_url") && !trackJSON.isNull("preview_url")) track.setPreviewUrl(trackJSON.getString("preview_url"));
        track.setId(trackJSON.getString("id"));

        // album
        JSONObject albumJSON = trackJSON.getJSONObject("album");
        SpotifyAlbum album = new SpotifyAlbum();
        album.setAlbumType(albumJSON.getString("album_type"));
        album.setNumTracks(albumJSON.getInt("total_tracks"));
        album.setSpotifyId(albumJSON.getString("id"));
        album.setName(albumJSON.getString("name"));
        album.setPopularity(0);
        // album.setReleaseDate(albumJSON.getString("release_date"));
        String releaseDateUnformatted = albumJSON.getString("release_date");

        // hacky, but LocalDate expects month/day to be set.
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
        try {
            album.setReleaseDate(LocalDate.parse(releaseDateUnformatted, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        } catch (Exception e) {
            // fuck
            album.setReleaseDate(LocalDate.now());
        }
        JSONArray albumArt = albumJSON.getJSONArray("images");
        if (albumArt.length() > 0) {
            album.setCoverArtURL(albumArt.getJSONObject(0).getString("url"));
        } else {
            // hacky
            album.setCoverArtURL("");
        }
        // album.setCoverArtURL(albumJSON.getJSONArray("images").getJSONObject(0).getString("url"));

        track.setAlbum(album);
        //SpotifyTrackAudioFeatures defaultFeatures = new SpotifyTrackAudioFeatures();
        //track.setAudioFeatures(defaultFeatures);

        // only care about main artist
        JSONObject mainArtistJSON = trackJSON.getJSONArray("artists").getJSONObject(0);
        SpotifyArtist mainArtist = new SpotifyArtist();
        mainArtist.setName(mainArtistJSON.getString("name"));
        mainArtist.setSpotifyId(mainArtistJSON.getString("id"));

        // popularity doesn't seem to exist for some artists... wtf?
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

    private ArrayList<String> batchSpotifyIDs(ArrayList<String> ids, int maxPerBatch) {
        ArrayList<String> batches = new ArrayList<>();
        String curBatch = "";
        for (int i = 0; i < ids.size(); i++) {
            curBatch += ids.get(i);
            if (i > 0 && ((i % maxPerBatch) == maxPerBatch - 1)) {
                batches.add(curBatch);
                curBatch = "";
            } else if (i != ids.size() - 1) {
                curBatch += ",";
            }
        }
        if (curBatch.length() > 0) {
            batches.add(curBatch);
        }
        return batches;
    }
}

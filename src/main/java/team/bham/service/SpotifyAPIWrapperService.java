package team.bham.service;

import java.util.List;
import java.util.Optional;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import team.bham.domain.AppUser;
import team.bham.repository.AppUserRepository;

@Service
@Transactional
public class SpotifyAPIWrapperService {

    @Autowired
    private RestTemplate restTemplate;

    private AppUserRepository appUserRepository;

    public SpotifyAPIWrapperService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public JSONObject getUserDetails(AppUser user) {
        String endpoint = "https://api.spotify.com/v1/me";
        return APICall(HttpMethod.GET, endpoint, user);
    }

    public JSONObject getCurrentUserPlaylists(AppUser user) {
        String endpoint = "https://api.spotify.com/v1/me/playlists?limit=1&offset=0";
        return APICall(HttpMethod.GET, endpoint, user);
    }

    public JSONObject getPlaylistTracks(AppUser user, String playlistId) {
        String endpoint = "https://api.spotify.com/v1/playlists/" + playlistId + "/tracks";
        return APICall(HttpMethod.GET, endpoint, user);
    }

    public JSONObject getCurrentUserShortTermTopArtists(AppUser user) {
        String endpoint = "https://api.spotify.com/v1/me/top/artists?offset=0&limit=15&time_range=short_term";
        return APICall(HttpMethod.GET, endpoint, user);
    }

    public JSONObject getCurrentUserMediumTermTopArtists(AppUser user) {
        String endpoint = "https://api.spotify.com/v1/me/top/artists?offset=0&limit=15&time_range=medium_term";
        return APICall(HttpMethod.GET, endpoint, user);
    }

    public JSONObject getCurrentUserLongTermTopArtists(AppUser user) {
        String endpoint = "https://api.spotify.com/v1/me/top/artists?offset=0&limit=15&time_range=long_term";
        return APICall(HttpMethod.GET, endpoint, user);
    }

    //overload for calls with extra params
    //todo: handle errors, token expiry etc.
    private JSONObject APICall(HttpMethod method, String endpoint, AppUser user) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + user.getSpotifyAuthToken());
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(endpoint, method, entity, String.class);
        HttpStatus status = response.getStatusCode();

        //gnarly..
        while (status != HttpStatus.OK) {
            //something blew up, whoops
            if (status != HttpStatus.UNAUTHORIZED) return null;

            //HTTP 401 is more than likely a bad access token.
            //update cur user's access token and retry call
            refreshAccessToken(user);

            headers.set("Authorization", "Bearer " + user.getSpotifyAuthToken());
            entity = new HttpEntity<>(headers);
            response = restTemplate.exchange(endpoint, method, entity, String.class);
            status = response.getStatusCode();
        }
        return new JSONObject(response.getBody());
    }

    //header might need client id + secret??
    //todo: thoroughly test this
    private void refreshAccessToken(AppUser user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "refresh_token");
        requestBody.add("refresh_token", user.getSpotifyRefreshToken());

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
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
    }
}

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
import team.bham.service.APIWrapper.SpotifyAPIResponse;

@Service
@Transactional
public class SpotifyAPIWrapperService {

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

    public SpotifyAPIResponse getUserDetails(AppUser user) {
        String endpoint = "https://api.spotify.com/v1/me";
        return APICall(HttpMethod.GET, endpoint, user);
    }

    public SpotifyAPIResponse getCurrentUserPlaylists(AppUser user) {
        String endpoint = "https://api.spotify.com/v1/me/playlists?limit=1&offset=0";
        return APICall(HttpMethod.GET, endpoint, user);
    }

    public SpotifyAPIResponse getPlaylistTracks(AppUser user, String playlistId) {
        String endpoint = "https://api.spotify.com/v1/playlists/" + playlistId + "/tracks";
        return APICall(HttpMethod.GET, endpoint, user);
    }

    public SpotifyAPIResponse getCurrentUserShortTermTopArtists(AppUser user) {
        String endpoint = "https://api.spotify.com/v1/me/top/artists?offset=0&limit=35&time_range=short_term";
        return APICall(HttpMethod.GET, endpoint, user);
    }

    public SpotifyAPIResponse getCurrentUserMediumTermTopArtists(AppUser user) {
        String endpoint = "https://api.spotify.com/v1/me/top/artists?offset=0&limit=35&time_range=medium_term";
        return APICall(HttpMethod.GET, endpoint, user);
    }

    public SpotifyAPIResponse getCurrentUserLongTermTopArtists(AppUser user) {
        String endpoint = "https://api.spotify.com/v1/me/top/artists?offset=0&limit=35&time_range=long_term";
        return APICall(HttpMethod.GET, endpoint, user);
    }

    private SpotifyAPIResponse APICall(HttpMethod method, String endpoint, AppUser user) {
        SpotifyAPIResponse apiResponse = new SpotifyAPIResponse();
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
            //refresh failed..
            if (!refreshAccessToken(user)) {
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
}

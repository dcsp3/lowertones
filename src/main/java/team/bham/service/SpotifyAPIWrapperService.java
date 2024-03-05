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

@Service
@Transactional
public class SpotifyAPIWrapperService {

    @Autowired
    private RestTemplate restTemplate;

    public SpotifyAPIWrapperService() {}

    public JSONObject getUserDetails(AppUser user) {
        String accessToken = user.getSpotifyAuthToken();
        String endpoint = "https://api.spotify.com/v1/me";
        return APICall(HttpMethod.GET, endpoint, accessToken);
    }

    public JSONObject getCurrentUserPlaylists(AppUser user) {
        String accessToken = user.getSpotifyAuthToken();
        String endpoint = "https://api.spotify.com/v1/me/playlists?limit=1&offset=0";
        return APICall(HttpMethod.GET, endpoint, accessToken);
    }

    public JSONObject getPlaylistTracks(AppUser user, String playlistId) {
        String accessToken = user.getSpotifyAuthToken();
        String endpoint = "https://api.spotify.com/v1/playlists/" + playlistId + "/tracks";
        return APICall(HttpMethod.GET, endpoint, accessToken);
    }

    //overload for calls with extra params
    //todo: handle errors, token expiry etc.
    private JSONObject APICall(HttpMethod method, String endpoint, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(endpoint, method, entity, String.class);
        return new JSONObject(response.getBody());
    }
}

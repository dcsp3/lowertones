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
import team.bham.domain.SpotifyExchangeCode;
import team.bham.repository.SpotifyExchangeCodeRepository;

/**
 * Service Implementation for managing {@link SpotifyExchangeCode}.
 */
@Service
@Transactional
public class SpotifyExchangeCodeService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${spotify.client.id}")
    private String clientId;

    @Value("${spotify.client.secret}")
    private String clientSecret;

    private final Logger log = LoggerFactory.getLogger(SpotifyExchangeCodeService.class);

    private final SpotifyExchangeCodeRepository spotifyExchangeCodeRepository;

    public SpotifyExchangeCodeService(SpotifyExchangeCodeRepository spotifyExchangeCodeRepository) {
        this.spotifyExchangeCodeRepository = spotifyExchangeCodeRepository;
    }

    /**
     * Save a spotifyExchangeCode.
     *
     * @param spotifyExchangeCode the entity to save.
     * @return the persisted entity.
     */
    public SpotifyExchangeCode save(SpotifyExchangeCode spotifyExchangeCode) {
        log.debug("Request to save SpotifyExchangeCode : {}", spotifyExchangeCode);
        return spotifyExchangeCodeRepository.save(spotifyExchangeCode);
    }

    public String exchangeCodeForToken(String code) {
        String spotifyTokenUrl = "https://accounts.spotify.com/api/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, clientSecret);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("code", code);
        requestBody.add("redirect_uri", "https://team37.dev.bham.team/callback");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(spotifyTokenUrl, HttpMethod.POST, requestEntity, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // Parse the response and extract the access token
            JSONObject responseJson = new JSONObject(responseEntity.getBody());
            String accessToken = responseJson.getString("access_token");
            return accessToken;
        } else {
            // Handle error response from Spotify API
            return null;
        }
    }

    /**
     * Update a spotifyExchangeCode.
     *
     * @param spotifyExchangeCode the entity to save.
     * @return the persisted entity.
     */
    public SpotifyExchangeCode update(SpotifyExchangeCode spotifyExchangeCode) {
        log.debug("Request to update SpotifyExchangeCode : {}", spotifyExchangeCode);
        // no save call needed as we have no fields that can be updated
        return spotifyExchangeCode;
    }

    /**
     * Partially update a spotifyExchangeCode.
     *
     * @param spotifyExchangeCode the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SpotifyExchangeCode> partialUpdate(SpotifyExchangeCode spotifyExchangeCode) {
        log.debug("Request to partially update SpotifyExchangeCode : {}", spotifyExchangeCode);

        return spotifyExchangeCodeRepository
            .findById(spotifyExchangeCode.getId())
            .map(existingSpotifyExchangeCode -> {
                return existingSpotifyExchangeCode;
            }); // .map(spotifyExchangeCodeRepository::save)
    }

    /**
     * Get all the spotifyExchangeCodes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SpotifyExchangeCode> findAll() {
        log.debug("Request to get all SpotifyExchangeCodes");
        return spotifyExchangeCodeRepository.findAll();
    }

    /**
     * Get one spotifyExchangeCode by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SpotifyExchangeCode> findOne(Long id) {
        log.debug("Request to get SpotifyExchangeCode : {}", id);
        return spotifyExchangeCodeRepository.findById(id);
    }

    /**
     * Delete the spotifyExchangeCode by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SpotifyExchangeCode : {}", id);
        spotifyExchangeCodeRepository.deleteById(id);
    }
}

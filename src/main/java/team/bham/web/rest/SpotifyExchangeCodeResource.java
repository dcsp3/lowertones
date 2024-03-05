package team.bham.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import team.bham.domain.AppUser;
import team.bham.domain.SpotifyExchangeCode;
import team.bham.domain.User;
import team.bham.repository.AppUserRepository;
import team.bham.repository.SpotifyExchangeCodeRepository;
import team.bham.repository.UserRepository;
import team.bham.service.SpotifyExchangeCodeService;
import team.bham.service.SpotifyExchangeCodeService;
import team.bham.service.dto.SpotifyExchangeCodeDTO;
import team.bham.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link team.bham.domain.SpotifyExchangeCode}.
 */
@RestController
@RequestMapping("/api")
public class SpotifyExchangeCodeResource {

    private final Logger log = LoggerFactory.getLogger(SpotifyExchangeCodeResource.class);

    private static final String ENTITY_NAME = "spotifyExchangeCode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpotifyExchangeCodeService spotifyExchangeCodeService;

    private final SpotifyExchangeCodeRepository spotifyExchangeCodeRepository;

    private final UserRepository userRepository;

    private final AppUserRepository appUserRepository;

    public SpotifyExchangeCodeResource(
        SpotifyExchangeCodeService spotifyExchangeCodeService,
        SpotifyExchangeCodeRepository spotifyExchangeCodeRepository,
        UserRepository userRepository,
        AppUserRepository appUserRepository
    ) {
        this.spotifyExchangeCodeService = spotifyExchangeCodeService;
        this.spotifyExchangeCodeRepository = spotifyExchangeCodeRepository;
        this.userRepository = userRepository;
        this.appUserRepository = appUserRepository;
    }

    /**
     * {@code POST  /spotify-exchange-codes} : Create a new spotifyExchangeCode.
     *
     * @param spotifyExchangeCode the spotifyExchangeCode to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new spotifyExchangeCode, or with status
     *         {@code 400 (Bad Request)} if the spotifyExchangeCode has already an
     *         ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/spotify/exchange-code")
    public ResponseEntity<String> exchangeCodeForToken(
        @RequestBody SpotifyExchangeCodeDTO spotifyExchangeCodeDTO,
        Authentication authentication
    ) {
        String responseText = spotifyExchangeCodeService.exchangeCodeForToken(
            spotifyExchangeCodeDTO.getCode(),
            spotifyExchangeCodeDTO.getUrl()
        );
        if (responseText != null) {
            JSONObject responseJson = new JSONObject(responseText);
            String accessToken = responseJson.getString("access_token");
            String refreshToken = responseJson.getString("refresh_token");
            String userName = authentication.getName();
            Optional<User> userOptional = userRepository.findOneByLogin(userName);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                Long userID = user.getId();
                // Find the corresponding AppUser
                Optional<AppUser> appUserOptional = appUserRepository.findByUserId(userID);
                if (appUserOptional.isPresent()) {
                    AppUser appUser = appUserOptional.get();
                    // Update AppUser with accessToken and refreshToken
                    appUser.setSpotifyAuthToken(accessToken);
                    appUser.setSpotifyRefreshToken(refreshToken);
                    appUserRepository.save(appUser); // Save the updated AppUser
                }
            }
            return ResponseEntity.ok(responseText);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to exchange code for access token");
        }
    }

    /**
     * {@code PUT  /spotify-exchange-codes/:id} : Updates an existing
     * spotifyExchangeCode.
     *
     * @param id                  the id of the spotifyExchangeCode to save.
     * @param spotifyExchangeCode the spotifyExchangeCode to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated spotifyExchangeCode,
     *         or with status {@code 400 (Bad Request)} if the spotifyExchangeCode
     *         is not valid,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         spotifyExchangeCode couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/spotify-exchange-codes/{id}")
    public ResponseEntity<SpotifyExchangeCode> updateSpotifyExchangeCode(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SpotifyExchangeCode spotifyExchangeCode
    ) throws URISyntaxException {
        log.debug("REST request to update SpotifyExchangeCode : {}, {}", id, spotifyExchangeCode);
        if (spotifyExchangeCode.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, spotifyExchangeCode.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!spotifyExchangeCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SpotifyExchangeCode result = spotifyExchangeCodeService.update(spotifyExchangeCode);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, spotifyExchangeCode.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /spotify-exchange-codes/:id} : Partial updates given fields of
     * an existing spotifyExchangeCode, field will ignore if it is null
     *
     * @param id                  the id of the spotifyExchangeCode to save.
     * @param spotifyExchangeCode the spotifyExchangeCode to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated spotifyExchangeCode,
     *         or with status {@code 400 (Bad Request)} if the spotifyExchangeCode
     *         is not valid,
     *         or with status {@code 404 (Not Found)} if the spotifyExchangeCode is
     *         not found,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         spotifyExchangeCode couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/spotify-exchange-codes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SpotifyExchangeCode> partialUpdateSpotifyExchangeCode(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SpotifyExchangeCode spotifyExchangeCode
    ) throws URISyntaxException {
        log.debug("REST request to partial update SpotifyExchangeCode partially : {}, {}", id, spotifyExchangeCode);
        if (spotifyExchangeCode.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, spotifyExchangeCode.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!spotifyExchangeCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SpotifyExchangeCode> result = spotifyExchangeCodeService.partialUpdate(spotifyExchangeCode);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, spotifyExchangeCode.getId().toString())
        );
    }

    /**
     * {@code GET  /spotify-exchange-codes} : get all the spotifyExchangeCodes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of spotifyExchangeCodes in body.
     */
    @GetMapping("/spotify-exchange-codes")
    public List<SpotifyExchangeCode> getAllSpotifyExchangeCodes() {
        log.debug("REST request to get all SpotifyExchangeCodes");
        return spotifyExchangeCodeService.findAll();
    }

    /**
     * {@code GET  /spotify-exchange-codes/:id} : get the "id" spotifyExchangeCode.
     *
     * @param id the id of the spotifyExchangeCode to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the spotifyExchangeCode, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/spotify-exchange-codes/{id}")
    public ResponseEntity<SpotifyExchangeCode> getSpotifyExchangeCode(@PathVariable Long id) {
        log.debug("REST request to get SpotifyExchangeCode : {}", id);
        Optional<SpotifyExchangeCode> spotifyExchangeCode = spotifyExchangeCodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(spotifyExchangeCode);
    }

    /**
     * {@code DELETE  /spotify-exchange-codes/:id} : delete the "id"
     * spotifyExchangeCode.
     *
     * @param id the id of the spotifyExchangeCode to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/spotify-exchange-codes/{id}")
    public ResponseEntity<Void> deleteSpotifyExchangeCode(@PathVariable Long id) {
        log.debug("REST request to delete SpotifyExchangeCode : {}", id);
        spotifyExchangeCodeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

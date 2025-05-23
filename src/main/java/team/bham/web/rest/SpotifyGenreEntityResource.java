package team.bham.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import team.bham.domain.SpotifyGenreEntity;
import team.bham.repository.SpotifyGenreEntityRepository;
import team.bham.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link team.bham.domain.SpotifyGenreEntity}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SpotifyGenreEntityResource {

    private final Logger log = LoggerFactory.getLogger(SpotifyGenreEntityResource.class);

    private static final String ENTITY_NAME = "spotifyGenreEntity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpotifyGenreEntityRepository spotifyGenreEntityRepository;

    public SpotifyGenreEntityResource(SpotifyGenreEntityRepository spotifyGenreEntityRepository) {
        this.spotifyGenreEntityRepository = spotifyGenreEntityRepository;
    }

    /**
     * {@code POST  /spotify-genre-entities} : Create a new spotifyGenreEntity.
     *
     * @param spotifyGenreEntity the spotifyGenreEntity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new spotifyGenreEntity, or with status {@code 400 (Bad Request)} if the spotifyGenreEntity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/spotify-genre-entities")
    public ResponseEntity<SpotifyGenreEntity> createSpotifyGenreEntity(@Valid @RequestBody SpotifyGenreEntity spotifyGenreEntity)
        throws URISyntaxException {
        log.debug("REST request to save SpotifyGenreEntity : {}", spotifyGenreEntity);
        if (spotifyGenreEntity.getId() != null) {
            throw new BadRequestAlertException("A new spotifyGenreEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SpotifyGenreEntity result = spotifyGenreEntityRepository.save(spotifyGenreEntity);
        return ResponseEntity
            .created(new URI("/api/spotify-genre-entities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /spotify-genre-entities/:id} : Updates an existing spotifyGenreEntity.
     *
     * @param id the id of the spotifyGenreEntity to save.
     * @param spotifyGenreEntity the spotifyGenreEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated spotifyGenreEntity,
     * or with status {@code 400 (Bad Request)} if the spotifyGenreEntity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the spotifyGenreEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/spotify-genre-entities/{id}")
    public ResponseEntity<SpotifyGenreEntity> updateSpotifyGenreEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SpotifyGenreEntity spotifyGenreEntity
    ) throws URISyntaxException {
        log.debug("REST request to update SpotifyGenreEntity : {}, {}", id, spotifyGenreEntity);
        if (spotifyGenreEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, spotifyGenreEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!spotifyGenreEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SpotifyGenreEntity result = spotifyGenreEntityRepository.save(spotifyGenreEntity);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, spotifyGenreEntity.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /spotify-genre-entities/:id} : Partial updates given fields of an existing spotifyGenreEntity, field will ignore if it is null
     *
     * @param id the id of the spotifyGenreEntity to save.
     * @param spotifyGenreEntity the spotifyGenreEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated spotifyGenreEntity,
     * or with status {@code 400 (Bad Request)} if the spotifyGenreEntity is not valid,
     * or with status {@code 404 (Not Found)} if the spotifyGenreEntity is not found,
     * or with status {@code 500 (Internal Server Error)} if the spotifyGenreEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/spotify-genre-entities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SpotifyGenreEntity> partialUpdateSpotifyGenreEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SpotifyGenreEntity spotifyGenreEntity
    ) throws URISyntaxException {
        log.debug("REST request to partial update SpotifyGenreEntity partially : {}, {}", id, spotifyGenreEntity);
        if (spotifyGenreEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, spotifyGenreEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!spotifyGenreEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SpotifyGenreEntity> result = spotifyGenreEntityRepository
            .findById(spotifyGenreEntity.getId())
            .map(existingSpotifyGenreEntity -> {
                if (spotifyGenreEntity.getSpotifyGenre() != null) {
                    existingSpotifyGenreEntity.setSpotifyGenre(spotifyGenreEntity.getSpotifyGenre());
                }

                return existingSpotifyGenreEntity;
            })
            .map(spotifyGenreEntityRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, spotifyGenreEntity.getId().toString())
        );
    }

    /**
     * {@code GET  /spotify-genre-entities} : get all the spotifyGenreEntities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of spotifyGenreEntities in body.
     */
    @GetMapping("/spotify-genre-entities")
    public List<SpotifyGenreEntity> getAllSpotifyGenreEntities() {
        log.debug("REST request to get all SpotifyGenreEntities");
        return spotifyGenreEntityRepository.findAll();
    }

    /**
     * {@code GET  /spotify-genre-entities/:id} : get the "id" spotifyGenreEntity.
     *
     * @param id the id of the spotifyGenreEntity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the spotifyGenreEntity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/spotify-genre-entities/{id}")
    public ResponseEntity<SpotifyGenreEntity> getSpotifyGenreEntity(@PathVariable Long id) {
        log.debug("REST request to get SpotifyGenreEntity : {}", id);
        Optional<SpotifyGenreEntity> spotifyGenreEntity = spotifyGenreEntityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(spotifyGenreEntity);
    }

    /**
     * {@code DELETE  /spotify-genre-entities/:id} : delete the "id" spotifyGenreEntity.
     *
     * @param id the id of the spotifyGenreEntity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/spotify-genre-entities/{id}")
    public ResponseEntity<Void> deleteSpotifyGenreEntity(@PathVariable Long id) {
        log.debug("REST request to delete SpotifyGenreEntity : {}", id);
        spotifyGenreEntityRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

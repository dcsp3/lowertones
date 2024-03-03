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
import team.bham.domain.MusicbrainzGenreEntity;
import team.bham.repository.MusicbrainzGenreEntityRepository;
import team.bham.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link team.bham.domain.MusicbrainzGenreEntity}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MusicbrainzGenreEntityResource {

    private final Logger log = LoggerFactory.getLogger(MusicbrainzGenreEntityResource.class);

    private static final String ENTITY_NAME = "musicbrainzGenreEntity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MusicbrainzGenreEntityRepository musicbrainzGenreEntityRepository;

    public MusicbrainzGenreEntityResource(MusicbrainzGenreEntityRepository musicbrainzGenreEntityRepository) {
        this.musicbrainzGenreEntityRepository = musicbrainzGenreEntityRepository;
    }

    /**
     * {@code POST  /musicbrainz-genre-entities} : Create a new musicbrainzGenreEntity.
     *
     * @param musicbrainzGenreEntity the musicbrainzGenreEntity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new musicbrainzGenreEntity, or with status {@code 400 (Bad Request)} if the musicbrainzGenreEntity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/musicbrainz-genre-entities")
    public ResponseEntity<MusicbrainzGenreEntity> createMusicbrainzGenreEntity(
        @Valid @RequestBody MusicbrainzGenreEntity musicbrainzGenreEntity
    ) throws URISyntaxException {
        log.debug("REST request to save MusicbrainzGenreEntity : {}", musicbrainzGenreEntity);
        if (musicbrainzGenreEntity.getId() != null) {
            throw new BadRequestAlertException("A new musicbrainzGenreEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MusicbrainzGenreEntity result = musicbrainzGenreEntityRepository.save(musicbrainzGenreEntity);
        return ResponseEntity
            .created(new URI("/api/musicbrainz-genre-entities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /musicbrainz-genre-entities/:id} : Updates an existing musicbrainzGenreEntity.
     *
     * @param id the id of the musicbrainzGenreEntity to save.
     * @param musicbrainzGenreEntity the musicbrainzGenreEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated musicbrainzGenreEntity,
     * or with status {@code 400 (Bad Request)} if the musicbrainzGenreEntity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the musicbrainzGenreEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/musicbrainz-genre-entities/{id}")
    public ResponseEntity<MusicbrainzGenreEntity> updateMusicbrainzGenreEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MusicbrainzGenreEntity musicbrainzGenreEntity
    ) throws URISyntaxException {
        log.debug("REST request to update MusicbrainzGenreEntity : {}, {}", id, musicbrainzGenreEntity);
        if (musicbrainzGenreEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, musicbrainzGenreEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!musicbrainzGenreEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MusicbrainzGenreEntity result = musicbrainzGenreEntityRepository.save(musicbrainzGenreEntity);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, musicbrainzGenreEntity.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /musicbrainz-genre-entities/:id} : Partial updates given fields of an existing musicbrainzGenreEntity, field will ignore if it is null
     *
     * @param id the id of the musicbrainzGenreEntity to save.
     * @param musicbrainzGenreEntity the musicbrainzGenreEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated musicbrainzGenreEntity,
     * or with status {@code 400 (Bad Request)} if the musicbrainzGenreEntity is not valid,
     * or with status {@code 404 (Not Found)} if the musicbrainzGenreEntity is not found,
     * or with status {@code 500 (Internal Server Error)} if the musicbrainzGenreEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/musicbrainz-genre-entities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MusicbrainzGenreEntity> partialUpdateMusicbrainzGenreEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MusicbrainzGenreEntity musicbrainzGenreEntity
    ) throws URISyntaxException {
        log.debug("REST request to partial update MusicbrainzGenreEntity partially : {}, {}", id, musicbrainzGenreEntity);
        if (musicbrainzGenreEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, musicbrainzGenreEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!musicbrainzGenreEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MusicbrainzGenreEntity> result = musicbrainzGenreEntityRepository
            .findById(musicbrainzGenreEntity.getId())
            .map(existingMusicbrainzGenreEntity -> {
                if (musicbrainzGenreEntity.getMusicbrainzGenre() != null) {
                    existingMusicbrainzGenreEntity.setMusicbrainzGenre(musicbrainzGenreEntity.getMusicbrainzGenre());
                }

                return existingMusicbrainzGenreEntity;
            })
            .map(musicbrainzGenreEntityRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, musicbrainzGenreEntity.getId().toString())
        );
    }

    /**
     * {@code GET  /musicbrainz-genre-entities} : get all the musicbrainzGenreEntities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of musicbrainzGenreEntities in body.
     */
    @GetMapping("/musicbrainz-genre-entities")
    public List<MusicbrainzGenreEntity> getAllMusicbrainzGenreEntities() {
        log.debug("REST request to get all MusicbrainzGenreEntities");
        return musicbrainzGenreEntityRepository.findAll();
    }

    /**
     * {@code GET  /musicbrainz-genre-entities/:id} : get the "id" musicbrainzGenreEntity.
     *
     * @param id the id of the musicbrainzGenreEntity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the musicbrainzGenreEntity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/musicbrainz-genre-entities/{id}")
    public ResponseEntity<MusicbrainzGenreEntity> getMusicbrainzGenreEntity(@PathVariable Long id) {
        log.debug("REST request to get MusicbrainzGenreEntity : {}", id);
        Optional<MusicbrainzGenreEntity> musicbrainzGenreEntity = musicbrainzGenreEntityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(musicbrainzGenreEntity);
    }

    /**
     * {@code DELETE  /musicbrainz-genre-entities/:id} : delete the "id" musicbrainzGenreEntity.
     *
     * @param id the id of the musicbrainzGenreEntity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/musicbrainz-genre-entities/{id}")
    public ResponseEntity<Void> deleteMusicbrainzGenreEntity(@PathVariable Long id) {
        log.debug("REST request to delete MusicbrainzGenreEntity : {}", id);
        musicbrainzGenreEntityRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

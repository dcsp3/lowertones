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
import team.bham.domain.MainArtist;
import team.bham.repository.MainArtistRepository;
import team.bham.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link team.bham.domain.MainArtist}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MainArtistResource {

    private final Logger log = LoggerFactory.getLogger(MainArtistResource.class);

    private static final String ENTITY_NAME = "mainArtist";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MainArtistRepository mainArtistRepository;

    public MainArtistResource(MainArtistRepository mainArtistRepository) {
        this.mainArtistRepository = mainArtistRepository;
    }

    /**
     * {@code POST  /main-artists} : Create a new mainArtist.
     *
     * @param mainArtist the mainArtist to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mainArtist, or with status {@code 400 (Bad Request)} if the mainArtist has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/main-artists")
    public ResponseEntity<MainArtist> createMainArtist(@Valid @RequestBody MainArtist mainArtist) throws URISyntaxException {
        log.debug("REST request to save MainArtist : {}", mainArtist);
        if (mainArtist.getId() != null) {
            throw new BadRequestAlertException("A new mainArtist cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MainArtist result = mainArtistRepository.save(mainArtist);
        return ResponseEntity
            .created(new URI("/api/main-artists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /main-artists/:id} : Updates an existing mainArtist.
     *
     * @param id the id of the mainArtist to save.
     * @param mainArtist the mainArtist to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mainArtist,
     * or with status {@code 400 (Bad Request)} if the mainArtist is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mainArtist couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/main-artists/{id}")
    public ResponseEntity<MainArtist> updateMainArtist(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MainArtist mainArtist
    ) throws URISyntaxException {
        log.debug("REST request to update MainArtist : {}, {}", id, mainArtist);
        if (mainArtist.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mainArtist.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mainArtistRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MainArtist result = mainArtistRepository.save(mainArtist);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, mainArtist.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /main-artists/:id} : Partial updates given fields of an existing mainArtist, field will ignore if it is null
     *
     * @param id the id of the mainArtist to save.
     * @param mainArtist the mainArtist to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mainArtist,
     * or with status {@code 400 (Bad Request)} if the mainArtist is not valid,
     * or with status {@code 404 (Not Found)} if the mainArtist is not found,
     * or with status {@code 500 (Internal Server Error)} if the mainArtist couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/main-artists/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MainArtist> partialUpdateMainArtist(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MainArtist mainArtist
    ) throws URISyntaxException {
        log.debug("REST request to partial update MainArtist partially : {}, {}", id, mainArtist);
        if (mainArtist.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mainArtist.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mainArtistRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MainArtist> result = mainArtistRepository
            .findById(mainArtist.getId())
            .map(existingMainArtist -> {
                if (mainArtist.getArtistSpotifyID() != null) {
                    existingMainArtist.setArtistSpotifyID(mainArtist.getArtistSpotifyID());
                }
                if (mainArtist.getArtistName() != null) {
                    existingMainArtist.setArtistName(mainArtist.getArtistName());
                }
                if (mainArtist.getArtistPopularity() != null) {
                    existingMainArtist.setArtistPopularity(mainArtist.getArtistPopularity());
                }
                if (mainArtist.getArtistImage() != null) {
                    existingMainArtist.setArtistImage(mainArtist.getArtistImage());
                }
                if (mainArtist.getArtistFollowers() != null) {
                    existingMainArtist.setArtistFollowers(mainArtist.getArtistFollowers());
                }
                if (mainArtist.getDateAddedToDB() != null) {
                    existingMainArtist.setDateAddedToDB(mainArtist.getDateAddedToDB());
                }
                if (mainArtist.getDateLastModified() != null) {
                    existingMainArtist.setDateLastModified(mainArtist.getDateLastModified());
                }

                return existingMainArtist;
            })
            .map(mainArtistRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, mainArtist.getId().toString())
        );
    }

    /**
     * {@code GET  /main-artists} : get all the mainArtists.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mainArtists in body.
     */
    @GetMapping("/main-artists")
    public List<MainArtist> getAllMainArtists() {
        log.debug("REST request to get all MainArtists");
        return mainArtistRepository.findAll();
    }

    /**
     * {@code GET  /main-artists/:id} : get the "id" mainArtist.
     *
     * @param id the id of the mainArtist to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mainArtist, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/main-artists/{id}")
    public ResponseEntity<MainArtist> getMainArtist(@PathVariable Long id) {
        log.debug("REST request to get MainArtist : {}", id);
        Optional<MainArtist> mainArtist = mainArtistRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(mainArtist);
    }

    /**
     * {@code DELETE  /main-artists/:id} : delete the "id" mainArtist.
     *
     * @param id the id of the mainArtist to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/main-artists/{id}")
    public ResponseEntity<Void> deleteMainArtist(@PathVariable Long id) {
        log.debug("REST request to delete MainArtist : {}", id);
        mainArtistRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

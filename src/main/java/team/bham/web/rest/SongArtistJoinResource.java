package team.bham.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import team.bham.domain.SongArtistJoin;
import team.bham.repository.SongArtistJoinRepository;
import team.bham.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link team.bham.domain.SongArtistJoin}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SongArtistJoinResource {

    private final Logger log = LoggerFactory.getLogger(SongArtistJoinResource.class);

    private static final String ENTITY_NAME = "songArtistJoin";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SongArtistJoinRepository songArtistJoinRepository;

    public SongArtistJoinResource(SongArtistJoinRepository songArtistJoinRepository) {
        this.songArtistJoinRepository = songArtistJoinRepository;
    }

    /**
     * {@code POST  /song-artist-joins} : Create a new songArtistJoin.
     *
     * @param songArtistJoin the songArtistJoin to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new songArtistJoin, or with status {@code 400 (Bad Request)} if the songArtistJoin has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/song-artist-joins")
    public ResponseEntity<SongArtistJoin> createSongArtistJoin(@RequestBody SongArtistJoin songArtistJoin) throws URISyntaxException {
        log.debug("REST request to save SongArtistJoin : {}", songArtistJoin);
        if (songArtistJoin.getId() != null) {
            throw new BadRequestAlertException("A new songArtistJoin cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SongArtistJoin result = songArtistJoinRepository.save(songArtistJoin);
        return ResponseEntity
            .created(new URI("/api/song-artist-joins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /song-artist-joins/:id} : Updates an existing songArtistJoin.
     *
     * @param id the id of the songArtistJoin to save.
     * @param songArtistJoin the songArtistJoin to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated songArtistJoin,
     * or with status {@code 400 (Bad Request)} if the songArtistJoin is not valid,
     * or with status {@code 500 (Internal Server Error)} if the songArtistJoin couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/song-artist-joins/{id}")
    public ResponseEntity<SongArtistJoin> updateSongArtistJoin(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SongArtistJoin songArtistJoin
    ) throws URISyntaxException {
        log.debug("REST request to update SongArtistJoin : {}, {}", id, songArtistJoin);
        if (songArtistJoin.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, songArtistJoin.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!songArtistJoinRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SongArtistJoin result = songArtistJoinRepository.save(songArtistJoin);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, songArtistJoin.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /song-artist-joins/:id} : Partial updates given fields of an existing songArtistJoin, field will ignore if it is null
     *
     * @param id the id of the songArtistJoin to save.
     * @param songArtistJoin the songArtistJoin to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated songArtistJoin,
     * or with status {@code 400 (Bad Request)} if the songArtistJoin is not valid,
     * or with status {@code 404 (Not Found)} if the songArtistJoin is not found,
     * or with status {@code 500 (Internal Server Error)} if the songArtistJoin couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/song-artist-joins/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SongArtistJoin> partialUpdateSongArtistJoin(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SongArtistJoin songArtistJoin
    ) throws URISyntaxException {
        log.debug("REST request to partial update SongArtistJoin partially : {}, {}", id, songArtistJoin);
        if (songArtistJoin.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, songArtistJoin.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!songArtistJoinRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SongArtistJoin> result = songArtistJoinRepository
            .findById(songArtistJoin.getId())
            .map(existingSongArtistJoin -> {
                if (songArtistJoin.getTopTrackIndex() != null) {
                    existingSongArtistJoin.setTopTrackIndex(songArtistJoin.getTopTrackIndex());
                }

                return existingSongArtistJoin;
            })
            .map(songArtistJoinRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, songArtistJoin.getId().toString())
        );
    }

    /**
     * {@code GET  /song-artist-joins} : get all the songArtistJoins.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of songArtistJoins in body.
     */
    @GetMapping("/song-artist-joins")
    public List<SongArtistJoin> getAllSongArtistJoins() {
        log.debug("REST request to get all SongArtistJoins");
        return songArtistJoinRepository.findAll();
    }

    /**
     * {@code GET  /song-artist-joins/:id} : get the "id" songArtistJoin.
     *
     * @param id the id of the songArtistJoin to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the songArtistJoin, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/song-artist-joins/{id}")
    public ResponseEntity<SongArtistJoin> getSongArtistJoin(@PathVariable Long id) {
        log.debug("REST request to get SongArtistJoin : {}", id);
        Optional<SongArtistJoin> songArtistJoin = songArtistJoinRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(songArtistJoin);
    }

    /**
     * {@code DELETE  /song-artist-joins/:id} : delete the "id" songArtistJoin.
     *
     * @param id the id of the songArtistJoin to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/song-artist-joins/{id}")
    public ResponseEntity<Void> deleteSongArtistJoin(@PathVariable Long id) {
        log.debug("REST request to delete SongArtistJoin : {}", id);
        songArtistJoinRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

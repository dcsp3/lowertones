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
import team.bham.domain.PlaylistSongJoin;
import team.bham.repository.PlaylistSongJoinRepository;
import team.bham.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link team.bham.domain.PlaylistSongJoin}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PlaylistSongJoinResource {

    private final Logger log = LoggerFactory.getLogger(PlaylistSongJoinResource.class);

    private static final String ENTITY_NAME = "playlistSongJoin";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlaylistSongJoinRepository playlistSongJoinRepository;

    public PlaylistSongJoinResource(PlaylistSongJoinRepository playlistSongJoinRepository) {
        this.playlistSongJoinRepository = playlistSongJoinRepository;
    }

    /**
     * {@code POST  /playlist-song-joins} : Create a new playlistSongJoin.
     *
     * @param playlistSongJoin the playlistSongJoin to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new playlistSongJoin, or with status {@code 400 (Bad Request)} if the playlistSongJoin has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/playlist-song-joins")
    public ResponseEntity<PlaylistSongJoin> createPlaylistSongJoin(@Valid @RequestBody PlaylistSongJoin playlistSongJoin)
        throws URISyntaxException {
        log.debug("REST request to save PlaylistSongJoin : {}", playlistSongJoin);
        if (playlistSongJoin.getId() != null) {
            throw new BadRequestAlertException("A new playlistSongJoin cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlaylistSongJoin result = playlistSongJoinRepository.save(playlistSongJoin);
        return ResponseEntity
            .created(new URI("/api/playlist-song-joins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /playlist-song-joins/:id} : Updates an existing playlistSongJoin.
     *
     * @param id the id of the playlistSongJoin to save.
     * @param playlistSongJoin the playlistSongJoin to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated playlistSongJoin,
     * or with status {@code 400 (Bad Request)} if the playlistSongJoin is not valid,
     * or with status {@code 500 (Internal Server Error)} if the playlistSongJoin couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/playlist-song-joins/{id}")
    public ResponseEntity<PlaylistSongJoin> updatePlaylistSongJoin(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PlaylistSongJoin playlistSongJoin
    ) throws URISyntaxException {
        log.debug("REST request to update PlaylistSongJoin : {}, {}", id, playlistSongJoin);
        if (playlistSongJoin.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, playlistSongJoin.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playlistSongJoinRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PlaylistSongJoin result = playlistSongJoinRepository.save(playlistSongJoin);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, playlistSongJoin.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /playlist-song-joins/:id} : Partial updates given fields of an existing playlistSongJoin, field will ignore if it is null
     *
     * @param id the id of the playlistSongJoin to save.
     * @param playlistSongJoin the playlistSongJoin to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated playlistSongJoin,
     * or with status {@code 400 (Bad Request)} if the playlistSongJoin is not valid,
     * or with status {@code 404 (Not Found)} if the playlistSongJoin is not found,
     * or with status {@code 500 (Internal Server Error)} if the playlistSongJoin couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/playlist-song-joins/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PlaylistSongJoin> partialUpdatePlaylistSongJoin(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PlaylistSongJoin playlistSongJoin
    ) throws URISyntaxException {
        log.debug("REST request to partial update PlaylistSongJoin partially : {}, {}", id, playlistSongJoin);
        if (playlistSongJoin.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, playlistSongJoin.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playlistSongJoinRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PlaylistSongJoin> result = playlistSongJoinRepository
            .findById(playlistSongJoin.getId())
            .map(existingPlaylistSongJoin -> {
                if (playlistSongJoin.getSongOrderIndex() != null) {
                    existingPlaylistSongJoin.setSongOrderIndex(playlistSongJoin.getSongOrderIndex());
                }

                return existingPlaylistSongJoin;
            })
            .map(playlistSongJoinRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, playlistSongJoin.getId().toString())
        );
    }

    /**
     * {@code GET  /playlist-song-joins} : get all the playlistSongJoins.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of playlistSongJoins in body.
     */
    @GetMapping("/playlist-song-joins")
    public List<PlaylistSongJoin> getAllPlaylistSongJoins() {
        log.debug("REST request to get all PlaylistSongJoins");
        return playlistSongJoinRepository.findAll();
    }

    /**
     * {@code GET  /playlist-song-joins/:id} : get the "id" playlistSongJoin.
     *
     * @param id the id of the playlistSongJoin to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the playlistSongJoin, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/playlist-song-joins/{id}")
    public ResponseEntity<PlaylistSongJoin> getPlaylistSongJoin(@PathVariable Long id) {
        log.debug("REST request to get PlaylistSongJoin : {}", id);
        Optional<PlaylistSongJoin> playlistSongJoin = playlistSongJoinRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(playlistSongJoin);
    }

    /**
     * {@code DELETE  /playlist-song-joins/:id} : delete the "id" playlistSongJoin.
     *
     * @param id the id of the playlistSongJoin to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/playlist-song-joins/{id}")
    public ResponseEntity<Void> deletePlaylistSongJoin(@PathVariable Long id) {
        log.debug("REST request to delete PlaylistSongJoin : {}", id);
        playlistSongJoinRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

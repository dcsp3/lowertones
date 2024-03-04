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
import team.bham.domain.MusicBrainzSongAttribution;
import team.bham.repository.MusicBrainzSongAttributionRepository;
import team.bham.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link team.bham.domain.MusicBrainzSongAttribution}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MusicBrainzSongAttributionResource {

    private final Logger log = LoggerFactory.getLogger(MusicBrainzSongAttributionResource.class);

    private static final String ENTITY_NAME = "musicBrainzSongAttribution";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MusicBrainzSongAttributionRepository musicBrainzSongAttributionRepository;

    public MusicBrainzSongAttributionResource(MusicBrainzSongAttributionRepository musicBrainzSongAttributionRepository) {
        this.musicBrainzSongAttributionRepository = musicBrainzSongAttributionRepository;
    }

    /**
     * {@code POST  /music-brainz-song-attributions} : Create a new musicBrainzSongAttribution.
     *
     * @param musicBrainzSongAttribution the musicBrainzSongAttribution to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new musicBrainzSongAttribution, or with status {@code 400 (Bad Request)} if the musicBrainzSongAttribution has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/music-brainz-song-attributions")
    public ResponseEntity<MusicBrainzSongAttribution> createMusicBrainzSongAttribution(
        @RequestBody MusicBrainzSongAttribution musicBrainzSongAttribution
    ) throws URISyntaxException {
        log.debug("REST request to save MusicBrainzSongAttribution : {}", musicBrainzSongAttribution);
        if (musicBrainzSongAttribution.getId() != null) {
            throw new BadRequestAlertException("A new musicBrainzSongAttribution cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MusicBrainzSongAttribution result = musicBrainzSongAttributionRepository.save(musicBrainzSongAttribution);
        return ResponseEntity
            .created(new URI("/api/music-brainz-song-attributions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /music-brainz-song-attributions/:id} : Updates an existing musicBrainzSongAttribution.
     *
     * @param id the id of the musicBrainzSongAttribution to save.
     * @param musicBrainzSongAttribution the musicBrainzSongAttribution to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated musicBrainzSongAttribution,
     * or with status {@code 400 (Bad Request)} if the musicBrainzSongAttribution is not valid,
     * or with status {@code 500 (Internal Server Error)} if the musicBrainzSongAttribution couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/music-brainz-song-attributions/{id}")
    public ResponseEntity<MusicBrainzSongAttribution> updateMusicBrainzSongAttribution(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MusicBrainzSongAttribution musicBrainzSongAttribution
    ) throws URISyntaxException {
        log.debug("REST request to update MusicBrainzSongAttribution : {}, {}", id, musicBrainzSongAttribution);
        if (musicBrainzSongAttribution.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, musicBrainzSongAttribution.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!musicBrainzSongAttributionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MusicBrainzSongAttribution result = musicBrainzSongAttributionRepository.save(musicBrainzSongAttribution);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, musicBrainzSongAttribution.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /music-brainz-song-attributions/:id} : Partial updates given fields of an existing musicBrainzSongAttribution, field will ignore if it is null
     *
     * @param id the id of the musicBrainzSongAttribution to save.
     * @param musicBrainzSongAttribution the musicBrainzSongAttribution to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated musicBrainzSongAttribution,
     * or with status {@code 400 (Bad Request)} if the musicBrainzSongAttribution is not valid,
     * or with status {@code 404 (Not Found)} if the musicBrainzSongAttribution is not found,
     * or with status {@code 500 (Internal Server Error)} if the musicBrainzSongAttribution couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/music-brainz-song-attributions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MusicBrainzSongAttribution> partialUpdateMusicBrainzSongAttribution(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MusicBrainzSongAttribution musicBrainzSongAttribution
    ) throws URISyntaxException {
        log.debug("REST request to partial update MusicBrainzSongAttribution partially : {}, {}", id, musicBrainzSongAttribution);
        if (musicBrainzSongAttribution.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, musicBrainzSongAttribution.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!musicBrainzSongAttributionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MusicBrainzSongAttribution> result = musicBrainzSongAttributionRepository
            .findById(musicBrainzSongAttribution.getId())
            .map(existingMusicBrainzSongAttribution -> {
                if (musicBrainzSongAttribution.getRecordingMBID() != null) {
                    existingMusicBrainzSongAttribution.setRecordingMBID(musicBrainzSongAttribution.getRecordingMBID());
                }
                if (musicBrainzSongAttribution.getRecordingTitle() != null) {
                    existingMusicBrainzSongAttribution.setRecordingTitle(musicBrainzSongAttribution.getRecordingTitle());
                }
                if (musicBrainzSongAttribution.getSongMainArtistName() != null) {
                    existingMusicBrainzSongAttribution.setSongMainArtistName(musicBrainzSongAttribution.getSongMainArtistName());
                }
                if (musicBrainzSongAttribution.getSongMainArtistID() != null) {
                    existingMusicBrainzSongAttribution.setSongMainArtistID(musicBrainzSongAttribution.getSongMainArtistID());
                }
                if (musicBrainzSongAttribution.getSongContributorMBID() != null) {
                    existingMusicBrainzSongAttribution.setSongContributorMBID(musicBrainzSongAttribution.getSongContributorMBID());
                }
                if (musicBrainzSongAttribution.getSongContributorName() != null) {
                    existingMusicBrainzSongAttribution.setSongContributorName(musicBrainzSongAttribution.getSongContributorName());
                }
                if (musicBrainzSongAttribution.getSongContributorRole() != null) {
                    existingMusicBrainzSongAttribution.setSongContributorRole(musicBrainzSongAttribution.getSongContributorRole());
                }
                if (musicBrainzSongAttribution.getSongContributorInstrument() != null) {
                    existingMusicBrainzSongAttribution.setSongContributorInstrument(
                        musicBrainzSongAttribution.getSongContributorInstrument()
                    );
                }

                return existingMusicBrainzSongAttribution;
            })
            .map(musicBrainzSongAttributionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, musicBrainzSongAttribution.getId().toString())
        );
    }

    /**
     * {@code GET  /music-brainz-song-attributions} : get all the musicBrainzSongAttributions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of musicBrainzSongAttributions in body.
     */
    @GetMapping("/music-brainz-song-attributions")
    public List<MusicBrainzSongAttribution> getAllMusicBrainzSongAttributions() {
        log.debug("REST request to get all MusicBrainzSongAttributions");
        return musicBrainzSongAttributionRepository.findAll();
    }

    /**
     * {@code GET  /music-brainz-song-attributions/:id} : get the "id" musicBrainzSongAttribution.
     *
     * @param id the id of the musicBrainzSongAttribution to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the musicBrainzSongAttribution, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/music-brainz-song-attributions/{id}")
    public ResponseEntity<MusicBrainzSongAttribution> getMusicBrainzSongAttribution(@PathVariable Long id) {
        log.debug("REST request to get MusicBrainzSongAttribution : {}", id);
        Optional<MusicBrainzSongAttribution> musicBrainzSongAttribution = musicBrainzSongAttributionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(musicBrainzSongAttribution);
    }

    /**
     * {@code DELETE  /music-brainz-song-attributions/:id} : delete the "id" musicBrainzSongAttribution.
     *
     * @param id the id of the musicBrainzSongAttribution to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/music-brainz-song-attributions/{id}")
    public ResponseEntity<Void> deleteMusicBrainzSongAttribution(@PathVariable Long id) {
        log.debug("REST request to delete MusicBrainzSongAttribution : {}", id);
        musicBrainzSongAttributionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

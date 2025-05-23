package team.bham.web.rest;

import com.google.gson.JsonObject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.spel.ast.OpInc;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import team.bham.domain.Song;
import team.bham.repository.SongRepository;
import team.bham.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link team.bham.domain.Song}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SongResource {

    private final Logger log = LoggerFactory.getLogger(SongResource.class);

    private static final String ENTITY_NAME = "song";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SongRepository songRepository;

    public SongResource(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    /**
     * {@code POST  /songs} : Create a new song.
     *
     * @param song the song to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new song, or with status {@code 400 (Bad Request)} if the song has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/songs")
    public ResponseEntity<Song> createSong(@Valid @RequestBody Song song) throws URISyntaxException {
        log.debug("REST request to save Song : {}", song);
        if (song.getId() != null) {
            throw new BadRequestAlertException("A new song cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Song result = songRepository.save(song);
        return ResponseEntity
            .created(new URI("/api/songs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /songs/:id} : Updates an existing song.
     *
     * @param id the id of the song to save.
     * @param song the song to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated song,
     * or with status {@code 400 (Bad Request)} if the song is not valid,
     * or with status {@code 500 (Internal Server Error)} if the song couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/songs/{id}")
    public ResponseEntity<Song> updateSong(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Song song)
        throws URISyntaxException {
        log.debug("REST request to update Song : {}, {}", id, song);
        if (song.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, song.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!songRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Song result = songRepository.save(song);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, song.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /songs/:id} : Partial updates given fields of an existing song, field will ignore if it is null
     *
     * @param id the id of the song to save.
     * @param song the song to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated song,
     * or with status {@code 400 (Bad Request)} if the song is not valid,
     * or with status {@code 404 (Not Found)} if the song is not found,
     * or with status {@code 500 (Internal Server Error)} if the song couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/songs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Song> partialUpdateSong(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Song song
    ) throws URISyntaxException {
        log.debug("REST request to partial update Song partially : {}, {}", id, song);
        if (song.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, song.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!songRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Song> result = songRepository
            .findById(song.getId())
            .map(existingSong -> {
                if (song.getSongSpotifyID() != null) {
                    existingSong.setSongSpotifyID(song.getSongSpotifyID());
                }
                if (song.getSongTitle() != null) {
                    existingSong.setSongTitle(song.getSongTitle());
                }
                if (song.getSongDuration() != null) {
                    existingSong.setSongDuration(song.getSongDuration());
                }
                if (song.getSongAlbumType() != null) {
                    existingSong.setSongAlbumType(song.getSongAlbumType());
                }
                if (song.getSongAlbumID() != null) {
                    existingSong.setSongAlbumID(song.getSongAlbumID());
                }
                if (song.getSongExplicit() != null) {
                    existingSong.setSongExplicit(song.getSongExplicit());
                }
                if (song.getSongPopularity() != null) {
                    existingSong.setSongPopularity(song.getSongPopularity());
                }
                if (song.getSongPreviewURL() != null) {
                    existingSong.setSongPreviewURL(song.getSongPreviewURL());
                }
                if (song.getSongTrackFeaturesAdded() != null) {
                    existingSong.setSongTrackFeaturesAdded(song.getSongTrackFeaturesAdded());
                }
                if (song.getSongAcousticness() != null) {
                    existingSong.setSongAcousticness(song.getSongAcousticness());
                }
                if (song.getSongDanceability() != null) {
                    existingSong.setSongDanceability(song.getSongDanceability());
                }
                if (song.getSongEnergy() != null) {
                    existingSong.setSongEnergy(song.getSongEnergy());
                }
                if (song.getSongInstrumentalness() != null) {
                    existingSong.setSongInstrumentalness(song.getSongInstrumentalness());
                }
                if (song.getSongLiveness() != null) {
                    existingSong.setSongLiveness(song.getSongLiveness());
                }
                if (song.getSongLoudness() != null) {
                    existingSong.setSongLoudness(song.getSongLoudness());
                }
                if (song.getSongSpeechiness() != null) {
                    existingSong.setSongSpeechiness(song.getSongSpeechiness());
                }
                if (song.getSongTempo() != null) {
                    existingSong.setSongTempo(song.getSongTempo());
                }
                if (song.getSongValence() != null) {
                    existingSong.setSongValence(song.getSongValence());
                }
                if (song.getSongKey() != null) {
                    existingSong.setSongKey(song.getSongKey());
                }
                if (song.getSongTimeSignature() != null) {
                    existingSong.setSongTimeSignature(song.getSongTimeSignature());
                }
                if (song.getSongDateAddedToDB() != null) {
                    existingSong.setSongDateAddedToDB(song.getSongDateAddedToDB());
                }
                if (song.getSongDateLastModified() != null) {
                    existingSong.setSongDateLastModified(song.getSongDateLastModified());
                }
                if (song.getRecordingMBID() != null) {
                    existingSong.setRecordingMBID(song.getRecordingMBID());
                }

                return existingSong;
            })
            .map(songRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, song.getId().toString())
        );
    }

    /**
     * {@code GET  /songs} : get all the songs.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of songs in body.
     */
    @GetMapping("/songs")
    public List<Song> getAllSongs(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Songs");
        if (eagerload) {
            return songRepository.findAllWithEagerRelationships();
        } else {
            return songRepository.findAll();
        }
    }

    /**
     * {@code GET  /songs/:id} : get the "id" song.
     *
     * @param id the id of the song to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the song, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/songs/{id}")
    public ResponseEntity<Song> getSong(@PathVariable Long id) {
        log.debug("REST request to get Song : {}", id);
        Optional<Song> song = songRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(song);
    }

    /**
     * {@code DELETE  /songs/:id} : delete the "id" song.
     *
     * @param id the id of the song to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/songs/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) {
        log.debug("REST request to delete Song : {}", id);
        songRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/mbid-spotify-song-mapping/{id}")
    public ResponseEntity<Map<String, String>> getMBIDBySpotify(@PathVariable String id) {
        String mbid = songRepository.findSongBySpotifyId(id).getRecordingMBID();
        Map<String, String> response = new HashMap<>();
        if (mbid != null) {
            response.put("MusicBrainz-Recording-ID", mbid);
        } else if (mbid == null) {
            response.put("error", "No artist found with that Spotify ID");
        }
        return ResponseEntity.ok(response);
    }
}

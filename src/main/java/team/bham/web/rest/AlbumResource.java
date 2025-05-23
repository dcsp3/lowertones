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
import team.bham.domain.Album;
import team.bham.repository.AlbumRepository;
import team.bham.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link team.bham.domain.Album}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AlbumResource {

    private final Logger log = LoggerFactory.getLogger(AlbumResource.class);

    private static final String ENTITY_NAME = "album";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlbumRepository albumRepository;

    public AlbumResource(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    /**
     * {@code POST  /albums} : Create a new album.
     *
     * @param album the album to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new album, or with status {@code 400 (Bad Request)} if the album has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/albums")
    public ResponseEntity<Album> createAlbum(@Valid @RequestBody Album album) throws URISyntaxException {
        log.debug("REST request to save Album : {}", album);
        if (album.getId() != null) {
            throw new BadRequestAlertException("A new album cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Album result = albumRepository.save(album);
        return ResponseEntity
            .created(new URI("/api/albums/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /albums/:id} : Updates an existing album.
     *
     * @param id the id of the album to save.
     * @param album the album to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated album,
     * or with status {@code 400 (Bad Request)} if the album is not valid,
     * or with status {@code 500 (Internal Server Error)} if the album couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/albums/{id}")
    public ResponseEntity<Album> updateAlbum(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Album album)
        throws URISyntaxException {
        log.debug("REST request to update Album : {}, {}", id, album);
        if (album.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, album.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!albumRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Album result = albumRepository.save(album);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, album.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /albums/:id} : Partial updates given fields of an existing album, field will ignore if it is null
     *
     * @param id the id of the album to save.
     * @param album the album to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated album,
     * or with status {@code 400 (Bad Request)} if the album is not valid,
     * or with status {@code 404 (Not Found)} if the album is not found,
     * or with status {@code 500 (Internal Server Error)} if the album couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/albums/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Album> partialUpdateAlbum(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Album album
    ) throws URISyntaxException {
        log.debug("REST request to partial update Album partially : {}, {}", id, album);
        if (album.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, album.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!albumRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Album> result = albumRepository
            .findById(album.getId())
            .map(existingAlbum -> {
                if (album.getAlbumSpotifyID() != null) {
                    existingAlbum.setAlbumSpotifyID(album.getAlbumSpotifyID());
                }
                if (album.getAlbumName() != null) {
                    existingAlbum.setAlbumName(album.getAlbumName());
                }
                if (album.getAlbumCoverArt() != null) {
                    existingAlbum.setAlbumCoverArt(album.getAlbumCoverArt());
                }
                if (album.getAlbumReleaseDate() != null) {
                    existingAlbum.setAlbumReleaseDate(album.getAlbumReleaseDate());
                }
                if (album.getReleaseDatePrecision() != null) {
                    existingAlbum.setReleaseDatePrecision(album.getReleaseDatePrecision());
                }
                if (album.getAlbumPopularity() != null) {
                    existingAlbum.setAlbumPopularity(album.getAlbumPopularity());
                }
                if (album.getAlbumType() != null) {
                    existingAlbum.setAlbumType(album.getAlbumType());
                }
                if (album.getSpotifyAlbumUPC() != null) {
                    existingAlbum.setSpotifyAlbumUPC(album.getSpotifyAlbumUPC());
                }
                if (album.getSpotifyAlbumEAN() != null) {
                    existingAlbum.setSpotifyAlbumEAN(album.getSpotifyAlbumEAN());
                }
                if (album.getSpotifyAlbumISRC() != null) {
                    existingAlbum.setSpotifyAlbumISRC(album.getSpotifyAlbumISRC());
                }
                if (album.getDateAddedToDB() != null) {
                    existingAlbum.setDateAddedToDB(album.getDateAddedToDB());
                }
                if (album.getDateLastModified() != null) {
                    existingAlbum.setDateLastModified(album.getDateLastModified());
                }
                if (album.getMusicbrainzMetadataAdded() != null) {
                    existingAlbum.setMusicbrainzMetadataAdded(album.getMusicbrainzMetadataAdded());
                }
                if (album.getMusicbrainzID() != null) {
                    existingAlbum.setMusicbrainzID(album.getMusicbrainzID());
                }

                return existingAlbum;
            })
            .map(albumRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, album.getId().toString())
        );
    }

    /**
     * {@code GET  /albums} : get all the albums.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of albums in body.
     */
    @GetMapping("/albums")
    public List<Album> getAllAlbums() {
        log.debug("REST request to get all Albums");
        return albumRepository.findAll();
    }

    /**
     * {@code GET  /albums/:id} : get the "id" album.
     *
     * @param id the id of the album to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the album, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/albums/{id}")
    public ResponseEntity<Album> getAlbum(@PathVariable Long id) {
        log.debug("REST request to get Album : {}", id);
        Optional<Album> album = albumRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(album);
    }

    /**
     * {@code DELETE  /albums/:id} : delete the "id" album.
     *
     * @param id the id of the album to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/albums/{id}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable Long id) {
        log.debug("REST request to delete Album : {}", id);
        albumRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

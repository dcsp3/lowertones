package team.bham.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import team.bham.domain.RelatedArtists;
import team.bham.repository.RelatedArtistsRepository;
import team.bham.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link team.bham.domain.RelatedArtists}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RelatedArtistsResource {

    private final Logger log = LoggerFactory.getLogger(RelatedArtistsResource.class);

    private static final String ENTITY_NAME = "relatedArtists";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RelatedArtistsRepository relatedArtistsRepository;

    public RelatedArtistsResource(RelatedArtistsRepository relatedArtistsRepository) {
        this.relatedArtistsRepository = relatedArtistsRepository;
    }

    /**
     * {@code POST  /related-artists} : Create a new relatedArtists.
     *
     * @param relatedArtists the relatedArtists to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new relatedArtists, or with status {@code 400 (Bad Request)} if the relatedArtists has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/related-artists")
    public ResponseEntity<RelatedArtists> createRelatedArtists(@Valid @RequestBody RelatedArtists relatedArtists)
        throws URISyntaxException {
        log.debug("REST request to save RelatedArtists : {}", relatedArtists);
        if (relatedArtists.getId() != null) {
            throw new BadRequestAlertException("A new relatedArtists cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RelatedArtists result = relatedArtistsRepository.save(relatedArtists);
        return ResponseEntity
            .created(new URI("/api/related-artists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /related-artists/:id} : Updates an existing relatedArtists.
     *
     * @param id the id of the relatedArtists to save.
     * @param relatedArtists the relatedArtists to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated relatedArtists,
     * or with status {@code 400 (Bad Request)} if the relatedArtists is not valid,
     * or with status {@code 500 (Internal Server Error)} if the relatedArtists couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/related-artists/{id}")
    public ResponseEntity<RelatedArtists> updateRelatedArtists(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RelatedArtists relatedArtists
    ) throws URISyntaxException {
        log.debug("REST request to update RelatedArtists : {}, {}", id, relatedArtists);
        if (relatedArtists.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, relatedArtists.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!relatedArtistsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RelatedArtists result = relatedArtistsRepository.save(relatedArtists);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, relatedArtists.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /related-artists/:id} : Partial updates given fields of an existing relatedArtists, field will ignore if it is null
     *
     * @param id the id of the relatedArtists to save.
     * @param relatedArtists the relatedArtists to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated relatedArtists,
     * or with status {@code 400 (Bad Request)} if the relatedArtists is not valid,
     * or with status {@code 404 (Not Found)} if the relatedArtists is not found,
     * or with status {@code 500 (Internal Server Error)} if the relatedArtists couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/related-artists/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RelatedArtists> partialUpdateRelatedArtists(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RelatedArtists relatedArtists
    ) throws URISyntaxException {
        log.debug("REST request to partial update RelatedArtists partially : {}, {}", id, relatedArtists);
        if (relatedArtists.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, relatedArtists.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!relatedArtistsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RelatedArtists> result = relatedArtistsRepository
            .findById(relatedArtists.getId())
            .map(existingRelatedArtists -> {
                if (relatedArtists.getMainArtistSptfyID() != null) {
                    existingRelatedArtists.setMainArtistSptfyID(relatedArtists.getMainArtistSptfyID());
                }
                if (relatedArtists.getRelatedArtistSpotifyID1() != null) {
                    existingRelatedArtists.setRelatedArtistSpotifyID1(relatedArtists.getRelatedArtistSpotifyID1());
                }
                if (relatedArtists.getRelatedArtistSpotifyID2() != null) {
                    existingRelatedArtists.setRelatedArtistSpotifyID2(relatedArtists.getRelatedArtistSpotifyID2());
                }
                if (relatedArtists.getRelatedArtistSpotifyID3() != null) {
                    existingRelatedArtists.setRelatedArtistSpotifyID3(relatedArtists.getRelatedArtistSpotifyID3());
                }
                if (relatedArtists.getRelatedArtistSpotifyID4() != null) {
                    existingRelatedArtists.setRelatedArtistSpotifyID4(relatedArtists.getRelatedArtistSpotifyID4());
                }
                if (relatedArtists.getRelatedArtistSpotifyID5() != null) {
                    existingRelatedArtists.setRelatedArtistSpotifyID5(relatedArtists.getRelatedArtistSpotifyID5());
                }
                if (relatedArtists.getRelatedArtistSpotifyID6() != null) {
                    existingRelatedArtists.setRelatedArtistSpotifyID6(relatedArtists.getRelatedArtistSpotifyID6());
                }
                if (relatedArtists.getRelatedArtistSpotifyID7() != null) {
                    existingRelatedArtists.setRelatedArtistSpotifyID7(relatedArtists.getRelatedArtistSpotifyID7());
                }
                if (relatedArtists.getRelatedArtistSpotifyID8() != null) {
                    existingRelatedArtists.setRelatedArtistSpotifyID8(relatedArtists.getRelatedArtistSpotifyID8());
                }
                if (relatedArtists.getRelatedArtistSpotifyID9() != null) {
                    existingRelatedArtists.setRelatedArtistSpotifyID9(relatedArtists.getRelatedArtistSpotifyID9());
                }
                if (relatedArtists.getRelatedArtistSpotifyID10() != null) {
                    existingRelatedArtists.setRelatedArtistSpotifyID10(relatedArtists.getRelatedArtistSpotifyID10());
                }
                if (relatedArtists.getRelatedArtistSpotifyID11() != null) {
                    existingRelatedArtists.setRelatedArtistSpotifyID11(relatedArtists.getRelatedArtistSpotifyID11());
                }
                if (relatedArtists.getRelatedArtistSpotifyID12() != null) {
                    existingRelatedArtists.setRelatedArtistSpotifyID12(relatedArtists.getRelatedArtistSpotifyID12());
                }
                if (relatedArtists.getRelatedArtistSpotifyID13() != null) {
                    existingRelatedArtists.setRelatedArtistSpotifyID13(relatedArtists.getRelatedArtistSpotifyID13());
                }
                if (relatedArtists.getRelatedArtistSpotifyID14() != null) {
                    existingRelatedArtists.setRelatedArtistSpotifyID14(relatedArtists.getRelatedArtistSpotifyID14());
                }
                if (relatedArtists.getRelatedArtistSpotifyID15() != null) {
                    existingRelatedArtists.setRelatedArtistSpotifyID15(relatedArtists.getRelatedArtistSpotifyID15());
                }
                if (relatedArtists.getRelatedArtistSpotifyID16() != null) {
                    existingRelatedArtists.setRelatedArtistSpotifyID16(relatedArtists.getRelatedArtistSpotifyID16());
                }
                if (relatedArtists.getRelatedArtistSpotifyID17() != null) {
                    existingRelatedArtists.setRelatedArtistSpotifyID17(relatedArtists.getRelatedArtistSpotifyID17());
                }
                if (relatedArtists.getRelatedArtistSpotifyID18() != null) {
                    existingRelatedArtists.setRelatedArtistSpotifyID18(relatedArtists.getRelatedArtistSpotifyID18());
                }
                if (relatedArtists.getRelatedArtistSpotifyID19() != null) {
                    existingRelatedArtists.setRelatedArtistSpotifyID19(relatedArtists.getRelatedArtistSpotifyID19());
                }
                if (relatedArtists.getRelatedArtistSpotifyID20() != null) {
                    existingRelatedArtists.setRelatedArtistSpotifyID20(relatedArtists.getRelatedArtistSpotifyID20());
                }

                return existingRelatedArtists;
            })
            .map(relatedArtistsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, relatedArtists.getId().toString())
        );
    }

    /**
     * {@code GET  /related-artists} : get all the relatedArtists.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of relatedArtists in body.
     */
    @GetMapping("/related-artists")
    public List<RelatedArtists> getAllRelatedArtists(@RequestParam(required = false) String filter) {
        if ("mainartist-is-null".equals(filter)) {
            log.debug("REST request to get all RelatedArtistss where mainArtist is null");
            return StreamSupport
                .stream(relatedArtistsRepository.findAll().spliterator(), false)
                .filter(relatedArtists -> relatedArtists.getMainArtist() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all RelatedArtists");
        return relatedArtistsRepository.findAll();
    }

    /**
     * {@code GET  /related-artists/:id} : get the "id" relatedArtists.
     *
     * @param id the id of the relatedArtists to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the relatedArtists, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/related-artists/{id}")
    public ResponseEntity<RelatedArtists> getRelatedArtists(@PathVariable Long id) {
        log.debug("REST request to get RelatedArtists : {}", id);
        Optional<RelatedArtists> relatedArtists = relatedArtistsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(relatedArtists);
    }

    /**
     * {@code DELETE  /related-artists/:id} : delete the "id" relatedArtists.
     *
     * @param id the id of the relatedArtists to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/related-artists/{id}")
    public ResponseEntity<Void> deleteRelatedArtists(@PathVariable Long id) {
        log.debug("REST request to delete RelatedArtists : {}", id);
        relatedArtistsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

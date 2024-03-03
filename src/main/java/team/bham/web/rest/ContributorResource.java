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
import team.bham.domain.Contributor;
import team.bham.repository.ContributorRepository;
import team.bham.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link team.bham.domain.Contributor}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ContributorResource {

    private final Logger log = LoggerFactory.getLogger(ContributorResource.class);

    private static final String ENTITY_NAME = "contributor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContributorRepository contributorRepository;

    public ContributorResource(ContributorRepository contributorRepository) {
        this.contributorRepository = contributorRepository;
    }

    /**
     * {@code POST  /contributors} : Create a new contributor.
     *
     * @param contributor the contributor to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contributor, or with status {@code 400 (Bad Request)} if the contributor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contributors")
    public ResponseEntity<Contributor> createContributor(@RequestBody Contributor contributor) throws URISyntaxException {
        log.debug("REST request to save Contributor : {}", contributor);
        if (contributor.getId() != null) {
            throw new BadRequestAlertException("A new contributor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Contributor result = contributorRepository.save(contributor);
        return ResponseEntity
            .created(new URI("/api/contributors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contributors/:id} : Updates an existing contributor.
     *
     * @param id the id of the contributor to save.
     * @param contributor the contributor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contributor,
     * or with status {@code 400 (Bad Request)} if the contributor is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contributor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contributors/{id}")
    public ResponseEntity<Contributor> updateContributor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Contributor contributor
    ) throws URISyntaxException {
        log.debug("REST request to update Contributor : {}, {}", id, contributor);
        if (contributor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contributor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contributorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Contributor result = contributorRepository.save(contributor);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, contributor.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /contributors/:id} : Partial updates given fields of an existing contributor, field will ignore if it is null
     *
     * @param id the id of the contributor to save.
     * @param contributor the contributor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contributor,
     * or with status {@code 400 (Bad Request)} if the contributor is not valid,
     * or with status {@code 404 (Not Found)} if the contributor is not found,
     * or with status {@code 500 (Internal Server Error)} if the contributor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/contributors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Contributor> partialUpdateContributor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Contributor contributor
    ) throws URISyntaxException {
        log.debug("REST request to partial update Contributor partially : {}, {}", id, contributor);
        if (contributor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contributor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contributorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Contributor> result = contributorRepository
            .findById(contributor.getId())
            .map(existingContributor -> {
                if (contributor.getName() != null) {
                    existingContributor.setName(contributor.getName());
                }
                if (contributor.getRole() != null) {
                    existingContributor.setRole(contributor.getRole());
                }
                if (contributor.getMusicbrainzID() != null) {
                    existingContributor.setMusicbrainzID(contributor.getMusicbrainzID());
                }

                return existingContributor;
            })
            .map(contributorRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, contributor.getId().toString())
        );
    }

    /**
     * {@code GET  /contributors} : get all the contributors.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contributors in body.
     */
    @GetMapping("/contributors")
    public List<Contributor> getAllContributors() {
        log.debug("REST request to get all Contributors");
        return contributorRepository.findAll();
    }

    /**
     * {@code GET  /contributors/:id} : get the "id" contributor.
     *
     * @param id the id of the contributor to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contributor, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contributors/{id}")
    public ResponseEntity<Contributor> getContributor(@PathVariable Long id) {
        log.debug("REST request to get Contributor : {}", id);
        Optional<Contributor> contributor = contributorRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(contributor);
    }

    /**
     * {@code DELETE  /contributors/:id} : delete the "id" contributor.
     *
     * @param id the id of the contributor to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contributors/{id}")
    public ResponseEntity<Void> deleteContributor(@PathVariable Long id) {
        log.debug("REST request to delete Contributor : {}", id);
        contributorRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

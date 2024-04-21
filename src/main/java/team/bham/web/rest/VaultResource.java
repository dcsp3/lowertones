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
import team.bham.domain.Vault;
import team.bham.repository.VaultRepository;
import team.bham.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link team.bham.domain.Vault}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VaultResource {

    private final Logger log = LoggerFactory.getLogger(VaultResource.class);

    private static final String ENTITY_NAME = "vault";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VaultRepository vaultRepository;

    public VaultResource(VaultRepository vaultRepository) {
        this.vaultRepository = vaultRepository;
    }

    /**
     * {@code POST  /vaults} : Create a new vault.
     *
     * @param vault the vault to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vault, or with status {@code 400 (Bad Request)} if the vault has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vaults")
    public ResponseEntity<Vault> createVault(@RequestBody Vault vault) throws URISyntaxException {
        log.debug("REST request to save Vault : {}", vault);
        if (vault.getId() != null) {
            throw new BadRequestAlertException("A new vault cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Vault result = vaultRepository.save(vault);
        return ResponseEntity
            .created(new URI("/api/vaults/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vaults/:id} : Updates an existing vault.
     *
     * @param id the id of the vault to save.
     * @param vault the vault to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vault,
     * or with status {@code 400 (Bad Request)} if the vault is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vault couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vaults/{id}")
    public ResponseEntity<Vault> updateVault(@PathVariable(value = "id", required = false) final Long id, @RequestBody Vault vault)
        throws URISyntaxException {
        log.debug("REST request to update Vault : {}, {}", id, vault);
        if (vault.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vault.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vaultRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Vault result = vaultRepository.save(vault);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vault.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vaults/:id} : Partial updates given fields of an existing vault, field will ignore if it is null
     *
     * @param id the id of the vault to save.
     * @param vault the vault to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vault,
     * or with status {@code 400 (Bad Request)} if the vault is not valid,
     * or with status {@code 404 (Not Found)} if the vault is not found,
     * or with status {@code 500 (Internal Server Error)} if the vault couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vaults/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Vault> partialUpdateVault(@PathVariable(value = "id", required = false) final Long id, @RequestBody Vault vault)
        throws URISyntaxException {
        log.debug("REST request to partial update Vault partially : {}, {}", id, vault);
        if (vault.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vault.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vaultRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Vault> result = vaultRepository
            .findById(vault.getId())
            .map(existingVault -> {
                if (vault.getSourcePlaylistID() != null) {
                    existingVault.setSourcePlaylistID(vault.getSourcePlaylistID());
                }
                if (vault.getPlaylistName() != null) {
                    existingVault.setPlaylistName(vault.getPlaylistName());
                }
                if (vault.getResultPlaylistID() != null) {
                    existingVault.setResultPlaylistID(vault.getResultPlaylistID());
                }
                if (vault.getFrequency() != null) {
                    existingVault.setFrequency(vault.getFrequency());
                }
                if (vault.getType() != null) {
                    existingVault.setType(vault.getType());
                }
                if (vault.getPlaylistCoverURL() != null) {
                    existingVault.setPlaylistCoverURL(vault.getPlaylistCoverURL());
                }
                if (vault.getPlaylistSnapshotID() != null) {
                    existingVault.setPlaylistSnapshotID(vault.getPlaylistSnapshotID());
                }

                return existingVault;
            })
            .map(vaultRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vault.getId().toString())
        );
    }

    /**
     * {@code GET  /vaults} : get all the vaults.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vaults in body.
     */
    @GetMapping("/vaults")
    public List<Vault> getAllVaults() {
        log.debug("REST request to get all Vaults");
        return vaultRepository.findAll();
    }

    /**
     * {@code GET  /vaults/:id} : get the "id" vault.
     *
     * @param id the id of the vault to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vault, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vaults/{id}")
    public ResponseEntity<Vault> getVault(@PathVariable Long id) {
        log.debug("REST request to get Vault : {}", id);
        Optional<Vault> vault = vaultRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(vault);
    }

    /**
     * {@code DELETE  /vaults/:id} : delete the "id" vault.
     *
     * @param id the id of the vault to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vaults/{id}")
    public ResponseEntity<Void> deleteVault(@PathVariable Long id) {
        log.debug("REST request to delete Vault : {}", id);
        vaultRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

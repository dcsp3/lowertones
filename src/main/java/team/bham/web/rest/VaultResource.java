package team.bham.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import team.bham.domain.Vault;
import team.bham.repository.VaultRepository;
import team.bham.service.VaultService;
import team.bham.service.dto.VaultRequest;
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

    @Autowired
    private VaultService vaultService;

    private static final Logger logger = LoggerFactory.getLogger(VaultResource.class);

    @PostMapping("/vaults/create-task")
    public ResponseEntity<?> createVaultTask(@RequestBody VaultRequest vaultRequest, Authentication authentication) {
        try {
            String playlistName = vaultRequest.getPlaylistName();
            String sourcePlaylistId = vaultRequest.getSourcePlaylistId();
            String frequency = vaultRequest.getFrequency();

            if (playlistName.isEmpty() || sourcePlaylistId.isEmpty() || frequency.isEmpty()) {
                logger.warn("Missing required fields in the request");
                return ResponseEntity.badRequest().body("Missing required fields");
            }

            Vault vault = vaultService.createVaultTask(vaultRequest, authentication);
            return ResponseEntity.status(HttpStatus.CREATED).body(vault);
        } catch (UsernameNotFoundException e) {
            logger.error("User not found", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            logger.error("Error creating vault task", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @GetMapping("/vaults/user")
    public ResponseEntity<List<Vault>> getVaultsByUser(Authentication authentication) {
        return vaultService.getVaultsByUserId(authentication);
    }

    @DeleteMapping("/vaults/{id}")
    public ResponseEntity<Void> deleteVault(@PathVariable Long id) {
        vaultService.deleteVault(id);
        return ResponseEntity.noContent().build();
    }
}

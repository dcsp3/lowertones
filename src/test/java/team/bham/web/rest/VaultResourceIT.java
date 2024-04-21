package team.bham.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import team.bham.IntegrationTest;
import team.bham.domain.Vault;
import team.bham.repository.VaultRepository;

/**
 * Integration tests for the {@link VaultResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VaultResourceIT {

    private static final String DEFAULT_SOURCE_PLAYLIST_ID = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_PLAYLIST_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PLAYLIST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PLAYLIST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_RESULT_PLAYLIST_ID = "AAAAAAAAAA";
    private static final String UPDATED_RESULT_PLAYLIST_ID = "BBBBBBBBBB";

    private static final String DEFAULT_FREQUENCY = "AAAAAAAAAA";
    private static final String UPDATED_FREQUENCY = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_PLAYLIST_COVER_URL = "AAAAAAAAAA";
    private static final String UPDATED_PLAYLIST_COVER_URL = "BBBBBBBBBB";

    private static final String DEFAULT_PLAYLIST_SNAPSHOT_ID = "AAAAAAAAAA";
    private static final String UPDATED_PLAYLIST_SNAPSHOT_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/vaults";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VaultRepository vaultRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVaultMockMvc;

    private Vault vault;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vault createEntity(EntityManager em) {
        Vault vault = new Vault()
            .sourcePlaylistID(DEFAULT_SOURCE_PLAYLIST_ID)
            .playlistName(DEFAULT_PLAYLIST_NAME)
            .resultPlaylistID(DEFAULT_RESULT_PLAYLIST_ID)
            .frequency(DEFAULT_FREQUENCY)
            .type(DEFAULT_TYPE)
            .playlistCoverURL(DEFAULT_PLAYLIST_COVER_URL)
            .playlistSnapshotID(DEFAULT_PLAYLIST_SNAPSHOT_ID);
        return vault;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vault createUpdatedEntity(EntityManager em) {
        Vault vault = new Vault()
            .sourcePlaylistID(UPDATED_SOURCE_PLAYLIST_ID)
            .playlistName(UPDATED_PLAYLIST_NAME)
            .resultPlaylistID(UPDATED_RESULT_PLAYLIST_ID)
            .frequency(UPDATED_FREQUENCY)
            .type(UPDATED_TYPE)
            .playlistCoverURL(UPDATED_PLAYLIST_COVER_URL)
            .playlistSnapshotID(UPDATED_PLAYLIST_SNAPSHOT_ID);
        return vault;
    }

    @BeforeEach
    public void initTest() {
        vault = createEntity(em);
    }

    @Test
    @Transactional
    void createVault() throws Exception {
        int databaseSizeBeforeCreate = vaultRepository.findAll().size();
        // Create the Vault
        restVaultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vault)))
            .andExpect(status().isCreated());

        // Validate the Vault in the database
        List<Vault> vaultList = vaultRepository.findAll();
        assertThat(vaultList).hasSize(databaseSizeBeforeCreate + 1);
        Vault testVault = vaultList.get(vaultList.size() - 1);
        assertThat(testVault.getSourcePlaylistID()).isEqualTo(DEFAULT_SOURCE_PLAYLIST_ID);
        assertThat(testVault.getPlaylistName()).isEqualTo(DEFAULT_PLAYLIST_NAME);
        assertThat(testVault.getResultPlaylistID()).isEqualTo(DEFAULT_RESULT_PLAYLIST_ID);
        assertThat(testVault.getFrequency()).isEqualTo(DEFAULT_FREQUENCY);
        assertThat(testVault.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testVault.getPlaylistCoverURL()).isEqualTo(DEFAULT_PLAYLIST_COVER_URL);
        assertThat(testVault.getPlaylistSnapshotID()).isEqualTo(DEFAULT_PLAYLIST_SNAPSHOT_ID);
    }

    @Test
    @Transactional
    void createVaultWithExistingId() throws Exception {
        // Create the Vault with an existing ID
        vault.setId(1L);

        int databaseSizeBeforeCreate = vaultRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVaultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vault)))
            .andExpect(status().isBadRequest());

        // Validate the Vault in the database
        List<Vault> vaultList = vaultRepository.findAll();
        assertThat(vaultList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVaults() throws Exception {
        // Initialize the database
        vaultRepository.saveAndFlush(vault);

        // Get all the vaultList
        restVaultMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vault.getId().intValue())))
            .andExpect(jsonPath("$.[*].sourcePlaylistID").value(hasItem(DEFAULT_SOURCE_PLAYLIST_ID)))
            .andExpect(jsonPath("$.[*].playlistName").value(hasItem(DEFAULT_PLAYLIST_NAME)))
            .andExpect(jsonPath("$.[*].resultPlaylistID").value(hasItem(DEFAULT_RESULT_PLAYLIST_ID)))
            .andExpect(jsonPath("$.[*].frequency").value(hasItem(DEFAULT_FREQUENCY)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].playlistCoverURL").value(hasItem(DEFAULT_PLAYLIST_COVER_URL)))
            .andExpect(jsonPath("$.[*].playlistSnapshotID").value(hasItem(DEFAULT_PLAYLIST_SNAPSHOT_ID)));
    }

    @Test
    @Transactional
    void getVault() throws Exception {
        // Initialize the database
        vaultRepository.saveAndFlush(vault);

        // Get the vault
        restVaultMockMvc
            .perform(get(ENTITY_API_URL_ID, vault.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vault.getId().intValue()))
            .andExpect(jsonPath("$.sourcePlaylistID").value(DEFAULT_SOURCE_PLAYLIST_ID))
            .andExpect(jsonPath("$.playlistName").value(DEFAULT_PLAYLIST_NAME))
            .andExpect(jsonPath("$.resultPlaylistID").value(DEFAULT_RESULT_PLAYLIST_ID))
            .andExpect(jsonPath("$.frequency").value(DEFAULT_FREQUENCY))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.playlistCoverURL").value(DEFAULT_PLAYLIST_COVER_URL))
            .andExpect(jsonPath("$.playlistSnapshotID").value(DEFAULT_PLAYLIST_SNAPSHOT_ID));
    }

    @Test
    @Transactional
    void getNonExistingVault() throws Exception {
        // Get the vault
        restVaultMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVault() throws Exception {
        // Initialize the database
        vaultRepository.saveAndFlush(vault);

        int databaseSizeBeforeUpdate = vaultRepository.findAll().size();

        // Update the vault
        Vault updatedVault = vaultRepository.findById(vault.getId()).get();
        // Disconnect from session so that the updates on updatedVault are not directly saved in db
        em.detach(updatedVault);
        updatedVault
            .sourcePlaylistID(UPDATED_SOURCE_PLAYLIST_ID)
            .playlistName(UPDATED_PLAYLIST_NAME)
            .resultPlaylistID(UPDATED_RESULT_PLAYLIST_ID)
            .frequency(UPDATED_FREQUENCY)
            .type(UPDATED_TYPE)
            .playlistCoverURL(UPDATED_PLAYLIST_COVER_URL)
            .playlistSnapshotID(UPDATED_PLAYLIST_SNAPSHOT_ID);

        restVaultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVault.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVault))
            )
            .andExpect(status().isOk());

        // Validate the Vault in the database
        List<Vault> vaultList = vaultRepository.findAll();
        assertThat(vaultList).hasSize(databaseSizeBeforeUpdate);
        Vault testVault = vaultList.get(vaultList.size() - 1);
        assertThat(testVault.getSourcePlaylistID()).isEqualTo(UPDATED_SOURCE_PLAYLIST_ID);
        assertThat(testVault.getPlaylistName()).isEqualTo(UPDATED_PLAYLIST_NAME);
        assertThat(testVault.getResultPlaylistID()).isEqualTo(UPDATED_RESULT_PLAYLIST_ID);
        assertThat(testVault.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
        assertThat(testVault.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testVault.getPlaylistCoverURL()).isEqualTo(UPDATED_PLAYLIST_COVER_URL);
        assertThat(testVault.getPlaylistSnapshotID()).isEqualTo(UPDATED_PLAYLIST_SNAPSHOT_ID);
    }

    @Test
    @Transactional
    void putNonExistingVault() throws Exception {
        int databaseSizeBeforeUpdate = vaultRepository.findAll().size();
        vault.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVaultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vault.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vault))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vault in the database
        List<Vault> vaultList = vaultRepository.findAll();
        assertThat(vaultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVault() throws Exception {
        int databaseSizeBeforeUpdate = vaultRepository.findAll().size();
        vault.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vault))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vault in the database
        List<Vault> vaultList = vaultRepository.findAll();
        assertThat(vaultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVault() throws Exception {
        int databaseSizeBeforeUpdate = vaultRepository.findAll().size();
        vault.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaultMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vault)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vault in the database
        List<Vault> vaultList = vaultRepository.findAll();
        assertThat(vaultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVaultWithPatch() throws Exception {
        // Initialize the database
        vaultRepository.saveAndFlush(vault);

        int databaseSizeBeforeUpdate = vaultRepository.findAll().size();

        // Update the vault using partial update
        Vault partialUpdatedVault = new Vault();
        partialUpdatedVault.setId(vault.getId());

        partialUpdatedVault
            .sourcePlaylistID(UPDATED_SOURCE_PLAYLIST_ID)
            .resultPlaylistID(UPDATED_RESULT_PLAYLIST_ID)
            .frequency(UPDATED_FREQUENCY)
            .playlistCoverURL(UPDATED_PLAYLIST_COVER_URL);

        restVaultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVault.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVault))
            )
            .andExpect(status().isOk());

        // Validate the Vault in the database
        List<Vault> vaultList = vaultRepository.findAll();
        assertThat(vaultList).hasSize(databaseSizeBeforeUpdate);
        Vault testVault = vaultList.get(vaultList.size() - 1);
        assertThat(testVault.getSourcePlaylistID()).isEqualTo(UPDATED_SOURCE_PLAYLIST_ID);
        assertThat(testVault.getPlaylistName()).isEqualTo(DEFAULT_PLAYLIST_NAME);
        assertThat(testVault.getResultPlaylistID()).isEqualTo(UPDATED_RESULT_PLAYLIST_ID);
        assertThat(testVault.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
        assertThat(testVault.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testVault.getPlaylistCoverURL()).isEqualTo(UPDATED_PLAYLIST_COVER_URL);
        assertThat(testVault.getPlaylistSnapshotID()).isEqualTo(DEFAULT_PLAYLIST_SNAPSHOT_ID);
    }

    @Test
    @Transactional
    void fullUpdateVaultWithPatch() throws Exception {
        // Initialize the database
        vaultRepository.saveAndFlush(vault);

        int databaseSizeBeforeUpdate = vaultRepository.findAll().size();

        // Update the vault using partial update
        Vault partialUpdatedVault = new Vault();
        partialUpdatedVault.setId(vault.getId());

        partialUpdatedVault
            .sourcePlaylistID(UPDATED_SOURCE_PLAYLIST_ID)
            .playlistName(UPDATED_PLAYLIST_NAME)
            .resultPlaylistID(UPDATED_RESULT_PLAYLIST_ID)
            .frequency(UPDATED_FREQUENCY)
            .type(UPDATED_TYPE)
            .playlistCoverURL(UPDATED_PLAYLIST_COVER_URL)
            .playlistSnapshotID(UPDATED_PLAYLIST_SNAPSHOT_ID);

        restVaultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVault.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVault))
            )
            .andExpect(status().isOk());

        // Validate the Vault in the database
        List<Vault> vaultList = vaultRepository.findAll();
        assertThat(vaultList).hasSize(databaseSizeBeforeUpdate);
        Vault testVault = vaultList.get(vaultList.size() - 1);
        assertThat(testVault.getSourcePlaylistID()).isEqualTo(UPDATED_SOURCE_PLAYLIST_ID);
        assertThat(testVault.getPlaylistName()).isEqualTo(UPDATED_PLAYLIST_NAME);
        assertThat(testVault.getResultPlaylistID()).isEqualTo(UPDATED_RESULT_PLAYLIST_ID);
        assertThat(testVault.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
        assertThat(testVault.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testVault.getPlaylistCoverURL()).isEqualTo(UPDATED_PLAYLIST_COVER_URL);
        assertThat(testVault.getPlaylistSnapshotID()).isEqualTo(UPDATED_PLAYLIST_SNAPSHOT_ID);
    }

    @Test
    @Transactional
    void patchNonExistingVault() throws Exception {
        int databaseSizeBeforeUpdate = vaultRepository.findAll().size();
        vault.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVaultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vault.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vault))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vault in the database
        List<Vault> vaultList = vaultRepository.findAll();
        assertThat(vaultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVault() throws Exception {
        int databaseSizeBeforeUpdate = vaultRepository.findAll().size();
        vault.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vault))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vault in the database
        List<Vault> vaultList = vaultRepository.findAll();
        assertThat(vaultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVault() throws Exception {
        int databaseSizeBeforeUpdate = vaultRepository.findAll().size();
        vault.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaultMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vault)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vault in the database
        List<Vault> vaultList = vaultRepository.findAll();
        assertThat(vaultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVault() throws Exception {
        // Initialize the database
        vaultRepository.saveAndFlush(vault);

        int databaseSizeBeforeDelete = vaultRepository.findAll().size();

        // Delete the vault
        restVaultMockMvc
            .perform(delete(ENTITY_API_URL_ID, vault.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vault> vaultList = vaultRepository.findAll();
        assertThat(vaultList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

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
import team.bham.domain.SpotifyExchangeCode;
import team.bham.repository.SpotifyExchangeCodeRepository;

/**
 * Integration tests for the {@link SpotifyExchangeCodeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SpotifyExchangeCodeResourceIT {

    private static final String ENTITY_API_URL = "/api/spotify-exchange-codes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SpotifyExchangeCodeRepository spotifyExchangeCodeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpotifyExchangeCodeMockMvc;

    private SpotifyExchangeCode spotifyExchangeCode;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpotifyExchangeCode createEntity(EntityManager em) {
        SpotifyExchangeCode spotifyExchangeCode = new SpotifyExchangeCode();
        return spotifyExchangeCode;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpotifyExchangeCode createUpdatedEntity(EntityManager em) {
        SpotifyExchangeCode spotifyExchangeCode = new SpotifyExchangeCode();
        return spotifyExchangeCode;
    }

    @BeforeEach
    public void initTest() {
        spotifyExchangeCode = createEntity(em);
    }

    @Test
    @Transactional
    void createSpotifyExchangeCode() throws Exception {
        int databaseSizeBeforeCreate = spotifyExchangeCodeRepository.findAll().size();
        // Create the SpotifyExchangeCode
        restSpotifyExchangeCodeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spotifyExchangeCode))
            )
            .andExpect(status().isCreated());

        // Validate the SpotifyExchangeCode in the database
        List<SpotifyExchangeCode> spotifyExchangeCodeList = spotifyExchangeCodeRepository.findAll();
        assertThat(spotifyExchangeCodeList).hasSize(databaseSizeBeforeCreate + 1);
        SpotifyExchangeCode testSpotifyExchangeCode = spotifyExchangeCodeList.get(spotifyExchangeCodeList.size() - 1);
    }

    @Test
    @Transactional
    void createSpotifyExchangeCodeWithExistingId() throws Exception {
        // Create the SpotifyExchangeCode with an existing ID
        spotifyExchangeCode.setId(1L);

        int databaseSizeBeforeCreate = spotifyExchangeCodeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpotifyExchangeCodeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spotifyExchangeCode))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpotifyExchangeCode in the database
        List<SpotifyExchangeCode> spotifyExchangeCodeList = spotifyExchangeCodeRepository.findAll();
        assertThat(spotifyExchangeCodeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSpotifyExchangeCodes() throws Exception {
        // Initialize the database
        spotifyExchangeCodeRepository.saveAndFlush(spotifyExchangeCode);

        // Get all the spotifyExchangeCodeList
        restSpotifyExchangeCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(spotifyExchangeCode.getId().intValue())));
    }

    @Test
    @Transactional
    void getSpotifyExchangeCode() throws Exception {
        // Initialize the database
        spotifyExchangeCodeRepository.saveAndFlush(spotifyExchangeCode);

        // Get the spotifyExchangeCode
        restSpotifyExchangeCodeMockMvc
            .perform(get(ENTITY_API_URL_ID, spotifyExchangeCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(spotifyExchangeCode.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingSpotifyExchangeCode() throws Exception {
        // Get the spotifyExchangeCode
        restSpotifyExchangeCodeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSpotifyExchangeCode() throws Exception {
        // Initialize the database
        spotifyExchangeCodeRepository.saveAndFlush(spotifyExchangeCode);

        int databaseSizeBeforeUpdate = spotifyExchangeCodeRepository.findAll().size();

        // Update the spotifyExchangeCode
        SpotifyExchangeCode updatedSpotifyExchangeCode = spotifyExchangeCodeRepository.findById(spotifyExchangeCode.getId()).get();
        // Disconnect from session so that the updates on updatedSpotifyExchangeCode are not directly saved in db
        em.detach(updatedSpotifyExchangeCode);

        restSpotifyExchangeCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSpotifyExchangeCode.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSpotifyExchangeCode))
            )
            .andExpect(status().isOk());

        // Validate the SpotifyExchangeCode in the database
        List<SpotifyExchangeCode> spotifyExchangeCodeList = spotifyExchangeCodeRepository.findAll();
        assertThat(spotifyExchangeCodeList).hasSize(databaseSizeBeforeUpdate);
        SpotifyExchangeCode testSpotifyExchangeCode = spotifyExchangeCodeList.get(spotifyExchangeCodeList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingSpotifyExchangeCode() throws Exception {
        int databaseSizeBeforeUpdate = spotifyExchangeCodeRepository.findAll().size();
        spotifyExchangeCode.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpotifyExchangeCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, spotifyExchangeCode.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(spotifyExchangeCode))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpotifyExchangeCode in the database
        List<SpotifyExchangeCode> spotifyExchangeCodeList = spotifyExchangeCodeRepository.findAll();
        assertThat(spotifyExchangeCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpotifyExchangeCode() throws Exception {
        int databaseSizeBeforeUpdate = spotifyExchangeCodeRepository.findAll().size();
        spotifyExchangeCode.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpotifyExchangeCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(spotifyExchangeCode))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpotifyExchangeCode in the database
        List<SpotifyExchangeCode> spotifyExchangeCodeList = spotifyExchangeCodeRepository.findAll();
        assertThat(spotifyExchangeCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpotifyExchangeCode() throws Exception {
        int databaseSizeBeforeUpdate = spotifyExchangeCodeRepository.findAll().size();
        spotifyExchangeCode.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpotifyExchangeCodeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spotifyExchangeCode))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SpotifyExchangeCode in the database
        List<SpotifyExchangeCode> spotifyExchangeCodeList = spotifyExchangeCodeRepository.findAll();
        assertThat(spotifyExchangeCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSpotifyExchangeCodeWithPatch() throws Exception {
        // Initialize the database
        spotifyExchangeCodeRepository.saveAndFlush(spotifyExchangeCode);

        int databaseSizeBeforeUpdate = spotifyExchangeCodeRepository.findAll().size();

        // Update the spotifyExchangeCode using partial update
        SpotifyExchangeCode partialUpdatedSpotifyExchangeCode = new SpotifyExchangeCode();
        partialUpdatedSpotifyExchangeCode.setId(spotifyExchangeCode.getId());

        restSpotifyExchangeCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpotifyExchangeCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpotifyExchangeCode))
            )
            .andExpect(status().isOk());

        // Validate the SpotifyExchangeCode in the database
        List<SpotifyExchangeCode> spotifyExchangeCodeList = spotifyExchangeCodeRepository.findAll();
        assertThat(spotifyExchangeCodeList).hasSize(databaseSizeBeforeUpdate);
        SpotifyExchangeCode testSpotifyExchangeCode = spotifyExchangeCodeList.get(spotifyExchangeCodeList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateSpotifyExchangeCodeWithPatch() throws Exception {
        // Initialize the database
        spotifyExchangeCodeRepository.saveAndFlush(spotifyExchangeCode);

        int databaseSizeBeforeUpdate = spotifyExchangeCodeRepository.findAll().size();

        // Update the spotifyExchangeCode using partial update
        SpotifyExchangeCode partialUpdatedSpotifyExchangeCode = new SpotifyExchangeCode();
        partialUpdatedSpotifyExchangeCode.setId(spotifyExchangeCode.getId());

        restSpotifyExchangeCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpotifyExchangeCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpotifyExchangeCode))
            )
            .andExpect(status().isOk());

        // Validate the SpotifyExchangeCode in the database
        List<SpotifyExchangeCode> spotifyExchangeCodeList = spotifyExchangeCodeRepository.findAll();
        assertThat(spotifyExchangeCodeList).hasSize(databaseSizeBeforeUpdate);
        SpotifyExchangeCode testSpotifyExchangeCode = spotifyExchangeCodeList.get(spotifyExchangeCodeList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingSpotifyExchangeCode() throws Exception {
        int databaseSizeBeforeUpdate = spotifyExchangeCodeRepository.findAll().size();
        spotifyExchangeCode.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpotifyExchangeCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, spotifyExchangeCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(spotifyExchangeCode))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpotifyExchangeCode in the database
        List<SpotifyExchangeCode> spotifyExchangeCodeList = spotifyExchangeCodeRepository.findAll();
        assertThat(spotifyExchangeCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpotifyExchangeCode() throws Exception {
        int databaseSizeBeforeUpdate = spotifyExchangeCodeRepository.findAll().size();
        spotifyExchangeCode.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpotifyExchangeCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(spotifyExchangeCode))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpotifyExchangeCode in the database
        List<SpotifyExchangeCode> spotifyExchangeCodeList = spotifyExchangeCodeRepository.findAll();
        assertThat(spotifyExchangeCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpotifyExchangeCode() throws Exception {
        int databaseSizeBeforeUpdate = spotifyExchangeCodeRepository.findAll().size();
        spotifyExchangeCode.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpotifyExchangeCodeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(spotifyExchangeCode))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SpotifyExchangeCode in the database
        List<SpotifyExchangeCode> spotifyExchangeCodeList = spotifyExchangeCodeRepository.findAll();
        assertThat(spotifyExchangeCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSpotifyExchangeCode() throws Exception {
        // Initialize the database
        spotifyExchangeCodeRepository.saveAndFlush(spotifyExchangeCode);

        int databaseSizeBeforeDelete = spotifyExchangeCodeRepository.findAll().size();

        // Delete the spotifyExchangeCode
        restSpotifyExchangeCodeMockMvc
            .perform(delete(ENTITY_API_URL_ID, spotifyExchangeCode.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SpotifyExchangeCode> spotifyExchangeCodeList = spotifyExchangeCodeRepository.findAll();
        assertThat(spotifyExchangeCodeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

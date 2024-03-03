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
import team.bham.domain.SpotifyGenreEntity;
import team.bham.repository.SpotifyGenreEntityRepository;

/**
 * Integration tests for the {@link SpotifyGenreEntityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SpotifyGenreEntityResourceIT {

    private static final String DEFAULT_SPOTIFY_GENRE = "AAAAAAAAAA";
    private static final String UPDATED_SPOTIFY_GENRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/spotify-genre-entities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SpotifyGenreEntityRepository spotifyGenreEntityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpotifyGenreEntityMockMvc;

    private SpotifyGenreEntity spotifyGenreEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpotifyGenreEntity createEntity(EntityManager em) {
        SpotifyGenreEntity spotifyGenreEntity = new SpotifyGenreEntity().spotifyGenre(DEFAULT_SPOTIFY_GENRE);
        return spotifyGenreEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpotifyGenreEntity createUpdatedEntity(EntityManager em) {
        SpotifyGenreEntity spotifyGenreEntity = new SpotifyGenreEntity().spotifyGenre(UPDATED_SPOTIFY_GENRE);
        return spotifyGenreEntity;
    }

    @BeforeEach
    public void initTest() {
        spotifyGenreEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createSpotifyGenreEntity() throws Exception {
        int databaseSizeBeforeCreate = spotifyGenreEntityRepository.findAll().size();
        // Create the SpotifyGenreEntity
        restSpotifyGenreEntityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spotifyGenreEntity))
            )
            .andExpect(status().isCreated());

        // Validate the SpotifyGenreEntity in the database
        List<SpotifyGenreEntity> spotifyGenreEntityList = spotifyGenreEntityRepository.findAll();
        assertThat(spotifyGenreEntityList).hasSize(databaseSizeBeforeCreate + 1);
        SpotifyGenreEntity testSpotifyGenreEntity = spotifyGenreEntityList.get(spotifyGenreEntityList.size() - 1);
        assertThat(testSpotifyGenreEntity.getSpotifyGenre()).isEqualTo(DEFAULT_SPOTIFY_GENRE);
    }

    @Test
    @Transactional
    void createSpotifyGenreEntityWithExistingId() throws Exception {
        // Create the SpotifyGenreEntity with an existing ID
        spotifyGenreEntity.setId(1L);

        int databaseSizeBeforeCreate = spotifyGenreEntityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpotifyGenreEntityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spotifyGenreEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpotifyGenreEntity in the database
        List<SpotifyGenreEntity> spotifyGenreEntityList = spotifyGenreEntityRepository.findAll();
        assertThat(spotifyGenreEntityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSpotifyGenreIsRequired() throws Exception {
        int databaseSizeBeforeTest = spotifyGenreEntityRepository.findAll().size();
        // set the field null
        spotifyGenreEntity.setSpotifyGenre(null);

        // Create the SpotifyGenreEntity, which fails.

        restSpotifyGenreEntityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spotifyGenreEntity))
            )
            .andExpect(status().isBadRequest());

        List<SpotifyGenreEntity> spotifyGenreEntityList = spotifyGenreEntityRepository.findAll();
        assertThat(spotifyGenreEntityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSpotifyGenreEntities() throws Exception {
        // Initialize the database
        spotifyGenreEntityRepository.saveAndFlush(spotifyGenreEntity);

        // Get all the spotifyGenreEntityList
        restSpotifyGenreEntityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(spotifyGenreEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].spotifyGenre").value(hasItem(DEFAULT_SPOTIFY_GENRE)));
    }

    @Test
    @Transactional
    void getSpotifyGenreEntity() throws Exception {
        // Initialize the database
        spotifyGenreEntityRepository.saveAndFlush(spotifyGenreEntity);

        // Get the spotifyGenreEntity
        restSpotifyGenreEntityMockMvc
            .perform(get(ENTITY_API_URL_ID, spotifyGenreEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(spotifyGenreEntity.getId().intValue()))
            .andExpect(jsonPath("$.spotifyGenre").value(DEFAULT_SPOTIFY_GENRE));
    }

    @Test
    @Transactional
    void getNonExistingSpotifyGenreEntity() throws Exception {
        // Get the spotifyGenreEntity
        restSpotifyGenreEntityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSpotifyGenreEntity() throws Exception {
        // Initialize the database
        spotifyGenreEntityRepository.saveAndFlush(spotifyGenreEntity);

        int databaseSizeBeforeUpdate = spotifyGenreEntityRepository.findAll().size();

        // Update the spotifyGenreEntity
        SpotifyGenreEntity updatedSpotifyGenreEntity = spotifyGenreEntityRepository.findById(spotifyGenreEntity.getId()).get();
        // Disconnect from session so that the updates on updatedSpotifyGenreEntity are not directly saved in db
        em.detach(updatedSpotifyGenreEntity);
        updatedSpotifyGenreEntity.spotifyGenre(UPDATED_SPOTIFY_GENRE);

        restSpotifyGenreEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSpotifyGenreEntity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSpotifyGenreEntity))
            )
            .andExpect(status().isOk());

        // Validate the SpotifyGenreEntity in the database
        List<SpotifyGenreEntity> spotifyGenreEntityList = spotifyGenreEntityRepository.findAll();
        assertThat(spotifyGenreEntityList).hasSize(databaseSizeBeforeUpdate);
        SpotifyGenreEntity testSpotifyGenreEntity = spotifyGenreEntityList.get(spotifyGenreEntityList.size() - 1);
        assertThat(testSpotifyGenreEntity.getSpotifyGenre()).isEqualTo(UPDATED_SPOTIFY_GENRE);
    }

    @Test
    @Transactional
    void putNonExistingSpotifyGenreEntity() throws Exception {
        int databaseSizeBeforeUpdate = spotifyGenreEntityRepository.findAll().size();
        spotifyGenreEntity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpotifyGenreEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, spotifyGenreEntity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(spotifyGenreEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpotifyGenreEntity in the database
        List<SpotifyGenreEntity> spotifyGenreEntityList = spotifyGenreEntityRepository.findAll();
        assertThat(spotifyGenreEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpotifyGenreEntity() throws Exception {
        int databaseSizeBeforeUpdate = spotifyGenreEntityRepository.findAll().size();
        spotifyGenreEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpotifyGenreEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(spotifyGenreEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpotifyGenreEntity in the database
        List<SpotifyGenreEntity> spotifyGenreEntityList = spotifyGenreEntityRepository.findAll();
        assertThat(spotifyGenreEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpotifyGenreEntity() throws Exception {
        int databaseSizeBeforeUpdate = spotifyGenreEntityRepository.findAll().size();
        spotifyGenreEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpotifyGenreEntityMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spotifyGenreEntity))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SpotifyGenreEntity in the database
        List<SpotifyGenreEntity> spotifyGenreEntityList = spotifyGenreEntityRepository.findAll();
        assertThat(spotifyGenreEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSpotifyGenreEntityWithPatch() throws Exception {
        // Initialize the database
        spotifyGenreEntityRepository.saveAndFlush(spotifyGenreEntity);

        int databaseSizeBeforeUpdate = spotifyGenreEntityRepository.findAll().size();

        // Update the spotifyGenreEntity using partial update
        SpotifyGenreEntity partialUpdatedSpotifyGenreEntity = new SpotifyGenreEntity();
        partialUpdatedSpotifyGenreEntity.setId(spotifyGenreEntity.getId());

        restSpotifyGenreEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpotifyGenreEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpotifyGenreEntity))
            )
            .andExpect(status().isOk());

        // Validate the SpotifyGenreEntity in the database
        List<SpotifyGenreEntity> spotifyGenreEntityList = spotifyGenreEntityRepository.findAll();
        assertThat(spotifyGenreEntityList).hasSize(databaseSizeBeforeUpdate);
        SpotifyGenreEntity testSpotifyGenreEntity = spotifyGenreEntityList.get(spotifyGenreEntityList.size() - 1);
        assertThat(testSpotifyGenreEntity.getSpotifyGenre()).isEqualTo(DEFAULT_SPOTIFY_GENRE);
    }

    @Test
    @Transactional
    void fullUpdateSpotifyGenreEntityWithPatch() throws Exception {
        // Initialize the database
        spotifyGenreEntityRepository.saveAndFlush(spotifyGenreEntity);

        int databaseSizeBeforeUpdate = spotifyGenreEntityRepository.findAll().size();

        // Update the spotifyGenreEntity using partial update
        SpotifyGenreEntity partialUpdatedSpotifyGenreEntity = new SpotifyGenreEntity();
        partialUpdatedSpotifyGenreEntity.setId(spotifyGenreEntity.getId());

        partialUpdatedSpotifyGenreEntity.spotifyGenre(UPDATED_SPOTIFY_GENRE);

        restSpotifyGenreEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpotifyGenreEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpotifyGenreEntity))
            )
            .andExpect(status().isOk());

        // Validate the SpotifyGenreEntity in the database
        List<SpotifyGenreEntity> spotifyGenreEntityList = spotifyGenreEntityRepository.findAll();
        assertThat(spotifyGenreEntityList).hasSize(databaseSizeBeforeUpdate);
        SpotifyGenreEntity testSpotifyGenreEntity = spotifyGenreEntityList.get(spotifyGenreEntityList.size() - 1);
        assertThat(testSpotifyGenreEntity.getSpotifyGenre()).isEqualTo(UPDATED_SPOTIFY_GENRE);
    }

    @Test
    @Transactional
    void patchNonExistingSpotifyGenreEntity() throws Exception {
        int databaseSizeBeforeUpdate = spotifyGenreEntityRepository.findAll().size();
        spotifyGenreEntity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpotifyGenreEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, spotifyGenreEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(spotifyGenreEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpotifyGenreEntity in the database
        List<SpotifyGenreEntity> spotifyGenreEntityList = spotifyGenreEntityRepository.findAll();
        assertThat(spotifyGenreEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpotifyGenreEntity() throws Exception {
        int databaseSizeBeforeUpdate = spotifyGenreEntityRepository.findAll().size();
        spotifyGenreEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpotifyGenreEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(spotifyGenreEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpotifyGenreEntity in the database
        List<SpotifyGenreEntity> spotifyGenreEntityList = spotifyGenreEntityRepository.findAll();
        assertThat(spotifyGenreEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpotifyGenreEntity() throws Exception {
        int databaseSizeBeforeUpdate = spotifyGenreEntityRepository.findAll().size();
        spotifyGenreEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpotifyGenreEntityMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(spotifyGenreEntity))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SpotifyGenreEntity in the database
        List<SpotifyGenreEntity> spotifyGenreEntityList = spotifyGenreEntityRepository.findAll();
        assertThat(spotifyGenreEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSpotifyGenreEntity() throws Exception {
        // Initialize the database
        spotifyGenreEntityRepository.saveAndFlush(spotifyGenreEntity);

        int databaseSizeBeforeDelete = spotifyGenreEntityRepository.findAll().size();

        // Delete the spotifyGenreEntity
        restSpotifyGenreEntityMockMvc
            .perform(delete(ENTITY_API_URL_ID, spotifyGenreEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SpotifyGenreEntity> spotifyGenreEntityList = spotifyGenreEntityRepository.findAll();
        assertThat(spotifyGenreEntityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

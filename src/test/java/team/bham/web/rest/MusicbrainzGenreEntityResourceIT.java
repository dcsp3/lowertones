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
import team.bham.domain.MusicbrainzGenreEntity;
import team.bham.repository.MusicbrainzGenreEntityRepository;

/**
 * Integration tests for the {@link MusicbrainzGenreEntityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MusicbrainzGenreEntityResourceIT {

    private static final String DEFAULT_MUSICBRAINZ_GENRE = "AAAAAAAAAA";
    private static final String UPDATED_MUSICBRAINZ_GENRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/musicbrainz-genre-entities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MusicbrainzGenreEntityRepository musicbrainzGenreEntityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMusicbrainzGenreEntityMockMvc;

    private MusicbrainzGenreEntity musicbrainzGenreEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MusicbrainzGenreEntity createEntity(EntityManager em) {
        MusicbrainzGenreEntity musicbrainzGenreEntity = new MusicbrainzGenreEntity().musicbrainzGenre(DEFAULT_MUSICBRAINZ_GENRE);
        return musicbrainzGenreEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MusicbrainzGenreEntity createUpdatedEntity(EntityManager em) {
        MusicbrainzGenreEntity musicbrainzGenreEntity = new MusicbrainzGenreEntity().musicbrainzGenre(UPDATED_MUSICBRAINZ_GENRE);
        return musicbrainzGenreEntity;
    }

    @BeforeEach
    public void initTest() {
        musicbrainzGenreEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createMusicbrainzGenreEntity() throws Exception {
        int databaseSizeBeforeCreate = musicbrainzGenreEntityRepository.findAll().size();
        // Create the MusicbrainzGenreEntity
        restMusicbrainzGenreEntityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(musicbrainzGenreEntity))
            )
            .andExpect(status().isCreated());

        // Validate the MusicbrainzGenreEntity in the database
        List<MusicbrainzGenreEntity> musicbrainzGenreEntityList = musicbrainzGenreEntityRepository.findAll();
        assertThat(musicbrainzGenreEntityList).hasSize(databaseSizeBeforeCreate + 1);
        MusicbrainzGenreEntity testMusicbrainzGenreEntity = musicbrainzGenreEntityList.get(musicbrainzGenreEntityList.size() - 1);
        assertThat(testMusicbrainzGenreEntity.getMusicbrainzGenre()).isEqualTo(DEFAULT_MUSICBRAINZ_GENRE);
    }

    @Test
    @Transactional
    void createMusicbrainzGenreEntityWithExistingId() throws Exception {
        // Create the MusicbrainzGenreEntity with an existing ID
        musicbrainzGenreEntity.setId(1L);

        int databaseSizeBeforeCreate = musicbrainzGenreEntityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMusicbrainzGenreEntityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(musicbrainzGenreEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the MusicbrainzGenreEntity in the database
        List<MusicbrainzGenreEntity> musicbrainzGenreEntityList = musicbrainzGenreEntityRepository.findAll();
        assertThat(musicbrainzGenreEntityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMusicbrainzGenreIsRequired() throws Exception {
        int databaseSizeBeforeTest = musicbrainzGenreEntityRepository.findAll().size();
        // set the field null
        musicbrainzGenreEntity.setMusicbrainzGenre(null);

        // Create the MusicbrainzGenreEntity, which fails.

        restMusicbrainzGenreEntityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(musicbrainzGenreEntity))
            )
            .andExpect(status().isBadRequest());

        List<MusicbrainzGenreEntity> musicbrainzGenreEntityList = musicbrainzGenreEntityRepository.findAll();
        assertThat(musicbrainzGenreEntityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMusicbrainzGenreEntities() throws Exception {
        // Initialize the database
        musicbrainzGenreEntityRepository.saveAndFlush(musicbrainzGenreEntity);

        // Get all the musicbrainzGenreEntityList
        restMusicbrainzGenreEntityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(musicbrainzGenreEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].musicbrainzGenre").value(hasItem(DEFAULT_MUSICBRAINZ_GENRE)));
    }

    @Test
    @Transactional
    void getMusicbrainzGenreEntity() throws Exception {
        // Initialize the database
        musicbrainzGenreEntityRepository.saveAndFlush(musicbrainzGenreEntity);

        // Get the musicbrainzGenreEntity
        restMusicbrainzGenreEntityMockMvc
            .perform(get(ENTITY_API_URL_ID, musicbrainzGenreEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(musicbrainzGenreEntity.getId().intValue()))
            .andExpect(jsonPath("$.musicbrainzGenre").value(DEFAULT_MUSICBRAINZ_GENRE));
    }

    @Test
    @Transactional
    void getNonExistingMusicbrainzGenreEntity() throws Exception {
        // Get the musicbrainzGenreEntity
        restMusicbrainzGenreEntityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMusicbrainzGenreEntity() throws Exception {
        // Initialize the database
        musicbrainzGenreEntityRepository.saveAndFlush(musicbrainzGenreEntity);

        int databaseSizeBeforeUpdate = musicbrainzGenreEntityRepository.findAll().size();

        // Update the musicbrainzGenreEntity
        MusicbrainzGenreEntity updatedMusicbrainzGenreEntity = musicbrainzGenreEntityRepository
            .findById(musicbrainzGenreEntity.getId())
            .get();
        // Disconnect from session so that the updates on updatedMusicbrainzGenreEntity are not directly saved in db
        em.detach(updatedMusicbrainzGenreEntity);
        updatedMusicbrainzGenreEntity.musicbrainzGenre(UPDATED_MUSICBRAINZ_GENRE);

        restMusicbrainzGenreEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMusicbrainzGenreEntity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMusicbrainzGenreEntity))
            )
            .andExpect(status().isOk());

        // Validate the MusicbrainzGenreEntity in the database
        List<MusicbrainzGenreEntity> musicbrainzGenreEntityList = musicbrainzGenreEntityRepository.findAll();
        assertThat(musicbrainzGenreEntityList).hasSize(databaseSizeBeforeUpdate);
        MusicbrainzGenreEntity testMusicbrainzGenreEntity = musicbrainzGenreEntityList.get(musicbrainzGenreEntityList.size() - 1);
        assertThat(testMusicbrainzGenreEntity.getMusicbrainzGenre()).isEqualTo(UPDATED_MUSICBRAINZ_GENRE);
    }

    @Test
    @Transactional
    void putNonExistingMusicbrainzGenreEntity() throws Exception {
        int databaseSizeBeforeUpdate = musicbrainzGenreEntityRepository.findAll().size();
        musicbrainzGenreEntity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMusicbrainzGenreEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, musicbrainzGenreEntity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(musicbrainzGenreEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the MusicbrainzGenreEntity in the database
        List<MusicbrainzGenreEntity> musicbrainzGenreEntityList = musicbrainzGenreEntityRepository.findAll();
        assertThat(musicbrainzGenreEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMusicbrainzGenreEntity() throws Exception {
        int databaseSizeBeforeUpdate = musicbrainzGenreEntityRepository.findAll().size();
        musicbrainzGenreEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMusicbrainzGenreEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(musicbrainzGenreEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the MusicbrainzGenreEntity in the database
        List<MusicbrainzGenreEntity> musicbrainzGenreEntityList = musicbrainzGenreEntityRepository.findAll();
        assertThat(musicbrainzGenreEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMusicbrainzGenreEntity() throws Exception {
        int databaseSizeBeforeUpdate = musicbrainzGenreEntityRepository.findAll().size();
        musicbrainzGenreEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMusicbrainzGenreEntityMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(musicbrainzGenreEntity))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MusicbrainzGenreEntity in the database
        List<MusicbrainzGenreEntity> musicbrainzGenreEntityList = musicbrainzGenreEntityRepository.findAll();
        assertThat(musicbrainzGenreEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMusicbrainzGenreEntityWithPatch() throws Exception {
        // Initialize the database
        musicbrainzGenreEntityRepository.saveAndFlush(musicbrainzGenreEntity);

        int databaseSizeBeforeUpdate = musicbrainzGenreEntityRepository.findAll().size();

        // Update the musicbrainzGenreEntity using partial update
        MusicbrainzGenreEntity partialUpdatedMusicbrainzGenreEntity = new MusicbrainzGenreEntity();
        partialUpdatedMusicbrainzGenreEntity.setId(musicbrainzGenreEntity.getId());

        partialUpdatedMusicbrainzGenreEntity.musicbrainzGenre(UPDATED_MUSICBRAINZ_GENRE);

        restMusicbrainzGenreEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMusicbrainzGenreEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMusicbrainzGenreEntity))
            )
            .andExpect(status().isOk());

        // Validate the MusicbrainzGenreEntity in the database
        List<MusicbrainzGenreEntity> musicbrainzGenreEntityList = musicbrainzGenreEntityRepository.findAll();
        assertThat(musicbrainzGenreEntityList).hasSize(databaseSizeBeforeUpdate);
        MusicbrainzGenreEntity testMusicbrainzGenreEntity = musicbrainzGenreEntityList.get(musicbrainzGenreEntityList.size() - 1);
        assertThat(testMusicbrainzGenreEntity.getMusicbrainzGenre()).isEqualTo(UPDATED_MUSICBRAINZ_GENRE);
    }

    @Test
    @Transactional
    void fullUpdateMusicbrainzGenreEntityWithPatch() throws Exception {
        // Initialize the database
        musicbrainzGenreEntityRepository.saveAndFlush(musicbrainzGenreEntity);

        int databaseSizeBeforeUpdate = musicbrainzGenreEntityRepository.findAll().size();

        // Update the musicbrainzGenreEntity using partial update
        MusicbrainzGenreEntity partialUpdatedMusicbrainzGenreEntity = new MusicbrainzGenreEntity();
        partialUpdatedMusicbrainzGenreEntity.setId(musicbrainzGenreEntity.getId());

        partialUpdatedMusicbrainzGenreEntity.musicbrainzGenre(UPDATED_MUSICBRAINZ_GENRE);

        restMusicbrainzGenreEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMusicbrainzGenreEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMusicbrainzGenreEntity))
            )
            .andExpect(status().isOk());

        // Validate the MusicbrainzGenreEntity in the database
        List<MusicbrainzGenreEntity> musicbrainzGenreEntityList = musicbrainzGenreEntityRepository.findAll();
        assertThat(musicbrainzGenreEntityList).hasSize(databaseSizeBeforeUpdate);
        MusicbrainzGenreEntity testMusicbrainzGenreEntity = musicbrainzGenreEntityList.get(musicbrainzGenreEntityList.size() - 1);
        assertThat(testMusicbrainzGenreEntity.getMusicbrainzGenre()).isEqualTo(UPDATED_MUSICBRAINZ_GENRE);
    }

    @Test
    @Transactional
    void patchNonExistingMusicbrainzGenreEntity() throws Exception {
        int databaseSizeBeforeUpdate = musicbrainzGenreEntityRepository.findAll().size();
        musicbrainzGenreEntity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMusicbrainzGenreEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, musicbrainzGenreEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(musicbrainzGenreEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the MusicbrainzGenreEntity in the database
        List<MusicbrainzGenreEntity> musicbrainzGenreEntityList = musicbrainzGenreEntityRepository.findAll();
        assertThat(musicbrainzGenreEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMusicbrainzGenreEntity() throws Exception {
        int databaseSizeBeforeUpdate = musicbrainzGenreEntityRepository.findAll().size();
        musicbrainzGenreEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMusicbrainzGenreEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(musicbrainzGenreEntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the MusicbrainzGenreEntity in the database
        List<MusicbrainzGenreEntity> musicbrainzGenreEntityList = musicbrainzGenreEntityRepository.findAll();
        assertThat(musicbrainzGenreEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMusicbrainzGenreEntity() throws Exception {
        int databaseSizeBeforeUpdate = musicbrainzGenreEntityRepository.findAll().size();
        musicbrainzGenreEntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMusicbrainzGenreEntityMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(musicbrainzGenreEntity))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MusicbrainzGenreEntity in the database
        List<MusicbrainzGenreEntity> musicbrainzGenreEntityList = musicbrainzGenreEntityRepository.findAll();
        assertThat(musicbrainzGenreEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMusicbrainzGenreEntity() throws Exception {
        // Initialize the database
        musicbrainzGenreEntityRepository.saveAndFlush(musicbrainzGenreEntity);

        int databaseSizeBeforeDelete = musicbrainzGenreEntityRepository.findAll().size();

        // Delete the musicbrainzGenreEntity
        restMusicbrainzGenreEntityMockMvc
            .perform(delete(ENTITY_API_URL_ID, musicbrainzGenreEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MusicbrainzGenreEntity> musicbrainzGenreEntityList = musicbrainzGenreEntityRepository.findAll();
        assertThat(musicbrainzGenreEntityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

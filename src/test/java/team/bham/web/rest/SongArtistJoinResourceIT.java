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
import team.bham.domain.SongArtistJoin;
import team.bham.repository.SongArtistJoinRepository;

/**
 * Integration tests for the {@link SongArtistJoinResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SongArtistJoinResourceIT {

    private static final Integer DEFAULT_TOP_TRACK_INDEX = 1;
    private static final Integer UPDATED_TOP_TRACK_INDEX = 2;

    private static final String ENTITY_API_URL = "/api/song-artist-joins";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SongArtistJoinRepository songArtistJoinRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSongArtistJoinMockMvc;

    private SongArtistJoin songArtistJoin;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SongArtistJoin createEntity(EntityManager em) {
        SongArtistJoin songArtistJoin = new SongArtistJoin().topTrackIndex(DEFAULT_TOP_TRACK_INDEX);
        return songArtistJoin;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SongArtistJoin createUpdatedEntity(EntityManager em) {
        SongArtistJoin songArtistJoin = new SongArtistJoin().topTrackIndex(UPDATED_TOP_TRACK_INDEX);
        return songArtistJoin;
    }

    @BeforeEach
    public void initTest() {
        songArtistJoin = createEntity(em);
    }

    @Test
    @Transactional
    void createSongArtistJoin() throws Exception {
        int databaseSizeBeforeCreate = songArtistJoinRepository.findAll().size();
        // Create the SongArtistJoin
        restSongArtistJoinMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(songArtistJoin))
            )
            .andExpect(status().isCreated());

        // Validate the SongArtistJoin in the database
        List<SongArtistJoin> songArtistJoinList = songArtistJoinRepository.findAll();
        assertThat(songArtistJoinList).hasSize(databaseSizeBeforeCreate + 1);
        SongArtistJoin testSongArtistJoin = songArtistJoinList.get(songArtistJoinList.size() - 1);
        assertThat(testSongArtistJoin.getTopTrackIndex()).isEqualTo(DEFAULT_TOP_TRACK_INDEX);
    }

    @Test
    @Transactional
    void createSongArtistJoinWithExistingId() throws Exception {
        // Create the SongArtistJoin with an existing ID
        songArtistJoin.setId(1L);

        int databaseSizeBeforeCreate = songArtistJoinRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSongArtistJoinMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(songArtistJoin))
            )
            .andExpect(status().isBadRequest());

        // Validate the SongArtistJoin in the database
        List<SongArtistJoin> songArtistJoinList = songArtistJoinRepository.findAll();
        assertThat(songArtistJoinList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSongArtistJoins() throws Exception {
        // Initialize the database
        songArtistJoinRepository.saveAndFlush(songArtistJoin);

        // Get all the songArtistJoinList
        restSongArtistJoinMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(songArtistJoin.getId().intValue())))
            .andExpect(jsonPath("$.[*].topTrackIndex").value(hasItem(DEFAULT_TOP_TRACK_INDEX)));
    }

    @Test
    @Transactional
    void getSongArtistJoin() throws Exception {
        // Initialize the database
        songArtistJoinRepository.saveAndFlush(songArtistJoin);

        // Get the songArtistJoin
        restSongArtistJoinMockMvc
            .perform(get(ENTITY_API_URL_ID, songArtistJoin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(songArtistJoin.getId().intValue()))
            .andExpect(jsonPath("$.topTrackIndex").value(DEFAULT_TOP_TRACK_INDEX));
    }

    @Test
    @Transactional
    void getNonExistingSongArtistJoin() throws Exception {
        // Get the songArtistJoin
        restSongArtistJoinMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSongArtistJoin() throws Exception {
        // Initialize the database
        songArtistJoinRepository.saveAndFlush(songArtistJoin);

        int databaseSizeBeforeUpdate = songArtistJoinRepository.findAll().size();

        // Update the songArtistJoin
        SongArtistJoin updatedSongArtistJoin = songArtistJoinRepository.findById(songArtistJoin.getId()).get();
        // Disconnect from session so that the updates on updatedSongArtistJoin are not directly saved in db
        em.detach(updatedSongArtistJoin);
        updatedSongArtistJoin.topTrackIndex(UPDATED_TOP_TRACK_INDEX);

        restSongArtistJoinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSongArtistJoin.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSongArtistJoin))
            )
            .andExpect(status().isOk());

        // Validate the SongArtistJoin in the database
        List<SongArtistJoin> songArtistJoinList = songArtistJoinRepository.findAll();
        assertThat(songArtistJoinList).hasSize(databaseSizeBeforeUpdate);
        SongArtistJoin testSongArtistJoin = songArtistJoinList.get(songArtistJoinList.size() - 1);
        assertThat(testSongArtistJoin.getTopTrackIndex()).isEqualTo(UPDATED_TOP_TRACK_INDEX);
    }

    @Test
    @Transactional
    void putNonExistingSongArtistJoin() throws Exception {
        int databaseSizeBeforeUpdate = songArtistJoinRepository.findAll().size();
        songArtistJoin.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSongArtistJoinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, songArtistJoin.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(songArtistJoin))
            )
            .andExpect(status().isBadRequest());

        // Validate the SongArtistJoin in the database
        List<SongArtistJoin> songArtistJoinList = songArtistJoinRepository.findAll();
        assertThat(songArtistJoinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSongArtistJoin() throws Exception {
        int databaseSizeBeforeUpdate = songArtistJoinRepository.findAll().size();
        songArtistJoin.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSongArtistJoinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(songArtistJoin))
            )
            .andExpect(status().isBadRequest());

        // Validate the SongArtistJoin in the database
        List<SongArtistJoin> songArtistJoinList = songArtistJoinRepository.findAll();
        assertThat(songArtistJoinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSongArtistJoin() throws Exception {
        int databaseSizeBeforeUpdate = songArtistJoinRepository.findAll().size();
        songArtistJoin.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSongArtistJoinMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(songArtistJoin)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SongArtistJoin in the database
        List<SongArtistJoin> songArtistJoinList = songArtistJoinRepository.findAll();
        assertThat(songArtistJoinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSongArtistJoinWithPatch() throws Exception {
        // Initialize the database
        songArtistJoinRepository.saveAndFlush(songArtistJoin);

        int databaseSizeBeforeUpdate = songArtistJoinRepository.findAll().size();

        // Update the songArtistJoin using partial update
        SongArtistJoin partialUpdatedSongArtistJoin = new SongArtistJoin();
        partialUpdatedSongArtistJoin.setId(songArtistJoin.getId());

        partialUpdatedSongArtistJoin.topTrackIndex(UPDATED_TOP_TRACK_INDEX);

        restSongArtistJoinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSongArtistJoin.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSongArtistJoin))
            )
            .andExpect(status().isOk());

        // Validate the SongArtistJoin in the database
        List<SongArtistJoin> songArtistJoinList = songArtistJoinRepository.findAll();
        assertThat(songArtistJoinList).hasSize(databaseSizeBeforeUpdate);
        SongArtistJoin testSongArtistJoin = songArtistJoinList.get(songArtistJoinList.size() - 1);
        assertThat(testSongArtistJoin.getTopTrackIndex()).isEqualTo(UPDATED_TOP_TRACK_INDEX);
    }

    @Test
    @Transactional
    void fullUpdateSongArtistJoinWithPatch() throws Exception {
        // Initialize the database
        songArtistJoinRepository.saveAndFlush(songArtistJoin);

        int databaseSizeBeforeUpdate = songArtistJoinRepository.findAll().size();

        // Update the songArtistJoin using partial update
        SongArtistJoin partialUpdatedSongArtistJoin = new SongArtistJoin();
        partialUpdatedSongArtistJoin.setId(songArtistJoin.getId());

        partialUpdatedSongArtistJoin.topTrackIndex(UPDATED_TOP_TRACK_INDEX);

        restSongArtistJoinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSongArtistJoin.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSongArtistJoin))
            )
            .andExpect(status().isOk());

        // Validate the SongArtistJoin in the database
        List<SongArtistJoin> songArtistJoinList = songArtistJoinRepository.findAll();
        assertThat(songArtistJoinList).hasSize(databaseSizeBeforeUpdate);
        SongArtistJoin testSongArtistJoin = songArtistJoinList.get(songArtistJoinList.size() - 1);
        assertThat(testSongArtistJoin.getTopTrackIndex()).isEqualTo(UPDATED_TOP_TRACK_INDEX);
    }

    @Test
    @Transactional
    void patchNonExistingSongArtistJoin() throws Exception {
        int databaseSizeBeforeUpdate = songArtistJoinRepository.findAll().size();
        songArtistJoin.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSongArtistJoinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, songArtistJoin.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(songArtistJoin))
            )
            .andExpect(status().isBadRequest());

        // Validate the SongArtistJoin in the database
        List<SongArtistJoin> songArtistJoinList = songArtistJoinRepository.findAll();
        assertThat(songArtistJoinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSongArtistJoin() throws Exception {
        int databaseSizeBeforeUpdate = songArtistJoinRepository.findAll().size();
        songArtistJoin.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSongArtistJoinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(songArtistJoin))
            )
            .andExpect(status().isBadRequest());

        // Validate the SongArtistJoin in the database
        List<SongArtistJoin> songArtistJoinList = songArtistJoinRepository.findAll();
        assertThat(songArtistJoinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSongArtistJoin() throws Exception {
        int databaseSizeBeforeUpdate = songArtistJoinRepository.findAll().size();
        songArtistJoin.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSongArtistJoinMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(songArtistJoin))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SongArtistJoin in the database
        List<SongArtistJoin> songArtistJoinList = songArtistJoinRepository.findAll();
        assertThat(songArtistJoinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSongArtistJoin() throws Exception {
        // Initialize the database
        songArtistJoinRepository.saveAndFlush(songArtistJoin);

        int databaseSizeBeforeDelete = songArtistJoinRepository.findAll().size();

        // Delete the songArtistJoin
        restSongArtistJoinMockMvc
            .perform(delete(ENTITY_API_URL_ID, songArtistJoin.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SongArtistJoin> songArtistJoinList = songArtistJoinRepository.findAll();
        assertThat(songArtistJoinList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package team.bham.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
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
import team.bham.domain.PlaylistSongJoin;
import team.bham.repository.PlaylistSongJoinRepository;

/**
 * Integration tests for the {@link PlaylistSongJoinResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PlaylistSongJoinResourceIT {

    private static final Integer DEFAULT_SONG_ORDER_INDEX = 1;
    private static final Integer UPDATED_SONG_ORDER_INDEX = 2;

    private static final LocalDate DEFAULT_SONG_DATE_ADDED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SONG_DATE_ADDED = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/playlist-song-joins";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PlaylistSongJoinRepository playlistSongJoinRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlaylistSongJoinMockMvc;

    private PlaylistSongJoin playlistSongJoin;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlaylistSongJoin createEntity(EntityManager em) {
        PlaylistSongJoin playlistSongJoin = new PlaylistSongJoin()
            .songOrderIndex(DEFAULT_SONG_ORDER_INDEX)
            .songDateAdded(DEFAULT_SONG_DATE_ADDED);
        return playlistSongJoin;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlaylistSongJoin createUpdatedEntity(EntityManager em) {
        PlaylistSongJoin playlistSongJoin = new PlaylistSongJoin()
            .songOrderIndex(UPDATED_SONG_ORDER_INDEX)
            .songDateAdded(UPDATED_SONG_DATE_ADDED);
        return playlistSongJoin;
    }

    @BeforeEach
    public void initTest() {
        playlistSongJoin = createEntity(em);
    }

    @Test
    @Transactional
    void createPlaylistSongJoin() throws Exception {
        int databaseSizeBeforeCreate = playlistSongJoinRepository.findAll().size();
        // Create the PlaylistSongJoin
        restPlaylistSongJoinMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(playlistSongJoin))
            )
            .andExpect(status().isCreated());

        // Validate the PlaylistSongJoin in the database
        List<PlaylistSongJoin> playlistSongJoinList = playlistSongJoinRepository.findAll();
        assertThat(playlistSongJoinList).hasSize(databaseSizeBeforeCreate + 1);
        PlaylistSongJoin testPlaylistSongJoin = playlistSongJoinList.get(playlistSongJoinList.size() - 1);
        assertThat(testPlaylistSongJoin.getSongOrderIndex()).isEqualTo(DEFAULT_SONG_ORDER_INDEX);
        assertThat(testPlaylistSongJoin.getSongDateAdded()).isEqualTo(DEFAULT_SONG_DATE_ADDED);
    }

    @Test
    @Transactional
    void createPlaylistSongJoinWithExistingId() throws Exception {
        // Create the PlaylistSongJoin with an existing ID
        playlistSongJoin.setId(1L);

        int databaseSizeBeforeCreate = playlistSongJoinRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlaylistSongJoinMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(playlistSongJoin))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlaylistSongJoin in the database
        List<PlaylistSongJoin> playlistSongJoinList = playlistSongJoinRepository.findAll();
        assertThat(playlistSongJoinList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSongOrderIndexIsRequired() throws Exception {
        int databaseSizeBeforeTest = playlistSongJoinRepository.findAll().size();
        // set the field null
        playlistSongJoin.setSongOrderIndex(null);

        // Create the PlaylistSongJoin, which fails.

        restPlaylistSongJoinMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(playlistSongJoin))
            )
            .andExpect(status().isBadRequest());

        List<PlaylistSongJoin> playlistSongJoinList = playlistSongJoinRepository.findAll();
        assertThat(playlistSongJoinList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSongDateAddedIsRequired() throws Exception {
        int databaseSizeBeforeTest = playlistSongJoinRepository.findAll().size();
        // set the field null
        playlistSongJoin.setSongDateAdded(null);

        // Create the PlaylistSongJoin, which fails.

        restPlaylistSongJoinMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(playlistSongJoin))
            )
            .andExpect(status().isBadRequest());

        List<PlaylistSongJoin> playlistSongJoinList = playlistSongJoinRepository.findAll();
        assertThat(playlistSongJoinList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPlaylistSongJoins() throws Exception {
        // Initialize the database
        playlistSongJoinRepository.saveAndFlush(playlistSongJoin);

        // Get all the playlistSongJoinList
        restPlaylistSongJoinMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(playlistSongJoin.getId().intValue())))
            .andExpect(jsonPath("$.[*].songOrderIndex").value(hasItem(DEFAULT_SONG_ORDER_INDEX)))
            .andExpect(jsonPath("$.[*].songDateAdded").value(hasItem(DEFAULT_SONG_DATE_ADDED.toString())));
    }

    @Test
    @Transactional
    void getPlaylistSongJoin() throws Exception {
        // Initialize the database
        playlistSongJoinRepository.saveAndFlush(playlistSongJoin);

        // Get the playlistSongJoin
        restPlaylistSongJoinMockMvc
            .perform(get(ENTITY_API_URL_ID, playlistSongJoin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(playlistSongJoin.getId().intValue()))
            .andExpect(jsonPath("$.songOrderIndex").value(DEFAULT_SONG_ORDER_INDEX))
            .andExpect(jsonPath("$.songDateAdded").value(DEFAULT_SONG_DATE_ADDED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPlaylistSongJoin() throws Exception {
        // Get the playlistSongJoin
        restPlaylistSongJoinMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPlaylistSongJoin() throws Exception {
        // Initialize the database
        playlistSongJoinRepository.saveAndFlush(playlistSongJoin);

        int databaseSizeBeforeUpdate = playlistSongJoinRepository.findAll().size();

        // Update the playlistSongJoin
        PlaylistSongJoin updatedPlaylistSongJoin = playlistSongJoinRepository.findById(playlistSongJoin.getId()).get();
        // Disconnect from session so that the updates on updatedPlaylistSongJoin are not directly saved in db
        em.detach(updatedPlaylistSongJoin);
        updatedPlaylistSongJoin.songOrderIndex(UPDATED_SONG_ORDER_INDEX).songDateAdded(UPDATED_SONG_DATE_ADDED);

        restPlaylistSongJoinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPlaylistSongJoin.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPlaylistSongJoin))
            )
            .andExpect(status().isOk());

        // Validate the PlaylistSongJoin in the database
        List<PlaylistSongJoin> playlistSongJoinList = playlistSongJoinRepository.findAll();
        assertThat(playlistSongJoinList).hasSize(databaseSizeBeforeUpdate);
        PlaylistSongJoin testPlaylistSongJoin = playlistSongJoinList.get(playlistSongJoinList.size() - 1);
        assertThat(testPlaylistSongJoin.getSongOrderIndex()).isEqualTo(UPDATED_SONG_ORDER_INDEX);
        assertThat(testPlaylistSongJoin.getSongDateAdded()).isEqualTo(UPDATED_SONG_DATE_ADDED);
    }

    @Test
    @Transactional
    void putNonExistingPlaylistSongJoin() throws Exception {
        int databaseSizeBeforeUpdate = playlistSongJoinRepository.findAll().size();
        playlistSongJoin.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlaylistSongJoinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, playlistSongJoin.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playlistSongJoin))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlaylistSongJoin in the database
        List<PlaylistSongJoin> playlistSongJoinList = playlistSongJoinRepository.findAll();
        assertThat(playlistSongJoinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlaylistSongJoin() throws Exception {
        int databaseSizeBeforeUpdate = playlistSongJoinRepository.findAll().size();
        playlistSongJoin.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaylistSongJoinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playlistSongJoin))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlaylistSongJoin in the database
        List<PlaylistSongJoin> playlistSongJoinList = playlistSongJoinRepository.findAll();
        assertThat(playlistSongJoinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlaylistSongJoin() throws Exception {
        int databaseSizeBeforeUpdate = playlistSongJoinRepository.findAll().size();
        playlistSongJoin.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaylistSongJoinMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(playlistSongJoin))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlaylistSongJoin in the database
        List<PlaylistSongJoin> playlistSongJoinList = playlistSongJoinRepository.findAll();
        assertThat(playlistSongJoinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlaylistSongJoinWithPatch() throws Exception {
        // Initialize the database
        playlistSongJoinRepository.saveAndFlush(playlistSongJoin);

        int databaseSizeBeforeUpdate = playlistSongJoinRepository.findAll().size();

        // Update the playlistSongJoin using partial update
        PlaylistSongJoin partialUpdatedPlaylistSongJoin = new PlaylistSongJoin();
        partialUpdatedPlaylistSongJoin.setId(playlistSongJoin.getId());

        partialUpdatedPlaylistSongJoin.songOrderIndex(UPDATED_SONG_ORDER_INDEX).songDateAdded(UPDATED_SONG_DATE_ADDED);

        restPlaylistSongJoinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlaylistSongJoin.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlaylistSongJoin))
            )
            .andExpect(status().isOk());

        // Validate the PlaylistSongJoin in the database
        List<PlaylistSongJoin> playlistSongJoinList = playlistSongJoinRepository.findAll();
        assertThat(playlistSongJoinList).hasSize(databaseSizeBeforeUpdate);
        PlaylistSongJoin testPlaylistSongJoin = playlistSongJoinList.get(playlistSongJoinList.size() - 1);
        assertThat(testPlaylistSongJoin.getSongOrderIndex()).isEqualTo(UPDATED_SONG_ORDER_INDEX);
        assertThat(testPlaylistSongJoin.getSongDateAdded()).isEqualTo(UPDATED_SONG_DATE_ADDED);
    }

    @Test
    @Transactional
    void fullUpdatePlaylistSongJoinWithPatch() throws Exception {
        // Initialize the database
        playlistSongJoinRepository.saveAndFlush(playlistSongJoin);

        int databaseSizeBeforeUpdate = playlistSongJoinRepository.findAll().size();

        // Update the playlistSongJoin using partial update
        PlaylistSongJoin partialUpdatedPlaylistSongJoin = new PlaylistSongJoin();
        partialUpdatedPlaylistSongJoin.setId(playlistSongJoin.getId());

        partialUpdatedPlaylistSongJoin.songOrderIndex(UPDATED_SONG_ORDER_INDEX).songDateAdded(UPDATED_SONG_DATE_ADDED);

        restPlaylistSongJoinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlaylistSongJoin.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlaylistSongJoin))
            )
            .andExpect(status().isOk());

        // Validate the PlaylistSongJoin in the database
        List<PlaylistSongJoin> playlistSongJoinList = playlistSongJoinRepository.findAll();
        assertThat(playlistSongJoinList).hasSize(databaseSizeBeforeUpdate);
        PlaylistSongJoin testPlaylistSongJoin = playlistSongJoinList.get(playlistSongJoinList.size() - 1);
        assertThat(testPlaylistSongJoin.getSongOrderIndex()).isEqualTo(UPDATED_SONG_ORDER_INDEX);
        assertThat(testPlaylistSongJoin.getSongDateAdded()).isEqualTo(UPDATED_SONG_DATE_ADDED);
    }

    @Test
    @Transactional
    void patchNonExistingPlaylistSongJoin() throws Exception {
        int databaseSizeBeforeUpdate = playlistSongJoinRepository.findAll().size();
        playlistSongJoin.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlaylistSongJoinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, playlistSongJoin.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(playlistSongJoin))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlaylistSongJoin in the database
        List<PlaylistSongJoin> playlistSongJoinList = playlistSongJoinRepository.findAll();
        assertThat(playlistSongJoinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlaylistSongJoin() throws Exception {
        int databaseSizeBeforeUpdate = playlistSongJoinRepository.findAll().size();
        playlistSongJoin.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaylistSongJoinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(playlistSongJoin))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlaylistSongJoin in the database
        List<PlaylistSongJoin> playlistSongJoinList = playlistSongJoinRepository.findAll();
        assertThat(playlistSongJoinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlaylistSongJoin() throws Exception {
        int databaseSizeBeforeUpdate = playlistSongJoinRepository.findAll().size();
        playlistSongJoin.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaylistSongJoinMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(playlistSongJoin))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlaylistSongJoin in the database
        List<PlaylistSongJoin> playlistSongJoinList = playlistSongJoinRepository.findAll();
        assertThat(playlistSongJoinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlaylistSongJoin() throws Exception {
        // Initialize the database
        playlistSongJoinRepository.saveAndFlush(playlistSongJoin);

        int databaseSizeBeforeDelete = playlistSongJoinRepository.findAll().size();

        // Delete the playlistSongJoin
        restPlaylistSongJoinMockMvc
            .perform(delete(ENTITY_API_URL_ID, playlistSongJoin.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PlaylistSongJoin> playlistSongJoinList = playlistSongJoinRepository.findAll();
        assertThat(playlistSongJoinList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

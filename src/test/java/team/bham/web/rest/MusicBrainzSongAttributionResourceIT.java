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
import team.bham.domain.MusicBrainzSongAttribution;
import team.bham.repository.MusicBrainzSongAttributionRepository;

/**
 * Integration tests for the {@link MusicBrainzSongAttributionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MusicBrainzSongAttributionResourceIT {

    private static final String DEFAULT_RECORDING_MBID = "AAAAAAAAAA";
    private static final String UPDATED_RECORDING_MBID = "BBBBBBBBBB";

    private static final String DEFAULT_RECORDING_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_RECORDING_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SONG_MAIN_ARTIST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SONG_MAIN_ARTIST_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_SONG_MAIN_ARTIST_ID = 1;
    private static final Integer UPDATED_SONG_MAIN_ARTIST_ID = 2;

    private static final String DEFAULT_SONG_CONTRIBUTOR_MBID = "AAAAAAAAAA";
    private static final String UPDATED_SONG_CONTRIBUTOR_MBID = "BBBBBBBBBB";

    private static final String DEFAULT_SONG_CONTRIBUTOR_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SONG_CONTRIBUTOR_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SONG_CONTRIBUTOR_ROLE = "AAAAAAAAAA";
    private static final String UPDATED_SONG_CONTRIBUTOR_ROLE = "BBBBBBBBBB";

    private static final String DEFAULT_SONG_CONTRIBUTOR_INSTRUMENT = "AAAAAAAAAA";
    private static final String UPDATED_SONG_CONTRIBUTOR_INSTRUMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/music-brainz-song-attributions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MusicBrainzSongAttributionRepository musicBrainzSongAttributionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMusicBrainzSongAttributionMockMvc;

    private MusicBrainzSongAttribution musicBrainzSongAttribution;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MusicBrainzSongAttribution createEntity(EntityManager em) {
        MusicBrainzSongAttribution musicBrainzSongAttribution = new MusicBrainzSongAttribution()
            .recordingMBID(DEFAULT_RECORDING_MBID)
            .recordingTitle(DEFAULT_RECORDING_TITLE)
            .songMainArtistName(DEFAULT_SONG_MAIN_ARTIST_NAME)
            .songMainArtistID(DEFAULT_SONG_MAIN_ARTIST_ID)
            .songContributorMBID(DEFAULT_SONG_CONTRIBUTOR_MBID)
            .songContributorName(DEFAULT_SONG_CONTRIBUTOR_NAME)
            .songContributorRole(DEFAULT_SONG_CONTRIBUTOR_ROLE)
            .songContributorInstrument(DEFAULT_SONG_CONTRIBUTOR_INSTRUMENT);
        return musicBrainzSongAttribution;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MusicBrainzSongAttribution createUpdatedEntity(EntityManager em) {
        MusicBrainzSongAttribution musicBrainzSongAttribution = new MusicBrainzSongAttribution()
            .recordingMBID(UPDATED_RECORDING_MBID)
            .recordingTitle(UPDATED_RECORDING_TITLE)
            .songMainArtistName(UPDATED_SONG_MAIN_ARTIST_NAME)
            .songMainArtistID(UPDATED_SONG_MAIN_ARTIST_ID)
            .songContributorMBID(UPDATED_SONG_CONTRIBUTOR_MBID)
            .songContributorName(UPDATED_SONG_CONTRIBUTOR_NAME)
            .songContributorRole(UPDATED_SONG_CONTRIBUTOR_ROLE)
            .songContributorInstrument(UPDATED_SONG_CONTRIBUTOR_INSTRUMENT);
        return musicBrainzSongAttribution;
    }

    @BeforeEach
    public void initTest() {
        musicBrainzSongAttribution = createEntity(em);
    }

    @Test
    @Transactional
    void createMusicBrainzSongAttribution() throws Exception {
        int databaseSizeBeforeCreate = musicBrainzSongAttributionRepository.findAll().size();
        // Create the MusicBrainzSongAttribution
        restMusicBrainzSongAttributionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(musicBrainzSongAttribution))
            )
            .andExpect(status().isCreated());

        // Validate the MusicBrainzSongAttribution in the database
        List<MusicBrainzSongAttribution> musicBrainzSongAttributionList = musicBrainzSongAttributionRepository.findAll();
        assertThat(musicBrainzSongAttributionList).hasSize(databaseSizeBeforeCreate + 1);
        MusicBrainzSongAttribution testMusicBrainzSongAttribution = musicBrainzSongAttributionList.get(
            musicBrainzSongAttributionList.size() - 1
        );
        assertThat(testMusicBrainzSongAttribution.getRecordingMBID()).isEqualTo(DEFAULT_RECORDING_MBID);
        assertThat(testMusicBrainzSongAttribution.getRecordingTitle()).isEqualTo(DEFAULT_RECORDING_TITLE);
        assertThat(testMusicBrainzSongAttribution.getSongMainArtistName()).isEqualTo(DEFAULT_SONG_MAIN_ARTIST_NAME);
        assertThat(testMusicBrainzSongAttribution.getSongMainArtistID()).isEqualTo(DEFAULT_SONG_MAIN_ARTIST_ID);
        assertThat(testMusicBrainzSongAttribution.getSongContributorMBID()).isEqualTo(DEFAULT_SONG_CONTRIBUTOR_MBID);
        assertThat(testMusicBrainzSongAttribution.getSongContributorName()).isEqualTo(DEFAULT_SONG_CONTRIBUTOR_NAME);
        assertThat(testMusicBrainzSongAttribution.getSongContributorRole()).isEqualTo(DEFAULT_SONG_CONTRIBUTOR_ROLE);
        assertThat(testMusicBrainzSongAttribution.getSongContributorInstrument()).isEqualTo(DEFAULT_SONG_CONTRIBUTOR_INSTRUMENT);
    }

    @Test
    @Transactional
    void createMusicBrainzSongAttributionWithExistingId() throws Exception {
        // Create the MusicBrainzSongAttribution with an existing ID
        musicBrainzSongAttribution.setId(1L);

        int databaseSizeBeforeCreate = musicBrainzSongAttributionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMusicBrainzSongAttributionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(musicBrainzSongAttribution))
            )
            .andExpect(status().isBadRequest());

        // Validate the MusicBrainzSongAttribution in the database
        List<MusicBrainzSongAttribution> musicBrainzSongAttributionList = musicBrainzSongAttributionRepository.findAll();
        assertThat(musicBrainzSongAttributionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMusicBrainzSongAttributions() throws Exception {
        // Initialize the database
        musicBrainzSongAttributionRepository.saveAndFlush(musicBrainzSongAttribution);

        // Get all the musicBrainzSongAttributionList
        restMusicBrainzSongAttributionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(musicBrainzSongAttribution.getId().intValue())))
            .andExpect(jsonPath("$.[*].recordingMBID").value(hasItem(DEFAULT_RECORDING_MBID)))
            .andExpect(jsonPath("$.[*].recordingTitle").value(hasItem(DEFAULT_RECORDING_TITLE)))
            .andExpect(jsonPath("$.[*].songMainArtistName").value(hasItem(DEFAULT_SONG_MAIN_ARTIST_NAME)))
            .andExpect(jsonPath("$.[*].songMainArtistID").value(hasItem(DEFAULT_SONG_MAIN_ARTIST_ID)))
            .andExpect(jsonPath("$.[*].songContributorMBID").value(hasItem(DEFAULT_SONG_CONTRIBUTOR_MBID)))
            .andExpect(jsonPath("$.[*].songContributorName").value(hasItem(DEFAULT_SONG_CONTRIBUTOR_NAME)))
            .andExpect(jsonPath("$.[*].songContributorRole").value(hasItem(DEFAULT_SONG_CONTRIBUTOR_ROLE)))
            .andExpect(jsonPath("$.[*].songContributorInstrument").value(hasItem(DEFAULT_SONG_CONTRIBUTOR_INSTRUMENT)));
    }

    @Test
    @Transactional
    void getMusicBrainzSongAttribution() throws Exception {
        // Initialize the database
        musicBrainzSongAttributionRepository.saveAndFlush(musicBrainzSongAttribution);

        // Get the musicBrainzSongAttribution
        restMusicBrainzSongAttributionMockMvc
            .perform(get(ENTITY_API_URL_ID, musicBrainzSongAttribution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(musicBrainzSongAttribution.getId().intValue()))
            .andExpect(jsonPath("$.recordingMBID").value(DEFAULT_RECORDING_MBID))
            .andExpect(jsonPath("$.recordingTitle").value(DEFAULT_RECORDING_TITLE))
            .andExpect(jsonPath("$.songMainArtistName").value(DEFAULT_SONG_MAIN_ARTIST_NAME))
            .andExpect(jsonPath("$.songMainArtistID").value(DEFAULT_SONG_MAIN_ARTIST_ID))
            .andExpect(jsonPath("$.songContributorMBID").value(DEFAULT_SONG_CONTRIBUTOR_MBID))
            .andExpect(jsonPath("$.songContributorName").value(DEFAULT_SONG_CONTRIBUTOR_NAME))
            .andExpect(jsonPath("$.songContributorRole").value(DEFAULT_SONG_CONTRIBUTOR_ROLE))
            .andExpect(jsonPath("$.songContributorInstrument").value(DEFAULT_SONG_CONTRIBUTOR_INSTRUMENT));
    }

    @Test
    @Transactional
    void getNonExistingMusicBrainzSongAttribution() throws Exception {
        // Get the musicBrainzSongAttribution
        restMusicBrainzSongAttributionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMusicBrainzSongAttribution() throws Exception {
        // Initialize the database
        musicBrainzSongAttributionRepository.saveAndFlush(musicBrainzSongAttribution);

        int databaseSizeBeforeUpdate = musicBrainzSongAttributionRepository.findAll().size();

        // Update the musicBrainzSongAttribution
        MusicBrainzSongAttribution updatedMusicBrainzSongAttribution = musicBrainzSongAttributionRepository
            .findById(musicBrainzSongAttribution.getId())
            .get();
        // Disconnect from session so that the updates on updatedMusicBrainzSongAttribution are not directly saved in db
        em.detach(updatedMusicBrainzSongAttribution);
        updatedMusicBrainzSongAttribution
            .recordingMBID(UPDATED_RECORDING_MBID)
            .recordingTitle(UPDATED_RECORDING_TITLE)
            .songMainArtistName(UPDATED_SONG_MAIN_ARTIST_NAME)
            .songMainArtistID(UPDATED_SONG_MAIN_ARTIST_ID)
            .songContributorMBID(UPDATED_SONG_CONTRIBUTOR_MBID)
            .songContributorName(UPDATED_SONG_CONTRIBUTOR_NAME)
            .songContributorRole(UPDATED_SONG_CONTRIBUTOR_ROLE)
            .songContributorInstrument(UPDATED_SONG_CONTRIBUTOR_INSTRUMENT);

        restMusicBrainzSongAttributionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMusicBrainzSongAttribution.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMusicBrainzSongAttribution))
            )
            .andExpect(status().isOk());

        // Validate the MusicBrainzSongAttribution in the database
        List<MusicBrainzSongAttribution> musicBrainzSongAttributionList = musicBrainzSongAttributionRepository.findAll();
        assertThat(musicBrainzSongAttributionList).hasSize(databaseSizeBeforeUpdate);
        MusicBrainzSongAttribution testMusicBrainzSongAttribution = musicBrainzSongAttributionList.get(
            musicBrainzSongAttributionList.size() - 1
        );
        assertThat(testMusicBrainzSongAttribution.getRecordingMBID()).isEqualTo(UPDATED_RECORDING_MBID);
        assertThat(testMusicBrainzSongAttribution.getRecordingTitle()).isEqualTo(UPDATED_RECORDING_TITLE);
        assertThat(testMusicBrainzSongAttribution.getSongMainArtistName()).isEqualTo(UPDATED_SONG_MAIN_ARTIST_NAME);
        assertThat(testMusicBrainzSongAttribution.getSongMainArtistID()).isEqualTo(UPDATED_SONG_MAIN_ARTIST_ID);
        assertThat(testMusicBrainzSongAttribution.getSongContributorMBID()).isEqualTo(UPDATED_SONG_CONTRIBUTOR_MBID);
        assertThat(testMusicBrainzSongAttribution.getSongContributorName()).isEqualTo(UPDATED_SONG_CONTRIBUTOR_NAME);
        assertThat(testMusicBrainzSongAttribution.getSongContributorRole()).isEqualTo(UPDATED_SONG_CONTRIBUTOR_ROLE);
        assertThat(testMusicBrainzSongAttribution.getSongContributorInstrument()).isEqualTo(UPDATED_SONG_CONTRIBUTOR_INSTRUMENT);
    }

    @Test
    @Transactional
    void putNonExistingMusicBrainzSongAttribution() throws Exception {
        int databaseSizeBeforeUpdate = musicBrainzSongAttributionRepository.findAll().size();
        musicBrainzSongAttribution.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMusicBrainzSongAttributionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, musicBrainzSongAttribution.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(musicBrainzSongAttribution))
            )
            .andExpect(status().isBadRequest());

        // Validate the MusicBrainzSongAttribution in the database
        List<MusicBrainzSongAttribution> musicBrainzSongAttributionList = musicBrainzSongAttributionRepository.findAll();
        assertThat(musicBrainzSongAttributionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMusicBrainzSongAttribution() throws Exception {
        int databaseSizeBeforeUpdate = musicBrainzSongAttributionRepository.findAll().size();
        musicBrainzSongAttribution.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMusicBrainzSongAttributionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(musicBrainzSongAttribution))
            )
            .andExpect(status().isBadRequest());

        // Validate the MusicBrainzSongAttribution in the database
        List<MusicBrainzSongAttribution> musicBrainzSongAttributionList = musicBrainzSongAttributionRepository.findAll();
        assertThat(musicBrainzSongAttributionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMusicBrainzSongAttribution() throws Exception {
        int databaseSizeBeforeUpdate = musicBrainzSongAttributionRepository.findAll().size();
        musicBrainzSongAttribution.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMusicBrainzSongAttributionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(musicBrainzSongAttribution))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MusicBrainzSongAttribution in the database
        List<MusicBrainzSongAttribution> musicBrainzSongAttributionList = musicBrainzSongAttributionRepository.findAll();
        assertThat(musicBrainzSongAttributionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMusicBrainzSongAttributionWithPatch() throws Exception {
        // Initialize the database
        musicBrainzSongAttributionRepository.saveAndFlush(musicBrainzSongAttribution);

        int databaseSizeBeforeUpdate = musicBrainzSongAttributionRepository.findAll().size();

        // Update the musicBrainzSongAttribution using partial update
        MusicBrainzSongAttribution partialUpdatedMusicBrainzSongAttribution = new MusicBrainzSongAttribution();
        partialUpdatedMusicBrainzSongAttribution.setId(musicBrainzSongAttribution.getId());

        partialUpdatedMusicBrainzSongAttribution
            .recordingTitle(UPDATED_RECORDING_TITLE)
            .songMainArtistName(UPDATED_SONG_MAIN_ARTIST_NAME)
            .songMainArtistID(UPDATED_SONG_MAIN_ARTIST_ID)
            .songContributorMBID(UPDATED_SONG_CONTRIBUTOR_MBID)
            .songContributorRole(UPDATED_SONG_CONTRIBUTOR_ROLE)
            .songContributorInstrument(UPDATED_SONG_CONTRIBUTOR_INSTRUMENT);

        restMusicBrainzSongAttributionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMusicBrainzSongAttribution.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMusicBrainzSongAttribution))
            )
            .andExpect(status().isOk());

        // Validate the MusicBrainzSongAttribution in the database
        List<MusicBrainzSongAttribution> musicBrainzSongAttributionList = musicBrainzSongAttributionRepository.findAll();
        assertThat(musicBrainzSongAttributionList).hasSize(databaseSizeBeforeUpdate);
        MusicBrainzSongAttribution testMusicBrainzSongAttribution = musicBrainzSongAttributionList.get(
            musicBrainzSongAttributionList.size() - 1
        );
        assertThat(testMusicBrainzSongAttribution.getRecordingMBID()).isEqualTo(DEFAULT_RECORDING_MBID);
        assertThat(testMusicBrainzSongAttribution.getRecordingTitle()).isEqualTo(UPDATED_RECORDING_TITLE);
        assertThat(testMusicBrainzSongAttribution.getSongMainArtistName()).isEqualTo(UPDATED_SONG_MAIN_ARTIST_NAME);
        assertThat(testMusicBrainzSongAttribution.getSongMainArtistID()).isEqualTo(UPDATED_SONG_MAIN_ARTIST_ID);
        assertThat(testMusicBrainzSongAttribution.getSongContributorMBID()).isEqualTo(UPDATED_SONG_CONTRIBUTOR_MBID);
        assertThat(testMusicBrainzSongAttribution.getSongContributorName()).isEqualTo(DEFAULT_SONG_CONTRIBUTOR_NAME);
        assertThat(testMusicBrainzSongAttribution.getSongContributorRole()).isEqualTo(UPDATED_SONG_CONTRIBUTOR_ROLE);
        assertThat(testMusicBrainzSongAttribution.getSongContributorInstrument()).isEqualTo(UPDATED_SONG_CONTRIBUTOR_INSTRUMENT);
    }

    @Test
    @Transactional
    void fullUpdateMusicBrainzSongAttributionWithPatch() throws Exception {
        // Initialize the database
        musicBrainzSongAttributionRepository.saveAndFlush(musicBrainzSongAttribution);

        int databaseSizeBeforeUpdate = musicBrainzSongAttributionRepository.findAll().size();

        // Update the musicBrainzSongAttribution using partial update
        MusicBrainzSongAttribution partialUpdatedMusicBrainzSongAttribution = new MusicBrainzSongAttribution();
        partialUpdatedMusicBrainzSongAttribution.setId(musicBrainzSongAttribution.getId());

        partialUpdatedMusicBrainzSongAttribution
            .recordingMBID(UPDATED_RECORDING_MBID)
            .recordingTitle(UPDATED_RECORDING_TITLE)
            .songMainArtistName(UPDATED_SONG_MAIN_ARTIST_NAME)
            .songMainArtistID(UPDATED_SONG_MAIN_ARTIST_ID)
            .songContributorMBID(UPDATED_SONG_CONTRIBUTOR_MBID)
            .songContributorName(UPDATED_SONG_CONTRIBUTOR_NAME)
            .songContributorRole(UPDATED_SONG_CONTRIBUTOR_ROLE)
            .songContributorInstrument(UPDATED_SONG_CONTRIBUTOR_INSTRUMENT);

        restMusicBrainzSongAttributionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMusicBrainzSongAttribution.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMusicBrainzSongAttribution))
            )
            .andExpect(status().isOk());

        // Validate the MusicBrainzSongAttribution in the database
        List<MusicBrainzSongAttribution> musicBrainzSongAttributionList = musicBrainzSongAttributionRepository.findAll();
        assertThat(musicBrainzSongAttributionList).hasSize(databaseSizeBeforeUpdate);
        MusicBrainzSongAttribution testMusicBrainzSongAttribution = musicBrainzSongAttributionList.get(
            musicBrainzSongAttributionList.size() - 1
        );
        assertThat(testMusicBrainzSongAttribution.getRecordingMBID()).isEqualTo(UPDATED_RECORDING_MBID);
        assertThat(testMusicBrainzSongAttribution.getRecordingTitle()).isEqualTo(UPDATED_RECORDING_TITLE);
        assertThat(testMusicBrainzSongAttribution.getSongMainArtistName()).isEqualTo(UPDATED_SONG_MAIN_ARTIST_NAME);
        assertThat(testMusicBrainzSongAttribution.getSongMainArtistID()).isEqualTo(UPDATED_SONG_MAIN_ARTIST_ID);
        assertThat(testMusicBrainzSongAttribution.getSongContributorMBID()).isEqualTo(UPDATED_SONG_CONTRIBUTOR_MBID);
        assertThat(testMusicBrainzSongAttribution.getSongContributorName()).isEqualTo(UPDATED_SONG_CONTRIBUTOR_NAME);
        assertThat(testMusicBrainzSongAttribution.getSongContributorRole()).isEqualTo(UPDATED_SONG_CONTRIBUTOR_ROLE);
        assertThat(testMusicBrainzSongAttribution.getSongContributorInstrument()).isEqualTo(UPDATED_SONG_CONTRIBUTOR_INSTRUMENT);
    }

    @Test
    @Transactional
    void patchNonExistingMusicBrainzSongAttribution() throws Exception {
        int databaseSizeBeforeUpdate = musicBrainzSongAttributionRepository.findAll().size();
        musicBrainzSongAttribution.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMusicBrainzSongAttributionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, musicBrainzSongAttribution.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(musicBrainzSongAttribution))
            )
            .andExpect(status().isBadRequest());

        // Validate the MusicBrainzSongAttribution in the database
        List<MusicBrainzSongAttribution> musicBrainzSongAttributionList = musicBrainzSongAttributionRepository.findAll();
        assertThat(musicBrainzSongAttributionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMusicBrainzSongAttribution() throws Exception {
        int databaseSizeBeforeUpdate = musicBrainzSongAttributionRepository.findAll().size();
        musicBrainzSongAttribution.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMusicBrainzSongAttributionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(musicBrainzSongAttribution))
            )
            .andExpect(status().isBadRequest());

        // Validate the MusicBrainzSongAttribution in the database
        List<MusicBrainzSongAttribution> musicBrainzSongAttributionList = musicBrainzSongAttributionRepository.findAll();
        assertThat(musicBrainzSongAttributionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMusicBrainzSongAttribution() throws Exception {
        int databaseSizeBeforeUpdate = musicBrainzSongAttributionRepository.findAll().size();
        musicBrainzSongAttribution.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMusicBrainzSongAttributionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(musicBrainzSongAttribution))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MusicBrainzSongAttribution in the database
        List<MusicBrainzSongAttribution> musicBrainzSongAttributionList = musicBrainzSongAttributionRepository.findAll();
        assertThat(musicBrainzSongAttributionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMusicBrainzSongAttribution() throws Exception {
        // Initialize the database
        musicBrainzSongAttributionRepository.saveAndFlush(musicBrainzSongAttribution);

        int databaseSizeBeforeDelete = musicBrainzSongAttributionRepository.findAll().size();

        // Delete the musicBrainzSongAttribution
        restMusicBrainzSongAttributionMockMvc
            .perform(delete(ENTITY_API_URL_ID, musicBrainzSongAttribution.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MusicBrainzSongAttribution> musicBrainzSongAttributionList = musicBrainzSongAttributionRepository.findAll();
        assertThat(musicBrainzSongAttributionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

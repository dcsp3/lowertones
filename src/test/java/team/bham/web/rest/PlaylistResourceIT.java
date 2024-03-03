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
import team.bham.domain.Playlist;
import team.bham.repository.PlaylistRepository;

/**
 * Integration tests for the {@link PlaylistResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PlaylistResourceIT {

    private static final LocalDate DEFAULT_DATE_ADDED_TO_DB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ADDED_TO_DB = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_LAST_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_LAST_MODIFIED = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_PLAYLIST_SPOTIFY_ID = "AAAAAAAAAA";
    private static final String UPDATED_PLAYLIST_SPOTIFY_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PLAYLIST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PLAYLIST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PLAYLIST_PHOTO = "AAAAAAAAAA";
    private static final String UPDATED_PLAYLIST_PHOTO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/playlists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlaylistMockMvc;

    private Playlist playlist;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Playlist createEntity(EntityManager em) {
        Playlist playlist = new Playlist()
            .dateAddedToDB(DEFAULT_DATE_ADDED_TO_DB)
            .dateLastModified(DEFAULT_DATE_LAST_MODIFIED)
            .playlistSpotifyID(DEFAULT_PLAYLIST_SPOTIFY_ID)
            .playlistName(DEFAULT_PLAYLIST_NAME)
            .playlistPhoto(DEFAULT_PLAYLIST_PHOTO);
        return playlist;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Playlist createUpdatedEntity(EntityManager em) {
        Playlist playlist = new Playlist()
            .dateAddedToDB(UPDATED_DATE_ADDED_TO_DB)
            .dateLastModified(UPDATED_DATE_LAST_MODIFIED)
            .playlistSpotifyID(UPDATED_PLAYLIST_SPOTIFY_ID)
            .playlistName(UPDATED_PLAYLIST_NAME)
            .playlistPhoto(UPDATED_PLAYLIST_PHOTO);
        return playlist;
    }

    @BeforeEach
    public void initTest() {
        playlist = createEntity(em);
    }

    @Test
    @Transactional
    void createPlaylist() throws Exception {
        int databaseSizeBeforeCreate = playlistRepository.findAll().size();
        // Create the Playlist
        restPlaylistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(playlist)))
            .andExpect(status().isCreated());

        // Validate the Playlist in the database
        List<Playlist> playlistList = playlistRepository.findAll();
        assertThat(playlistList).hasSize(databaseSizeBeforeCreate + 1);
        Playlist testPlaylist = playlistList.get(playlistList.size() - 1);
        assertThat(testPlaylist.getDateAddedToDB()).isEqualTo(DEFAULT_DATE_ADDED_TO_DB);
        assertThat(testPlaylist.getDateLastModified()).isEqualTo(DEFAULT_DATE_LAST_MODIFIED);
        assertThat(testPlaylist.getPlaylistSpotifyID()).isEqualTo(DEFAULT_PLAYLIST_SPOTIFY_ID);
        assertThat(testPlaylist.getPlaylistName()).isEqualTo(DEFAULT_PLAYLIST_NAME);
        assertThat(testPlaylist.getPlaylistPhoto()).isEqualTo(DEFAULT_PLAYLIST_PHOTO);
    }

    @Test
    @Transactional
    void createPlaylistWithExistingId() throws Exception {
        // Create the Playlist with an existing ID
        playlist.setId(1L);

        int databaseSizeBeforeCreate = playlistRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlaylistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(playlist)))
            .andExpect(status().isBadRequest());

        // Validate the Playlist in the database
        List<Playlist> playlistList = playlistRepository.findAll();
        assertThat(playlistList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateAddedToDBIsRequired() throws Exception {
        int databaseSizeBeforeTest = playlistRepository.findAll().size();
        // set the field null
        playlist.setDateAddedToDB(null);

        // Create the Playlist, which fails.

        restPlaylistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(playlist)))
            .andExpect(status().isBadRequest());

        List<Playlist> playlistList = playlistRepository.findAll();
        assertThat(playlistList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateLastModifiedIsRequired() throws Exception {
        int databaseSizeBeforeTest = playlistRepository.findAll().size();
        // set the field null
        playlist.setDateLastModified(null);

        // Create the Playlist, which fails.

        restPlaylistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(playlist)))
            .andExpect(status().isBadRequest());

        List<Playlist> playlistList = playlistRepository.findAll();
        assertThat(playlistList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPlaylistSpotifyIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = playlistRepository.findAll().size();
        // set the field null
        playlist.setPlaylistSpotifyID(null);

        // Create the Playlist, which fails.

        restPlaylistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(playlist)))
            .andExpect(status().isBadRequest());

        List<Playlist> playlistList = playlistRepository.findAll();
        assertThat(playlistList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPlaylistNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = playlistRepository.findAll().size();
        // set the field null
        playlist.setPlaylistName(null);

        // Create the Playlist, which fails.

        restPlaylistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(playlist)))
            .andExpect(status().isBadRequest());

        List<Playlist> playlistList = playlistRepository.findAll();
        assertThat(playlistList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPlaylists() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        // Get all the playlistList
        restPlaylistMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(playlist.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateAddedToDB").value(hasItem(DEFAULT_DATE_ADDED_TO_DB.toString())))
            .andExpect(jsonPath("$.[*].dateLastModified").value(hasItem(DEFAULT_DATE_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].playlistSpotifyID").value(hasItem(DEFAULT_PLAYLIST_SPOTIFY_ID)))
            .andExpect(jsonPath("$.[*].playlistName").value(hasItem(DEFAULT_PLAYLIST_NAME)))
            .andExpect(jsonPath("$.[*].playlistPhoto").value(hasItem(DEFAULT_PLAYLIST_PHOTO)));
    }

    @Test
    @Transactional
    void getPlaylist() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        // Get the playlist
        restPlaylistMockMvc
            .perform(get(ENTITY_API_URL_ID, playlist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(playlist.getId().intValue()))
            .andExpect(jsonPath("$.dateAddedToDB").value(DEFAULT_DATE_ADDED_TO_DB.toString()))
            .andExpect(jsonPath("$.dateLastModified").value(DEFAULT_DATE_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.playlistSpotifyID").value(DEFAULT_PLAYLIST_SPOTIFY_ID))
            .andExpect(jsonPath("$.playlistName").value(DEFAULT_PLAYLIST_NAME))
            .andExpect(jsonPath("$.playlistPhoto").value(DEFAULT_PLAYLIST_PHOTO));
    }

    @Test
    @Transactional
    void getNonExistingPlaylist() throws Exception {
        // Get the playlist
        restPlaylistMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPlaylist() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        int databaseSizeBeforeUpdate = playlistRepository.findAll().size();

        // Update the playlist
        Playlist updatedPlaylist = playlistRepository.findById(playlist.getId()).get();
        // Disconnect from session so that the updates on updatedPlaylist are not directly saved in db
        em.detach(updatedPlaylist);
        updatedPlaylist
            .dateAddedToDB(UPDATED_DATE_ADDED_TO_DB)
            .dateLastModified(UPDATED_DATE_LAST_MODIFIED)
            .playlistSpotifyID(UPDATED_PLAYLIST_SPOTIFY_ID)
            .playlistName(UPDATED_PLAYLIST_NAME)
            .playlistPhoto(UPDATED_PLAYLIST_PHOTO);

        restPlaylistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPlaylist.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPlaylist))
            )
            .andExpect(status().isOk());

        // Validate the Playlist in the database
        List<Playlist> playlistList = playlistRepository.findAll();
        assertThat(playlistList).hasSize(databaseSizeBeforeUpdate);
        Playlist testPlaylist = playlistList.get(playlistList.size() - 1);
        assertThat(testPlaylist.getDateAddedToDB()).isEqualTo(UPDATED_DATE_ADDED_TO_DB);
        assertThat(testPlaylist.getDateLastModified()).isEqualTo(UPDATED_DATE_LAST_MODIFIED);
        assertThat(testPlaylist.getPlaylistSpotifyID()).isEqualTo(UPDATED_PLAYLIST_SPOTIFY_ID);
        assertThat(testPlaylist.getPlaylistName()).isEqualTo(UPDATED_PLAYLIST_NAME);
        assertThat(testPlaylist.getPlaylistPhoto()).isEqualTo(UPDATED_PLAYLIST_PHOTO);
    }

    @Test
    @Transactional
    void putNonExistingPlaylist() throws Exception {
        int databaseSizeBeforeUpdate = playlistRepository.findAll().size();
        playlist.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlaylistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, playlist.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playlist))
            )
            .andExpect(status().isBadRequest());

        // Validate the Playlist in the database
        List<Playlist> playlistList = playlistRepository.findAll();
        assertThat(playlistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlaylist() throws Exception {
        int databaseSizeBeforeUpdate = playlistRepository.findAll().size();
        playlist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaylistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playlist))
            )
            .andExpect(status().isBadRequest());

        // Validate the Playlist in the database
        List<Playlist> playlistList = playlistRepository.findAll();
        assertThat(playlistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlaylist() throws Exception {
        int databaseSizeBeforeUpdate = playlistRepository.findAll().size();
        playlist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaylistMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(playlist)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Playlist in the database
        List<Playlist> playlistList = playlistRepository.findAll();
        assertThat(playlistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlaylistWithPatch() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        int databaseSizeBeforeUpdate = playlistRepository.findAll().size();

        // Update the playlist using partial update
        Playlist partialUpdatedPlaylist = new Playlist();
        partialUpdatedPlaylist.setId(playlist.getId());

        partialUpdatedPlaylist
            .dateAddedToDB(UPDATED_DATE_ADDED_TO_DB)
            .playlistSpotifyID(UPDATED_PLAYLIST_SPOTIFY_ID)
            .playlistPhoto(UPDATED_PLAYLIST_PHOTO);

        restPlaylistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlaylist.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlaylist))
            )
            .andExpect(status().isOk());

        // Validate the Playlist in the database
        List<Playlist> playlistList = playlistRepository.findAll();
        assertThat(playlistList).hasSize(databaseSizeBeforeUpdate);
        Playlist testPlaylist = playlistList.get(playlistList.size() - 1);
        assertThat(testPlaylist.getDateAddedToDB()).isEqualTo(UPDATED_DATE_ADDED_TO_DB);
        assertThat(testPlaylist.getDateLastModified()).isEqualTo(DEFAULT_DATE_LAST_MODIFIED);
        assertThat(testPlaylist.getPlaylistSpotifyID()).isEqualTo(UPDATED_PLAYLIST_SPOTIFY_ID);
        assertThat(testPlaylist.getPlaylistName()).isEqualTo(DEFAULT_PLAYLIST_NAME);
        assertThat(testPlaylist.getPlaylistPhoto()).isEqualTo(UPDATED_PLAYLIST_PHOTO);
    }

    @Test
    @Transactional
    void fullUpdatePlaylistWithPatch() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        int databaseSizeBeforeUpdate = playlistRepository.findAll().size();

        // Update the playlist using partial update
        Playlist partialUpdatedPlaylist = new Playlist();
        partialUpdatedPlaylist.setId(playlist.getId());

        partialUpdatedPlaylist
            .dateAddedToDB(UPDATED_DATE_ADDED_TO_DB)
            .dateLastModified(UPDATED_DATE_LAST_MODIFIED)
            .playlistSpotifyID(UPDATED_PLAYLIST_SPOTIFY_ID)
            .playlistName(UPDATED_PLAYLIST_NAME)
            .playlistPhoto(UPDATED_PLAYLIST_PHOTO);

        restPlaylistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlaylist.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlaylist))
            )
            .andExpect(status().isOk());

        // Validate the Playlist in the database
        List<Playlist> playlistList = playlistRepository.findAll();
        assertThat(playlistList).hasSize(databaseSizeBeforeUpdate);
        Playlist testPlaylist = playlistList.get(playlistList.size() - 1);
        assertThat(testPlaylist.getDateAddedToDB()).isEqualTo(UPDATED_DATE_ADDED_TO_DB);
        assertThat(testPlaylist.getDateLastModified()).isEqualTo(UPDATED_DATE_LAST_MODIFIED);
        assertThat(testPlaylist.getPlaylistSpotifyID()).isEqualTo(UPDATED_PLAYLIST_SPOTIFY_ID);
        assertThat(testPlaylist.getPlaylistName()).isEqualTo(UPDATED_PLAYLIST_NAME);
        assertThat(testPlaylist.getPlaylistPhoto()).isEqualTo(UPDATED_PLAYLIST_PHOTO);
    }

    @Test
    @Transactional
    void patchNonExistingPlaylist() throws Exception {
        int databaseSizeBeforeUpdate = playlistRepository.findAll().size();
        playlist.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlaylistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, playlist.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(playlist))
            )
            .andExpect(status().isBadRequest());

        // Validate the Playlist in the database
        List<Playlist> playlistList = playlistRepository.findAll();
        assertThat(playlistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlaylist() throws Exception {
        int databaseSizeBeforeUpdate = playlistRepository.findAll().size();
        playlist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaylistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(playlist))
            )
            .andExpect(status().isBadRequest());

        // Validate the Playlist in the database
        List<Playlist> playlistList = playlistRepository.findAll();
        assertThat(playlistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlaylist() throws Exception {
        int databaseSizeBeforeUpdate = playlistRepository.findAll().size();
        playlist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaylistMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(playlist)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Playlist in the database
        List<Playlist> playlistList = playlistRepository.findAll();
        assertThat(playlistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlaylist() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        int databaseSizeBeforeDelete = playlistRepository.findAll().size();

        // Delete the playlist
        restPlaylistMockMvc
            .perform(delete(ENTITY_API_URL_ID, playlist.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Playlist> playlistList = playlistRepository.findAll();
        assertThat(playlistList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

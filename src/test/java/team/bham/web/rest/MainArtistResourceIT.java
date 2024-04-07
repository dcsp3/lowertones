package team.bham.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import team.bham.IntegrationTest;
import team.bham.domain.MainArtist;
import team.bham.repository.MainArtistRepository;

/**
 * Integration tests for the {@link MainArtistResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MainArtistResourceIT {

    private static final String DEFAULT_ARTIST_SPOTIFY_ID = "AAAAAAAAAA";
    private static final String UPDATED_ARTIST_SPOTIFY_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ARTIST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ARTIST_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_ARTIST_POPULARITY = 1;
    private static final Integer UPDATED_ARTIST_POPULARITY = 2;

    private static final String DEFAULT_ARTIST_IMAGE_SMALL = "AAAAAAAAAA";
    private static final String UPDATED_ARTIST_IMAGE_SMALL = "BBBBBBBBBB";

    private static final String DEFAULT_ARTIST_IMAGE_MEDIUM = "AAAAAAAAAA";
    private static final String UPDATED_ARTIST_IMAGE_MEDIUM = "BBBBBBBBBB";

    private static final String DEFAULT_ARTIST_IMAGE_LARGE = "AAAAAAAAAA";
    private static final String UPDATED_ARTIST_IMAGE_LARGE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ARTIST_FOLLOWERS = 1;
    private static final Integer UPDATED_ARTIST_FOLLOWERS = 2;

    private static final LocalDate DEFAULT_DATE_ADDED_TO_DB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ADDED_TO_DB = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_LAST_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_LAST_MODIFIED = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_MUSICBRAINZ_ID = "AAAAAAAAAA";
    private static final String UPDATED_MUSICBRAINZ_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/main-artists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MainArtistRepository mainArtistRepository;

    @Mock
    private MainArtistRepository mainArtistRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMainArtistMockMvc;

    private MainArtist mainArtist;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MainArtist createEntity(EntityManager em) {
        MainArtist mainArtist = new MainArtist()
            .artistSpotifyID(DEFAULT_ARTIST_SPOTIFY_ID)
            .artistName(DEFAULT_ARTIST_NAME)
            .artistPopularity(DEFAULT_ARTIST_POPULARITY)
            .artistImageSmall(DEFAULT_ARTIST_IMAGE_SMALL)
            .artistImageMedium(DEFAULT_ARTIST_IMAGE_MEDIUM)
            .artistImageLarge(DEFAULT_ARTIST_IMAGE_LARGE)
            .artistFollowers(DEFAULT_ARTIST_FOLLOWERS)
            .dateAddedToDB(DEFAULT_DATE_ADDED_TO_DB)
            .dateLastModified(DEFAULT_DATE_LAST_MODIFIED)
            .musicbrainzID(DEFAULT_MUSICBRAINZ_ID);
        return mainArtist;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MainArtist createUpdatedEntity(EntityManager em) {
        MainArtist mainArtist = new MainArtist()
            .artistSpotifyID(UPDATED_ARTIST_SPOTIFY_ID)
            .artistName(UPDATED_ARTIST_NAME)
            .artistPopularity(UPDATED_ARTIST_POPULARITY)
            .artistImageSmall(UPDATED_ARTIST_IMAGE_SMALL)
            .artistImageMedium(UPDATED_ARTIST_IMAGE_MEDIUM)
            .artistImageLarge(UPDATED_ARTIST_IMAGE_LARGE)
            .artistFollowers(UPDATED_ARTIST_FOLLOWERS)
            .dateAddedToDB(UPDATED_DATE_ADDED_TO_DB)
            .dateLastModified(UPDATED_DATE_LAST_MODIFIED)
            .musicbrainzID(UPDATED_MUSICBRAINZ_ID);
        return mainArtist;
    }

    @BeforeEach
    public void initTest() {
        mainArtist = createEntity(em);
    }

    @Test
    @Transactional
    void createMainArtist() throws Exception {
        int databaseSizeBeforeCreate = mainArtistRepository.findAll().size();
        // Create the MainArtist
        restMainArtistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mainArtist)))
            .andExpect(status().isCreated());

        // Validate the MainArtist in the database
        List<MainArtist> mainArtistList = mainArtistRepository.findAll();
        assertThat(mainArtistList).hasSize(databaseSizeBeforeCreate + 1);
        MainArtist testMainArtist = mainArtistList.get(mainArtistList.size() - 1);
        assertThat(testMainArtist.getArtistSpotifyID()).isEqualTo(DEFAULT_ARTIST_SPOTIFY_ID);
        assertThat(testMainArtist.getArtistName()).isEqualTo(DEFAULT_ARTIST_NAME);
        assertThat(testMainArtist.getArtistPopularity()).isEqualTo(DEFAULT_ARTIST_POPULARITY);
        assertThat(testMainArtist.getArtistImageSmall()).isEqualTo(DEFAULT_ARTIST_IMAGE_SMALL);
        assertThat(testMainArtist.getArtistImageMedium()).isEqualTo(DEFAULT_ARTIST_IMAGE_MEDIUM);
        assertThat(testMainArtist.getArtistImageLarge()).isEqualTo(DEFAULT_ARTIST_IMAGE_LARGE);
        assertThat(testMainArtist.getArtistFollowers()).isEqualTo(DEFAULT_ARTIST_FOLLOWERS);
        assertThat(testMainArtist.getDateAddedToDB()).isEqualTo(DEFAULT_DATE_ADDED_TO_DB);
        assertThat(testMainArtist.getDateLastModified()).isEqualTo(DEFAULT_DATE_LAST_MODIFIED);
        assertThat(testMainArtist.getMusicbrainzID()).isEqualTo(DEFAULT_MUSICBRAINZ_ID);
    }

    @Test
    @Transactional
    void createMainArtistWithExistingId() throws Exception {
        // Create the MainArtist with an existing ID
        mainArtist.setId(1L);

        int databaseSizeBeforeCreate = mainArtistRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMainArtistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mainArtist)))
            .andExpect(status().isBadRequest());

        // Validate the MainArtist in the database
        List<MainArtist> mainArtistList = mainArtistRepository.findAll();
        assertThat(mainArtistList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkArtistSpotifyIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = mainArtistRepository.findAll().size();
        // set the field null
        mainArtist.setArtistSpotifyID(null);

        // Create the MainArtist, which fails.

        restMainArtistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mainArtist)))
            .andExpect(status().isBadRequest());

        List<MainArtist> mainArtistList = mainArtistRepository.findAll();
        assertThat(mainArtistList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkArtistNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = mainArtistRepository.findAll().size();
        // set the field null
        mainArtist.setArtistName(null);

        // Create the MainArtist, which fails.

        restMainArtistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mainArtist)))
            .andExpect(status().isBadRequest());

        List<MainArtist> mainArtistList = mainArtistRepository.findAll();
        assertThat(mainArtistList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkArtistPopularityIsRequired() throws Exception {
        int databaseSizeBeforeTest = mainArtistRepository.findAll().size();
        // set the field null
        mainArtist.setArtistPopularity(null);

        // Create the MainArtist, which fails.

        restMainArtistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mainArtist)))
            .andExpect(status().isBadRequest());

        List<MainArtist> mainArtistList = mainArtistRepository.findAll();
        assertThat(mainArtistList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMainArtists() throws Exception {
        // Initialize the database
        mainArtistRepository.saveAndFlush(mainArtist);

        // Get all the mainArtistList
        restMainArtistMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mainArtist.getId().intValue())))
            .andExpect(jsonPath("$.[*].artistSpotifyID").value(hasItem(DEFAULT_ARTIST_SPOTIFY_ID)))
            .andExpect(jsonPath("$.[*].artistName").value(hasItem(DEFAULT_ARTIST_NAME)))
            .andExpect(jsonPath("$.[*].artistPopularity").value(hasItem(DEFAULT_ARTIST_POPULARITY)))
            .andExpect(jsonPath("$.[*].artistImageSmall").value(hasItem(DEFAULT_ARTIST_IMAGE_SMALL)))
            .andExpect(jsonPath("$.[*].artistImageMedium").value(hasItem(DEFAULT_ARTIST_IMAGE_MEDIUM)))
            .andExpect(jsonPath("$.[*].artistImageLarge").value(hasItem(DEFAULT_ARTIST_IMAGE_LARGE)))
            .andExpect(jsonPath("$.[*].artistFollowers").value(hasItem(DEFAULT_ARTIST_FOLLOWERS)))
            .andExpect(jsonPath("$.[*].dateAddedToDB").value(hasItem(DEFAULT_DATE_ADDED_TO_DB.toString())))
            .andExpect(jsonPath("$.[*].dateLastModified").value(hasItem(DEFAULT_DATE_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].musicbrainzID").value(hasItem(DEFAULT_MUSICBRAINZ_ID)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMainArtistsWithEagerRelationshipsIsEnabled() throws Exception {
        when(mainArtistRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMainArtistMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(mainArtistRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMainArtistsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(mainArtistRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMainArtistMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(mainArtistRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMainArtist() throws Exception {
        // Initialize the database
        mainArtistRepository.saveAndFlush(mainArtist);

        // Get the mainArtist
        restMainArtistMockMvc
            .perform(get(ENTITY_API_URL_ID, mainArtist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mainArtist.getId().intValue()))
            .andExpect(jsonPath("$.artistSpotifyID").value(DEFAULT_ARTIST_SPOTIFY_ID))
            .andExpect(jsonPath("$.artistName").value(DEFAULT_ARTIST_NAME))
            .andExpect(jsonPath("$.artistPopularity").value(DEFAULT_ARTIST_POPULARITY))
            .andExpect(jsonPath("$.artistImageSmall").value(DEFAULT_ARTIST_IMAGE_SMALL))
            .andExpect(jsonPath("$.artistImageMedium").value(DEFAULT_ARTIST_IMAGE_MEDIUM))
            .andExpect(jsonPath("$.artistImageLarge").value(DEFAULT_ARTIST_IMAGE_LARGE))
            .andExpect(jsonPath("$.artistFollowers").value(DEFAULT_ARTIST_FOLLOWERS))
            .andExpect(jsonPath("$.dateAddedToDB").value(DEFAULT_DATE_ADDED_TO_DB.toString()))
            .andExpect(jsonPath("$.dateLastModified").value(DEFAULT_DATE_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.musicbrainzID").value(DEFAULT_MUSICBRAINZ_ID));
    }

    @Test
    @Transactional
    void getNonExistingMainArtist() throws Exception {
        // Get the mainArtist
        restMainArtistMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMainArtist() throws Exception {
        // Initialize the database
        mainArtistRepository.saveAndFlush(mainArtist);

        int databaseSizeBeforeUpdate = mainArtistRepository.findAll().size();

        // Update the mainArtist
        MainArtist updatedMainArtist = mainArtistRepository.findById(mainArtist.getId()).get();
        // Disconnect from session so that the updates on updatedMainArtist are not directly saved in db
        em.detach(updatedMainArtist);
        updatedMainArtist
            .artistSpotifyID(UPDATED_ARTIST_SPOTIFY_ID)
            .artistName(UPDATED_ARTIST_NAME)
            .artistPopularity(UPDATED_ARTIST_POPULARITY)
            .artistImageSmall(UPDATED_ARTIST_IMAGE_SMALL)
            .artistImageMedium(UPDATED_ARTIST_IMAGE_MEDIUM)
            .artistImageLarge(UPDATED_ARTIST_IMAGE_LARGE)
            .artistFollowers(UPDATED_ARTIST_FOLLOWERS)
            .dateAddedToDB(UPDATED_DATE_ADDED_TO_DB)
            .dateLastModified(UPDATED_DATE_LAST_MODIFIED)
            .musicbrainzID(UPDATED_MUSICBRAINZ_ID);

        restMainArtistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMainArtist.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMainArtist))
            )
            .andExpect(status().isOk());

        // Validate the MainArtist in the database
        List<MainArtist> mainArtistList = mainArtistRepository.findAll();
        assertThat(mainArtistList).hasSize(databaseSizeBeforeUpdate);
        MainArtist testMainArtist = mainArtistList.get(mainArtistList.size() - 1);
        assertThat(testMainArtist.getArtistSpotifyID()).isEqualTo(UPDATED_ARTIST_SPOTIFY_ID);
        assertThat(testMainArtist.getArtistName()).isEqualTo(UPDATED_ARTIST_NAME);
        assertThat(testMainArtist.getArtistPopularity()).isEqualTo(UPDATED_ARTIST_POPULARITY);
        assertThat(testMainArtist.getArtistImageSmall()).isEqualTo(UPDATED_ARTIST_IMAGE_SMALL);
        assertThat(testMainArtist.getArtistImageMedium()).isEqualTo(UPDATED_ARTIST_IMAGE_MEDIUM);
        assertThat(testMainArtist.getArtistImageLarge()).isEqualTo(UPDATED_ARTIST_IMAGE_LARGE);
        assertThat(testMainArtist.getArtistFollowers()).isEqualTo(UPDATED_ARTIST_FOLLOWERS);
        assertThat(testMainArtist.getDateAddedToDB()).isEqualTo(UPDATED_DATE_ADDED_TO_DB);
        assertThat(testMainArtist.getDateLastModified()).isEqualTo(UPDATED_DATE_LAST_MODIFIED);
        assertThat(testMainArtist.getMusicbrainzID()).isEqualTo(UPDATED_MUSICBRAINZ_ID);
    }

    @Test
    @Transactional
    void putNonExistingMainArtist() throws Exception {
        int databaseSizeBeforeUpdate = mainArtistRepository.findAll().size();
        mainArtist.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMainArtistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mainArtist.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mainArtist))
            )
            .andExpect(status().isBadRequest());

        // Validate the MainArtist in the database
        List<MainArtist> mainArtistList = mainArtistRepository.findAll();
        assertThat(mainArtistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMainArtist() throws Exception {
        int databaseSizeBeforeUpdate = mainArtistRepository.findAll().size();
        mainArtist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMainArtistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mainArtist))
            )
            .andExpect(status().isBadRequest());

        // Validate the MainArtist in the database
        List<MainArtist> mainArtistList = mainArtistRepository.findAll();
        assertThat(mainArtistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMainArtist() throws Exception {
        int databaseSizeBeforeUpdate = mainArtistRepository.findAll().size();
        mainArtist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMainArtistMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mainArtist)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MainArtist in the database
        List<MainArtist> mainArtistList = mainArtistRepository.findAll();
        assertThat(mainArtistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMainArtistWithPatch() throws Exception {
        // Initialize the database
        mainArtistRepository.saveAndFlush(mainArtist);

        int databaseSizeBeforeUpdate = mainArtistRepository.findAll().size();

        // Update the mainArtist using partial update
        MainArtist partialUpdatedMainArtist = new MainArtist();
        partialUpdatedMainArtist.setId(mainArtist.getId());

        partialUpdatedMainArtist
            .artistName(UPDATED_ARTIST_NAME)
            .artistImageLarge(UPDATED_ARTIST_IMAGE_LARGE)
            .dateLastModified(UPDATED_DATE_LAST_MODIFIED);

        restMainArtistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMainArtist.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMainArtist))
            )
            .andExpect(status().isOk());

        // Validate the MainArtist in the database
        List<MainArtist> mainArtistList = mainArtistRepository.findAll();
        assertThat(mainArtistList).hasSize(databaseSizeBeforeUpdate);
        MainArtist testMainArtist = mainArtistList.get(mainArtistList.size() - 1);
        assertThat(testMainArtist.getArtistSpotifyID()).isEqualTo(DEFAULT_ARTIST_SPOTIFY_ID);
        assertThat(testMainArtist.getArtistName()).isEqualTo(UPDATED_ARTIST_NAME);
        assertThat(testMainArtist.getArtistPopularity()).isEqualTo(DEFAULT_ARTIST_POPULARITY);
        assertThat(testMainArtist.getArtistImageSmall()).isEqualTo(DEFAULT_ARTIST_IMAGE_SMALL);
        assertThat(testMainArtist.getArtistImageMedium()).isEqualTo(DEFAULT_ARTIST_IMAGE_MEDIUM);
        assertThat(testMainArtist.getArtistImageLarge()).isEqualTo(UPDATED_ARTIST_IMAGE_LARGE);
        assertThat(testMainArtist.getArtistFollowers()).isEqualTo(DEFAULT_ARTIST_FOLLOWERS);
        assertThat(testMainArtist.getDateAddedToDB()).isEqualTo(DEFAULT_DATE_ADDED_TO_DB);
        assertThat(testMainArtist.getDateLastModified()).isEqualTo(UPDATED_DATE_LAST_MODIFIED);
        assertThat(testMainArtist.getMusicbrainzID()).isEqualTo(DEFAULT_MUSICBRAINZ_ID);
    }

    @Test
    @Transactional
    void fullUpdateMainArtistWithPatch() throws Exception {
        // Initialize the database
        mainArtistRepository.saveAndFlush(mainArtist);

        int databaseSizeBeforeUpdate = mainArtistRepository.findAll().size();

        // Update the mainArtist using partial update
        MainArtist partialUpdatedMainArtist = new MainArtist();
        partialUpdatedMainArtist.setId(mainArtist.getId());

        partialUpdatedMainArtist
            .artistSpotifyID(UPDATED_ARTIST_SPOTIFY_ID)
            .artistName(UPDATED_ARTIST_NAME)
            .artistPopularity(UPDATED_ARTIST_POPULARITY)
            .artistImageSmall(UPDATED_ARTIST_IMAGE_SMALL)
            .artistImageMedium(UPDATED_ARTIST_IMAGE_MEDIUM)
            .artistImageLarge(UPDATED_ARTIST_IMAGE_LARGE)
            .artistFollowers(UPDATED_ARTIST_FOLLOWERS)
            .dateAddedToDB(UPDATED_DATE_ADDED_TO_DB)
            .dateLastModified(UPDATED_DATE_LAST_MODIFIED)
            .musicbrainzID(UPDATED_MUSICBRAINZ_ID);

        restMainArtistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMainArtist.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMainArtist))
            )
            .andExpect(status().isOk());

        // Validate the MainArtist in the database
        List<MainArtist> mainArtistList = mainArtistRepository.findAll();
        assertThat(mainArtistList).hasSize(databaseSizeBeforeUpdate);
        MainArtist testMainArtist = mainArtistList.get(mainArtistList.size() - 1);
        assertThat(testMainArtist.getArtistSpotifyID()).isEqualTo(UPDATED_ARTIST_SPOTIFY_ID);
        assertThat(testMainArtist.getArtistName()).isEqualTo(UPDATED_ARTIST_NAME);
        assertThat(testMainArtist.getArtistPopularity()).isEqualTo(UPDATED_ARTIST_POPULARITY);
        assertThat(testMainArtist.getArtistImageSmall()).isEqualTo(UPDATED_ARTIST_IMAGE_SMALL);
        assertThat(testMainArtist.getArtistImageMedium()).isEqualTo(UPDATED_ARTIST_IMAGE_MEDIUM);
        assertThat(testMainArtist.getArtistImageLarge()).isEqualTo(UPDATED_ARTIST_IMAGE_LARGE);
        assertThat(testMainArtist.getArtistFollowers()).isEqualTo(UPDATED_ARTIST_FOLLOWERS);
        assertThat(testMainArtist.getDateAddedToDB()).isEqualTo(UPDATED_DATE_ADDED_TO_DB);
        assertThat(testMainArtist.getDateLastModified()).isEqualTo(UPDATED_DATE_LAST_MODIFIED);
        assertThat(testMainArtist.getMusicbrainzID()).isEqualTo(UPDATED_MUSICBRAINZ_ID);
    }

    @Test
    @Transactional
    void patchNonExistingMainArtist() throws Exception {
        int databaseSizeBeforeUpdate = mainArtistRepository.findAll().size();
        mainArtist.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMainArtistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mainArtist.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mainArtist))
            )
            .andExpect(status().isBadRequest());

        // Validate the MainArtist in the database
        List<MainArtist> mainArtistList = mainArtistRepository.findAll();
        assertThat(mainArtistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMainArtist() throws Exception {
        int databaseSizeBeforeUpdate = mainArtistRepository.findAll().size();
        mainArtist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMainArtistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mainArtist))
            )
            .andExpect(status().isBadRequest());

        // Validate the MainArtist in the database
        List<MainArtist> mainArtistList = mainArtistRepository.findAll();
        assertThat(mainArtistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMainArtist() throws Exception {
        int databaseSizeBeforeUpdate = mainArtistRepository.findAll().size();
        mainArtist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMainArtistMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(mainArtist))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MainArtist in the database
        List<MainArtist> mainArtistList = mainArtistRepository.findAll();
        assertThat(mainArtistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMainArtist() throws Exception {
        // Initialize the database
        mainArtistRepository.saveAndFlush(mainArtist);

        int databaseSizeBeforeDelete = mainArtistRepository.findAll().size();

        // Delete the mainArtist
        restMainArtistMockMvc
            .perform(delete(ENTITY_API_URL_ID, mainArtist.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MainArtist> mainArtistList = mainArtistRepository.findAll();
        assertThat(mainArtistList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

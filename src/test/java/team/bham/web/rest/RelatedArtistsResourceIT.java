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
import team.bham.domain.RelatedArtists;
import team.bham.repository.RelatedArtistsRepository;

/**
 * Integration tests for the {@link RelatedArtistsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RelatedArtistsResourceIT {

    private static final String DEFAULT_MAIN_ARTIST_SPTFY_ID = "AAAAAAAAAA";
    private static final String UPDATED_MAIN_ARTIST_SPTFY_ID = "BBBBBBBBBB";

    private static final String DEFAULT_RELATED_ARTIST_SPOTIFY_ID_1 = "AAAAAAAAAA";
    private static final String UPDATED_RELATED_ARTIST_SPOTIFY_ID_1 = "BBBBBBBBBB";

    private static final String DEFAULT_RELATED_ARTIST_SPOTIFY_ID_2 = "AAAAAAAAAA";
    private static final String UPDATED_RELATED_ARTIST_SPOTIFY_ID_2 = "BBBBBBBBBB";

    private static final String DEFAULT_RELATED_ARTIST_SPOTIFY_ID_3 = "AAAAAAAAAA";
    private static final String UPDATED_RELATED_ARTIST_SPOTIFY_ID_3 = "BBBBBBBBBB";

    private static final String DEFAULT_RELATED_ARTIST_SPOTIFY_ID_4 = "AAAAAAAAAA";
    private static final String UPDATED_RELATED_ARTIST_SPOTIFY_ID_4 = "BBBBBBBBBB";

    private static final String DEFAULT_RELATED_ARTIST_SPOTIFY_ID_5 = "AAAAAAAAAA";
    private static final String UPDATED_RELATED_ARTIST_SPOTIFY_ID_5 = "BBBBBBBBBB";

    private static final String DEFAULT_RELATED_ARTIST_SPOTIFY_ID_6 = "AAAAAAAAAA";
    private static final String UPDATED_RELATED_ARTIST_SPOTIFY_ID_6 = "BBBBBBBBBB";

    private static final String DEFAULT_RELATED_ARTIST_SPOTIFY_ID_7 = "AAAAAAAAAA";
    private static final String UPDATED_RELATED_ARTIST_SPOTIFY_ID_7 = "BBBBBBBBBB";

    private static final String DEFAULT_RELATED_ARTIST_SPOTIFY_ID_8 = "AAAAAAAAAA";
    private static final String UPDATED_RELATED_ARTIST_SPOTIFY_ID_8 = "BBBBBBBBBB";

    private static final String DEFAULT_RELATED_ARTIST_SPOTIFY_ID_9 = "AAAAAAAAAA";
    private static final String UPDATED_RELATED_ARTIST_SPOTIFY_ID_9 = "BBBBBBBBBB";

    private static final String DEFAULT_RELATED_ARTIST_SPOTIFY_ID_10 = "AAAAAAAAAA";
    private static final String UPDATED_RELATED_ARTIST_SPOTIFY_ID_10 = "BBBBBBBBBB";

    private static final String DEFAULT_RELATED_ARTIST_SPOTIFY_ID_11 = "AAAAAAAAAA";
    private static final String UPDATED_RELATED_ARTIST_SPOTIFY_ID_11 = "BBBBBBBBBB";

    private static final String DEFAULT_RELATED_ARTIST_SPOTIFY_ID_12 = "AAAAAAAAAA";
    private static final String UPDATED_RELATED_ARTIST_SPOTIFY_ID_12 = "BBBBBBBBBB";

    private static final String DEFAULT_RELATED_ARTIST_SPOTIFY_ID_13 = "AAAAAAAAAA";
    private static final String UPDATED_RELATED_ARTIST_SPOTIFY_ID_13 = "BBBBBBBBBB";

    private static final String DEFAULT_RELATED_ARTIST_SPOTIFY_ID_14 = "AAAAAAAAAA";
    private static final String UPDATED_RELATED_ARTIST_SPOTIFY_ID_14 = "BBBBBBBBBB";

    private static final String DEFAULT_RELATED_ARTIST_SPOTIFY_ID_15 = "AAAAAAAAAA";
    private static final String UPDATED_RELATED_ARTIST_SPOTIFY_ID_15 = "BBBBBBBBBB";

    private static final String DEFAULT_RELATED_ARTIST_SPOTIFY_ID_16 = "AAAAAAAAAA";
    private static final String UPDATED_RELATED_ARTIST_SPOTIFY_ID_16 = "BBBBBBBBBB";

    private static final String DEFAULT_RELATED_ARTIST_SPOTIFY_ID_17 = "AAAAAAAAAA";
    private static final String UPDATED_RELATED_ARTIST_SPOTIFY_ID_17 = "BBBBBBBBBB";

    private static final String DEFAULT_RELATED_ARTIST_SPOTIFY_ID_18 = "AAAAAAAAAA";
    private static final String UPDATED_RELATED_ARTIST_SPOTIFY_ID_18 = "BBBBBBBBBB";

    private static final String DEFAULT_RELATED_ARTIST_SPOTIFY_ID_19 = "AAAAAAAAAA";
    private static final String UPDATED_RELATED_ARTIST_SPOTIFY_ID_19 = "BBBBBBBBBB";

    private static final String DEFAULT_RELATED_ARTIST_SPOTIFY_ID_20 = "AAAAAAAAAA";
    private static final String UPDATED_RELATED_ARTIST_SPOTIFY_ID_20 = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/related-artists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RelatedArtistsRepository relatedArtistsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRelatedArtistsMockMvc;

    private RelatedArtists relatedArtists;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RelatedArtists createEntity(EntityManager em) {
        RelatedArtists relatedArtists = new RelatedArtists()
            .mainArtistSptfyID(DEFAULT_MAIN_ARTIST_SPTFY_ID)
            .relatedArtistSpotifyID1(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_1)
            .relatedArtistSpotifyID2(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_2)
            .relatedArtistSpotifyID3(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_3)
            .relatedArtistSpotifyID4(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_4)
            .relatedArtistSpotifyID5(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_5)
            .relatedArtistSpotifyID6(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_6)
            .relatedArtistSpotifyID7(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_7)
            .relatedArtistSpotifyID8(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_8)
            .relatedArtistSpotifyID9(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_9)
            .relatedArtistSpotifyID10(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_10)
            .relatedArtistSpotifyID11(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_11)
            .relatedArtistSpotifyID12(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_12)
            .relatedArtistSpotifyID13(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_13)
            .relatedArtistSpotifyID14(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_14)
            .relatedArtistSpotifyID15(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_15)
            .relatedArtistSpotifyID16(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_16)
            .relatedArtistSpotifyID17(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_17)
            .relatedArtistSpotifyID18(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_18)
            .relatedArtistSpotifyID19(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_19)
            .relatedArtistSpotifyID20(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_20);
        return relatedArtists;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RelatedArtists createUpdatedEntity(EntityManager em) {
        RelatedArtists relatedArtists = new RelatedArtists()
            .mainArtistSptfyID(UPDATED_MAIN_ARTIST_SPTFY_ID)
            .relatedArtistSpotifyID1(UPDATED_RELATED_ARTIST_SPOTIFY_ID_1)
            .relatedArtistSpotifyID2(UPDATED_RELATED_ARTIST_SPOTIFY_ID_2)
            .relatedArtistSpotifyID3(UPDATED_RELATED_ARTIST_SPOTIFY_ID_3)
            .relatedArtistSpotifyID4(UPDATED_RELATED_ARTIST_SPOTIFY_ID_4)
            .relatedArtistSpotifyID5(UPDATED_RELATED_ARTIST_SPOTIFY_ID_5)
            .relatedArtistSpotifyID6(UPDATED_RELATED_ARTIST_SPOTIFY_ID_6)
            .relatedArtistSpotifyID7(UPDATED_RELATED_ARTIST_SPOTIFY_ID_7)
            .relatedArtistSpotifyID8(UPDATED_RELATED_ARTIST_SPOTIFY_ID_8)
            .relatedArtistSpotifyID9(UPDATED_RELATED_ARTIST_SPOTIFY_ID_9)
            .relatedArtistSpotifyID10(UPDATED_RELATED_ARTIST_SPOTIFY_ID_10)
            .relatedArtistSpotifyID11(UPDATED_RELATED_ARTIST_SPOTIFY_ID_11)
            .relatedArtistSpotifyID12(UPDATED_RELATED_ARTIST_SPOTIFY_ID_12)
            .relatedArtistSpotifyID13(UPDATED_RELATED_ARTIST_SPOTIFY_ID_13)
            .relatedArtistSpotifyID14(UPDATED_RELATED_ARTIST_SPOTIFY_ID_14)
            .relatedArtistSpotifyID15(UPDATED_RELATED_ARTIST_SPOTIFY_ID_15)
            .relatedArtistSpotifyID16(UPDATED_RELATED_ARTIST_SPOTIFY_ID_16)
            .relatedArtistSpotifyID17(UPDATED_RELATED_ARTIST_SPOTIFY_ID_17)
            .relatedArtistSpotifyID18(UPDATED_RELATED_ARTIST_SPOTIFY_ID_18)
            .relatedArtistSpotifyID19(UPDATED_RELATED_ARTIST_SPOTIFY_ID_19)
            .relatedArtistSpotifyID20(UPDATED_RELATED_ARTIST_SPOTIFY_ID_20);
        return relatedArtists;
    }

    @BeforeEach
    public void initTest() {
        relatedArtists = createEntity(em);
    }

    @Test
    @Transactional
    void createRelatedArtists() throws Exception {
        int databaseSizeBeforeCreate = relatedArtistsRepository.findAll().size();
        // Create the RelatedArtists
        restRelatedArtistsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relatedArtists))
            )
            .andExpect(status().isCreated());

        // Validate the RelatedArtists in the database
        List<RelatedArtists> relatedArtistsList = relatedArtistsRepository.findAll();
        assertThat(relatedArtistsList).hasSize(databaseSizeBeforeCreate + 1);
        RelatedArtists testRelatedArtists = relatedArtistsList.get(relatedArtistsList.size() - 1);
        assertThat(testRelatedArtists.getMainArtistSptfyID()).isEqualTo(DEFAULT_MAIN_ARTIST_SPTFY_ID);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID1()).isEqualTo(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_1);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID2()).isEqualTo(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_2);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID3()).isEqualTo(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_3);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID4()).isEqualTo(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_4);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID5()).isEqualTo(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_5);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID6()).isEqualTo(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_6);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID7()).isEqualTo(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_7);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID8()).isEqualTo(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_8);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID9()).isEqualTo(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_9);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID10()).isEqualTo(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_10);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID11()).isEqualTo(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_11);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID12()).isEqualTo(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_12);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID13()).isEqualTo(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_13);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID14()).isEqualTo(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_14);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID15()).isEqualTo(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_15);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID16()).isEqualTo(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_16);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID17()).isEqualTo(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_17);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID18()).isEqualTo(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_18);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID19()).isEqualTo(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_19);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID20()).isEqualTo(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_20);
    }

    @Test
    @Transactional
    void createRelatedArtistsWithExistingId() throws Exception {
        // Create the RelatedArtists with an existing ID
        relatedArtists.setId(1L);

        int databaseSizeBeforeCreate = relatedArtistsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRelatedArtistsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relatedArtists))
            )
            .andExpect(status().isBadRequest());

        // Validate the RelatedArtists in the database
        List<RelatedArtists> relatedArtistsList = relatedArtistsRepository.findAll();
        assertThat(relatedArtistsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMainArtistSptfyIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = relatedArtistsRepository.findAll().size();
        // set the field null
        relatedArtists.setMainArtistSptfyID(null);

        // Create the RelatedArtists, which fails.

        restRelatedArtistsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relatedArtists))
            )
            .andExpect(status().isBadRequest());

        List<RelatedArtists> relatedArtistsList = relatedArtistsRepository.findAll();
        assertThat(relatedArtistsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRelatedArtistSpotifyID1IsRequired() throws Exception {
        int databaseSizeBeforeTest = relatedArtistsRepository.findAll().size();
        // set the field null
        relatedArtists.setRelatedArtistSpotifyID1(null);

        // Create the RelatedArtists, which fails.

        restRelatedArtistsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relatedArtists))
            )
            .andExpect(status().isBadRequest());

        List<RelatedArtists> relatedArtistsList = relatedArtistsRepository.findAll();
        assertThat(relatedArtistsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRelatedArtistSpotifyID2IsRequired() throws Exception {
        int databaseSizeBeforeTest = relatedArtistsRepository.findAll().size();
        // set the field null
        relatedArtists.setRelatedArtistSpotifyID2(null);

        // Create the RelatedArtists, which fails.

        restRelatedArtistsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relatedArtists))
            )
            .andExpect(status().isBadRequest());

        List<RelatedArtists> relatedArtistsList = relatedArtistsRepository.findAll();
        assertThat(relatedArtistsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRelatedArtistSpotifyID3IsRequired() throws Exception {
        int databaseSizeBeforeTest = relatedArtistsRepository.findAll().size();
        // set the field null
        relatedArtists.setRelatedArtistSpotifyID3(null);

        // Create the RelatedArtists, which fails.

        restRelatedArtistsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relatedArtists))
            )
            .andExpect(status().isBadRequest());

        List<RelatedArtists> relatedArtistsList = relatedArtistsRepository.findAll();
        assertThat(relatedArtistsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRelatedArtistSpotifyID4IsRequired() throws Exception {
        int databaseSizeBeforeTest = relatedArtistsRepository.findAll().size();
        // set the field null
        relatedArtists.setRelatedArtistSpotifyID4(null);

        // Create the RelatedArtists, which fails.

        restRelatedArtistsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relatedArtists))
            )
            .andExpect(status().isBadRequest());

        List<RelatedArtists> relatedArtistsList = relatedArtistsRepository.findAll();
        assertThat(relatedArtistsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRelatedArtistSpotifyID5IsRequired() throws Exception {
        int databaseSizeBeforeTest = relatedArtistsRepository.findAll().size();
        // set the field null
        relatedArtists.setRelatedArtistSpotifyID5(null);

        // Create the RelatedArtists, which fails.

        restRelatedArtistsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relatedArtists))
            )
            .andExpect(status().isBadRequest());

        List<RelatedArtists> relatedArtistsList = relatedArtistsRepository.findAll();
        assertThat(relatedArtistsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRelatedArtistSpotifyID6IsRequired() throws Exception {
        int databaseSizeBeforeTest = relatedArtistsRepository.findAll().size();
        // set the field null
        relatedArtists.setRelatedArtistSpotifyID6(null);

        // Create the RelatedArtists, which fails.

        restRelatedArtistsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relatedArtists))
            )
            .andExpect(status().isBadRequest());

        List<RelatedArtists> relatedArtistsList = relatedArtistsRepository.findAll();
        assertThat(relatedArtistsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRelatedArtistSpotifyID7IsRequired() throws Exception {
        int databaseSizeBeforeTest = relatedArtistsRepository.findAll().size();
        // set the field null
        relatedArtists.setRelatedArtistSpotifyID7(null);

        // Create the RelatedArtists, which fails.

        restRelatedArtistsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relatedArtists))
            )
            .andExpect(status().isBadRequest());

        List<RelatedArtists> relatedArtistsList = relatedArtistsRepository.findAll();
        assertThat(relatedArtistsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRelatedArtistSpotifyID8IsRequired() throws Exception {
        int databaseSizeBeforeTest = relatedArtistsRepository.findAll().size();
        // set the field null
        relatedArtists.setRelatedArtistSpotifyID8(null);

        // Create the RelatedArtists, which fails.

        restRelatedArtistsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relatedArtists))
            )
            .andExpect(status().isBadRequest());

        List<RelatedArtists> relatedArtistsList = relatedArtistsRepository.findAll();
        assertThat(relatedArtistsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRelatedArtistSpotifyID9IsRequired() throws Exception {
        int databaseSizeBeforeTest = relatedArtistsRepository.findAll().size();
        // set the field null
        relatedArtists.setRelatedArtistSpotifyID9(null);

        // Create the RelatedArtists, which fails.

        restRelatedArtistsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relatedArtists))
            )
            .andExpect(status().isBadRequest());

        List<RelatedArtists> relatedArtistsList = relatedArtistsRepository.findAll();
        assertThat(relatedArtistsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRelatedArtistSpotifyID10IsRequired() throws Exception {
        int databaseSizeBeforeTest = relatedArtistsRepository.findAll().size();
        // set the field null
        relatedArtists.setRelatedArtistSpotifyID10(null);

        // Create the RelatedArtists, which fails.

        restRelatedArtistsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relatedArtists))
            )
            .andExpect(status().isBadRequest());

        List<RelatedArtists> relatedArtistsList = relatedArtistsRepository.findAll();
        assertThat(relatedArtistsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRelatedArtistSpotifyID11IsRequired() throws Exception {
        int databaseSizeBeforeTest = relatedArtistsRepository.findAll().size();
        // set the field null
        relatedArtists.setRelatedArtistSpotifyID11(null);

        // Create the RelatedArtists, which fails.

        restRelatedArtistsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relatedArtists))
            )
            .andExpect(status().isBadRequest());

        List<RelatedArtists> relatedArtistsList = relatedArtistsRepository.findAll();
        assertThat(relatedArtistsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRelatedArtistSpotifyID12IsRequired() throws Exception {
        int databaseSizeBeforeTest = relatedArtistsRepository.findAll().size();
        // set the field null
        relatedArtists.setRelatedArtistSpotifyID12(null);

        // Create the RelatedArtists, which fails.

        restRelatedArtistsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relatedArtists))
            )
            .andExpect(status().isBadRequest());

        List<RelatedArtists> relatedArtistsList = relatedArtistsRepository.findAll();
        assertThat(relatedArtistsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRelatedArtistSpotifyID13IsRequired() throws Exception {
        int databaseSizeBeforeTest = relatedArtistsRepository.findAll().size();
        // set the field null
        relatedArtists.setRelatedArtistSpotifyID13(null);

        // Create the RelatedArtists, which fails.

        restRelatedArtistsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relatedArtists))
            )
            .andExpect(status().isBadRequest());

        List<RelatedArtists> relatedArtistsList = relatedArtistsRepository.findAll();
        assertThat(relatedArtistsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRelatedArtistSpotifyID14IsRequired() throws Exception {
        int databaseSizeBeforeTest = relatedArtistsRepository.findAll().size();
        // set the field null
        relatedArtists.setRelatedArtistSpotifyID14(null);

        // Create the RelatedArtists, which fails.

        restRelatedArtistsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relatedArtists))
            )
            .andExpect(status().isBadRequest());

        List<RelatedArtists> relatedArtistsList = relatedArtistsRepository.findAll();
        assertThat(relatedArtistsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRelatedArtistSpotifyID15IsRequired() throws Exception {
        int databaseSizeBeforeTest = relatedArtistsRepository.findAll().size();
        // set the field null
        relatedArtists.setRelatedArtistSpotifyID15(null);

        // Create the RelatedArtists, which fails.

        restRelatedArtistsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relatedArtists))
            )
            .andExpect(status().isBadRequest());

        List<RelatedArtists> relatedArtistsList = relatedArtistsRepository.findAll();
        assertThat(relatedArtistsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRelatedArtistSpotifyID16IsRequired() throws Exception {
        int databaseSizeBeforeTest = relatedArtistsRepository.findAll().size();
        // set the field null
        relatedArtists.setRelatedArtistSpotifyID16(null);

        // Create the RelatedArtists, which fails.

        restRelatedArtistsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relatedArtists))
            )
            .andExpect(status().isBadRequest());

        List<RelatedArtists> relatedArtistsList = relatedArtistsRepository.findAll();
        assertThat(relatedArtistsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRelatedArtistSpotifyID17IsRequired() throws Exception {
        int databaseSizeBeforeTest = relatedArtistsRepository.findAll().size();
        // set the field null
        relatedArtists.setRelatedArtistSpotifyID17(null);

        // Create the RelatedArtists, which fails.

        restRelatedArtistsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relatedArtists))
            )
            .andExpect(status().isBadRequest());

        List<RelatedArtists> relatedArtistsList = relatedArtistsRepository.findAll();
        assertThat(relatedArtistsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRelatedArtistSpotifyID18IsRequired() throws Exception {
        int databaseSizeBeforeTest = relatedArtistsRepository.findAll().size();
        // set the field null
        relatedArtists.setRelatedArtistSpotifyID18(null);

        // Create the RelatedArtists, which fails.

        restRelatedArtistsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relatedArtists))
            )
            .andExpect(status().isBadRequest());

        List<RelatedArtists> relatedArtistsList = relatedArtistsRepository.findAll();
        assertThat(relatedArtistsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRelatedArtistSpotifyID19IsRequired() throws Exception {
        int databaseSizeBeforeTest = relatedArtistsRepository.findAll().size();
        // set the field null
        relatedArtists.setRelatedArtistSpotifyID19(null);

        // Create the RelatedArtists, which fails.

        restRelatedArtistsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relatedArtists))
            )
            .andExpect(status().isBadRequest());

        List<RelatedArtists> relatedArtistsList = relatedArtistsRepository.findAll();
        assertThat(relatedArtistsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRelatedArtistSpotifyID20IsRequired() throws Exception {
        int databaseSizeBeforeTest = relatedArtistsRepository.findAll().size();
        // set the field null
        relatedArtists.setRelatedArtistSpotifyID20(null);

        // Create the RelatedArtists, which fails.

        restRelatedArtistsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relatedArtists))
            )
            .andExpect(status().isBadRequest());

        List<RelatedArtists> relatedArtistsList = relatedArtistsRepository.findAll();
        assertThat(relatedArtistsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRelatedArtists() throws Exception {
        // Initialize the database
        relatedArtistsRepository.saveAndFlush(relatedArtists);

        // Get all the relatedArtistsList
        restRelatedArtistsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(relatedArtists.getId().intValue())))
            .andExpect(jsonPath("$.[*].mainArtistSptfyID").value(hasItem(DEFAULT_MAIN_ARTIST_SPTFY_ID)))
            .andExpect(jsonPath("$.[*].relatedArtistSpotifyID1").value(hasItem(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_1)))
            .andExpect(jsonPath("$.[*].relatedArtistSpotifyID2").value(hasItem(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_2)))
            .andExpect(jsonPath("$.[*].relatedArtistSpotifyID3").value(hasItem(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_3)))
            .andExpect(jsonPath("$.[*].relatedArtistSpotifyID4").value(hasItem(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_4)))
            .andExpect(jsonPath("$.[*].relatedArtistSpotifyID5").value(hasItem(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_5)))
            .andExpect(jsonPath("$.[*].relatedArtistSpotifyID6").value(hasItem(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_6)))
            .andExpect(jsonPath("$.[*].relatedArtistSpotifyID7").value(hasItem(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_7)))
            .andExpect(jsonPath("$.[*].relatedArtistSpotifyID8").value(hasItem(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_8)))
            .andExpect(jsonPath("$.[*].relatedArtistSpotifyID9").value(hasItem(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_9)))
            .andExpect(jsonPath("$.[*].relatedArtistSpotifyID10").value(hasItem(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_10)))
            .andExpect(jsonPath("$.[*].relatedArtistSpotifyID11").value(hasItem(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_11)))
            .andExpect(jsonPath("$.[*].relatedArtistSpotifyID12").value(hasItem(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_12)))
            .andExpect(jsonPath("$.[*].relatedArtistSpotifyID13").value(hasItem(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_13)))
            .andExpect(jsonPath("$.[*].relatedArtistSpotifyID14").value(hasItem(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_14)))
            .andExpect(jsonPath("$.[*].relatedArtistSpotifyID15").value(hasItem(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_15)))
            .andExpect(jsonPath("$.[*].relatedArtistSpotifyID16").value(hasItem(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_16)))
            .andExpect(jsonPath("$.[*].relatedArtistSpotifyID17").value(hasItem(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_17)))
            .andExpect(jsonPath("$.[*].relatedArtistSpotifyID18").value(hasItem(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_18)))
            .andExpect(jsonPath("$.[*].relatedArtistSpotifyID19").value(hasItem(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_19)))
            .andExpect(jsonPath("$.[*].relatedArtistSpotifyID20").value(hasItem(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_20)));
    }

    @Test
    @Transactional
    void getRelatedArtists() throws Exception {
        // Initialize the database
        relatedArtistsRepository.saveAndFlush(relatedArtists);

        // Get the relatedArtists
        restRelatedArtistsMockMvc
            .perform(get(ENTITY_API_URL_ID, relatedArtists.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(relatedArtists.getId().intValue()))
            .andExpect(jsonPath("$.mainArtistSptfyID").value(DEFAULT_MAIN_ARTIST_SPTFY_ID))
            .andExpect(jsonPath("$.relatedArtistSpotifyID1").value(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_1))
            .andExpect(jsonPath("$.relatedArtistSpotifyID2").value(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_2))
            .andExpect(jsonPath("$.relatedArtistSpotifyID3").value(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_3))
            .andExpect(jsonPath("$.relatedArtistSpotifyID4").value(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_4))
            .andExpect(jsonPath("$.relatedArtistSpotifyID5").value(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_5))
            .andExpect(jsonPath("$.relatedArtistSpotifyID6").value(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_6))
            .andExpect(jsonPath("$.relatedArtistSpotifyID7").value(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_7))
            .andExpect(jsonPath("$.relatedArtistSpotifyID8").value(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_8))
            .andExpect(jsonPath("$.relatedArtistSpotifyID9").value(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_9))
            .andExpect(jsonPath("$.relatedArtistSpotifyID10").value(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_10))
            .andExpect(jsonPath("$.relatedArtistSpotifyID11").value(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_11))
            .andExpect(jsonPath("$.relatedArtistSpotifyID12").value(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_12))
            .andExpect(jsonPath("$.relatedArtistSpotifyID13").value(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_13))
            .andExpect(jsonPath("$.relatedArtistSpotifyID14").value(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_14))
            .andExpect(jsonPath("$.relatedArtistSpotifyID15").value(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_15))
            .andExpect(jsonPath("$.relatedArtistSpotifyID16").value(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_16))
            .andExpect(jsonPath("$.relatedArtistSpotifyID17").value(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_17))
            .andExpect(jsonPath("$.relatedArtistSpotifyID18").value(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_18))
            .andExpect(jsonPath("$.relatedArtistSpotifyID19").value(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_19))
            .andExpect(jsonPath("$.relatedArtistSpotifyID20").value(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_20));
    }

    @Test
    @Transactional
    void getNonExistingRelatedArtists() throws Exception {
        // Get the relatedArtists
        restRelatedArtistsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRelatedArtists() throws Exception {
        // Initialize the database
        relatedArtistsRepository.saveAndFlush(relatedArtists);

        int databaseSizeBeforeUpdate = relatedArtistsRepository.findAll().size();

        // Update the relatedArtists
        RelatedArtists updatedRelatedArtists = relatedArtistsRepository.findById(relatedArtists.getId()).get();
        // Disconnect from session so that the updates on updatedRelatedArtists are not directly saved in db
        em.detach(updatedRelatedArtists);
        updatedRelatedArtists
            .mainArtistSptfyID(UPDATED_MAIN_ARTIST_SPTFY_ID)
            .relatedArtistSpotifyID1(UPDATED_RELATED_ARTIST_SPOTIFY_ID_1)
            .relatedArtistSpotifyID2(UPDATED_RELATED_ARTIST_SPOTIFY_ID_2)
            .relatedArtistSpotifyID3(UPDATED_RELATED_ARTIST_SPOTIFY_ID_3)
            .relatedArtistSpotifyID4(UPDATED_RELATED_ARTIST_SPOTIFY_ID_4)
            .relatedArtistSpotifyID5(UPDATED_RELATED_ARTIST_SPOTIFY_ID_5)
            .relatedArtistSpotifyID6(UPDATED_RELATED_ARTIST_SPOTIFY_ID_6)
            .relatedArtistSpotifyID7(UPDATED_RELATED_ARTIST_SPOTIFY_ID_7)
            .relatedArtistSpotifyID8(UPDATED_RELATED_ARTIST_SPOTIFY_ID_8)
            .relatedArtistSpotifyID9(UPDATED_RELATED_ARTIST_SPOTIFY_ID_9)
            .relatedArtistSpotifyID10(UPDATED_RELATED_ARTIST_SPOTIFY_ID_10)
            .relatedArtistSpotifyID11(UPDATED_RELATED_ARTIST_SPOTIFY_ID_11)
            .relatedArtistSpotifyID12(UPDATED_RELATED_ARTIST_SPOTIFY_ID_12)
            .relatedArtistSpotifyID13(UPDATED_RELATED_ARTIST_SPOTIFY_ID_13)
            .relatedArtistSpotifyID14(UPDATED_RELATED_ARTIST_SPOTIFY_ID_14)
            .relatedArtistSpotifyID15(UPDATED_RELATED_ARTIST_SPOTIFY_ID_15)
            .relatedArtistSpotifyID16(UPDATED_RELATED_ARTIST_SPOTIFY_ID_16)
            .relatedArtistSpotifyID17(UPDATED_RELATED_ARTIST_SPOTIFY_ID_17)
            .relatedArtistSpotifyID18(UPDATED_RELATED_ARTIST_SPOTIFY_ID_18)
            .relatedArtistSpotifyID19(UPDATED_RELATED_ARTIST_SPOTIFY_ID_19)
            .relatedArtistSpotifyID20(UPDATED_RELATED_ARTIST_SPOTIFY_ID_20);

        restRelatedArtistsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRelatedArtists.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRelatedArtists))
            )
            .andExpect(status().isOk());

        // Validate the RelatedArtists in the database
        List<RelatedArtists> relatedArtistsList = relatedArtistsRepository.findAll();
        assertThat(relatedArtistsList).hasSize(databaseSizeBeforeUpdate);
        RelatedArtists testRelatedArtists = relatedArtistsList.get(relatedArtistsList.size() - 1);
        assertThat(testRelatedArtists.getMainArtistSptfyID()).isEqualTo(UPDATED_MAIN_ARTIST_SPTFY_ID);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID1()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_1);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID2()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_2);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID3()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_3);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID4()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_4);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID5()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_5);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID6()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_6);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID7()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_7);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID8()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_8);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID9()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_9);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID10()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_10);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID11()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_11);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID12()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_12);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID13()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_13);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID14()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_14);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID15()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_15);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID16()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_16);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID17()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_17);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID18()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_18);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID19()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_19);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID20()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_20);
    }

    @Test
    @Transactional
    void putNonExistingRelatedArtists() throws Exception {
        int databaseSizeBeforeUpdate = relatedArtistsRepository.findAll().size();
        relatedArtists.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRelatedArtistsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, relatedArtists.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(relatedArtists))
            )
            .andExpect(status().isBadRequest());

        // Validate the RelatedArtists in the database
        List<RelatedArtists> relatedArtistsList = relatedArtistsRepository.findAll();
        assertThat(relatedArtistsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRelatedArtists() throws Exception {
        int databaseSizeBeforeUpdate = relatedArtistsRepository.findAll().size();
        relatedArtists.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRelatedArtistsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(relatedArtists))
            )
            .andExpect(status().isBadRequest());

        // Validate the RelatedArtists in the database
        List<RelatedArtists> relatedArtistsList = relatedArtistsRepository.findAll();
        assertThat(relatedArtistsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRelatedArtists() throws Exception {
        int databaseSizeBeforeUpdate = relatedArtistsRepository.findAll().size();
        relatedArtists.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRelatedArtistsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relatedArtists)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RelatedArtists in the database
        List<RelatedArtists> relatedArtistsList = relatedArtistsRepository.findAll();
        assertThat(relatedArtistsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRelatedArtistsWithPatch() throws Exception {
        // Initialize the database
        relatedArtistsRepository.saveAndFlush(relatedArtists);

        int databaseSizeBeforeUpdate = relatedArtistsRepository.findAll().size();

        // Update the relatedArtists using partial update
        RelatedArtists partialUpdatedRelatedArtists = new RelatedArtists();
        partialUpdatedRelatedArtists.setId(relatedArtists.getId());

        partialUpdatedRelatedArtists
            .relatedArtistSpotifyID1(UPDATED_RELATED_ARTIST_SPOTIFY_ID_1)
            .relatedArtistSpotifyID2(UPDATED_RELATED_ARTIST_SPOTIFY_ID_2)
            .relatedArtistSpotifyID4(UPDATED_RELATED_ARTIST_SPOTIFY_ID_4)
            .relatedArtistSpotifyID5(UPDATED_RELATED_ARTIST_SPOTIFY_ID_5)
            .relatedArtistSpotifyID6(UPDATED_RELATED_ARTIST_SPOTIFY_ID_6)
            .relatedArtistSpotifyID7(UPDATED_RELATED_ARTIST_SPOTIFY_ID_7)
            .relatedArtistSpotifyID15(UPDATED_RELATED_ARTIST_SPOTIFY_ID_15)
            .relatedArtistSpotifyID17(UPDATED_RELATED_ARTIST_SPOTIFY_ID_17)
            .relatedArtistSpotifyID18(UPDATED_RELATED_ARTIST_SPOTIFY_ID_18)
            .relatedArtistSpotifyID19(UPDATED_RELATED_ARTIST_SPOTIFY_ID_19)
            .relatedArtistSpotifyID20(UPDATED_RELATED_ARTIST_SPOTIFY_ID_20);

        restRelatedArtistsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRelatedArtists.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRelatedArtists))
            )
            .andExpect(status().isOk());

        // Validate the RelatedArtists in the database
        List<RelatedArtists> relatedArtistsList = relatedArtistsRepository.findAll();
        assertThat(relatedArtistsList).hasSize(databaseSizeBeforeUpdate);
        RelatedArtists testRelatedArtists = relatedArtistsList.get(relatedArtistsList.size() - 1);
        assertThat(testRelatedArtists.getMainArtistSptfyID()).isEqualTo(DEFAULT_MAIN_ARTIST_SPTFY_ID);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID1()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_1);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID2()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_2);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID3()).isEqualTo(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_3);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID4()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_4);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID5()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_5);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID6()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_6);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID7()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_7);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID8()).isEqualTo(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_8);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID9()).isEqualTo(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_9);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID10()).isEqualTo(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_10);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID11()).isEqualTo(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_11);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID12()).isEqualTo(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_12);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID13()).isEqualTo(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_13);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID14()).isEqualTo(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_14);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID15()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_15);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID16()).isEqualTo(DEFAULT_RELATED_ARTIST_SPOTIFY_ID_16);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID17()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_17);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID18()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_18);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID19()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_19);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID20()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_20);
    }

    @Test
    @Transactional
    void fullUpdateRelatedArtistsWithPatch() throws Exception {
        // Initialize the database
        relatedArtistsRepository.saveAndFlush(relatedArtists);

        int databaseSizeBeforeUpdate = relatedArtistsRepository.findAll().size();

        // Update the relatedArtists using partial update
        RelatedArtists partialUpdatedRelatedArtists = new RelatedArtists();
        partialUpdatedRelatedArtists.setId(relatedArtists.getId());

        partialUpdatedRelatedArtists
            .mainArtistSptfyID(UPDATED_MAIN_ARTIST_SPTFY_ID)
            .relatedArtistSpotifyID1(UPDATED_RELATED_ARTIST_SPOTIFY_ID_1)
            .relatedArtistSpotifyID2(UPDATED_RELATED_ARTIST_SPOTIFY_ID_2)
            .relatedArtistSpotifyID3(UPDATED_RELATED_ARTIST_SPOTIFY_ID_3)
            .relatedArtistSpotifyID4(UPDATED_RELATED_ARTIST_SPOTIFY_ID_4)
            .relatedArtistSpotifyID5(UPDATED_RELATED_ARTIST_SPOTIFY_ID_5)
            .relatedArtistSpotifyID6(UPDATED_RELATED_ARTIST_SPOTIFY_ID_6)
            .relatedArtistSpotifyID7(UPDATED_RELATED_ARTIST_SPOTIFY_ID_7)
            .relatedArtistSpotifyID8(UPDATED_RELATED_ARTIST_SPOTIFY_ID_8)
            .relatedArtistSpotifyID9(UPDATED_RELATED_ARTIST_SPOTIFY_ID_9)
            .relatedArtistSpotifyID10(UPDATED_RELATED_ARTIST_SPOTIFY_ID_10)
            .relatedArtistSpotifyID11(UPDATED_RELATED_ARTIST_SPOTIFY_ID_11)
            .relatedArtistSpotifyID12(UPDATED_RELATED_ARTIST_SPOTIFY_ID_12)
            .relatedArtistSpotifyID13(UPDATED_RELATED_ARTIST_SPOTIFY_ID_13)
            .relatedArtistSpotifyID14(UPDATED_RELATED_ARTIST_SPOTIFY_ID_14)
            .relatedArtistSpotifyID15(UPDATED_RELATED_ARTIST_SPOTIFY_ID_15)
            .relatedArtistSpotifyID16(UPDATED_RELATED_ARTIST_SPOTIFY_ID_16)
            .relatedArtistSpotifyID17(UPDATED_RELATED_ARTIST_SPOTIFY_ID_17)
            .relatedArtistSpotifyID18(UPDATED_RELATED_ARTIST_SPOTIFY_ID_18)
            .relatedArtistSpotifyID19(UPDATED_RELATED_ARTIST_SPOTIFY_ID_19)
            .relatedArtistSpotifyID20(UPDATED_RELATED_ARTIST_SPOTIFY_ID_20);

        restRelatedArtistsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRelatedArtists.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRelatedArtists))
            )
            .andExpect(status().isOk());

        // Validate the RelatedArtists in the database
        List<RelatedArtists> relatedArtistsList = relatedArtistsRepository.findAll();
        assertThat(relatedArtistsList).hasSize(databaseSizeBeforeUpdate);
        RelatedArtists testRelatedArtists = relatedArtistsList.get(relatedArtistsList.size() - 1);
        assertThat(testRelatedArtists.getMainArtistSptfyID()).isEqualTo(UPDATED_MAIN_ARTIST_SPTFY_ID);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID1()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_1);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID2()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_2);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID3()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_3);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID4()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_4);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID5()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_5);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID6()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_6);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID7()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_7);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID8()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_8);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID9()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_9);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID10()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_10);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID11()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_11);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID12()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_12);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID13()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_13);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID14()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_14);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID15()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_15);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID16()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_16);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID17()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_17);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID18()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_18);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID19()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_19);
        assertThat(testRelatedArtists.getRelatedArtistSpotifyID20()).isEqualTo(UPDATED_RELATED_ARTIST_SPOTIFY_ID_20);
    }

    @Test
    @Transactional
    void patchNonExistingRelatedArtists() throws Exception {
        int databaseSizeBeforeUpdate = relatedArtistsRepository.findAll().size();
        relatedArtists.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRelatedArtistsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, relatedArtists.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(relatedArtists))
            )
            .andExpect(status().isBadRequest());

        // Validate the RelatedArtists in the database
        List<RelatedArtists> relatedArtistsList = relatedArtistsRepository.findAll();
        assertThat(relatedArtistsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRelatedArtists() throws Exception {
        int databaseSizeBeforeUpdate = relatedArtistsRepository.findAll().size();
        relatedArtists.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRelatedArtistsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(relatedArtists))
            )
            .andExpect(status().isBadRequest());

        // Validate the RelatedArtists in the database
        List<RelatedArtists> relatedArtistsList = relatedArtistsRepository.findAll();
        assertThat(relatedArtistsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRelatedArtists() throws Exception {
        int databaseSizeBeforeUpdate = relatedArtistsRepository.findAll().size();
        relatedArtists.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRelatedArtistsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(relatedArtists))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RelatedArtists in the database
        List<RelatedArtists> relatedArtistsList = relatedArtistsRepository.findAll();
        assertThat(relatedArtistsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRelatedArtists() throws Exception {
        // Initialize the database
        relatedArtistsRepository.saveAndFlush(relatedArtists);

        int databaseSizeBeforeDelete = relatedArtistsRepository.findAll().size();

        // Delete the relatedArtists
        restRelatedArtistsMockMvc
            .perform(delete(ENTITY_API_URL_ID, relatedArtists.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RelatedArtists> relatedArtistsList = relatedArtistsRepository.findAll();
        assertThat(relatedArtistsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

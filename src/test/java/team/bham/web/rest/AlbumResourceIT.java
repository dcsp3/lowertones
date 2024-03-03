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
import team.bham.domain.Album;
import team.bham.domain.enumeration.AlbumType;
import team.bham.domain.enumeration.ReleaseDatePrecision;
import team.bham.repository.AlbumRepository;

/**
 * Integration tests for the {@link AlbumResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlbumResourceIT {

    private static final String DEFAULT_ALBUM_SPOTIFY_ID = "AAAAAAAAAA";
    private static final String UPDATED_ALBUM_SPOTIFY_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ALBUM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ALBUM_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ALBUM_COVER_ART = "AAAAAAAAAA";
    private static final String UPDATED_ALBUM_COVER_ART = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ALBUM_RELEASE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ALBUM_RELEASE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final ReleaseDatePrecision DEFAULT_RELEASE_DATE_PRECISION = ReleaseDatePrecision.YEAR;
    private static final ReleaseDatePrecision UPDATED_RELEASE_DATE_PRECISION = ReleaseDatePrecision.MONTH;

    private static final Integer DEFAULT_ALBUM_POPULARITY = 1;
    private static final Integer UPDATED_ALBUM_POPULARITY = 2;

    private static final AlbumType DEFAULT_ALBUM_TYPE = AlbumType.ALBUM;
    private static final AlbumType UPDATED_ALBUM_TYPE = AlbumType.SINGLE;

    private static final String DEFAULT_SPOTIFY_ALBUM_UPC = "AAAAAAAAAA";
    private static final String UPDATED_SPOTIFY_ALBUM_UPC = "BBBBBBBBBB";

    private static final String DEFAULT_SPOTIFY_ALBUM_EAN = "AAAAAAAAAA";
    private static final String UPDATED_SPOTIFY_ALBUM_EAN = "BBBBBBBBBB";

    private static final String DEFAULT_SPOTIFY_ALBUM_ISRC = "AAAAAAAAAA";
    private static final String UPDATED_SPOTIFY_ALBUM_ISRC = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_ADDED_TO_DB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ADDED_TO_DB = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_LAST_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_LAST_MODIFIED = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_MUSICBRAINZ_METADATA_ADDED = false;
    private static final Boolean UPDATED_MUSICBRAINZ_METADATA_ADDED = true;

    private static final String DEFAULT_MUSICBRAINZ_ID = "AAAAAAAAAA";
    private static final String UPDATED_MUSICBRAINZ_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/albums";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlbumMockMvc;

    private Album album;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Album createEntity(EntityManager em) {
        Album album = new Album()
            .albumSpotifyID(DEFAULT_ALBUM_SPOTIFY_ID)
            .albumName(DEFAULT_ALBUM_NAME)
            .albumCoverArt(DEFAULT_ALBUM_COVER_ART)
            .albumReleaseDate(DEFAULT_ALBUM_RELEASE_DATE)
            .releaseDatePrecision(DEFAULT_RELEASE_DATE_PRECISION)
            .albumPopularity(DEFAULT_ALBUM_POPULARITY)
            .albumType(DEFAULT_ALBUM_TYPE)
            .spotifyAlbumUPC(DEFAULT_SPOTIFY_ALBUM_UPC)
            .spotifyAlbumEAN(DEFAULT_SPOTIFY_ALBUM_EAN)
            .spotifyAlbumISRC(DEFAULT_SPOTIFY_ALBUM_ISRC)
            .dateAddedToDB(DEFAULT_DATE_ADDED_TO_DB)
            .dateLastModified(DEFAULT_DATE_LAST_MODIFIED)
            .musicbrainzMetadataAdded(DEFAULT_MUSICBRAINZ_METADATA_ADDED)
            .musicbrainzID(DEFAULT_MUSICBRAINZ_ID);
        return album;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Album createUpdatedEntity(EntityManager em) {
        Album album = new Album()
            .albumSpotifyID(UPDATED_ALBUM_SPOTIFY_ID)
            .albumName(UPDATED_ALBUM_NAME)
            .albumCoverArt(UPDATED_ALBUM_COVER_ART)
            .albumReleaseDate(UPDATED_ALBUM_RELEASE_DATE)
            .releaseDatePrecision(UPDATED_RELEASE_DATE_PRECISION)
            .albumPopularity(UPDATED_ALBUM_POPULARITY)
            .albumType(UPDATED_ALBUM_TYPE)
            .spotifyAlbumUPC(UPDATED_SPOTIFY_ALBUM_UPC)
            .spotifyAlbumEAN(UPDATED_SPOTIFY_ALBUM_EAN)
            .spotifyAlbumISRC(UPDATED_SPOTIFY_ALBUM_ISRC)
            .dateAddedToDB(UPDATED_DATE_ADDED_TO_DB)
            .dateLastModified(UPDATED_DATE_LAST_MODIFIED)
            .musicbrainzMetadataAdded(UPDATED_MUSICBRAINZ_METADATA_ADDED)
            .musicbrainzID(UPDATED_MUSICBRAINZ_ID);
        return album;
    }

    @BeforeEach
    public void initTest() {
        album = createEntity(em);
    }

    @Test
    @Transactional
    void createAlbum() throws Exception {
        int databaseSizeBeforeCreate = albumRepository.findAll().size();
        // Create the Album
        restAlbumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(album)))
            .andExpect(status().isCreated());

        // Validate the Album in the database
        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeCreate + 1);
        Album testAlbum = albumList.get(albumList.size() - 1);
        assertThat(testAlbum.getAlbumSpotifyID()).isEqualTo(DEFAULT_ALBUM_SPOTIFY_ID);
        assertThat(testAlbum.getAlbumName()).isEqualTo(DEFAULT_ALBUM_NAME);
        assertThat(testAlbum.getAlbumCoverArt()).isEqualTo(DEFAULT_ALBUM_COVER_ART);
        assertThat(testAlbum.getAlbumReleaseDate()).isEqualTo(DEFAULT_ALBUM_RELEASE_DATE);
        assertThat(testAlbum.getReleaseDatePrecision()).isEqualTo(DEFAULT_RELEASE_DATE_PRECISION);
        assertThat(testAlbum.getAlbumPopularity()).isEqualTo(DEFAULT_ALBUM_POPULARITY);
        assertThat(testAlbum.getAlbumType()).isEqualTo(DEFAULT_ALBUM_TYPE);
        assertThat(testAlbum.getSpotifyAlbumUPC()).isEqualTo(DEFAULT_SPOTIFY_ALBUM_UPC);
        assertThat(testAlbum.getSpotifyAlbumEAN()).isEqualTo(DEFAULT_SPOTIFY_ALBUM_EAN);
        assertThat(testAlbum.getSpotifyAlbumISRC()).isEqualTo(DEFAULT_SPOTIFY_ALBUM_ISRC);
        assertThat(testAlbum.getDateAddedToDB()).isEqualTo(DEFAULT_DATE_ADDED_TO_DB);
        assertThat(testAlbum.getDateLastModified()).isEqualTo(DEFAULT_DATE_LAST_MODIFIED);
        assertThat(testAlbum.getMusicbrainzMetadataAdded()).isEqualTo(DEFAULT_MUSICBRAINZ_METADATA_ADDED);
        assertThat(testAlbum.getMusicbrainzID()).isEqualTo(DEFAULT_MUSICBRAINZ_ID);
    }

    @Test
    @Transactional
    void createAlbumWithExistingId() throws Exception {
        // Create the Album with an existing ID
        album.setId(1L);

        int databaseSizeBeforeCreate = albumRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlbumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(album)))
            .andExpect(status().isBadRequest());

        // Validate the Album in the database
        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAlbumSpotifyIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = albumRepository.findAll().size();
        // set the field null
        album.setAlbumSpotifyID(null);

        // Create the Album, which fails.

        restAlbumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(album)))
            .andExpect(status().isBadRequest());

        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAlbumNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = albumRepository.findAll().size();
        // set the field null
        album.setAlbumName(null);

        // Create the Album, which fails.

        restAlbumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(album)))
            .andExpect(status().isBadRequest());

        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAlbumCoverArtIsRequired() throws Exception {
        int databaseSizeBeforeTest = albumRepository.findAll().size();
        // set the field null
        album.setAlbumCoverArt(null);

        // Create the Album, which fails.

        restAlbumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(album)))
            .andExpect(status().isBadRequest());

        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAlbumReleaseDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = albumRepository.findAll().size();
        // set the field null
        album.setAlbumReleaseDate(null);

        // Create the Album, which fails.

        restAlbumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(album)))
            .andExpect(status().isBadRequest());

        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReleaseDatePrecisionIsRequired() throws Exception {
        int databaseSizeBeforeTest = albumRepository.findAll().size();
        // set the field null
        album.setReleaseDatePrecision(null);

        // Create the Album, which fails.

        restAlbumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(album)))
            .andExpect(status().isBadRequest());

        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAlbumPopularityIsRequired() throws Exception {
        int databaseSizeBeforeTest = albumRepository.findAll().size();
        // set the field null
        album.setAlbumPopularity(null);

        // Create the Album, which fails.

        restAlbumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(album)))
            .andExpect(status().isBadRequest());

        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAlbumTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = albumRepository.findAll().size();
        // set the field null
        album.setAlbumType(null);

        // Create the Album, which fails.

        restAlbumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(album)))
            .andExpect(status().isBadRequest());

        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateAddedToDBIsRequired() throws Exception {
        int databaseSizeBeforeTest = albumRepository.findAll().size();
        // set the field null
        album.setDateAddedToDB(null);

        // Create the Album, which fails.

        restAlbumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(album)))
            .andExpect(status().isBadRequest());

        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateLastModifiedIsRequired() throws Exception {
        int databaseSizeBeforeTest = albumRepository.findAll().size();
        // set the field null
        album.setDateLastModified(null);

        // Create the Album, which fails.

        restAlbumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(album)))
            .andExpect(status().isBadRequest());

        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMusicbrainzMetadataAddedIsRequired() throws Exception {
        int databaseSizeBeforeTest = albumRepository.findAll().size();
        // set the field null
        album.setMusicbrainzMetadataAdded(null);

        // Create the Album, which fails.

        restAlbumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(album)))
            .andExpect(status().isBadRequest());

        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlbums() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList
        restAlbumMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(album.getId().intValue())))
            .andExpect(jsonPath("$.[*].albumSpotifyID").value(hasItem(DEFAULT_ALBUM_SPOTIFY_ID)))
            .andExpect(jsonPath("$.[*].albumName").value(hasItem(DEFAULT_ALBUM_NAME)))
            .andExpect(jsonPath("$.[*].albumCoverArt").value(hasItem(DEFAULT_ALBUM_COVER_ART)))
            .andExpect(jsonPath("$.[*].albumReleaseDate").value(hasItem(DEFAULT_ALBUM_RELEASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].releaseDatePrecision").value(hasItem(DEFAULT_RELEASE_DATE_PRECISION.toString())))
            .andExpect(jsonPath("$.[*].albumPopularity").value(hasItem(DEFAULT_ALBUM_POPULARITY)))
            .andExpect(jsonPath("$.[*].albumType").value(hasItem(DEFAULT_ALBUM_TYPE.toString())))
            .andExpect(jsonPath("$.[*].spotifyAlbumUPC").value(hasItem(DEFAULT_SPOTIFY_ALBUM_UPC)))
            .andExpect(jsonPath("$.[*].spotifyAlbumEAN").value(hasItem(DEFAULT_SPOTIFY_ALBUM_EAN)))
            .andExpect(jsonPath("$.[*].spotifyAlbumISRC").value(hasItem(DEFAULT_SPOTIFY_ALBUM_ISRC)))
            .andExpect(jsonPath("$.[*].dateAddedToDB").value(hasItem(DEFAULT_DATE_ADDED_TO_DB.toString())))
            .andExpect(jsonPath("$.[*].dateLastModified").value(hasItem(DEFAULT_DATE_LAST_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].musicbrainzMetadataAdded").value(hasItem(DEFAULT_MUSICBRAINZ_METADATA_ADDED.booleanValue())))
            .andExpect(jsonPath("$.[*].musicbrainzID").value(hasItem(DEFAULT_MUSICBRAINZ_ID)));
    }

    @Test
    @Transactional
    void getAlbum() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get the album
        restAlbumMockMvc
            .perform(get(ENTITY_API_URL_ID, album.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(album.getId().intValue()))
            .andExpect(jsonPath("$.albumSpotifyID").value(DEFAULT_ALBUM_SPOTIFY_ID))
            .andExpect(jsonPath("$.albumName").value(DEFAULT_ALBUM_NAME))
            .andExpect(jsonPath("$.albumCoverArt").value(DEFAULT_ALBUM_COVER_ART))
            .andExpect(jsonPath("$.albumReleaseDate").value(DEFAULT_ALBUM_RELEASE_DATE.toString()))
            .andExpect(jsonPath("$.releaseDatePrecision").value(DEFAULT_RELEASE_DATE_PRECISION.toString()))
            .andExpect(jsonPath("$.albumPopularity").value(DEFAULT_ALBUM_POPULARITY))
            .andExpect(jsonPath("$.albumType").value(DEFAULT_ALBUM_TYPE.toString()))
            .andExpect(jsonPath("$.spotifyAlbumUPC").value(DEFAULT_SPOTIFY_ALBUM_UPC))
            .andExpect(jsonPath("$.spotifyAlbumEAN").value(DEFAULT_SPOTIFY_ALBUM_EAN))
            .andExpect(jsonPath("$.spotifyAlbumISRC").value(DEFAULT_SPOTIFY_ALBUM_ISRC))
            .andExpect(jsonPath("$.dateAddedToDB").value(DEFAULT_DATE_ADDED_TO_DB.toString()))
            .andExpect(jsonPath("$.dateLastModified").value(DEFAULT_DATE_LAST_MODIFIED.toString()))
            .andExpect(jsonPath("$.musicbrainzMetadataAdded").value(DEFAULT_MUSICBRAINZ_METADATA_ADDED.booleanValue()))
            .andExpect(jsonPath("$.musicbrainzID").value(DEFAULT_MUSICBRAINZ_ID));
    }

    @Test
    @Transactional
    void getNonExistingAlbum() throws Exception {
        // Get the album
        restAlbumMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlbum() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        int databaseSizeBeforeUpdate = albumRepository.findAll().size();

        // Update the album
        Album updatedAlbum = albumRepository.findById(album.getId()).get();
        // Disconnect from session so that the updates on updatedAlbum are not directly saved in db
        em.detach(updatedAlbum);
        updatedAlbum
            .albumSpotifyID(UPDATED_ALBUM_SPOTIFY_ID)
            .albumName(UPDATED_ALBUM_NAME)
            .albumCoverArt(UPDATED_ALBUM_COVER_ART)
            .albumReleaseDate(UPDATED_ALBUM_RELEASE_DATE)
            .releaseDatePrecision(UPDATED_RELEASE_DATE_PRECISION)
            .albumPopularity(UPDATED_ALBUM_POPULARITY)
            .albumType(UPDATED_ALBUM_TYPE)
            .spotifyAlbumUPC(UPDATED_SPOTIFY_ALBUM_UPC)
            .spotifyAlbumEAN(UPDATED_SPOTIFY_ALBUM_EAN)
            .spotifyAlbumISRC(UPDATED_SPOTIFY_ALBUM_ISRC)
            .dateAddedToDB(UPDATED_DATE_ADDED_TO_DB)
            .dateLastModified(UPDATED_DATE_LAST_MODIFIED)
            .musicbrainzMetadataAdded(UPDATED_MUSICBRAINZ_METADATA_ADDED)
            .musicbrainzID(UPDATED_MUSICBRAINZ_ID);

        restAlbumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlbum.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAlbum))
            )
            .andExpect(status().isOk());

        // Validate the Album in the database
        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeUpdate);
        Album testAlbum = albumList.get(albumList.size() - 1);
        assertThat(testAlbum.getAlbumSpotifyID()).isEqualTo(UPDATED_ALBUM_SPOTIFY_ID);
        assertThat(testAlbum.getAlbumName()).isEqualTo(UPDATED_ALBUM_NAME);
        assertThat(testAlbum.getAlbumCoverArt()).isEqualTo(UPDATED_ALBUM_COVER_ART);
        assertThat(testAlbum.getAlbumReleaseDate()).isEqualTo(UPDATED_ALBUM_RELEASE_DATE);
        assertThat(testAlbum.getReleaseDatePrecision()).isEqualTo(UPDATED_RELEASE_DATE_PRECISION);
        assertThat(testAlbum.getAlbumPopularity()).isEqualTo(UPDATED_ALBUM_POPULARITY);
        assertThat(testAlbum.getAlbumType()).isEqualTo(UPDATED_ALBUM_TYPE);
        assertThat(testAlbum.getSpotifyAlbumUPC()).isEqualTo(UPDATED_SPOTIFY_ALBUM_UPC);
        assertThat(testAlbum.getSpotifyAlbumEAN()).isEqualTo(UPDATED_SPOTIFY_ALBUM_EAN);
        assertThat(testAlbum.getSpotifyAlbumISRC()).isEqualTo(UPDATED_SPOTIFY_ALBUM_ISRC);
        assertThat(testAlbum.getDateAddedToDB()).isEqualTo(UPDATED_DATE_ADDED_TO_DB);
        assertThat(testAlbum.getDateLastModified()).isEqualTo(UPDATED_DATE_LAST_MODIFIED);
        assertThat(testAlbum.getMusicbrainzMetadataAdded()).isEqualTo(UPDATED_MUSICBRAINZ_METADATA_ADDED);
        assertThat(testAlbum.getMusicbrainzID()).isEqualTo(UPDATED_MUSICBRAINZ_ID);
    }

    @Test
    @Transactional
    void putNonExistingAlbum() throws Exception {
        int databaseSizeBeforeUpdate = albumRepository.findAll().size();
        album.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlbumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, album.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(album))
            )
            .andExpect(status().isBadRequest());

        // Validate the Album in the database
        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlbum() throws Exception {
        int databaseSizeBeforeUpdate = albumRepository.findAll().size();
        album.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlbumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(album))
            )
            .andExpect(status().isBadRequest());

        // Validate the Album in the database
        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlbum() throws Exception {
        int databaseSizeBeforeUpdate = albumRepository.findAll().size();
        album.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlbumMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(album)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Album in the database
        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlbumWithPatch() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        int databaseSizeBeforeUpdate = albumRepository.findAll().size();

        // Update the album using partial update
        Album partialUpdatedAlbum = new Album();
        partialUpdatedAlbum.setId(album.getId());

        partialUpdatedAlbum
            .albumName(UPDATED_ALBUM_NAME)
            .albumCoverArt(UPDATED_ALBUM_COVER_ART)
            .releaseDatePrecision(UPDATED_RELEASE_DATE_PRECISION)
            .albumPopularity(UPDATED_ALBUM_POPULARITY)
            .spotifyAlbumUPC(UPDATED_SPOTIFY_ALBUM_UPC)
            .dateLastModified(UPDATED_DATE_LAST_MODIFIED);

        restAlbumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlbum.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAlbum))
            )
            .andExpect(status().isOk());

        // Validate the Album in the database
        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeUpdate);
        Album testAlbum = albumList.get(albumList.size() - 1);
        assertThat(testAlbum.getAlbumSpotifyID()).isEqualTo(DEFAULT_ALBUM_SPOTIFY_ID);
        assertThat(testAlbum.getAlbumName()).isEqualTo(UPDATED_ALBUM_NAME);
        assertThat(testAlbum.getAlbumCoverArt()).isEqualTo(UPDATED_ALBUM_COVER_ART);
        assertThat(testAlbum.getAlbumReleaseDate()).isEqualTo(DEFAULT_ALBUM_RELEASE_DATE);
        assertThat(testAlbum.getReleaseDatePrecision()).isEqualTo(UPDATED_RELEASE_DATE_PRECISION);
        assertThat(testAlbum.getAlbumPopularity()).isEqualTo(UPDATED_ALBUM_POPULARITY);
        assertThat(testAlbum.getAlbumType()).isEqualTo(DEFAULT_ALBUM_TYPE);
        assertThat(testAlbum.getSpotifyAlbumUPC()).isEqualTo(UPDATED_SPOTIFY_ALBUM_UPC);
        assertThat(testAlbum.getSpotifyAlbumEAN()).isEqualTo(DEFAULT_SPOTIFY_ALBUM_EAN);
        assertThat(testAlbum.getSpotifyAlbumISRC()).isEqualTo(DEFAULT_SPOTIFY_ALBUM_ISRC);
        assertThat(testAlbum.getDateAddedToDB()).isEqualTo(DEFAULT_DATE_ADDED_TO_DB);
        assertThat(testAlbum.getDateLastModified()).isEqualTo(UPDATED_DATE_LAST_MODIFIED);
        assertThat(testAlbum.getMusicbrainzMetadataAdded()).isEqualTo(DEFAULT_MUSICBRAINZ_METADATA_ADDED);
        assertThat(testAlbum.getMusicbrainzID()).isEqualTo(DEFAULT_MUSICBRAINZ_ID);
    }

    @Test
    @Transactional
    void fullUpdateAlbumWithPatch() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        int databaseSizeBeforeUpdate = albumRepository.findAll().size();

        // Update the album using partial update
        Album partialUpdatedAlbum = new Album();
        partialUpdatedAlbum.setId(album.getId());

        partialUpdatedAlbum
            .albumSpotifyID(UPDATED_ALBUM_SPOTIFY_ID)
            .albumName(UPDATED_ALBUM_NAME)
            .albumCoverArt(UPDATED_ALBUM_COVER_ART)
            .albumReleaseDate(UPDATED_ALBUM_RELEASE_DATE)
            .releaseDatePrecision(UPDATED_RELEASE_DATE_PRECISION)
            .albumPopularity(UPDATED_ALBUM_POPULARITY)
            .albumType(UPDATED_ALBUM_TYPE)
            .spotifyAlbumUPC(UPDATED_SPOTIFY_ALBUM_UPC)
            .spotifyAlbumEAN(UPDATED_SPOTIFY_ALBUM_EAN)
            .spotifyAlbumISRC(UPDATED_SPOTIFY_ALBUM_ISRC)
            .dateAddedToDB(UPDATED_DATE_ADDED_TO_DB)
            .dateLastModified(UPDATED_DATE_LAST_MODIFIED)
            .musicbrainzMetadataAdded(UPDATED_MUSICBRAINZ_METADATA_ADDED)
            .musicbrainzID(UPDATED_MUSICBRAINZ_ID);

        restAlbumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlbum.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAlbum))
            )
            .andExpect(status().isOk());

        // Validate the Album in the database
        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeUpdate);
        Album testAlbum = albumList.get(albumList.size() - 1);
        assertThat(testAlbum.getAlbumSpotifyID()).isEqualTo(UPDATED_ALBUM_SPOTIFY_ID);
        assertThat(testAlbum.getAlbumName()).isEqualTo(UPDATED_ALBUM_NAME);
        assertThat(testAlbum.getAlbumCoverArt()).isEqualTo(UPDATED_ALBUM_COVER_ART);
        assertThat(testAlbum.getAlbumReleaseDate()).isEqualTo(UPDATED_ALBUM_RELEASE_DATE);
        assertThat(testAlbum.getReleaseDatePrecision()).isEqualTo(UPDATED_RELEASE_DATE_PRECISION);
        assertThat(testAlbum.getAlbumPopularity()).isEqualTo(UPDATED_ALBUM_POPULARITY);
        assertThat(testAlbum.getAlbumType()).isEqualTo(UPDATED_ALBUM_TYPE);
        assertThat(testAlbum.getSpotifyAlbumUPC()).isEqualTo(UPDATED_SPOTIFY_ALBUM_UPC);
        assertThat(testAlbum.getSpotifyAlbumEAN()).isEqualTo(UPDATED_SPOTIFY_ALBUM_EAN);
        assertThat(testAlbum.getSpotifyAlbumISRC()).isEqualTo(UPDATED_SPOTIFY_ALBUM_ISRC);
        assertThat(testAlbum.getDateAddedToDB()).isEqualTo(UPDATED_DATE_ADDED_TO_DB);
        assertThat(testAlbum.getDateLastModified()).isEqualTo(UPDATED_DATE_LAST_MODIFIED);
        assertThat(testAlbum.getMusicbrainzMetadataAdded()).isEqualTo(UPDATED_MUSICBRAINZ_METADATA_ADDED);
        assertThat(testAlbum.getMusicbrainzID()).isEqualTo(UPDATED_MUSICBRAINZ_ID);
    }

    @Test
    @Transactional
    void patchNonExistingAlbum() throws Exception {
        int databaseSizeBeforeUpdate = albumRepository.findAll().size();
        album.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlbumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, album.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(album))
            )
            .andExpect(status().isBadRequest());

        // Validate the Album in the database
        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlbum() throws Exception {
        int databaseSizeBeforeUpdate = albumRepository.findAll().size();
        album.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlbumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(album))
            )
            .andExpect(status().isBadRequest());

        // Validate the Album in the database
        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlbum() throws Exception {
        int databaseSizeBeforeUpdate = albumRepository.findAll().size();
        album.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlbumMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(album)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Album in the database
        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlbum() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        int databaseSizeBeforeDelete = albumRepository.findAll().size();

        // Delete the album
        restAlbumMockMvc
            .perform(delete(ENTITY_API_URL_ID, album.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

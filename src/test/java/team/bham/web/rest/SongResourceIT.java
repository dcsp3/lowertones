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
import team.bham.domain.Song;
import team.bham.domain.enumeration.AlbumType;
import team.bham.repository.SongRepository;

/**
 * Integration tests for the {@link SongResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SongResourceIT {

    private static final String DEFAULT_SONG_SPOTIFY_ID = "AAAAAAAAAA";
    private static final String UPDATED_SONG_SPOTIFY_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SONG_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_SONG_TITLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_SONG_DURATION = 1;
    private static final Integer UPDATED_SONG_DURATION = 2;

    private static final AlbumType DEFAULT_SONG_ALBUM_TYPE = AlbumType.ALBUM;
    private static final AlbumType UPDATED_SONG_ALBUM_TYPE = AlbumType.SINGLE;

    private static final String DEFAULT_SONG_ALBUM_ID = "AAAAAAAAAA";
    private static final String UPDATED_SONG_ALBUM_ID = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SONG_EXPLICIT = false;
    private static final Boolean UPDATED_SONG_EXPLICIT = true;

    private static final Integer DEFAULT_SONG_POPULARITY = 1;
    private static final Integer UPDATED_SONG_POPULARITY = 2;

    private static final String DEFAULT_SONG_PREVIEW_URL = "AAAAAAAAAA";
    private static final String UPDATED_SONG_PREVIEW_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SONG_TRACK_FEATURES_ADDED = false;
    private static final Boolean UPDATED_SONG_TRACK_FEATURES_ADDED = true;

    private static final Float DEFAULT_SONG_ACOUSTICNESS = 1F;
    private static final Float UPDATED_SONG_ACOUSTICNESS = 2F;

    private static final Float DEFAULT_SONG_DANCEABILITY = 1F;
    private static final Float UPDATED_SONG_DANCEABILITY = 2F;

    private static final Float DEFAULT_SONG_ENERGY = 1F;
    private static final Float UPDATED_SONG_ENERGY = 2F;

    private static final Float DEFAULT_SONG_INSTRUMENTALNESS = 1F;
    private static final Float UPDATED_SONG_INSTRUMENTALNESS = 2F;

    private static final Float DEFAULT_SONG_LIVENESS = 1F;
    private static final Float UPDATED_SONG_LIVENESS = 2F;

    private static final Float DEFAULT_SONG_LOUDNESS = 1F;
    private static final Float UPDATED_SONG_LOUDNESS = 2F;

    private static final Float DEFAULT_SONG_SPEECHINESS = 1F;
    private static final Float UPDATED_SONG_SPEECHINESS = 2F;

    private static final Float DEFAULT_SONG_TEMPO = 1F;
    private static final Float UPDATED_SONG_TEMPO = 2F;

    private static final Float DEFAULT_SONG_VALENCE = 1F;
    private static final Float UPDATED_SONG_VALENCE = 2F;

    private static final Integer DEFAULT_SONG_KEY = 1;
    private static final Integer UPDATED_SONG_KEY = 2;

    private static final Integer DEFAULT_SONG_TIME_SIGNATURE = 1;
    private static final Integer UPDATED_SONG_TIME_SIGNATURE = 2;

    private static final LocalDate DEFAULT_SONG_DATE_ADDED_TO_DB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SONG_DATE_ADDED_TO_DB = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_SONG_DATE_LAST_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SONG_DATE_LAST_MODIFIED = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/songs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SongRepository songRepository;

    @Mock
    private SongRepository songRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSongMockMvc;

    private Song song;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Song createEntity(EntityManager em) {
        Song song = new Song()
            .songSpotifyID(DEFAULT_SONG_SPOTIFY_ID)
            .songTitle(DEFAULT_SONG_TITLE)
            .songDuration(DEFAULT_SONG_DURATION)
            .songAlbumType(DEFAULT_SONG_ALBUM_TYPE)
            .songAlbumID(DEFAULT_SONG_ALBUM_ID)
            .songExplicit(DEFAULT_SONG_EXPLICIT)
            .songPopularity(DEFAULT_SONG_POPULARITY)
            .songPreviewURL(DEFAULT_SONG_PREVIEW_URL)
            .songTrackFeaturesAdded(DEFAULT_SONG_TRACK_FEATURES_ADDED)
            .songAcousticness(DEFAULT_SONG_ACOUSTICNESS)
            .songDanceability(DEFAULT_SONG_DANCEABILITY)
            .songEnergy(DEFAULT_SONG_ENERGY)
            .songInstrumentalness(DEFAULT_SONG_INSTRUMENTALNESS)
            .songLiveness(DEFAULT_SONG_LIVENESS)
            .songLoudness(DEFAULT_SONG_LOUDNESS)
            .songSpeechiness(DEFAULT_SONG_SPEECHINESS)
            .songTempo(DEFAULT_SONG_TEMPO)
            .songValence(DEFAULT_SONG_VALENCE)
            .songKey(DEFAULT_SONG_KEY)
            .songTimeSignature(DEFAULT_SONG_TIME_SIGNATURE)
            .songDateAddedToDB(DEFAULT_SONG_DATE_ADDED_TO_DB)
            .songDateLastModified(DEFAULT_SONG_DATE_LAST_MODIFIED);
        return song;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Song createUpdatedEntity(EntityManager em) {
        Song song = new Song()
            .songSpotifyID(UPDATED_SONG_SPOTIFY_ID)
            .songTitle(UPDATED_SONG_TITLE)
            .songDuration(UPDATED_SONG_DURATION)
            .songAlbumType(UPDATED_SONG_ALBUM_TYPE)
            .songAlbumID(UPDATED_SONG_ALBUM_ID)
            .songExplicit(UPDATED_SONG_EXPLICIT)
            .songPopularity(UPDATED_SONG_POPULARITY)
            .songPreviewURL(UPDATED_SONG_PREVIEW_URL)
            .songTrackFeaturesAdded(UPDATED_SONG_TRACK_FEATURES_ADDED)
            .songAcousticness(UPDATED_SONG_ACOUSTICNESS)
            .songDanceability(UPDATED_SONG_DANCEABILITY)
            .songEnergy(UPDATED_SONG_ENERGY)
            .songInstrumentalness(UPDATED_SONG_INSTRUMENTALNESS)
            .songLiveness(UPDATED_SONG_LIVENESS)
            .songLoudness(UPDATED_SONG_LOUDNESS)
            .songSpeechiness(UPDATED_SONG_SPEECHINESS)
            .songTempo(UPDATED_SONG_TEMPO)
            .songValence(UPDATED_SONG_VALENCE)
            .songKey(UPDATED_SONG_KEY)
            .songTimeSignature(UPDATED_SONG_TIME_SIGNATURE)
            .songDateAddedToDB(UPDATED_SONG_DATE_ADDED_TO_DB)
            .songDateLastModified(UPDATED_SONG_DATE_LAST_MODIFIED);
        return song;
    }

    @BeforeEach
    public void initTest() {
        song = createEntity(em);
    }

    @Test
    @Transactional
    void createSong() throws Exception {
        int databaseSizeBeforeCreate = songRepository.findAll().size();
        // Create the Song
        restSongMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(song)))
            .andExpect(status().isCreated());

        // Validate the Song in the database
        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeCreate + 1);
        Song testSong = songList.get(songList.size() - 1);
        assertThat(testSong.getSongSpotifyID()).isEqualTo(DEFAULT_SONG_SPOTIFY_ID);
        assertThat(testSong.getSongTitle()).isEqualTo(DEFAULT_SONG_TITLE);
        assertThat(testSong.getSongDuration()).isEqualTo(DEFAULT_SONG_DURATION);
        assertThat(testSong.getSongAlbumType()).isEqualTo(DEFAULT_SONG_ALBUM_TYPE);
        assertThat(testSong.getSongAlbumID()).isEqualTo(DEFAULT_SONG_ALBUM_ID);
        assertThat(testSong.getSongExplicit()).isEqualTo(DEFAULT_SONG_EXPLICIT);
        assertThat(testSong.getSongPopularity()).isEqualTo(DEFAULT_SONG_POPULARITY);
        assertThat(testSong.getSongPreviewURL()).isEqualTo(DEFAULT_SONG_PREVIEW_URL);
        assertThat(testSong.getSongTrackFeaturesAdded()).isEqualTo(DEFAULT_SONG_TRACK_FEATURES_ADDED);
        assertThat(testSong.getSongAcousticness()).isEqualTo(DEFAULT_SONG_ACOUSTICNESS);
        assertThat(testSong.getSongDanceability()).isEqualTo(DEFAULT_SONG_DANCEABILITY);
        assertThat(testSong.getSongEnergy()).isEqualTo(DEFAULT_SONG_ENERGY);
        assertThat(testSong.getSongInstrumentalness()).isEqualTo(DEFAULT_SONG_INSTRUMENTALNESS);
        assertThat(testSong.getSongLiveness()).isEqualTo(DEFAULT_SONG_LIVENESS);
        assertThat(testSong.getSongLoudness()).isEqualTo(DEFAULT_SONG_LOUDNESS);
        assertThat(testSong.getSongSpeechiness()).isEqualTo(DEFAULT_SONG_SPEECHINESS);
        assertThat(testSong.getSongTempo()).isEqualTo(DEFAULT_SONG_TEMPO);
        assertThat(testSong.getSongValence()).isEqualTo(DEFAULT_SONG_VALENCE);
        assertThat(testSong.getSongKey()).isEqualTo(DEFAULT_SONG_KEY);
        assertThat(testSong.getSongTimeSignature()).isEqualTo(DEFAULT_SONG_TIME_SIGNATURE);
        assertThat(testSong.getSongDateAddedToDB()).isEqualTo(DEFAULT_SONG_DATE_ADDED_TO_DB);
        assertThat(testSong.getSongDateLastModified()).isEqualTo(DEFAULT_SONG_DATE_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void createSongWithExistingId() throws Exception {
        // Create the Song with an existing ID
        song.setId(1L);

        int databaseSizeBeforeCreate = songRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSongMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(song)))
            .andExpect(status().isBadRequest());

        // Validate the Song in the database
        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSongSpotifyIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = songRepository.findAll().size();
        // set the field null
        song.setSongSpotifyID(null);

        // Create the Song, which fails.

        restSongMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(song)))
            .andExpect(status().isBadRequest());

        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSongTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = songRepository.findAll().size();
        // set the field null
        song.setSongTitle(null);

        // Create the Song, which fails.

        restSongMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(song)))
            .andExpect(status().isBadRequest());

        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSongDurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = songRepository.findAll().size();
        // set the field null
        song.setSongDuration(null);

        // Create the Song, which fails.

        restSongMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(song)))
            .andExpect(status().isBadRequest());

        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSongAlbumTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = songRepository.findAll().size();
        // set the field null
        song.setSongAlbumType(null);

        // Create the Song, which fails.

        restSongMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(song)))
            .andExpect(status().isBadRequest());

        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSongAlbumIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = songRepository.findAll().size();
        // set the field null
        song.setSongAlbumID(null);

        // Create the Song, which fails.

        restSongMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(song)))
            .andExpect(status().isBadRequest());

        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSongExplicitIsRequired() throws Exception {
        int databaseSizeBeforeTest = songRepository.findAll().size();
        // set the field null
        song.setSongExplicit(null);

        // Create the Song, which fails.

        restSongMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(song)))
            .andExpect(status().isBadRequest());

        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSongPopularityIsRequired() throws Exception {
        int databaseSizeBeforeTest = songRepository.findAll().size();
        // set the field null
        song.setSongPopularity(null);

        // Create the Song, which fails.

        restSongMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(song)))
            .andExpect(status().isBadRequest());

        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSongTrackFeaturesAddedIsRequired() throws Exception {
        int databaseSizeBeforeTest = songRepository.findAll().size();
        // set the field null
        song.setSongTrackFeaturesAdded(null);

        // Create the Song, which fails.

        restSongMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(song)))
            .andExpect(status().isBadRequest());

        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSongDateAddedToDBIsRequired() throws Exception {
        int databaseSizeBeforeTest = songRepository.findAll().size();
        // set the field null
        song.setSongDateAddedToDB(null);

        // Create the Song, which fails.

        restSongMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(song)))
            .andExpect(status().isBadRequest());

        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSongDateLastModifiedIsRequired() throws Exception {
        int databaseSizeBeforeTest = songRepository.findAll().size();
        // set the field null
        song.setSongDateLastModified(null);

        // Create the Song, which fails.

        restSongMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(song)))
            .andExpect(status().isBadRequest());

        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSongs() throws Exception {
        // Initialize the database
        songRepository.saveAndFlush(song);

        // Get all the songList
        restSongMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(song.getId().intValue())))
            .andExpect(jsonPath("$.[*].songSpotifyID").value(hasItem(DEFAULT_SONG_SPOTIFY_ID)))
            .andExpect(jsonPath("$.[*].songTitle").value(hasItem(DEFAULT_SONG_TITLE)))
            .andExpect(jsonPath("$.[*].songDuration").value(hasItem(DEFAULT_SONG_DURATION)))
            .andExpect(jsonPath("$.[*].songAlbumType").value(hasItem(DEFAULT_SONG_ALBUM_TYPE.toString())))
            .andExpect(jsonPath("$.[*].songAlbumID").value(hasItem(DEFAULT_SONG_ALBUM_ID)))
            .andExpect(jsonPath("$.[*].songExplicit").value(hasItem(DEFAULT_SONG_EXPLICIT.booleanValue())))
            .andExpect(jsonPath("$.[*].songPopularity").value(hasItem(DEFAULT_SONG_POPULARITY)))
            .andExpect(jsonPath("$.[*].songPreviewURL").value(hasItem(DEFAULT_SONG_PREVIEW_URL)))
            .andExpect(jsonPath("$.[*].songTrackFeaturesAdded").value(hasItem(DEFAULT_SONG_TRACK_FEATURES_ADDED.booleanValue())))
            .andExpect(jsonPath("$.[*].songAcousticness").value(hasItem(DEFAULT_SONG_ACOUSTICNESS.doubleValue())))
            .andExpect(jsonPath("$.[*].songDanceability").value(hasItem(DEFAULT_SONG_DANCEABILITY.doubleValue())))
            .andExpect(jsonPath("$.[*].songEnergy").value(hasItem(DEFAULT_SONG_ENERGY.doubleValue())))
            .andExpect(jsonPath("$.[*].songInstrumentalness").value(hasItem(DEFAULT_SONG_INSTRUMENTALNESS.doubleValue())))
            .andExpect(jsonPath("$.[*].songLiveness").value(hasItem(DEFAULT_SONG_LIVENESS.doubleValue())))
            .andExpect(jsonPath("$.[*].songLoudness").value(hasItem(DEFAULT_SONG_LOUDNESS.doubleValue())))
            .andExpect(jsonPath("$.[*].songSpeechiness").value(hasItem(DEFAULT_SONG_SPEECHINESS.doubleValue())))
            .andExpect(jsonPath("$.[*].songTempo").value(hasItem(DEFAULT_SONG_TEMPO.doubleValue())))
            .andExpect(jsonPath("$.[*].songValence").value(hasItem(DEFAULT_SONG_VALENCE.doubleValue())))
            .andExpect(jsonPath("$.[*].songKey").value(hasItem(DEFAULT_SONG_KEY)))
            .andExpect(jsonPath("$.[*].songTimeSignature").value(hasItem(DEFAULT_SONG_TIME_SIGNATURE)))
            .andExpect(jsonPath("$.[*].songDateAddedToDB").value(hasItem(DEFAULT_SONG_DATE_ADDED_TO_DB.toString())))
            .andExpect(jsonPath("$.[*].songDateLastModified").value(hasItem(DEFAULT_SONG_DATE_LAST_MODIFIED.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSongsWithEagerRelationshipsIsEnabled() throws Exception {
        when(songRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSongMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(songRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSongsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(songRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSongMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(songRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSong() throws Exception {
        // Initialize the database
        songRepository.saveAndFlush(song);

        // Get the song
        restSongMockMvc
            .perform(get(ENTITY_API_URL_ID, song.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(song.getId().intValue()))
            .andExpect(jsonPath("$.songSpotifyID").value(DEFAULT_SONG_SPOTIFY_ID))
            .andExpect(jsonPath("$.songTitle").value(DEFAULT_SONG_TITLE))
            .andExpect(jsonPath("$.songDuration").value(DEFAULT_SONG_DURATION))
            .andExpect(jsonPath("$.songAlbumType").value(DEFAULT_SONG_ALBUM_TYPE.toString()))
            .andExpect(jsonPath("$.songAlbumID").value(DEFAULT_SONG_ALBUM_ID))
            .andExpect(jsonPath("$.songExplicit").value(DEFAULT_SONG_EXPLICIT.booleanValue()))
            .andExpect(jsonPath("$.songPopularity").value(DEFAULT_SONG_POPULARITY))
            .andExpect(jsonPath("$.songPreviewURL").value(DEFAULT_SONG_PREVIEW_URL))
            .andExpect(jsonPath("$.songTrackFeaturesAdded").value(DEFAULT_SONG_TRACK_FEATURES_ADDED.booleanValue()))
            .andExpect(jsonPath("$.songAcousticness").value(DEFAULT_SONG_ACOUSTICNESS.doubleValue()))
            .andExpect(jsonPath("$.songDanceability").value(DEFAULT_SONG_DANCEABILITY.doubleValue()))
            .andExpect(jsonPath("$.songEnergy").value(DEFAULT_SONG_ENERGY.doubleValue()))
            .andExpect(jsonPath("$.songInstrumentalness").value(DEFAULT_SONG_INSTRUMENTALNESS.doubleValue()))
            .andExpect(jsonPath("$.songLiveness").value(DEFAULT_SONG_LIVENESS.doubleValue()))
            .andExpect(jsonPath("$.songLoudness").value(DEFAULT_SONG_LOUDNESS.doubleValue()))
            .andExpect(jsonPath("$.songSpeechiness").value(DEFAULT_SONG_SPEECHINESS.doubleValue()))
            .andExpect(jsonPath("$.songTempo").value(DEFAULT_SONG_TEMPO.doubleValue()))
            .andExpect(jsonPath("$.songValence").value(DEFAULT_SONG_VALENCE.doubleValue()))
            .andExpect(jsonPath("$.songKey").value(DEFAULT_SONG_KEY))
            .andExpect(jsonPath("$.songTimeSignature").value(DEFAULT_SONG_TIME_SIGNATURE))
            .andExpect(jsonPath("$.songDateAddedToDB").value(DEFAULT_SONG_DATE_ADDED_TO_DB.toString()))
            .andExpect(jsonPath("$.songDateLastModified").value(DEFAULT_SONG_DATE_LAST_MODIFIED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSong() throws Exception {
        // Get the song
        restSongMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSong() throws Exception {
        // Initialize the database
        songRepository.saveAndFlush(song);

        int databaseSizeBeforeUpdate = songRepository.findAll().size();

        // Update the song
        Song updatedSong = songRepository.findById(song.getId()).get();
        // Disconnect from session so that the updates on updatedSong are not directly saved in db
        em.detach(updatedSong);
        updatedSong
            .songSpotifyID(UPDATED_SONG_SPOTIFY_ID)
            .songTitle(UPDATED_SONG_TITLE)
            .songDuration(UPDATED_SONG_DURATION)
            .songAlbumType(UPDATED_SONG_ALBUM_TYPE)
            .songAlbumID(UPDATED_SONG_ALBUM_ID)
            .songExplicit(UPDATED_SONG_EXPLICIT)
            .songPopularity(UPDATED_SONG_POPULARITY)
            .songPreviewURL(UPDATED_SONG_PREVIEW_URL)
            .songTrackFeaturesAdded(UPDATED_SONG_TRACK_FEATURES_ADDED)
            .songAcousticness(UPDATED_SONG_ACOUSTICNESS)
            .songDanceability(UPDATED_SONG_DANCEABILITY)
            .songEnergy(UPDATED_SONG_ENERGY)
            .songInstrumentalness(UPDATED_SONG_INSTRUMENTALNESS)
            .songLiveness(UPDATED_SONG_LIVENESS)
            .songLoudness(UPDATED_SONG_LOUDNESS)
            .songSpeechiness(UPDATED_SONG_SPEECHINESS)
            .songTempo(UPDATED_SONG_TEMPO)
            .songValence(UPDATED_SONG_VALENCE)
            .songKey(UPDATED_SONG_KEY)
            .songTimeSignature(UPDATED_SONG_TIME_SIGNATURE)
            .songDateAddedToDB(UPDATED_SONG_DATE_ADDED_TO_DB)
            .songDateLastModified(UPDATED_SONG_DATE_LAST_MODIFIED);

        restSongMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSong.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSong))
            )
            .andExpect(status().isOk());

        // Validate the Song in the database
        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeUpdate);
        Song testSong = songList.get(songList.size() - 1);
        assertThat(testSong.getSongSpotifyID()).isEqualTo(UPDATED_SONG_SPOTIFY_ID);
        assertThat(testSong.getSongTitle()).isEqualTo(UPDATED_SONG_TITLE);
        assertThat(testSong.getSongDuration()).isEqualTo(UPDATED_SONG_DURATION);
        assertThat(testSong.getSongAlbumType()).isEqualTo(UPDATED_SONG_ALBUM_TYPE);
        assertThat(testSong.getSongAlbumID()).isEqualTo(UPDATED_SONG_ALBUM_ID);
        assertThat(testSong.getSongExplicit()).isEqualTo(UPDATED_SONG_EXPLICIT);
        assertThat(testSong.getSongPopularity()).isEqualTo(UPDATED_SONG_POPULARITY);
        assertThat(testSong.getSongPreviewURL()).isEqualTo(UPDATED_SONG_PREVIEW_URL);
        assertThat(testSong.getSongTrackFeaturesAdded()).isEqualTo(UPDATED_SONG_TRACK_FEATURES_ADDED);
        assertThat(testSong.getSongAcousticness()).isEqualTo(UPDATED_SONG_ACOUSTICNESS);
        assertThat(testSong.getSongDanceability()).isEqualTo(UPDATED_SONG_DANCEABILITY);
        assertThat(testSong.getSongEnergy()).isEqualTo(UPDATED_SONG_ENERGY);
        assertThat(testSong.getSongInstrumentalness()).isEqualTo(UPDATED_SONG_INSTRUMENTALNESS);
        assertThat(testSong.getSongLiveness()).isEqualTo(UPDATED_SONG_LIVENESS);
        assertThat(testSong.getSongLoudness()).isEqualTo(UPDATED_SONG_LOUDNESS);
        assertThat(testSong.getSongSpeechiness()).isEqualTo(UPDATED_SONG_SPEECHINESS);
        assertThat(testSong.getSongTempo()).isEqualTo(UPDATED_SONG_TEMPO);
        assertThat(testSong.getSongValence()).isEqualTo(UPDATED_SONG_VALENCE);
        assertThat(testSong.getSongKey()).isEqualTo(UPDATED_SONG_KEY);
        assertThat(testSong.getSongTimeSignature()).isEqualTo(UPDATED_SONG_TIME_SIGNATURE);
        assertThat(testSong.getSongDateAddedToDB()).isEqualTo(UPDATED_SONG_DATE_ADDED_TO_DB);
        assertThat(testSong.getSongDateLastModified()).isEqualTo(UPDATED_SONG_DATE_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void putNonExistingSong() throws Exception {
        int databaseSizeBeforeUpdate = songRepository.findAll().size();
        song.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSongMockMvc
            .perform(
                put(ENTITY_API_URL_ID, song.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(song))
            )
            .andExpect(status().isBadRequest());

        // Validate the Song in the database
        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSong() throws Exception {
        int databaseSizeBeforeUpdate = songRepository.findAll().size();
        song.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSongMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(song))
            )
            .andExpect(status().isBadRequest());

        // Validate the Song in the database
        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSong() throws Exception {
        int databaseSizeBeforeUpdate = songRepository.findAll().size();
        song.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSongMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(song)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Song in the database
        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSongWithPatch() throws Exception {
        // Initialize the database
        songRepository.saveAndFlush(song);

        int databaseSizeBeforeUpdate = songRepository.findAll().size();

        // Update the song using partial update
        Song partialUpdatedSong = new Song();
        partialUpdatedSong.setId(song.getId());

        partialUpdatedSong
            .songSpotifyID(UPDATED_SONG_SPOTIFY_ID)
            .songTitle(UPDATED_SONG_TITLE)
            .songDuration(UPDATED_SONG_DURATION)
            .songAlbumType(UPDATED_SONG_ALBUM_TYPE)
            .songExplicit(UPDATED_SONG_EXPLICIT)
            .songPreviewURL(UPDATED_SONG_PREVIEW_URL)
            .songEnergy(UPDATED_SONG_ENERGY)
            .songInstrumentalness(UPDATED_SONG_INSTRUMENTALNESS)
            .songValence(UPDATED_SONG_VALENCE);

        restSongMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSong.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSong))
            )
            .andExpect(status().isOk());

        // Validate the Song in the database
        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeUpdate);
        Song testSong = songList.get(songList.size() - 1);
        assertThat(testSong.getSongSpotifyID()).isEqualTo(UPDATED_SONG_SPOTIFY_ID);
        assertThat(testSong.getSongTitle()).isEqualTo(UPDATED_SONG_TITLE);
        assertThat(testSong.getSongDuration()).isEqualTo(UPDATED_SONG_DURATION);
        assertThat(testSong.getSongAlbumType()).isEqualTo(UPDATED_SONG_ALBUM_TYPE);
        assertThat(testSong.getSongAlbumID()).isEqualTo(DEFAULT_SONG_ALBUM_ID);
        assertThat(testSong.getSongExplicit()).isEqualTo(UPDATED_SONG_EXPLICIT);
        assertThat(testSong.getSongPopularity()).isEqualTo(DEFAULT_SONG_POPULARITY);
        assertThat(testSong.getSongPreviewURL()).isEqualTo(UPDATED_SONG_PREVIEW_URL);
        assertThat(testSong.getSongTrackFeaturesAdded()).isEqualTo(DEFAULT_SONG_TRACK_FEATURES_ADDED);
        assertThat(testSong.getSongAcousticness()).isEqualTo(DEFAULT_SONG_ACOUSTICNESS);
        assertThat(testSong.getSongDanceability()).isEqualTo(DEFAULT_SONG_DANCEABILITY);
        assertThat(testSong.getSongEnergy()).isEqualTo(UPDATED_SONG_ENERGY);
        assertThat(testSong.getSongInstrumentalness()).isEqualTo(UPDATED_SONG_INSTRUMENTALNESS);
        assertThat(testSong.getSongLiveness()).isEqualTo(DEFAULT_SONG_LIVENESS);
        assertThat(testSong.getSongLoudness()).isEqualTo(DEFAULT_SONG_LOUDNESS);
        assertThat(testSong.getSongSpeechiness()).isEqualTo(DEFAULT_SONG_SPEECHINESS);
        assertThat(testSong.getSongTempo()).isEqualTo(DEFAULT_SONG_TEMPO);
        assertThat(testSong.getSongValence()).isEqualTo(UPDATED_SONG_VALENCE);
        assertThat(testSong.getSongKey()).isEqualTo(DEFAULT_SONG_KEY);
        assertThat(testSong.getSongTimeSignature()).isEqualTo(DEFAULT_SONG_TIME_SIGNATURE);
        assertThat(testSong.getSongDateAddedToDB()).isEqualTo(DEFAULT_SONG_DATE_ADDED_TO_DB);
        assertThat(testSong.getSongDateLastModified()).isEqualTo(DEFAULT_SONG_DATE_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void fullUpdateSongWithPatch() throws Exception {
        // Initialize the database
        songRepository.saveAndFlush(song);

        int databaseSizeBeforeUpdate = songRepository.findAll().size();

        // Update the song using partial update
        Song partialUpdatedSong = new Song();
        partialUpdatedSong.setId(song.getId());

        partialUpdatedSong
            .songSpotifyID(UPDATED_SONG_SPOTIFY_ID)
            .songTitle(UPDATED_SONG_TITLE)
            .songDuration(UPDATED_SONG_DURATION)
            .songAlbumType(UPDATED_SONG_ALBUM_TYPE)
            .songAlbumID(UPDATED_SONG_ALBUM_ID)
            .songExplicit(UPDATED_SONG_EXPLICIT)
            .songPopularity(UPDATED_SONG_POPULARITY)
            .songPreviewURL(UPDATED_SONG_PREVIEW_URL)
            .songTrackFeaturesAdded(UPDATED_SONG_TRACK_FEATURES_ADDED)
            .songAcousticness(UPDATED_SONG_ACOUSTICNESS)
            .songDanceability(UPDATED_SONG_DANCEABILITY)
            .songEnergy(UPDATED_SONG_ENERGY)
            .songInstrumentalness(UPDATED_SONG_INSTRUMENTALNESS)
            .songLiveness(UPDATED_SONG_LIVENESS)
            .songLoudness(UPDATED_SONG_LOUDNESS)
            .songSpeechiness(UPDATED_SONG_SPEECHINESS)
            .songTempo(UPDATED_SONG_TEMPO)
            .songValence(UPDATED_SONG_VALENCE)
            .songKey(UPDATED_SONG_KEY)
            .songTimeSignature(UPDATED_SONG_TIME_SIGNATURE)
            .songDateAddedToDB(UPDATED_SONG_DATE_ADDED_TO_DB)
            .songDateLastModified(UPDATED_SONG_DATE_LAST_MODIFIED);

        restSongMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSong.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSong))
            )
            .andExpect(status().isOk());

        // Validate the Song in the database
        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeUpdate);
        Song testSong = songList.get(songList.size() - 1);
        assertThat(testSong.getSongSpotifyID()).isEqualTo(UPDATED_SONG_SPOTIFY_ID);
        assertThat(testSong.getSongTitle()).isEqualTo(UPDATED_SONG_TITLE);
        assertThat(testSong.getSongDuration()).isEqualTo(UPDATED_SONG_DURATION);
        assertThat(testSong.getSongAlbumType()).isEqualTo(UPDATED_SONG_ALBUM_TYPE);
        assertThat(testSong.getSongAlbumID()).isEqualTo(UPDATED_SONG_ALBUM_ID);
        assertThat(testSong.getSongExplicit()).isEqualTo(UPDATED_SONG_EXPLICIT);
        assertThat(testSong.getSongPopularity()).isEqualTo(UPDATED_SONG_POPULARITY);
        assertThat(testSong.getSongPreviewURL()).isEqualTo(UPDATED_SONG_PREVIEW_URL);
        assertThat(testSong.getSongTrackFeaturesAdded()).isEqualTo(UPDATED_SONG_TRACK_FEATURES_ADDED);
        assertThat(testSong.getSongAcousticness()).isEqualTo(UPDATED_SONG_ACOUSTICNESS);
        assertThat(testSong.getSongDanceability()).isEqualTo(UPDATED_SONG_DANCEABILITY);
        assertThat(testSong.getSongEnergy()).isEqualTo(UPDATED_SONG_ENERGY);
        assertThat(testSong.getSongInstrumentalness()).isEqualTo(UPDATED_SONG_INSTRUMENTALNESS);
        assertThat(testSong.getSongLiveness()).isEqualTo(UPDATED_SONG_LIVENESS);
        assertThat(testSong.getSongLoudness()).isEqualTo(UPDATED_SONG_LOUDNESS);
        assertThat(testSong.getSongSpeechiness()).isEqualTo(UPDATED_SONG_SPEECHINESS);
        assertThat(testSong.getSongTempo()).isEqualTo(UPDATED_SONG_TEMPO);
        assertThat(testSong.getSongValence()).isEqualTo(UPDATED_SONG_VALENCE);
        assertThat(testSong.getSongKey()).isEqualTo(UPDATED_SONG_KEY);
        assertThat(testSong.getSongTimeSignature()).isEqualTo(UPDATED_SONG_TIME_SIGNATURE);
        assertThat(testSong.getSongDateAddedToDB()).isEqualTo(UPDATED_SONG_DATE_ADDED_TO_DB);
        assertThat(testSong.getSongDateLastModified()).isEqualTo(UPDATED_SONG_DATE_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void patchNonExistingSong() throws Exception {
        int databaseSizeBeforeUpdate = songRepository.findAll().size();
        song.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSongMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, song.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(song))
            )
            .andExpect(status().isBadRequest());

        // Validate the Song in the database
        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSong() throws Exception {
        int databaseSizeBeforeUpdate = songRepository.findAll().size();
        song.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSongMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(song))
            )
            .andExpect(status().isBadRequest());

        // Validate the Song in the database
        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSong() throws Exception {
        int databaseSizeBeforeUpdate = songRepository.findAll().size();
        song.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSongMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(song)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Song in the database
        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSong() throws Exception {
        // Initialize the database
        songRepository.saveAndFlush(song);

        int databaseSizeBeforeDelete = songRepository.findAll().size();

        // Delete the song
        restSongMockMvc
            .perform(delete(ENTITY_API_URL_ID, song.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

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
import team.bham.domain.AppUser;
import team.bham.repository.AppUserRepository;

/**
 * Integration tests for the {@link AppUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AppUserResourceIT {

    private static final String DEFAULT_SPOTIFY_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_SPOTIFY_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_USER_IMAGE_LARGE = "AAAAAAAAAA";
    private static final String UPDATED_USER_IMAGE_LARGE = "BBBBBBBBBB";

    private static final String DEFAULT_USER_IMAGE_MEDIUM = "AAAAAAAAAA";
    private static final String UPDATED_USER_IMAGE_MEDIUM = "BBBBBBBBBB";

    private static final String DEFAULT_USER_IMAGE_SMALL = "AAAAAAAAAA";
    private static final String UPDATED_USER_IMAGE_SMALL = "BBBBBBBBBB";

    private static final String DEFAULT_SPOTIFY_REFRESH_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_SPOTIFY_REFRESH_TOKEN = "BBBBBBBBBB";

    private static final String DEFAULT_SPOTIFY_AUTH_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_SPOTIFY_AUTH_TOKEN = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_LAST_LOGIN_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_LOGIN_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_DISCOVER_WEEKLY_BUFFER_SETTINGS = 1;
    private static final Integer UPDATED_DISCOVER_WEEKLY_BUFFER_SETTINGS = 2;

    private static final String DEFAULT_DISCOVER_WEEKLY_BUFFER_PLAYLIST_ID = "AAAAAAAAAA";
    private static final String UPDATED_DISCOVER_WEEKLY_BUFFER_PLAYLIST_ID = "BBBBBBBBBB";

    private static final Boolean DEFAULT_HIGH_CONTRAST_MODE = false;
    private static final Boolean UPDATED_HIGH_CONTRAST_MODE = true;

    private static final Integer DEFAULT_TEXT_SIZE = 1;
    private static final Integer UPDATED_TEXT_SIZE = 2;

    private static final Boolean DEFAULT_EMAIL_UPDATES_ENABLED = false;
    private static final Boolean UPDATED_EMAIL_UPDATES_ENABLED = true;

    private static final String ENTITY_API_URL = "/api/app-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppUserMockMvc;

    private AppUser appUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppUser createEntity(EntityManager em) {
        AppUser appUser = new AppUser()
            .spotifyUserID(DEFAULT_SPOTIFY_USER_ID)
            .name(DEFAULT_NAME)
            .email(DEFAULT_EMAIL)
            .userImageLarge(DEFAULT_USER_IMAGE_LARGE)
            .userImageMedium(DEFAULT_USER_IMAGE_MEDIUM)
            .userImageSmall(DEFAULT_USER_IMAGE_SMALL)
            .spotifyRefreshToken(DEFAULT_SPOTIFY_REFRESH_TOKEN)
            .spotifyAuthToken(DEFAULT_SPOTIFY_AUTH_TOKEN)
            .lastLoginDate(DEFAULT_LAST_LOGIN_DATE)
            .discoverWeeklyBufferSettings(DEFAULT_DISCOVER_WEEKLY_BUFFER_SETTINGS)
            .discoverWeeklyBufferPlaylistID(DEFAULT_DISCOVER_WEEKLY_BUFFER_PLAYLIST_ID)
            .highContrastMode(DEFAULT_HIGH_CONTRAST_MODE)
            .textSize(DEFAULT_TEXT_SIZE)
            .emailUpdatesEnabled(DEFAULT_EMAIL_UPDATES_ENABLED);
        return appUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppUser createUpdatedEntity(EntityManager em) {
        AppUser appUser = new AppUser()
            .spotifyUserID(UPDATED_SPOTIFY_USER_ID)
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .userImageLarge(UPDATED_USER_IMAGE_LARGE)
            .userImageMedium(UPDATED_USER_IMAGE_MEDIUM)
            .userImageSmall(UPDATED_USER_IMAGE_SMALL)
            .spotifyRefreshToken(UPDATED_SPOTIFY_REFRESH_TOKEN)
            .spotifyAuthToken(UPDATED_SPOTIFY_AUTH_TOKEN)
            .lastLoginDate(UPDATED_LAST_LOGIN_DATE)
            .discoverWeeklyBufferSettings(UPDATED_DISCOVER_WEEKLY_BUFFER_SETTINGS)
            .discoverWeeklyBufferPlaylistID(UPDATED_DISCOVER_WEEKLY_BUFFER_PLAYLIST_ID)
            .highContrastMode(UPDATED_HIGH_CONTRAST_MODE)
            .textSize(UPDATED_TEXT_SIZE)
            .emailUpdatesEnabled(UPDATED_EMAIL_UPDATES_ENABLED);
        return appUser;
    }

    @BeforeEach
    public void initTest() {
        appUser = createEntity(em);
    }

    @Test
    @Transactional
    void createAppUser() throws Exception {
        int databaseSizeBeforeCreate = appUserRepository.findAll().size();
        // Create the AppUser
        restAppUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appUser)))
            .andExpect(status().isCreated());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeCreate + 1);
        AppUser testAppUser = appUserList.get(appUserList.size() - 1);
        assertThat(testAppUser.getSpotifyUserID()).isEqualTo(DEFAULT_SPOTIFY_USER_ID);
        assertThat(testAppUser.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAppUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAppUser.getUserImageLarge()).isEqualTo(DEFAULT_USER_IMAGE_LARGE);
        assertThat(testAppUser.getUserImageMedium()).isEqualTo(DEFAULT_USER_IMAGE_MEDIUM);
        assertThat(testAppUser.getUserImageSmall()).isEqualTo(DEFAULT_USER_IMAGE_SMALL);
        assertThat(testAppUser.getSpotifyRefreshToken()).isEqualTo(DEFAULT_SPOTIFY_REFRESH_TOKEN);
        assertThat(testAppUser.getSpotifyAuthToken()).isEqualTo(DEFAULT_SPOTIFY_AUTH_TOKEN);
        assertThat(testAppUser.getLastLoginDate()).isEqualTo(DEFAULT_LAST_LOGIN_DATE);
        assertThat(testAppUser.getDiscoverWeeklyBufferSettings()).isEqualTo(DEFAULT_DISCOVER_WEEKLY_BUFFER_SETTINGS);
        assertThat(testAppUser.getDiscoverWeeklyBufferPlaylistID()).isEqualTo(DEFAULT_DISCOVER_WEEKLY_BUFFER_PLAYLIST_ID);
        assertThat(testAppUser.getHighContrastMode()).isEqualTo(DEFAULT_HIGH_CONTRAST_MODE);
        assertThat(testAppUser.getTextSize()).isEqualTo(DEFAULT_TEXT_SIZE);
        assertThat(testAppUser.getEmailUpdatesEnabled()).isEqualTo(DEFAULT_EMAIL_UPDATES_ENABLED);
    }

    @Test
    @Transactional
    void createAppUserWithExistingId() throws Exception {
        // Create the AppUser with an existing ID
        appUser.setId(1L);

        int databaseSizeBeforeCreate = appUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appUser)))
            .andExpect(status().isBadRequest());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSpotifyUserIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = appUserRepository.findAll().size();
        // set the field null
        appUser.setSpotifyUserID(null);

        // Create the AppUser, which fails.

        restAppUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appUser)))
            .andExpect(status().isBadRequest());

        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = appUserRepository.findAll().size();
        // set the field null
        appUser.setName(null);

        // Create the AppUser, which fails.

        restAppUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appUser)))
            .andExpect(status().isBadRequest());

        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = appUserRepository.findAll().size();
        // set the field null
        appUser.setEmail(null);

        // Create the AppUser, which fails.

        restAppUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appUser)))
            .andExpect(status().isBadRequest());

        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastLoginDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = appUserRepository.findAll().size();
        // set the field null
        appUser.setLastLoginDate(null);

        // Create the AppUser, which fails.

        restAppUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appUser)))
            .andExpect(status().isBadRequest());

        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDiscoverWeeklyBufferSettingsIsRequired() throws Exception {
        int databaseSizeBeforeTest = appUserRepository.findAll().size();
        // set the field null
        appUser.setDiscoverWeeklyBufferSettings(null);

        // Create the AppUser, which fails.

        restAppUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appUser)))
            .andExpect(status().isBadRequest());

        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHighContrastModeIsRequired() throws Exception {
        int databaseSizeBeforeTest = appUserRepository.findAll().size();
        // set the field null
        appUser.setHighContrastMode(null);

        // Create the AppUser, which fails.

        restAppUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appUser)))
            .andExpect(status().isBadRequest());

        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTextSizeIsRequired() throws Exception {
        int databaseSizeBeforeTest = appUserRepository.findAll().size();
        // set the field null
        appUser.setTextSize(null);

        // Create the AppUser, which fails.

        restAppUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appUser)))
            .andExpect(status().isBadRequest());

        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailUpdatesEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = appUserRepository.findAll().size();
        // set the field null
        appUser.setEmailUpdatesEnabled(null);

        // Create the AppUser, which fails.

        restAppUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appUser)))
            .andExpect(status().isBadRequest());

        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAppUsers() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList
        restAppUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].spotifyUserID").value(hasItem(DEFAULT_SPOTIFY_USER_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].userImageLarge").value(hasItem(DEFAULT_USER_IMAGE_LARGE)))
            .andExpect(jsonPath("$.[*].userImageMedium").value(hasItem(DEFAULT_USER_IMAGE_MEDIUM)))
            .andExpect(jsonPath("$.[*].userImageSmall").value(hasItem(DEFAULT_USER_IMAGE_SMALL)))
            .andExpect(jsonPath("$.[*].spotifyRefreshToken").value(hasItem(DEFAULT_SPOTIFY_REFRESH_TOKEN)))
            .andExpect(jsonPath("$.[*].spotifyAuthToken").value(hasItem(DEFAULT_SPOTIFY_AUTH_TOKEN)))
            .andExpect(jsonPath("$.[*].lastLoginDate").value(hasItem(DEFAULT_LAST_LOGIN_DATE.toString())))
            .andExpect(jsonPath("$.[*].discoverWeeklyBufferSettings").value(hasItem(DEFAULT_DISCOVER_WEEKLY_BUFFER_SETTINGS)))
            .andExpect(jsonPath("$.[*].discoverWeeklyBufferPlaylistID").value(hasItem(DEFAULT_DISCOVER_WEEKLY_BUFFER_PLAYLIST_ID)))
            .andExpect(jsonPath("$.[*].highContrastMode").value(hasItem(DEFAULT_HIGH_CONTRAST_MODE.booleanValue())))
            .andExpect(jsonPath("$.[*].textSize").value(hasItem(DEFAULT_TEXT_SIZE)))
            .andExpect(jsonPath("$.[*].emailUpdatesEnabled").value(hasItem(DEFAULT_EMAIL_UPDATES_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    void getAppUser() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get the appUser
        restAppUserMockMvc
            .perform(get(ENTITY_API_URL_ID, appUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appUser.getId().intValue()))
            .andExpect(jsonPath("$.spotifyUserID").value(DEFAULT_SPOTIFY_USER_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.userImageLarge").value(DEFAULT_USER_IMAGE_LARGE))
            .andExpect(jsonPath("$.userImageMedium").value(DEFAULT_USER_IMAGE_MEDIUM))
            .andExpect(jsonPath("$.userImageSmall").value(DEFAULT_USER_IMAGE_SMALL))
            .andExpect(jsonPath("$.spotifyRefreshToken").value(DEFAULT_SPOTIFY_REFRESH_TOKEN))
            .andExpect(jsonPath("$.spotifyAuthToken").value(DEFAULT_SPOTIFY_AUTH_TOKEN))
            .andExpect(jsonPath("$.lastLoginDate").value(DEFAULT_LAST_LOGIN_DATE.toString()))
            .andExpect(jsonPath("$.discoverWeeklyBufferSettings").value(DEFAULT_DISCOVER_WEEKLY_BUFFER_SETTINGS))
            .andExpect(jsonPath("$.discoverWeeklyBufferPlaylistID").value(DEFAULT_DISCOVER_WEEKLY_BUFFER_PLAYLIST_ID))
            .andExpect(jsonPath("$.highContrastMode").value(DEFAULT_HIGH_CONTRAST_MODE.booleanValue()))
            .andExpect(jsonPath("$.textSize").value(DEFAULT_TEXT_SIZE))
            .andExpect(jsonPath("$.emailUpdatesEnabled").value(DEFAULT_EMAIL_UPDATES_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingAppUser() throws Exception {
        // Get the appUser
        restAppUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAppUser() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();

        // Update the appUser
        AppUser updatedAppUser = appUserRepository.findById(appUser.getId()).get();
        // Disconnect from session so that the updates on updatedAppUser are not directly saved in db
        em.detach(updatedAppUser);
        updatedAppUser
            .spotifyUserID(UPDATED_SPOTIFY_USER_ID)
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .userImageLarge(UPDATED_USER_IMAGE_LARGE)
            .userImageMedium(UPDATED_USER_IMAGE_MEDIUM)
            .userImageSmall(UPDATED_USER_IMAGE_SMALL)
            .spotifyRefreshToken(UPDATED_SPOTIFY_REFRESH_TOKEN)
            .spotifyAuthToken(UPDATED_SPOTIFY_AUTH_TOKEN)
            .lastLoginDate(UPDATED_LAST_LOGIN_DATE)
            .discoverWeeklyBufferSettings(UPDATED_DISCOVER_WEEKLY_BUFFER_SETTINGS)
            .discoverWeeklyBufferPlaylistID(UPDATED_DISCOVER_WEEKLY_BUFFER_PLAYLIST_ID)
            .highContrastMode(UPDATED_HIGH_CONTRAST_MODE)
            .textSize(UPDATED_TEXT_SIZE)
            .emailUpdatesEnabled(UPDATED_EMAIL_UPDATES_ENABLED);

        restAppUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAppUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAppUser))
            )
            .andExpect(status().isOk());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
        AppUser testAppUser = appUserList.get(appUserList.size() - 1);
        assertThat(testAppUser.getSpotifyUserID()).isEqualTo(UPDATED_SPOTIFY_USER_ID);
        assertThat(testAppUser.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAppUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAppUser.getUserImageLarge()).isEqualTo(UPDATED_USER_IMAGE_LARGE);
        assertThat(testAppUser.getUserImageMedium()).isEqualTo(UPDATED_USER_IMAGE_MEDIUM);
        assertThat(testAppUser.getUserImageSmall()).isEqualTo(UPDATED_USER_IMAGE_SMALL);
        assertThat(testAppUser.getSpotifyRefreshToken()).isEqualTo(UPDATED_SPOTIFY_REFRESH_TOKEN);
        assertThat(testAppUser.getSpotifyAuthToken()).isEqualTo(UPDATED_SPOTIFY_AUTH_TOKEN);
        assertThat(testAppUser.getLastLoginDate()).isEqualTo(UPDATED_LAST_LOGIN_DATE);
        assertThat(testAppUser.getDiscoverWeeklyBufferSettings()).isEqualTo(UPDATED_DISCOVER_WEEKLY_BUFFER_SETTINGS);
        assertThat(testAppUser.getDiscoverWeeklyBufferPlaylistID()).isEqualTo(UPDATED_DISCOVER_WEEKLY_BUFFER_PLAYLIST_ID);
        assertThat(testAppUser.getHighContrastMode()).isEqualTo(UPDATED_HIGH_CONTRAST_MODE);
        assertThat(testAppUser.getTextSize()).isEqualTo(UPDATED_TEXT_SIZE);
        assertThat(testAppUser.getEmailUpdatesEnabled()).isEqualTo(UPDATED_EMAIL_UPDATES_ENABLED);
    }

    @Test
    @Transactional
    void putNonExistingAppUser() throws Exception {
        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();
        appUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppUser() throws Exception {
        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();
        appUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppUser() throws Exception {
        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();
        appUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAppUserWithPatch() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();

        // Update the appUser using partial update
        AppUser partialUpdatedAppUser = new AppUser();
        partialUpdatedAppUser.setId(appUser.getId());

        partialUpdatedAppUser
            .spotifyUserID(UPDATED_SPOTIFY_USER_ID)
            .name(UPDATED_NAME)
            .userImageMedium(UPDATED_USER_IMAGE_MEDIUM)
            .userImageSmall(UPDATED_USER_IMAGE_SMALL)
            .lastLoginDate(UPDATED_LAST_LOGIN_DATE)
            .discoverWeeklyBufferSettings(UPDATED_DISCOVER_WEEKLY_BUFFER_SETTINGS)
            .discoverWeeklyBufferPlaylistID(UPDATED_DISCOVER_WEEKLY_BUFFER_PLAYLIST_ID)
            .highContrastMode(UPDATED_HIGH_CONTRAST_MODE)
            .textSize(UPDATED_TEXT_SIZE);

        restAppUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppUser))
            )
            .andExpect(status().isOk());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
        AppUser testAppUser = appUserList.get(appUserList.size() - 1);
        assertThat(testAppUser.getSpotifyUserID()).isEqualTo(UPDATED_SPOTIFY_USER_ID);
        assertThat(testAppUser.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAppUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAppUser.getUserImageLarge()).isEqualTo(DEFAULT_USER_IMAGE_LARGE);
        assertThat(testAppUser.getUserImageMedium()).isEqualTo(UPDATED_USER_IMAGE_MEDIUM);
        assertThat(testAppUser.getUserImageSmall()).isEqualTo(UPDATED_USER_IMAGE_SMALL);
        assertThat(testAppUser.getSpotifyRefreshToken()).isEqualTo(DEFAULT_SPOTIFY_REFRESH_TOKEN);
        assertThat(testAppUser.getSpotifyAuthToken()).isEqualTo(DEFAULT_SPOTIFY_AUTH_TOKEN);
        assertThat(testAppUser.getLastLoginDate()).isEqualTo(UPDATED_LAST_LOGIN_DATE);
        assertThat(testAppUser.getDiscoverWeeklyBufferSettings()).isEqualTo(UPDATED_DISCOVER_WEEKLY_BUFFER_SETTINGS);
        assertThat(testAppUser.getDiscoverWeeklyBufferPlaylistID()).isEqualTo(UPDATED_DISCOVER_WEEKLY_BUFFER_PLAYLIST_ID);
        assertThat(testAppUser.getHighContrastMode()).isEqualTo(UPDATED_HIGH_CONTRAST_MODE);
        assertThat(testAppUser.getTextSize()).isEqualTo(UPDATED_TEXT_SIZE);
        assertThat(testAppUser.getEmailUpdatesEnabled()).isEqualTo(DEFAULT_EMAIL_UPDATES_ENABLED);
    }

    @Test
    @Transactional
    void fullUpdateAppUserWithPatch() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();

        // Update the appUser using partial update
        AppUser partialUpdatedAppUser = new AppUser();
        partialUpdatedAppUser.setId(appUser.getId());

        partialUpdatedAppUser
            .spotifyUserID(UPDATED_SPOTIFY_USER_ID)
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .userImageLarge(UPDATED_USER_IMAGE_LARGE)
            .userImageMedium(UPDATED_USER_IMAGE_MEDIUM)
            .userImageSmall(UPDATED_USER_IMAGE_SMALL)
            .spotifyRefreshToken(UPDATED_SPOTIFY_REFRESH_TOKEN)
            .spotifyAuthToken(UPDATED_SPOTIFY_AUTH_TOKEN)
            .lastLoginDate(UPDATED_LAST_LOGIN_DATE)
            .discoverWeeklyBufferSettings(UPDATED_DISCOVER_WEEKLY_BUFFER_SETTINGS)
            .discoverWeeklyBufferPlaylistID(UPDATED_DISCOVER_WEEKLY_BUFFER_PLAYLIST_ID)
            .highContrastMode(UPDATED_HIGH_CONTRAST_MODE)
            .textSize(UPDATED_TEXT_SIZE)
            .emailUpdatesEnabled(UPDATED_EMAIL_UPDATES_ENABLED);

        restAppUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppUser))
            )
            .andExpect(status().isOk());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
        AppUser testAppUser = appUserList.get(appUserList.size() - 1);
        assertThat(testAppUser.getSpotifyUserID()).isEqualTo(UPDATED_SPOTIFY_USER_ID);
        assertThat(testAppUser.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAppUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAppUser.getUserImageLarge()).isEqualTo(UPDATED_USER_IMAGE_LARGE);
        assertThat(testAppUser.getUserImageMedium()).isEqualTo(UPDATED_USER_IMAGE_MEDIUM);
        assertThat(testAppUser.getUserImageSmall()).isEqualTo(UPDATED_USER_IMAGE_SMALL);
        assertThat(testAppUser.getSpotifyRefreshToken()).isEqualTo(UPDATED_SPOTIFY_REFRESH_TOKEN);
        assertThat(testAppUser.getSpotifyAuthToken()).isEqualTo(UPDATED_SPOTIFY_AUTH_TOKEN);
        assertThat(testAppUser.getLastLoginDate()).isEqualTo(UPDATED_LAST_LOGIN_DATE);
        assertThat(testAppUser.getDiscoverWeeklyBufferSettings()).isEqualTo(UPDATED_DISCOVER_WEEKLY_BUFFER_SETTINGS);
        assertThat(testAppUser.getDiscoverWeeklyBufferPlaylistID()).isEqualTo(UPDATED_DISCOVER_WEEKLY_BUFFER_PLAYLIST_ID);
        assertThat(testAppUser.getHighContrastMode()).isEqualTo(UPDATED_HIGH_CONTRAST_MODE);
        assertThat(testAppUser.getTextSize()).isEqualTo(UPDATED_TEXT_SIZE);
        assertThat(testAppUser.getEmailUpdatesEnabled()).isEqualTo(UPDATED_EMAIL_UPDATES_ENABLED);
    }

    @Test
    @Transactional
    void patchNonExistingAppUser() throws Exception {
        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();
        appUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppUser() throws Exception {
        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();
        appUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppUser() throws Exception {
        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();
        appUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(appUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAppUser() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        int databaseSizeBeforeDelete = appUserRepository.findAll().size();

        // Delete the appUser
        restAppUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, appUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

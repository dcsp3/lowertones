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
import team.bham.domain.Contributor;
import team.bham.repository.ContributorRepository;

/**
 * Integration tests for the {@link ContributorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContributorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ROLE = "AAAAAAAAAA";
    private static final String UPDATED_ROLE = "BBBBBBBBBB";

    private static final String DEFAULT_MUSICBRAINZ_ID = "AAAAAAAAAA";
    private static final String UPDATED_MUSICBRAINZ_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/contributors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContributorRepository contributorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContributorMockMvc;

    private Contributor contributor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contributor createEntity(EntityManager em) {
        Contributor contributor = new Contributor().name(DEFAULT_NAME).role(DEFAULT_ROLE).musicbrainzID(DEFAULT_MUSICBRAINZ_ID);
        return contributor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contributor createUpdatedEntity(EntityManager em) {
        Contributor contributor = new Contributor().name(UPDATED_NAME).role(UPDATED_ROLE).musicbrainzID(UPDATED_MUSICBRAINZ_ID);
        return contributor;
    }

    @BeforeEach
    public void initTest() {
        contributor = createEntity(em);
    }

    @Test
    @Transactional
    void createContributor() throws Exception {
        int databaseSizeBeforeCreate = contributorRepository.findAll().size();
        // Create the Contributor
        restContributorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contributor)))
            .andExpect(status().isCreated());

        // Validate the Contributor in the database
        List<Contributor> contributorList = contributorRepository.findAll();
        assertThat(contributorList).hasSize(databaseSizeBeforeCreate + 1);
        Contributor testContributor = contributorList.get(contributorList.size() - 1);
        assertThat(testContributor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testContributor.getRole()).isEqualTo(DEFAULT_ROLE);
        assertThat(testContributor.getMusicbrainzID()).isEqualTo(DEFAULT_MUSICBRAINZ_ID);
    }

    @Test
    @Transactional
    void createContributorWithExistingId() throws Exception {
        // Create the Contributor with an existing ID
        contributor.setId(1L);

        int databaseSizeBeforeCreate = contributorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContributorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contributor)))
            .andExpect(status().isBadRequest());

        // Validate the Contributor in the database
        List<Contributor> contributorList = contributorRepository.findAll();
        assertThat(contributorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllContributors() throws Exception {
        // Initialize the database
        contributorRepository.saveAndFlush(contributor);

        // Get all the contributorList
        restContributorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contributor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE)))
            .andExpect(jsonPath("$.[*].musicbrainzID").value(hasItem(DEFAULT_MUSICBRAINZ_ID)));
    }

    @Test
    @Transactional
    void getContributor() throws Exception {
        // Initialize the database
        contributorRepository.saveAndFlush(contributor);

        // Get the contributor
        restContributorMockMvc
            .perform(get(ENTITY_API_URL_ID, contributor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contributor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE))
            .andExpect(jsonPath("$.musicbrainzID").value(DEFAULT_MUSICBRAINZ_ID));
    }

    @Test
    @Transactional
    void getNonExistingContributor() throws Exception {
        // Get the contributor
        restContributorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContributor() throws Exception {
        // Initialize the database
        contributorRepository.saveAndFlush(contributor);

        int databaseSizeBeforeUpdate = contributorRepository.findAll().size();

        // Update the contributor
        Contributor updatedContributor = contributorRepository.findById(contributor.getId()).get();
        // Disconnect from session so that the updates on updatedContributor are not directly saved in db
        em.detach(updatedContributor);
        updatedContributor.name(UPDATED_NAME).role(UPDATED_ROLE).musicbrainzID(UPDATED_MUSICBRAINZ_ID);

        restContributorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedContributor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedContributor))
            )
            .andExpect(status().isOk());

        // Validate the Contributor in the database
        List<Contributor> contributorList = contributorRepository.findAll();
        assertThat(contributorList).hasSize(databaseSizeBeforeUpdate);
        Contributor testContributor = contributorList.get(contributorList.size() - 1);
        assertThat(testContributor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContributor.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testContributor.getMusicbrainzID()).isEqualTo(UPDATED_MUSICBRAINZ_ID);
    }

    @Test
    @Transactional
    void putNonExistingContributor() throws Exception {
        int databaseSizeBeforeUpdate = contributorRepository.findAll().size();
        contributor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContributorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contributor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contributor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contributor in the database
        List<Contributor> contributorList = contributorRepository.findAll();
        assertThat(contributorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContributor() throws Exception {
        int databaseSizeBeforeUpdate = contributorRepository.findAll().size();
        contributor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContributorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contributor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contributor in the database
        List<Contributor> contributorList = contributorRepository.findAll();
        assertThat(contributorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContributor() throws Exception {
        int databaseSizeBeforeUpdate = contributorRepository.findAll().size();
        contributor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContributorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contributor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contributor in the database
        List<Contributor> contributorList = contributorRepository.findAll();
        assertThat(contributorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContributorWithPatch() throws Exception {
        // Initialize the database
        contributorRepository.saveAndFlush(contributor);

        int databaseSizeBeforeUpdate = contributorRepository.findAll().size();

        // Update the contributor using partial update
        Contributor partialUpdatedContributor = new Contributor();
        partialUpdatedContributor.setId(contributor.getId());

        partialUpdatedContributor.name(UPDATED_NAME).role(UPDATED_ROLE).musicbrainzID(UPDATED_MUSICBRAINZ_ID);

        restContributorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContributor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContributor))
            )
            .andExpect(status().isOk());

        // Validate the Contributor in the database
        List<Contributor> contributorList = contributorRepository.findAll();
        assertThat(contributorList).hasSize(databaseSizeBeforeUpdate);
        Contributor testContributor = contributorList.get(contributorList.size() - 1);
        assertThat(testContributor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContributor.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testContributor.getMusicbrainzID()).isEqualTo(UPDATED_MUSICBRAINZ_ID);
    }

    @Test
    @Transactional
    void fullUpdateContributorWithPatch() throws Exception {
        // Initialize the database
        contributorRepository.saveAndFlush(contributor);

        int databaseSizeBeforeUpdate = contributorRepository.findAll().size();

        // Update the contributor using partial update
        Contributor partialUpdatedContributor = new Contributor();
        partialUpdatedContributor.setId(contributor.getId());

        partialUpdatedContributor.name(UPDATED_NAME).role(UPDATED_ROLE).musicbrainzID(UPDATED_MUSICBRAINZ_ID);

        restContributorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContributor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContributor))
            )
            .andExpect(status().isOk());

        // Validate the Contributor in the database
        List<Contributor> contributorList = contributorRepository.findAll();
        assertThat(contributorList).hasSize(databaseSizeBeforeUpdate);
        Contributor testContributor = contributorList.get(contributorList.size() - 1);
        assertThat(testContributor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContributor.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testContributor.getMusicbrainzID()).isEqualTo(UPDATED_MUSICBRAINZ_ID);
    }

    @Test
    @Transactional
    void patchNonExistingContributor() throws Exception {
        int databaseSizeBeforeUpdate = contributorRepository.findAll().size();
        contributor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContributorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contributor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contributor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contributor in the database
        List<Contributor> contributorList = contributorRepository.findAll();
        assertThat(contributorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContributor() throws Exception {
        int databaseSizeBeforeUpdate = contributorRepository.findAll().size();
        contributor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContributorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contributor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contributor in the database
        List<Contributor> contributorList = contributorRepository.findAll();
        assertThat(contributorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContributor() throws Exception {
        int databaseSizeBeforeUpdate = contributorRepository.findAll().size();
        contributor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContributorMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(contributor))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contributor in the database
        List<Contributor> contributorList = contributorRepository.findAll();
        assertThat(contributorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContributor() throws Exception {
        // Initialize the database
        contributorRepository.saveAndFlush(contributor);

        int databaseSizeBeforeDelete = contributorRepository.findAll().size();

        // Delete the contributor
        restContributorMockMvc
            .perform(delete(ENTITY_API_URL_ID, contributor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Contributor> contributorList = contributorRepository.findAll();
        assertThat(contributorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package com.brookeboatman.magicserver.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.brookeboatman.magicserver.IntegrationTest;
import com.brookeboatman.magicserver.domain.CardInstance;
import com.brookeboatman.magicserver.repository.CardInstanceRepository;
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

/**
 * Integration tests for the {@link CardInstanceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CardInstanceResourceIT {

    private static final Integer DEFAULT_COUNT = 1;
    private static final Integer UPDATED_COUNT = 2;

    private static final Boolean DEFAULT_MISSING = false;
    private static final Boolean UPDATED_MISSING = true;

    private static final String DEFAULT_PARSED_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PARSED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/card-instances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CardInstanceRepository cardInstanceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCardInstanceMockMvc;

    private CardInstance cardInstance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CardInstance createEntity(EntityManager em) {
        CardInstance cardInstance = new CardInstance().count(DEFAULT_COUNT).missing(DEFAULT_MISSING).parsedName(DEFAULT_PARSED_NAME);
        return cardInstance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CardInstance createUpdatedEntity(EntityManager em) {
        CardInstance cardInstance = new CardInstance().count(UPDATED_COUNT).missing(UPDATED_MISSING).parsedName(UPDATED_PARSED_NAME);
        return cardInstance;
    }

    @BeforeEach
    public void initTest() {
        cardInstance = createEntity(em);
    }

    @Test
    @Transactional
    void createCardInstance() throws Exception {
        int databaseSizeBeforeCreate = cardInstanceRepository.findAll().size();
        // Create the CardInstance
        restCardInstanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardInstance)))
            .andExpect(status().isCreated());

        // Validate the CardInstance in the database
        List<CardInstance> cardInstanceList = cardInstanceRepository.findAll();
        assertThat(cardInstanceList).hasSize(databaseSizeBeforeCreate + 1);
        CardInstance testCardInstance = cardInstanceList.get(cardInstanceList.size() - 1);
        assertThat(testCardInstance.getCount()).isEqualTo(DEFAULT_COUNT);
        assertThat(testCardInstance.getMissing()).isEqualTo(DEFAULT_MISSING);
        assertThat(testCardInstance.getParsedName()).isEqualTo(DEFAULT_PARSED_NAME);
    }

    @Test
    @Transactional
    void createCardInstanceWithExistingId() throws Exception {
        // Create the CardInstance with an existing ID
        cardInstance.setId(1L);

        int databaseSizeBeforeCreate = cardInstanceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCardInstanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardInstance)))
            .andExpect(status().isBadRequest());

        // Validate the CardInstance in the database
        List<CardInstance> cardInstanceList = cardInstanceRepository.findAll();
        assertThat(cardInstanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCardInstances() throws Exception {
        // Initialize the database
        cardInstanceRepository.saveAndFlush(cardInstance);

        // Get all the cardInstanceList
        restCardInstanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardInstance.getId().intValue())))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT)))
            .andExpect(jsonPath("$.[*].missing").value(hasItem(DEFAULT_MISSING.booleanValue())))
            .andExpect(jsonPath("$.[*].parsedName").value(hasItem(DEFAULT_PARSED_NAME)));
    }

    @Test
    @Transactional
    void getCardInstance() throws Exception {
        // Initialize the database
        cardInstanceRepository.saveAndFlush(cardInstance);

        // Get the cardInstance
        restCardInstanceMockMvc
            .perform(get(ENTITY_API_URL_ID, cardInstance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cardInstance.getId().intValue()))
            .andExpect(jsonPath("$.count").value(DEFAULT_COUNT))
            .andExpect(jsonPath("$.missing").value(DEFAULT_MISSING.booleanValue()))
            .andExpect(jsonPath("$.parsedName").value(DEFAULT_PARSED_NAME));
    }

    @Test
    @Transactional
    void getNonExistingCardInstance() throws Exception {
        // Get the cardInstance
        restCardInstanceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCardInstance() throws Exception {
        // Initialize the database
        cardInstanceRepository.saveAndFlush(cardInstance);

        int databaseSizeBeforeUpdate = cardInstanceRepository.findAll().size();

        // Update the cardInstance
        CardInstance updatedCardInstance = cardInstanceRepository.findById(cardInstance.getId()).get();
        // Disconnect from session so that the updates on updatedCardInstance are not directly saved in db
        em.detach(updatedCardInstance);
        updatedCardInstance.count(UPDATED_COUNT).missing(UPDATED_MISSING).parsedName(UPDATED_PARSED_NAME);

        restCardInstanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCardInstance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCardInstance))
            )
            .andExpect(status().isOk());

        // Validate the CardInstance in the database
        List<CardInstance> cardInstanceList = cardInstanceRepository.findAll();
        assertThat(cardInstanceList).hasSize(databaseSizeBeforeUpdate);
        CardInstance testCardInstance = cardInstanceList.get(cardInstanceList.size() - 1);
        assertThat(testCardInstance.getCount()).isEqualTo(UPDATED_COUNT);
        assertThat(testCardInstance.getMissing()).isEqualTo(UPDATED_MISSING);
        assertThat(testCardInstance.getParsedName()).isEqualTo(UPDATED_PARSED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingCardInstance() throws Exception {
        int databaseSizeBeforeUpdate = cardInstanceRepository.findAll().size();
        cardInstance.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardInstanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cardInstance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardInstance))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardInstance in the database
        List<CardInstance> cardInstanceList = cardInstanceRepository.findAll();
        assertThat(cardInstanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCardInstance() throws Exception {
        int databaseSizeBeforeUpdate = cardInstanceRepository.findAll().size();
        cardInstance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardInstanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardInstance))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardInstance in the database
        List<CardInstance> cardInstanceList = cardInstanceRepository.findAll();
        assertThat(cardInstanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCardInstance() throws Exception {
        int databaseSizeBeforeUpdate = cardInstanceRepository.findAll().size();
        cardInstance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardInstanceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardInstance)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CardInstance in the database
        List<CardInstance> cardInstanceList = cardInstanceRepository.findAll();
        assertThat(cardInstanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCardInstanceWithPatch() throws Exception {
        // Initialize the database
        cardInstanceRepository.saveAndFlush(cardInstance);

        int databaseSizeBeforeUpdate = cardInstanceRepository.findAll().size();

        // Update the cardInstance using partial update
        CardInstance partialUpdatedCardInstance = new CardInstance();
        partialUpdatedCardInstance.setId(cardInstance.getId());

        partialUpdatedCardInstance.missing(UPDATED_MISSING).parsedName(UPDATED_PARSED_NAME);

        restCardInstanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCardInstance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCardInstance))
            )
            .andExpect(status().isOk());

        // Validate the CardInstance in the database
        List<CardInstance> cardInstanceList = cardInstanceRepository.findAll();
        assertThat(cardInstanceList).hasSize(databaseSizeBeforeUpdate);
        CardInstance testCardInstance = cardInstanceList.get(cardInstanceList.size() - 1);
        assertThat(testCardInstance.getCount()).isEqualTo(DEFAULT_COUNT);
        assertThat(testCardInstance.getMissing()).isEqualTo(UPDATED_MISSING);
        assertThat(testCardInstance.getParsedName()).isEqualTo(UPDATED_PARSED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateCardInstanceWithPatch() throws Exception {
        // Initialize the database
        cardInstanceRepository.saveAndFlush(cardInstance);

        int databaseSizeBeforeUpdate = cardInstanceRepository.findAll().size();

        // Update the cardInstance using partial update
        CardInstance partialUpdatedCardInstance = new CardInstance();
        partialUpdatedCardInstance.setId(cardInstance.getId());

        partialUpdatedCardInstance.count(UPDATED_COUNT).missing(UPDATED_MISSING).parsedName(UPDATED_PARSED_NAME);

        restCardInstanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCardInstance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCardInstance))
            )
            .andExpect(status().isOk());

        // Validate the CardInstance in the database
        List<CardInstance> cardInstanceList = cardInstanceRepository.findAll();
        assertThat(cardInstanceList).hasSize(databaseSizeBeforeUpdate);
        CardInstance testCardInstance = cardInstanceList.get(cardInstanceList.size() - 1);
        assertThat(testCardInstance.getCount()).isEqualTo(UPDATED_COUNT);
        assertThat(testCardInstance.getMissing()).isEqualTo(UPDATED_MISSING);
        assertThat(testCardInstance.getParsedName()).isEqualTo(UPDATED_PARSED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingCardInstance() throws Exception {
        int databaseSizeBeforeUpdate = cardInstanceRepository.findAll().size();
        cardInstance.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardInstanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cardInstance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardInstance))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardInstance in the database
        List<CardInstance> cardInstanceList = cardInstanceRepository.findAll();
        assertThat(cardInstanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCardInstance() throws Exception {
        int databaseSizeBeforeUpdate = cardInstanceRepository.findAll().size();
        cardInstance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardInstanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardInstance))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardInstance in the database
        List<CardInstance> cardInstanceList = cardInstanceRepository.findAll();
        assertThat(cardInstanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCardInstance() throws Exception {
        int databaseSizeBeforeUpdate = cardInstanceRepository.findAll().size();
        cardInstance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardInstanceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cardInstance))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CardInstance in the database
        List<CardInstance> cardInstanceList = cardInstanceRepository.findAll();
        assertThat(cardInstanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCardInstance() throws Exception {
        // Initialize the database
        cardInstanceRepository.saveAndFlush(cardInstance);

        int databaseSizeBeforeDelete = cardInstanceRepository.findAll().size();

        // Delete the cardInstance
        restCardInstanceMockMvc
            .perform(delete(ENTITY_API_URL_ID, cardInstance.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CardInstance> cardInstanceList = cardInstanceRepository.findAll();
        assertThat(cardInstanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

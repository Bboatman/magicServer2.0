package com.brookeboatman.magicserver.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.brookeboatman.magicserver.IntegrationTest;
import com.brookeboatman.magicserver.domain.Deck;
import com.brookeboatman.magicserver.repository.DeckRepository;
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
 * Integration tests for the {@link DeckResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeckResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/decks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeckMockMvc;

    private Deck deck;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deck createEntity(EntityManager em) {
        Deck deck = new Deck().name(DEFAULT_NAME).url(DEFAULT_URL);
        return deck;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deck createUpdatedEntity(EntityManager em) {
        Deck deck = new Deck().name(UPDATED_NAME).url(UPDATED_URL);
        return deck;
    }

    @BeforeEach
    public void initTest() {
        deck = createEntity(em);
    }

    @Test
    @Transactional
    void createDeck() throws Exception {
        int databaseSizeBeforeCreate = deckRepository.findAll().size();
        // Create the Deck
        restDeckMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deck)))
            .andExpect(status().isCreated());

        // Validate the Deck in the database
        List<Deck> deckList = deckRepository.findAll();
        assertThat(deckList).hasSize(databaseSizeBeforeCreate + 1);
        Deck testDeck = deckList.get(deckList.size() - 1);
        assertThat(testDeck.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDeck.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    void createDeckWithExistingId() throws Exception {
        // Create the Deck with an existing ID
        deck.setId(1L);

        int databaseSizeBeforeCreate = deckRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeckMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deck)))
            .andExpect(status().isBadRequest());

        // Validate the Deck in the database
        List<Deck> deckList = deckRepository.findAll();
        assertThat(deckList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = deckRepository.findAll().size();
        // set the field null
        deck.setUrl(null);

        // Create the Deck, which fails.

        restDeckMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deck)))
            .andExpect(status().isBadRequest());

        List<Deck> deckList = deckRepository.findAll();
        assertThat(deckList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDecks() throws Exception {
        // Initialize the database
        deckRepository.saveAndFlush(deck);

        // Get all the deckList
        restDeckMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deck.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));
    }

    @Test
    @Transactional
    void getDeck() throws Exception {
        // Initialize the database
        deckRepository.saveAndFlush(deck);

        // Get the deck
        restDeckMockMvc
            .perform(get(ENTITY_API_URL_ID, deck.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deck.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL));
    }

    @Test
    @Transactional
    void getNonExistingDeck() throws Exception {
        // Get the deck
        restDeckMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDeck() throws Exception {
        // Initialize the database
        deckRepository.saveAndFlush(deck);

        int databaseSizeBeforeUpdate = deckRepository.findAll().size();

        // Update the deck
        Deck updatedDeck = deckRepository.findById(deck.getId()).get();
        // Disconnect from session so that the updates on updatedDeck are not directly saved in db
        em.detach(updatedDeck);
        updatedDeck.name(UPDATED_NAME).url(UPDATED_URL);

        restDeckMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDeck.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDeck))
            )
            .andExpect(status().isOk());

        // Validate the Deck in the database
        List<Deck> deckList = deckRepository.findAll();
        assertThat(deckList).hasSize(databaseSizeBeforeUpdate);
        Deck testDeck = deckList.get(deckList.size() - 1);
        assertThat(testDeck.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDeck.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void putNonExistingDeck() throws Exception {
        int databaseSizeBeforeUpdate = deckRepository.findAll().size();
        deck.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeckMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deck.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deck))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deck in the database
        List<Deck> deckList = deckRepository.findAll();
        assertThat(deckList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeck() throws Exception {
        int databaseSizeBeforeUpdate = deckRepository.findAll().size();
        deck.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeckMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deck))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deck in the database
        List<Deck> deckList = deckRepository.findAll();
        assertThat(deckList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeck() throws Exception {
        int databaseSizeBeforeUpdate = deckRepository.findAll().size();
        deck.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeckMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deck)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Deck in the database
        List<Deck> deckList = deckRepository.findAll();
        assertThat(deckList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeckWithPatch() throws Exception {
        // Initialize the database
        deckRepository.saveAndFlush(deck);

        int databaseSizeBeforeUpdate = deckRepository.findAll().size();

        // Update the deck using partial update
        Deck partialUpdatedDeck = new Deck();
        partialUpdatedDeck.setId(deck.getId());

        partialUpdatedDeck.url(UPDATED_URL);

        restDeckMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeck.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeck))
            )
            .andExpect(status().isOk());

        // Validate the Deck in the database
        List<Deck> deckList = deckRepository.findAll();
        assertThat(deckList).hasSize(databaseSizeBeforeUpdate);
        Deck testDeck = deckList.get(deckList.size() - 1);
        assertThat(testDeck.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDeck.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void fullUpdateDeckWithPatch() throws Exception {
        // Initialize the database
        deckRepository.saveAndFlush(deck);

        int databaseSizeBeforeUpdate = deckRepository.findAll().size();

        // Update the deck using partial update
        Deck partialUpdatedDeck = new Deck();
        partialUpdatedDeck.setId(deck.getId());

        partialUpdatedDeck.name(UPDATED_NAME).url(UPDATED_URL);

        restDeckMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeck.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeck))
            )
            .andExpect(status().isOk());

        // Validate the Deck in the database
        List<Deck> deckList = deckRepository.findAll();
        assertThat(deckList).hasSize(databaseSizeBeforeUpdate);
        Deck testDeck = deckList.get(deckList.size() - 1);
        assertThat(testDeck.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDeck.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void patchNonExistingDeck() throws Exception {
        int databaseSizeBeforeUpdate = deckRepository.findAll().size();
        deck.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeckMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deck.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deck))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deck in the database
        List<Deck> deckList = deckRepository.findAll();
        assertThat(deckList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeck() throws Exception {
        int databaseSizeBeforeUpdate = deckRepository.findAll().size();
        deck.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeckMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deck))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deck in the database
        List<Deck> deckList = deckRepository.findAll();
        assertThat(deckList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeck() throws Exception {
        int databaseSizeBeforeUpdate = deckRepository.findAll().size();
        deck.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeckMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(deck)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Deck in the database
        List<Deck> deckList = deckRepository.findAll();
        assertThat(deckList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDeck() throws Exception {
        // Initialize the database
        deckRepository.saveAndFlush(deck);

        int databaseSizeBeforeDelete = deckRepository.findAll().size();

        // Delete the deck
        restDeckMockMvc
            .perform(delete(ENTITY_API_URL_ID, deck.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Deck> deckList = deckRepository.findAll();
        assertThat(deckList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

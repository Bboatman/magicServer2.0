package com.brookeboatman.magicserver.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.brookeboatman.magicserver.IntegrationTest;
import com.brookeboatman.magicserver.domain.Card;
import com.brookeboatman.magicserver.repository.CardRepository;
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
 * Integration tests for the {@link CardResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CardResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_RARITY = 1;
    private static final Integer UPDATED_RARITY = 2;

    private static final Long DEFAULT_CARD_TYPE = 1L;
    private static final Long UPDATED_CARD_TYPE = 2L;

    private static final String DEFAULT_TOUGHNESS = "AAAAAAAAAA";
    private static final String UPDATED_TOUGHNESS = "BBBBBBBBBB";

    private static final Long DEFAULT_POWER = 1L;
    private static final Long UPDATED_POWER = 2L;

    private static final Integer DEFAULT_CMC = 1;
    private static final Integer UPDATED_CMC = 2;

    private static final Integer DEFAULT_COLOR_IDENTITY = 1;
    private static final Integer UPDATED_COLOR_IDENTITY = 2;

    private static final Float DEFAULT_X = 1F;
    private static final Float UPDATED_X = 2F;

    private static final Float DEFAULT_Y = 1F;
    private static final Float UPDATED_Y = 2F;

    private static final String ENTITY_API_URL = "/api/cards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCardMockMvc;

    private Card card;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Card createEntity(EntityManager em) {
        Card card = new Card()
            .name(DEFAULT_NAME)
            .rarity(DEFAULT_RARITY)
            .cardType(DEFAULT_CARD_TYPE)
            .toughness(DEFAULT_TOUGHNESS)
            .power(DEFAULT_POWER)
            .cmc(DEFAULT_CMC)
            .colorIdentity(DEFAULT_COLOR_IDENTITY)
            .x(DEFAULT_X)
            .y(DEFAULT_Y);
        return card;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Card createUpdatedEntity(EntityManager em) {
        Card card = new Card()
            .name(UPDATED_NAME)
            .rarity(UPDATED_RARITY)
            .cardType(UPDATED_CARD_TYPE)
            .toughness(UPDATED_TOUGHNESS)
            .power(UPDATED_POWER)
            .cmc(UPDATED_CMC)
            .colorIdentity(UPDATED_COLOR_IDENTITY)
            .x(UPDATED_X)
            .y(UPDATED_Y);
        return card;
    }

    @BeforeEach
    public void initTest() {
        card = createEntity(em);
    }

    @Test
    @Transactional
    void createCard() throws Exception {
        int databaseSizeBeforeCreate = cardRepository.findAll().size();
        // Create the Card
        restCardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(card)))
            .andExpect(status().isCreated());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeCreate + 1);
        Card testCard = cardList.get(cardList.size() - 1);
        assertThat(testCard.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCard.getRarity()).isEqualTo(DEFAULT_RARITY);
        assertThat(testCard.getCardType()).isEqualTo(DEFAULT_CARD_TYPE);
        assertThat(testCard.getToughness()).isEqualTo(DEFAULT_TOUGHNESS);
        assertThat(testCard.getPower()).isEqualTo(DEFAULT_POWER);
        assertThat(testCard.getCmc()).isEqualTo(DEFAULT_CMC);
        assertThat(testCard.getColorIdentity()).isEqualTo(DEFAULT_COLOR_IDENTITY);
        assertThat(testCard.getX()).isEqualTo(DEFAULT_X);
        assertThat(testCard.getY()).isEqualTo(DEFAULT_Y);
    }

    @Test
    @Transactional
    void createCardWithExistingId() throws Exception {
        // Create the Card with an existing ID
        card.setId(1L);

        int databaseSizeBeforeCreate = cardRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(card)))
            .andExpect(status().isBadRequest());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardRepository.findAll().size();
        // set the field null
        card.setName(null);

        // Create the Card, which fails.

        restCardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(card)))
            .andExpect(status().isBadRequest());

        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCards() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get all the cardList
        restCardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(card.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].rarity").value(hasItem(DEFAULT_RARITY)))
            .andExpect(jsonPath("$.[*].cardType").value(hasItem(DEFAULT_CARD_TYPE.intValue())))
            .andExpect(jsonPath("$.[*].toughness").value(hasItem(DEFAULT_TOUGHNESS)))
            .andExpect(jsonPath("$.[*].power").value(hasItem(DEFAULT_POWER.intValue())))
            .andExpect(jsonPath("$.[*].cmc").value(hasItem(DEFAULT_CMC)))
            .andExpect(jsonPath("$.[*].colorIdentity").value(hasItem(DEFAULT_COLOR_IDENTITY)))
            .andExpect(jsonPath("$.[*].x").value(hasItem(DEFAULT_X.doubleValue())))
            .andExpect(jsonPath("$.[*].y").value(hasItem(DEFAULT_Y.doubleValue())));
    }

    @Test
    @Transactional
    void getCard() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get the card
        restCardMockMvc
            .perform(get(ENTITY_API_URL_ID, card.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(card.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.rarity").value(DEFAULT_RARITY))
            .andExpect(jsonPath("$.cardType").value(DEFAULT_CARD_TYPE.intValue()))
            .andExpect(jsonPath("$.toughness").value(DEFAULT_TOUGHNESS))
            .andExpect(jsonPath("$.power").value(DEFAULT_POWER.intValue()))
            .andExpect(jsonPath("$.cmc").value(DEFAULT_CMC))
            .andExpect(jsonPath("$.colorIdentity").value(DEFAULT_COLOR_IDENTITY))
            .andExpect(jsonPath("$.x").value(DEFAULT_X.doubleValue()))
            .andExpect(jsonPath("$.y").value(DEFAULT_Y.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingCard() throws Exception {
        // Get the card
        restCardMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCard() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        int databaseSizeBeforeUpdate = cardRepository.findAll().size();

        // Update the card
        Card updatedCard = cardRepository.findById(card.getId()).get();
        // Disconnect from session so that the updates on updatedCard are not directly saved in db
        em.detach(updatedCard);
        updatedCard
            .name(UPDATED_NAME)
            .rarity(UPDATED_RARITY)
            .cardType(UPDATED_CARD_TYPE)
            .toughness(UPDATED_TOUGHNESS)
            .power(UPDATED_POWER)
            .cmc(UPDATED_CMC)
            .colorIdentity(UPDATED_COLOR_IDENTITY)
            .x(UPDATED_X)
            .y(UPDATED_Y);

        restCardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCard.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCard))
            )
            .andExpect(status().isOk());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeUpdate);
        Card testCard = cardList.get(cardList.size() - 1);
        assertThat(testCard.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCard.getRarity()).isEqualTo(UPDATED_RARITY);
        assertThat(testCard.getCardType()).isEqualTo(UPDATED_CARD_TYPE);
        assertThat(testCard.getToughness()).isEqualTo(UPDATED_TOUGHNESS);
        assertThat(testCard.getPower()).isEqualTo(UPDATED_POWER);
        assertThat(testCard.getCmc()).isEqualTo(UPDATED_CMC);
        assertThat(testCard.getColorIdentity()).isEqualTo(UPDATED_COLOR_IDENTITY);
        assertThat(testCard.getX()).isEqualTo(UPDATED_X);
        assertThat(testCard.getY()).isEqualTo(UPDATED_Y);
    }

    @Test
    @Transactional
    void putNonExistingCard() throws Exception {
        int databaseSizeBeforeUpdate = cardRepository.findAll().size();
        card.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, card.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(card))
            )
            .andExpect(status().isBadRequest());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCard() throws Exception {
        int databaseSizeBeforeUpdate = cardRepository.findAll().size();
        card.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(card))
            )
            .andExpect(status().isBadRequest());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCard() throws Exception {
        int databaseSizeBeforeUpdate = cardRepository.findAll().size();
        card.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(card)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCardWithPatch() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        int databaseSizeBeforeUpdate = cardRepository.findAll().size();

        // Update the card using partial update
        Card partialUpdatedCard = new Card();
        partialUpdatedCard.setId(card.getId());

        partialUpdatedCard
            .name(UPDATED_NAME)
            .toughness(UPDATED_TOUGHNESS)
            .power(UPDATED_POWER)
            .colorIdentity(UPDATED_COLOR_IDENTITY)
            .x(UPDATED_X);

        restCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCard.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCard))
            )
            .andExpect(status().isOk());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeUpdate);
        Card testCard = cardList.get(cardList.size() - 1);
        assertThat(testCard.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCard.getRarity()).isEqualTo(DEFAULT_RARITY);
        assertThat(testCard.getCardType()).isEqualTo(DEFAULT_CARD_TYPE);
        assertThat(testCard.getToughness()).isEqualTo(UPDATED_TOUGHNESS);
        assertThat(testCard.getPower()).isEqualTo(UPDATED_POWER);
        assertThat(testCard.getCmc()).isEqualTo(DEFAULT_CMC);
        assertThat(testCard.getColorIdentity()).isEqualTo(UPDATED_COLOR_IDENTITY);
        assertThat(testCard.getX()).isEqualTo(UPDATED_X);
        assertThat(testCard.getY()).isEqualTo(DEFAULT_Y);
    }

    @Test
    @Transactional
    void fullUpdateCardWithPatch() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        int databaseSizeBeforeUpdate = cardRepository.findAll().size();

        // Update the card using partial update
        Card partialUpdatedCard = new Card();
        partialUpdatedCard.setId(card.getId());

        partialUpdatedCard
            .name(UPDATED_NAME)
            .rarity(UPDATED_RARITY)
            .cardType(UPDATED_CARD_TYPE)
            .toughness(UPDATED_TOUGHNESS)
            .power(UPDATED_POWER)
            .cmc(UPDATED_CMC)
            .colorIdentity(UPDATED_COLOR_IDENTITY)
            .x(UPDATED_X)
            .y(UPDATED_Y);

        restCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCard.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCard))
            )
            .andExpect(status().isOk());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeUpdate);
        Card testCard = cardList.get(cardList.size() - 1);
        assertThat(testCard.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCard.getRarity()).isEqualTo(UPDATED_RARITY);
        assertThat(testCard.getCardType()).isEqualTo(UPDATED_CARD_TYPE);
        assertThat(testCard.getToughness()).isEqualTo(UPDATED_TOUGHNESS);
        assertThat(testCard.getPower()).isEqualTo(UPDATED_POWER);
        assertThat(testCard.getCmc()).isEqualTo(UPDATED_CMC);
        assertThat(testCard.getColorIdentity()).isEqualTo(UPDATED_COLOR_IDENTITY);
        assertThat(testCard.getX()).isEqualTo(UPDATED_X);
        assertThat(testCard.getY()).isEqualTo(UPDATED_Y);
    }

    @Test
    @Transactional
    void patchNonExistingCard() throws Exception {
        int databaseSizeBeforeUpdate = cardRepository.findAll().size();
        card.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, card.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(card))
            )
            .andExpect(status().isBadRequest());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCard() throws Exception {
        int databaseSizeBeforeUpdate = cardRepository.findAll().size();
        card.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(card))
            )
            .andExpect(status().isBadRequest());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCard() throws Exception {
        int databaseSizeBeforeUpdate = cardRepository.findAll().size();
        card.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(card)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCard() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        int databaseSizeBeforeDelete = cardRepository.findAll().size();

        // Delete the card
        restCardMockMvc
            .perform(delete(ENTITY_API_URL_ID, card.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

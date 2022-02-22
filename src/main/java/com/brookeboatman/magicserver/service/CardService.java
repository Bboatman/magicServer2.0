package com.brookeboatman.magicserver.service;

import com.brookeboatman.magicserver.domain.Card;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Card}.
 */
public interface CardService {
    /**
     * Save a card.
     *
     * @param card the entity to save.
     * @return the persisted entity.
     */
    Card save(Card card);

    /**
     * Save many cards.
     *
     * @param cardList the list of entities to save.
     * @return the persisted list of entities.
     */
    List<Card> insertAll(List<Card> cardList);

    /**
     * Partially updates a card.
     *
     * @param card the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Card> partialUpdate(Card card);

    /**
     * Get all the cards.
     *
     * @return the list of entities.
     */
    List<Card> findAll();

    /**
     * Get the "id" card.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Card> findOne(Long id);

    /**
     * Delete the "id" card.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

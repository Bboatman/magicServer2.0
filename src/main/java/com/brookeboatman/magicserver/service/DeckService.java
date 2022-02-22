package com.brookeboatman.magicserver.service;

import com.brookeboatman.magicserver.domain.Deck;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Deck}.
 */
public interface DeckService {
    /**
     * Save a deck.
     *
     * @param deck the entity to save.
     * @return the persisted entity.
     */
    Deck save(Deck deck);

    /**
     * Partially updates a deck.
     *
     * @param deck the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Deck> partialUpdate(Deck deck);

    /**
     * Get all the decks.
     *
     * @return the list of entities.
     */
    List<Deck> findAll();

    /**
     * Get the "id" deck.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Deck> findOne(Long id);

    /**
     * Delete the "id" deck.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

package boatman.brooke.magicmonolithic.service;

import boatman.brooke.magicmonolithic.domain.Card;
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

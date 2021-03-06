package com.brookeboatman.magicserver.service;

import com.brookeboatman.magicserver.domain.CardInstance;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service Interface for managing {@link CardInstance}.
 */
public interface CardInstanceService {
    /**
     * Save a cardInstance.
     *
     * @param cardInstance the entity to save.
     * @return the persisted entity.
     */
    CardInstance save(CardInstance cardInstance);

    /**
     * Save many cards.
     *
     * @param cardInstances the set of entities to save.
     * @return the persisted set of entities.
     */
    Set<CardInstance> insertAll(Set<CardInstance> cardInstances);

    /**
     * Partially updates a cardInstance.
     *
     * @param cardInstance the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CardInstance> partialUpdate(CardInstance cardInstance);

    /**
     * Get all the cardInstances.
     *
     * @return the list of entities.
     */
    List<CardInstance> findAll();

    /**
     * Get the "id" cardInstance.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CardInstance> findOne(Long id);

    /**
     * Delete the "id" cardInstance.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

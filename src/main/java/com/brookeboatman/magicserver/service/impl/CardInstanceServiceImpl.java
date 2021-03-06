package com.brookeboatman.magicserver.service.impl;

import com.brookeboatman.magicserver.domain.CardInstance;
import com.brookeboatman.magicserver.repository.CardInstanceRepository;
import com.brookeboatman.magicserver.service.CardInstanceService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CardInstance}.
 */
@Service
@Transactional
public class CardInstanceServiceImpl implements CardInstanceService {

    private final Logger log = LoggerFactory.getLogger(CardInstanceServiceImpl.class);

    private final CardInstanceRepository cardInstanceRepository;

    public CardInstanceServiceImpl(CardInstanceRepository cardInstanceRepository) {
        this.cardInstanceRepository = cardInstanceRepository;
    }

    @Override
    public CardInstance save(CardInstance cardInstance) {
        log.debug("Request to save CardInstance : {}", cardInstance);
        return cardInstanceRepository.save(cardInstance);
    }

    @Override
    public Set<CardInstance> insertAll(Set<CardInstance> cardInstances) {
        log.debug("Saving all card instances", cardInstances);
        return new HashSet<>(cardInstanceRepository.saveAll(cardInstances));
    }

    @Override
    public Optional<CardInstance> partialUpdate(CardInstance cardInstance) {
        log.debug("Request to partially update CardInstance : {}", cardInstance);

        return cardInstanceRepository
            .findById(cardInstance.getId())
            .map(existingCardInstance -> {
                if (cardInstance.getCount() != null) {
                    existingCardInstance.setCount(cardInstance.getCount());
                }
                if (cardInstance.getMissing() != null) {
                    existingCardInstance.setMissing(cardInstance.getMissing());
                }
                if (cardInstance.getParsedName() != null) {
                    existingCardInstance.setParsedName(cardInstance.getParsedName());
                }

                return existingCardInstance;
            })
            .map(cardInstanceRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardInstance> findAll() {
        log.debug("Request to get all CardInstances");
        return cardInstanceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CardInstance> findOne(Long id) {
        log.debug("Request to get CardInstance : {}", id);
        return cardInstanceRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CardInstance : {}", id);
        cardInstanceRepository.deleteById(id);
    }
}

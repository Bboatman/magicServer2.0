package com.brookeboatman.magicserver.service.impl;

import com.brookeboatman.magicserver.domain.Card;
import com.brookeboatman.magicserver.repository.CardRepository;
import com.brookeboatman.magicserver.service.CardService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Card}.
 */
@Service
@Transactional
public class CardServiceImpl implements CardService {

    private final Logger log = LoggerFactory.getLogger(CardServiceImpl.class);

    private final CardRepository cardRepository;

    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public Card save(Card card) {
        log.debug("Request to save Card : {}", card);
        return cardRepository.save(card);
    }

    @Override
    public List<Card> insertAll(List<Card> cardList) {
        return (List<Card>) cardRepository.saveAll(cardList);
    }

    @Override
    public Optional<Card> partialUpdate(Card card) {
        log.debug("Request to partially update Card : {}", card);

        return cardRepository
            .findById(card.getId())
            .map(existingCard -> {
                if (card.getName() != null) {
                    existingCard.setName(card.getName());
                }
                if (card.getRarity() != null) {
                    existingCard.setRarity(card.getRarity());
                }
                if (card.getCardType() != null) {
                    existingCard.setCardType(card.getCardType());
                }
                if (card.getToughness() != null) {
                    existingCard.setToughness(card.getToughness());
                }
                if (card.getPower() != null) {
                    existingCard.setPower(card.getPower());
                }
                if (card.getCmc() != null) {
                    existingCard.setCmc(card.getCmc());
                }
                if (card.getColorIdentity() != null) {
                    existingCard.setColorIdentity(card.getColorIdentity());
                }
                if (card.getX() != null) {
                    existingCard.setX(card.getX());
                }
                if (card.getY() != null) {
                    existingCard.setY(card.getY());
                }

                return existingCard;
            })
            .map(cardRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Card> findAll() {
        log.debug("Request to get all Cards");
        return cardRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Card> findOne(Long id) {
        log.debug("Request to get Card : {}", id);
        return cardRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Card : {}", id);
        cardRepository.deleteById(id);
    }
}

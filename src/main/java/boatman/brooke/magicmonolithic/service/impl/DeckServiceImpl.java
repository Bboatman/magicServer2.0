package boatman.brooke.magicmonolithic.service.impl;

import boatman.brooke.magicmonolithic.domain.Deck;
import boatman.brooke.magicmonolithic.repository.DeckRepository;
import boatman.brooke.magicmonolithic.service.DeckService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Deck}.
 */
@Service
@Transactional
public class DeckServiceImpl implements DeckService {

    private final Logger log = LoggerFactory.getLogger(DeckServiceImpl.class);

    private final DeckRepository deckRepository;

    public DeckServiceImpl(DeckRepository deckRepository) {
        this.deckRepository = deckRepository;
    }

    @Override
    public Deck save(Deck deck) {
        log.debug("Request to save Deck : {}", deck);
        return deckRepository.save(deck);
    }

    @Override
    public Optional<Deck> partialUpdate(Deck deck) {
        log.debug("Request to partially update Deck : {}", deck);

        return deckRepository
            .findById(deck.getId())
            .map(existingDeck -> {
                if (deck.getName() != null) {
                    existingDeck.setName(deck.getName());
                }
                if (deck.getUrl() != null) {
                    existingDeck.setUrl(deck.getUrl());
                }

                return existingDeck;
            })
            .map(deckRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Deck> findAll() {
        log.debug("Request to get all Decks");
        return deckRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Deck> findOne(Long id) {
        log.debug("Request to get Deck : {}", id);
        return deckRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Deck : {}", id);
        deckRepository.deleteById(id);
    }
}

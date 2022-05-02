package com.brookeboatman.magicserver.web.rest;

import com.brookeboatman.magicserver.domain.Card;
import com.brookeboatman.magicserver.domain.CardInstance;
import com.brookeboatman.magicserver.domain.Deck;
import com.brookeboatman.magicserver.repository.DeckRepository;
import com.brookeboatman.magicserver.service.CardInstanceService;
import com.brookeboatman.magicserver.service.CardService;
import com.brookeboatman.magicserver.service.DeckService;
import com.brookeboatman.magicserver.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.brookeboatman.magicserver.domain.Deck}.
 */
@RestController
@RequestMapping("/api")
public class DeckResource {

    private final Logger log = LoggerFactory.getLogger(DeckResource.class);

    private static final String ENTITY_NAME = "deck";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeckService deckService;

    private final CardService cardService;

    private final CardInstanceService cardInstanceService;

    private final DeckRepository deckRepository;

    public DeckResource(
        DeckService deckService,
        CardService cardService,
        CardInstanceService cardInstanceService,
        DeckRepository deckRepository
    ) {
        this.deckService = deckService;
        this.cardService = cardService;
        this.cardInstanceService = cardInstanceService;
        this.deckRepository = deckRepository;
    }

    /**
     * {@code POST  /decks} : Create a new deck.
     *
     * @param deck the deck to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deck, or with status {@code 400 (Bad Request)} if the deck has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/decks")
    public ResponseEntity<Deck> createDeck(@Valid @RequestBody Deck deck) throws URISyntaxException {
        log.debug("REST request to save Deck : {}", deck);
        if (deck.getId() != null) {
            throw new BadRequestAlertException("A new deck cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Deck result = deckService.save(deck);

        Set<CardInstance> cardInstances = deck
            .getCardInstances()
            .stream()
            .map((CardInstance c) -> {
                Optional<Card> card = cardService.findOne(c.getParsedName());
                c.setDeck(result);
                if (card.isPresent()) {
                    c.setCard(card.get());
                } else {
                    c.setMissing(true);
                    Optional<Card> bestMatch = cardService.findBestMatch(c.getParsedName());
                    if (bestMatch.isPresent()) {
                        c.setCard(bestMatch.get());
                    }
                }
                return c;
            })
            .collect(Collectors.toSet());

        cardInstanceService.insertAll(cardInstances);

        return ResponseEntity
            .created(new URI("/api/decks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /decks/:id} : Updates an existing deck.
     *
     * @param id the id of the deck to save.
     * @param deck the deck to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deck,
     * or with status {@code 400 (Bad Request)} if the deck is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deck couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/decks/{id}")
    public ResponseEntity<Deck> updateDeck(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Deck deck)
        throws URISyntaxException {
        log.debug("REST request to update Deck : {}, {}", id, deck);
        if (deck.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deck.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deckRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Deck result = deckService.save(deck);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, deck.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /decks/:id} : Partial updates given fields of an existing deck, field will ignore if it is null
     *
     * @param id the id of the deck to save.
     * @param deck the deck to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deck,
     * or with status {@code 400 (Bad Request)} if the deck is not valid,
     * or with status {@code 404 (Not Found)} if the deck is not found,
     * or with status {@code 500 (Internal Server Error)} if the deck couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/decks/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Deck> partialUpdateDeck(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Deck deck
    ) throws URISyntaxException {
        log.debug("REST request to partial update Deck partially : {}, {}", id, deck);
        if (deck.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deck.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deckRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Deck> result = deckService.partialUpdate(deck);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, deck.getId().toString())
        );
    }

    /**
     * {@code GET  /decks} : get all the decks.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of decks in body.
     */
    @GetMapping("/decks")
    public List<Deck> getAllDecks() {
        log.debug("REST request to get all Decks");
        return deckService.findAll();
    }

    /**
     * {@code GET  /decks/:id} : get the "id" deck.
     *
     * @param id the id of the deck to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deck, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/decks/{id}")
    public ResponseEntity<Deck> getDeck(@PathVariable Long id) {
        log.debug("REST request to get Deck : {}", id);
        Optional<Deck> deck = deckService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deck);
    }

    /**
     * {@code GET  /decks/urls} : get all the cards.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cards in body.
     */
    @GetMapping("/decks/urls")
    public List<String> getDeckUrls() {
        log.debug("REST request to get all deck urls");
        return deckService.getDeckUrls();
    }

    /**
     * {@code DELETE  /decks/:id} : delete the "id" deck.
     *
     * @param id the id of the deck to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/decks/{id}")
    public ResponseEntity<Void> deleteDeck(@PathVariable Long id) {
        log.debug("REST request to delete Deck : {}", id);
        deckService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

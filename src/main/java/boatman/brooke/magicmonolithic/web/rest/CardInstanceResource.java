package boatman.brooke.magicmonolithic.web.rest;

import boatman.brooke.magicmonolithic.domain.CardInstance;
import boatman.brooke.magicmonolithic.repository.CardInstanceRepository;
import boatman.brooke.magicmonolithic.service.CardInstanceService;
import boatman.brooke.magicmonolithic.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link boatman.brooke.magicmonolithic.domain.CardInstance}.
 */
@RestController
@RequestMapping("/api")
public class CardInstanceResource {

    private final Logger log = LoggerFactory.getLogger(CardInstanceResource.class);

    private static final String ENTITY_NAME = "cardInstance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CardInstanceService cardInstanceService;

    private final CardInstanceRepository cardInstanceRepository;

    public CardInstanceResource(CardInstanceService cardInstanceService, CardInstanceRepository cardInstanceRepository) {
        this.cardInstanceService = cardInstanceService;
        this.cardInstanceRepository = cardInstanceRepository;
    }

    /**
     * {@code POST  /card-instances} : Create a new cardInstance.
     *
     * @param cardInstance the cardInstance to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cardInstance, or with status {@code 400 (Bad Request)} if the cardInstance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/card-instances")
    public ResponseEntity<CardInstance> createCardInstance(@RequestBody CardInstance cardInstance) throws URISyntaxException {
        log.debug("REST request to save CardInstance : {}", cardInstance);
        if (cardInstance.getId() != null) {
            throw new BadRequestAlertException("A new cardInstance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CardInstance result = cardInstanceService.save(cardInstance);
        return ResponseEntity
            .created(new URI("/api/card-instances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /card-instances/:id} : Updates an existing cardInstance.
     *
     * @param id the id of the cardInstance to save.
     * @param cardInstance the cardInstance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardInstance,
     * or with status {@code 400 (Bad Request)} if the cardInstance is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cardInstance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/card-instances/{id}")
    public ResponseEntity<CardInstance> updateCardInstance(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CardInstance cardInstance
    ) throws URISyntaxException {
        log.debug("REST request to update CardInstance : {}, {}", id, cardInstance);
        if (cardInstance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cardInstance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cardInstanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CardInstance result = cardInstanceService.save(cardInstance);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cardInstance.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /card-instances/:id} : Partial updates given fields of an existing cardInstance, field will ignore if it is null
     *
     * @param id the id of the cardInstance to save.
     * @param cardInstance the cardInstance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardInstance,
     * or with status {@code 400 (Bad Request)} if the cardInstance is not valid,
     * or with status {@code 404 (Not Found)} if the cardInstance is not found,
     * or with status {@code 500 (Internal Server Error)} if the cardInstance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/card-instances/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CardInstance> partialUpdateCardInstance(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CardInstance cardInstance
    ) throws URISyntaxException {
        log.debug("REST request to partial update CardInstance partially : {}, {}", id, cardInstance);
        if (cardInstance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cardInstance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cardInstanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CardInstance> result = cardInstanceService.partialUpdate(cardInstance);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cardInstance.getId().toString())
        );
    }

    /**
     * {@code GET  /card-instances} : get all the cardInstances.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cardInstances in body.
     */
    @GetMapping("/card-instances")
    public List<CardInstance> getAllCardInstances() {
        log.debug("REST request to get all CardInstances");
        return cardInstanceService.findAll();
    }

    /**
     * {@code GET  /card-instances/:id} : get the "id" cardInstance.
     *
     * @param id the id of the cardInstance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cardInstance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/card-instances/{id}")
    public ResponseEntity<CardInstance> getCardInstance(@PathVariable Long id) {
        log.debug("REST request to get CardInstance : {}", id);
        Optional<CardInstance> cardInstance = cardInstanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cardInstance);
    }

    /**
     * {@code DELETE  /card-instances/:id} : delete the "id" cardInstance.
     *
     * @param id the id of the cardInstance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/card-instances/{id}")
    public ResponseEntity<Void> deleteCardInstance(@PathVariable Long id) {
        log.debug("REST request to delete CardInstance : {}", id);
        cardInstanceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

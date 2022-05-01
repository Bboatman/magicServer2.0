package com.brookeboatman.magicserver.repository;

import com.brookeboatman.magicserver.domain.Deck;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Deck entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeckRepository extends JpaRepository<Deck, Long> {
    @Query(value = "select d.url from DECK d", nativeQuery = true)
    public List<String> getDeckUrls();
}

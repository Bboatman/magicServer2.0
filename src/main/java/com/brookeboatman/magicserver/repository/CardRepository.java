package com.brookeboatman.magicserver.repository;

import com.brookeboatman.magicserver.domain.Card;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Card entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    @Query("SELECT c.name FROM Card c LEFT JOIN CardInstance instance ON instance.parsedName=c.name WHERE instance.parsedName IS NULL")
    public List<String> findUnseen();

    public Optional<Card> findByName(String cardName);
}

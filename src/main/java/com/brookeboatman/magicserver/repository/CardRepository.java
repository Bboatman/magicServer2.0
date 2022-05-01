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
    static final String findUnseenQuery =
        "select top 10 card.name from card where name not in (SELECT ci.parsed_name as name FROM card_instance ci group by parsed_name, id) order by rand()";

    @Query(value = findUnseenQuery, nativeQuery = true)
    public List<String> findUnseen();

    public Optional<Card> findByName(String cardName);
}

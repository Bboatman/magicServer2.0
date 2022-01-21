package boatman.brooke.magicmonolithic.repository;

import boatman.brooke.magicmonolithic.domain.Deck;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Deck entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeckRepository extends JpaRepository<Deck, Long> {}

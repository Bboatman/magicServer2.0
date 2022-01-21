package boatman.brooke.magicmonolithic.repository;

import boatman.brooke.magicmonolithic.domain.Card;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Card entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CardRepository extends JpaRepository<Card, Long> {}

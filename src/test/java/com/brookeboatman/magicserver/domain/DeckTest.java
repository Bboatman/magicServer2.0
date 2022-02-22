package com.brookeboatman.magicserver.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.brookeboatman.magicserver.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeckTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Deck.class);
        Deck deck1 = new Deck();
        deck1.setId(1L);
        Deck deck2 = new Deck();
        deck2.setId(deck1.getId());
        assertThat(deck1).isEqualTo(deck2);
        deck2.setId(2L);
        assertThat(deck1).isNotEqualTo(deck2);
        deck1.setId(null);
        assertThat(deck1).isNotEqualTo(deck2);
    }
}

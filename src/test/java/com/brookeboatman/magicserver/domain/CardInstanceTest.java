package com.brookeboatman.magicserver.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.brookeboatman.magicserver.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CardInstanceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CardInstance.class);
        CardInstance cardInstance1 = new CardInstance();
        cardInstance1.setId(1L);
        CardInstance cardInstance2 = new CardInstance();
        cardInstance2.setId(cardInstance1.getId());
        assertThat(cardInstance1).isEqualTo(cardInstance2);
        cardInstance2.setId(2L);
        assertThat(cardInstance1).isNotEqualTo(cardInstance2);
        cardInstance1.setId(null);
        assertThat(cardInstance1).isNotEqualTo(cardInstance2);
    }
}

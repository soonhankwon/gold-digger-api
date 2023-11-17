package dev.golddiggerapi.expenditure.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExpenditureStatusTest {

    @Test
    void values() {
        assertThat(ExpenditureStatus.values().length)
                .isEqualTo(2);
    }

    @Test
    void valueOf() {
        assertThat(ExpenditureStatus.valueOf("INCLUDED"))
                .isSameAs(ExpenditureStatus.INCLUDED);
    }
}
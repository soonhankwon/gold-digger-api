package dev.golddiggerapi.expenditure.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExpenditureCategoryTest {

    @Test
    void no_args_constructor() {
        ExpenditureCategory expenditureCategory = new ExpenditureCategory();
        assertThat(expenditureCategory).isNotNull();
    }

    @Test
    void getId() {
        ExpenditureCategory expenditureCategory = new ExpenditureCategory(1L, "식비");
        assertThat(expenditureCategory.getId()).isEqualTo(1L);
    }

    @Test
    void getName() {
        ExpenditureCategory expenditureCategory = new ExpenditureCategory(1L, "식비");
        assertThat(expenditureCategory.getName()).isEqualTo("식비");
    }
}
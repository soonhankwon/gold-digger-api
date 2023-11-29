package dev.golddiggerapi.expenditure.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "expenditure_category", indexes = {
        @Index(name = "idx_name_idx", columnList = "name")
})
public class ExpenditureCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "카테고리 ID", example = "1")
    private Long id;

    @Schema(description = "카테고리 이름", example = "식비")
    private String name;

    private Integer minimumExpenditurePerDay;

    public ExpenditureCategory(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void updateMinimumExpenditurePerDay(int expenditureAvgPerDay) {
        this.minimumExpenditurePerDay = Math.toIntExact(Math.round(expenditureAvgPerDay / 100.0) * 100);
    }
}

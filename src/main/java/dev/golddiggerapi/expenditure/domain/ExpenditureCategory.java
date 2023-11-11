package dev.golddiggerapi.expenditure.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "expenditure_category")
public class ExpenditureCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public ExpenditureCategory(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}

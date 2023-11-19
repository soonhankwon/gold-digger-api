package dev.golddiggerapi.expenditure.domain;

import dev.golddiggerapi.expenditure.controller.dto.ExpenditureRequest;
import dev.golddiggerapi.expenditure.controller.dto.ExpenditureUpdateRequest;
import dev.golddiggerapi.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Entity
@Getter
@Table(name = "expenditure", indexes = {
        @Index(name = "fk_ex_user_idx", columnList = "user_id"),
        @Index(name = "fk_ex_expenditure_category_idx", columnList = "expenditure_category_id"),
        @Index(name = "idx_expenditure_date_time_idx", columnList = "expenditure_date_time"),
        @Index(name = "idx_expenditure_status_idx", columnList = "expenditure_status")
})
public class Expenditure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    private Long amount;

    private String memo;

    @Column(name = "expenditure_date_time")
    private LocalDateTime expenditureDateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "expenditure_status")
    private ExpenditureStatus expenditureStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expenditure_category_id")
    private ExpenditureCategory expenditureCategory;

    public Expenditure(User user, ExpenditureCategory category, ExpenditureRequest request) {
        this.amount = request.amount();
        this.memo = request.memo();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:00");
        this.expenditureDateTime = LocalDateTime.parse(request.dateTime(), formatter);
        this.expenditureStatus = ExpenditureStatus.INCLUDED;
        this.user = user;
        this.expenditureCategory = category;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void update(ExpenditureUpdateRequest request, ExpenditureCategory category) {
        this.amount = request.amount();
        this.memo = request.memo();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:00");
        this.expenditureDateTime = LocalDateTime.parse(request.dateTime(), formatter);
        if(!isInputCategorySameAsThisCategory(category)) {
            this.expenditureCategory = category;
        }
        this.updatedAt = LocalDateTime.now();
    }

    private boolean isInputCategorySameAsThisCategory(ExpenditureCategory category) {
        return this.expenditureCategory == category;
    }

    public void exclude() {
        if(isExpenditureStatusIncluded()) {
            this.expenditureStatus = ExpenditureStatus.EXCLUDED;
        }
    }

    private boolean isExpenditureStatusIncluded() {
        return expenditureStatus == ExpenditureStatus.INCLUDED;
    }
}

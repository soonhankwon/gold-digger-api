package dev.golddiggerapi.user.domain;

import dev.golddiggerapi.expenditure.domain.ExpenditureCategory;
import dev.golddiggerapi.user.controller.dto.UserBudgetCreateRequest;
import dev.golddiggerapi.user.controller.dto.UserBudgetUpdateRequest;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Table(name = "user_budget")
public class UserBudget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long amount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expenditure_category_id")
    private ExpenditureCategory expenditureCategory;


    public UserBudget(User user, ExpenditureCategory category, UserBudgetCreateRequest request) {
        this.amount = request.amount();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.user = user;
        this.expenditureCategory = category;
    }

    public UserBudget(User user, ExpenditureCategory category, Long amount) {
        this.amount = amount;
        this.user = user;
        this.expenditureCategory = category;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void update(UserBudgetUpdateRequest request, ExpenditureCategory category) {
        this.amount = request.amount();
        this.updatedAt = LocalDateTime.now();
        // Input 카테고리가 같지않다면 수정합니다.
        if(!isInputCategorySameAsThisCategory(category)) {
            this.expenditureCategory = category;
        }
    }

    private boolean isInputCategorySameAsThisCategory(ExpenditureCategory category) {
        return this.expenditureCategory == category;
    }
}

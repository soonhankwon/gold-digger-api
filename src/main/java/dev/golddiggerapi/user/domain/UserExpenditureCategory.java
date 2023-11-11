package dev.golddiggerapi.user.domain;

import dev.golddiggerapi.expenditure.domain.ExpenditureCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor
@Entity
@Getter
@Table(name = "user_expenditure_category")
public class UserExpenditureCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expenditure_category_id")
    private ExpenditureCategory expenditureCategory;

    public UserExpenditureCategory(User user, ExpenditureCategory expenditureCategory, Long amount) {
        this.user = user;
        this.expenditureCategory = expenditureCategory;
        this.amount = amount;
    }

    public void updateAmount(Long amount) {
        this.amount += amount;
    }

    public void decreaseAmount(Long expenditureDifference) {
        if(expenditureDifference > this.amount) {
            this.amount = 0L;
            return;
        }
        this.amount =- expenditureDifference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserExpenditureCategory that = (UserExpenditureCategory) o;
        return Objects.equals(user, that.user) && Objects.equals(expenditureCategory, that.expenditureCategory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, expenditureCategory);
    }
}
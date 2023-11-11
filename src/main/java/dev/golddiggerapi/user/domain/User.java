package dev.golddiggerapi.user.domain;

import dev.golddiggerapi.user.controller.dto.UserSignupRequest;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@NoArgsConstructor
@Entity
@Table(name = "`user`")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountName;

    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<UserExpenditureCategory> userExpenditureCategories = new ArrayList<>();

    public User(UserSignupRequest request, Function<String, String> encoderFunction) {
        this.accountName = request.accountName();
        this.password = encoderFunction.apply(request.password());
    }

    public void addExpenditureCategory(UserExpenditureCategory userExpenditureCategory) {
        if (this.userExpenditureCategories.contains(userExpenditureCategory)) {
            UserExpenditureCategory userCategory =
                    this.userExpenditureCategories
                            .stream()
                            .filter(i -> i.equals(userExpenditureCategory))
                            .findFirst()
                            .orElseThrow();
            userCategory.updateAmount(userExpenditureCategory.getAmount());
            return;
        }
        this.userExpenditureCategories.add(userExpenditureCategory);
    }
}

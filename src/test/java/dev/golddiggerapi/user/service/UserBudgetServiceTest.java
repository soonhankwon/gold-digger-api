package dev.golddiggerapi.user.service;

import dev.golddiggerapi.expenditure.domain.ExpenditureCategory;
import dev.golddiggerapi.expenditure.repository.ExpenditureCategoryRepository;
import dev.golddiggerapi.user.controller.dto.UserBudgetCreateRequest;
import dev.golddiggerapi.user.controller.dto.UserBudgetUpdateRequest;
import dev.golddiggerapi.user.controller.dto.UserSignupRequest;
import dev.golddiggerapi.user.domain.User;
import dev.golddiggerapi.user.domain.UserBudget;
import dev.golddiggerapi.user.repository.UserBudgetRepository;
import dev.golddiggerapi.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserBudgetServiceTest {

    @InjectMocks
    UserBudgetService userBudgetService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserBudgetRepository userBudgetRepository;

    @Mock
    ExpenditureCategoryRepository expenditureCategoryRepository;

    @Test
    @Transactional
    void createUserBudget() {
        Function<String, String> encodedFunction = s -> "encoded";
        User user = new User(new UserSignupRequest("test", "1234", Boolean.FALSE, "www"), encodedFunction);
        when(userRepository.findUserByUsername("test")).thenReturn(Optional.of(user));

        ExpenditureCategory category1 = new ExpenditureCategory(1L, "식비");
        when(expenditureCategoryRepository.findById(1L)).thenReturn(Optional.of(category1));

        userBudgetService.createUserBudget("test", 1L, new UserBudgetCreateRequest(100_000L, 2023, 3));

        verify(userBudgetRepository, times(1)).save(any(UserBudget.class));
    }

    @Test
    @Transactional
    void updateUserBudget() {
        Function<String, String> encodedFunction = s -> "encoded";
        User user = new User(new UserSignupRequest("test", "1234", Boolean.FALSE, "www"), encodedFunction);
        when(userRepository.findUserByUsername("test")).thenReturn(Optional.of(user));

        ExpenditureCategory category1 = new ExpenditureCategory(1L, "식비");
        ExpenditureCategory category2 = new ExpenditureCategory(2L, "쇼핑");
        UserBudget userBudget = new UserBudget(user, category1, new UserBudgetCreateRequest(100_000L, 2023, 3));

        when(userBudgetRepository.findById(1L)).thenReturn(Optional.of(userBudget));
        when(expenditureCategoryRepository.findById(2L)).thenReturn(Optional.of(category2));

        userBudgetService.updateUserBudget("test",
                1L,
                new UserBudgetUpdateRequest(50_000L, 2023, 12, 2L));

        assertThat(userBudget.getAmount()).isEqualTo(50_000L);
    }

    @Test
    @Transactional
    void updateUserBudget_pessimistic_lock_throw_exception_same_transaction() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Function<String, String> encodedFunction = s -> "encoded";
        User user = new User(new UserSignupRequest("test", "1234", Boolean.FALSE, "www"), encodedFunction);
        when(userRepository.findUserByUsername("test")).thenReturn(Optional.of(user));

        ExpenditureCategory category1 = new ExpenditureCategory(1L, "식비");
        ExpenditureCategory category2 = new ExpenditureCategory(2L, "쇼핑");
        ExpenditureCategory category3 = new ExpenditureCategory(3L, "교통");
        UserBudget userBudget = new UserBudget(user, category1, new UserBudgetCreateRequest(100_000L, 2023, 3));

        when(userBudgetRepository.findById(1L)).thenReturn(Optional.of(userBudget));
        when(expenditureCategoryRepository.findById(2L)).thenReturn(Optional.of(category2));
        when(expenditureCategoryRepository.findById(3L)).thenReturn(Optional.of(category3));

        // 첫 번째 업데이트
        userBudgetService.updateUserBudget("test",
                1L,
                new UserBudgetUpdateRequest(50_000L, 2023, 12, 3L));

        // 커밋지연 CountDownLatch - 트랜잭션 대기
        new Thread(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 동일 트랜잭션에서의 업데이트 시도
            assertThatThrownBy(() -> userBudgetService.updateUserBudget("test",
                    1L,
                    new UserBudgetUpdateRequest(60_000L, 2023, 12, 2L)))
                    .isInstanceOf(ObjectOptimisticLockingFailureException.class);
        }).start();

        // CountDownLatch 를 사용하여 현재 스레드를 지연시키고 커밋을 늦춤
        latch.countDown();

        // 3초대기
        TimeUnit.SECONDS.sleep(3);
    }

    @Test
    void cacheUserBudgetAvgRatioByCategoryStatistic() {
    }

    @Test
    void getUserBudgetByRecommendation() {
    }

    @Test
    void createUserBudgetByRecommendation() {
    }
}
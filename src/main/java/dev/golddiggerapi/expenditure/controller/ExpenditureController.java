package dev.golddiggerapi.expenditure.controller;

import dev.golddiggerapi.expenditure.controller.dto.*;
import dev.golddiggerapi.expenditure.service.ExpenditureService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/expenditures")
public class ExpenditureController {

    private final ExpenditureService expenditureService;

    @PostMapping("/{categoryId}")
    public ResponseEntity<String> createExpenditure(@PathVariable Long categoryId,
                                                    @Validated @RequestBody ExpenditureRequest request) {
        // SecurityContextHolder 에서 정보를 꺼내서 사용할 예정입니다.
        String mockAccountName = "abc";
        String res = expenditureService.createExpenditure(mockAccountName, categoryId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PatchMapping("/{expenditureId}")
    public ResponseEntity<String> updateExpenditure(@PathVariable Long expenditureId,
                                                    @Validated @RequestBody ExpenditureUpdateRequest request) {
        // SecurityContextHolder 에서 정보를 꺼내서 사용할 예정입니다.
        String mockAccountName = "abc";
        String res = expenditureService.updateExpenditure(mockAccountName, expenditureId, request);
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/{expenditureId}")
    public ResponseEntity<ExpenditureResponse> getExpenditure(@PathVariable Long expenditureId) {
        // SecurityContextHolder 에서 정보를 꺼내서 사용할 예정입니다.
        String mockAccountName = "abc";
        ExpenditureResponse res = expenditureService.getExpenditure(mockAccountName, expenditureId);
        return ResponseEntity.ok().body(res);
    }

    @GetMapping
    public ResponseEntity<ExpenditureByUserResponse> getExpendituresByUser(@RequestParam(value = "start") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
                                                                           @RequestParam(value = "end") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end,
                                                                           @RequestParam(value = "categoryId", required = false) Long categoryId,
                                                                           @RequestParam(value = "minAndMax", required = false) Boolean hasMinAndMax) {
        // SecurityContextHolder 에서 정보를 꺼내서 사용할 예정입니다.
        String mockAccountName = "abc";
        ExpenditureByUserRequest request = new ExpenditureByUserRequest(start, end, categoryId, hasMinAndMax);
        ExpenditureByUserResponse res = expenditureService.getExpendituresByUser(mockAccountName, request);
        return ResponseEntity.ok().body(res);
    }

    @DeleteMapping("/{expenditureId}")
    public ResponseEntity<String> deleteExpenditure(@PathVariable Long expenditureId) {
        // SecurityContextHolder 에서 정보를 꺼내서 사용할 예정입니다.
        String mockAccountName = "abc";
        String res = expenditureService.deleteExpenditure(mockAccountName, expenditureId);
        return ResponseEntity.ok().body(res);
    }

    @PatchMapping("/{expenditureId}/exclude")
    public ResponseEntity<String> excludeExpenditure(@PathVariable Long expenditureId) {
        // SecurityContextHolder 에서 정보를 꺼내서 사용할 예정입니다.
        String mockAccountName = "abc";
        String res = expenditureService.excludeExpenditure(mockAccountName, expenditureId);
        return ResponseEntity.ok().body(res);
    }
}

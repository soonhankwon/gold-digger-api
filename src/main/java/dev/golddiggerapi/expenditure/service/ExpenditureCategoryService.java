package dev.golddiggerapi.expenditure.service;

import dev.golddiggerapi.expenditure.controller.dto.ExpenditureCategoryResponse;
import dev.golddiggerapi.expenditure.domain.ExpenditureCategory;
import dev.golddiggerapi.expenditure.repository.ExpenditureCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpenditureCategoryService {

    @Value("${category-expenditure.file-path}")
    private String filePath;

    private final ExpenditureCategoryRepository expenditureCategoryRepository;

    @Cacheable(value = "expenditure:category:1:collections", cacheManager = "cacheManager")
    public List<ExpenditureCategoryResponse> getExpenditureCategories() {
        List<ExpenditureCategory> categories = expenditureCategoryRepository.findAll();

        return categories.stream()
                .map(ExpenditureCategoryResponse::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveMinimumExpenditureByCategoryFromCsv() {
        Map<String, Integer> map = new ConcurrentHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String row;
            while ((row = br.readLine()) != null) {
                String[] split = row.split(",");
                String name = split[1];

                int expenditureAveragePerMonth = Integer.parseInt(split[2]);
                int expenditureAveragePerDay = expenditureAveragePerMonth / 30;
                map.put(name, map.getOrDefault(name, 0) + expenditureAveragePerDay);
            }
            map.keySet()
                    .forEach(i -> {
                        ExpenditureCategory expenditureCategory = expenditureCategoryRepository.findExpenditureCategoryByName(i);
                        expenditureCategory.updateMinimumExpenditurePerDay(map.get(i));
                    });
        } catch (IOException e) {
            log.error("csv file invalid={}", e.getMessage());
        }
    }
}

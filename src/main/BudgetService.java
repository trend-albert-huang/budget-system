package main;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MONTHS;

public class BudgetService {

    private BudgetRepository budgetRepository;

    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    public double query(LocalDate end, LocalDate start) {
        List<Budget> budgets = budgetRepository.getAll();

        YearMonth startYearMonth = YearMonth.from(start);
        YearMonth endYearMonth = YearMonth.from(end);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMM");

        if (end.compareTo(start) < 0){
            return 0.0;
        }

        List<Budget> filteredBudgets = budgets.stream().filter(budget -> {
            YearMonth budgetDate = YearMonth.parse(budget.getYearMonth(), dateTimeFormatter);

            boolean afterStart = budgetDate.compareTo(startYearMonth) >= 0;
            boolean beforeEnd = budgetDate.compareTo(endYearMonth) <= 0;
            return afterStart && beforeEnd;
        }).collect(Collectors.toList());

        Map<YearMonth, Integer> selectedmonthDaysMap = new HashMap<>();

        int monthDiff = (int) MONTHS.between(start, end);
        if (monthDiff == 0) {
            selectedmonthDaysMap.put(startYearMonth, Long.valueOf(DAYS.between(start, end) + 1).intValue());
        } else {
            for (int i = 0; i <= monthDiff; i ++){
                if (i == 0) {
                    selectedmonthDaysMap.put(startYearMonth, Long.valueOf(DAYS.between(start, startYearMonth.atEndOfMonth()) + 1).intValue());
                } else if (i == monthDiff){
                    selectedmonthDaysMap.put(endYearMonth, end.getDayOfMonth());
                } else {
                    selectedmonthDaysMap.put(startYearMonth.plusMonths(i), startYearMonth.plusMonths(i).lengthOfMonth());
                }
            }
        }

        double result = 0.0;

        for (Budget budget : filteredBudgets){
            YearMonth budgetDate = YearMonth.parse(budget.getYearMonth(), dateTimeFormatter);
            result += budget.getAmount() / budgetDate.lengthOfMonth() * selectedmonthDaysMap.get(budgetDate);
        }


        return result;
    }
}

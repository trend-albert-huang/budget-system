package test;

import main.Budget;
import main.BudgetService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Arrays;

public class BudgetServiceTest {
    private final FakeBudgetRepositoryImpl budgetRepository = new FakeBudgetRepositoryImpl();
    private BudgetService budgetService;

    @Before
    public void setup(){
        budgetService = new BudgetService(budgetRepository);
    }

    @Test
    public void partialMonth() {
        budgetRepository.setBudgets(Arrays.asList(
                new Budget("202306", 300)
        ));

        LocalDate start = Year.of(2023).atMonth(Month.JUNE).atDay(1);
        LocalDate end = Year.of(2023).atMonth(Month.JUNE).atDay(2);
        Assert.assertEquals(20.0, budgetService.query(end, start), 0);
    }

    @Test
    public void fullMonth() {
        budgetRepository.setBudgets(Arrays.asList(
                new Budget("202306", 300)
        ));

        LocalDate start = Year.of(2023).atMonth(Month.JUNE).atDay(1);
        LocalDate end = Year.of(2023).atMonth(Month.JUNE).atDay(30);
        Assert.assertEquals(300.0, budgetService.query(end, start), 0);
    }

    @Test
    public void crossTwoMonth() {
        budgetRepository.setBudgets(Arrays.asList(
                new Budget("202306", 300),
                new Budget("202307", 31)
        ));

        LocalDate start = Year.of(2023).atMonth(Month.JUNE).atDay(1);
        LocalDate end = Year.of(2023).atMonth(Month.JULY).atDay(1);
        Assert.assertEquals(301.0, budgetService.query(end, start), 0);
    }

    @Test
    public void crossThreeMonth() {
        budgetRepository.setBudgets(Arrays.asList(
                new Budget("202306", 300),
                new Budget("202307", 31),
                new Budget("202308", 310)
        ));

        LocalDate start = Year.of(2023).atMonth(Month.JUNE).atDay(1);
        LocalDate end = Year.of(2023).atMonth(Month.AUGUST).atDay(1);
        Assert.assertEquals(341.0, budgetService.query(end, start), 0);
    }

    @Test
    public void crossThreeMonth_withoutOneMonthBudget() {
        budgetRepository.setBudgets(Arrays.asList(
                new Budget("202306", 300),
                new Budget("202308", 310)
        ));

        LocalDate start = Year.of(2023).atMonth(Month.JUNE).atDay(1);
        LocalDate end = Year.of(2023).atMonth(Month.AUGUST).atDay(1);
        Assert.assertEquals(310.0, budgetService.query(end, start), 0);
    }

    @Test
    public void invalidInput() {
        budgetRepository.setBudgets(Arrays.asList(
                new Budget("202306", 300),
                new Budget("202308", 310)
        ));

        LocalDate start = Year.of(2023).atMonth(Month.JUNE).atDay(3);
        LocalDate end = Year.of(2023).atMonth(Month.JUNE).atDay(1);
        Assert.assertEquals(0.0, budgetService.query(end, start), 0);
    }
}

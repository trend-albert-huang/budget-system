package test;

import main.Budget;
import main.BudgetRepository;

import java.util.List;

public class FakeBudgetRepositoryImpl implements BudgetRepository {
    private List<Budget> budgets;

    @Override
    public List<Budget> getAll() {
        return budgets;
    }

    public void setBudgets(List<Budget> budgets) {
        this.budgets = budgets;
    }
}

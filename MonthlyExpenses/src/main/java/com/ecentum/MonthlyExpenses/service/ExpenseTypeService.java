package com.ecentum.MonthlyExpenses.service;

import com.ecentum.MonthlyExpenses.entity.ExpenseType;
import com.ecentum.MonthlyExpenses.repository.ExpenseTypeRepository;
import com.ecentum.MonthlyExpenses.exceptions.ExpenseTypeAlreadyExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
public class ExpenseTypeService {

    private final ExpenseTypeRepository expenseTypeRepository;

    public ExpenseTypeService(ExpenseTypeRepository expenseTypeRepository) {
        this.expenseTypeRepository = expenseTypeRepository;
    }

    public ExpenseType findById(Long id) {
        return expenseTypeRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }


    /**
     * Initializes the application's default data.
     *
     * This method is automatically executed during application startup due to the
     * presence of the {@link PostConstruct} annotation. It checks if there are any
     * existing ExpenseType records in the database. If no records are found, it
     * creates and saves a default ExpenseType with the name "Home".
     */
    @PostConstruct
    public void init() {
        Iterable<ExpenseType> allExpenses = expenseTypeRepository.findAll();
        if (((Collection<?>) allExpenses).isEmpty()) {
            List<ExpenseType> defaultExpenseTypes = Arrays.asList(
                    new ExpenseType(null, "House repair/needs"),
                    new ExpenseType(null, "Rent"),
                    new ExpenseType(null, "Groceries"),
                    new ExpenseType(null, "Electricity Bill"),
                    new ExpenseType(null, "Water Bill"),
                    new ExpenseType(null, "Gas Bill"),
                    new ExpenseType(null, "Cable or Internet Bill"),
                    new ExpenseType(null, "Recharge Bills"),
                    new ExpenseType(null, "Medical Bills"),
                    new ExpenseType(null, "Shopping"),
                    new ExpenseType(null, "Medicines"),
                    new ExpenseType(null, "Clothing"),
                    new ExpenseType(null, "Kids care"),
                    new ExpenseType(null, "Travel Charges"),
                    new ExpenseType(null, "Fuel for Vehicles"),
                    new ExpenseType(null, "Vehicle Servicing Charges"),
                    new ExpenseType(null, "Education"),
                    new ExpenseType(null, "Pet Care"),
                    new ExpenseType(null, "Credit Card"),
                    new ExpenseType(null, "Food for Feast"),
                    new ExpenseType(null, "Laundry"),
                    new ExpenseType(null, "Personal Care"),
                    new ExpenseType(null, "Entertainment"),
                    new ExpenseType(null, "Memberships"),
                    new ExpenseType(null, "Taxes"),
                    new ExpenseType(null, "Home Loan"),
                    new ExpenseType(null, "Vehicle Loan"),
                    new ExpenseType(null, "vehicle Insurance"),
                    new ExpenseType(null, "Health Insurance"),
                    new ExpenseType(null, "Life Insurance"),
                    new ExpenseType(null, "Medical Insurance"),
                    new ExpenseType(null, "Rental Insurance"),
                    new ExpenseType(null, "Gifts"),
                    new ExpenseType(null, "Donations"),
                    new ExpenseType(null, "Others")
            );
            expenseTypeRepository.saveAll(defaultExpenseTypes);
        }
    }


    public Iterable<ExpenseType> findAll() {
        return expenseTypeRepository.findAll();
    }

    public void deleteById(Long id) {
        ExpenseType expenseTypeToBeDeleted = findById(id);
        expenseTypeRepository.delete(expenseTypeToBeDeleted);
    }

}

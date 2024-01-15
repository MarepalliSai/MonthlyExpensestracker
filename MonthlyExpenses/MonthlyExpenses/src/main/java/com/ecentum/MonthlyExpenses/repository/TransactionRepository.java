package com.ecentum.MonthlyExpenses.repository;


import com.ecentum.MonthlyExpenses.entity.Transactions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transactions, Long> {

    /**
 * Retrieves a page of expenses with creation dates falling within the specified date range,
 * sorted in descending order of their creation dates.
 *
 * @param startDate The start date of the date range.
 * @param endDate   The end date of the date range.
 * @param page      Pageable object specifying the requested page number and page size.
 * @return A Page of Expense objects within the given date range, sorted by creation date in descending order.
 */
Page<Transactions> findByDateBetweenOrderByCreationDateDesc(LocalDate startDate, LocalDate endDate, Pageable page);

    /**
     * Retrieves a page of expenses with the specified expense type, sorted in descending order of their creation dates.
     *
     * @param expenseType The expense type to filter expenses by.
     * @param page Pageable object specifying the requested page number and page size.
     * @return A Page of Expense objects with the given expense type, sorted by creation date in descending order.
     */
    Page<Transactions> findByExpenseTypeOrderByDateDesc(String expenseType, Pageable page);

    /**
     * Retrieves a page of expenses within the specified date range and with the given expense type,
     * sorted in descending order of their creation dates.
     *
     * @param startDate   The start date of the date range.
     * @param endDate     The end date of the date range.
     * @param expenseType The expense type to filter expenses by.
     * @param page        Pageable object specifying the requested page number and page size.
     * @return A Page of Expense objects within the date range and with the specified expense type,
     *         sorted by creation date in descending order.
     */
    Page<Transactions> findByDateBetweenAndExpenseTypeOrderByCreationDateDesc(LocalDate startDate, LocalDate endDate, String expenseType, Pageable page);
}


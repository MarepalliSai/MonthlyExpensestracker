package com.ecentum.MonthlyExpenses.service;

import com.ecentum.MonthlyExpenses.entity.Transactions;
import com.ecentum.MonthlyExpenses.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.stream.StreamSupport;

@Service
public class TransactionsService {
    @Autowired
    private final TransactionRepository transactionRepository;

    public TransactionsService(TransactionRepository transactionsRepository) {
        this.transactionRepository = transactionsRepository;
    }

    public Transactions save(Transactions entity) {
        return transactionRepository.save(entity);
    }

    /**
     *
     * @param id
     * @returns the saved entity if found, otherwise throws EntityNotFoundException
     */
    public Transactions findById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sorry, the content you are looking for does not exist."));
    }

    public Page<Transactions> findAll(Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("creationDate").descending());

        return transactionRepository.findAll(sortedPageable);
    }

    public Iterable<Transactions> findAll() {
        return transactionRepository.findAll();
    }

    public void deleteById(Long id) {
        Transactions transactionsToBeDeleted = findById(id);
        transactionRepository.delete(transactionsToBeDeleted);
    }

    /**
     * Estimates the total amount of transactions from the given Iterable of Expense objects.
     *
     * @param transactions An Iterable of Expense objects containing expense data.
     * @return The total amount of transactions as a BigDecimal value.
     */
    public BigDecimal getTotalAmount(Iterable<Transactions> transactions){
        return StreamSupport.
                stream(transactions.spliterator(), false)
                .toList()
                .stream()
                .map(Transactions::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Page<Transactions> getTransactionsByYearMonthAndType(int year, Month month, String expenseType, Pageable page) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return transactionRepository.findByDateBetweenAndExpenseTypeOrderByCreationDateDesc(startDate, endDate, expenseType, page);
    }

    public Page<Transactions> getTransactionsByYearMonth(int year, Month month, Pageable page) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return transactionRepository.findByDateBetweenOrderByCreationDateDesc(startDate, endDate, page);
    }

    /**
     * Retrieves a Page of Expense objects filtered by expense type.
     *
     * This method queries the expense repository to retrieve expenses of the specified expense type.
     * The results are ordered by creation date in descending order.
     *
     * @param expenseType The expense type for filtering expenses.
     * @param page The Pageable object specifying the desired page and page size.
     * @return A Page containing filtered Expense objects.
     */
    public Page<Transactions> getTransactionsByType(String expenseType, Pageable page) {
        return transactionRepository.findByExpenseTypeOrderByDateDesc(expenseType, page);
    }
}
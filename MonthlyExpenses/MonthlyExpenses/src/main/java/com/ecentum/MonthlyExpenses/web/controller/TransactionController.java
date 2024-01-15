package com.ecentum.MonthlyExpenses.web.controller;

import com.ecentum.MonthlyExpenses.entity.ExpenseType;
import com.ecentum.MonthlyExpenses.entity.Transactions;
import com.ecentum.MonthlyExpenses.exceptions.ExpenseTypeAlreadyExistsException;
import com.ecentum.MonthlyExpenses.service.ExpenseTypeService;
import com.ecentum.MonthlyExpenses.web.controller.helpers.Help;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import com.ecentum.MonthlyExpenses.service.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Month;


@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private final TransactionsService transactionService;
    private final ExpenseTypeService expenseTypeService;
    private static final int PAGE_SIZE = 10; // number of records per page

    public TransactionController(TransactionsService transactionsService, ExpenseTypeService expenseTypeService) {
        this.transactionService = transactionsService;
        this.expenseTypeService = expenseTypeService;
    }

    @ModelAttribute("totalAmount")
    public BigDecimal getTotalAmount() {
        Iterable<Transactions> transactions = transactionService.findAll();
        return transactionService.getTotalAmount(transactions);
    }

    @ModelAttribute("transactions")
    public Page<Transactions> getTransactions(@PageableDefault(size = PAGE_SIZE) Pageable page) {
        return transactionService.findAll(page);
    }

    @GetMapping("/transactions")
    public String showTransactions() {
        return "/transactions";
    }

    @ModelAttribute("expenseTypes")
    public Iterable<ExpenseType> getExpenseTypes() {
        return expenseTypeService.findAll();
    }

    @ModelAttribute
    public Transactions getTransaction() {
        return new Transactions();
    }

    @ModelAttribute
    public ExpenseType getExpenseType() {
        return new ExpenseType();
    }

    @GetMapping("/newExpenseType")
    public String showTransactionTypes() {
        return "/newExpenseType";
    }

    @PostMapping("/newExpenseType")
    public String addTransactionType(@Valid ExpenseType expenseType, Errors errors, Model model) {
        if (errors.hasErrors()) {
            return "newExpenseType";
        }
        try {
            expenseTypeService.save(expenseType);
        } catch (ExpenseTypeAlreadyExistsException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "newExpenseType";
        }
        return "redirect:/newExpenseType";
    }

    @PostMapping(value = "transactions/delete/individual/{id}")
    public String deleteTransaction(@PathVariable("id") Long id) {
        transactionService.deleteById(id);
        return "redirect:/transactions";
    }

    @PostMapping(value = "newTransactionType/delete/{id}")
    public String deleteTransactionType(@PathVariable("id") Long id) {
        expenseTypeService.deleteById(id);
        return "redirect:/newExpenseType";
    }

    @PostMapping("/addTransaction")
    public String addTransaction(@Valid Transactions transaction, Errors errors) {
        if (errors.hasErrors()) {
            return "transactions";
        }
        transactionService.save(transaction);
        return "redirect:/transactions";
    }

    @GetMapping("/updateTransaction/{id}")
    public String showUpdateTransactionForm(@PathVariable String id, Model model) {
        Long longId = Long.parseLong(id);
        Transactions transaction = transactionService.findById(longId);
        model.addAttribute("transaction", transaction);
        return "updateTransaction";
    }

    @PostMapping("/updateTransaction")
    public String updateTransaction(@Valid Transactions transaction, Errors errors) {
        if (errors.hasErrors()) {
            return "updateTransaction";
        }
        transactionService.save(transaction);
        return "redirect:/transactions";
    }

    @GetMapping("/transactions/filter")
    public String showFilteredTransactions(@RequestParam(name = "year", required = false) Integer year,
                                           @RequestParam(name = "month", required = false) Month month,
                                           @RequestParam(name = "expenseTypeFilter", required = false) String expenseType,
                                           Model model, @PageableDefault(size = PAGE_SIZE) Pageable page) {

        Page<Transactions> transactions;
        String monthToDisplay = null;
        String yearToDisplay = null;

        if (year != null && month != null && expenseType != null && !expenseType.isEmpty()) {
            transactions = transactionService.getTransactionsByYearMonthAndType(year, month, expenseType, page);
            monthToDisplay = Help.toSentenceCase(month.toString());
            yearToDisplay = year.toString();
        } else if (year != null && month != null) {
            transactions = transactionService.getTransactionsByYearMonth(year, month, page);
            monthToDisplay = Help.toSentenceCase(month.toString());
            yearToDisplay = year.toString();
        } else if (expenseType != null && !expenseType.isEmpty()) {
            transactions = transactionService.getTransactionsByType(expenseType, page);
        } else {
            transactions = transactionService.findAll(page);
        }

        model.addAttribute("transactions", transactions);
        model.addAttribute("month", monthToDisplay);
        model.addAttribute("year", yearToDisplay);
        model.addAttribute("expenseType", expenseType);

        return "transactions";
    }

}
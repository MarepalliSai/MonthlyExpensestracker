package com.ecentum.MonthlyExpenses.web.controller;

import com.ecentum.MonthlyExpenses.entity.Expense;
import com.ecentum.MonthlyExpenses.entity.ExpenseType;
import com.ecentum.MonthlyExpenses.service.ExpenseService;
import com.ecentum.MonthlyExpenses.service.ExpenseTypeService;
import com.ecentum.MonthlyExpenses.exceptions.ExpenseTypeAlreadyExistsException;
import com.ecentum.MonthlyExpenses.helpers.Help;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Month;

@Controller
@RequestMapping
public class ExpenseController {

    private final ExpenseService expenseService;
    private final ExpenseTypeService expenseTypeService;

    private static final int PAGE_SIZE = 10; //number of records per page

    public ExpenseController(ExpenseService expenseService, ExpenseTypeService expenseTypeService) {
        this.expenseService = expenseService;
        this.expenseTypeService = expenseTypeService;
    }


    /**
     * This method retrieves the total amount of all expenses and then adds it into a model
     * so that it could be accessed and displayed in view
     *
     *
     * @return The total amount of all expenses as a {@link BigDecimal} value.
     */
    @ModelAttribute("totalAmount")
    public BigDecimal getTotalAmount(){
        Iterable<Expense> expenses = expenseService.findAll();
        return expenseService.getTotalAmount(expenses);
    }


    /**
     * This method fetches a page of expenses from the expense service using the
     * provided pagination settings. The fetched expenses are then added to the
     * model, making them available for display in the view.
     *
     * @param page The pagination settings, including page number and page size.
     * @return A Page object containing the fetched expenses.
     */
    @ModelAttribute("expenses")
    public Page<Expense> getExpenses(@PageableDefault(size = PAGE_SIZE) Pageable page){
        return expenseService.findAll(page);
    }

    @GetMapping("/expenses")
    public String showExpenses(){
        return "/expenses";
    }

    @ModelAttribute("expenseTypes")
    public Iterable<ExpenseType> getExpenseTypes() {
        return expenseTypeService.findAll();
    }

    @ModelAttribute
    public Expense getExpense(){
        return new Expense();
    }

    @ModelAttribute
    public ExpenseType getExpenseType(){
        return new ExpenseType();
    }


    /**
     * Handles the deletion of an individual expense by its unique identifier.
     *
     * @param id The unique identifier of the expense to be deleted.
     * @return A view name for redirection to the "expenses" page.
     */
    @PostMapping(value = "expenses/delete/individual/{id}")
    public String deleteExpense(@PathVariable("id") Long id){
        expenseService.deleteById(id);
        return "redirect:/expenses";
    }


    /**
     * Handles the addition of a new expense record.
     *
     * @param expense The expense object to be added.
     * @param errors  The validation errors, if any, from the submitted expense.
     * @return A view name for redirection to the "expenses" page.
     */
    @PostMapping("/AddExpense")
    public String addExpense(@Valid Expense expense, Errors errors, Model model){
        if (errors.hasErrors()) {
            model.addAttribute("errorMessage", "Please fix the errors in the form.");
            return "expenses";
        }
        expenseService.save(expense);

        return "redirect:/expenses";
    }


    /**
     * Displays the update expense form.
     *
     * The expense data is then pre-populated in the updateExpense form fields for editing.
     * The user is presented with the pre-filled form to make updates to the expense details.
     *
     * @param id    The ID of the expense to be updated.
     * @param model The Spring model to add attributes for the view.
     * @return The name of the view for updating an expense record.
     */
    @GetMapping("/update/{id}")
    public String showUpdateExpenseForm(@PathVariable String id, Model model) {
        Long longId = Long.parseLong(id);

        // Retrieve the expense object by ID and add it to the model
        Expense expense = expenseService.findById(longId);
        model.addAttribute("expense", expense);

        return "updateExpense";

    }
//
//    @PostMapping("/update")
//    public String updateExpense(@Valid Expense expense, Errors errors, RedirectAttributes redirectAttributes) {
//        if (errors.hasErrors()) {
//            return "updateExpense";
//        }
//        // Fetch the existing expense from the database
//        Expense existingExpense = expenseService.findById(expense.getId());
//
//        // Update the ExpenseType and amount of the existing expense
//        existingExpense.setExpenseType(expense.getExpenseType());
//        existingExpense.setAmount(expense.getAmount());
//
//        // Save the updated expense object to the database
//        expenseService.save(existingExpense);
//
//        // Add a flash attribute to pass the updated expense ID to the expenses page
//        redirectAttributes.addFlashAttribute("updatedExpenseId", existingExpense.getId());
//
//        return "redirect:/expenses";
//    }

    /**
     * Handles filtering and displaying expenses based on the provided filter parameters.
     *
     * @param year         The year filter for expenses.
     * @param month        The month filter for expenses.
     * @param expenseType  The expense type filter for expenses.
     * @param model        Model object for adding attributes.
     * @param page         Pageable object specifying the requested page number and page size.
     * @return The view name to display the filtered expenses along with filter options.
     */
    @GetMapping("/expenses/filter")
    public String showFilteredExpenses(@RequestParam(name = "year", required = false) Integer year,
                                       @RequestParam(name = "month", required = false) Month month,
                                       @RequestParam(name = "expenseTypeFilter", required = false) String expenseType,
                                       Model model, @PageableDefault(size = PAGE_SIZE) Pageable page) {

        Page<Expense> expenses= null;
        String monthToDisplay = null;
        String yearToDisplay = null;

        // If all filters are provided (year, month, and expense type)
        if (year != null && month != null && expenseType != null && !expenseType.isEmpty()) {
            expenses = expenseService.getExpensesByYearMonthAndType(year, month, expenseType, page);
            monthToDisplay = Help.toSentenceCase(month.toString());
            yearToDisplay = year.toString();
        }
        // If only year and month filters are provided
        else if (year != null && month != null) {
            expenses = expenseService.getExpensesByYearMonth(year, month, page);
            monthToDisplay = Help.toSentenceCase(month.toString());
            yearToDisplay = year.toString();
        }
        // If only expense type filter is provided
        else if (expenseType != null && !expenseType.isEmpty()) {
            expenses = expenseService.getExpensesByType(expenseType, page);
        }
        // If no filters are provided, show all expenses
        if (expenses != null && expenses.hasContent()) {
            model.addAttribute("expenses", expenses);
            model.addAttribute("month", monthToDisplay);
            model.addAttribute("year", yearToDisplay);
            model.addAttribute("expenseType", expenseType);
            return "expenses";
        } else {
            // Handle the case when no expenses are found, e.g., display a message
            model.addAttribute("noExpensesMessage", "No expenses found for the specified filters.");
            return "noExpensesFound"; // You need to create a Thymeleaf template for this view
        }
    }

}
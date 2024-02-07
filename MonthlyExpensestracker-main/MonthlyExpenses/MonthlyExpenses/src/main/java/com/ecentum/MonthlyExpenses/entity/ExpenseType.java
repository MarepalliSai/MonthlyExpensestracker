package com.ecentum.MonthlyExpenses.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
public class ExpenseType {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "expenseType_seq_generator", sequenceName = "EXPENSE_TYPE_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "expense_category")
    @NotEmpty(message = "Please specify the type of expense")
    private String expenseCategory;

    public ExpenseType() {
    }
}

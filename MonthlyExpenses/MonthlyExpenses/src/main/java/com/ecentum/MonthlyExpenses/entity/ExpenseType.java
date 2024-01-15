package com.ecentum.MonthlyExpenses.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "expense")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class ExpenseType {

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty(message = "Please specify the type of expense")
    @Column(unique = true)
    private String expenseCategory;

}

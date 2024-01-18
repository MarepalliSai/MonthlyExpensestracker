package com.ecentum.MonthlyExpenses.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;



@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "expense_seq_generator", sequenceName = "EXPENSE_SEQ", allocationSize = 1)
    private Long id;


    @NotEmpty(message = "Please specify the transactionId")
    private String transactionId;

    @NotEmpty(message = "Please specify the expenseType")
    private String expenseType;

    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 8, fraction = 2)
    @NotNull(message = "Please specify an amount")
    private BigDecimal amount;

    @NotNull(message = "Date cannot be empty!")
    private LocalDate date;

    @CreationTimestamp
    private Timestamp creationDate;

}
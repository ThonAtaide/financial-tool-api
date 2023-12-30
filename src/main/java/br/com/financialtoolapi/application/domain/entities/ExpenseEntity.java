package br.com.financialtoolapi.application.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "EXPENSE")
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ExpenseEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "EXPENSE_DESCRIPTION", nullable = false)
    private String description;

    @Column(name = "AMOUNT", nullable = false)
    private BigDecimal amount;

    @Column(name = "IS_FIXED_EXPENSE", nullable = false)
    private boolean isFixedExpense;

    @Column(name = "DAT_PURCHASE", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date datPurchase;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    private ExpenseCategoryEntity expenseCategory;

    @ManyToOne
    @JoinColumn(name = "EXPENSE_OWNER", nullable = false)
    private UserAccountEntity owner;

    @Column(name = "DAT_CREATION", updatable = false, nullable = false)
    @CreatedDate
    private Instant datCreation;

    @Column(name = "DAT_UPDATE", updatable = false, nullable = false)
    @LastModifiedDate
    private Instant datUpdate;
}

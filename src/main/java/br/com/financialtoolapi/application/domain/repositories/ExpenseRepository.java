package br.com.financialtoolapi.application.domain.repositories;

import br.com.financialtoolapi.application.domain.entities.ExpenseEntity;
import br.com.financialtoolapi.application.dtos.out.ExpenseGroupOutputDto;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long>, JpaSpecificationExecutor<ExpenseEntity> {

    @Query("SELECT new br.com.financialtoolapi.application.dtos.out.ExpenseGroupOutputDto(e.expenseCategory.id, e.expenseCategory.name, sum(e.amount)) from ExpenseEntity e " +
            "where e.owner.id = ?1 " +
            "and e.datPurchase <= ?2 " +
            "and e.datPurchase >= ?3 " +
            "group by(e.expenseCategory.id, e.expenseCategory.name)"
    )
    Set<ExpenseGroupOutputDto> groupByExpenseCategories(
            final UUID accountIdentifier,
            final Date from,
            final Date until
    );

    @Query("SELECT e.isFixedExpense as isFixed, sum(e.amount) as amount from ExpenseEntity e " +
            "where e.owner.id = ?1 " +
            "and e.datPurchase <= ?2 " +
            "and e.datPurchase >= ?3 " +
            "group by (e.isFixedExpense)")
    Set<Tuple> groupByExpenseIsFixed(
            final UUID accountIdentifier,
            final Date from,
            final Date until
    );

    static Specification<ExpenseEntity> buildPurchaseDateSpecification(final Date from, final Date until) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("datPurchase"), until, from);
    }

    static Specification<ExpenseEntity> buildOwnerAccountSpecification(final UUID ownerAccountIdentifier) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("owner").get("id"), ownerAccountIdentifier);
    }

    static Specification<ExpenseEntity> buildCategorySpecification(final List<Long> expensecategories) {
        return (root, query, criteriaBuilder) -> {
            if (expensecategories == null || expensecategories.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.get("expenseCategory").get("id").in(expensecategories);
        };
    }

    static Specification<ExpenseEntity> buildExpenseDescriptionSpecification(final String description) {
        return (root, query, criteriaBuilder) -> {
            if (description.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.upper(root.get("description")),
                    description.toUpperCase(Locale.ROOT).concat("%"));
        };
    }
}

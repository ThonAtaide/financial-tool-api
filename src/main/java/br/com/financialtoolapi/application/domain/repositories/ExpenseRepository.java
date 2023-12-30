package br.com.financialtoolapi.application.domain.repositories;

import br.com.financialtoolapi.application.domain.entities.ExpenseEntity;
import br.com.financialtoolapi.application.dtos.out.ExpenseGroupOutputDto;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long>, JpaSpecificationExecutor<ExpenseEntity> {

    @Query("SELECT new br.com.financialtoolapi.application.dtos.out.ExpenseGroupOutputDto(e.expenseCategory.name, count(e.id)) from ExpenseEntity e " +
            "where e.owner.id = ?1 " +
            "and e.datPurchase <= ?2 " +
            "and e.datPurchase >= ?3 " +
            "group by (e.expenseCategory.name)")
    Set<ExpenseGroupOutputDto> groupByExpenseCategories(
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
}

package br.com.financialtoolapi.application.domain.repositories;

import br.com.financialtoolapi.application.domain.entities.ExpenseEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.UUID;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long>, JpaSpecificationExecutor<ExpenseEntity> {

    static Specification<ExpenseEntity> buildPurchaseDateSpecification(final Date from, final Date until) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("datPurchase"), until, from);
    }

    static Specification<ExpenseEntity> buildOwnerAccountSpecification(final UUID ownerAccountIdentifier) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("owner").get("id"), ownerAccountIdentifier);
    }
}

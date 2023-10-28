package br.com.financialtoolapi.application.domain.repositories;

import br.com.financialtoolapi.application.domain.entities.business.ExpenseCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "expenseCategories", path = "expenseCategories")
public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategoryEntity, Long> {
}

package br.com.financialtoolapi.application.domain.repositories;

import br.com.financialtoolapi.application.domain.entities.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {
}

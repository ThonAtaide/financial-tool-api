package br.com.financialtoolapi.application.domain.repositories;

import br.com.financialtoolapi.application.domain.entities.security.UserAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccountEntity, UUID> {

    Optional<UserAccountEntity> findUserAccountByEmailEquals(String email);
}

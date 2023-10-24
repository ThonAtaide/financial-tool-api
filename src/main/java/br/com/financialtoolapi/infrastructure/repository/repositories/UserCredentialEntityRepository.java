package br.com.financialtoolapi.infrastructure.repository.repositories;

import br.com.financialtoolapi.application.model.entities.UserCredentialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserCredentialEntityRepository extends JpaRepository<UserCredentialEntity, UUID> {

    Optional<UserCredentialEntity> findUserByUsernameEquals(String username);
}

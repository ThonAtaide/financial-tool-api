package br.com.financialtoolapi.application.domain.repositories;

import br.com.financialtoolapi.application.domain.entities.security.UserCredentialDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserCredentialDataEntityRepository extends JpaRepository<UserCredentialDataEntity, UUID> {

    Optional<UserCredentialDataEntity> findUserByUsernameEquals(String username);
}

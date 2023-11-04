package br.com.financialtoolapi.application.domain.entities.security;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@ToString(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_credentials_data")
public class UserCredentialDataEntity {

    @Id
    @ToString.Include
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(unique = true, nullable = false)
    @ToString.Include
    private String username;

    @Column(nullable = false)
    @ToString.Include
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_account_id", nullable = false)
    private UserAccountEntity userAccount;
}

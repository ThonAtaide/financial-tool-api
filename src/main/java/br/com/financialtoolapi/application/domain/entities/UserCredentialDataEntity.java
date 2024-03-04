package br.com.financialtoolapi.application.domain.entities;

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
@Table(name = "USER_CREDENTIALS_DATA")
public class UserCredentialDataEntity {

    @Id
    @ToString.Include
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ToString.Include
    @Column(name = "USER_USERNAME", unique = true, nullable = false)
    private String username;

    @Column(name = "USER_PASSWORD", nullable = false)
    private String password;

    @ToString.Include
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "USER_ACCOUNT_ID", nullable = false)
    private UserAccountEntity userAccount;
}

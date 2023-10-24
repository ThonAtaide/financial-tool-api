package br.com.financialtoolapi.application.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "user_authentication")
public class UserCredentialEntity {

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

    @OneToOne(optional = false)
    @JoinColumn(name = "user_profile_id", nullable = false)
    private UserProfileEntity userProfile;
}

package br.com.financialtoolapi.application.model.entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "user_profile")
@ToString(onlyExplicitlyIncluded = true)
public class UserProfileEntity {

    @Id
    @ToString.Include
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ToString.Include
    @Column(unique = true, nullable = false)
    private String email;

    @ToString.Include
    @Column(nullable = false)
    private String nickname;
}

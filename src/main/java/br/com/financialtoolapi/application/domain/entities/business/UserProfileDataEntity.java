package br.com.financialtoolapi.application.domain.entities.business;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "user_profile_data")
@ToString(onlyExplicitlyIncluded = true)
public class UserProfileDataEntity {

    @Id
    @ToString.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ToString.Include
    @Column(nullable = false)
    private String nickname;


}

package br.com.financialtoolapi.application.domain.entities.security;

import br.com.financialtoolapi.application.domain.entities.business.UserProfileDataEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.util.UUID;


@Builder
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserAccountEntity {

    @Id
    @ToString.Include
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ToString.Include
    @Column(nullable = false, unique = true)
    private String email;

    @ToString.Include
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_profile_id", nullable = false)
    private UserProfileDataEntity userProfile;
}

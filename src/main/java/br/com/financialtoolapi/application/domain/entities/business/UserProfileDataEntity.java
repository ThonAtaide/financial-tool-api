package br.com.financialtoolapi.application.domain.entities.business;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Builder
@Data
@Entity
@Table(name = "user_profile_data")
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String nickname;

}

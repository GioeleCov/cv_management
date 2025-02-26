package it.giocode.cv_managment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "candidati")
public class CandidateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "candidato_id")
    private Long candidateId;

    @Column(name = "nome", nullable = false, length = 50)
    private String name;

    @Column(name = "cognome", nullable = false, length = 50)
    private String surname;

    @Column(name = "età", nullable = false)
    private Integer age;

    @Column(name = "recapito_telefonico", nullable = false, unique = true, length = 15)
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "utente_id", unique = true)
    private UserEntity user;

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CVEntity> cvEntityList;

    @Column(name = "data_creazione")
    private LocalDateTime createdAt;

    @Column(name = "data_modifica")
    private LocalDateTime updatedAt;
}

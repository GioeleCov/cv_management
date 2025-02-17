package it.giocode.cv_managment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "cv")
public class CVEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cv_id")
    private Long cvId;

    @Column(name = "titolo_cv", nullable = false)
    private String cvTitle;

    @Column(name = "istruzione", nullable = false, columnDefinition = "TEXT")
    private String education;

    @Column(name = "lingue_parlate", nullable = false, columnDefinition = "TEXT")
    private String spokenLanguage;

    @Column(name = "competenze", nullable = false, columnDefinition = "TEXT")
    private String skills;

    @Column(name = "esperienze", nullable = false, columnDefinition = "TEXT")
    private String experiences;

    @Column(name = "immagine_profilo",  columnDefinition = "TEXT")
    private String profileImage;

    @Column(name = "nomae_file", nullable = false, unique = true)
    private String fileName;

    @ManyToOne
    @JoinColumn(name = "candidato_id")
    private CandidateEntity candidate;

    @Column(name = "data_creazione")
    private LocalDateTime createdAt;

    @Column(name = "data_modifica")
    private LocalDateTime updatedAt;
}

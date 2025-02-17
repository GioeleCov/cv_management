package it.giocode.cv_managment.repository;

import it.giocode.cv_managment.entity.CandidateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidateRepository extends JpaRepository<CandidateEntity, Long> {
    boolean existsByPhoneNumber(String phoneNumber);
    List<CandidateEntity> findByName(String name);
}

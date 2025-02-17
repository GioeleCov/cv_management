package it.giocode.cv_managment.repository;

import it.giocode.cv_managment.entity.CVEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CVRepository extends JpaRepository<CVEntity, Long> {
}

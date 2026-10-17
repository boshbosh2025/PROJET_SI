package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.autoloc.domain.Employe;

import java.util.List;

@Repository
public interface EmployeRepository extends JpaRepository<Employe, Long> {

    List<Employe> findByAgenceIdAgence(Long idAgence);
}

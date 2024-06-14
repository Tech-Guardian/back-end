package techguardian.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import techguardian.api.entity.Alert;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long>{

}

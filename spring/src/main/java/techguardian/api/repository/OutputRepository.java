package techguardian.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import techguardian.api.entity.Output;

@Repository
public interface OutputRepository extends JpaRepository<Output, Long>{

}

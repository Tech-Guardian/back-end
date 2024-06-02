package techguardian.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import techguardian.api.entity.Area;

@Repository
public interface AreaRepository extends JpaRepository<Area, Long>{

}

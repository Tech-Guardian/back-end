package techguardian.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import techguardian.api.entity.RedZone;

@Repository
public interface RedZoneRepository extends JpaRepository<RedZone, Long>{

    RedZone findRedZoneByName(String name);
}

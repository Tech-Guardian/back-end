package techguardian.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import techguardian.api.entity.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long>{

}

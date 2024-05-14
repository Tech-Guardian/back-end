package techguardian.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import techguardian.api.entity.Input;

@Repository
public interface InputRepository extends JpaRepository<Input, Long> {

}

package techguardian.api.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import techguardian.api.entity.Input;

@Repository
public interface InputRepository extends JpaRepository<Input, Long> {

    List<Input> findByDataEntrada(LocalDate dataEntrada);
}

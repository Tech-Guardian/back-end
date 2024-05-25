package techguardian.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import techguardian.api.entity.Input;

@Repository
public interface InputRepository extends JpaRepository<Input, Long> {

    List<Input> findByDataEntrada(String dataEntrada);

    List<Input> findByDataEntradaBetween(String startDate, String endDate);

    List<Input> findByDataEntradaAndHoraEntradaBetween(String dataEntrada, String startTime, String endTime);
}

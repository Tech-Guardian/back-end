package techguardian.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import techguardian.api.entity.Output;

@Repository
public interface OutputRepository extends JpaRepository<Output, Long>{

    List<Output> findByDataSaida(String dataSaida);

    List<Output> findByDataSaidaBetween(String startDate, String endDate);

    List<Output> findByDataSaidaAndHoraSaidaBetween(String dataSaida, String startTime, String endTime);
}

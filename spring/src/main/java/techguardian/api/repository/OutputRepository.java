package techguardian.api.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import techguardian.api.entity.Output;

@Repository
public interface OutputRepository extends JpaRepository<Output, Long>{

    List<Output> findByDataSaida(LocalDate dataSaida);

    List<Output> findByDataSaidaBetween(LocalDate startDate, LocalDate endDate);
}

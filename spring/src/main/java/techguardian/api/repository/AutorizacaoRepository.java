package techguardian.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import techguardian.api.entity.Autorizacao;

@Repository
public interface AutorizacaoRepository extends JpaRepository<Autorizacao, Long>{

    Optional<Autorizacao> findByNome(String nome);
}

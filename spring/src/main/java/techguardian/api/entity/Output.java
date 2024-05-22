package techguardian.api.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "registro_saida")
public class Output {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sai_id")
    private Long id;

    @Column(name = "data_saida")
    private LocalDate dataSaida;

    @Column(name = "hora_saida")
    private LocalTime horaSaida;

    @Column(name = "quant_saida")
    private Integer quantSaida;
    
    @Column(name = "status_saida")
    private String statusSaida;

    @Column(name = "obs_saida")
    private String obsSaida;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(LocalDate dataSaida) {
        this.dataSaida = dataSaida;
    }

    public LocalTime getHoraSaida() {
        return horaSaida;
    }

    public void setHoraSaida(LocalTime horaSaida) {
        this.horaSaida = horaSaida;
    }

    public Integer getQuantSaida() {
        return quantSaida;
    }

    public void setQuantSaida(Integer quantSaida) {
        this.quantSaida = quantSaida;
    }

    public String getStatusSaida() {
        return statusSaida;
    }

    public void setStatusSaida(String statusSaida) {
        this.statusSaida = statusSaida;
    }

    public String getObsSaida() {
        return obsSaida;
    }

    public void setObsSaida(String obsSaida) {
        this.obsSaida = obsSaida;
    }

}

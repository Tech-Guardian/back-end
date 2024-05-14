package techguardian.api.entity;

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
    private String dataSaida;

    @Column(name = "hora_saida")
    private String horaSaida;

    @Column(name = "quant_saida")
    private Integer quantSaida;
    
    @Column(name = "status_saida")
    private String status;

    @Column(name = "obs_saida")
    private String obsSaida;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(String dataSaida) {
        this.dataSaida = dataSaida;
    }

    public String getHoraSaida() {
        return horaSaida;
    }

    public void setHoraSaida(String horaSaida) {
        this.horaSaida = horaSaida;
    }

    public Integer getQuantSaida() {
        return quantSaida;
    }

    public void setQuantSaida(Integer quantSaida) {
        this.quantSaida = quantSaida;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObsSaida() {
        return obsSaida;
    }

    public void setObsSaida(String obsSaida) {
        this.obsSaida = obsSaida;
    }

}

package techguardian.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "registro_entrada")
public class Input {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ent_id")
    private Long id;

    @Column(name = "data_entrada")
    private String dataEntrada;

    @Column(name = "hora_entrada")
    private String horaEntrada;

    @Column(name = "quant_entrada")
    private Integer quantEntrada;
    
    @Column(name = "status_entrada")
    private String statusEntrada;

    @Column(name = "obs_entrada")
    private String obsEntrada;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(String dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public String getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(String horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public Integer getQuantEntrada() {
        return quantEntrada;
    }

    public void setQuantEntrada(Integer quantEntrada) {
        this.quantEntrada = quantEntrada;
    }

    public String getStatusEntrada() {
        return statusEntrada;
    }

    public void setStatusEntrada(String statusEntrada) {
        this.statusEntrada = statusEntrada;
    }

    public String getObsEntrada() {
        return obsEntrada;
    }

    public void setObsEntrada(String obsEntrada) {
        this.obsEntrada = obsEntrada;
    }

}

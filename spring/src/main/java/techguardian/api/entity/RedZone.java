package techguardian.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "redzone")
public class RedZone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "redZ_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "cam")
    private String camIp;

    @Column(name = "restrict_date")
    private String restrictDate;

    @Column(name = "restrict_hour")
    private String restrictHour;

    @ManyToOne
    @JoinColumn(name = "area_id")
    private Area area;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCamIp() {
        return camIp;
    }

    public void setCamIp(String camIp) {
        this.camIp = camIp;
    }

    public String getRestrictDate() {
        return restrictDate;
    }

    public void setRestrictDate(String restrictDate) {
        this.restrictDate = restrictDate;
    }

    public String getRestrictHour() {
        return restrictHour;
    }

    public void setRestrictHour(String restrictHour) {
        this.restrictHour = restrictHour;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

}

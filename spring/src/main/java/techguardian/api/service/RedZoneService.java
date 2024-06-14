package techguardian.api.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import techguardian.api.entity.Area;
import techguardian.api.entity.RedZone;
import techguardian.api.repository.AreaRepository;
import techguardian.api.repository.RedZoneRepository;

@Service
public class RedZoneService {

    @Autowired
    private RedZoneRepository redZRepo;

    @Autowired
    private AreaRepository areaRepo;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<RedZone> findAll() {
        return redZRepo.findAll();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public RedZone findRedZoneByName(String name) {
        return redZRepo.findRedZoneByName(name);  
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<RedZone> findByCamIp(String camIp) {
        return redZRepo.findByCamIpContaining(camIp);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public RedZone createRedZone(RedZone createdRedZone) {
        RedZone redZone = new RedZone();
    
        redZone.setName(createdRedZone.getName());
        redZone.setCamIp(addCamIp(null, createdRedZone.getCamIp()));
        
        if (createdRedZone.getArea() != null) {
            Area area = areaRepo.findById(createdRedZone.getArea().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Área não encontrada - ID: " + createdRedZone.getArea().getId()));
            redZone.setArea(area);
        }
    
        return redZRepo.save(redZone);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public RedZone updateRedZone(Long id, RedZone updatedRedZone) {
        RedZone redZone = redZRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("RedZone não encontrada - ID: " + id));

        redZone.setName(updatedRedZone.getName());
        redZone.setCamIp(addCamIp(redZone.getCamIp(), updatedRedZone.getCamIp()));

        Area area = areaRepo.findById(updatedRedZone.getArea().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Área não encontrada - ID: " + updatedRedZone.getArea().getId()));
        redZone.setArea(area);

        return redZRepo.save(redZone);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public RedZone deleteRedZone(Long id) {
        RedZone redZone = redZRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Redzone não encontrada - ID: " + id));
        redZRepo.deleteById(id);
        return redZone;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public String addCamIp(String existingCamIp, String newCamIp) {
        if (existingCamIp == null || existingCamIp.isEmpty()) {
            return newCamIp;
        } else {
            return existingCamIp + ";" + newCamIp;
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public RedZone addRestrictDateTime(Long id, Map<String, String> restrictInfo) {
        RedZone redZone = redZRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "RedZone não encontrada - ID: " + id));

        redZone.setStartDate(restrictInfo.get("startDate"));
        redZone.setStartHour(restrictInfo.get("startHour"));
        redZone.setEndDate(restrictInfo.get("endDate"));
        redZone.setEndHour(restrictInfo.get("endHour"));

        return redZRepo.save(redZone);
    }

}

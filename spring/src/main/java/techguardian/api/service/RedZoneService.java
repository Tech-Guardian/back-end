package techguardian.api.service;

import java.util.List;

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
    public RedZone createRedZone(RedZone createdRedZone) {
        RedZone redZone = new RedZone();
        redZone.setName(createdRedZone.getName());
        redZone.setCamIp(createdRedZone.getCamIp());
        redZone.setArea(createdRedZone.getArea());

        Area area = areaRepo.findById(redZone.getArea().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Area não encontrada - ID" + redZone.getArea().getId()));
        redZone.setArea(area);

        return redZRepo.save(redZone);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public RedZone updateRedZone(Long id, RedZone updatedRedZone) {
        RedZone redZone = redZRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("RedZone não encontrada" + id));

        redZone.setName(updatedRedZone.getName());
        redZone.setCamIp(updatedRedZone.getCamIp());

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
}

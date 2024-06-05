package techguardian.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    public List<RedZone> findAll() {
        return redZRepo.findAll();
    }

    public RedZone findRedZoneByName(String name) {
        return redZRepo.findRedZoneByName(name);  
    }

    public RedZone createRedZone(RedZone createdRedZone) {
        RedZone redZone = new RedZone();

        redZone.setName(createdRedZone.getName());
        redZone.setCamIp(addCamIp(null, createdRedZone.getCamIp()));
        redZone.setArea(createdRedZone.getArea());

        Area area = areaRepo.findById(redZone.getArea().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Área não encontrada - ID: " + redZone.getArea().getId()));
        redZone.setArea(area);

        return redZRepo.save(redZone);
    }

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

    public RedZone deleteRedZone(Long id) {
        RedZone redZone = redZRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Redzone não encontrada - ID: " + id));
        redZRepo.deleteById(id);
        return redZone;
    }

    private String addCamIp(String existingCamIp, String newCamIp) {
        if (existingCamIp == null || existingCamIp.isEmpty()) {
            return newCamIp;
        } else {
            return existingCamIp + ";" + newCamIp;
        }
    }
}

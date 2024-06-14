package techguardian.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import techguardian.api.entity.Area;
import techguardian.api.repository.AreaRepository;

@Service
public class AreaService {

    @Autowired
    private AreaRepository areaRepo;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<Area> findAll() {
        return areaRepo.findAll();
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Area findAreaByName(String name) {
        return areaRepo.findAreaByName(name);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Area createArea(Area createdArea) {
        return areaRepo.save(createdArea);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Area updateArea(Long id, Area updatedArea) {
        Area existArea = areaRepo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Área não encontrada - ID: " + id));
        if (existArea.getName() != null) {
            existArea.setName(null);
        }
        return areaRepo.save(existArea);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Area deleteArea(Long id) {
        Area area = areaRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Área não encontrada - ID: " + id));
        areaRepo.deleteById(id);
        return area;
    }

}

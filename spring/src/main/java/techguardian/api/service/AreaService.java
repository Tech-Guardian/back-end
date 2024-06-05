package techguardian.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import techguardian.api.entity.Area;
import techguardian.api.repository.AreaRepository;

@Service
public class AreaService {

    @Autowired
    private AreaRepository areaRepo;

    public List<Area> findAll() {
        return areaRepo.findAll();
    }

    public Area findAreaByName(String name) {
        return areaRepo.findAreaByName(name);
    }

    public Area createArea(Area createdArea) {
        return areaRepo.save(createdArea);
    }

    public Area updateArea(Long id, Area updatedArea) {
        Area existArea = areaRepo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Área não encontrada - ID: " + id));
        if (existArea.getName() != null) {
            existArea.setName(null);
        }
        return areaRepo.save(existArea);
    }

    public Area deleteArea(Long id) {
        Area area = areaRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Área não encontrada - ID: " + id));
        areaRepo.deleteById(id);
        return area;
    }

}

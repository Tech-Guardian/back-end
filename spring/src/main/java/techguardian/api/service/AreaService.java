package techguardian.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import techguardian.api.entity.Area;
import techguardian.api.entity.Usuario;
import techguardian.api.repository.AreaRepository;
import techguardian.api.repository.UsuarioRepository;

@Service
public class AreaService {

    @Autowired
    private AreaRepository areaRepo;

    @Autowired
    private UsuarioRepository usrRepo;

    public List<Area> findAll() {
        return areaRepo.findAll();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Area createArea(Area createdArea) {
        Usuario usuario = usrRepo.findById(createdArea.getUsuario().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "Usuário não encontrado - ID: " + createdArea.getUsuario().getId()));
        createdArea.setUsuario(usuario);
        return areaRepo.save(createdArea);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Area updateArea(Long id, Area updatedArea) {
        Area area = areaRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "Área não encontrada - ID: " + id));

        area.setName(updatedArea.getName());

        Usuario usuario = usrRepo.findById(updatedArea.getUsuario().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "Usuário não encontrado - ID: " + updatedArea.getUsuario().getId()));
        area.setUsuario(usuario);

        return areaRepo.save(area);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Area deleteArea(Long id) {
        Area area = areaRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Área não encontrado - ID: " + id));
        areaRepo.deleteById(id);
        return area;
    }
}

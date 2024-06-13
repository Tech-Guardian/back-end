package techguardian.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import techguardian.api.entity.Input;
import techguardian.api.entity.RedZone;
import techguardian.api.repository.InputRepository;
import techguardian.api.repository.RedZoneRepository;

@Service
public class InputService {

    @Autowired
    private InputRepository inputRepo;

    @Autowired
    private RedZoneRepository redZRepo;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<Input> findAll() {
        return inputRepo.findAll();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Input createInput(Input createdInput) {
        Input input = new Input();
        input.setDataEntrada(createdInput.getDataEntrada());
        input.setHoraEntrada(createdInput.getHoraEntrada());
        input.setQuantEntrada(createdInput.getQuantEntrada());
        input.setStatusEntrada(createdInput.getStatusEntrada());
        input.setObsEntrada(createdInput.getObsEntrada());

        if (createdInput.getRedZone() != null) {
            RedZone redZone = redZRepo.findById(createdInput.getRedZone().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "RedZone não encontrada - ID: " + createdInput.getRedZone().getId()));
            input.setRedZone(redZone);

            String obs = input.getObsEntrada();

            if (redZone.getRestrictDate() != null && redZone.getRestrictDate().equals(createdInput.getDataEntrada().toString())) {
                obs = addInput(obs, "Alerta: Entrada em um dia não autorizado.");
            }

            if (redZone.getRestrictHour() != null && redZone.getRestrictHour().equals(createdInput.getHoraEntrada().toString())) {
                obs = addInput(obs, "Alerta: Entrada em um horário não autorizado.");
            }

            input.setObsEntrada(obs);
        }

        return inputRepo.save(input);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Input updateInput(Long id, Input updatedInput) {
        Input existInput = inputRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entrada não encontrada - ID: " + id));

    if (!ObjectUtils.isEmpty(updatedInput.getDataEntrada())) {
        existInput.setDataEntrada(updatedInput.getDataEntrada());    
    }

    if (!ObjectUtils.isEmpty(updatedInput.getHoraEntrada())) {
        existInput.setHoraEntrada(updatedInput.getHoraEntrada());
    }

    if (!ObjectUtils.isEmpty(updatedInput.getQuantEntrada())) {
        existInput.setQuantEntrada(updatedInput.getQuantEntrada());
    }

    if (!ObjectUtils.isEmpty(updatedInput.getStatusEntrada())) {
        existInput.setStatusEntrada(updatedInput.getStatusEntrada());
    }

    if (!ObjectUtils.isEmpty(updatedInput.getObsEntrada())) {
        existInput.setObsEntrada(updatedInput.getObsEntrada());
    }

    if (!ObjectUtils.isEmpty(updatedInput.getRedZone())) {
        RedZone redZone = redZRepo.findById(updatedInput.getRedZone().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "RedZone não encontrada - ID: " + updatedInput.getRedZone().getId()));
        existInput.setRedZone(redZone);
    }

    return inputRepo.save(existInput);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Input deleteInput(Long id) {
        Input input = inputRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entrada não encontrada - ID: " + id));
        inputRepo.deleteById(id);
        return input;
    }

    public String addInput(String existingObs, String newObs) {
        if (existingObs == null || existingObs.isEmpty()) {
            return newObs;
        } else {
            return existingObs + ";" + newObs;
        }
    }
}

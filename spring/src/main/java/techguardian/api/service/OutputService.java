package techguardian.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import techguardian.api.entity.Output;
import techguardian.api.entity.RedZone;
import techguardian.api.repository.OutputRepository;
import techguardian.api.repository.RedZoneRepository;

@Service
public class OutputService {

    @Autowired
    private OutputRepository outRepo;

    @Autowired
    private RedZoneRepository redZRepo;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<Output> findAll() {
        return outRepo.findAll();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Output createOutput(Output createdOutput) {
        Output output = new Output();
        output.setDataSaida(createdOutput.getDataSaida());
        output.setHoraSaida(createdOutput.getHoraSaida());
        output.setQuantSaida(createdOutput.getQuantSaida());
        output.setStatusSaida(createdOutput.getStatusSaida());
        output.setObsSaida(createdOutput.getObsSaida());

        if (createdOutput.getRedZone() != null) {
            RedZone redZone = redZRepo.findById(createdOutput.getRedZone().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "RedZone não encontrada - ID: " + createdOutput.getRedZone().getId()));
            output.setRedZone(redZone);

            if (redZone.getRestrictDate() != null && redZone.getRestrictDate().equals(createdOutput.getDataSaida())) {
                output.setObsSaida("Alerta: Saída em um dia não autorizado.");
            }
        }

        return outRepo.save(output);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Output updateOutput(Long id, Output updatedOutput) {
        Output existOutput = outRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Saída não encontrada - ID: " + id));

    if (!ObjectUtils.isEmpty(updatedOutput.getDataSaida())) {
        existOutput.setDataSaida(updatedOutput.getDataSaida());    
    }

    if (!ObjectUtils.isEmpty(updatedOutput.getHoraSaida())) {
        existOutput.setHoraSaida(updatedOutput.getHoraSaida());
    }

    if (!ObjectUtils.isEmpty(updatedOutput.getQuantSaida())) {
        existOutput.setQuantSaida(updatedOutput.getQuantSaida());
    }

    if (!ObjectUtils.isEmpty(updatedOutput.getStatusSaida())) {
        existOutput.setStatusSaida(updatedOutput.getStatusSaida());
    }
    
    if (!ObjectUtils.isEmpty(updatedOutput.getObsSaida())) {
        existOutput.setObsSaida(updatedOutput.getObsSaida());    
    }

    if (!ObjectUtils.isEmpty(updatedOutput.getRedZone())) {
        RedZone redZone = redZRepo.findById(updatedOutput.getRedZone().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "RedZone não encontrada - ID: " + existOutput.getRedZone().getId()));
        existOutput.setRedZone(redZone);
    }

    return outRepo.save(existOutput);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Output deleteOutput(Long id) {
        Output output = outRepo.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Saída não encontrado - ID: " + id));
        outRepo.deleteById(id);
        return output;
    }
}

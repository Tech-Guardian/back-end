package techguardian.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import techguardian.api.entity.Input;
import techguardian.api.repository.InputRepository;

@Service
public class InputService {

    @Autowired
    private InputRepository inputRepo;

    public List<Input> findAll() {
        return inputRepo.findAll();
    }

    public Input createInput(Input createdInput) {
        Input input = new Input();
        input.setDataEntrada(createdInput.getDataEntrada());
        input.setHoraEntrada(createdInput.getHoraEntrada());
        input.setQuantEntrada(createdInput.getQuantEntrada());
        input.setStatus(createdInput.getStatus());
        input.setObsEntrada(createdInput.getObsEntrada());

        return inputRepo.save(input);
    }

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

    if (!ObjectUtils.isEmpty(updatedInput.getStatus())) {
        existInput.setStatus(updatedInput.getStatus());
    }

    if (!ObjectUtils.isEmpty(updatedInput.getObsEntrada())) {
        existInput.setStatus(updatedInput.getObsEntrada());
    }

    return inputRepo.save(existInput);
    }

    public Input deleteInput(Long id) {
        Input input = inputRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado - ID: " + id));
        inputRepo.deleteById(id);
        return input;
    }
}

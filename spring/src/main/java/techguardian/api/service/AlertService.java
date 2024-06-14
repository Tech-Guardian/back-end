package techguardian.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import techguardian.api.entity.Alert;
import techguardian.api.entity.Input;
import techguardian.api.entity.RedZone;
import techguardian.api.repository.AlertRepository;
import techguardian.api.repository.InputRepository;
import techguardian.api.repository.RedZoneRepository;

@Service
public class AlertService {

    @Autowired
    private AlertRepository alertRepo;

    @Autowired
    private InputRepository inputRepo;

    @Autowired
    private RedZoneRepository redZRepo;

    public List<Alert> findAll() {
        return alertRepo.findAll();
    }

    public Alert createAlert(Alert createdAlert) {
        Input input = inputRepo.findById(createdAlert.getInput().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Input não encontrado - ID: " + createdAlert.getInput().getId()));
        RedZone redZone = redZRepo.findById(createdAlert.getRedZone().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "RedZone não encontrada - ID: " + createdAlert.getRedZone().getId()));

        createdAlert.setInput(input);
        createdAlert.setRedZone(redZone);

        return alertRepo.save(createdAlert);
    }

    public Alert updateAlert(Long id, Alert updatedAlert) {
        Optional<Alert> optionalAlert = alertRepo.findById(id);

        if (optionalAlert.isPresent()) {
            Alert alert = optionalAlert.get();
            alert.setNotificacao(updatedAlert.getNotificacao());

            Input input = inputRepo.findById(updatedAlert.getInput().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Input não encontrado - ID: " + updatedAlert.getInput().getId()));
            RedZone redZone = redZRepo.findById(updatedAlert.getRedZone().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "RedZone não encontrada - ID: " + updatedAlert.getRedZone().getId()));

            alert.setInput(input);
            alert.setRedZone(redZone);

            return alertRepo.save(alert);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Notificação não encontrada - ID: " + id);
        }
    }

    public Alert deleteAlert(Long id) {
        Alert alert = alertRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Notificação não encontrada - ID: " + id));
        alertRepo.deleteById(id);
        return alert;
    }
}

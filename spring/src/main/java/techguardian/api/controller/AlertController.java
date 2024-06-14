package techguardian.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import techguardian.api.entity.Alert;
import techguardian.api.service.AlertService;

@RestController
@CrossOrigin
public class AlertController {

    @Autowired
    private AlertService alertService;

    @GetMapping("/registro/alerta")
    public List<Alert> findAll() {
        return alertService.findAll();
    }

    @PostMapping("/alerta")
    public Alert createAlert(@RequestBody Alert alert) {
        return alertService.createAlert(alert);
    }

    @PutMapping("/alerta/{id}")
    public Alert updateAlert(@PathVariable Long id, @RequestBody Alert alertDetails) {
        return alertService.updateAlert(id, alertDetails);
    }

    @DeleteMapping("/alerta/{id}")
    public Alert deleteAlert(@PathVariable Long id) {
        return alertService.deleteAlert(id);
    }

}

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

import techguardian.api.entity.Input;
import techguardian.api.service.InputService;

@RestController
@CrossOrigin
public class InputController {

    @Autowired
    private InputService inputService;

    @GetMapping("/registro/entrada")
    public List<Input> findAll() {
        return inputService.findAll();
    }

    @PostMapping("/entrada")
    public Input createInput(@RequestBody Input input) {
        return inputService.createInput(input);
    }

    @PutMapping("/entrada/{id}")
    public Input updateInput(@PathVariable Long id, @RequestBody Input input) {
        Input updatedInput = inputService.updateInput(id, input);
        return inputService.createInput(updatedInput);
    }

    @DeleteMapping("/entrada/{id}")
    public Input deleteInput(@PathVariable Long id) {
        return inputService.deleteInput(id);
    }

}
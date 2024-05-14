package techguardian.api.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Input> updateInput(@PathVariable Long id, @RequestBody Input input) {
        Input updatedInput = inputService.updateInput(id, input);
        return ResponseEntity.ok(updatedInput);
    }
    @DeleteMapping("/entrada/{id}")
    public ResponseEntity<String> deleteInput(@PathVariable Long id) {
        try {
            inputService.deleteInput(id);
            return ResponseEntity.ok("Entrada com o ID " + id + " foi deletada com sucesso.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro ao deletar a entrada.");
        }
    }

}
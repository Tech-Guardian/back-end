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

import techguardian.api.entity.Output;
import techguardian.api.service.OutputService;

@RestController
@CrossOrigin
public class OutputController {

    @Autowired
    private OutputService outService;

    @GetMapping("/registro/saida")
    public List<Output> findAll() {
        return outService.findAll();
    }

    @PostMapping("/saida")
    public Output createOutput(@RequestBody Output output) {
        return outService.createOutput(output);
    }

    @PutMapping("/saida/{id}")
    public ResponseEntity<Output> updateOutput(@PathVariable Long id, @RequestBody Output output) {
        Output updatedOutput = outService.updateOutput(id, output);
        return ResponseEntity.ok(updatedOutput);
    }

    @DeleteMapping("/saida/{id}")
    public ResponseEntity<String> deleteOutput(@PathVariable Long id) {
        try {
            outService.deleteOutput(id);
            return ResponseEntity.ok("Saida com o ID " + id + " foi deletada com sucesso.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro ao deletar a saida.");
        }
    }
}

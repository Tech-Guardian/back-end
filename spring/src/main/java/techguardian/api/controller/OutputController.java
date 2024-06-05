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
    public Output updateOutput(@PathVariable Long id, @RequestBody Output output) {
        Output updatedOutput = outService.updateOutput(id, output);
        return outService.createOutput(updatedOutput);
    }

    @DeleteMapping("/saida/{id}")
    public Output delteOutput(@PathVariable Long id) {
        return outService.deleteOutput(id);
    }
}

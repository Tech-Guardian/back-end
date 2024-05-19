package techguardian.api.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import techguardian.api.service.CSVService;

@RestController
@RequestMapping("/csv")
public class CSVController {

    @Autowired
    private CSVService csvService;

    @GetMapping("/entrada")
    public void exportToCSVInput(HttpServletResponse response) throws IOException {
        csvService.exportToCSVInput(response);
    }

    @GetMapping("/saida")
    public void exportToCSVOutput(HttpServletResponse response) throws IOException {
        csvService.exportToCSVOutput(response);
    }
}

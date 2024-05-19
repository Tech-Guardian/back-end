package techguardian.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import techguardian.api.service.ExcelService;

@RestController
@RequestMapping("/excel")
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    @GetMapping("/entrada")
    public ResponseEntity<byte[]> generateExcelForInput() {
        return excelService.generateExcelForInput();
    }

    @GetMapping("/saida")
    public ResponseEntity<byte[]> generateExcelForOutput() {
        return excelService.generateExcelForOutput();
    }
}
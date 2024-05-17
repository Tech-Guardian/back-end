package techguardian.api.controller;

import java.io.ByteArrayInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import techguardian.api.service.PDFService;

@RestController
@RequestMapping("/pdf")
public class PDFController {

    @Autowired
    private PDFService pdfService;

    @GetMapping("/entrada")
    public ResponseEntity<InputStreamResource> generateEntradaPdf() {
        ByteArrayInputStream bis = pdfService.generatePdf();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=entrada.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}

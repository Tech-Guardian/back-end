package techguardian.api.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import techguardian.api.entity.Input;
import techguardian.api.entity.Output;
import techguardian.api.repository.InputRepository;
import techguardian.api.repository.OutputRepository;
import techguardian.api.service.PDFService;

@RestController
@RequestMapping("/pdf")
public class PDFController {

    @Autowired
    private InputRepository inputRepo;

    @Autowired
    private OutputRepository outRepo;

    @GetMapping(value = "/entrada", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> inputReports(HttpServletResponse response) throws IOException {

		List<Input> allInputs = inputRepo.findAll();

		ByteArrayInputStream bis = PDFService.inputReport(allInputs);

		HttpHeaders headers = new HttpHeaders();

		headers.add("Content-Disposition", "attachment;filename=Registro_entrada.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bis));
	}

    @GetMapping(value = "/saida", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> outputReports(HttpServletResponse response) throws IOException {

		List<Output> allOutputs = outRepo.findAll();

		ByteArrayInputStream bis = PDFService.outputReport(allOutputs);

		HttpHeaders headers = new HttpHeaders();

		headers.add("Content-Disposition", "attachment;filename=Registro_saida.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bis));
	}
}

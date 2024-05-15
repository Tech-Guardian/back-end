package techguardian.api.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import jakarta.servlet.http.HttpServletResponse;
import techguardian.api.entity.Input;
import techguardian.api.entity.Output;
import techguardian.api.service.InputService;

@RestController
@CrossOrigin
@RequestMapping("/csv")
public class CSVController {

    @Autowired
    private InputService inpService;

    @Autowired
    private OutputController outService;

    @GetMapping("/entrada")
    public void exportToCSVInput(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Entrada_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);

        List<Input> listInput = inpService.findAll();

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"ID", "Data", "Hora", "Quantidade", "Status"};
        String[] nameMapping = {"id", "dataEntrada", "horaEntrada", "quantEntrada", "status"};

        csvWriter.writeHeader(csvHeader);

        for (Input input : listInput) {
            csvWriter.write(input, nameMapping);
        }

        csvWriter.close();
    }

    @GetMapping("/saida")
    public void exportToCSVOutput(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Saida_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);

        List<Output> listOutputs = outService.findAll();

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"ID", "Data", "Hora", "Quantidade", "Status"};
        String[] nameMapping = {"id", "dataSaida", "horaSaida", "quantSaida", "status"};

        csvWriter.writeHeader(csvHeader);

        for (Output output : listOutputs) {
            csvWriter.write(output, nameMapping);
        }

        csvWriter.close();
    }
}

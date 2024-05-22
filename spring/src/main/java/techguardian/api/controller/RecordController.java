package techguardian.api.controller;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import techguardian.api.service.RecordService;

@RestController
@RequestMapping("/export")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @GetMapping("/csv")
    public void exportCSV(HttpServletResponse response) throws IOException {
        recordService.exportCSV(response);
    }

    @GetMapping("/date-csv")
    public void exportCSVLocalDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, HttpServletResponse response) throws IOException {
        recordService.exportCSVLocalDate(response, date);
    }

    @GetMapping("/excel")
    public ResponseEntity<byte[]> exportExcel() {
        return recordService.exportExcel();
    }

    @GetMapping("/date-excel")
    public ResponseEntity<byte[]> exportExcelLocalDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return recordService.exportExcelLocalDate(date);
    }
}

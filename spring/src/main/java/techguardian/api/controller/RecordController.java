package techguardian.api.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
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
    public void exportCSVData(@RequestParam("date") String date, HttpServletResponse response) throws IOException {
        recordService.exportCSVData(response, date);
    }

    @GetMapping("/hour-csv")
    public void exportCSVHour(@RequestParam("date") String date, @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime, HttpServletResponse response) throws IOException {
        recordService.exportCSVHour(response, date, startTime, endTime);
    }

    @GetMapping("/dates-csv")
    public void exportCSVDataBetween(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, HttpServletResponse response) throws IOException {
        recordService.exportCSVDataBetween(response, startDate, endDate);
    }

    @GetMapping("/excel")
    public ResponseEntity<byte[]> exportExcel() {
        return recordService.exportExcel();
    }

    @GetMapping("/date-excel")
    public ResponseEntity<byte[]> exportExcelData(@RequestParam("date") String date) {
        return recordService.exportExcelData(date);
    }

    @GetMapping("/hour-excel")
    public ResponseEntity<byte[]> exportExcelHour(@RequestParam("date") String date, @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) {
        return recordService.exportExcelHour(date, startTime, endTime);
    }

    @GetMapping("/dates-excel")
    public ResponseEntity<byte[]> exportExcelDataBetween(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
        return recordService.exportExcelDataBetween(startDate, endDate);
    }
}

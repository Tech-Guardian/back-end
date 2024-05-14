package techguardian.api.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.AxisCrosses;
import org.apache.poi.xddf.usermodel.chart.AxisOrientation;
import org.apache.poi.xddf.usermodel.chart.AxisPosition;
import org.apache.poi.xddf.usermodel.chart.AxisTickMark;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.LegendPosition;
import org.apache.poi.xddf.usermodel.chart.XDDFCategoryAxis;
import org.apache.poi.xddf.usermodel.chart.XDDFChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFChartLegend;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFValueAxis;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import techguardian.api.entity.Input;
import techguardian.api.entity.Output;
import techguardian.api.service.InputService;
import techguardian.api.service.OutputService;

@RestController
@RequestMapping("/excel")
public class RecordController {

    @Autowired
    private InputService inputService;

    @Autowired
    private OutputService outputService;

    @GetMapping("/entrada")
    public ResponseEntity<byte[]> generateExcel() {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Registro_Entrada");

            List<Input> inputs = inputService.findAll();

            CellStyle headerStyle = workbook.createCellStyle();
            XSSFFont headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            XSSFRow headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Data de Entrada");
            headerRow.createCell(2).setCellValue("Hora de Entrada");
            headerRow.createCell(3).setCellValue("Quantidade de Entrada");
            headerRow.createCell(4).setCellValue("Observações");

            for (Cell cell : headerRow) {
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (Input input : inputs) {
                XSSFRow row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(input.getId());
                row.createCell(1).setCellValue(input.getDataEntrada());
                row.createCell(2).setCellValue(input.getHoraEntrada());
                row.createCell(3).setCellValue(input.getQuantEntrada());
                row.createCell(4).setCellValue(input.getStatus());
            }

            XSSFDrawing drawing = sheet.createDrawingPatriarch();
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 5, 1, 15, 15);
            XSSFChart chart = drawing.createChart(anchor);
            XDDFChartLegend legend = chart.getOrAddLegend();
            legend.setPosition(LegendPosition.TOP_RIGHT);

            XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
            bottomAxis.setMajorTickMark(AxisTickMark.OUT);
            bottomAxis.setMinorTickMark(AxisTickMark.NONE);
            bottomAxis.setCrosses(AxisCrosses.AUTO_ZERO);

            XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
            leftAxis.setMajorTickMark(AxisTickMark.OUT);
            leftAxis.setMinorTickMark(AxisTickMark.NONE);
            leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);
            leftAxis.setOrientation(AxisOrientation.MAX_MIN);

            XDDFDataSource<String> xs = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(1, inputs.size(), 2, 2));
            XDDFNumericalDataSource<Double> ys = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, inputs.size(), 3, 3));

            XDDFChartData data = chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
            XDDFChartData.Series series = data.addSeries(xs, ys);
            series.setTitle("Quantidade de Entrada", null);
            chart.plot(data);

            for (int i = 0; i < 5; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("filename", "Registro_Entrada.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            // Tratamento de erro
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/saida")
    public ResponseEntity<byte[]> recordOutput() {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Registro_Saida");

            List<Output> outputs = outputService.findAll();

            CellStyle headerStyle = workbook.createCellStyle();
            XSSFFont headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            XSSFRow headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Data de Saida");
            headerRow.createCell(2).setCellValue("Hora de Saida");
            headerRow.createCell(3).setCellValue("Quantidade de Saida");
            headerRow.createCell(4).setCellValue("Observações");

            for (Cell cell : headerRow) {
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (Output output : outputs) {
                XSSFRow row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(output.getId());
                row.createCell(1).setCellValue(output.getDataSaida());
                row.createCell(2).setCellValue(output.getHoraSaida());
                row.createCell(3).setCellValue(output.getQuantSaida());
                row.createCell(4).setCellValue(output.getStatus());
            }

            int rowCount = sheet.getLastRowNum();
            int columnCount = headerRow.getLastCellNum();
            CellRangeAddress tableRange = new CellRangeAddress(0, rowCount, 0, columnCount - 1);
            sheet.setAutoFilter(tableRange);

            XSSFDrawing drawing = sheet.createDrawingPatriarch();
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 5, 1, 15, 15);
            XSSFChart chart = drawing.createChart(anchor);
            XDDFChartLegend legend = chart.getOrAddLegend();
            legend.setPosition(LegendPosition.TOP_RIGHT);

            XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
            bottomAxis.setMajorTickMark(AxisTickMark.OUT);
            bottomAxis.setMinorTickMark(AxisTickMark.NONE);
            bottomAxis.setCrosses(AxisCrosses.AUTO_ZERO);

            XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
            leftAxis.setMajorTickMark(AxisTickMark.OUT);
            leftAxis.setMinorTickMark(AxisTickMark.NONE);
            leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);
            leftAxis.setOrientation(AxisOrientation.MAX_MIN);

            XDDFDataSource<String> xs = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(1, outputs.size(), 2, 2));
            XDDFNumericalDataSource<Double> ys = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, outputs.size(), 3, 3));

            XDDFChartData data = chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
            XDDFChartData.Series series = data.addSeries(xs, ys);
            series.setTitle("Quantidade de Saida", null);
            chart.plot(data);

            for (int i = 0; i < 5; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("filename", "Registro_Saida.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
package techguardian.api.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.springframework.stereotype.Service;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import jakarta.servlet.http.HttpServletResponse;
import techguardian.api.entity.Input;
import techguardian.api.entity.Output;
import techguardian.api.repository.InputRepository;
import techguardian.api.repository.OutputRepository;

@Service
public class RecordService {

    @Autowired
    private InputRepository inputRepo;

    @Autowired
    private OutputRepository outRepo;

    public void exportCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Registro_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);

        List<Input> listInput = inputRepo.findAll();
        List<Output> listOutput = outRepo.findAll();

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"ID", "Data", "Hora", "Quantidade", "Observações", "Status"};
        String[] inputMapping = {"id", "dataEntrada", "horaEntrada", "quantEntrada", "obsEntrada", "statusEntrada"};
        String[] outputMapping = {"id", "dataSaida", "horaSaida", "quantSaida", "obsSaida", "statusSaida"};

        csvWriter.writeHeader(csvHeader);

        for (Input input : listInput) {
            csvWriter.write(input, inputMapping);
        }

        for (Output output : listOutput) {
            csvWriter.write(output, outputMapping);
        }

        csvWriter.close();
    }

    //excel 
    public ResponseEntity<byte[]> exportExcel() {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Registro");
    
            List<Input> inputs = inputRepo.findAll();
            List<Output> outputs = outRepo.findAll();
            createHeaderRow(workbook, sheet, "ID", "Data", "Hora", "Quantidade", "Observações", "Status");
    
            int rowNum = 1;
            for (Input input : inputs) {
                XSSFRow row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(input.getId());
                row.createCell(1).setCellValue(input.getDataEntrada());
                row.createCell(2).setCellValue(input.getHoraEntrada());
                row.createCell(3).setCellValue(input.getQuantEntrada());
                row.createCell(4).setCellValue(input.getObsEntrada());
                row.createCell(5).setCellValue(input.getStatusEntrada());
            }
    
            for (Output output : outputs) {
                XSSFRow row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(output.getId());
                row.createCell(1).setCellValue(output.getDataSaida());
                row.createCell(2).setCellValue(output.getHoraSaida());
                row.createCell(3).setCellValue(output.getQuantSaida());
                row.createCell(4).setCellValue(output.getObsSaida());
                row.createCell(5).setCellValue(output.getStatusSaida());
            }
    
            createChart(sheet, inputs.size(), "Quantidade de Entrada");
    
            autoSizeColumns(sheet, 6);
    
            return createResponseEntity(workbook, "Registro.xlsx");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    private void createHeaderRow(XSSFWorkbook workbook, XSSFSheet sheet, String... headers) {
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
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    private void createChart(XSSFSheet sheet, int size, String title) {
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

        XDDFDataSource<String> xs = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(1, size, 2, 2));
        XDDFNumericalDataSource<Double> ys = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, size, 3, 3));

        XDDFChartData data = chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
        XDDFChartData.Series series = data.addSeries(xs, ys);
        series.setTitle(title, null);
        chart.plot(data);
    }

    private void autoSizeColumns(XSSFSheet sheet, int numberOfColumns) {
        for (int i = 0; i < numberOfColumns; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private ResponseEntity<byte[]> createResponseEntity(XSSFWorkbook workbook, String filename) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("filename", filename);

        return ResponseEntity.ok()
                .headers(headers)
                .body(outputStream.toByteArray());
    }
}

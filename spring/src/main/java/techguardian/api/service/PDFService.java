package techguardian.api.service;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import techguardian.api.entity.Input;
import techguardian.api.repository.InputRepository;

@Service
public class PDFService {

    private static final Logger logger = LoggerFactory.getLogger(PDFService.class);

    @Autowired
    private InputRepository inputRepository;

    public ByteArrayInputStream generatePdf() {
        List<Input> inputs = inputRepository.findAll();

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Título
            Paragraph title = new Paragraph("Relatório de Entradas")
                    .setFontSize(18)
                    .setBold()
                    .setMarginBottom(20);
            document.add(title);

            // Tabela
            float[] columnWidths = {50, 80, 80, 80, 80, 150};
            Table table = new Table(columnWidths);

            // Cabeçalho
            table.addHeaderCell(new Cell().add(new Paragraph("ID")));
            table.addHeaderCell(new Cell().add(new Paragraph("Data Entrada")));
            table.addHeaderCell(new Cell().add(new Paragraph("Hora Entrada")));
            table.addHeaderCell(new Cell().add(new Paragraph("Quantidade Entrada")));
            table.addHeaderCell(new Cell().add(new Paragraph("Status Entrada")));
            table.addHeaderCell(new Cell().add(new Paragraph("Observação Entrada")));

            // Dados
            for (Input input : inputs) {
                table.addCell(new Cell().add(new Paragraph(input.getId().toString())));
                table.addCell(new Cell().add(new Paragraph(input.getDataEntrada())));
                table.addCell(new Cell().add(new Paragraph(input.getHoraEntrada())));
                table.addCell(new Cell().add(new Paragraph(input.getQuantEntrada().toString())));
                table.addCell(new Cell().add(new Paragraph(input.getStatus())));
                table.addCell(new Cell().add(new Paragraph(input.getObsEntrada())));
            }

            document.add(table);
            document.close();

        } catch (Exception e) {
            logger.error("Erro ao gerar PDF", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}

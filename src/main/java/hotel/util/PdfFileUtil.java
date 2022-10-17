package hotel.util;

import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.Cell;
import be.quodlibet.boxable.Row;
import hotel.model.Booking;
import java.awt.Color;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

public class PdfFileUtil implements Constant {
    private static final String FONT_FILE_PATH = "/fonts/Roboto-Regular.ttf";
    private static final float FONT_SIZE = 12f;
    private static final float LEADING = 15f;

    public void createPdfFile(HttpServletRequest req, Booking booking, String fileName)
            throws IOException {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(SESSION_ATTRIBUTE_LANGUAGE,
                Locale.forLanguageTag(
                        (String) req.getSession().getAttribute(SESSION_ATTRIBUTE_LANGUAGE)));
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        PDFont font = PDType0Font.load(document,
                getClass().getResourceAsStream(FONT_FILE_PATH));
        formationPdfFile(document, page, font, booking, resourceBundle);
        createTable(document, page, font);
        document.save(req.getContextPath() + fileName);
        document.close();
    }

    private void formationPdfFile(PDDocument document, PDPage page, PDFont font,
                                  Booking booking, ResourceBundle resourceBundle)
            throws IOException {
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.setFont(font, FONT_SIZE);
        contentStream.beginText();
        contentStream.setLeading(LEADING);
        float width = page.getMediaBox().getWidth();
        float positionX = 0;
        StringBuilder title = new StringBuilder();
        title.append(resourceBundle.getString("invoice.name"))
                .append(" ")
                .append(booking.getId())
                .append(" ")
                .append(resourceBundle.getString("invoice.from"))
                .append(" ")
                .append(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        float titleWidth = font.getStringWidth(title.toString()) / 1000 * FONT_SIZE;
        positionX = (width - titleWidth) / 2 - positionX;
        contentStream.newLineAtOffset(positionX, 725);
        contentStream.showText(title.toString());
        contentStream.endText();
        contentStream.setNonStrokingColor(Color.DARK_GRAY);
        contentStream.addRect(50, 710, width - 100, 1);
        contentStream.fill();
        contentStream.close();
    }

    private void createTable(PDDocument document, PDPage page, PDFont font) throws IOException {
        float margin = 50;
        float startNewPageY = page.getMediaBox().getHeight() - (2 * margin);
        float tableWidth = page.getMediaBox().getWidth() - (2 * margin);
        boolean drawContent = true;
        float bottomMargin = 70;
        float startY = 600;

        BaseTable table = new BaseTable(startY, startNewPageY,
                bottomMargin, tableWidth, margin, document, page, true, drawContent);

        Row<PDPage> headerRow = table.createRow(15f);
        Cell<PDPage> cell = headerRow.createCell(100, "заголовок");
        cell.setFont(font);
        cell.setFontSize(10);
        table.addHeaderRow(headerRow);

        Row<PDPage> row = table.createRow(15f);
        cell = row.createCell(70, "что то ");
        cell.setFont(font);
        cell.setFontSize(10);
        cell = row.createCell(15, "еще что то");
        cell.setFont(font);
        cell.setFontSize(10);
        cell = row.createCell(15, "что то другое");
        cell.setFont(font);
        cell.setFontSize(10);

        table.draw();
    }
}

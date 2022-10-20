package hotel.util;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import hotel.dto.BookingDto;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ResourceBundle;

public class PdfFileUtil implements Constant {
    private static final String FONT_FILE_PATH = "/fonts/Roboto-Regular.ttf";
    private static final String STAMP_FILE_PATH = "/img/stamp.png";
    private static final String ENCODING_1251 = "Cp1251";
    private static final String PDF_INVOICE_TITLE = "pdf.invoice.title";
    private static final String PDF_INVOICE_SUBJECT = "pdf.invoice.subject";
    private static final String PDF_INVOICE_COMPANY_NAME = "company.name";
    private static final String PDF_INVOICE_COMPANY_ADDRESS = "company.address";
    private static final String PDF_INVOICE_COMPANY_ADDRESS_TITLE = "company.address.title";
    private static final String PDF_INVOICE_COMPANY_BANK = "company.bank";
    private static final String PDF_INVOICE_COMPANY_BANK_TITLE = "company.bank.title";
    private static final String PDF_INVOICE_COMPANY_BANK_ACCOUNT = "company.bank.account";
    private static final String PDF_INVOICE_COMPANY_PHONE = "company.phone";
    private static final String PDF_INVOICE_COMPANY_PHONE_TITLE = "company.phone.title";
    private static final String PDF_INVOICE_COMPANY_EMAIL = "company.email";
    private static final String PDF_INVOICE_COMPANY_EMAIL_TITLE = "company.email.title";
    private static final String COLUMN_NUMBER = "booking.all.room.number";
    private static final String COLUMN_CLASS = "booking.all.room.class";
    private static final String COLUMN_FROM_DATE = "booking.all.checkin";
    private static final String COLUMN_TO_DATE = "booking.all.checkout";
    private static final String COLUMN_TOTAL_PRICE = "booking.all.total.price";
    private PdfFont fontTitle;
    private PdfFont fontText;

    public void createPdfFile(ResourceBundle resourceBundle, String pathName,
                              BookingDto bookingDto) {
        try {
            PdfDocument pdfDocument = new PdfDocument(
                    new PdfWriter(new FileOutputStream(pathName)));
            addMetaData(pdfDocument, resourceBundle);
            Document document = new Document(pdfDocument);
            fontTitle = PdfFontFactory.createFont(FONT_FILE_PATH, ENCODING_1251);
            fontText = PdfFontFactory.createFont(FONT_FILE_PATH, ENCODING_1251);
            addHeader(document, resourceBundle, bookingDto);
            addTable(document, resourceBundle, bookingDto);
            document.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addTable(Document document, ResourceBundle resourceBundle, BookingDto bookingDto) {
        Table table = new Table(new float[]{35, 15, 15, 15, 20}, true);
        table.addCell(new Cell()
                .add(new Paragraph(resourceBundle.getString(COLUMN_NUMBER))
                        .setFont(fontTitle)
                        .setFontSize(10f)
                        .setTextAlignment(TextAlignment.CENTER)));
        table.addCell(new Cell()
                .add(new Paragraph(resourceBundle.getString(COLUMN_CLASS))
                        .setFont(fontTitle)
                        .setFontSize(10f)
                        .setTextAlignment(TextAlignment.CENTER)));
        table.addCell(new Cell()
                .add(new Paragraph(resourceBundle.getString(COLUMN_FROM_DATE))
                        .setFont(fontTitle)
                        .setFontSize(10f)
                        .setTextAlignment(TextAlignment.CENTER)));
        table.addCell(new Cell()
                .add(new Paragraph(resourceBundle.getString(COLUMN_TO_DATE))
                        .setFont(fontTitle)
                        .setFontSize(10f)
                        .setTextAlignment(TextAlignment.CENTER)));
        table.addCell(new Cell()
                .add(new Paragraph(resourceBundle.getString(COLUMN_TOTAL_PRICE))
                        .setFont(fontTitle)
                        .setFontSize(10f)
                        .setTextAlignment(TextAlignment.CENTER)));
        table.startNewRow();
        table.addCell(new Cell()
                .add(new Paragraph(bookingDto.getNumber())
                        .setFont(fontTitle).setFontSize(10f)));
        table.addCell(new Cell()
                .add(new Paragraph(bookingDto.getApartmentClassName())
                        .setFont(fontTitle).setFontSize(10f)
                        .setTextAlignment(TextAlignment.CENTER)));
        table.addCell(new Cell()
                .add(new Paragraph(String.valueOf(bookingDto.getCheckin()))
                        .setFont(fontTitle).setFontSize(10f)
                        .setTextAlignment(TextAlignment.CENTER)));
        table.addCell(new Cell()
                .add(new Paragraph(String.valueOf(bookingDto.getCheckout()))
                        .setFont(fontTitle).setFontSize(10f)
                        .setTextAlignment(TextAlignment.CENTER)));
        table.addCell(new Cell()
                .add(new Paragraph(String.valueOf(bookingDto.getTotal()))
                        .setFont(fontTitle).setFontSize(10f)
                        .setTextAlignment(TextAlignment.RIGHT)));
        document.add(table);
        table.complete();
    }

    private void addHeader(Document document, ResourceBundle resourceBundle,
                           BookingDto bookingDto) {
        Text title = new Text(resourceBundle.getString(PDF_INVOICE_TITLE) + " "
                + bookingDto.getId() + "-" + bookingDto.getDate()).setFont(fontTitle);

        Paragraph titleParagraph = new Paragraph().add(title)
                .setFontSize(16f)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(titleParagraph);

        Text companyName = new Text(resourceBundle.getString(PDF_INVOICE_COMPANY_NAME));
        Text address = new Text(resourceBundle.getString(PDF_INVOICE_COMPANY_ADDRESS_TITLE)
                + resourceBundle.getString(PDF_INVOICE_COMPANY_ADDRESS));
        Text bank = new Text(resourceBundle.getString(PDF_INVOICE_COMPANY_BANK_TITLE)
                + resourceBundle.getString(PDF_INVOICE_COMPANY_BANK) + ", "
                + resourceBundle.getString(PDF_INVOICE_COMPANY_BANK_ACCOUNT));
        Text contactInformation = new Text(resourceBundle.getString(PDF_INVOICE_COMPANY_PHONE_TITLE)
                + resourceBundle.getString(PDF_INVOICE_COMPANY_PHONE) + ", "
                + resourceBundle.getString(PDF_INVOICE_COMPANY_EMAIL_TITLE)
                + resourceBundle.getString(PDF_INVOICE_COMPANY_EMAIL));
        Paragraph companyDetailsParagraph = new Paragraph().setFont(fontText).setFontSize(9f);
        companyDetailsParagraph
                .add(companyName)
                .add(new Text("\n"))
                .add(address)
                .add(new Text("\n"))
                .add(bank)
                .add(new Text("\n"))
                .add(contactInformation)
                .add(new Text("\n"));
        document.add(companyDetailsParagraph);
    }

    private void addMetaData(PdfDocument document, ResourceBundle resourceBundle) {
        PdfDocumentInfo pdfDocumentInfo = document.getDocumentInfo();
        pdfDocumentInfo.setTitle(resourceBundle.getString(PDF_INVOICE_TITLE));
        pdfDocumentInfo.setSubject(resourceBundle.getString(PDF_INVOICE_SUBJECT));
        pdfDocumentInfo.setAuthor(resourceBundle.getString(PDF_INVOICE_COMPANY_NAME));
        pdfDocumentInfo.setCreator(resourceBundle.getString(PDF_INVOICE_COMPANY_NAME));
    }
}

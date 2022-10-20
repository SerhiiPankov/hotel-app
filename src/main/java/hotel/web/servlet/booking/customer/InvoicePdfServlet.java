package hotel.web.servlet.booking.customer;

import static hotel.util.Constant.MAPPING_DOWNLOAD;

import hotel.dto.BookingDto;
import hotel.exception.DataProcessingException;
import hotel.exception.WrongDataException;
import hotel.lib.Injector;
import hotel.service.BookingService;
import hotel.service.UserService;
import hotel.util.Constant;
import hotel.util.EmailUtil;
import hotel.util.PdfFileUtil;
import hotel.validation.Validator;
import jakarta.mail.MessagingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet(MAPPING_DOWNLOAD)
public class InvoicePdfServlet extends HttpServlet implements Constant {
    private static final Logger logger = LogManager.getLogger(InvoicePdfServlet.class);
    private static final Injector injector =
            Injector.getInstance(MAIN_PACKAGE_NAME);
    private final BookingService bookingService = (BookingService) injector
            .getInstance(BookingService.class);
    private final UserService userService = (UserService) injector
            .getInstance(UserService.class);

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        long userId = (long) req.getSession().getAttribute(SESSION_ATTRIBUTE_USER_ID);
        long bookingId;
        try {
            bookingId = Long.parseLong(Validator.validate(req.getParameter(PARAMETER_BOOKING_ID)));
        } catch (WrongDataException e) {
            logger.warn("Referencing a page " + MAPPING_DOWNLOAD
                    + " without parameters" + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_ERROR_PAGE + "?"
                    + PARAMETER_MESSAGE + "=" + NO_PARAMETER_ERROR_MSG);
            return;
        }
        long customerId;
        BookingDto bookingDto;
        try {
            customerId = bookingService.getCustomerIdByBookingId(bookingId);
            if (userId != customerId) {
                logger.warn("Referencing a page " + MAPPING_DOWNLOAD
                                + " with wrong parameters");
                resp.sendRedirect(req.getContextPath() + MAPPING_ERROR_PAGE + "?"
                        + PARAMETER_MESSAGE + "=" + WRONG_PARAMETER_ERROR_MSG);
                return;
            }
            bookingDto = bookingService.getBookingDtoById(bookingId);
        } catch (DataProcessingException e) {
            logger.warn("Can not get booking with id " + bookingId);
            resp.sendRedirect(req.getContextPath() + MAPPING_ERROR_PAGE + "?"
                    + PARAMETER_MESSAGE + "=" + ACCESS_ERROR_MSG);
            return;
        }
        String fileName = bookingId + "-" + bookingDto.getDate() + FILE_NAME;
        resp.setContentType(CONTENT_TYPE_PDF);
        resp.setHeader(HEADER_CONTENT_DISPOSITION, HEADER_ATTACHMENT_FILENAME + fileName);
        try {
            String pathName = req.getContextPath() + fileName;
            File file = new File(pathName);
            PdfFileUtil pdfFileUtil = new PdfFileUtil();
            ResourceBundle resourceBundle = ResourceBundle.getBundle(SESSION_ATTRIBUTE_LANGUAGE,
                    Locale.forLanguageTag(
                            (String) req.getSession().getAttribute(SESSION_ATTRIBUTE_LANGUAGE)));
            pdfFileUtil.createPdfFile(resourceBundle, pathName, bookingDto);
            sendFile(resp, file);
            EmailUtil.sendInvoiceEmail(file, req,userService.get(userId));
            file.delete();
            logger.info("Invoice was loaded: " + fileName);
        } catch (IOException | MessagingException
                 | DataProcessingException | WrongDataException e) {
            logger.warn("Load invoice failed " + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_ERROR_PAGE + "?"
                    + PARAMETER_MESSAGE + "=" + ACCESS_ERROR_MSG);
        }
    }

    private void sendFile(HttpServletResponse resp, File file) throws IOException {
        OutputStream out = resp.getOutputStream();
        FileInputStream in = new FileInputStream(file);
        byte[] buffer = new byte[4096];
        int length;
        while ((length = in.read(buffer)) > 0) {
            out.write(buffer, 0, length);
        }
        in.close();
        out.flush();
    }
}

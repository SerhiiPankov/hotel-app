package hotel.web.servlet.booking.customer;

import static hotel.util.Constant.MAPPING_DOWNLOAD;

import hotel.exception.DataProcessingException;
import hotel.exception.WrongDataException;
import hotel.lib.Injector;
import hotel.model.Booking;
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
        Booking booking;
        try {
            booking = bookingService.getById(bookingId);
            if (userId != booking.getCustomerId()) {
                logger.warn("Referencing a page " + MAPPING_DOWNLOAD
                                + " with wrong parameters");
                resp.sendRedirect(req.getContextPath() + MAPPING_ERROR_PAGE + "?"
                        + PARAMETER_MESSAGE + "=" + WRONG_PARAMETER_ERROR_MSG);
                return;
            }
        } catch (DataProcessingException e) {
            logger.warn("Can not get booking with id " + bookingId);
            resp.sendRedirect(req.getContextPath() + MAPPING_ERROR_PAGE + "?"
                    + PARAMETER_MESSAGE + "=" + ACCESS_ERROR_MSG);
            return;
        }
        String fileName = bookingId + "-" + booking.getDate() + FILE_NAME;
        resp.setContentType(CONTENT_TYPE_PDF);
        resp.setHeader(HEADER_CONTENT_DISPOSITION, HEADER_ATTACHMENT_FILENAME + fileName);
        try {
            File file = new File(req.getContextPath() + fileName);
            PdfFileUtil pdfFileUtil = new PdfFileUtil();
            pdfFileUtil.createPdfFile(req, booking, fileName);
            sendFile(resp, file);
            EmailUtil.sendInvoiceEmail(file, req, booking,userService.get(userId));
            file.delete();
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

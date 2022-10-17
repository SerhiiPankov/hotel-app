package hotel.web.servlet.booking.customer;

import static hotel.util.Constant.MAPPING_BOOKING_ACCEPT;

import hotel.exception.DataProcessingException;
import hotel.exception.WrongDataException;
import hotel.lib.Injector;
import hotel.model.Booking;
import hotel.model.enums.BookingStatus;
import hotel.model.enums.PaymentStatus;
import hotel.service.BookingService;
import hotel.util.Constant;
import hotel.validation.Validator;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet(MAPPING_BOOKING_ACCEPT)
public class AcceptServlet extends HttpServlet implements Constant {
    private static final Logger logger = LogManager.getLogger(AcceptServlet.class);
    private static final Injector injector = Injector.getInstance(MAIN_PACKAGE_NAME);
    private final BookingService bookingService = (BookingService) injector
            .getInstance(BookingService.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        long bookingId;
        try {
            bookingId = Long.parseLong(Validator.validate(req.getParameter(PARAMETER_BOOKING_ID)));
        } catch (WrongDataException e) {
            logger.warn("Referencing a page " + MAPPING_BOOKING_ACCEPT
                    + " without parameter" + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_ERROR_PAGE + "?"
                    + PARAMETER_MESSAGE + "=" + NO_PARAMETER_ERROR_MSG);
            return;
        }
        try {
            Booking booking = bookingService.getById(bookingId);
            long userId = (long) req.getSession().getAttribute(SESSION_ATTRIBUTE_USER_ID);
            if (userId != booking.getCustomerId()) {
                logger.warn("Referencing a page " + MAPPING_BOOKING_ACCEPT
                        + "with wrong parameters");
                resp.sendRedirect(req.getContextPath() + MAPPING_ERROR_PAGE + "?"
                        + PARAMETER_MESSAGE + "=" + WRONG_PARAMETER_ERROR_MSG);
            }
            bookingService.updateReserve(booking, PaymentStatus.WAIT, BookingStatus.ACCEPT);
            logger.info("For booking with id " + bookingId + " status 'wait' has been set.");
            resp.sendRedirect(req.getContextPath() + MAPPING_BOOKING_ALL);
        } catch (DataProcessingException e) {
            logger.warn("Accepting an offer for booking with id " + bookingId + " failed.");
            resp.sendRedirect(req.getContextPath() + MAPPING_BOOKING_ALL + "?"
                    + PARAMETER_MESSAGE + "=" + ACCESS_ERROR_MSG);
        }
    }
}

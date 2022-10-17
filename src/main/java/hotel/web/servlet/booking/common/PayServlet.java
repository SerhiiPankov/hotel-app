package hotel.web.servlet.booking.common;

import static hotel.util.Constant.MAPPING_BOOKING_PAY;

import hotel.exception.DataProcessingException;
import hotel.exception.WrongDataException;
import hotel.lib.Injector;
import hotel.model.Booking;
import hotel.model.User;
import hotel.model.enums.BookingStatus;
import hotel.model.enums.PaymentStatus;
import hotel.model.enums.Role;
import hotel.service.BookingService;
import hotel.service.UserService;
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

@WebServlet(MAPPING_BOOKING_PAY)
public class PayServlet extends HttpServlet implements Constant {
    private static final Logger logger = LogManager.getLogger(PayServlet.class);
    private static final Injector injector =
            Injector.getInstance(MAIN_PACKAGE_NAME);
    private final BookingService bookingService = (BookingService) injector
            .getInstance(BookingService.class);
    private final UserService userService = (UserService) injector
            .getInstance(UserService.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        logger.info("GET request " + MAPPING_BOOKING_PAY);
        long userId = (long) req.getSession().getAttribute(SESSION_ATTRIBUTE_USER_ID);
        long bookingId;
        try {
            bookingId = Long.parseLong(Validator.validate(req.getParameter(PARAMETER_BOOKING_ID)));
        } catch (WrongDataException e) {
            logger.warn("Referencing a page " + MAPPING_BOOKING_PAY
                    + "without parameters");
            resp.sendRedirect(req.getContextPath() + MAPPING_ERROR_PAGE + "?"
                    + PARAMETER_MESSAGE + "=" + NO_PARAMETER_ERROR_MSG);
            return;
        }
        try {
            User user = userService.get(userId);
            logger.info("User by id " + userId + " was successfully loaded from the database");
            Booking booking = bookingService.getById(bookingId);
            logger.info("Booking by id " + bookingId
                    + " was successfully loaded from the database");
            setPaid(user, booking);
            logger.info("Booking with id " + bookingId + " paid successfully");
            resp.sendRedirect(req.getContextPath() + MAPPING_BOOKING_ALL);
        } catch (DataProcessingException | WrongDataException e) {
            logger.warn("Setting status PAID for booking with id " + bookingId
                    + "  by user with id " + userId + " failed" + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_ERROR_PAGE + "?"
                    + PARAMETER_MESSAGE + "=" + ACCESS_ERROR_MSG);
        }
    }

    private void setPaid(User user, Booking booking)
            throws DataProcessingException, WrongDataException {
        if (user.getRole().equals(Role.MANAGER)
                || user.getRole().equals(Role.ADMIN)) {
            bookingService.updateReserve(booking,
                    PaymentStatus.PAID, BookingStatus.BUSY);
            return;
        }
        if (user.getRole().equals(Role.CUSTOMER)
                && booking.getCustomerId() == user.getId()) {
            bookingService.updateReserve(booking,
                    PaymentStatus.PAID, BookingStatus.BUSY);
            return;
        }
        throw new WrongDataException("Wrong access data");
    }
}

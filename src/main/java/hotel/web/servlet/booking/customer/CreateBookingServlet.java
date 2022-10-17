package hotel.web.servlet.booking.customer;

import static hotel.util.Constant.MAPPING_BOOKING_CREATE;

import hotel.exception.DataProcessingException;
import hotel.exception.WrongDataException;
import hotel.lib.Injector;
import hotel.model.Booking;
import hotel.model.enums.PaymentStatus;
import hotel.service.BookingService;
import hotel.service.ScheduleService;
import hotel.service.UserService;
import hotel.util.Constant;
import hotel.validation.Validator;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet(MAPPING_BOOKING_CREATE)
public class CreateBookingServlet extends HttpServlet implements Constant {
    static final long PARAMETER_FILTER_PAYMENT_STATUS_WAIT = 1L;
    private static final Logger logger = LogManager.getLogger(CreateBookingServlet.class);
    private static final Injector injector = Injector.getInstance(MAIN_PACKAGE_NAME);
    private final BookingService bookingService = (BookingService) injector
            .getInstance(BookingService.class);
    private final UserService userService = (UserService) injector
            .getInstance(UserService.class);
    private final ScheduleService scheduleService = (ScheduleService) injector
            .getInstance(ScheduleService.class);

    @Override
    protected synchronized void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        logger.info("POST request " + MAPPING_BOOKING_CREATE);
        LocalDate fromDate =
                (LocalDate) req.getSession().getAttribute(SESSION_ATTRIBUTE_FROM_DATE_SELECT);
        LocalDate toDate =
                (LocalDate) req.getSession().getAttribute(SESSION_ATTRIBUTE_TO_DATE_SELECT);
        int numberOfGuests =
                (int) req.getSession().getAttribute(SESSION_ATTRIBUTE_NUMBER_OF_GUESTS_SELECT);
        long customerId = (long) req.getSession().getAttribute(SESSION_ATTRIBUTE_USER_ID);
        long hotelRoomId;
        try {
            hotelRoomId = Long.parseLong(
                    Validator.validate(req.getParameter(PARAMETER_HOTEL_ROOM_ID)));
        } catch (WrongDataException e) {
            logger.warn("Referencing a page " + MAPPING_BOOKING_SELECT
                    + " without parameter" + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_ERROR_PAGE + "?"
                    + PARAMETER_MESSAGE + "=" + NO_PARAMETER_ERROR_MSG);
            return;
        }
        try {
            if (checkBusyInterval(fromDate, toDate, hotelRoomId)) {
                logger.info("Apartment booking with id " + hotelRoomId + " was unsuccessful, from "
                        + fromDate + " to " + toDate + " was busy.");
                resp.sendRedirect(req.getContextPath() + MAPPING_BOOKING_SELECT + "?"
                        + PARAMETER_MESSAGE + "=" + BUSY_HOTEL_ROOM_ERROR_MSG);
                return;
            }
            Booking booking = parseBooking(
                    hotelRoomId, fromDate, toDate, numberOfGuests, customerId);
            bookingService.createBooking(booking);
            logger.info("Booking apartment with id " + hotelRoomId + " was successful, from "
                    + fromDate + " to " + toDate);
            cleanSessionAttribute(req);
            req.getSession().setAttribute(SESSION_ATTRIBUTE_FILTER_BOOKING,
                    PARAMETER_FILTER_PAYMENT_STATUS_WAIT);
            resp.sendRedirect(req.getContextPath() + MAPPING_BOOKING_ALL);
        } catch (DataProcessingException | WrongDataException e) {
            logger.warn("Booking apartment with id " + hotelRoomId + " was unsuccessful " + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_BOOKING_SELECT + "?"
                    + PARAMETER_MESSAGE + "=" + ACCESS_ERROR_MSG);
        }
    }

    private void cleanSessionAttribute(HttpServletRequest req) {
        req.getSession().removeAttribute(SESSION_ATTRIBUTE_FROM_DATE_SELECT);
        req.getSession().removeAttribute(SESSION_ATTRIBUTE_TO_DATE_SELECT);
        req.getSession().removeAttribute(SESSION_ATTRIBUTE_NUMBER_OF_GUESTS_SELECT);
    }

    private Booking parseBooking(long hotelRoomId, LocalDate fromDate, LocalDate toDate,
                                 int numberOfGuests, long customerId)
            throws DataProcessingException, WrongDataException {
        long managerId = userService.findByEmail(ADMIN_EMAIL).getId();
        BigDecimal totalPrice = bookingService.getTotalPrice(hotelRoomId,
                fromDate, toDate);
        Booking booking = new Booking();
        booking.setDate(LocalDate.now());
        booking.setHotelRoomId(hotelRoomId);
        booking.setCheckin(fromDate);
        booking.setCheckout(toDate);
        booking.setNumberOfGuests(numberOfGuests);
        booking.setTotal(totalPrice);
        booking.setManagerId(managerId);
        booking.setCustomerId(customerId);
        booking.setPaymentStatus(PaymentStatus.WAIT);
        return booking;
    }

    private boolean checkBusyInterval(LocalDate fromDate, LocalDate toDate, long hotelRoomId)
            throws DataProcessingException {
        int interval = (int) ChronoUnit.DAYS.between(fromDate,
                toDate.plusDays(1L));
        int freeDays = scheduleService.countFreeDaysByInterval(
                hotelRoomId, fromDate, toDate);
        return freeDays < interval;
    }
}

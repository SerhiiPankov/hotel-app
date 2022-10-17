package hotel.web.servlet.booking.manager;

import static hotel.util.Constant.MAPPING_BOOKING_SELECTION;

import hotel.dto.HotelRoomDto;
import hotel.exception.DataProcessingException;
import hotel.exception.WrongDataException;
import hotel.lib.Injector;
import hotel.model.Booking;
import hotel.model.HotelRoomClass;
import hotel.model.Request;
import hotel.model.enums.BookingStatus;
import hotel.model.enums.PaymentStatus;
import hotel.service.BookingService;
import hotel.service.HotelRoomClassService;
import hotel.service.HotelRoomService;
import hotel.service.RequestService;
import hotel.service.ScheduleService;
import hotel.service.UserService;
import hotel.util.Constant;
import hotel.validation.Validator;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet(MAPPING_BOOKING_SELECTION)
public class SelectionBookingServlet extends HttpServlet implements Constant {
    private static final Logger logger = LogManager.getLogger(SelectionBookingServlet.class);
    private static final Injector injector = Injector.getInstance(MAIN_PACKAGE_NAME);
    private final HotelRoomService hotelRoomService = (HotelRoomService) injector
            .getInstance(HotelRoomService.class);
    private final HotelRoomClassService hotelRoomClassService = (HotelRoomClassService) injector
            .getInstance(HotelRoomClassService.class);
    private final RequestService requestService = (RequestService) injector
            .getInstance(RequestService.class);
    private final BookingService bookingService = (BookingService) injector
            .getInstance(BookingService.class);
    private final ScheduleService scheduleService = (ScheduleService) injector
            .getInstance(ScheduleService.class);
    private final UserService userService = (UserService) injector
            .getInstance(UserService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        long requestId;
        try {
            requestId = Long.parseLong(
                    Validator.validate(req.getParameter(PARAMETER_REQUEST_ID)));
            req.getSession().setAttribute(SESSION_ATTRIBUTE_REQUEST_ID, requestId);
        } catch (WrongDataException e) {
            try {
                requestId =
                        Validator.validate(
                                (Long) req.getSession().getAttribute(
                                        SESSION_ATTRIBUTE_REQUEST_ID));
            } catch (WrongDataException ex) {
                logger.warn("Referencing a page " + MAPPING_BOOKING_SELECTION
                        + " without parameter" + e);
                resp.sendRedirect(req.getContextPath() + MAPPING_ERROR_PAGE + "?"
                        + PARAMETER_MESSAGE + "=" + NO_PARAMETER_ERROR_MSG);
                return;
            }
        }
        try {
            Request request = requestService.get(requestId);
            List<HotelRoomDto> hotelRooms =
                    hotelRoomService.getAllHotelRoomDtoByRequest(
                            request,BookingStatus.FREE);
            HotelRoomClass hotelRoomClass =
                    hotelRoomClassService.get(request.getHotelRoomClassId());
            if (hotelRooms.size() == 0) {
                req.setAttribute(ATTRIBUTE_MESSAGE, ATTRIBUTE_NO_AVAILABLE_ROOM);
            }
            req.setAttribute(ATTRIBUTE_HOTEL_ROOM_CLASS_NAME, hotelRoomClass.getName());
            req.setAttribute(ATTRIBUTE_REQUEST, request);
            req.setAttribute(ATTRIBUTE_HOTEL_ROOMS, hotelRooms);
            req.getRequestDispatcher(JSP_BOOKINGS_SELECTED).forward(req, resp);
        } catch (DataProcessingException | WrongDataException e) {
            logger.warn("The selection of apartments according "
                    + "to the specified parameters was unsuccessful" + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_BOOKING_REQUEST_ALL + "?"
                    + PARAMETER_MESSAGE + "=" + SELECTION_HOTEL_ROOM_ERROR_MSG);
        }
    }

    @Override
    protected synchronized void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String hotelRoomIdParameter;
        long hotelRoomId;
        try {
            hotelRoomIdParameter =
                    Validator.validate(req.getParameter(PARAMETER_HOTEL_ROOM_ID));
            hotelRoomId = Long.parseLong(hotelRoomIdParameter);
        } catch (WrongDataException e) {
            logger.warn("Referencing a page " + MAPPING_BOOKING_SELECTION
                    + " without parameter " + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_ERROR_PAGE + "?"
                    + PARAMETER_MESSAGE + "=" + NO_PARAMETER_ERROR_MSG);
            return;
        }
        long requestId =
                (Long) req.getSession().getAttribute(SESSION_ATTRIBUTE_REQUEST_ID);
        try {
            Booking booking = parseBooking(req, requestId, hotelRoomId);
            LocalDate fromDate = booking.getCheckin();
            LocalDate toDate = booking.getCheckout();
            if (checkBusyInterval(fromDate, toDate, hotelRoomId)) {
                logger.info("Apartment booking with id " + hotelRoomId + " was unsuccessful, from "
                        + fromDate + " to " + toDate + " was busy.");
                resp.sendRedirect(req.getContextPath() + MAPPING_BOOKING_REQUEST_ALL + "?"
                        + PARAMETER_MESSAGE + "=" + BUSY_HOTEL_ROOM_ERROR_MSG);
                return;
            }
            bookingService.createReserve(booking, requestId);
            logger.info("Booking apartment with id " + hotelRoomId + " was successful, from "
                    + fromDate + " to " + toDate);
            cleanSessionAttribute(req);
            resp.sendRedirect(req.getContextPath() + MAPPING_BOOKING_ALL);
        } catch (DataProcessingException | WrongDataException e) {
            logger.warn("Booking apartment with id " + hotelRoomId + " was unsuccessful " + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_BOOKING_REQUEST_ALL + "?"
                    + PARAMETER_MESSAGE + "=" + ACCESS_ERROR_MSG);
        }
    }

    private Booking parseBooking(HttpServletRequest req, long requestId, long hotelRoomId)
            throws DataProcessingException, WrongDataException {
        Request request = requestService.get(requestId);
        BigDecimal totalPrice = bookingService.getTotalPrice(hotelRoomId,
                request.getCheckIn(), request.getCheckOut());
        long managerId = userService.get((Long) req.getSession().getAttribute(
                SESSION_ATTRIBUTE_USER_ID)).getId();
        Booking booking = new Booking();
        booking.setDate(LocalDate.now());
        booking.setHotelRoomId(hotelRoomId);
        booking.setCheckin(request.getCheckIn());
        booking.setCheckout(request.getCheckOut());
        booking.setNumberOfGuests(request.getNumberOfGuests());
        booking.setTotal(totalPrice);
        booking.setManagerId(managerId);
        booking.setCustomerId(request.getCustomerId());
        booking.setPaymentStatus(PaymentStatus.PROPOSAL);
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

    private void cleanSessionAttribute(HttpServletRequest req) {
        req.getSession().removeAttribute(SESSION_ATTRIBUTE_HOTEL_ROOM_ID);
        req.getSession().removeAttribute(SESSION_ATTRIBUTE_REQUEST_ID);
    }
}

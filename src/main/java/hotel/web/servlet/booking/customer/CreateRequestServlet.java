package hotel.web.servlet.booking.customer;

import static hotel.util.Constant.MAPPING_BOOKING_REQUEST_CREATE;

import hotel.exception.DataProcessingException;
import hotel.exception.WrongDataException;
import hotel.lib.Injector;
import hotel.model.HotelRoomClass;
import hotel.model.Request;
import hotel.service.HotelRoomClassService;
import hotel.service.RequestService;
import hotel.util.Constant;
import hotel.validation.Validator;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet(MAPPING_BOOKING_REQUEST_CREATE)
public class CreateRequestServlet extends HttpServlet implements Constant {
    private static final String SPLIT_REGEX = " - ";
    private static final int INDEX_FROM_DATE = 0;
    private static final int INDEX_TO_DATE = 1;
    private static final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern(PATTERN_DATE);
    private static final Logger logger = LogManager.getLogger(CreateRequestServlet.class);
    private static final Injector injector = Injector.getInstance(MAIN_PACKAGE_NAME);
    private final HotelRoomClassService hotelRoomClassService = (HotelRoomClassService) injector
            .getInstance(HotelRoomClassService.class);
    private final RequestService requestService = (RequestService) injector
            .getInstance(RequestService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        logger.info("GET request " + MAPPING_BOOKING_REQUEST_CREATE);
        try {
            List<HotelRoomClass> hotelRoomClasses = hotelRoomClassService.getAllWithoutDeleted();
            logger.info("The list of hotel room class was loaded successfully");
            req.setAttribute(ATTRIBUTE_HOTEL_ROOM_CLASSES, hotelRoomClasses);
            logger.info("Load jsp page " + JSP_BOOKINGS_REQUEST);
            req.getRequestDispatcher(JSP_BOOKINGS_REQUEST).forward(req, resp);
        } catch (DataProcessingException e) {
            logger.warn("The list of hotel room classes could not be loaded" + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_ERROR_PAGE + "?"
                    + PARAMETER_MESSAGE + "=" + ACCESS_ERROR_MSG);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        logger.info("POST request " + MAPPING_BOOKING_REQUEST_CREATE);
        Long customerId = (Long) req.getSession().getAttribute(SESSION_ATTRIBUTE_USER_ID);
        long hotelRoomClassId;
        int numberOfGuests;
        String dateParameter;
        try {
            hotelRoomClassId = Long.parseLong(
                    Validator.validate(req.getParameter(PARAMETER_HOTEL_ROOM_CLASS_ID)));
            numberOfGuests = Integer.parseInt(
                    Validator.validate(req.getParameter(PARAMETER_NUMBER_OF_GUESTS)));
            dateParameter = Validator.validate(req.getParameter(PARAMETER_DATE));
        } catch (WrongDataException e) {
            logger.warn("Referencing a page " + MAPPING_BOOKING_REQUEST_CREATE
                    + " without parameter" + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_ERROR_PAGE + "?"
                    + PARAMETER_MESSAGE + "=" + NO_PARAMETER_ERROR_MSG);
            return;
        }
        String[] date = dateParameter.split(SPLIT_REGEX);
        LocalDate checkIn = LocalDate.parse(date[INDEX_FROM_DATE], dateTimeFormatter);
        LocalDate checkOut = LocalDate.parse(date[INDEX_TO_DATE], dateTimeFormatter);
        Request request = parseRequest(
                customerId, hotelRoomClassId, numberOfGuests, checkIn, checkOut);
        try {
            requestService.create(request);
            logger.info("Booking request for user with id " + customerId + " was created");
            resp.sendRedirect(req.getContextPath() + MAPPING_BOOKING_REQUEST_ALL);
        } catch (DataProcessingException e) {
            logger.warn("Create booking request from user with id " + customerId + " failed");
            resp.sendRedirect(req.getContextPath() + MAPPING_BOOKING_REQUEST_ALL + "?"
                    + PARAMETER_MESSAGE + "=" + CREATE_ERROR_MSG);
        }
    }

    private Request parseRequest(Long customerId, Long hotelRoomClassId,
                                               int numberOfGuests, LocalDate checkIn,
                                               LocalDate checkOut) {
        Request request = new Request();
        request.setDate(LocalDate.now());
        request.setCustomerId(customerId);
        request.setHotelRoomClassId(hotelRoomClassId);
        request.setNumberOfGuests(numberOfGuests);
        request.setCheckIn(checkIn);
        request.setCheckOut(checkOut);
        return request;
    }
}

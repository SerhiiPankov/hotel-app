package hotel.web.servlet.booking.customer;

import static hotel.util.Constant.MAPPING_BOOKING_SELECT;

import hotel.dto.list.SelectedHotelRoomsDto;
import hotel.exception.DataProcessingException;
import hotel.exception.WrongDataException;
import hotel.lib.Injector;
import hotel.model.enums.BookingStatus;
import hotel.service.HotelRoomService;
import hotel.util.Constant;
import hotel.util.PageUtil;
import hotel.validation.Validator;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet(MAPPING_BOOKING_SELECT)
public class SelectServlet extends HttpServlet implements Constant, PageUtil {
    private static final long ATTRIBUTE_SORT_ROOM_NUMBER_OF_GUESTS = 1L;
    private static final long ATTRIBUTE_SORT_ROOM_NUMBER_OF_GUESTS_DESC = 2L;
    private static final long ATTRIBUTE_SORT_ROOM_PRICE_ASC = 3L;
    private static final long ATTRIBUTE_SORT_ROOM_PRICE_DESC = 4L;
    private static final long ATTRIBUTE_SORT_ROOM_CLASS_ASC = 5L;
    private static final long ATTRIBUTE_SORT_ROOM_CLASS_DESC = 6L;
    private static final long PARAMETER_FILTER_PAYMENT_STATUS_WAIT = 1L;
    private static final Map<Long, String> sortParameters = new HashMap<>();
    private static final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern(PATTERN_DATE);
    private static final Logger logger =
            LogManager.getLogger(SelectServlet.class);
    private static final Injector injector = Injector.getInstance(MAIN_PACKAGE_NAME);
    private final HotelRoomService hotelRoomService = (HotelRoomService) injector
            .getInstance(HotelRoomService.class);

    static {
        sortParameters.put(ATTRIBUTE_SORT_ROOM_NUMBER_OF_GUESTS,
                COLUMN_NAME_NUMBER_OF_GUESTS);
        sortParameters.put(ATTRIBUTE_SORT_ROOM_NUMBER_OF_GUESTS_DESC,
                COLUMN_NAME_NUMBER_OF_GUESTS + " " + ORDER_DESC);
        sortParameters.put(ATTRIBUTE_SORT_ROOM_PRICE_ASC,
                COLUMN_NAME_TOTAL_PRICE + ", " + COLUMN_NAME_NUMBER_OF_GUESTS);
        sortParameters.put(ATTRIBUTE_SORT_ROOM_PRICE_DESC,
                COLUMN_NAME_TOTAL_PRICE + " " + ORDER_DESC + ", "
                        + COLUMN_NAME_NUMBER_OF_GUESTS);
        sortParameters.put(ATTRIBUTE_SORT_ROOM_CLASS_ASC,
                COLUMN_NAME_HOTEL_ROOM_CLASS_NAME + ", "
                        + COLUMN_NAME_NUMBER_OF_GUESTS);
        sortParameters.put(ATTRIBUTE_SORT_ROOM_CLASS_DESC,
                COLUMN_NAME_HOTEL_ROOM_CLASS_NAME + " " + ORDER_DESC + ", "
                        + COLUMN_NAME_NUMBER_OF_GUESTS);
        sortParameters.put(null, COLUMN_NAME_NUMBER_OF_GUESTS);
    }

    /**
     *
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws ServletException Signals a Servlet exception
     * @throws IOException Signals an I/O exception.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        LocalDate fromDate =
                (LocalDate) req.getSession().getAttribute(SESSION_ATTRIBUTE_FROM_DATE_SELECT);
        LocalDate toDate =
                (LocalDate) req.getSession().getAttribute(SESSION_ATTRIBUTE_TO_DATE_SELECT);
        Integer numberOfGuests =
                (Integer) req.getSession().getAttribute(SESSION_ATTRIBUTE_NUMBER_OF_GUESTS_SELECT);
        SelectedHotelRoomsDto roomsDto;
        if (fromDate != null || toDate != null || numberOfGuests != null) {
            try {
                int page = getPage(req);
                Long sort = (Long) req.getSession().getAttribute(SESSION_ATTRIBUTE_SORT_ROOM);
                roomsDto = hotelRoomService.getAllHotelRoomDtoByDateRangeAndBookingStatus(
                        fromDate, toDate, numberOfGuests,
                        BookingStatus.FREE, page, ROOM_RECORDS_PER_PAGE,
                        sortParameters.get(sort));
                setAttribute(req, roomsDto, page);
            } catch (DataProcessingException e) {
                logger.warn("Load list of booking failed " + e);
                cleanSessionAttribute(req);
                resp.sendRedirect(req.getContextPath() + MAPPING_BOOKING_SELECT + "?"
                        + PARAMETER_MESSAGE + "=" + ACCESS_ERROR_MSG);
                return;
            }
        }
        req.getRequestDispatcher(JSP_BOOKINGS_SELECT).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String dateRangeParameter;
        int numberOfGuests;
        try {
            dateRangeParameter = Validator.validate(req.getParameter(PARAMETER_DATE));
            numberOfGuests = Integer.parseInt(
                    Validator.validate(req.getParameter(PARAMETER_NUMBER_OF_GUESTS)));
        } catch (WrongDataException e) {
            logger.warn("Referencing a page " + MAPPING_BOOKING_SELECT_PARAMETER
                    + " without parameters" + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_ERROR_PAGE + "?"
                    + PARAMETER_MESSAGE + "=" + NO_PARAMETER_ERROR_MSG);
            return;
        }
        String[] dateRange = dateRangeParameter.split(SPLIT_REGEXP);
        LocalDate fromDate = LocalDate.parse(dateRange[INDEX_FROM_DATE], dateTimeFormatter);
        LocalDate toDate = LocalDate.parse(dateRange[INDEX_TO_DATE], dateTimeFormatter);
        req.getSession().setAttribute(ATTRIBUTE_FROM_DATE_SELECT, fromDate);
        req.getSession().setAttribute(ATTRIBUTE_TO_DATE_SELECT, toDate);
        req.getSession().setAttribute(ATTRIBUTE_NUMBER_OF_GUESTS_SELECT, numberOfGuests);
        req.getSession().setAttribute(SESSION_ATTRIBUTE_FILTER_BOOKING,
                PARAMETER_FILTER_PAYMENT_STATUS_WAIT);
        req.getSession().setAttribute(SESSION_ATTRIBUTE_SORT_ROOM,
                ATTRIBUTE_SORT_ROOM_NUMBER_OF_GUESTS);
        logger.info("Load parameters for selection hotel room was successful");
        resp.sendRedirect(req.getContextPath() + MAPPING_BOOKING_SELECT);
    }

    private void cleanSessionAttribute(HttpServletRequest req) {
        req.getSession().removeAttribute(SESSION_ATTRIBUTE_FROM_DATE_SELECT);
        req.getSession().removeAttribute(SESSION_ATTRIBUTE_TO_DATE_SELECT);
        req.getSession().removeAttribute(SESSION_ATTRIBUTE_NUMBER_OF_GUESTS_SELECT);
    }

    private void setAttribute(HttpServletRequest req, SelectedHotelRoomsDto roomsDto, int page) {
        int numberOfPages = (int)
                Math.ceil(roomsDto.getNumberOfRooms() * 1.0 / BOOKING_RECORDS_PER_PAGE);
        req.setAttribute(ATTRIBUTE_HOTEL_ROOMS, roomsDto.getRooms());
        req.setAttribute(ATTRIBUTE_ROOM_CURRENT_PAGE, page);
        req.setAttribute(ATTRIBUTE_ROOM_NUMBER_OF_PAGES, numberOfPages);
    }
}

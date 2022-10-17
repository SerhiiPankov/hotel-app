package hotel.web.servlet.booking.common;

import static hotel.util.Constant.MAPPING_BOOKING_ALL;

import hotel.dto.list.BookingsDto;
import hotel.exception.DataProcessingException;
import hotel.lib.Injector;
import hotel.model.enums.PaymentStatus;
import hotel.model.enums.Role;
import hotel.service.BookingService;
import hotel.util.Constant;
import hotel.util.PageUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet(MAPPING_BOOKING_ALL)
public class ManageBookingServlet extends HttpServlet implements Constant, PageUtil {
    private static final long PARAMETER_FILTER_PAYMENT_STATUS_WAIT = 1L;
    private static final long PARAMETER_FILTER_PAYMENT_STATUS_INVOICE = 2L;
    private static final long PARAMETER_FILTER_PAYMENT_STATUS_PROPOSAL = 3L;
    private static final long PARAMETER_FILTER_PAYMENT_STATUS_PAID = 4L;
    private static final long PARAMETER_FILTER_PAYMENT_STATUS_DELETE = 5L;
    private static final Map<Long, PaymentStatus> filterParameters = new HashMap<>();
    private static final Logger logger = LogManager.getLogger(ManageBookingServlet.class);
    private static final Injector injector =
            Injector.getInstance(MAIN_PACKAGE_NAME);
    private final BookingService bookingService = (BookingService) injector
            .getInstance(BookingService.class);

    static {
        filterParameters.put(PARAMETER_FILTER_PAYMENT_STATUS_WAIT, PaymentStatus.WAIT);
        filterParameters.put(PARAMETER_FILTER_PAYMENT_STATUS_INVOICE, PaymentStatus.INVOICE);
        filterParameters.put(PARAMETER_FILTER_PAYMENT_STATUS_PROPOSAL, PaymentStatus.PROPOSAL);
        filterParameters.put(PARAMETER_FILTER_PAYMENT_STATUS_PAID, PaymentStatus.PAID);
        filterParameters.put(PARAMETER_FILTER_PAYMENT_STATUS_DELETE, PaymentStatus.DELETE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        logger.info("GET request " + MAPPING_BOOKING_ALL);
        Role userRole = (Role) req.getSession().getAttribute(SESSION_ATTRIBUTE_USER_ROLE);
        int page = getPage(req);
        try {
            BookingsDto bookingsDto = getBookingsDto(req, userRole, page);
            logger.info("The filtered list of bookings was loaded successfully");
            setAttribute(req, bookingsDto, page);
            logger.info("Load jsp page " + JSP_BOOKINGS_ALL_BOOKING);
            req.getRequestDispatcher(JSP_BOOKINGS_ALL_BOOKING)
                    .forward(req, resp);
        } catch (DataProcessingException e) {
            logger.warn("Load list of booking failed" + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_ERROR_PAGE + "?"
                    + PARAMETER_MESSAGE + "=" + ACCESS_ERROR_MSG);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        logger.info("POST request " + MAPPING_BOOKING_ALL);
        if (req.getParameter(PARAMETER_FILTER_BOOKING) != null
                && !req.getParameter(PARAMETER_FILTER_BOOKING).equals("")) {
            Long sort = Long.valueOf(req.getParameter(PARAMETER_FILTER_BOOKING));
            req.getSession().setAttribute(SESSION_ATTRIBUTE_FILTER_BOOKING, sort);
            logger.info(SESSION_ATTRIBUTE_FILTER_BOOKING + " - session attribute has been set ");
        }
        resp.sendRedirect(req.getContextPath() + MAPPING_BOOKING_ALL);
    }

    private BookingsDto getBookingsDto(HttpServletRequest req, Role userRole, int page)
            throws DataProcessingException {
        Long sort = (Long) req.getSession()
                .getAttribute(SESSION_ATTRIBUTE_FILTER_BOOKING);
        if (userRole.equals(Role.CUSTOMER)) {
            if (sort == null) {
                sort = PARAMETER_FILTER_PAYMENT_STATUS_INVOICE;
            }
            Long customerId = (Long) req.getSession().getAttribute(SESSION_ATTRIBUTE_USER_ID);
            return bookingService.getAllByCustomerId(customerId, page,
                    BOOKING_RECORDS_PER_PAGE, filterParameters.get(sort));
        }
        if (userRole.equals(Role.MANAGER) || userRole.equals(Role.ADMIN)) {
            if (sort == null) {
                sort = PARAMETER_FILTER_PAYMENT_STATUS_WAIT;
            }
            return bookingService.getAll(page, BOOKING_RECORDS_PER_PAGE,
                    filterParameters.get(sort));
        }
        return new BookingsDto();
    }

    private void setAttribute(HttpServletRequest req, BookingsDto bookingsDto, int page) {
        int numberOfPages = (int)
                Math.ceil(bookingsDto.getNumberOfBookings() * 1.0 / BOOKING_RECORDS_PER_PAGE);
        req.setAttribute(ATTRIBUTE_ALL_BOOKINGS, bookingsDto.getBookings());
        req.setAttribute(ATTRIBUTE_BOOKING_CURRENT_PAGE, page);
        req.setAttribute(ATTRIBUTE_BOOKING_NUMBER_OF_PAGES, numberOfPages);
    }
}

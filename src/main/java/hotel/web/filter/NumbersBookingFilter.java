package hotel.web.filter;

import static hotel.util.Constant.MAPPING_ALL_PAGES;

import hotel.exception.DataProcessingException;
import hotel.lib.Injector;
import hotel.model.enums.PaymentStatus;
import hotel.model.enums.Role;
import hotel.service.BookingService;
import hotel.service.RequestService;
import hotel.util.Constant;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSessionAttributeListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebFilter(MAPPING_ALL_PAGES)
public class NumbersBookingFilter implements Constant, HttpSessionAttributeListener, Filter {
    private static final Logger logger = LogManager.getLogger(NumbersBookingFilter.class);
    private static final Injector injector = Injector.getInstance(MAIN_PACKAGE_NAME);
    private final RequestService requestService;
    private final BookingService bookingService;

    public NumbersBookingFilter() {
        requestService = (RequestService) injector.getInstance(RequestService.class);
        bookingService = (BookingService) injector.getInstance(BookingService.class);
    }

    public NumbersBookingFilter(RequestService requestService, BookingService bookingService) {
        this.requestService = requestService;
        this.bookingService = bookingService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        try {
            setNumberOfBookingRequestsToSession(httpServletRequest);
            setNumberOfBookingToSession(httpServletRequest);
        } catch (DataProcessingException e) {
            logger.warn("Parameter setting failed or passed with an error " + e);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void setNumberOfBookingRequestsToSession(HttpServletRequest req)
            throws DataProcessingException {
        if (req.getSession().getAttribute(SESSION_ATTRIBUTE_USER_ID) != null
                    && req.getSession().getAttribute(
                            SESSION_ATTRIBUTE_USER_ROLE) != null) {
            if (req.getSession().getAttribute(
                    SESSION_ATTRIBUTE_USER_ROLE) == Role.CUSTOMER) {
                Long userId =
                        (Long) req.getSession().getAttribute(SESSION_ATTRIBUTE_USER_ID);
                int numberOfBookingRequests =
                        requestService.getNumberOfBookingRequestsPerCustomer(userId);
                req.getSession().setAttribute(
                        SESSION_ATTRIBUTE_NUMBER_OF_REQUEST, numberOfBookingRequests);
            }
            if (req.getSession().getAttribute(
                    SESSION_ATTRIBUTE_USER_ROLE) == Role.MANAGER
                    || req.getSession().getAttribute(
                    SESSION_ATTRIBUTE_USER_ROLE) == Role.ADMIN) {
                int numberOfBookingRequests =
                        requestService.getNumberOfBookingRequests();
                req.getSession().setAttribute(
                        SESSION_ATTRIBUTE_NUMBER_OF_REQUEST, numberOfBookingRequests);
            }

        }
    }

    private void setNumberOfBookingToSession(HttpServletRequest httpServletRequest)
            throws DataProcessingException {
        if (httpServletRequest.getSession().getAttribute(SESSION_ATTRIBUTE_USER_ID) != null
                && httpServletRequest.getSession().getAttribute(
                        SESSION_ATTRIBUTE_USER_ROLE) != null) {
            if (httpServletRequest.getSession().getAttribute(
                    SESSION_ATTRIBUTE_USER_ROLE) == Role.CUSTOMER) {
                Long userId = (Long) httpServletRequest.getSession().getAttribute(
                        SESSION_ATTRIBUTE_USER_ID);
                int numberOfBookingProposal = bookingService.getNumberOfBookingPerCustomer(
                        userId, PaymentStatus.PROPOSAL);
                int numberOfBookingInvoice = bookingService.getNumberOfBookingPerCustomer(
                        userId, PaymentStatus.INVOICE);
                httpServletRequest.getSession().setAttribute(
                        SESSION_ATTRIBUTE_NUMBER_OF_BOOKING,
                        numberOfBookingProposal + numberOfBookingInvoice);
                httpServletRequest.getSession().setAttribute(
                        SESSION_ATTRIBUTE_NUMBER_OF_BOOKING_PROPOSAL,
                        numberOfBookingProposal);
                httpServletRequest.getSession().setAttribute(
                        SESSION_ATTRIBUTE_NUMBER_OF_BOOKING_INVOICE,
                        numberOfBookingInvoice);
            }
            if (httpServletRequest.getSession().getAttribute(
                    SESSION_ATTRIBUTE_USER_ROLE) == Role.ADMIN
                    || httpServletRequest.getSession().getAttribute(
                            SESSION_ATTRIBUTE_USER_ROLE) == Role.MANAGER) {
                int numberOfBookingWait =
                        bookingService.getNumberOfBooking(PaymentStatus.WAIT);
                httpServletRequest.getSession().setAttribute(
                        SESSION_ATTRIBUTE_NUMBER_OF_BOOKING, numberOfBookingWait);
            }
        }
    }
}

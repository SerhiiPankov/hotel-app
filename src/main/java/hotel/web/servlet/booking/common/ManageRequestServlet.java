package hotel.web.servlet.booking.common;

import static hotel.util.Constant.MAPPING_BOOKING_REQUEST_ALL;

import hotel.dto.list.RequestsDto;
import hotel.exception.DataProcessingException;
import hotel.lib.Injector;
import hotel.model.enums.Role;
import hotel.service.RequestService;
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

@WebServlet(MAPPING_BOOKING_REQUEST_ALL)
public class ManageRequestServlet extends HttpServlet implements Constant, PageUtil {
    private static final long PARAMETER_FILTER_STATUS_ON_THE_ANVIL = 1L;
    private static final long PARAMETER_FILTER_STATUS_PROCESSED = 2L;
    private static final Map<Long, Boolean> filterParameters = new HashMap<>();
    private static final Logger logger =
            LogManager.getLogger(ManageRequestServlet.class);
    private static final Injector injector = Injector.getInstance(MAIN_PACKAGE_NAME);
    private final RequestService requestService = (RequestService) injector
            .getInstance(RequestService.class);

    static {
        filterParameters.put(PARAMETER_FILTER_STATUS_ON_THE_ANVIL, false);
        filterParameters.put(PARAMETER_FILTER_STATUS_PROCESSED, true);
        filterParameters.put(null, false);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        logger.info("GET request " + MAPPING_BOOKING_REQUEST_ALL);
        Role userRole = (Role) req.getSession().getAttribute(SESSION_ATTRIBUTE_USER_ROLE);
        int page = getPage(req);
        try {
            RequestsDto requestsDto = getRequestsDto(req, userRole, page);
            logger.info("The filtered list of requests was loaded successfully");
            setAttribute(req, requestsDto, page);
            logger.info("Load jsp page " + JSP_BOOKINGS_ALL_REQUEST);
            req.getRequestDispatcher(JSP_BOOKINGS_ALL_REQUEST)
                    .forward(req, resp);
        } catch (DataProcessingException e) {
            logger.warn("Load list of requests failed " + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_ERROR_PAGE + "?"
                    + PARAMETER_MESSAGE + "=" + ACCESS_ERROR_MSG);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        logger.info("POST request " + MAPPING_BOOKING_REQUEST_ALL);
        if (req.getParameter(PARAMETER_FILTER_REQUEST) != null
                && !req.getParameter(PARAMETER_FILTER_REQUEST).equals("")) {
            Long sort = Long.valueOf(req.getParameter(PARAMETER_FILTER_REQUEST));
            req.getSession().setAttribute(SESSION_ATTRIBUTE_FILTER_REQUEST, sort);
            logger.info(SESSION_ATTRIBUTE_FILTER_REQUEST + " - session attribute has been set ");
        }
        resp.sendRedirect(req.getContextPath() + MAPPING_BOOKING_REQUEST_ALL);
    }

    private RequestsDto getRequestsDto(HttpServletRequest req, Role userRole, int page)
            throws DataProcessingException {
        Long filter = (Long) req.getSession()
                .getAttribute(SESSION_ATTRIBUTE_FILTER_REQUEST);
        if (userRole.equals(Role.CUSTOMER)) {
            Long customerId = (Long) req.getSession().getAttribute(SESSION_ATTRIBUTE_USER_ID);
            return requestService.getAllDtoByCustomerId(
                    customerId, page, REQUEST_RECORDS_PER_PAGE, filterParameters.get(filter));
        }
        if (userRole.equals(Role.MANAGER) || userRole.equals(Role.ADMIN)) {
            return requestService.getAllDto(
                    page, REQUEST_RECORDS_PER_PAGE, filterParameters.get(filter));
        }
        return new RequestsDto();
    }

    private void setAttribute(HttpServletRequest req, RequestsDto requestsDto, int page) {
        int numberOfPages = (int)
                Math.ceil(requestsDto.getNumberOfRequests() * 1.0 / REQUEST_RECORDS_PER_PAGE);
        req.setAttribute(ATTRIBUTE_ALL_REQUESTS, requestsDto.getRequests());
        req.setAttribute(ATTRIBUTE_REQUEST_CURRENT_PAGE, page);
        req.setAttribute(ATTRIBUTE_REQUEST_NUMBER_OF_PAGES, numberOfPages);
    }
}

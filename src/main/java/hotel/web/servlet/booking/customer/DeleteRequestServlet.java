package hotel.web.servlet.booking.customer;

import static hotel.util.Constant.MAPPING_BOOKING_REQUEST_DELETE;

import hotel.exception.DataProcessingException;
import hotel.exception.WrongDataException;
import hotel.lib.Injector;
import hotel.model.Request;
import hotel.service.RequestService;
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

@WebServlet(MAPPING_BOOKING_REQUEST_DELETE)
public class DeleteRequestServlet extends HttpServlet implements Constant {
    private static final Logger logger = LogManager.getLogger(DeleteRequestServlet.class);
    private static final Injector injector = Injector.getInstance(MAIN_PACKAGE_NAME);
    private final RequestService requestService = (RequestService) injector
            .getInstance(RequestService.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String bookingRequestIdParameter;
        try {
            bookingRequestIdParameter = Validator.validate(req.getParameter(PARAMETER_REQUEST_ID));
        } catch (WrongDataException e) {
            logger.warn("Referencing a page " + MAPPING_BOOKING_REQUEST_DELETE
                    + " without parameter" + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_ERROR_PAGE + "?"
                    + PARAMETER_MESSAGE + "=" + NO_PARAMETER_ERROR_MSG);
            return;
        }
        long bookingRequestId = Long.parseLong(bookingRequestIdParameter);
        try {
            Request request = requestService.get(bookingRequestId);
            long userId = (long) req.getSession().getAttribute(SESSION_ATTRIBUTE_USER_ID);
            if (userId != request.getCustomerId()) {
                logger.warn("Referencing a page " + MAPPING_BOOKING_REQUEST_DELETE
                                + "with wrong parameters");
                resp.sendRedirect(req.getContextPath() + MAPPING_ERROR_PAGE + "?"
                        + PARAMETER_MESSAGE + "=" + WRONG_PARAMETER_ERROR_MSG);
            }
            requestService.delete(bookingRequestId);
            logger.info("Booking request with id:" + bookingRequestId + " was deleted");
            resp.sendRedirect(req.getContextPath() + MAPPING_BOOKING_REQUEST_ALL);
        } catch (DataProcessingException | WrongDataException e) {
            logger.warn("Delete booking request with id " + bookingRequestId + " failed " + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_BOOKING_REQUEST_ALL + "?"
                    + PARAMETER_MESSAGE + "=" + DELETE_ERROR_MSG);
        }
    }
}

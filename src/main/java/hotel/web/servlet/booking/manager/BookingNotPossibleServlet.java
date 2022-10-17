package hotel.web.servlet.booking.manager;

import static hotel.util.Constant.MAPPING_BOOKING_NOT_POSSIBLE;

import hotel.exception.DataProcessingException;
import hotel.exception.WrongDataException;
import hotel.lib.Injector;
import hotel.model.Request;
import hotel.model.User;
import hotel.service.RequestService;
import hotel.service.UserService;
import hotel.util.Constant;
import hotel.util.EmailUtil;
import hotel.validation.Validator;
import jakarta.mail.MessagingException;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet(MAPPING_BOOKING_NOT_POSSIBLE)
public class BookingNotPossibleServlet extends HttpServlet implements Constant {
    private static final Logger logger = LogManager.getLogger(BookingNotPossibleServlet.class);
    private static final Injector injector = Injector.getInstance(MAIN_PACKAGE_NAME);
    private final RequestService requestService = (RequestService) injector
            .getInstance(RequestService.class);
    private final UserService userService = (UserService) injector
            .getInstance(UserService.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        long requestId;
        try {
            requestId = Long.parseLong(
                    Validator.validate(req.getParameter(PARAMETER_REQUEST_ID)));
        } catch (WrongDataException e) {
            logger.warn("Referencing a page " + MAPPING_BOOKING_NOT_POSSIBLE
                    + " without parameter" + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_ERROR_PAGE + "?"
                    + PARAMETER_MESSAGE + "=" + NO_PARAMETER_ERROR_MSG);
            return;
        }

        try {
            requestService.setIsProcessing(requestId);
            Request request = requestService.get(requestId);
            User user = userService.get(request.getCustomerId());
            EmailUtil.sendBookingNotPossibleEmail(req, request, user);
            req.getSession().removeAttribute(SESSION_ATTRIBUTE_REQUEST_ID);
            resp.sendRedirect(req.getContextPath() + MAPPING_BOOKING_REQUEST_ALL);
        } catch (DataProcessingException | WrongDataException e) {
            logger.warn("Sending an e-mail message about the impossibility "
                    + "of the reservation failed " + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_BOOKING_SELECTION + "?"
                    + PARAMETER_MESSAGE + "=" + FAILED_TO_SEND_NOTIFICATION);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}

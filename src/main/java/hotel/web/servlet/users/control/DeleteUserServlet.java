package hotel.web.servlet.users.control;

import static hotel.util.Constant.MAPPING_USERS_DELETE;

import hotel.exception.DataProcessingException;
import hotel.exception.WrongDataException;
import hotel.lib.Injector;
import hotel.service.UserService;
import hotel.util.Constant;
import hotel.validation.Validator;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Servlet for delete user account
 *
 *  @author Serhii Pankov
 *  @version 1.0
 */
@WebServlet(MAPPING_USERS_DELETE)
public class DeleteUserServlet extends HttpServlet implements Constant {
    private static final Logger logger = LogManager.getLogger(DeleteUserServlet.class);
    private static final Injector injector = Injector.getInstance(MAIN_PACKAGE_NAME);
    private final UserService userService;

    public DeleteUserServlet() {
        userService = (UserService) injector
                .getInstance(UserService.class);
    }

    public DeleteUserServlet(UserService userService) {
        this.userService = userService;
    }

    /**
     * doPost method for delete user account <br>
     *
     * - request parameter validation <br>
     * - user account deletion <br>
     * - redirect to Users management servlet <br>
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws IOException Signals an I/O exception.
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        long userId;
        try {
            userId = Long.parseLong(Validator.validate(req.getParameter(PARAMETER_USER_ID)));
        } catch (WrongDataException e) {
            logger.warn("Referencing a page " + MAPPING_USERS_DELETE
                    + " without parameter" + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_ERROR_PAGE + "?"
                    + PARAMETER_MESSAGE + "=" + NO_PARAMETER_ERROR_MSG);
            return;
        }
        try {
            userService.delete(userId);
            resp.sendRedirect(req.getContextPath() + MAPPING_USERS);
        } catch (DataProcessingException e) {
            logger.warn("Delete user with id" + userId + " failed" + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_USERS + "?"
                    + PARAMETER_MESSAGE + "=" + ACCESS_ERROR_MSG);
        }
    }
}

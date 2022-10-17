package hotel.web.servlet.users.account;

import static hotel.util.Constant.MAPPING_USERS_ACCOUNT_DELETE;

import hotel.exception.DataProcessingException;
import hotel.exception.WrongDataException;
import hotel.lib.Injector;
import hotel.service.AuthenticationService;
import hotel.service.UserService;
import hotel.util.Constant;
import hotel.util.UserPhoneUtil;
import hotel.validation.Validator;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Servlet to delete own user account
 *
 *  @author Serhii pankov
 *  @version 1.0
 */
@WebServlet(MAPPING_USERS_ACCOUNT_DELETE)
public class DeleteUserAccountServlet extends HttpServlet
        implements UserPhoneUtil, Constant {
    private static final Logger logger = LogManager.getLogger(DeleteUserAccountServlet.class);
    private static final Injector injector = Injector.getInstance(MAIN_PACKAGE_NAME);
    private final UserService userService;
    private final AuthenticationService authenticationService;

    public DeleteUserAccountServlet() {
        this.userService = (UserService) injector.getInstance(UserService.class);
        this.authenticationService = (AuthenticationService) injector
                .getInstance(AuthenticationService.class);
    }

    public DeleteUserAccountServlet(UserService userService,
                                    AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    /**
     * doPost method for delete own user account <br>
     *
     * - request parameter validation <br>
     * - check user password <br>
     * - user account deletion <br>
     * - redirect to Logout servlet <br>
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws IOException Signals an I/O exception.
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String password;
        try {
            password = Validator.validate(req.getParameter(PARAMETER_USER_PASSWORD_DELETE));
        } catch (WrongDataException e) {
            logger.warn("Referencing a page " + MAPPING_USERS_ACCOUNT_DELETE
                    + " without parameter - " + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_ERROR_PAGE + "?"
                    + PARAMETER_MESSAGE + "=" + NO_PARAMETER_ERROR_MSG);
            return;
        }
        Long userId = (Long) req.getSession().getAttribute(SESSION_ATTRIBUTE_USER_ID);
        try {
            if (authenticationService.checkPassword(userId, password)) {
                userService.delete(userId);
                logger.info("User with id " + userId + " deleted his account");
                resp.sendRedirect(req.getContextPath() + MAPPING_LOGOUT);
            } else {
                logger.warn("When deleting an account, User with id "
                        + userId + " entered wrong password");
                resp.sendRedirect(req.getContextPath() + MAPPING_USERS_ACCOUNT + "?"
                        + PARAMETER_MESSAGE + "=" + NO_VALID_PASSWORD_ERROR_MSG);
            }
        } catch (DataProcessingException | WrongDataException e) {
            logger.warn("Delete user account with id " + userId + " failed - " + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_USERS_ACCOUNT + "?"
                    + PARAMETER_MESSAGE + "=" + ACCESS_ERROR_MSG);
        }
    }
}

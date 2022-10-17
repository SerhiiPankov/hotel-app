package hotel.web.servlet.users;

import static hotel.util.Constant.MAPPING_AUTHORIZATION;

import hotel.exception.AuthenticationException;
import hotel.exception.DataProcessingException;
import hotel.exception.WrongDataException;
import hotel.lib.Injector;
import hotel.model.User;
import hotel.service.AuthenticationService;
import hotel.util.Constant;
import hotel.validation.Validator;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet(MAPPING_AUTHORIZATION)
public class AuthorizationServlet extends HttpServlet implements Constant {
    private static final Logger logger = LogManager.getLogger(AuthorizationServlet.class);
    private static final Injector injector = Injector.getInstance(MAIN_PACKAGE_NAME);
    private final AuthenticationService authenticationService;

    public AuthorizationServlet() {
        authenticationService =
                (AuthenticationService) injector.getInstance(AuthenticationService.class);
    }

    public AuthorizationServlet(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * doPost method for Login page <br>
     *
     * - get login form data <br>
     * - request parameter validation <br>
     * - user authorization <br>
     * - setting the session attribute for the authorized user <br>
     * - redirect to main page <br>
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws IOException Signals an I/O exception.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String email;
        String password;
        try {
            email = Validator.validate(req.getParameter(PARAMETER_EMAIL));
            password = Validator.validate(req.getParameter(PARAMETER_PASSWORD));
        } catch (WrongDataException e) {
            logger.warn("Referencing a page " + MAPPING_LOGIN
                    + " without parameter" + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_ERROR_PAGE + "?"
                    + PARAMETER_MESSAGE + "=" + NO_PARAMETER_ERROR_MSG);
            return;
        }
        try {
            User user = authenticationService.login(email, password);
            req.getSession().setAttribute(SESSION_ATTRIBUTE_USER_ID, user.getId());
            req.getSession().setAttribute(SESSION_ATTRIBUTE_USER_ROLE, user.getRole());
            req.getSession().setAttribute(SESSION_ATTRIBUTE_USER_NAME, user.getName());
            req.getSession().setAttribute(SESSION_ATTRIBUTE_LANGUAGE, user.getLanguage());
            logger.info("User with email " + email + " successfully logged in");
            resp.sendRedirect(req.getContextPath() + MAPPING_ENTRY_POINT);
        } catch (AuthenticationException e) {
            logger.warn("User with email " + email + " tried to login with wrong password " + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_LOGIN + "?"
                    + PARAMETER_MESSAGE + "=" + AUTH_ERROR_MSG);
        } catch (WrongDataException e) {
            logger.warn("User with email " + email + " unsuccessfully tried to login " + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_REGISTER + "?"
                    + PARAMETER_MESSAGE + "=" + REGISTER_ERROR_MSG);
        } catch (DataProcessingException e) {
            logger.warn("User with email " + email + " unsuccessfully tried to login " + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_LOGIN + "?"
                    + PARAMETER_MESSAGE + "=" + ACCESS_ERROR_MSG);
        }
    }
}

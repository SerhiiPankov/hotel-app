package hotel.web.servlet.users;

import static hotel.util.Constant.MAPPING_DO_REGISTER;

import hotel.exception.DataProcessingException;
import hotel.exception.WrongDataException;
import hotel.lib.Injector;
import hotel.model.User;
import hotel.model.enums.Role;
import hotel.service.AuthenticationService;
import hotel.util.Constant;
import hotel.util.UserPhoneUtil;
import hotel.validation.PasswordValidator;
import hotel.validation.Validator;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Servlet to get registration form data and register user in database
 *
 *  @author Serhii Pankov
 *  @version 1.0
 */
@WebServlet(MAPPING_DO_REGISTER)
public class AuthenticationServlet extends HttpServlet implements UserPhoneUtil, Constant {
    private static final Logger logger = LogManager.getLogger(AuthenticationServlet.class);
    private static final Injector injector = Injector.getInstance(MAIN_PACKAGE_NAME);
    private final AuthenticationService authenticationService;

    public AuthenticationServlet() {
        this.authenticationService = (AuthenticationService) injector
                .getInstance(AuthenticationService.class);
    }

    protected AuthenticationServlet(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * doPost method for Registration page <br>
     *
     * - get registration form data <br>
     * - request parameter validation <br>
     * - user role check <br>
     * - password matching check <br>
     * - registers a user in the database based on the user's role <br>
     * - redirection based on user role
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws IOException Signals an I/O exception.
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String email;
        String rawPassword;
        String rawPasswordRepeat;
        String name;
        String countryCode;
        String phone;
        try {
            email = Validator.validate(req.getParameter(PARAMETER_NEW_USER_EMAIL));
            rawPassword = Validator.validate(req.getParameter(PARAMETER_NEW_USER_PASSWORD));
            rawPasswordRepeat =
                    Validator.validate(req.getParameter(PARAMETER_NEW_USER_PASSWORD_REPEAT));
            name = Validator.validate(req.getParameter(PARAMETER_NEW_USER_NAME));
            countryCode = Validator.validate(req.getParameter(PARAMETER_NEW_USER_COUNTRY_CODE));
            phone = Validator.validate(req.getParameter(PARAMETER_NEW_USER_PHONE_NUMBER));
        } catch (WrongDataException e) {
            logger.warn("Referencing a page " + MAPPING_REGISTER
                    + " without parameter" + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_ERROR_PAGE + "?"
                    + PARAMETER_MESSAGE + "=" + NO_PARAMETER_ERROR_MSG);
            return;
        }
        Role userRole = (Role) req.getSession().getAttribute(SESSION_ATTRIBUTE_USER_ROLE);
        Role role = Role.CUSTOMER;
        if (userRole != null && userRole.equals(Role.ADMIN)) {
            role = Role.MANAGER;
        }
        if (PasswordValidator.isEqual(rawPassword, rawPasswordRepeat)) {
            try {
                User user = new User.Builder(email)
                        .setPassword(rawPassword)
                        .setName(name)
                        .setPhone(getPhone(phone, countryCode))
                        .setRole(role)
                        .build();
                authenticationService.register(user);
                logger.info("Registration user with email " + email + " was successful");
                if (role.equals(Role.MANAGER)) {
                    resp.sendRedirect(req.getContextPath() + MAPPING_USERS);
                    return;
                }
                resp.sendRedirect(req.getContextPath() + MAPPING_REGISTER + "?"
                        + PARAMETER_MESSAGE + "=" + REGISTER_OK_MSG);
            } catch (DataProcessingException e) {
                logger.warn("Registration user with email "
                        + email + " was unsuccessful" + e);
                resp.sendRedirect(req.getContextPath() + MAPPING_REGISTER + "?"
                        + PARAMETER_MESSAGE + "=" + ACCESS_ERROR_MSG + "&"
                        + PARAMETER_EMAIL + "=" + email);
            }
        } else {
            logger.info("Invalid registration user with email " + email
                    + ", passwords do not match");
            resp.sendRedirect(req.getContextPath() + MAPPING_REGISTER + "?"
                    + PARAMETER_MESSAGE + "=" + NO_MATCH_PASSWORDS_ERROR_MSG);
        }
    }
}

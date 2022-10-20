package hotel.web.servlet.users.account;

import static hotel.util.Constant.MAPPING_USERS_ACCOUNT_UPDATE;

import hotel.exception.DataProcessingException;
import hotel.exception.WrongDataException;
import hotel.lib.Injector;
import hotel.model.User;
import hotel.service.AuthenticationService;
import hotel.service.UserService;
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
 * Servlet for update user account
 *
 *  @author Serhii pankov
 *  @version 1.0
 */
@WebServlet(MAPPING_USERS_ACCOUNT_UPDATE)
public class UpdateUserAccountServlet extends HttpServlet
        implements UserPhoneUtil, Constant {
    private static final Logger logger = LogManager.getLogger(UpdateUserAccountServlet.class);
    private static final Injector injector = Injector.getInstance(MAIN_PACKAGE_NAME);
    private final AuthenticationService authenticationService;
    private final UserService userService;

    public UpdateUserAccountServlet() {
        authenticationService = (AuthenticationService) injector
                .getInstance(AuthenticationService.class);
        userService = (UserService) injector
                .getInstance(UserService.class);
    }

    protected UpdateUserAccountServlet(AuthenticationService authenticationService,
                                    UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    /**
     *
     * doPost method for update user account <br>
     *
     * - get user account form data <br>
     * - request parameter validation <br>
     * - check user password <br>
     * - checking for a new password <br>
     * - update user credentials <br>
     * - set actual username to session <br>
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws IOException Signals an I/O exception.
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String password;
        String email;
        String name;
        String phoneNumber;
        String countryCode;
        try {
            password = Validator.validate(req.getParameter(PARAMETER_UPDATE_FORM_PASSWORD));
            email = Validator.validate(req.getParameter(PARAMETER_USER_EMAIL));
            name = Validator.validate(req.getParameter(PARAMETER_USER_NAME));
            phoneNumber = Validator.validate(req.getParameter(PARAMETER_USER_PHONE_NUMBER));
            countryCode = Validator.validate(req.getParameter(PARAMETER_USER_COUNTRY_CODE));
        } catch (WrongDataException e) {
            logger.warn("Referencing a page " + MAPPING_USERS_ACCOUNT_UPDATE
                    + " without parameter" + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_ERROR_PAGE + "?"
                    + PARAMETER_MESSAGE + "=" + NO_PARAMETER_ERROR_MSG);
            return;
        }
        Long userId = (Long) req.getSession().getAttribute(SESSION_ATTRIBUTE_USER_ID);
        try {
            if (authenticationService.checkPassword(userId, password)) {
                User userFromDb = userService.get(userId);
                String rawNewPassword = req.getParameter(PARAMETER_NEW_PASSWORD_UPDATE);
                String rawNewPasswordRepeat =
                        req.getParameter(PARAMETER_NEW_PASSWORD_REPEAT_UPDATE);
                if (rawNewPassword != null && !rawNewPassword.equals("")) {
                    if (PasswordValidator.isEqual(rawNewPassword, rawNewPasswordRepeat)) {
                        password = rawNewPassword;
                    } else {
                        logger.info("Unsuccessful attempt to change password, "
                                + "new passwords do not match. User with id " + userId);
                        resp.sendRedirect(req.getContextPath() + MAPPING_USERS_ACCOUNT + "?"
                                + PARAMETER_MESSAGE + "=" + NO_MATCH_PASSWORDS_ERROR_MSG);
                    }
                }
                User user = new User.Builder(email)
                        .setId(userId)
                        .setPassword(password)
                        .setName(name)
                        .setPhone(getPhone(phoneNumber, countryCode))
                        .setRole(userFromDb.getRole())
                        .setLanguage(userFromDb.getLanguage())
                        .build();
                user = userService.update(user);
                req.getSession().setAttribute(SESSION_ATTRIBUTE_USER_NAME, name);
                logger.info("User with id " + userId
                        + " successfully made changes to his account");
                resp.sendRedirect(req.getContextPath() + MAPPING_USERS_ACCOUNT);
            } else {
                logger.warn("User with id "
                        + userId + " update account failed, not valid password");
                resp.sendRedirect(req.getContextPath() + MAPPING_USERS_ACCOUNT + "?"
                        + PARAMETER_MESSAGE + "=" + NO_VALID_PASSWORD_ERROR_MSG);
            }
        } catch (DataProcessingException | WrongDataException e) {
            logger.warn("User account update with id "
                    + userId + " failed " + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_USERS_ACCOUNT + "?"
                    + PARAMETER_MESSAGE + "=" + ACCESS_ERROR_MSG);
        }
    }
}

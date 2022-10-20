package hotel.web.servlet.users.account;

import static hotel.util.Constant.MAPPING_USERS_ACCOUNT;

import hotel.exception.DataProcessingException;
import hotel.exception.WrongDataException;
import hotel.lib.Injector;
import hotel.model.User;
import hotel.service.UserService;
import hotel.util.Constant;
import hotel.util.UserPhoneUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Servlet to get user credentials for update
 *
 *  @author Serhii pankov
 *  @version 1.0
 */
@WebServlet(MAPPING_USERS_ACCOUNT)
public class UserAccountServlet extends HttpServlet implements UserPhoneUtil, Constant {
    private static final Logger logger = LogManager.getLogger(UserAccountServlet.class);
    private static final Injector injector = Injector.getInstance(MAIN_PACKAGE_NAME);
    private final UserService userService;

    public UserAccountServlet() {
        userService = (UserService) injector.getInstance(UserService.class);
    }

    protected UserAccountServlet(UserService userService) {
        this.userService = userService;
    }

    /**
     * doGet method for User account page <br>
     *
     * - get user account credentials <br>
     * - set user account credentials to attribute <br>
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws ServletException Signals a Servlet exception
     * @throws IOException Signals an I/O exception.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long userId = (Long) req.getSession().getAttribute(SESSION_ATTRIBUTE_USER_ID);
        try {
            User user = userService.get(userId);
            req.setAttribute(ATTRIBUTE_ACCOUNT_USER_EMAIL, user.getEmail());
            req.setAttribute(ATTRIBUTE_ACCOUNT_USER_NAME, user.getName());
            req.setAttribute(ATTRIBUTE_ACCOUNT_USER_COUNTRY_CODE,
                    getCountryCode(user.getPhone()));
            req.setAttribute(ATTRIBUTE_ACCOUNT_USER_PHONE_NUMBER,
                    getPhoneNumber(user.getPhone()));
            req.getRequestDispatcher(JSP_USERS_ACCOUNT).forward(req, resp);
        } catch (DataProcessingException | WrongDataException e) {
            logger.warn("Load user account with id " + userId + " failed - " + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_ERROR_PAGE + "?"
                    + PARAMETER_MESSAGE + "=" + ACCESS_ERROR_MSG);
        }
    }
}

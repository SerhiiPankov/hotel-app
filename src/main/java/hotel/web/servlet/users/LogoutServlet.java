package hotel.web.servlet.users;

import static hotel.util.Constant.MAPPING_LOGOUT;

import hotel.util.Constant;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Servlet to call logout
 *
 *  @author Serhii Pankov
 *  @version 1.0
 */
@WebServlet(MAPPING_LOGOUT)
public class LogoutServlet extends HttpServlet implements Constant {
    private static final Logger logger = LogManager.getLogger(LogoutServlet.class);

    /**
     * doGet method for logout <br>
     *
     * - get user id from session <br>
     * - invalidate session <br>
     * - redirect to main page <br>
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws IOException Signals an I/O exception.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        Long userId = (Long) req.getSession().getAttribute(SESSION_ATTRIBUTE_USER_ID);
        req.getSession().invalidate();
        logger.info("User with id " + userId + " logged out");
        resp.sendRedirect(req.getContextPath() + MAPPING_ENTRY_POINT);
    }
}

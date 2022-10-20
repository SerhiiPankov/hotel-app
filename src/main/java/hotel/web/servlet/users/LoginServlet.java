package hotel.web.servlet.users;

import static hotel.util.Constant.MAPPING_LOGIN;

import hotel.util.Constant;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Servlet for getting the login page and getting the data of the login form
 * and the user's login to the application
 *
 *  @author Serhii Pankov
 *  @version 1.0
 */
@WebServlet(MAPPING_LOGIN)
public class LoginServlet extends HttpServlet implements Constant {
    private static final Logger logger = LogManager.getLogger(LogoutServlet.class);

    /**
     * doGet method for Login page
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
        if (userId != null) {
            logger.info("Authorized user with id " + userId
                    + " made an attempt to access the login page");
            resp.sendRedirect(req.getContextPath() + MAPPING_ENTRY_POINT);
            return;
        }
        logger.info("An unauthorized user has entered the login page");
        req.getRequestDispatcher(JSP_LOGIN).forward(req, resp);
    }
}

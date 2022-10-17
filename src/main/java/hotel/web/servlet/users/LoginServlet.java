package hotel.web.servlet.users;

import static hotel.util.Constant.MAPPING_LOGIN;

import hotel.util.Constant;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for getting the login page and getting the data of the login form
 * and the user's login to the application
 *
 *  @author Serhii Pankov
 *  @version 1.0
 */
@WebServlet(MAPPING_LOGIN)
public class LoginServlet extends HttpServlet implements Constant {

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
        if (req.getSession().getAttribute(SESSION_ATTRIBUTE_USER_ID) != null) {
            resp.sendRedirect(req.getContextPath() + MAPPING_ENTRY_POINT);
            return;
        }
        req.getRequestDispatcher(JSP_LOGIN).forward(req, resp);
    }
}

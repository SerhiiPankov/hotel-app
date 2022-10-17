package hotel.web.servlet.users;

import static hotel.util.Constant.MAPPING_ENTRY_POINT;

import hotel.util.Constant;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet to call main page
 *
 * @author Serhii Pankov
 * @version 1.0
 */
@WebServlet(MAPPING_ENTRY_POINT)
public class IndexServlet extends HttpServlet implements Constant {

    /**
     * doGet method for Main page
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws ServletException Signals a Servlet exception
     * @throws IOException Signals an I/O exception.
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(JSP_INDEX).forward(req, resp);
    }
}

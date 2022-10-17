package hotel.web.servlet;

import static hotel.util.Constant.MAPPING_ERROR_PAGE;

import hotel.util.Constant;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet to call Error page
 *
 * @author Serhii Pankov
 * @version 1.0
 */
@WebServlet(MAPPING_ERROR_PAGE)
public class ErrorPageServlet extends HttpServlet implements Constant {

    /**
     * doGet method for Error page
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws ServletException Signals a Servlet exception
     * @throws IOException Signals an I/O exception.
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(JSP_ERROR_PAGE).forward(req, resp);
    }
}

package hotel.web.servlet.booking.customer;

import static hotel.util.Constant.MAPPING_BOOKING_SELECT_SORT;

import hotel.util.Constant;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet(MAPPING_BOOKING_SELECT_SORT)
public class SortSelectServlet extends HttpServlet implements Constant {
    private static final Logger logger =
            LogManager.getLogger(SortSelectServlet.class);

    /**
     * Setting sorting options for selected rooms
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws ServletException Signals a Servlet exception
     * @throws IOException Signals an I/O exception.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        long sortRoom;
        if (req.getParameter(PARAMETER_SORT_ROOM) == null
                || req.getParameter(PARAMETER_SORT_ROOM).equals("")) {
            sortRoom = 1L;
        } else {
            sortRoom = Long.parseLong(req.getParameter(PARAMETER_SORT_ROOM));
            logger.info("Sorting option for selected rooms changed");
        }
        req.getSession().setAttribute(SESSION_ATTRIBUTE_SORT_ROOM,
                sortRoom);
        resp.sendRedirect(req.getContextPath() + MAPPING_BOOKING_SELECT);
    }
}

package hotel.web.servlet.users.control;

import static hotel.util.Constant.MAPPING_USERS;

import hotel.dto.list.UsersDto;
import hotel.exception.DataProcessingException;
import hotel.lib.Injector;
import hotel.model.enums.Role;
import hotel.service.UserService;
import hotel.util.Constant;
import hotel.util.PageUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Servlet to get list of users
 *
 *  @author Serhii pankov
 *  @version 1.0
 */
@WebServlet(MAPPING_USERS)
public class ManageUserServlet extends HttpServlet implements Constant, PageUtil {
    private static final long PARAMETER_SORT_USER_NAME = 1L;
    private static final long PARAMETER_SORT_USER_EMAIL = 2L;
    private static final long PARAMETER_SORT_USER_IS_DELETED = 3L;
    private static final Map<Long, String> sortParameters = new HashMap<>();
    private static final Logger logger = LogManager.getLogger(ManageUserServlet.class);
    private static final Injector injector = Injector.getInstance(MAIN_PACKAGE_NAME);

    private final UserService userService;

    static {
        sortParameters.put(PARAMETER_SORT_USER_NAME, COLUMN_NAME_NAME);
        sortParameters.put(PARAMETER_SORT_USER_EMAIL, COLUMN_NAME_EMAIL);
        sortParameters.put(PARAMETER_SORT_USER_IS_DELETED, COLUMN_NAME_IS_DELETED);
        sortParameters.put(null, COLUMN_NAME_IS_DELETED);
    }

    public ManageUserServlet() {
        userService = (UserService) injector
                .getInstance(UserService.class);
    }

    protected ManageUserServlet(UserService userService) {
        this.userService = userService;
    }

    /**
     * doGet method for Manage users page <br>
     *
     * - getting Role from session <br>
     * - getting part of the list of users depending on the role and pagination page <br>
     * - set list of users to attribute
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws ServletException Signals a Servlet exception
     * @throws IOException Signals an I/O exception
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        logger.info("GET request " + MAPPING_USERS);
        Role userRole = (Role) req.getSession().getAttribute(SESSION_ATTRIBUTE_USER_ROLE);
        int page = getPage(req);
        try {
            UsersDto usersDto = getUsersDto(req, userRole, page);
            logger.info("The sorted list of bookings was loaded successfully");
            setAttribute(req, usersDto, page);
            logger.info("Load jsp page " + JSP_USERS_ALL_USERS);
            req.getRequestDispatcher(JSP_USERS_ALL_USERS).forward(req, resp);
        } catch (DataProcessingException e) {
            logger.warn("Load list of users failed" + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_ERROR_PAGE + "?"
                    + PARAMETER_MESSAGE + "=" + ACCESS_ERROR_MSG);
        }
    }

    /**
     * doPost method for Manage users page <br>
     *
     * - getting a parameter to sort the list of users <br>
     * - setting sort attribute to session <br>
     *
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws IOException Signals an I/O exception.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        logger.info("POST request " + MAPPING_USERS);
        if (req.getParameter(PARAMETER_SORT_USER) != null
                && !req.getParameter(PARAMETER_SORT_USER).equals("")) {
            Long sort = Long.valueOf(req.getParameter(PARAMETER_SORT_USER));
            req.getSession().setAttribute(SESSION_ATTRIBUTE_SORT_USER, sort);
            logger.info(SESSION_ATTRIBUTE_SORT_USER + " - session attribute has been set ");
        }
        resp.sendRedirect(req.getContextPath() + MAPPING_USERS);
    }

    /**
     * Setting attribute for page
     *
     * @param req HttpServletRequest
     * @param usersDto UsersDto
     * @param page int
     */
    private void setAttribute(HttpServletRequest req, UsersDto usersDto, int page) {
        int numberOfPages = (int)
                Math.ceil(usersDto.getNumberOfUsers() * 1.0 / USER_RECORDS_PER_PAGE);
        req.setAttribute(ATTRIBUTE_ALL_USERS, usersDto.getUsers());
        req.setAttribute(ATTRIBUTE_USER_CURRENT_PAGE, page);
        req.setAttribute(ATTRIBUTE_USER_NUMBER_OF_PAGES, numberOfPages);
    }

    /**
     * Getting list of users by Role and actual pagination page
     *
     * @param req HttpServletRequest
     * @param userRole Actual user Role (ADMIN, MANAGER, CUSTOMER)
     * @param page int - current pagination page
     * @return UsersDto
     * @throws DataProcessingException Signals a database exception
     */
    private UsersDto getUsersDto(HttpServletRequest req, Role userRole, int page)
            throws DataProcessingException {
        Long sort = (Long) req.getSession()
                .getAttribute(SESSION_ATTRIBUTE_SORT_USER);
        if (userRole.equals(Role.ADMIN)) {
            return userService.getAllByRole(sortParameters.get(sort), page,
                    USER_RECORDS_PER_PAGE, Role.CUSTOMER, Role.MANAGER);
        }
        if (userRole.equals(Role.MANAGER)) {
            return userService.getAllByRole(sortParameters.get(sort), page,
                    USER_RECORDS_PER_PAGE, Role.CUSTOMER);
        }
        return new UsersDto();
    }
}

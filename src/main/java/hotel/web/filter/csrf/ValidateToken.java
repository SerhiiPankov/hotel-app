package hotel.web.filter.csrf;

import static hotel.util.Constant.MAPPING_AUTHORIZATION;
import static hotel.util.Constant.MAPPING_DO_REGISTER;
import static hotel.util.Constant.MAPPING_SET_SCHEDULE;
import static hotel.util.Constant.MAPPING_USERS_ACCOUNT_DELETE;
import static hotel.util.Constant.MAPPING_USERS_ACCOUNT_UPDATE;
import static hotel.util.Constant.MAPPING_USERS_DELETE;
import static hotel.util.Constant.MAPPING_USERS_RESTORE;

import hotel.exception.WrongDataException;
import hotel.util.Constant;
import hotel.validation.Validator;
import java.io.IOException;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Filter to validate tokens to protect against csrf attacks
 *
 * @author Serhii Pankov
 * @version 1.0
 */
@WebFilter({MAPPING_DO_REGISTER,
        MAPPING_USERS_DELETE,
        MAPPING_USERS_RESTORE,
        MAPPING_USERS_ACCOUNT_DELETE,
        MAPPING_USERS_ACCOUNT_UPDATE,
        MAPPING_SET_SCHEDULE,
        MAPPING_AUTHORIZATION,})
public class ValidateToken implements Filter, Constant {
    private static final Logger logger = LogManager.getLogger(ValidateToken.class);

    /**
     * doFilter method to validate CSRF tokens <br>
     *
     * - getting csrf token parameter <br>
     * - getting list of valid tokens <br>
     * - token validity check <br>
     *
     * @param request ServletRequest
     * @param response ServletResponse
     * @param chain FilterChain
     * @throws ServletException Signals a Servlet exception
     * @throws IOException Signals an I/O exception.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String salt;
        List<String> salts;
        try {
            salt = Validator.validate(req.getParameter("csrfSalt"));
            salts = (List<String>) req.getSession().getAttribute("salts");
            if (salts == null || !salts.contains(salt)) {
                throw new WrongDataException(
                        "Potential CSRF detected!! Inform a scary sysadmin ASAP.");
            }
            chain.doFilter(request, response);
        } catch (WrongDataException e) {
            logger.warn("Referencing a page " + MAPPING_LOGIN
                    + " without parameter " + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_ERROR_PAGE + "?"
                    + PARAMETER_MESSAGE + "=" + CSRF_ERROR);
        }
    }
}

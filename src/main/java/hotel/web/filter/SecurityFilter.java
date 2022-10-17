package hotel.web.filter;

import static hotel.util.Constant.MAPPING_ALL_PAGES;

import hotel.model.enums.Role;
import hotel.util.Constant;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Security filter - distribution of access to pages
 *
 *  @author Serhii Pankov
 *  @version 1.0
 */
@WebFilter(MAPPING_ALL_PAGES)
public class SecurityFilter implements Filter, Constant {
    private final Set<String> allowedUrl = new HashSet<>();
    private final Set<String> customerUrl = new HashSet<>();
    private final Set<String> managerUrl = new HashSet<>();

    /**
     * init method for Security filter <br>
     *
     * Formation of URL lists with access distribution for an unauthorized, authorized user, manager
     *
     * @param filterConfig FilterConfig
     */
    @Override
    public void init(FilterConfig filterConfig) {
        // All access
        allowedUrl.add(MAPPING_ENTRY_POINT);
        allowedUrl.add(MAPPING_LOGIN);
        allowedUrl.add(MAPPING_AUTHORIZATION);
        allowedUrl.add(MAPPING_REGISTER);
        allowedUrl.add(MAPPING_DO_REGISTER);
        allowedUrl.add(MAPPING_ERROR_PAGE);
        allowedUrl.add(MAPPING_DOWNLOAD);
        // Customer access
        customerUrl.addAll(allowedUrl);
        customerUrl.add(MAPPING_LOGOUT);
        customerUrl.add(MAPPING_USERS_ACCOUNT);
        customerUrl.add(MAPPING_USERS_ACCOUNT_DELETE);
        customerUrl.add(MAPPING_USERS_ACCOUNT_UPDATE);
        customerUrl.add(MAPPING_BOOKING_REQUEST_ALL);
        customerUrl.add(MAPPING_BOOKING_REQUEST_CREATE);
        customerUrl.add(MAPPING_BOOKING_REQUEST_DELETE);
        customerUrl.add(MAPPING_BOOKING_ALL);
        customerUrl.add(MAPPING_BOOKING_ACCEPT);
        customerUrl.add(MAPPING_BOOKING_SELECT);
        customerUrl.add(MAPPING_BOOKING_SELECT_PARAMETER);
        customerUrl.add(MAPPING_BOOKING_PAY);
        customerUrl.add(MAPPING_BOOKING_CREATE);
        customerUrl.add(MAPPING_BOOKING_SELECT_SORT);
        // Manager access
        managerUrl.addAll(customerUrl);
        managerUrl.add(MAPPING_BOOKING_SELECTION);
        managerUrl.add(MAPPING_BOOKING_INVOICE);
        managerUrl.add(MAPPING_USERS);
        managerUrl.add(MAPPING_USERS_DELETE);
        managerUrl.add(MAPPING_HOTEL_ROOMS_CLASS);
        managerUrl.add(MAPPING_HOTEL_ROOMS_CLASS_DELETE);
        managerUrl.add(MAPPING_HOTEL_ROOMS);
        managerUrl.add(MAPPING_HOTEL_ROOMS_DELETE);
        managerUrl.add(MAPPING_HOTEL_ROOMS_SCHEDULE);
    }

    /**
     * doFilter method for Security filter <br>
     *
     * - checking user authorization by attribute Role in the session
     * - redirect to the login page if it is not available for the user with the current role
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
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpSession session = httpServletRequest.getSession();
        Role role = (Role) session.getAttribute(SESSION_ATTRIBUTE_USER_ROLE);
        if (role == null && allowedUrl.contains(httpServletRequest.getServletPath())) {
            chain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        if (role == null) {
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath()
                    + MAPPING_ENTRY_POINT);
            return;
        }
        if (role.equals(Role.ADMIN)) {
            chain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        if (role.equals(Role.MANAGER) && managerUrl.contains(httpServletRequest.getServletPath())) {
            chain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        if (role.equals(Role.CUSTOMER)
                && customerUrl.contains(httpServletRequest.getServletPath())) {
            chain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + MAPPING_ENTRY_POINT);
    }
}

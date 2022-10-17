package hotel.web.filter;

import static hotel.util.Constant.MAPPING_ALL_PAGES;

import hotel.exception.DataProcessingException;
import hotel.lib.Injector;
import hotel.service.UserService;
import hotel.util.Constant;
import java.io.IOException;
import java.util.Locale;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Filter to set user language
 *
 *  @author Serhii Pankov
 *  @version 1.0
 */
@WebFilter(MAPPING_ALL_PAGES)
public class LanguageFilter implements Filter, Constant {
    private static final Logger logger = LogManager.getLogger(LanguageFilter.class);
    private static final Injector injector = Injector.getInstance(MAIN_PACKAGE_NAME);
    private final UserService userService;

    public LanguageFilter() {
        userService = (UserService) injector
                .getInstance(UserService.class);
    }

    public LanguageFilter(UserService userService) {
        this.userService = userService;
    }

    /**
     * doFilter method for Language filter <br>
     *
     * - getting a language attribute from a session or a parameter from a request
     * - setting the language for the user account
     *
     * @param servletRequest ServletRequest
     * @param servletResponse ServletResponse
     * @param filterChain FilterChain
     * @throws ServletException Signals a Servlet exception
     * @throws IOException Signals an I/O exception.
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        HttpSession session = httpServletRequest.getSession();
        if (session.getAttribute(SESSION_ATTRIBUTE_USER_ID) != null
                && httpServletRequest.getParameter(PARAMETER_LANGUAGE) != null) {
            Long userId = (Long) session.getAttribute(SESSION_ATTRIBUTE_USER_ID);
            String language = httpServletRequest.getParameter(PARAMETER_LANGUAGE);
            try {
                userService.setLanguage(userId, language);
                httpServletRequest.getSession().setAttribute(SESSION_ATTRIBUTE_LANGUAGE, language);
                httpServletResponse.setLocale(Locale.forLanguageTag(language));
                logger.info("The user with id " + userId
                        + " has successfully changed the interface language");
            } catch (DataProcessingException e) {
                logger.warn("Failed to change language for user with id " + userId);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}

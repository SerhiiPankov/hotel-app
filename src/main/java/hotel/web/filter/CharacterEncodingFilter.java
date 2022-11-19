package hotel.web.filter;

import static hotel.util.Constant.MAPPING_ALL_PAGES;

import hotel.util.Constant;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

/**
 * Filter to set character encoding for servlets
 *
 * @author Serhii Pankov
 * @version 1.0
 */
@WebFilter(MAPPING_ALL_PAGES)
public class CharacterEncodingFilter implements Filter, Constant {

    /**
     * doFilter method for CharacterEncoding filter <br>
     *
     * - setting the character encoding for request
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
        servletRequest.setCharacterEncoding(CHARACTER_ENCODING);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}

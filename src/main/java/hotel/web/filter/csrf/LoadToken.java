package hotel.web.filter.csrf;

import static hotel.util.Constant.MAPPING_ALL_PAGES;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * Filter to load tokens to protect against csrf attacks
 *
 * @author Serhii Pankov
 * @version 1.0
 */
@WebFilter(MAPPING_ALL_PAGES)
public class LoadToken implements Filter {

    /**
     * doFilter method to load CSRF tokens <br>
     *
     * - checking for the presence of a session attribute with a list of tokens <br>
     * - adding an attribute with a list of tokens if it was missing <br>
     * - token validity check <br>
     * - checking list of tokens size and deleting old tokens <br>
     * - generating a new token and adding it to the list <br>
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
        List<String> salts;
        if (req.getSession().getAttribute("salts") == null) {
            salts = new LinkedList<>();
            req.getSession().setAttribute("salts", salts);
        } else {
            salts = (List<String>) req.getSession().getAttribute("salts");
            checkSaltsSize(salts);
        }
        String salt = RandomStringUtils.random(20, 0, 0,
                true, true, null, new SecureRandom());
        salts.add(salt);
        req.setAttribute("csrfSalt", salt);
        chain.doFilter(request, response);
    }

    private void checkSaltsSize(List<String> salts) {
        if (salts.size() > 10) {
            for (int i = 0; i < 3; i++) {
                salts.remove(i);
            }
        }
    }
}

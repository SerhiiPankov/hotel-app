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

@WebFilter(MAPPING_ALL_PAGES)
public class LoadToken implements Filter {

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

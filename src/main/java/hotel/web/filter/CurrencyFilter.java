package hotel.web.filter;

import static hotel.util.Constant.MAPPING_ALL_PAGES;

import hotel.exception.DataProcessingException;
import hotel.lib.Injector;
import hotel.model.Currency;
import hotel.service.CurrencyService;
import hotel.util.Constant;
import hotel.util.currency.CurrencyUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
 * Filter to set currencies
 *
 * @author Serhii Pankov
 * @version 1.0
 */
@WebFilter(MAPPING_ALL_PAGES)
public class CurrencyFilter implements Filter, Constant {
    private static final Logger logger = LogManager.getLogger(CurrencyFilter.class);
    private static final Injector injector = Injector.getInstance(MAIN_PACKAGE_NAME);
    private final CurrencyService currencyService = (CurrencyService) injector
            .getInstance(CurrencyService.class);


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        HttpSession session = req.getSession();
        if (session.getAttribute(SESSION_ATTRIBUTE_USER_ID) != null) {
            List<Currency> currencies = new ArrayList<>();
            try {
                currencies = currencyService.getAllCurrencies();
            } catch (DataProcessingException e) {
                logger.warn("Cannot get currencies");
                filterChain.doFilter(req, resp);
                return;
            }
            int systemCurrency;
            if (session.getAttribute(SESSION_ATTRIBUTE_SYSTEM_CURRENCY) == null) {
                systemCurrency = CODE_USD;
            } else {
                systemCurrency = (int) session.getAttribute(SESSION_ATTRIBUTE_SYSTEM_CURRENCY);
            }
            session.setAttribute(SESSION_ATTRIBUTE_CURRENCY,
                    CurrencyUtil.getCurrencySign(systemCurrency));
        }
        filterChain.doFilter(req, resp);
    }
}

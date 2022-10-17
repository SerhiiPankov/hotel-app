package hotel.web.filter;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import hotel.exception.DataProcessingException;
import hotel.service.UserService;
import hotel.util.Constant;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;

class LanguageFilterTest implements Constant {

    @Test
    void doFilter_NullUserId_Ok() throws ServletException, IOException {
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute(SESSION_ATTRIBUTE_USER_ID))
                .thenReturn(null);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        LanguageFilter filter = new LanguageFilter();
        filter.doFilter(request, response, chain);
        verify(session).getAttribute(SESSION_ATTRIBUTE_USER_ID);
        verify(chain).doFilter(request, response);
    }

    @Test
    void doFilter_NoParameter_Ok() throws ServletException, IOException {
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute(SESSION_ATTRIBUTE_USER_ID))
                .thenReturn(1L);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter(PARAMETER_LANGUAGE)).thenReturn(null);
        when(request.getSession()).thenReturn(session);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        LanguageFilter filter = new LanguageFilter();
        filter.doFilter(request, response, chain);
        verify(session).getAttribute(SESSION_ATTRIBUTE_USER_ID);
        verify(chain).doFilter(request, response);
    }

    @Test
    void doFilter_Ok() throws ServletException, IOException, DataProcessingException {
        UserService userService = mock(UserService.class);
        doNothing().when(userService).setLanguage(1L, "uk");
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute(SESSION_ATTRIBUTE_USER_ID))
                .thenReturn(1L);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter(PARAMETER_LANGUAGE)).thenReturn("uk");
        when(request.getSession()).thenReturn(session);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        LanguageFilter filter = new LanguageFilter(userService);
        filter.doFilter(request, response, chain);
        verify(session, times(2))
                .getAttribute(SESSION_ATTRIBUTE_USER_ID);
        verify(request, times(2))
                .getParameter(PARAMETER_LANGUAGE);
        verify(userService).setLanguage(1L, "uk");
        verify(session).setAttribute(SESSION_ATTRIBUTE_LANGUAGE, "uk");
        verify(response).setLocale(Locale.forLanguageTag("uk"));
        verify(chain).doFilter(request, response);
    }

    @Test
    void doFilter_DBException_Ok() throws ServletException, IOException, DataProcessingException {
        UserService userService = mock(UserService.class);
        doThrow(new DataProcessingException("Failed to change language",
                new SQLException()))
                .when(userService).setLanguage(1L, "uk");
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute(SESSION_ATTRIBUTE_USER_ID))
                .thenReturn(1L);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter(PARAMETER_LANGUAGE)).thenReturn("uk");
        when(request.getSession()).thenReturn(session);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        LanguageFilter filter = new LanguageFilter(userService);
        filter.doFilter(request, response, chain);
        verify(session, times(2))
                .getAttribute(SESSION_ATTRIBUTE_USER_ID);
        verify(request, times(2))
                .getParameter(PARAMETER_LANGUAGE);
        verify(userService).setLanguage(1L, "uk");
        verify(chain).doFilter(request, response);
    }
}
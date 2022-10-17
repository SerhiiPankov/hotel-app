package hotel.web.filter;

import hotel.model.enums.Role;
import hotel.util.Constant;
import org.junit.jupiter.api.Test;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityFilterTest implements Constant {

    @Test
    void doFilter_NullRoleAllowedUrl_Ok() throws ServletException, IOException {
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute(SESSION_ATTRIBUTE_USER_ROLE))
                .thenReturn(null);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);
        when(request.getServletPath()).thenReturn(MAPPING_REGISTER);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        SecurityFilter filter = new SecurityFilter();
        FilterConfig filterConfig = mock(FilterConfig.class);
        filter.init(filterConfig);
        filter.doFilter(request, response, chain);
        verify(chain).doFilter(request, response);
    }

    @Test
    void doFilter_NullRoleNotAllowedUrl_NotOk() throws ServletException, IOException {
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute(SESSION_ATTRIBUTE_USER_ROLE))
                .thenReturn(null);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);
        when(request.getServletPath()).thenReturn(MAPPING_BOOKING_PAY);
        when(request.getContextPath()).thenReturn("localhost:8080");
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        SecurityFilter filter = new SecurityFilter();
        FilterConfig filterConfig = mock(FilterConfig.class);
        filter.init(filterConfig);
        filter.doFilter(request, response, chain);
        verify(response).sendRedirect("localhost:8080" + MAPPING_ENTRY_POINT);
    }

    @Test
    void doFilter_AdminRole_Ok() throws ServletException, IOException {
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute(SESSION_ATTRIBUTE_USER_ROLE))
                .thenReturn(Role.ADMIN);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);
        when(request.getServletPath()).thenReturn(MAPPING_HOTEL_ROOMS_DELETE);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        SecurityFilter filter = new SecurityFilter();
        FilterConfig filterConfig = mock(FilterConfig.class);
        filter.init(filterConfig);
        filter.doFilter(request, response, chain);
        verify(chain).doFilter(request, response);
    }

    @Test
    void doFilter_ManagerRoleManagerUrl_Ok() throws ServletException, IOException {
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute(SESSION_ATTRIBUTE_USER_ROLE))
                .thenReturn(Role.MANAGER);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);
        when(request.getServletPath()).thenReturn(MAPPING_HOTEL_ROOMS_CLASS);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        SecurityFilter filter = new SecurityFilter();
        FilterConfig filterConfig = mock(FilterConfig.class);
        filter.init(filterConfig);
        filter.doFilter(request, response, chain);
        verify(chain).doFilter(request, response);
    }

    @Test
    void doFilter_ManagerRoleAllowedUrl_Ok() throws ServletException, IOException {
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute(SESSION_ATTRIBUTE_USER_ROLE))
                .thenReturn(Role.MANAGER);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);
        when(request.getServletPath()).thenReturn(MAPPING_LOGIN);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        SecurityFilter filter = new SecurityFilter();
        FilterConfig filterConfig = mock(FilterConfig.class);
        filter.init(filterConfig);
        filter.doFilter(request, response, chain);
        verify(chain).doFilter(request, response);
    }

    @Test
    void doFilter_CustomerRoleAllowedUrl_Ok() throws ServletException, IOException {
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute(SESSION_ATTRIBUTE_USER_ROLE))
                .thenReturn(Role.CUSTOMER);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);
        when(request.getServletPath()).thenReturn(MAPPING_DO_REGISTER);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        SecurityFilter filter = new SecurityFilter();
        FilterConfig filterConfig = mock(FilterConfig.class);
        filter.init(filterConfig);
        filter.doFilter(request, response, chain);
        verify(chain).doFilter(request, response);
    }

    @Test
    void doFilter_CustomerRoleCustomerUrl_Ok() throws ServletException, IOException {
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute(SESSION_ATTRIBUTE_USER_ROLE))
                .thenReturn(Role.CUSTOMER);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);
        when(request.getServletPath()).thenReturn(MAPPING_BOOKING_REQUEST_ALL);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        SecurityFilter filter = new SecurityFilter();
        FilterConfig filterConfig = mock(FilterConfig.class);
        filter.init(filterConfig);
        filter.doFilter(request, response, chain);
        verify(chain).doFilter(request, response);
    }

    @Test
    void doFilter_CustomerRoleManagerUrl_NotOk() throws ServletException, IOException {
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute(SESSION_ATTRIBUTE_USER_ROLE))
                .thenReturn(Role.CUSTOMER);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);
        when(request.getServletPath()).thenReturn(MAPPING_USERS_DELETE);
        when(request.getContextPath()).thenReturn("localhost:8080");
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        SecurityFilter filter = new SecurityFilter();
        FilterConfig filterConfig = mock(FilterConfig.class);
        filter.init(filterConfig);
        filter.doFilter(request, response, chain);
        verify(response).sendRedirect("localhost:8080" + MAPPING_ENTRY_POINT);
    }
}
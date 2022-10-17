package hotel.web.filter;

import hotel.exception.DataProcessingException;
import hotel.model.enums.PaymentStatus;
import hotel.model.enums.Role;
import hotel.service.BookingService;
import hotel.service.RequestService;
import hotel.util.Constant;
import org.junit.jupiter.api.Test;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NumbersBookingFilterTest implements Constant {

    @Test
    void doFilter_RoleCustomer_Ok() throws DataProcessingException, ServletException, IOException {
        RequestService requestService = mock(RequestService.class);
        when(requestService.getNumberOfBookingRequestsPerCustomer(1L))
                .thenReturn(5);
        BookingService bookingService = mock(BookingService.class);
        when(bookingService.getNumberOfBookingPerCustomer(
                1L, PaymentStatus.PROPOSAL))
                .thenReturn(5);
        when(bookingService.getNumberOfBookingPerCustomer(
                1L, PaymentStatus.INVOICE))
                .thenReturn(5);
        NumbersBookingFilter filter = new NumbersBookingFilter(requestService, bookingService);
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute(SESSION_ATTRIBUTE_USER_ID)).thenReturn(1L);
        when(session.getAttribute(SESSION_ATTRIBUTE_USER_ROLE)).thenReturn(Role.CUSTOMER);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        filter.doFilter(request, response, chain);
        verify(session, times(4))
                .getAttribute(SESSION_ATTRIBUTE_USER_ID);
        verify(session, times(8))
                .getAttribute(SESSION_ATTRIBUTE_USER_ROLE);
        verify(requestService).getNumberOfBookingRequestsPerCustomer(1L);
        verify(bookingService)
                .getNumberOfBookingPerCustomer(1L, PaymentStatus.PROPOSAL);
        verify(bookingService)
                .getNumberOfBookingPerCustomer(1L, PaymentStatus.INVOICE);
        verify(session).setAttribute(SESSION_ATTRIBUTE_NUMBER_OF_REQUEST, 5);
        verify(session).setAttribute(SESSION_ATTRIBUTE_NUMBER_OF_BOOKING, 10);
        verify(session).setAttribute(SESSION_ATTRIBUTE_NUMBER_OF_BOOKING_PROPOSAL, 5);
        verify(session).setAttribute(SESSION_ATTRIBUTE_NUMBER_OF_BOOKING_INVOICE, 5);
        verify(chain).doFilter(request, response);
    }

    @Test
    void doFilter_RoleManager_Ok() throws DataProcessingException, ServletException, IOException {
        RequestService requestService = mock(RequestService.class);
        when(requestService.getNumberOfBookingRequests())
                .thenReturn(5);
        BookingService bookingService = mock(BookingService.class);
        when(bookingService.getNumberOfBooking(PaymentStatus.WAIT))
                .thenReturn(5);
        NumbersBookingFilter filter = new NumbersBookingFilter(requestService, bookingService);
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute(SESSION_ATTRIBUTE_USER_ID)).thenReturn(1L);
        when(session.getAttribute(SESSION_ATTRIBUTE_USER_ROLE)).thenReturn(Role.MANAGER);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        filter.doFilter(request, response, chain);
        verify(session, times(2))
                .getAttribute(SESSION_ATTRIBUTE_USER_ID);
        verify(session, times(7))
                .getAttribute(SESSION_ATTRIBUTE_USER_ROLE);
        verify(requestService).getNumberOfBookingRequests();
        verify(bookingService)
                .getNumberOfBooking(PaymentStatus.WAIT);
        verify(session).setAttribute(SESSION_ATTRIBUTE_NUMBER_OF_REQUEST, 5);
        verify(session).setAttribute(SESSION_ATTRIBUTE_NUMBER_OF_BOOKING, 5);
        verify(chain).doFilter(request, response);
    }

    @Test
    void doFilter_DBException_NotOk() throws DataProcessingException, ServletException, IOException {
        RequestService requestService = mock(RequestService.class);
        when(requestService.getNumberOfBookingRequests())
                .thenThrow(new DataProcessingException("Can't get parameter from DB",
                        new SQLException()));
        BookingService bookingService = mock(BookingService.class);
        NumbersBookingFilter filter = new NumbersBookingFilter(requestService, bookingService);
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute(SESSION_ATTRIBUTE_USER_ID)).thenReturn(1L);
        when(session.getAttribute(SESSION_ATTRIBUTE_USER_ROLE)).thenReturn(Role.MANAGER);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        filter.doFilter(request, response, chain);
        verify(session, times(1))
                .getAttribute(SESSION_ATTRIBUTE_USER_ID);
        verify(session, times(3))
                .getAttribute(SESSION_ATTRIBUTE_USER_ROLE);
        verify(requestService).getNumberOfBookingRequests();
        verify(chain).doFilter(request, response);
    }
}
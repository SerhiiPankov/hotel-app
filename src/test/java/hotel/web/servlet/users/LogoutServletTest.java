package hotel.web.servlet.users;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import hotel.util.Constant;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class LogoutServletTest implements Constant {
    @Test
    void doGet_Ok() throws ServletException, IOException {
        LogoutServlet errorPageController = new LogoutServlet();
        HttpSession httpSession = mock(HttpSession.class);
        when(httpSession.getAttribute(SESSION_ATTRIBUTE_USER_ID))
                .thenReturn(1L);
        doNothing().when(httpSession).invalidate();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getContextPath())
                .thenReturn("localhost:8080");
        when(request.getSession())
                .thenReturn(httpSession);
        errorPageController.doGet(request, response);
        verify(request, times(2))
                .getSession();
        verify(httpSession, times(1))
                .getAttribute(SESSION_ATTRIBUTE_USER_ID);
        verify(httpSession, times(1))
                .invalidate();
        verify(response).sendRedirect("localhost:8080" + MAPPING_ENTRY_POINT);
    }
}
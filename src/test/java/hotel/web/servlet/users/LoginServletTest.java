package hotel.web.servlet.users;

import hotel.util.Constant;
import org.junit.jupiter.api.Test;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.Mockito.*;

class LoginServletTest implements Constant {

    @Test
    void doGet_AuthorizedUser_Ok() throws IOException, ServletException {
        LoginServlet controller = new LoginServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession httpSession = mock(HttpSession.class);
        when(request.getSession())
                .thenReturn(httpSession);
        when(httpSession.getAttribute(SESSION_ATTRIBUTE_USER_ID))
                .thenReturn(1L);
        when(request.getContextPath())
                .thenReturn("localhost:8080");
        controller.doGet(request, response);
        verify(request).getSession();
        verify(response).sendRedirect("localhost:8080" + MAPPING_ENTRY_POINT);
    }

    @Test
    void doGet_UnauthorizedUser_Ok() throws ServletException, IOException {
        LoginServlet controller = new LoginServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        HttpSession httpSession = mock(HttpSession.class);
        when(request.getSession())
                .thenReturn(httpSession);
        when(httpSession.getAttribute(SESSION_ATTRIBUTE_USER_ID))
                .thenReturn(null);
        when(request.getRequestDispatcher(JSP_LOGIN))
                .thenReturn(requestDispatcher);
        controller.doGet(request, response);
        verify(request).getSession();
        verify(request).getRequestDispatcher(JSP_LOGIN);
        verify(requestDispatcher).forward(request, response);
    }
}

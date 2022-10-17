package hotel.web.servlet;

import hotel.util.Constant;
import org.junit.jupiter.api.Test;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static org.mockito.Mockito.*;

class ErrorPageServletTest implements Constant {

    @Test
    void doGet_Ok() throws ServletException, IOException {
        ErrorPageServlet controller = new ErrorPageServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(JSP_ERROR_PAGE))
                .thenReturn(requestDispatcher);
        controller.doGet(request, response);
        verify(request, times(1))
                .getRequestDispatcher(JSP_ERROR_PAGE);
        verify(request, never())
                .getSession();
        verify(requestDispatcher)
                .forward(request, response);
    }
}

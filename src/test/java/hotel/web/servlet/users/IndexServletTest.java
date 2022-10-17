package hotel.web.servlet.users;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import hotel.util.Constant;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hotel.web.servlet.users.IndexServlet;
import org.junit.jupiter.api.Test;

class IndexServletTest implements Constant {

    @Test
    void doGet_Ok() throws ServletException, IOException {
        IndexServlet controller = new IndexServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(JSP_INDEX))
                .thenReturn(requestDispatcher);
        controller.doGet(request, response);
        verify(request, times(1))
                .getRequestDispatcher(JSP_INDEX);
        verify(request, never())
                .getSession();
        verify(requestDispatcher)
                .forward(request, response);
    }
}
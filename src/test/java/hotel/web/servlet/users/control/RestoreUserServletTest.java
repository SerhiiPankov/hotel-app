package hotel.web.servlet.users.control;

import hotel.exception.DataProcessingException;
import hotel.service.UserService;
import hotel.util.Constant;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class RestoreUserServletTest implements Constant {

    @Test
    void doPost_Ok() throws DataProcessingException, IOException {
        UserService userService = mock(UserService.class);
        doNothing().when(userService).restore(1L);
        RestoreUserServlet controller =
                new RestoreUserServlet(userService);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter(PARAMETER_USER_ID))
                .thenReturn("1");
        when(request.getContextPath())
                .thenReturn("localhost:8080");
        HttpServletResponse response = mock(HttpServletResponse.class);
        controller.doPost(request, response);
        verify(userService).restore(1L);
        verify(response)
                .sendRedirect("localhost:8080" + MAPPING_USERS);
    }

    @Test
    void doPost_WithoutParameter_NotOk() throws IOException {
        RestoreUserServlet controller =
                new RestoreUserServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter(PARAMETER_USER_ID))
                .thenReturn("");
        when(request.getContextPath())
                .thenReturn("localhost:8080");
        HttpServletResponse response = mock(HttpServletResponse.class);
        controller.doPost(request, response);
        verify(response)
                .sendRedirect("localhost:8080" + MAPPING_ERROR_PAGE + "?"
                        + PARAMETER_MESSAGE + "=" + NO_PARAMETER_ERROR_MSG);
    }

    @Test
    void doPost_DBExceptionUserService_NotOk() throws DataProcessingException, IOException {
        UserService userService = mock(UserService.class);
        doThrow(new DataProcessingException("Can't get user", new SQLException()))
                .when(userService).restore(1L);
        RestoreUserServlet controller =
                new RestoreUserServlet(userService);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter(PARAMETER_USER_ID))
                .thenReturn("1");
        when(request.getContextPath())
                .thenReturn("localhost:8080");
        HttpServletResponse response = mock(HttpServletResponse.class);
        controller.doPost(request, response);
        verify(userService).restore(1L);
        verify(response)
                .sendRedirect("localhost:8080" + MAPPING_USERS + "?"
                        + PARAMETER_MESSAGE + "=" + ACCESS_ERROR_MSG);
    }
}
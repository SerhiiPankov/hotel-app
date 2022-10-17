package hotel.web.servlet.users.account;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import hotel.exception.DataProcessingException;
import hotel.exception.WrongDataException;
import hotel.service.AuthenticationService;
import hotel.service.UserService;
import hotel.util.Constant;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;

class DeleteUserAccountServletTest implements Constant {

    @Test
    void doPost_Ok() throws IOException, WrongDataException, DataProcessingException {
        UserService userService = mock(UserService.class);
        doNothing().when(userService).delete(1L);
        AuthenticationService authenticationService = mock(AuthenticationService.class);
        when(authenticationService.checkPassword(1L, "1234"))
                .thenReturn(true);
        DeleteUserAccountServlet controller =
                new DeleteUserAccountServlet(userService, authenticationService);
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute(SESSION_ATTRIBUTE_USER_ID))
                .thenReturn(1L);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession())
                .thenReturn(session);
        when(request.getParameter(PARAMETER_USER_PASSWORD_DELETE))
                .thenReturn("1234");
        when(request.getContextPath())
                .thenReturn("localhost:8080");
        HttpServletResponse response = mock(HttpServletResponse.class);
        controller.doPost(request, response);
        verify(userService).delete(1L);
        verify(response)
                .sendRedirect("localhost:8080" + MAPPING_LOGOUT);
    }

    @Test
    void doPost_WithoutParameter_NotOk() throws IOException {
        DeleteUserAccountServlet controller = new DeleteUserAccountServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter(PARAMETER_USER_PASSWORD_DELETE))
                .thenReturn(null);
        when(request.getContextPath())
                .thenReturn("localhost:8080");
        HttpServletResponse response = mock(HttpServletResponse.class);
        controller.doPost(request, response);
        verify(response)
                .sendRedirect("localhost:8080" + MAPPING_ERROR_PAGE + "?"
                        + PARAMETER_MESSAGE + "=" + NO_PARAMETER_ERROR_MSG);
    }

    @Test
    void doPost_WrongPassword_NotOk() throws DataProcessingException, WrongDataException, IOException {
        UserService userService = mock(UserService.class);
        AuthenticationService authenticationService = mock(AuthenticationService.class);
        when(authenticationService.checkPassword(1L, "1234"))
                .thenReturn(false);
        DeleteUserAccountServlet controller =
                new DeleteUserAccountServlet(userService, authenticationService);
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute(SESSION_ATTRIBUTE_USER_ID))
                .thenReturn(1L);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession())
                .thenReturn(session);
        when(request.getParameter(PARAMETER_USER_PASSWORD_DELETE))
                .thenReturn("1234");
        when(request.getContextPath())
                .thenReturn("localhost:8080");
        HttpServletResponse response = mock(HttpServletResponse.class);
        controller.doPost(request, response);
        verify(response)
                .sendRedirect("localhost:8080" + MAPPING_USERS_ACCOUNT + "?"
                        + PARAMETER_MESSAGE + "=" + NO_VALID_PASSWORD_ERROR_MSG);
    }

    @Test
    void doPost_DBExceptionAuthenticationService_NotOK() throws DataProcessingException, WrongDataException, ServletException, IOException {
        UserService userService = mock(UserService.class);
        AuthenticationService authenticationService = mock(AuthenticationService.class);
        when(authenticationService.checkPassword(1L, "1234"))
                .thenThrow(new DataProcessingException("Can't get user", new SQLException()));
        DeleteUserAccountServlet controller =
                new DeleteUserAccountServlet(userService, authenticationService);
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute(SESSION_ATTRIBUTE_USER_ID))
                .thenReturn(1L);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession())
                .thenReturn(session);
        when(request.getParameter(PARAMETER_USER_PASSWORD_DELETE))
                .thenReturn("1234");
        when(request.getContextPath())
                .thenReturn("localhost:8080");
        HttpServletResponse response = mock(HttpServletResponse.class);
        controller.doPost(request, response);
        verify(response)
                .sendRedirect("localhost:8080" + MAPPING_USERS_ACCOUNT + "?"
                        + PARAMETER_MESSAGE + "=" + ACCESS_ERROR_MSG);
    }

    @Test
    void doPost_DBExceptionUserService_NotOK() throws WrongDataException, DataProcessingException, IOException {
        UserService userService = mock(UserService.class);
        doThrow(new DataProcessingException("Can't get user", new SQLException()))
                .when(userService).delete(1L);
        AuthenticationService authenticationService = mock(AuthenticationService.class);
        when(authenticationService.checkPassword(1L, "1234"))
                .thenReturn(true);
        DeleteUserAccountServlet controller =
                new DeleteUserAccountServlet(userService, authenticationService);
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute(SESSION_ATTRIBUTE_USER_ID))
                .thenReturn(1L);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession())
                .thenReturn(session);
        when(request.getParameter(PARAMETER_USER_PASSWORD_DELETE))
                .thenReturn("1234");
        when(request.getContextPath())
                .thenReturn("localhost:8080");
        HttpServletResponse response = mock(HttpServletResponse.class);
        controller.doPost(request, response);
        verify(response)
                .sendRedirect("localhost:8080" + MAPPING_USERS_ACCOUNT + "?"
                        + PARAMETER_MESSAGE + "=" + ACCESS_ERROR_MSG);
    }
}
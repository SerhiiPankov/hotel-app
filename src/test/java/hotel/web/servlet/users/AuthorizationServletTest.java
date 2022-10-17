package hotel.web.servlet.users;

import hotel.exception.AuthenticationException;
import hotel.exception.DataProcessingException;
import hotel.exception.WrongDataException;
import hotel.model.User;
import hotel.model.enums.Role;
import hotel.service.AuthenticationService;
import hotel.util.Constant;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class AuthorizationServletTest implements Constant {
    private final User bob = new User.Builder("bob@gmail.com")
            .setId(1L)
            .setPassword("1234")
            .setSalt(new byte[]{1, 2, 3, 4, 5})
            .setName("Bob")
            .setPhone("+380 (66) 666-66-66")
            .setRole(Role.MANAGER)
            .setLanguage("en")
            .build();

    @Test
    void doPost_Ok() throws AuthenticationException, WrongDataException, DataProcessingException, ServletException, IOException {
        AuthenticationService authenticationService = mock(AuthenticationService.class);
        when(authenticationService.login("bob@gmail.com","1234"))
                .thenReturn(bob);
        AuthorizationServlet controller = new AuthorizationServlet(authenticationService);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter(PARAMETER_EMAIL))
                .thenReturn("bob@gmail.com");
        when(request.getParameter(PARAMETER_PASSWORD))
                .thenReturn("1234");
        HttpSession httpSession = mock(HttpSession.class);
        when(request.getSession())
                .thenReturn(httpSession);
        when(request.getContextPath())
                .thenReturn("localhost:8080");
        controller.doPost(request, response);
        verify(request).getParameter(PARAMETER_EMAIL);
        verify(request).getParameter(PARAMETER_PASSWORD);
        verify(authenticationService).login("bob@gmail.com", "1234");
        verify(request, times(4)).getSession();
        verify(httpSession).setAttribute(SESSION_ATTRIBUTE_USER_ID, bob.getId());
        verify(httpSession).setAttribute(SESSION_ATTRIBUTE_USER_ROLE, bob.getRole());
        verify(httpSession).setAttribute(SESSION_ATTRIBUTE_USER_NAME, bob.getName());
        verify(httpSession).setAttribute(SESSION_ATTRIBUTE_LANGUAGE, bob.getLanguage());
        verify(response).sendRedirect("localhost:8080" + MAPPING_ENTRY_POINT);
    }

    @Test
    void doPost_NoParameter_NotOk() throws AuthenticationException, WrongDataException, DataProcessingException, ServletException, IOException {
        AuthorizationServlet controller = new AuthorizationServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter(PARAMETER_EMAIL))
                .thenReturn("");
        when(request.getContextPath())
                .thenReturn("localhost:8080");
        controller.doPost(request, response);
        verify(response)
                .sendRedirect("localhost:8080" + MAPPING_ERROR_PAGE + "?"
                        + PARAMETER_MESSAGE + "=" + NO_PARAMETER_ERROR_MSG);
    }

    @Test
    void doPost_AuthoriseException_NotOk() throws AuthenticationException, WrongDataException, DataProcessingException, ServletException, IOException {
        AuthenticationService authenticationService = mock(AuthenticationService.class);
        when(authenticationService.login("bob@gmail.com","1234"))
                .thenThrow(new AuthenticationException("User not authorized"));
        AuthorizationServlet controller = new AuthorizationServlet(authenticationService);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter(PARAMETER_EMAIL))
                .thenReturn("bob@gmail.com");
        when(request.getParameter(PARAMETER_PASSWORD))
                .thenReturn("1234");
        when(request.getContextPath())
                .thenReturn("localhost:8080");
        controller.doPost(request, response);
        verify(authenticationService).login("bob@gmail.com", "1234");
        verify(response).sendRedirect("localhost:8080" + MAPPING_LOGIN + "?"
                + PARAMETER_MESSAGE + "=" + AUTH_ERROR_MSG);
    }

    @Test
    void doPost_AuthenticateException_NotOk() throws AuthenticationException, WrongDataException, DataProcessingException, ServletException, IOException {
        AuthenticationService authenticationService = mock(AuthenticationService.class);
        when(authenticationService.login("bob@gmail.com","1234"))
                .thenThrow(new WrongDataException("User not found in database"));
        AuthorizationServlet controller = new AuthorizationServlet(authenticationService);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter(PARAMETER_EMAIL))
                .thenReturn("bob@gmail.com");
        when(request.getParameter(PARAMETER_PASSWORD))
                .thenReturn("1234");
        when(request.getContextPath())
                .thenReturn("localhost:8080");
        controller.doPost(request, response);
        verify(authenticationService).login("bob@gmail.com", "1234");
        verify(response).sendRedirect("localhost:8080" + MAPPING_REGISTER + "?"
                + PARAMETER_MESSAGE + "=" + REGISTER_ERROR_MSG);
    }

    @Test
    void doPost_DBException_NotOk() throws AuthenticationException, WrongDataException, DataProcessingException, ServletException, IOException {
        AuthenticationService authenticationService = mock(AuthenticationService.class);
        when(authenticationService.login("bob@gmail.com","1234"))
                .thenThrow(new DataProcessingException("User not authorized", new SQLException()));
        AuthorizationServlet controller = new AuthorizationServlet(authenticationService);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter(PARAMETER_EMAIL))
                .thenReturn("bob@gmail.com");
        when(request.getParameter(PARAMETER_PASSWORD))
                .thenReturn("1234");
        when(request.getContextPath())
                .thenReturn("localhost:8080");
        controller.doPost(request, response);
        verify(authenticationService).login("bob@gmail.com", "1234");
        verify(response).sendRedirect("localhost:8080" + MAPPING_LOGIN + "?"
                + PARAMETER_MESSAGE + "=" + ACCESS_ERROR_MSG);
    }
}
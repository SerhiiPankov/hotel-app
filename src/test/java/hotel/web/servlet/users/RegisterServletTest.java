package hotel.web.servlet.users;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import hotel.exception.DataProcessingException;
import hotel.model.User;
import hotel.model.enums.Role;
import hotel.service.AuthenticationService;
import hotel.util.Constant;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;

class RegisterServletTest implements Constant {
    private final User bob = new User.Builder("bob@gmail.com")
            .setId(1L)
            .setPassword("1234")
            .setSalt(new byte[]{1, 2, 3, 4, 5})
            .setName("Bob")
            .setPhone("+380 (66) 666-66-66")
            .setRole(Role.MANAGER)
            .setLanguage("en")
            .build();
    private final User alice = new User.Builder("alice@gmail.com")
            .setId(2L)
            .setPassword("4321")
            .setSalt(new byte[]{5, 4, 3, 2, 1})
            .setName("Alice")
            .setPhone("+380 (77) 777-77-77")
            .setRole(Role.CUSTOMER)
            .setLanguage("en")
            .build();
    private final User firstUser = new User.Builder("bob@gmail.com")
            .setPassword("1234")
            .setName("Bob")
            .setPhone("+380 (66) 666-66-66")
            .setRole(Role.MANAGER)
            .build();
    private final User secondUser = new User.Builder("alice@gmail.com")
            .setPassword("4321")
            .setName("Alice")
            .setPhone("+380 (77) 777-77-77")
            .setRole(Role.CUSTOMER)
            .build();

    @Test
    void doGet() throws ServletException, IOException {
        RegisterServlet errorPageController = new RegisterServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(JSP_REGISTER))
                .thenReturn(requestDispatcher);
        errorPageController.doGet(request, response);
        verify(request, times(1))
                .getRequestDispatcher(JSP_REGISTER);
        verify(request, never())
                .getSession();
        verify(requestDispatcher)
                .forward(request, response);
    }

    @Test
    void doPost_RoleAdmin_Ok() throws DataProcessingException, ServletException, IOException {
        AuthenticationService authenticationService = mock(AuthenticationService.class);
        when(authenticationService.register(firstUser))
                .thenReturn(bob);
        AuthenticationServlet controller = new AuthenticationServlet(authenticationService);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter(PARAMETER_NEW_USER_EMAIL))
                .thenReturn("bob@gmail.com");
        when(request.getParameter(PARAMETER_NEW_USER_PASSWORD))
                .thenReturn("1234");
        when(request.getParameter(PARAMETER_NEW_USER_PASSWORD_REPEAT))
                .thenReturn("1234");
        when(request.getParameter(PARAMETER_NEW_USER_NAME))
                .thenReturn("Bob");
        when(request.getParameter(PARAMETER_NEW_USER_COUNTRY_CODE))
                .thenReturn("380");
        when(request.getParameter(PARAMETER_NEW_USER_PHONE_NUMBER))
                .thenReturn("666666666");
        HttpSession httpSession = mock(HttpSession.class);
        when(httpSession.getAttribute(SESSION_ATTRIBUTE_USER_ROLE))
                .thenReturn(Role.ADMIN);
        when(request.getSession())
                .thenReturn(httpSession);
        when(request.getContextPath())
                .thenReturn("localhost:8080");
        controller.doPost(request, response);
        verify(request, times(1))
                .getParameter(PARAMETER_NEW_USER_EMAIL);
        verify(request, times(1))
                .getParameter(PARAMETER_NEW_USER_PASSWORD);
        verify(request, times(1))
                .getParameter(PARAMETER_NEW_USER_PASSWORD_REPEAT);
        verify(request, times(1))
                .getParameter(PARAMETER_NEW_USER_NAME);
        verify(request, times(1))
                .getParameter(PARAMETER_NEW_USER_COUNTRY_CODE);
        verify(request, times(1))
                .getParameter(PARAMETER_NEW_USER_PHONE_NUMBER);
        verify(request, times(1))
                .getSession();
        verify(httpSession, times(1))
                .getAttribute(SESSION_ATTRIBUTE_USER_ROLE);
        verify(authenticationService, times(1))
                .register(firstUser);
        verify(response, times(1))
                .sendRedirect("localhost:8080" + MAPPING_USERS);
    }

    @Test
    void doPost_RoleCustomer_Ok() throws DataProcessingException, ServletException, IOException {
        AuthenticationService authenticationService = mock(AuthenticationService.class);
        when(authenticationService.register(secondUser))
                .thenReturn(alice);
        AuthenticationServlet controller = new AuthenticationServlet(authenticationService);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter(PARAMETER_NEW_USER_EMAIL))
                .thenReturn("alice@gmail.com");
        when(request.getParameter(PARAMETER_NEW_USER_PASSWORD))
                .thenReturn("4321");
        when(request.getParameter(PARAMETER_NEW_USER_PASSWORD_REPEAT))
                .thenReturn("4321");
        when(request.getParameter(PARAMETER_NEW_USER_NAME))
                .thenReturn("Alice");
        when(request.getParameter(PARAMETER_NEW_USER_COUNTRY_CODE))
                .thenReturn("380");
        when(request.getParameter(PARAMETER_NEW_USER_PHONE_NUMBER))
                .thenReturn("777777777");
        HttpSession httpSession = mock(HttpSession.class);
        when(httpSession.getAttribute(SESSION_ATTRIBUTE_USER_ROLE))
                .thenReturn(Role.CUSTOMER);
        when(request.getSession())
                .thenReturn(httpSession);
        when(request.getContextPath())
                .thenReturn("localhost:8080");
        controller.doPost(request, response);
        verify(request, times(1))
                .getSession();
        verify(authenticationService, times(1))
                .register(secondUser);
        verify(response, times(1))
                .sendRedirect("localhost:8080" + MAPPING_REGISTER + "?"
                        + PARAMETER_MESSAGE + "=" + REGISTER_OK_MSG);
    }

    @Test
    void doPost_NoParameter_NotOk() throws ServletException, IOException {
        AuthenticationServlet controller = new AuthenticationServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter(PARAMETER_NEW_USER_EMAIL))
                .thenReturn("");
        when(request.getContextPath())
                .thenReturn("localhost:8080");
        controller.doPost(request, response);
        verify(response)
                .sendRedirect("localhost:8080" + MAPPING_ERROR_PAGE + "?"
                        + PARAMETER_MESSAGE + "=" + NO_PARAMETER_ERROR_MSG);
    }

    @Test
    void doPost_DBException_NotOk() throws DataProcessingException, ServletException, IOException {
        AuthenticationService authenticationService = mock(AuthenticationService.class);
        when(authenticationService.register(firstUser)).thenThrow(
                new DataProcessingException("Can't register user", new SQLException()));
        AuthenticationServlet controller = new AuthenticationServlet(authenticationService);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter(PARAMETER_NEW_USER_EMAIL))
                .thenReturn("bob@gmail.com");
        when(request.getParameter(PARAMETER_NEW_USER_PASSWORD))
                .thenReturn("1234");
        when(request.getParameter(PARAMETER_NEW_USER_PASSWORD_REPEAT))
                .thenReturn("1234");
        when(request.getParameter(PARAMETER_NEW_USER_NAME))
                .thenReturn("Bob");
        when(request.getParameter(PARAMETER_NEW_USER_COUNTRY_CODE))
                .thenReturn("380");
        when(request.getParameter(PARAMETER_NEW_USER_PHONE_NUMBER))
                .thenReturn("666666666");
        HttpSession httpSession = mock(HttpSession.class);
        when(httpSession.getAttribute(SESSION_ATTRIBUTE_USER_ROLE))
                .thenReturn(Role.ADMIN);
        when(request.getSession())
                .thenReturn(httpSession);
        when(request.getContextPath())
                .thenReturn("localhost:8080");
        controller.doPost(request, response);
        verify(request)
                .getSession();
        verify(authenticationService)
                .register(firstUser);
        verify(response, times(1))
                .sendRedirect("localhost:8080" + MAPPING_REGISTER + "?"
                        + PARAMETER_MESSAGE + "=" + ACCESS_ERROR_MSG + "&"
                        + PARAMETER_EMAIL + "=" + "bob@gmail.com");
    }

    @Test
    void doPost_NoMatchPasswords_NotOk() throws DataProcessingException, ServletException, IOException {
        AuthenticationServlet controller = new AuthenticationServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter(PARAMETER_NEW_USER_EMAIL))
                .thenReturn("bob@gmail.com");
        when(request.getParameter(PARAMETER_NEW_USER_PASSWORD))
                .thenReturn("1234");
        when(request.getParameter(PARAMETER_NEW_USER_PASSWORD_REPEAT))
                .thenReturn("12345");
        when(request.getParameter(PARAMETER_NEW_USER_NAME))
                .thenReturn("Bob");
        when(request.getParameter(PARAMETER_NEW_USER_COUNTRY_CODE))
                .thenReturn("380");
        when(request.getParameter(PARAMETER_NEW_USER_PHONE_NUMBER))
                .thenReturn("666666666");
        HttpSession httpSession = mock(HttpSession.class);
        when(httpSession.getAttribute(SESSION_ATTRIBUTE_USER_ROLE))
                .thenReturn(Role.ADMIN);
        when(request.getSession())
                .thenReturn(httpSession);
        when(request.getContextPath())
                .thenReturn("localhost:8080");
        controller.doPost(request, response);
        verify(response, times(1))
                .sendRedirect("localhost:8080" + MAPPING_REGISTER + "?"
                        + PARAMETER_MESSAGE + "=" + NO_MATCH_PASSWORDS_ERROR_MSG);
    }
}

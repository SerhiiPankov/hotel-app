package hotel.web.servlet.users.account;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import hotel.exception.DataProcessingException;
import hotel.exception.WrongDataException;
import hotel.service.UserService;
import hotel.model.User;
import hotel.model.enums.Role;
import hotel.util.Constant;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import hotel.web.servlet.users.account.UserAccountServlet;
import org.junit.jupiter.api.Test;

class UserAccountServletTest implements Constant {
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
    void doGet_Ok() throws ServletException, IOException, WrongDataException, DataProcessingException {
        UserService userService = mock(UserService.class);
        when(userService.get(1L)).thenReturn(bob);
        UserAccountServlet controller = new UserAccountServlet(userService);
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute(SESSION_ATTRIBUTE_USER_ID))
                .thenReturn(1L);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession())
                .thenReturn(session);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(JSP_USERS_ACCOUNT))
                .thenReturn(requestDispatcher);
        controller.doGet(request, response);
        verify(request, times(1))
                .getRequestDispatcher(JSP_USERS_ACCOUNT);
        verify(request, times(1))
                .getSession();
        verify(request, times(1))
                .setAttribute(ATTRIBUTE_ACCOUNT_USER_EMAIL, "bob@gmail.com");
        verify(request, times(1))
                .setAttribute(ATTRIBUTE_ACCOUNT_USER_NAME, "Bob");
        verify(request, times(1))
                .setAttribute(ATTRIBUTE_ACCOUNT_USER_COUNTRY_CODE, "380");
        verify(request, times(1))
                .setAttribute(ATTRIBUTE_ACCOUNT_USER_PHONE_NUMBER, "666666666");
        verify(requestDispatcher)
                .forward(request, response);
    }

    @Test
    void doGet_DBException_NotOk() throws WrongDataException, DataProcessingException, ServletException, IOException {
        UserService userService = mock(UserService.class);
        when(userService.get(5L))
                .thenThrow(new WrongDataException("Can't get user"),
                        new DataProcessingException("Can't get user", new SQLException()));
        UserAccountServlet userAccountServlet = new UserAccountServlet(userService);
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute(SESSION_ATTRIBUTE_USER_ID))
                .thenReturn(5L);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession())
                .thenReturn(session);
        when(request.getContextPath())
                .thenReturn("localhost:8080");
        HttpServletResponse response = mock(HttpServletResponse.class);
        userAccountServlet.doGet(request, response);
        verify(response)
                .sendRedirect("localhost:8080" + MAPPING_ERROR_PAGE + "?"
                        + PARAMETER_MESSAGE + "=" + ACCESS_ERROR_MSG);
    }
}
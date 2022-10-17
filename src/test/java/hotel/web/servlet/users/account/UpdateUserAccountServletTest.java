package hotel.web.servlet.users.account;

import hotel.exception.DataProcessingException;
import hotel.exception.WrongDataException;
import hotel.model.User;
import hotel.model.enums.Role;
import hotel.service.AuthenticationService;
import hotel.service.UserService;
import hotel.util.Constant;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import static org.mockito.Mockito.*;

class UpdateUserAccountServletTest implements Constant {
    private final User bob = new User.Builder("bob@gmail.com")
            .setId(1L)
            .setPassword("1234")
            .setSalt(new byte[]{1, 2, 3, 4, 5})
            .setName("Bob")
            .setPhone("+380 (66) 666-66-66")
            .setRole(Role.MANAGER)
            .setLanguage("en")
            .build();
    private final User updatedBob = new User.Builder("newbob@gmail.com")
            .setId(1L)
            .setPassword("12345")
            .setName("Bob Didrikson")
            .setPhone("+380 (66) 777-77-77")
            .setRole(Role.MANAGER)
            .setLanguage("en")
            .build();

    @Test
    void doPost_Ok()
            throws WrongDataException, DataProcessingException, ServletException, IOException {
        UserService userService = mock(UserService.class);
        when(userService.get(1L))
                .thenReturn(bob);
        when(userService.update(updatedBob))
                .thenReturn(updatedBob);
        AuthenticationService authenticationService = mock(AuthenticationService.class);
        when(authenticationService.checkPassword(1L, "1234"))
                .thenReturn(true);
        UpdateUserAccountServlet controller =
                new UpdateUserAccountServlet(authenticationService, userService);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter(PARAMETER_UPDATE_FORM_PASSWORD))
                .thenReturn("1234");
        when(request.getParameter(PARAMETER_USER_EMAIL))
                .thenReturn("newbob@gmail.com");
        when(request.getParameter(PARAMETER_USER_NAME))
                .thenReturn("Bob Didrikson");
        when(request.getParameter(PARAMETER_USER_PHONE_NUMBER))
                .thenReturn("667777777");
        when(request.getParameter(PARAMETER_USER_COUNTRY_CODE))
                .thenReturn("380");
        when(request.getParameter(PARAMETER_NEW_PASSWORD_UPDATE))
                .thenReturn("12345");
        when(request.getParameter(PARAMETER_NEW_PASSWORD_REPEAT_UPDATE))
                .thenReturn("12345");
        HttpSession httpSession = mock(HttpSession.class);
        when(httpSession.getAttribute(SESSION_ATTRIBUTE_USER_ID))
                .thenReturn(1L);
        when(request.getSession())
                .thenReturn(httpSession);
        when(request.getContextPath())
                .thenReturn("localhost:8080");
        controller.doPost(request, response);
        verify(userService).get(1L);
        verify(userService).update(updatedBob);
        verify(authenticationService).checkPassword(1L, "1234");
        verify(request).getParameter(PARAMETER_UPDATE_FORM_PASSWORD);
        verify(request).getParameter(PARAMETER_USER_EMAIL);
        verify(request).getParameter(PARAMETER_USER_NAME);
        verify(request).getParameter(PARAMETER_USER_PHONE_NUMBER);
        verify(request).getParameter(PARAMETER_USER_COUNTRY_CODE);
        verify(request).getParameter(PARAMETER_NEW_PASSWORD_UPDATE);
        verify(request).getParameter(PARAMETER_NEW_PASSWORD_REPEAT_UPDATE);
        verify(request, times(2)).getSession();
        verify(httpSession).getAttribute(SESSION_ATTRIBUTE_USER_ID);
        verify(httpSession).setAttribute(SESSION_ATTRIBUTE_USER_NAME, "Bob Didrikson");
        verify(response).sendRedirect("localhost:8080" + MAPPING_USERS_ACCOUNT);
    }

    @Test
    void doPost_NotMatchNewPasswords_NotOk()
            throws WrongDataException, DataProcessingException, ServletException, IOException {
        UserService userService = mock(UserService.class);
        when(userService.get(1L)).thenReturn(bob);
        when(userService.update(updatedBob)).thenReturn(updatedBob);
        AuthenticationService authenticationService = mock(AuthenticationService.class);
        when(authenticationService.checkPassword(1L, "1234"))
                .thenReturn(true);
        UpdateUserAccountServlet controller =
                new UpdateUserAccountServlet(authenticationService, userService);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter(PARAMETER_UPDATE_FORM_PASSWORD))
                .thenReturn("1234");
        when(request.getParameter(PARAMETER_USER_EMAIL))
                .thenReturn("newbob@gmail.com");
        when(request.getParameter(PARAMETER_USER_NAME))
                .thenReturn("Bob Didrikson");
        when(request.getParameter(PARAMETER_USER_PHONE_NUMBER))
                .thenReturn("667777777");
        when(request.getParameter(PARAMETER_USER_COUNTRY_CODE))
                .thenReturn("380");
        when(request.getParameter(PARAMETER_NEW_PASSWORD_UPDATE))
                .thenReturn("1234");
        when(request.getParameter(PARAMETER_NEW_PASSWORD_REPEAT_UPDATE))
                .thenReturn("1235");
        HttpSession httpSession = mock(HttpSession.class);
        when(request.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute(SESSION_ATTRIBUTE_USER_ID))
                .thenReturn(1L);
        when(request.getContextPath()).thenReturn("localhost:8080");
        controller.doPost(request, response);
        verify(response).sendRedirect("localhost:8080" + MAPPING_USERS_ACCOUNT + "?"
                        + PARAMETER_MESSAGE + "=" + NO_MATCH_PASSWORDS_ERROR_MSG);
    }

    @Test
    void doPost_NoParameter_Ok()
            throws WrongDataException, DataProcessingException, ServletException, IOException {
        UpdateUserAccountServlet controller =
                new UpdateUserAccountServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter(PARAMETER_UPDATE_FORM_PASSWORD))
                .thenReturn("");
        when(request.getContextPath()).thenReturn("localhost:8080");
        controller.doPost(request, response);
        verify(response).sendRedirect("localhost:8080" + MAPPING_ERROR_PAGE + "?"
                        + PARAMETER_MESSAGE + "=" + NO_PARAMETER_ERROR_MSG);
    }

    @Test
    void doPost_NotValidPassword_Ok()
            throws WrongDataException, DataProcessingException, ServletException, IOException {
        UserService userService = mock(UserService.class);
        when(userService.get(1L)).thenReturn(bob);
        AuthenticationService authenticationService = mock(AuthenticationService.class);
        when(authenticationService.checkPassword(1L, "1235"))
                .thenReturn(false);
        UpdateUserAccountServlet controller =
                new UpdateUserAccountServlet(authenticationService, userService);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter(PARAMETER_UPDATE_FORM_PASSWORD))
                .thenReturn("1235");
        when(request.getParameter(PARAMETER_USER_EMAIL))
                .thenReturn("newbob@gmail.com");
        when(request.getParameter(PARAMETER_USER_NAME))
                .thenReturn("Bob Didrikson");
        when(request.getParameter(PARAMETER_USER_PHONE_NUMBER))
                .thenReturn("667777777");
        when(request.getParameter(PARAMETER_USER_COUNTRY_CODE))
                .thenReturn("380");
        HttpSession httpSession = mock(HttpSession.class);
        when(httpSession.getAttribute(SESSION_ATTRIBUTE_USER_ID))
                .thenReturn(1L);
        when(request.getSession()).thenReturn(httpSession);
        when(request.getContextPath()).thenReturn("localhost:8080");
        controller.doPost(request, response);
        verify(authenticationService).checkPassword(1L, "1235");
        verify(response).sendRedirect("localhost:8080" + MAPPING_USERS_ACCOUNT + "?"
                        + PARAMETER_MESSAGE + "=" + NO_VALID_PASSWORD_ERROR_MSG);
    }

    @Test
    void doPost_DBException_Ok()
            throws WrongDataException, DataProcessingException, ServletException, IOException {
        UserService userService = mock(UserService.class);
        when(userService.get(1L))
                .thenThrow(new WrongDataException("Can't update user account"));
        AuthenticationService authenticationService = mock(AuthenticationService.class);
        when(authenticationService.checkPassword(1L, "1234"))
                .thenReturn(true);
        UpdateUserAccountServlet controller =
                new UpdateUserAccountServlet(authenticationService, userService);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter(PARAMETER_UPDATE_FORM_PASSWORD))
                .thenReturn("1234");
        when(request.getParameter(PARAMETER_USER_EMAIL))
                .thenReturn("newbob@gmail.com");
        when(request.getParameter(PARAMETER_USER_NAME))
                .thenReturn("Bob Didrikson");
        when(request.getParameter(PARAMETER_USER_PHONE_NUMBER))
                .thenReturn("667777777");
        when(request.getParameter(PARAMETER_USER_COUNTRY_CODE))
                .thenReturn("380");
        HttpSession httpSession = mock(HttpSession.class);
        when(httpSession.getAttribute(SESSION_ATTRIBUTE_USER_ID))
                .thenReturn(1L);
        when(request.getSession()).thenReturn(httpSession);
        when(request.getContextPath()).thenReturn("localhost:8080");
        controller.doPost(request, response);
        verify(userService).get(1L);
        verify(response).sendRedirect("localhost:8080" + MAPPING_USERS_ACCOUNT + "?"
                        + PARAMETER_MESSAGE + "=" + ACCESS_ERROR_MSG);
    }
}

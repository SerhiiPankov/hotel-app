package hotel.web.servlet.users.control;

import hotel.dto.UserDto;
import hotel.dto.list.UsersDto;
import hotel.exception.DataProcessingException;
import hotel.model.enums.Role;
import hotel.service.UserService;
import hotel.util.Constant;
import org.junit.jupiter.api.Test;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManageUserServletTest implements Constant {

    @Test
    void doGet_NullSortParameter_Ok() throws ServletException, IOException, DataProcessingException {
        List<UserDto> listUsersDto = new ArrayList<>();
        UsersDto usersDto = new UsersDto();
        usersDto.setUsers(listUsersDto);
        usersDto.setNumberOfUsers(5);
        UserService userService = mock(UserService.class);
        when(userService.getAllByRole(
                COLUMN_NAME_IS_DELETED, 1,
                        USER_RECORDS_PER_PAGE,
                        Role.CUSTOMER, Role.MANAGER))
                .thenReturn(usersDto);
        ManageUserServlet controller = new ManageUserServlet(userService);
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute(SESSION_ATTRIBUTE_USER_ROLE))
                .thenReturn(Role.ADMIN);
        when(session.getAttribute(SESSION_ATTRIBUTE_SORT_USER))
                .thenReturn(3L);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter(PARAMETER_PAGE))
                .thenReturn("1");
        when(request.getSession())
                .thenReturn(session);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(JSP_USERS_ALL_USERS))
                .thenReturn(requestDispatcher);
        controller.doGet(request, response);
        verify(session).getAttribute(SESSION_ATTRIBUTE_USER_ROLE);
        verify(request, times(3))
                .getParameter(PARAMETER_PAGE);
        verify(userService).getAllByRole(
                COLUMN_NAME_IS_DELETED, 1,
                USER_RECORDS_PER_PAGE,
                Role.CUSTOMER, Role.MANAGER);
        verify(request).setAttribute(ATTRIBUTE_ALL_USERS, listUsersDto);
        verify(request).setAttribute(ATTRIBUTE_USER_CURRENT_PAGE, 1);
        verify(request).setAttribute(ATTRIBUTE_USER_NUMBER_OF_PAGES, 1);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void doGet_NameSortParameter_Ok() throws ServletException, IOException, DataProcessingException {
        List<UserDto> listUsersDto = new ArrayList<>();
        UsersDto usersDto = new UsersDto();
        usersDto.setUsers(listUsersDto);
        usersDto.setNumberOfUsers(20);
        UserService userService = mock(UserService.class);
        when(userService.getAllByRole(
                COLUMN_NAME_NAME, 2,
                USER_RECORDS_PER_PAGE,
                Role.CUSTOMER))
                .thenReturn(usersDto);
        ManageUserServlet controller = new ManageUserServlet(userService);
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute(SESSION_ATTRIBUTE_USER_ROLE))
                .thenReturn(Role.MANAGER);
        when(session.getAttribute(SESSION_ATTRIBUTE_SORT_USER))
                .thenReturn(1L);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter(PARAMETER_PAGE))
                .thenReturn("2");
        when(request.getSession())
                .thenReturn(session);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(JSP_USERS_ALL_USERS))
                .thenReturn(requestDispatcher);
        controller.doGet(request, response);
        verify(session).getAttribute(SESSION_ATTRIBUTE_USER_ROLE);
        verify(request, times(3))
                .getParameter(PARAMETER_PAGE);
        verify(userService).getAllByRole(
                COLUMN_NAME_NAME, 2,
                USER_RECORDS_PER_PAGE,
                Role.CUSTOMER);
        verify(request).setAttribute(ATTRIBUTE_ALL_USERS, listUsersDto);
        verify(request).setAttribute(ATTRIBUTE_USER_CURRENT_PAGE, 2);
        verify(request).setAttribute(ATTRIBUTE_USER_NUMBER_OF_PAGES, 2);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void doGet_EmailSortParameter_Ok() throws ServletException, IOException, DataProcessingException {
        List<UserDto> listUsersDto = new ArrayList<>();
        UsersDto usersDto = new UsersDto();
        usersDto.setUsers(listUsersDto);
        usersDto.setNumberOfUsers(50);
        UserService userService = mock(UserService.class);
        when(userService.getAllByRole(
                COLUMN_NAME_EMAIL, 2,
                USER_RECORDS_PER_PAGE,
                Role.CUSTOMER))
                .thenReturn(usersDto);
        ManageUserServlet controller = new ManageUserServlet(userService);
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute(SESSION_ATTRIBUTE_USER_ROLE))
                .thenReturn(Role.MANAGER);
        when(session.getAttribute(SESSION_ATTRIBUTE_SORT_USER))
                .thenReturn(2L);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter(PARAMETER_PAGE))
                .thenReturn("2");
        when(request.getSession())
                .thenReturn(session);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(JSP_USERS_ALL_USERS))
                .thenReturn(requestDispatcher);
        controller.doGet(request, response);
        verify(session).getAttribute(SESSION_ATTRIBUTE_USER_ROLE);
        verify(request, times(3))
                .getParameter(PARAMETER_PAGE);
        verify(userService).getAllByRole(
                COLUMN_NAME_EMAIL, 2,
                USER_RECORDS_PER_PAGE,
                Role.CUSTOMER);
        verify(request).setAttribute(ATTRIBUTE_ALL_USERS, listUsersDto);
        verify(request).setAttribute(ATTRIBUTE_USER_CURRENT_PAGE, 2);
        verify(request).setAttribute(ATTRIBUTE_USER_NUMBER_OF_PAGES, 5);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void doGet_DBException_NotOk() throws ServletException, IOException, DataProcessingException {
        UserService userService = mock(UserService.class);
        when(userService.getAllByRole(
                COLUMN_NAME_IS_DELETED, 1,
                USER_RECORDS_PER_PAGE,
                Role.CUSTOMER, Role.MANAGER))
                .thenThrow(new DataProcessingException("Can't get list of users",
                        new SQLException()));
        ManageUserServlet controller = new ManageUserServlet(userService);
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute(SESSION_ATTRIBUTE_USER_ROLE))
                .thenReturn(Role.ADMIN);
        when(session.getAttribute(SESSION_ATTRIBUTE_SORT_USER))
                .thenReturn(3L);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession())
                .thenReturn(session);
        when(request.getParameter(PARAMETER_PAGE))
                .thenReturn("1");
        when(request.getContextPath())
                .thenReturn("localhost:8080");
        HttpServletResponse response = mock(HttpServletResponse.class);
        controller.doGet(request, response);
        verify(response)
                .sendRedirect("localhost:8080" + MAPPING_ERROR_PAGE + "?"
                        + PARAMETER_MESSAGE + "=" + ACCESS_ERROR_MSG);
    }

    @Test
    void doPost_Ok() throws ServletException, IOException {
        ManageUserServlet controller = new ManageUserServlet();
        HttpSession session = mock(HttpSession.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter(PARAMETER_SORT_USER))
                .thenReturn("2");
        when(request.getSession())
                .thenReturn(session);
        when(request.getContextPath())
                .thenReturn("localhost:8080");
        HttpServletResponse response = mock(HttpServletResponse.class);
        controller.doPost(request, response);
        verify(session).setAttribute(SESSION_ATTRIBUTE_SORT_USER, 2L);
        verify(response).sendRedirect("localhost:8080" + MAPPING_USERS);
    }

    @Test
    void doPost_NoParameter_Ok() throws ServletException, IOException {
        ManageUserServlet controller = new ManageUserServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter(PARAMETER_SORT_USER))
                .thenReturn("");
        when(request.getContextPath())
                .thenReturn("localhost:8080");
        HttpServletResponse response = mock(HttpServletResponse.class);
        controller.doPost(request, response);
        verify(response).sendRedirect("localhost:8080" + MAPPING_USERS);
    }
}
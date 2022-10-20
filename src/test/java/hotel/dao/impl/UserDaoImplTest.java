package hotel.dao.impl;

import org.junit.jupiter.api.BeforeAll;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import util.TestConnectionUtil;
import hotel.dao.UserDao;
import hotel.exception.DataProcessingException;
import hotel.model.User;
import hotel.model.enums.Role;
import hotel.util.ConnectionUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import static org.mockito.Mockito.*;

class UserDaoImplTest {
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
            .setPhone("+380 (67) 777-77-77")
            .setRole(Role.CUSTOMER)
            .setLanguage("uk")
            .build();
    private static UserDao userDao;

    @BeforeAll
    static void beforeAll() {
        userDao = new UserDaoImpl();
    }

    @Test
    void getAll_Ok() throws SQLException, DataProcessingException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);
        when(resultSet.getObject("id", Long.class))
                .thenReturn(1L)
                .thenReturn(2L);
        when(resultSet.getNString("email"))
                .thenReturn("bob@gmail.com")
                .thenReturn("alice@gmail.com");
        when(resultSet.getNString("password"))
                .thenReturn("1234")
                .thenReturn("4321");
        when(resultSet.getBytes("salt"))
                .thenReturn(new byte[]{1, 2, 3, 4, 5})
                .thenReturn(new byte[]{5, 4, 3, 2, 1});
        when(resultSet.getNString("name"))
                .thenReturn("Bob")
                .thenReturn("Alice");
        when(resultSet.getNString("phone"))
                .thenReturn("+380 (66) 666-66-66")
                .thenReturn("+380 (67) 777-77-77");
        when(resultSet.getNString("role"))
                .thenReturn("MANAGER")
                .thenReturn("CUSTOMER");
        when(resultSet.getNString("language"))
                .thenReturn("en")
                .thenReturn("uk");
        PreparedStatement statement = mock(PreparedStatement.class);
        when(statement.executeQuery())
                .thenReturn(resultSet);
        String query = "SELECT * FROM users WHERE is_deleted = FALSE ORDER BY id";
        Connection connection = mock(Connection.class);
        when(connection.prepareStatement(query))
                .thenReturn(statement);
        MockedStatic<ConnectionUtil> utilities = TestConnectionUtil.getConnectionUtil();
        utilities.when(ConnectionUtil::getConnection)
                .thenReturn(connection);
        List<User> expected= List.of(bob, alice);
        List<User> actual = userDao.getAll();
        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    void getAll_SQLException_NotOk() throws SQLException {
        String query = "SELECT * FROM users WHERE is_deleted = FALSE ORDER BY id";
        Connection connection = mock(Connection.class);
        when(connection.prepareStatement(query))
                .thenThrow(new SQLException());
        MockedStatic<ConnectionUtil> utilities = TestConnectionUtil.getConnectionUtil();
        utilities.when(ConnectionUtil::getConnection)
                .thenReturn(connection);
        Assertions.assertThrows(DataProcessingException.class,
                () -> userDao.getAll());
    }

    @Test
    void get_Ok() throws SQLException, DataProcessingException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next())
                .thenReturn(true)
                .thenReturn(false);
        when(resultSet.getObject("id", Long.class))
                .thenReturn(1L);
        when(resultSet.getNString("email"))
                .thenReturn("bob@gmail.com");
        when(resultSet.getNString("password"))
                .thenReturn("1234");
        when(resultSet.getBytes("salt"))
                .thenReturn(new byte[]{1, 2, 3, 4, 5});
        when(resultSet.getNString("name"))
                .thenReturn("Bob");
        when(resultSet.getNString("phone"))
                .thenReturn("+380 (66) 666-66-66");
        when(resultSet.getNString("role"))
                .thenReturn("MANAGER");
        when(resultSet.getNString("language"))
                .thenReturn("en");
        PreparedStatement statement = mock(PreparedStatement.class);
        when(statement.executeQuery())
                .thenReturn(resultSet);
        String query = "SELECT * FROM users WHERE id = ? AND is_deleted = FALSE";
        Connection connection = mock(Connection.class);
        when(connection.prepareStatement(query))
                .thenReturn(statement);
        MockedStatic<ConnectionUtil> utilities = TestConnectionUtil.getConnectionUtil();
        utilities.when(ConnectionUtil::getConnection)
                .thenReturn(connection);
        User actual = userDao.get(1L).get();
        Assertions.assertTrue(new ReflectionEquals(bob).matches(actual));
    }

    @Test
    void get_SQLException_NotOk() throws SQLException {
        String query = "SELECT * FROM users WHERE id = ? AND is_deleted = FALSE";
        Connection connection = mock(Connection.class);
        when(connection.prepareStatement(query))
                .thenThrow(new SQLException());
        MockedStatic<ConnectionUtil> utilities = TestConnectionUtil.getConnectionUtil();
        utilities.when(ConnectionUtil::getConnection)
                .thenReturn(connection);
        Assertions.assertThrows(DataProcessingException.class,
                () -> userDao.get(1L));
    }
}

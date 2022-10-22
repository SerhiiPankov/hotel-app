package hotel.dao.impl;

import static org.junit.jupiter.api.Assertions.*;

import hotel.dao.RequestDao;
import hotel.dto.list.RequestsDto;
import hotel.exception.DataProcessingException;
import hotel.model.Request;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.TestConnectionUtil;
import util.TestConstant;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

class RequestDaoImplTest implements TestConstant {
    private static RequestDao requestDao;
    private Connection connection;

    @BeforeAll
    static void beforeAll() {
        requestDao = new RequestDaoImpl();
    }

    @BeforeEach
    void setUp() throws SQLException {
        connection = TestConnectionUtil.getConnection();
        connection.createStatement().executeUpdate(CREATE_ROOM_CLASSES_TABLE);
        connection.createStatement().executeUpdate(ROOM_CLASS_INSERT_QUERY_STANDARD);
        connection.createStatement().executeUpdate(ROOM_CLASS_INSERT_QUERY_LUX);
        connection.createStatement().executeUpdate(CREATE_ROOM_TABLE);
        connection.createStatement().executeUpdate(ROOM_INSERT_QUERY_101);
        connection.createStatement().executeUpdate(ROOM_INSERT_QUERY_102);
        connection.createStatement().executeUpdate(CREATE_USER_TABLE);
        addUser(connection);
        connection.createStatement().executeUpdate(CREATE_REQUEST_TABLE);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection = TestConnectionUtil.getConnection();
        connection.createStatement().executeUpdate(DROP_REQUEST_TABLE);
        connection.createStatement().executeUpdate(DROP_USER_TABLE);
        connection.createStatement().executeUpdate(DROP_ROOM_TABLE);
        connection.createStatement().executeUpdate(DROP_ROOM_CLASSES_TABLE);
        connection.close();
    }

    @Test
    void get_Ok() throws SQLException, DataProcessingException {
        addRequest(connection);
        Request actual = requestDao.get(REQUEST_ID).get();
        assertNotNull(actual);
        assertEquals(REQUEST_ID, actual.getId());
        assertEquals(LocalDate.now(), actual.getDate());
        assertEquals(USER_BOB_ID, actual.getCustomerId());
        assertEquals(ROOM_CLASS_ID_STANDARD, actual.getHotelRoomClassId());
        assertEquals(REQUEST_NUMBER_OF_GUESTS, actual.getNumberOfGuests());
        assertEquals(LocalDate.now().plusDays(ADD_FOR_CHECKIN_DAY),
                actual.getCheckIn());
        assertEquals(LocalDate.now().plusDays(ADD_FOR_CHECKOUT_DAY),
                actual.getCheckOut());
    }

    @Test
    void get_SQLException_NotOk() throws SQLException {
        connection.close();
        assertThrows(DataProcessingException.class,
                () -> requestDao.get(REQUEST_ID));
    }

    @Test
    void getAllDto_Ok() throws SQLException, DataProcessingException {
        for (int i = 0; i < 10; i++) {
            addRequest(connection);
        }
        String query = "UPDATE booking_requests SET is_processed = true "
                + "WHERE id = 2";
        connection.createStatement().executeUpdate(query);
        RequestsDto actual = requestDao.getAllDto(5, 5, false);

        assertEquals(4, actual.getRequests().size());
        assertEquals(9, actual.getNumberOfRequests());
        assertEquals(5, actual.getRequests().get(0).getRequestId());
    }

    @Test
    void getAllDto_SQLException_NotOk() throws SQLException {
        connection.close();
        assertThrows(DataProcessingException.class,
                () -> requestDao.getAllDto(5, 5, false));
    }

    private void addRequest(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(REQUEST_INSERT_QUERY);
        statement.setDate(1, Date.valueOf(LocalDate.now()));
        statement.setLong(2, USER_BOB_ID);
        statement.setLong(3, ROOM_CLASS_ID_STANDARD);
        statement.setInt(4, REQUEST_NUMBER_OF_GUESTS);
        statement.setDate(5, Date.valueOf(
                LocalDate.now().plusDays(ADD_FOR_CHECKIN_DAY)));
        statement.setDate(6, Date.valueOf(
                LocalDate.now().plusDays(ADD_FOR_CHECKOUT_DAY)));
        statement.executeUpdate();
    }

    private void addUser(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(USER_INSERT_BOB);
        statement.setString(1, USER_BOB_EMAIL);
        statement.setString(2, USER_BOB_PASSWORD);
        statement.setBytes(3, USER_BOB_SALT);
        statement.setString(4, USER_BOB_NAME);
        statement.setString(5, USER_BOB_PHONE);
        statement.setString(6, USER_BOB_ROLE.name());
        statement.executeUpdate();
    }

}
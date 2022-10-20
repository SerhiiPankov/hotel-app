package hotel.dao.impl;

import hotel.dao.ScheduleDao;
import hotel.exception.DataProcessingException;
import hotel.model.Schedule;
import hotel.model.enums.BookingStatus;
import hotel.util.Constant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.TestConnectionUtil;
import util.TestConstant;
import java.sql.*;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleDaoImplTest implements Constant, TestConstant {
    private static ScheduleDao scheduleDao;
    private Connection connection;
    
    @BeforeAll
    static void beforeAll() {
        scheduleDao = new ScheduleDaoImpl();
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
        connection.createStatement().executeUpdate(CREATE_SCHEDULE_TABLE);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection = TestConnectionUtil.getConnection();
        connection.createStatement().executeUpdate(DROP_SCHEDULE_TABLE);
        connection.createStatement().executeUpdate(DROP_ROOM_TABLE);
        connection.createStatement().executeUpdate(DROP_ROOM_CLASSES_TABLE);
        connection.close();
    }

    @Test
    void add_Ok() throws DataProcessingException, SQLException {
        Schedule schedule = new Schedule();
        schedule.setHotelRoomId(ROOM_ID_101);
        schedule.setDay(SCHEDULE_DAY_1);
        schedule.setPrice(SCHEDULE_PRICE_1);
        schedule.setBookingStatus(SCHEDULE_BOOKING_STATUS_1);
        scheduleDao.add(schedule);
        connection = TestConnectionUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(SCHEDULE_SELECT_QUERY_ROOM_DAY);
        statement.setLong(1, ROOM_ID_101);
        statement.setDate(2, Date.valueOf(SCHEDULE_DAY_1));
        ResultSet resultSet = statement.executeQuery();
        Schedule actual = new Schedule();
        if (resultSet.next()) {
            actual = parseScheduleFromResultSet(resultSet);
        }
        assertEquals(schedule, actual);
    }

    @Test
    void add_SQLException_NotOk() throws SQLException {
        connection.close();
        Schedule schedule = new Schedule();
        schedule.setHotelRoomId(ROOM_ID_101);
        schedule.setDay(SCHEDULE_DAY_1);
        schedule.setPrice(SCHEDULE_PRICE_1);
        schedule.setBookingStatus(SCHEDULE_BOOKING_STATUS_1);
        assertThrows(DataProcessingException.class,
                () -> scheduleDao.add(schedule));
    }

    @Test
    void update_Ok() throws SQLException, DataProcessingException {
        PreparedStatement statementInsert = connection.prepareStatement(SCHEDULE_INSERT_QUERY_1);
        statementInsert.setLong(1, ROOM_ID_101);
        statementInsert.setDate(2, Date.valueOf(SCHEDULE_DAY_1));
        statementInsert.setBigDecimal(3, SCHEDULE_PRICE_1);
        statementInsert.setString(4, SCHEDULE_BOOKING_STATUS_1.name());
        statementInsert.execute();

        Schedule schedule = new Schedule();
        schedule.setHotelRoomId(ROOM_ID_101);
        schedule.setDay(SCHEDULE_DAY_1);
        schedule.setPrice(SCHEDULE_PRICE_2);
        schedule.setBookingStatus(SCHEDULE_BOOKING_STATUS_2);
        scheduleDao.update(schedule);

        connection = TestConnectionUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(SCHEDULE_SELECT_QUERY_ROOM_DAY);
        statement.setLong(1, ROOM_ID_101);
        statement.setDate(2, Date.valueOf(SCHEDULE_DAY_1));
        ResultSet resultSet = statement.executeQuery();
        Schedule actual = new Schedule();
        if (resultSet.next()) {
            actual = parseScheduleFromResultSet(resultSet);
        }
        assertEquals(schedule, actual);
    }

    @Test
    void update_SQLException_NotOk() throws SQLException {
        connection.close();
        Schedule schedule = new Schedule();
        schedule.setHotelRoomId(ROOM_ID_101);
        schedule.setDay(SCHEDULE_DAY_1);
        schedule.setPrice(SCHEDULE_PRICE_2);
        schedule.setBookingStatus(SCHEDULE_BOOKING_STATUS_2);
        assertThrows(DataProcessingException.class,
                () -> scheduleDao.update(schedule));
    }

    @Test
    void deleteExpiredSchedule_Ok() throws SQLException, DataProcessingException {
        PreparedStatement statementInsert = connection.prepareStatement(SCHEDULE_INSERT_QUERY_1);
        statementInsert.setLong(1, ROOM_ID_101);
        statementInsert.setDate(2, Date.valueOf(SCHEDULE_DAY_1));
        statementInsert.setBigDecimal(3, SCHEDULE_PRICE_1);
        statementInsert.setString(4, SCHEDULE_BOOKING_STATUS_1.name());
        statementInsert.execute();

        LocalDate delete = SCHEDULE_DAY_1.plusDays(1L);
        int i = scheduleDao.deleteExpiredSchedule(delete);
        assertEquals(1, i);

        connection = TestConnectionUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(SCHEDULE_SELECT_QUERY_ROOM_DAY);
        statement.setLong(1, ROOM_ID_101);
        statement.setDate(2, Date.valueOf(SCHEDULE_DAY_1));
        ResultSet resultSet = statement.executeQuery();
        Schedule actual = new Schedule();
        if (resultSet.next()) {
            actual = parseScheduleFromResultSet(resultSet);
        }
        assertEquals(BookingStatus.UNAVAILABLE, actual.getBookingStatus());
    }

    @Test
    void deleteExpiredSchedule_SQLException_NotOk() throws SQLException {
        connection.close();
        LocalDate delete = SCHEDULE_DAY_1.plusDays(1L);
        assertThrows(DataProcessingException.class,
                () -> scheduleDao.deleteExpiredSchedule(delete));
    }

    private Schedule parseScheduleFromResultSet(ResultSet resultSet) throws SQLException {
        Schedule schedule = new Schedule();
        schedule.setHotelRoomId(resultSet.getObject(COLUMN_NAME_HOTEL_ROOM_ID, Long.class));
        schedule.setDay(resultSet.getDate(COLUMN_NAME_DAY).toLocalDate());
        schedule.setPrice(resultSet.getBigDecimal(COLUMN_NAME_PRICE));
        schedule.setBookingStatus(BookingStatus.valueOf(resultSet.getNString(COLUMN_NAME_BOOKING_STATUS)));
        return schedule;
    }
}
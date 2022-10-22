package hotel.dao.impl;

import hotel.dao.ScheduleDao;
import hotel.dto.ScheduleDto;
import hotel.dto.list.SchedulesDto;
import hotel.exception.DataProcessingException;
import hotel.model.Schedule;
import hotel.model.enums.BookingStatus;
import hotel.util.Constant;
import org.junit.jupiter.api.*;
import util.TestConnectionUtil;
import util.TestConstant;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
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
        createSchedule(connection,ROOM_ID_101, SCHEDULE_DAY_1, SCHEDULE_PRICE_1, SCHEDULE_BOOKING_STATUS_1);
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
        createSchedule(connection,ROOM_ID_101, SCHEDULE_DAY_1, SCHEDULE_PRICE_1, SCHEDULE_BOOKING_STATUS_1);
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

    @Test
    void getAllByHotelRoomIdAndDateInterval_Ok() throws SQLException, DataProcessingException {
        createSchedule(connection,ROOM_ID_101, SCHEDULE_DAY_1, SCHEDULE_PRICE_1, SCHEDULE_BOOKING_STATUS_1);
        createSchedule(connection,ROOM_ID_101, SCHEDULE_DAY_2, SCHEDULE_PRICE_2, SCHEDULE_BOOKING_STATUS_1);
        ScheduleDto expected = new ScheduleDto();
        expected.setHotelRoomId(ROOM_ID_101);
        expected.setDay(SCHEDULE_DAY_2);
        expected.setPrice(SCHEDULE_PRICE_2);
        expected.setBookingStatus(SCHEDULE_BOOKING_STATUS_1);
        List<ScheduleDto> actual = scheduleDao.getAllByHotelRoomIdAndDateInterval(
                ROOM_ID_101,
                SCHEDULE_DAY_2.minusDays(1L),
                SCHEDULE_DAY_2.plusDays(1L));
        assertEquals(1, actual.size());
        assertEquals(expected, actual.get(0));
    }

    @Test
    void getAllByHotelRoomIdAndDateInterval_SQLException_NotOk() throws SQLException {
        connection.close();
        assertThrows(DataProcessingException.class,
                () -> scheduleDao.getAllByHotelRoomIdAndDateInterval(
                        ROOM_ID_101,
                        SCHEDULE_DAY_2.minusDays(1L),
                        SCHEDULE_DAY_2.plusDays(1L)));
    }

    @Test
    void countFreeDaysByInterval_Ok() throws SQLException, DataProcessingException {
        LocalDate day = LocalDate.now().plusDays(1L);
        createSchedules(connection, day);
        int actual = scheduleDao.countFreeDaysByInterval(ROOM_ID_101,
                day.plusDays(1L), day.plusDays(4L));
        assertEquals(4, actual);
    }

    @Test
    void countFreeDaysByInterval_SQLException_NotOk() throws SQLException {
        connection.close();
        assertThrows(DataProcessingException.class,
                () -> scheduleDao.countFreeDaysByInterval(ROOM_ID_101,
                        SCHEDULE_DAY_1.minusDays(2L),
                        SCHEDULE_DAY_1.plusDays(2L)));
    }

        @Test
    void getAllByHotelRoomId_Ok() throws SQLException, DataProcessingException {
        LocalDate day = LocalDate.now().plusDays(1L);
        createSchedules(connection, day);
        SchedulesDto actual =
                scheduleDao.getAllByHotelRoomId(ROOM_ID_101, 2, 2);
        assertEquals(day.plusDays(2L), actual.getSchedules().get(0).getDay());
        assertEquals(day.plusDays(3L), actual.getSchedules().get(1).getDay());
        assertEquals(5, actual.getNumberOfSchedules());
        assertEquals(ROOM_ID_101, actual.getSchedules().get(0).getHotelRoomId());
    }

    @Test
    void getAllByHotelRoomId_SQLException_NotOk() throws SQLException {
        connection.close();
        assertThrows(DataProcessingException.class,
                () -> scheduleDao.getAllByHotelRoomId(
                        ROOM_ID_101, 0, 2));
    }

    private void createSchedule(Connection connection, long roomId, LocalDate day,
                                BigDecimal price, BookingStatus status)
            throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SCHEDULE_INSERT_QUERY);
        statement.setLong(1, roomId);
        statement.setDate(2, Date.valueOf(day));
        statement.setBigDecimal(3, price);
        statement.setString(4, status.name());
        statement.execute();
    }

    private void createSchedules(Connection connection, LocalDate day) throws SQLException {
        createSchedule(connection,ROOM_ID_101, day,
                SCHEDULE_PRICE_1, SCHEDULE_BOOKING_STATUS_2);
        createSchedule(connection,ROOM_ID_101, day.plusDays(1L),
                SCHEDULE_PRICE_2, SCHEDULE_BOOKING_STATUS_1);
        createSchedule(connection,ROOM_ID_101, day.plusDays(2L),
                SCHEDULE_PRICE_2, SCHEDULE_BOOKING_STATUS_1);
        createSchedule(connection,ROOM_ID_101, day.plusDays(3L),
                SCHEDULE_PRICE_2, SCHEDULE_BOOKING_STATUS_1);
        createSchedule(connection,ROOM_ID_101, day.plusDays(4L),
                SCHEDULE_PRICE_2, SCHEDULE_BOOKING_STATUS_1);
    }

    private Schedule parseScheduleFromResultSet(ResultSet resultSet) throws SQLException {
        Schedule schedule = new Schedule();
        schedule.setHotelRoomId(resultSet.getObject(COLUMN_NAME_HOTEL_ROOM_ID, Long.class));
        schedule.setDay(resultSet.getDate(COLUMN_NAME_DAY).toLocalDate());
        schedule.setPrice(resultSet.getBigDecimal(COLUMN_NAME_PRICE));
        schedule.setBookingStatus(BookingStatus.valueOf(
                resultSet.getNString(COLUMN_NAME_BOOKING_STATUS)));
        return schedule;
    }
}
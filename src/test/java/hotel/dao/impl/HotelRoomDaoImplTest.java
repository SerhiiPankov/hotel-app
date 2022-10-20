package hotel.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import hotel.dao.HotelRoomDao;
import hotel.dto.HotelRoomDto;
import hotel.exception.DataProcessingException;
import hotel.model.HotelRoom;
import hotel.util.Constant;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.TestConnectionUtil;
import util.TestConstant;

class HotelRoomDaoImplTest implements Constant, TestConstant {
    private static HotelRoomDao hotelRoomDao;
    private Connection connection;

    @BeforeAll
    static void beforeAll() {
        hotelRoomDao = new HotelRoomDaoImpl();
    }

    @BeforeEach
    void setUp() throws SQLException {
        connection = TestConnectionUtil.getConnection();
        connection.createStatement().executeUpdate(CREATE_ROOM_CLASSES_TABLE);
        connection.createStatement().executeUpdate(ROOM_CLASS_INSERT_QUERY_STANDARD);
        connection.createStatement().executeUpdate(ROOM_CLASS_INSERT_QUERY_LUX);
        connection.createStatement().executeUpdate(CREATE_ROOM_TABLE);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection = TestConnectionUtil.getConnection();
        connection.createStatement().executeUpdate(DROP_ROOM_TABLE);
        connection.createStatement().executeUpdate(DROP_ROOM_CLASSES_TABLE);
        connection.close();
    }

    @Test
    void create_Ok() throws DataProcessingException {
        HotelRoom hotelRoom = new HotelRoom();
        hotelRoom.setNumber(ROOM_NUMBER_101);
        hotelRoom.setHotelRoomClassId(ROOM_ROOM_CLASS_ID_101);
        hotelRoom.setNumberOfGuests(ROOM_NUMBER_OF_GUESTS_101);
        hotelRoom.setDescription(ROOM_DESCRIPTION_101);
        HotelRoom actual = hotelRoomDao.create(hotelRoom);
        assertNotNull(actual);
        assertEquals(ROOM_ID_101, actual.getId());
        assertNull(hotelRoom.getSchedule());
    }

    @Test
    void create_SQLException_NotOk() throws SQLException {
        connection.close();
        HotelRoom hotelRoom = new HotelRoom();
        hotelRoom.setNumber(ROOM_NUMBER_101);
        hotelRoom.setHotelRoomClassId(ROOM_ROOM_CLASS_ID_101);
        hotelRoom.setNumberOfGuests(ROOM_NUMBER_OF_GUESTS_101);
        hotelRoom.setDescription(ROOM_DESCRIPTION_101);
        assertThrows(DataProcessingException.class,
                () -> hotelRoomDao.create(hotelRoom));
    }

    @Test
    void get_Ok() throws SQLException, DataProcessingException {
        connection.createStatement().executeUpdate(ROOM_INSERT_QUERY_101);
        HotelRoom actual = hotelRoomDao.get(ROOM_ID_101).get();
        assertNotNull(actual);
        assertEquals(ROOM_ID_101, actual.getId());
        assertEquals(ROOM_NUMBER_101, actual.getNumber());
        assertEquals(ROOM_ROOM_CLASS_ID_101, actual.getHotelRoomClassId());
        assertEquals(ROOM_NUMBER_OF_GUESTS_101, actual.getNumberOfGuests());
        assertEquals(ROOM_DESCRIPTION_101, actual.getDescription());
    }

    @Test
    void get_SQLException_NotOk() throws SQLException {
        connection.close();
        assertThrows(DataProcessingException.class,
                () -> hotelRoomDao.get(ROOM_ID_101));
    }

    @Test
    void getAll_Ok() throws SQLException, DataProcessingException {
        connection.createStatement().executeUpdate(ROOM_INSERT_QUERY_101);
        connection.createStatement().executeUpdate(ROOM_INSERT_QUERY_102);
        List<HotelRoom> actual = hotelRoomDao.getAll();
        assertEquals(2, actual.size());
        assertEquals(ROOM_ID_101, actual.get(0).getId());
        assertEquals(ROOM_ID_102, actual.get(1).getId());
    }

    @Test
    void getAll_SQLException_NotOk() throws SQLException {
        connection.close();
        assertThrows(DataProcessingException.class,
                () -> hotelRoomDao.getAll());
    }

    @Test
    void getAllDto() throws SQLException, DataProcessingException {
        connection.createStatement().executeUpdate(ROOM_INSERT_QUERY_101);
        connection.createStatement().executeUpdate(ROOM_INSERT_QUERY_102);
        List<HotelRoomDto> actual = hotelRoomDao.getAllDto();
        assertEquals(2, actual.size());
        assertEquals(ROOM_ID_101, actual.get(0).getId());
        assertEquals(ROOM_CLASS_NAME_STANDARD, actual.get(0).getHotelRoomClassName());
        assertEquals(ROOM_ID_102, actual.get(1).getId());
        assertEquals(ROOM_CLASS_NAME_LUX, actual.get(1).getHotelRoomClassName());
    }

    @Test
    void getAllDto_SQLException_NotOk() throws SQLException {
        connection.close();
        assertThrows(DataProcessingException.class,
                () -> hotelRoomDao.getAllDto());
    }

    @Test
    void update_Ok() throws SQLException, DataProcessingException {
        connection.createStatement().executeUpdate(ROOM_INSERT_QUERY_101);
        HotelRoom hotelRoom = new HotelRoom();
        hotelRoom.setId(ROOM_ID_101);
        hotelRoom.setNumber(ROOM_NUMBER_102);
        hotelRoom.setHotelRoomClassId(ROOM_ROOM_CLASS_ID_102);
        hotelRoom.setNumberOfGuests(ROOM_NUMBER_OF_GUESTS_102);
        hotelRoom.setDescription(ROOM_DESCRIPTION_102);
        hotelRoomDao.update(hotelRoom);
        connection = TestConnectionUtil.getConnection();
        ResultSet resultSet = connection.createStatement().executeQuery(ROOM_SELECT_QUERY_ID);
        HotelRoom actual = new HotelRoom();
        if (resultSet.next()) {
            actual = parseRoomFromResultSet(resultSet);
        }
        assertEquals(hotelRoom, actual);
    }

    @Test
    void update_SQLException_NotOk() throws SQLException {
        connection.close();
        HotelRoom hotelRoom = new HotelRoom();
        hotelRoom.setId(ROOM_ID_101);
        hotelRoom.setNumber(ROOM_NUMBER_102);
        hotelRoom.setHotelRoomClassId(ROOM_ROOM_CLASS_ID_102);
        hotelRoom.setNumberOfGuests(ROOM_NUMBER_OF_GUESTS_102);
        hotelRoom.setDescription(ROOM_DESCRIPTION_102);
        assertThrows(DataProcessingException.class,
                () -> hotelRoomDao.update(hotelRoom));
    }

    @Test
    void delete_Ok() throws SQLException, DataProcessingException {
        connection.createStatement().executeUpdate(ROOM_INSERT_QUERY_101);
        boolean delete = hotelRoomDao.delete(ROOM_ID_101);
        assertTrue(delete);
        connection = TestConnectionUtil.getConnection();
        ResultSet resultSet = connection.createStatement().executeQuery(ROOM_SELECT_QUERY_ALL);
        List<HotelRoom> actual = new ArrayList<>();
        while (resultSet.next()) {
            actual.add(parseRoomFromResultSet(resultSet));
        }
        assertEquals(1, actual.size());
    }

    @Test
    void delete_SQLException_NotOk() throws SQLException {
        connection.close();
        assertThrows(DataProcessingException.class,
                () -> hotelRoomDao.delete(ROOM_ID_101));
    }

    @Test
    void getHotelRoomByNumber_Ok() throws SQLException, DataProcessingException {
        connection.createStatement().executeUpdate(ROOM_INSERT_QUERY_101);
        HotelRoom actual = hotelRoomDao.getHotelRoomByNumber(ROOM_NUMBER_101).get();
        assertNotNull(actual);
        assertEquals(ROOM_ID_101, actual.getId());
        assertEquals(ROOM_NUMBER_101, actual.getNumber());
        assertEquals(ROOM_ROOM_CLASS_ID_101, actual.getHotelRoomClassId());
        assertEquals(ROOM_NUMBER_OF_GUESTS_101, actual.getNumberOfGuests());
        assertEquals(ROOM_DESCRIPTION_101, actual.getDescription());
    }

    @Test
    void getHotelRoomByNumber_SQLException_NotOk() throws SQLException {
        connection.close();
        assertThrows(DataProcessingException.class,
                () -> hotelRoomDao.getHotelRoomByNumber(ROOM_NUMBER_101));
    }

    @Test
    void getHotelRoomDtoById() throws SQLException, DataProcessingException {
        connection.createStatement().executeUpdate(ROOM_INSERT_QUERY_101);
        HotelRoomDto actual = hotelRoomDao.getHotelRoomDtoById(ROOM_ID_101).get();
        assertNotNull(actual);
        assertEquals(ROOM_ID_101, actual.getId());
        assertEquals(ROOM_NUMBER_101, actual.getNumber());
        assertEquals(ROOM_CLASS_NAME_STANDARD, actual.getHotelRoomClassName());
        assertEquals(ROOM_NUMBER_OF_GUESTS_101, actual.getNumberOfGuests());
        assertEquals(ROOM_DESCRIPTION_101, actual.getDescription());
    }

    @Test
    void getHotelRoomDtoById_SQLException_NotOk() throws SQLException {
        connection.close();
        assertThrows(DataProcessingException.class,
                () -> hotelRoomDao.getHotelRoomDtoById(ROOM_ID_101));
    }

    private HotelRoom parseRoomFromResultSet(ResultSet resultSet) throws SQLException {
        HotelRoom hotelRoom = new HotelRoom();
        hotelRoom.setId(resultSet.getObject(COLUMN_NAME_ID, Long.class));
        hotelRoom.setNumber(resultSet.getNString(COLUMN_NAME_NUMBER));
        hotelRoom.setHotelRoomClassId(
                resultSet.getObject(COLUMN_NAME_HOTEL_ROOM_CLASS_ID, Long.class));
        hotelRoom.setNumberOfGuests(
                resultSet.getObject(COLUMN_NAME_NUMBER_OF_GUESTS, Integer.class));
        hotelRoom.setDescription(
                resultSet.getNString(COLUMN_NAME_DESCRIPTION));
        return hotelRoom;
    }
}

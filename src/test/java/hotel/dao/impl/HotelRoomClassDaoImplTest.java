package hotel.dao.impl;

import hotel.dao.HotelRoomClassDao;
import hotel.exception.DataProcessingException;
import hotel.model.HotelRoomClass;
import hotel.util.Constant;
import org.junit.jupiter.api.*;
import util.TestConnectionUtil;
import util.TestConstant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HotelRoomClassDaoImplTest implements Constant, TestConstant {
    private static HotelRoomClassDao hotelRoomCLassDao;
    private Connection connection;

    @BeforeAll
    static void beforeAll() {
        hotelRoomCLassDao = new HotelRoomClassDaoImpl();
    }

    @BeforeEach
    void setUp() throws SQLException {
        connection = TestConnectionUtil.getConnection();
        connection.createStatement().executeUpdate(CREATE_ROOM_CLASSES_TABLE);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection = TestConnectionUtil.getConnection();
        connection.createStatement().executeUpdate(DROP_ROOM_CLASSES_TABLE);
        connection.close();
    }

    @Test
    void create_Ok() throws DataProcessingException {
        HotelRoomClass hotelRoomClass = new HotelRoomClass();
        hotelRoomClass.setName(ROOM_CLASS_NAME_STANDARD);
        hotelRoomClass.setDescription(ROOM_CLASS_DESCRIPTION_STANDARD);
        HotelRoomClass actual = hotelRoomCLassDao.create(hotelRoomClass);
        assertNotNull(actual);
        assertEquals(ROOM_CLASS_ID_STANDARD, actual.getId());
        assertFalse(actual.getIsDeleted());
    }

    @Test
    void create_SQLException_NotOk() throws SQLException {
        connection.close();
        HotelRoomClass hotelRoomClass = new HotelRoomClass();
        hotelRoomClass.setName(ROOM_CLASS_NAME_STANDARD);
        hotelRoomClass.setDescription(ROOM_CLASS_DESCRIPTION_STANDARD);
        assertThrows(DataProcessingException.class,
                () -> hotelRoomCLassDao.create(hotelRoomClass));
    }

    @Test
    void get_Ok() throws SQLException, DataProcessingException {
        connection.createStatement().executeUpdate(ROOM_CLASS_INSERT_QUERY_STANDARD);
        HotelRoomClass actual = hotelRoomCLassDao.get(ROOM_CLASS_ID_STANDARD).get();
        assertNotNull(actual);
        assertEquals(ROOM_CLASS_ID_STANDARD, actual.getId());
        assertFalse(actual.getIsDeleted());
    }

    @Test
    void get_SQLException_NotOk() throws SQLException {
        connection.close();
        assertThrows(DataProcessingException.class,
                () -> hotelRoomCLassDao.get(ROOM_CLASS_ID_STANDARD));
    }

    @Test
    void getAll_Ok() throws SQLException, DataProcessingException {
        connection.createStatement().executeUpdate(ROOM_CLASS_INSERT_QUERY_STANDARD);
        connection.createStatement().executeUpdate(ROOM_CLASS_INSERT_QUERY_LUX);
        List<HotelRoomClass> actual = hotelRoomCLassDao.getAll();
        assertNotNull(actual);
        assertEquals(2, actual.size());
    }

    @Test
    void getAll_SQLException_NotOk() throws SQLException {
        connection.close();
        assertThrows(DataProcessingException.class,
                () -> hotelRoomCLassDao.getAll());
    }

    @Test
    void getAllWithoutDeleted_Ok() throws SQLException, DataProcessingException {
        connection.createStatement().executeUpdate(ROOM_CLASS_INSERT_QUERY_STANDARD);
        connection.createStatement().executeUpdate(ROOM_CLASS_INSERT_QUERY_LUX);
        connection.createStatement().executeUpdate(UPDATE_QUERY_ROOM_CLASS_ID);
        List<HotelRoomClass> actual = hotelRoomCLassDao.getAllWithoutDeleted();
        assertNotNull(actual);
        assertEquals(1, actual.size());
    }

    @Test
    void getAllWithoutDeleted_SQLException_NotOk() throws SQLException {
        connection.close();
        assertThrows(DataProcessingException.class,
                () -> hotelRoomCLassDao.getAllWithoutDeleted());
    }

    @Test
    void update_Ok() throws SQLException, DataProcessingException {
        connection.createStatement().executeUpdate(ROOM_CLASS_INSERT_QUERY_STANDARD);
        HotelRoomClass hotelRoomClass = new HotelRoomClass();
        hotelRoomClass.setId(ROOM_CLASS_ID_STANDARD);
        hotelRoomClass.setName(ROOM_CLASS_NAME_LUX);
        hotelRoomClass.setDescription(ROOM_CLASS_DESCRIPTION_LUX);
        hotelRoomCLassDao.update(hotelRoomClass);
        connection = TestConnectionUtil.getConnection();
        ResultSet resultSet = connection.createStatement().executeQuery(SELECT_QUERY_ROOM_CLASS_ID);
        HotelRoomClass actual = new HotelRoomClass();
        if (resultSet.next()) {
            actual = parseRoomClassFromResultSet(resultSet);
        }
        assertEquals(hotelRoomClass, actual);
    }

    @Test
    void update_SQLException_NotOk() throws SQLException {
        connection.close();
        HotelRoomClass hotelRoomClass = new HotelRoomClass();
        hotelRoomClass.setId(ROOM_CLASS_ID_STANDARD);
        hotelRoomClass.setName(ROOM_CLASS_NAME_LUX);
        hotelRoomClass.setDescription(ROOM_CLASS_DESCRIPTION_LUX);
        assertThrows(DataProcessingException.class,
                () -> hotelRoomCLassDao.update(hotelRoomClass));
    }

    @Test
    void delete_Ok() throws SQLException, DataProcessingException {
        connection.createStatement().executeUpdate(ROOM_CLASS_INSERT_QUERY_STANDARD);
        boolean delete = hotelRoomCLassDao.delete(ROOM_CLASS_ID_STANDARD);
        assertTrue(delete);
        connection = TestConnectionUtil.getConnection();
        ResultSet resultSet = connection.createStatement().executeQuery(SELECT_QUERY_ROOM_CLASS_ALL);
        List<HotelRoomClass> actual = new ArrayList<>();
        while (resultSet.next()) {
            actual.add(parseRoomClassFromResultSet(resultSet));
        }
        assertEquals(1, actual.size());
        assertTrue(actual.get(0).getIsDeleted());
    }

    @Test
    void delete_SQLException_NotOk() throws SQLException {
        connection.close();
        assertThrows(DataProcessingException.class,
                () -> hotelRoomCLassDao.delete(ROOM_CLASS_ID_STANDARD));
    }

    @Test
    void getByName_Ok() throws SQLException, DataProcessingException {
        connection.createStatement().executeUpdate(ROOM_CLASS_INSERT_QUERY_STANDARD);
        HotelRoomClass actual = hotelRoomCLassDao.getByName(ROOM_CLASS_NAME_STANDARD).get();
        assertNotNull(actual);
        assertEquals(ROOM_CLASS_ID_STANDARD, actual.getId());
        assertEquals(ROOM_CLASS_NAME_STANDARD, actual.getName());
        assertEquals(ROOM_CLASS_DESCRIPTION_STANDARD, actual.getDescription());
        assertFalse(actual.getIsDeleted());
    }

    @Test
    void getByName_SQLException_NotOk() throws SQLException {
        connection.close();
        assertThrows(DataProcessingException.class,
                () -> hotelRoomCLassDao.getByName(ROOM_CLASS_NAME_STANDARD));
    }

    private HotelRoomClass parseRoomClassFromResultSet(ResultSet resultSet)
            throws SQLException {
        HotelRoomClass hotelRoomClass = new HotelRoomClass();
        hotelRoomClass.setId(resultSet.getObject(COLUMN_NAME_ID, Long.class));
        hotelRoomClass.setName(resultSet.getNString(COLUMN_NAME_NAME));
        hotelRoomClass.setDescription(resultSet.getNString(COLUMN_NAME_DESCRIPTION));
        hotelRoomClass.setDeleted(resultSet.getBoolean(COLUMN_NAME_IS_DELETED));
        return hotelRoomClass;
    }
}

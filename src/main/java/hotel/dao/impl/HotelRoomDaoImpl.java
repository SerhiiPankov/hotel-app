package hotel.dao.impl;

import hotel.dao.HotelRoomDao;
import hotel.dto.HotelRoomDto;
import hotel.dto.SelectedHotelRoom;
import hotel.dto.list.SelectedHotelRoomsDto;
import hotel.exception.DataProcessingException;
import hotel.lib.Dao;
import hotel.model.HotelRoom;
import hotel.model.enums.BookingStatus;
import hotel.util.ConnectionUtil;
import hotel.util.Constant;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Dao
public class HotelRoomDaoImpl implements HotelRoomDao, Constant {
    private static final Logger logger = LogManager.getLogger(HotelRoomDaoImpl.class);

    @Override
    public HotelRoom create(HotelRoom hotelRoom) throws DataProcessingException {
        String query = "INSERT INTO hotel_rooms "
                + "(number, hotel_room_class_id, number_of_guests, description) "
                + "VALUES (?, ?, ?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query,
                         Statement.RETURN_GENERATED_KEYS)) {
            setHotelRoomParameterToStatement(statement, hotelRoom);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                hotelRoom.setId(resultSet.getObject(1, Long.class));
            }
            logger.info("Request to the database to create room " + hotelRoom
                    + " was successful");
            return hotelRoom;
        } catch (SQLException e) {
            logger.warn("Request to the database to create room " + hotelRoom
                    + " failed " + e);
            throw new DataProcessingException("Couldn't create "
                    + hotelRoom + ". ", e);
        }
    }

    @Override
    public Optional<HotelRoom> get(Long hotelRoomId) throws DataProcessingException {
        String query = "SELECT * FROM hotel_rooms WHERE id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, hotelRoomId);
            ResultSet resultSet = statement.executeQuery();
            HotelRoom hotelRoom = null;
            if (resultSet.next()) {
                hotelRoom = parseHotelRoomFromResultSet(resultSet);
            }
            logger.info("Request to the database to get room by id "
                    + hotelRoomId + " was successful");
            return Optional.ofNullable(hotelRoom);
        } catch (SQLException e) {
            logger.warn("Request to the database to get room by id "
                    + hotelRoomId + " failed " + e);
            throw new DataProcessingException("Couldn't get room by id "
                    + hotelRoomId, e);
        }
    }

    @Override
    public List<HotelRoom> getAll() throws DataProcessingException {
        String query = "SELECT * FROM hotel_rooms WHERE is_deleted = FALSE";
        List<HotelRoom> hotelRooms = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement statement =
                         connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                hotelRooms.add(parseHotelRoomFromResultSet(resultSet));
            }
            logger.info("Request to the database to get list of all rooms"
                    + " was successful");
            return hotelRooms;
        } catch (SQLException e) {
            logger.warn("Request to the database to get list of all rooms"
                    + " failed " + e);
            throw new DataProcessingException("Couldn't get list of all rooms from DB.",
                    e);
        }
    }

    @Override
    public List<HotelRoomDto> getAllDto() throws DataProcessingException {
        String query = "SELECT hr.id AS id, hrc.name AS ac_name, "
                + "number, number_of_guests, hr.description, hr.is_deleted "
                + "FROM hotel_rooms hr "
                + "JOIN hotel_room_classes hrc on hrc.id = hr.hotel_room_class_id ";
        List<HotelRoomDto> hotelRooms = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement statement =
                         connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                hotelRooms.add(parseHotelRoomDtoFromResultSet(resultSet));
            }
            logger.info("Request to the database to get list of all without deleted"
                    + " rooms was successful");
            return hotelRooms;
        } catch (SQLException e) {
            logger.warn("Request to the database to get list of all without deleted"
                    + " rooms failed " + e);
            throw new DataProcessingException("Couldn't get list of rooms from DB.",
                    e);
        }
    }

    @Override
    public HotelRoom update(HotelRoom hotelRoom) throws DataProcessingException {
        String query = "UPDATE hotel_rooms "
                + "SET number = ?, hotel_room_class_id = ?, number_of_guests = ?, "
                + "description = ?, is_deleted = FALSE "
                + "WHERE id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement statement
                         = connection.prepareStatement(query)) {
            setHotelRoomParameterToStatement(statement, hotelRoom);
            statement.setLong(5, hotelRoom.getId());
            statement.executeUpdate();
            logger.info("Request to the database to update a room " + hotelRoom
                    + " was successful");
            return hotelRoom;
        } catch (SQLException e) {
            logger.warn("Request to the database to update room " + hotelRoom
                    + " failed " + e);
            throw new DataProcessingException("Couldn't update "
                    + hotelRoom + " in DB.", e);
        }
    }

    @Override
    public boolean delete(Long hotelRoomId) throws DataProcessingException {
        String query = "UPDATE hotel_rooms SET is_deleted = TRUE WHERE id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement statement =
                         connection.prepareStatement(query)) {
            statement.setLong(1, hotelRoomId);
            boolean result = statement.executeUpdate() > 0;
            logger.info("Request to the database to delete room with id "
                    + hotelRoomId + " was successful");
            return result;
        } catch (SQLException e) {
            logger.warn("Request to the database to delete room by id "
                    + hotelRoomId + " failed " + e);
            throw new DataProcessingException("Couldn't delete room with id "
                    + hotelRoomId, e);
        }
    }

    @Override
    public Optional<HotelRoom> getHotelRoomByNumber(String number) throws DataProcessingException {
        String query = "SELECT * FROM hotel_rooms "
                + "WHERE number = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement statement =
                         connection.prepareStatement(query)) {
            statement.setString(1, number);
            ResultSet resultSet = statement.executeQuery();
            HotelRoom hotelRoom = null;
            if (resultSet.next()) {
                hotelRoom = parseHotelRoomFromResultSet(resultSet);
            }
            logger.info("Request to the database to get room by number "
                    + number + " was successful");
            return Optional.ofNullable(hotelRoom);
        } catch (SQLException e) {
            logger.warn("Request to the database to get room by number "
                    + number + " failed " + e);
            throw new DataProcessingException("Couldn't get room by number " + number, e);
        }
    }

    @Override
    public Optional<HotelRoomDto> getHotelRoomDtoById(Long hotelRoomId)
            throws DataProcessingException {
        String query = "SELECT hr.id AS id, hrc.name AS ac_name, "
                + "number, number_of_guests, hr.description, hr.is_deleted "
                + "FROM hotel_rooms hr "
                + "JOIN hotel_room_classes hrc on hrc.id = hr.hotel_room_class_id "
                + "WHERE hr.id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement statement =
                         connection.prepareStatement(query)) {
            statement.setLong(1, hotelRoomId);
            ResultSet resultSet = statement.executeQuery();
            HotelRoomDto hotelRoom = null;
            if (resultSet.next()) {
                hotelRoom = parseHotelRoomDtoFromResultSet(resultSet);
            }
            logger.info("Request to the database to get room class by id "
                    + hotelRoomId + " was successful");
            return Optional.ofNullable(hotelRoom);
        } catch (SQLException e) {
            logger.warn("Request to the database to get room class by id "
                    + hotelRoomId + " failed " + e);
            throw new DataProcessingException("Couldn't get hotel room by id " + hotelRoomId, e);
        }
    }

    @Override
    public List<HotelRoomDto> getAllHotelRoomDtoByDateAndByNumberOfGuestAndByHotelRoomClass(
            long hotelRoomClassId, int numberOfGuests,
            LocalDate fromDate, LocalDate toDate,
            BookingStatus bookingStatus, Long interval) throws DataProcessingException {
        String query = "SELECT hr.id AS id, hrc.name AS ac_name, "
                + "number, number_of_guests, hr.description, hr.is_deleted, COUNT(*) AS count_days "
                + "FROM hotel_rooms hr "
                + "JOIN hotel_room_classes hrc on hrc.id = hr.hotel_room_class_id "
                + "JOIN schedules s on hr.id = s.hotel_room_id "
                + "WHERE hotel_room_class_id = ? AND number_of_guests >= ? "
                + "AND day_schedule BETWEEN ? AND ? AND s.booking_status = ?"
                + "GROUP BY id "
                + "HAVING count_days >= ? ";
        List<HotelRoomDto> hotelRooms = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement statement =
                        connection.prepareStatement(query)) {
            setHotelRoomDtoParameterToStatement(statement, hotelRoomClassId,
                    numberOfGuests, fromDate, toDate, bookingStatus, interval);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                hotelRooms.add(parseHotelRoomDtoFromResultSet(resultSet));
            }
            logger.info("Request to the database to get list of"
                    + " rooms by date, number of guests and room class was successful");
            return hotelRooms;
        } catch (SQLException e) {
            logger.info("Request to the database to get list of"
                    + " rooms by date, number of guests and room class failed");
            throw new DataProcessingException("Couldn't get a list of hotel room from DB.",
                    e);
        }
    }

    @Override
    public SelectedHotelRoomsDto getAllHotelRoomDtoByDateRangeAndBookingStatus(
            LocalDate fromDate, LocalDate toDate, int numberOfGuests,
            long interval, BookingStatus bookingStatus,
            int startRecord, int recordsPerPage, String sort)
            throws DataProcessingException {
        String query = "SELECT SQL_CALC_FOUND_ROWS hr.id AS id, hrc.name AS ac_name, "
                + "SUM(s.price) AS total_price, COUNT(*) AS count_days, "
                + "number, number_of_guests, hr.description, hr.is_deleted "
                + "FROM hotel_rooms hr "
                + "JOIN hotel_room_classes hrc on hrc.id = hr.hotel_room_class_id "
                + "JOIN schedules s on hr.id = s.hotel_room_id "
                + "WHERE number_of_guests >= ? AND day_schedule BETWEEN ? AND ? "
                + "AND s.booking_status = ? "
                + "GROUP BY id, number_of_guests "
                + "HAVING count_days >= ? "
                + "ORDER BY " + sort + " "
                + "LIMIT ?, ?";
        String queryCount = "SELECT FOUND_ROWS()";
        List<SelectedHotelRoom> rooms = new ArrayList<>();
        int numberOfRecords = 0;
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement statement =
                         connection.prepareStatement(query);
                 PreparedStatement statementCount =
                         connection.prepareStatement(queryCount)) {
            connection.setAutoCommit(false);
            statement.setInt(1, numberOfGuests);
            statement.setDate(2, Date.valueOf(fromDate));
            statement.setDate(3, Date.valueOf(toDate));
            statement.setString(4,bookingStatus.name());
            statement.setLong(5,interval);
            statement.setInt(6, startRecord);
            statement.setInt(7, recordsPerPage);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                rooms.add(parseSelectedHotelRoomDtoFromResultSet(resultSet));
            }
            ResultSet resultSetCount = statementCount.executeQuery();
            if (resultSetCount.next()) {
                numberOfRecords = resultSetCount.getInt(1);
            }
            SelectedHotelRoomsDto roomsDto = new SelectedHotelRoomsDto();
            roomsDto.setRooms(rooms);
            roomsDto.setNumberOfRooms(numberOfRecords);
            connection.setAutoCommit(true);
            logger.info("Request to the database to get list of"
                    + " rooms by date range and booking status from "
                    + startRecord + " record was successful");
            return roomsDto;
        } catch (SQLException e) {
            logger.info("Request to the database to get list of"
                    + " rooms by date range and booking status from "
                    + startRecord + " record failed");
            throw new DataProcessingException("Couldn't get a list of hotel room from DB.",
                    e);
        }
    }

    private void setHotelRoomDtoParameterToStatement(PreparedStatement statement,
                                                     Long hotelRoomClassId, int numberOfGuests,
                                                     LocalDate fromDate, LocalDate toDate,
                                                     BookingStatus bookingStatus, Long interval)
            throws SQLException {
        statement.setLong(1, hotelRoomClassId);
        statement.setInt(2, numberOfGuests);
        statement.setDate(3, Date.valueOf(fromDate));
        statement.setDate(4,Date.valueOf(toDate));
        statement.setString(5,bookingStatus.name());
        statement.setLong(6,interval);
    }

    private void setHotelRoomParameterToStatement(PreparedStatement statement,
                                                  HotelRoom hotelRoom) throws SQLException {
        statement.setString(1, hotelRoom.getNumber());
        statement.setLong(2, hotelRoom.getHotelRoomClassId());
        statement.setInt(3, hotelRoom.getNumberOfGuests());
        statement.setString(4, hotelRoom.getDescription());

    }

    private HotelRoom parseHotelRoomFromResultSet(ResultSet resultSet) throws SQLException {
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

    private HotelRoomDto parseHotelRoomDtoFromResultSet(ResultSet resultSet) throws SQLException {
        HotelRoomDto hotelRoomDto = new HotelRoomDto();
        hotelRoomDto.setId(resultSet.getObject(COLUMN_NAME_ID, Long.class));
        hotelRoomDto.setNumber(resultSet.getNString(COLUMN_NAME_NUMBER));
        hotelRoomDto.setHotelRoomClassName(
                resultSet.getNString(COLUMN_NAME_HOTEL_ROOM_CLASS_NAME));
        hotelRoomDto.setNumberOfGuests(
                resultSet.getObject(COLUMN_NAME_NUMBER_OF_GUESTS, Integer.class));
        hotelRoomDto.setDescription(
                resultSet.getNString(COLUMN_NAME_DESCRIPTION));
        hotelRoomDto.setDeleted(
                resultSet.getBoolean(COLUMN_NAME_IS_DELETED));
        return hotelRoomDto;
    }

    private SelectedHotelRoom parseSelectedHotelRoomDtoFromResultSet(ResultSet resultSet)
            throws SQLException {
        SelectedHotelRoom selectedHotelRoom = new SelectedHotelRoom();
        selectedHotelRoom.setId(resultSet.getObject(COLUMN_NAME_ID, Long.class));
        selectedHotelRoom.setNumber(resultSet.getNString(COLUMN_NAME_NUMBER));
        selectedHotelRoom.setHotelRoomClassName(
                resultSet.getNString(COLUMN_NAME_HOTEL_ROOM_CLASS_NAME));
        selectedHotelRoom.setNumberOfGuests(
                resultSet.getObject(COLUMN_NAME_NUMBER_OF_GUESTS, Integer.class));
        selectedHotelRoom.setDescription(
                resultSet.getNString(COLUMN_NAME_DESCRIPTION));
        selectedHotelRoom.setTotalPrice(resultSet.getBigDecimal(COLUMN_NAME_TOTAL_PRICE));
        selectedHotelRoom.setDeleted(
                resultSet.getBoolean(COLUMN_NAME_IS_DELETED));
        return selectedHotelRoom;
    }
}

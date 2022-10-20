package hotel.dao.impl;

import hotel.dao.HotelRoomClassDao;
import hotel.exception.DataProcessingException;
import hotel.lib.Dao;
import hotel.model.HotelRoomClass;
import hotel.util.ConnectionUtil;
import hotel.util.Constant;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Dao
public class HotelRoomClassDaoImpl implements HotelRoomClassDao, Constant {
    private static final Logger logger = LogManager.getLogger(HotelRoomClassDaoImpl.class);

    @Override
    public HotelRoomClass create(HotelRoomClass hotelRoomClass) throws DataProcessingException {
        String query = "INSERT INTO hotel_room_classes (name, description) "
                + "VALUES (?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement statement =
                         connection.prepareStatement(query,
                                 Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, hotelRoomClass.getName());
            statement.setString(2, hotelRoomClass.getDescription());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                hotelRoomClass.setId(resultSet.getObject(1, Long.class));
            }
            logger.info("Request to the database to create room class " + hotelRoomClass
                    + " was successful");
            return hotelRoomClass;
        } catch (SQLException e) {
            logger.warn("Request to the database to create room class" + hotelRoomClass
                    + " failed " + e);
            throw new DataProcessingException("Couldn't create room class "
                    + hotelRoomClass + ". ", e);
        }
    }

    @Override
    public Optional<HotelRoomClass> get(Long roomClassId) throws DataProcessingException {
        String query = "SELECT * FROM hotel_room_classes WHERE id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement statement =
                         connection.prepareStatement(query)) {
            statement.setLong(1, roomClassId);
            ResultSet resultSet = statement.executeQuery();
            HotelRoomClass hotelRoomClass = null;
            if (resultSet.next()) {
                hotelRoomClass = parseRoomClassFromResultSet(resultSet);
            }
            logger.info("Request to the database to get room class by id "
                    + roomClassId + " was successful");
            return Optional.ofNullable(hotelRoomClass);
        } catch (SQLException e) {
            logger.warn("Request to the database to get room class by id "
                    + roomClassId + " failed " + e);
            throw new DataProcessingException("Couldn't get room class by id "
                    + roomClassId, e);
        }
    }

    @Override
    public List<HotelRoomClass> getAll() throws DataProcessingException {
        String query = "SELECT * FROM hotel_room_classes ORDER BY name";
        List<HotelRoomClass> hotelRoomClasses = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement statement =
                         connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                hotelRoomClasses.add(parseRoomClassFromResultSet(resultSet));
            }
            logger.info("Request to the database to get list of all room classes"
                    + " was successful");
            return hotelRoomClasses;
        } catch (SQLException e) {
            logger.warn("Request to the database to get list of all room classes"
                    + " failed " + e);
            throw new DataProcessingException("Couldn't get list of all room class from DB.",
                    e);
        }
    }

    @Override
    public List<HotelRoomClass> getAllWithoutDeleted() throws DataProcessingException {
        String query = "SELECT * FROM hotel_room_classes WHERE is_deleted = FALSE";
        List<HotelRoomClass> hotelRoomClasses = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement statement =
                         connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                hotelRoomClasses.add(parseRoomClassFromResultSet(resultSet));
            }
            logger.info("Request to the database to get list of all without deleted"
                    + " room classes was successful");
            return hotelRoomClasses;
        } catch (SQLException e) {
            logger.warn("Request to the database to get list of all without deleted"
                    + " room classes failed " + e);
            throw new DataProcessingException("Couldn't get list of room class from DB.",
                    e);
        }
    }

    @Override
    public HotelRoomClass update(HotelRoomClass hotelRoomClass) throws DataProcessingException {
        String query = "UPDATE hotel_room_classes "
                + "SET name = ?, description = ?, is_deleted = FALSE "
                + "WHERE id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement statement
                         = connection.prepareStatement(query)) {
            statement.setString(1, hotelRoomClass.getName());
            statement.setString(2, hotelRoomClass.getDescription());
            statement.setLong(3, hotelRoomClass.getId());
            statement.executeUpdate();
            logger.info("Request to the database to update room class " + hotelRoomClass
                    + " was successful");
            return hotelRoomClass;
        } catch (SQLException e) {
            logger.warn("Request to the database to update room class" + hotelRoomClass
                    + " failed " + e);
            throw new DataProcessingException("Couldn't update "
                    + hotelRoomClass + " in DB.", e);
        }
    }

    @Override
    public boolean delete(Long roomClassId) throws DataProcessingException {
        String query = "UPDATE hotel_room_classes SET is_deleted = TRUE WHERE id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement statement =
                         connection.prepareStatement(query)) {
            statement.setLong(1, roomClassId);
            boolean result = statement.executeUpdate() > 0;
            logger.info("Request to the database to delete room class with id "
                    + roomClassId + " was successful");
            return result;
        } catch (SQLException e) {
            logger.warn("Request to the database to delete room class by id "
                    + roomClassId + " failed " + e);
            throw new DataProcessingException("Couldn't delete room class with id "
                    + roomClassId, e);
        }
    }

    @Override
    public Optional<HotelRoomClass> getByName(String name) throws DataProcessingException {
        String query = "SELECT * FROM hotel_room_classes WHERE name = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement statement =
                         connection.prepareStatement(query)) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            HotelRoomClass hotelRoomClass = null;
            if (resultSet.next()) {
                hotelRoomClass = parseRoomClassFromResultSet(resultSet);
            }
            logger.info("Request to the database to get room class by name "
                    + name + " was successful");
            return Optional.ofNullable(hotelRoomClass);
        } catch (SQLException e) {
            logger.warn("Request to the database to get room class by name "
                    + name + " failed " + e);
            throw new DataProcessingException("Couldn't get room by name " + name, e);
        }
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

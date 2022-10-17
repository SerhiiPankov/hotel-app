package hotel.dao.impl;

import hotel.dao.UserDao;
import hotel.dto.UserDto;
import hotel.dto.list.UsersDto;
import hotel.exception.DataProcessingException;
import hotel.lib.Dao;
import hotel.model.User;
import hotel.model.enums.Role;
import hotel.util.ConnectionUtil;
import hotel.util.Constant;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Dao
public class UserDaoImpl implements UserDao, Constant {
    @Override
    public List<User> getAll() throws DataProcessingException {
        String query = "SELECT * FROM users WHERE is_deleted = FALSE ORDER BY id";
        List<User> users = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                users.add(parseUserFromResultSet(resultSet));
            }
            return users;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get a list of users from DB.",
                    e);
        }
    }

    @Override
    public UsersDto getAllByRole(String orderBy, int startRecord,
                                 int recordsPerPage, Role... roles) throws DataProcessingException {
        String query = "SELECT SQL_CALC_FOUND_ROWS * FROM users "
                + "WHERE role IN ("
                + Arrays.stream(roles).map(r -> "?")
                .collect(Collectors.joining(", ")) + ") "
                + "ORDER BY " + orderBy
                + " LIMIT ?, ?";
        String queryCount = "SELECT FOUND_ROWS()";
        List<UserDto> users = new ArrayList<>();
        int numberOfRecords = 0;
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement statement =
                         connection.prepareStatement(query);
                 PreparedStatement statementCount =
                         connection.prepareStatement(queryCount)) {
            connection.setAutoCommit(false);
            int rolesLength = roles.length;
            for (int i = 0; i < rolesLength; i++) {
                statement.setString(i + 1, roles[i].name());
            }
            statement.setInt(rolesLength + 1, startRecord);
            statement.setInt(rolesLength + 2, recordsPerPage);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                users.add(parseUserDtoFromResultSet(resultSet));
            }
            ResultSet resultSetCount = statementCount.executeQuery();
            if (resultSetCount.next()) {
                numberOfRecords = resultSetCount.getInt(1);
            }
            UsersDto usersDto = new UsersDto();
            usersDto.setUsers(users);
            usersDto.setNumberOfUsers(numberOfRecords);
            return usersDto;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get a list of users from DB.",
                    e);
        }
    }

    @Override
    public Optional<User> get(Long userId) throws DataProcessingException {
        String query = "SELECT * FROM users WHERE id = ? AND is_deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            User user = null;
            if (resultSet.next()) {
                user = parseUserFromResultSet(resultSet);
            }
            return Optional.ofNullable(user);
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get user by id " + userId, e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) throws DataProcessingException {
        String query = "SELECT * FROM users WHERE email = ? AND is_deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            User user = null;
            if (resultSet.next()) {
                user = parseUserFromResultSet(resultSet);
            }
            return Optional.ofNullable(user);
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get user by email " + email, e);
        }
    }

    @Override
    public User create(User user) throws DataProcessingException {
        String query = "INSERT INTO users (email, password, salt, name, phone, role) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query,
                         Statement.RETURN_GENERATED_KEYS)) {
            setUserParameterToStatement(statement, user);
            statement.setString(6, user.getRole().name());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getObject(1, Long.class));
            }
            return user;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't create "
                    + user + ". ", e);
        }
    }

    @Override
    public User update(User user) throws DataProcessingException {
        String query = "UPDATE users "
                + "SET email = ?, password = ?, salt = ?, name = ?, phone = ?, is_deleted = FALSE "
                + "WHERE id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement
                        = connection.prepareStatement(query)) {
            setUserParameterToStatement(statement, user);
            statement.setLong(6, user.getId());
            statement.executeUpdate();
            return user;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't update "
                    + user + " in DB.", e);
        }
    }

    @Override
    public void delete(Long userId) throws DataProcessingException {
        String query = "UPDATE users SET is_deleted = TRUE WHERE id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            statement.execute();
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't delete user with id " + userId, e);
        }
    }

    @Override
    public void restore(long userId) throws DataProcessingException {
        String query = "UPDATE users SET is_deleted = FALSE WHERE id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            statement.execute();
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't restore user with id " + userId, e);
        }
    }

    @Override
    public void setLanguage(Long userId, String language) throws DataProcessingException {
        String query = "UPDATE users SET language = ? WHERE id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, language);
            statement.setLong(2, userId);
            statement.execute();
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't update language for user with id "
                    + userId, e);
        }
    }

    private void setUserParameterToStatement(PreparedStatement preparedStatement, User user)
            throws SQLException {
        preparedStatement.setString(1, user.getEmail());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.setBytes(3, user.getSalt());
        preparedStatement.setString(4, user.getName());
        preparedStatement.setString(5, user.getPhone());
    }

    private User parseUserFromResultSet(ResultSet resultSet) throws SQLException {
        return new User.Builder(resultSet.getNString(COLUMN_NAME_EMAIL))
                .setId(resultSet.getObject(COLUMN_NAME_ID, Long.class))
                .setPassword(resultSet.getNString(COLUMN_NAME_PASSWORD))
                .setSalt(resultSet.getBytes(COLUMN_NAME_SALT))
                .setName(resultSet.getNString(COLUMN_NAME_NAME))
                .setPhone(resultSet.getNString(COLUMN_NAME_PHONE))
                .setRole(Role.valueOf(resultSet.getNString(COLUMN_NAME_ROLE)))
                .setLanguage(resultSet.getNString(COLUMN_NAME_LANGUAGE))
                .build();
    }

    private UserDto parseUserDtoFromResultSet(ResultSet resultSet) throws SQLException {
        UserDto userDto = new UserDto();
        userDto.setId(resultSet.getObject(COLUMN_NAME_ID, Long.class));
        userDto.setEmail(resultSet.getNString(COLUMN_NAME_EMAIL));
        userDto.setName(resultSet.getNString(COLUMN_NAME_NAME));
        userDto.setPhone(resultSet.getNString(COLUMN_NAME_PHONE));
        userDto.setRole(Role.valueOf(resultSet.getNString(COLUMN_NAME_ROLE)));
        userDto.setDeleted(resultSet.getBoolean(COLUMN_NAME_IS_DELETED));
        return userDto;
    }
}

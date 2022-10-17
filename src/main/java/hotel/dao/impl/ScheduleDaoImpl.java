package hotel.dao.impl;

import hotel.dao.ScheduleDao;
import hotel.dto.ScheduleDto;
import hotel.dto.list.SchedulesDto;
import hotel.exception.DataProcessingException;
import hotel.lib.Dao;
import hotel.model.Schedule;
import hotel.model.enums.BookingStatus;
import hotel.util.ConnectionUtil;
import hotel.util.Constant;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Dao
public class ScheduleDaoImpl implements ScheduleDao, Constant {
    @Override
    public Schedule add(Schedule schedule) throws DataProcessingException {
        String query = "INSERT INTO schedules (hotel_room_id, day, price, booking_status) "
                + "VALUES (?, ?, ?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement preparedStatement =
                         connection.prepareStatement(query)) {
            preparedStatement.setLong(1, schedule.getHotelRoomId());
            preparedStatement.setDate(2, Date.valueOf(schedule.getDay()));
            preparedStatement.setBigDecimal(3, schedule.getPrice());
            preparedStatement.setString(4, schedule.getBookingStatus().name());
            preparedStatement.execute();
            return schedule;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't add "
                    + schedule + ". ", e);
        }
    }

    @Override
    public Schedule update(Schedule schedule) throws DataProcessingException {
        String query = "UPDATE schedules "
                + "SET price = ?, booking_status = ? "
                + "WHERE hotel_room_id = ? AND day = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement preparedStatement =
                         connection.prepareStatement(query)) {
            preparedStatement.setBigDecimal(1, schedule.getPrice());
            preparedStatement.setString(2, schedule.getBookingStatus().name());
            preparedStatement.setLong(3, schedule.getHotelRoomId());
            preparedStatement.setDate(4, Date.valueOf(schedule.getDay()));
            preparedStatement.executeUpdate();
            return schedule;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't update "
                    + schedule + " in DB.", e);
        }
    }

    @Override
    public int deleteExpiredSchedule(LocalDate date) throws DataProcessingException {
        String query = "UPDATE schedules "
                + "SET booking_status = 'UNAVAILABLE' "
                + "WHERE booking_status = 'FREE' AND day < ?";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement preparedStatement =
                         connection.prepareStatement(query)) {
            preparedStatement.setDate(1, Date.valueOf(date));
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't delete expired schedule ", e);
        }
    }

    @Override
    public List<ScheduleDto> getAllByHotelRoomIdAndDateInterval(
            long hotelRoomId, LocalDate fromDate, LocalDate toDate) throws DataProcessingException {
        String query = "SELECT * FROM schedules "
                + "WHERE hotel_room_id = ? AND day BETWEEN ? AND ?";
        List<ScheduleDto> schedules = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement preparedStatement =
                         connection.prepareStatement(query)) {
            setSearchParameterToStatement(preparedStatement, hotelRoomId, fromDate, toDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                schedules.add(parseScheduleFromResultSet(resultSet));
            }
            return schedules;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get a list of schedules from DB.",
                    e);
        }
    }

    @Override
    public int countFreeDaysByInterval(
            long hotelRoomId, LocalDate fromDate, LocalDate toDate) throws DataProcessingException {
        String query = "SELECT COUNT(*) AS free_days FROM schedules "
                + "WHERE hotel_room_id = ? "
                + "AND booking_status = 'FREE' "
                + "AND day BETWEEN ? AND ?";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement preparedStatement =
                         connection.prepareStatement(query)) {
            setSearchParameterToStatement(preparedStatement, hotelRoomId, fromDate, toDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            int countFreeDays = 0;
            if (resultSet.next()) {
                countFreeDays = resultSet.getInt("free_days");
            }
            return countFreeDays;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get a list of schedules from DB.",
                    e);
        }
    }

    @Override
    public SchedulesDto getAllByHotelRoomId(long hotelRoomId, int startRecord, int recordsPerPage)
            throws DataProcessingException {
        String query = "SELECT SQL_CALC_FOUND_ROWS * FROM schedules "
                + "WHERE hotel_room_id = ? AND day >= ? ORDER BY day LIMIT ?, ?";
        String queryCount = "SELECT FOUND_ROWS()";
        List<ScheduleDto> schedules = new ArrayList<>();
        int numberOfRecords = 0;
        Connection connection = ConnectionUtil.getConnection();
        try (PreparedStatement statement =
                         connection.prepareStatement(query);
                 PreparedStatement statementCount =
                         connection.prepareStatement(queryCount)) {
            connection.setAutoCommit(false);
            statement.setLong(1, hotelRoomId);
            statement.setDate(2, Date.valueOf(LocalDate.now()));
            statement.setInt(3, startRecord);
            statement.setInt(4, recordsPerPage);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                schedules.add(parseScheduleFromResultSet(resultSet));
            }
            ResultSet resultSetCount = statementCount.executeQuery();
            if (resultSetCount.next()) {
                numberOfRecords = resultSetCount.getInt(1);
            }
            SchedulesDto schedulesDto = new SchedulesDto();
            schedulesDto.setSchedules(schedules);
            schedulesDto.setNumberOfSchedules(numberOfRecords);
            connection.commit();
            connection.setAutoCommit(true);
            connection.close();
            return schedulesDto;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get a list of schedules from DB.",
                    e);
        }
    }

    private void setSearchParameterToStatement(PreparedStatement preparedStatement,
                                               long hotelRoomId, LocalDate fromDate,
                                               LocalDate toDate) throws SQLException {
        preparedStatement.setLong(1, hotelRoomId);
        preparedStatement.setDate(2,Date.valueOf(fromDate));
        preparedStatement.setDate(3,Date.valueOf(toDate));
    }

    private ScheduleDto parseScheduleFromResultSet(ResultSet resultSet) throws SQLException {
        ScheduleDto scheduleDto = new ScheduleDto();
        scheduleDto.setHotelRoomId(resultSet.getObject(COLUMN_NAME_HOTEL_ROOM_ID, Long.class));
        scheduleDto.setDay(resultSet.getDate(COLUMN_NAME_DAY).toLocalDate());
        scheduleDto.setPrice(resultSet.getBigDecimal(COLUMN_NAME_PRICE));
        scheduleDto.setBookingStatus(BookingStatus.valueOf(
                resultSet.getNString(COLUMN_NAME_BOOKING_STATUS)));
        return scheduleDto;
    }
}

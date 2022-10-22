package hotel.dao.impl;

import hotel.dao.RequestDao;
import hotel.dto.RequestDto;
import hotel.dto.list.RequestsDto;
import hotel.exception.DataProcessingException;
import hotel.lib.Dao;
import hotel.model.Request;
import hotel.util.ConnectionUtil;
import hotel.util.Constant;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Dao
public class RequestDaoImpl implements RequestDao, Constant {

    @Override
    public RequestsDto getAllDto(int startRecord, int recordsPerPage, boolean isProcessed)
            throws DataProcessingException {
        String query = "SELECT br.id AS id, hrc.name AS ac_name, u.email AS email, "
                + "date, number_of_guests, check_in, check_out, is_processed "
                + "FROM booking_requests br "
                + "JOIN hotel_room_classes hrc ON hrc.id = br.hotel_room_class_id "
                + "JOIN users u on u.id = br.customer_id "
                + "WHERE is_processed = ? "
                + "ORDER BY date DESC "
                + "LIMIT ?, ?";
        String queryCount = "SELECT COUNT(*) AS number_of_request "
                + "FROM booking_requests br "
                + "JOIN hotel_room_classes hrc ON hrc.id = br.hotel_room_class_id "
                + "JOIN users u on u.id = br.customer_id "
                + "WHERE is_processed = ? ";
        List<RequestDto> requests = new ArrayList<>();
        int numberOfRecords = 0;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement =
                         connection.prepareStatement(query);
                PreparedStatement statementCount =
                         connection.prepareStatement(queryCount)) {
            connection.setAutoCommit(false);
            statement.setBoolean(1, isProcessed);
            statement.setInt(2, startRecord);
            statement.setInt(3, recordsPerPage);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                requests.add(parseBookingRequestDtoFromResultSet(resultSet));
            }
            statementCount.setBoolean(1, isProcessed);
            ResultSet resultSetCount = statementCount.executeQuery();
            if (resultSetCount.next()) {
                numberOfRecords = resultSetCount.getInt(COLUMN_NAME_NUMBER_OF_REQUEST);
            }
            RequestsDto requestsDto = new RequestsDto();
            requestsDto.setRequests(requests);
            requestsDto.setNumberOfRequests(numberOfRecords);
            connection.setAutoCommit(true);
            return requestsDto;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get a list of booking requests from DB.",
                    e);
        }
    }

    @Override
    public RequestsDto getAllDtoByCustomerId(Long customerId, int startRecord,
                                             int recordsPerPage, boolean isProcessed)
            throws DataProcessingException {
        String query = "SELECT SQL_CALC_FOUND_ROWS "
                + "br.id AS id, hrc.name AS ac_name, u.email AS email,"
                + "date, number_of_guests, check_in, check_out, is_processed "
                + "FROM booking_requests br "
                + "JOIN hotel_room_classes hrc ON hrc.id = br.hotel_room_class_id "
                + "JOIN users u on u.id = br.customer_id "
                + "WHERE customer_id = ? AND is_processed = ? "
                + "ORDER BY date DESC "
                + "LIMIT ?, ?";
        String queryCount = "SELECT FOUND_ROWS()";
        List<RequestDto> requests = new ArrayList<>();
        int numberOfRecords = 0;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement =
                         connection.prepareStatement(query);
                PreparedStatement statementCount =
                         connection.prepareStatement(queryCount)) {
            connection.setAutoCommit(false);
            statement.setLong(1, customerId);
            statement.setBoolean(2, isProcessed);
            statement.setInt(3, startRecord);
            statement.setInt(4, recordsPerPage);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                requests.add(parseBookingRequestDtoFromResultSet(resultSet));
            }
            ResultSet resultSetCount = statementCount.executeQuery();
            if (resultSetCount.next()) {
                numberOfRecords = resultSetCount.getInt(1);
            }
            RequestsDto requestsDto = new RequestsDto();
            requestsDto.setRequests(requests);
            requestsDto.setNumberOfRequests(numberOfRecords);
            connection.setAutoCommit(true);
            return requestsDto;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get a list of booking requests from DB.",
                    e);
        }
    }

    @Override
    public Optional<Request> get(Long bookingRequestId) throws DataProcessingException {
        String query = "SELECT * FROM booking_requests WHERE id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement preparedStatement =
                         connection.prepareStatement(query)) {
            preparedStatement.setLong(1, bookingRequestId);
            ResultSet resultSet = preparedStatement.executeQuery();
            Request request = null;
            if (resultSet.next()) {
                request = parseBookingRequestFromResultSet(resultSet);
            }
            return Optional.ofNullable(request);
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get booking request by id "
                    + bookingRequestId, e);
        }
    }

    @Override
    public int getNumberOfBookingRequestsPerCustomer(Long customerId)
            throws DataProcessingException {
        String query = "SELECT COUNT(*) AS number_of_booking_requests "
                + "FROM booking_requests "
                + "WHERE customer_id = ? AND is_processed = 0";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement preparedStatement =
                         connection.prepareStatement(query)) {
            preparedStatement.setLong(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("number_of_booking_requests");
            }
            return 0;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't count booking request for user with id "
                    + customerId, e);
        }
    }

    @Override
    public int getNumberOfBookingRequests() throws DataProcessingException {
        String query = "SELECT COUNT(*) AS number_of_booking_requests "
                + "FROM booking_requests "
                + "WHERE is_processed = 0";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement preparedStatement =
                         connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("number_of_booking_requests");
            }
            return 0;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't count booking request ", e);
        }
    }

    @Override
    public Request create(Request request) throws DataProcessingException {
        String query = "INSERT INTO booking_requests "
                + "(date, customer_id, hotel_room_class_id, number_of_guests, check_in, check_out) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement preparedStatement =
                          connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS)) {
            setBookingRequestParameterToStatement(preparedStatement, request);
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                request.setId(resultSet.getObject(1, Long.class));
            }
            return request;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't create "
                    + request + ". ", e);
        }
    }

    @Override
    public boolean delete(Long requestId) throws DataProcessingException {
        String query = "DELETE FROM booking_requests WHERE id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement preparedStatement =
                         connection.prepareStatement(query)) {
            preparedStatement.setLong(1, requestId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't delete booking request with id "
                    + requestId, e);
        }
    }

    @Override
    public void setIsProcessing(long requestId) throws DataProcessingException {
        String query = "UPDATE booking_requests SET is_processed = true "
                + "WHERE id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement preparedStatement =
                         connection.prepareStatement(query)) {
            preparedStatement.setLong(1, requestId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't set isProcessing TRUE "
                    + "for booking request with id " + requestId, e);
        }
    }

    private void setBookingRequestParameterToStatement(PreparedStatement statement,
                                                       Request request)
            throws SQLException {
        statement.setDate(1, Date.valueOf(request.getDate()));
        statement.setLong(2, request.getCustomerId());
        statement.setLong(3, request.getHotelRoomClassId());
        statement.setInt(4, request.getNumberOfGuests());
        statement.setDate(5, Date.valueOf(request.getCheckIn()));
        statement.setDate(6, Date.valueOf(request.getCheckOut()));
    }

    private Request parseBookingRequestFromResultSet(ResultSet resultSet)
            throws SQLException {
        Request request = new Request();
        request.setId(resultSet.getObject(COLUMN_NAME_ID, Long.class));
        request.setDate(resultSet.getDate(COLUMN_NAME_DATE).toLocalDate());
        request.setCustomerId(
                resultSet.getObject(COLUMN_NAME_CUSTOMER_ID, Long.class));
        request.setHotelRoomClassId(
                resultSet.getObject(COLUMN_NAME_HOTEL_ROOM_CLASS_ID, Long.class));
        request.setNumberOfGuests(
                resultSet.getObject(COLUMN_NAME_NUMBER_OF_GUESTS, Integer.class));
        request.setCheckIn(resultSet.getDate(COLUMN_NAME_CHECK_IN).toLocalDate());
        request.setCheckOut(resultSet.getDate(COLUMN_NAME_CHECK_OUT).toLocalDate());
        request.setProcessed(resultSet.getBoolean(COLUMN_NAME_IS_PROCESSED));
        return request;
    }

    private RequestDto parseBookingRequestDtoFromResultSet(ResultSet resultSet)
            throws SQLException {
        RequestDto requestDto = new RequestDto();
        requestDto.setRequestId(
                resultSet.getObject(COLUMN_NAME_ID, Long.class));
        requestDto.setEmail(resultSet.getNString(COLUMN_NAME_EMAIL));
        requestDto.setDate(resultSet.getDate(COLUMN_NAME_DATE).toLocalDate());
        requestDto.setHotelRoomClassName(
                resultSet.getNString(COLUMN_NAME_HOTEL_ROOM_CLASS_NAME));
        requestDto.setNumberOfGuests(
                resultSet.getObject(COLUMN_NAME_NUMBER_OF_GUESTS, Integer.class));
        requestDto.setCheckIn(resultSet.getDate(COLUMN_NAME_CHECK_IN).toLocalDate());
        requestDto.setCheckOut(resultSet.getDate(COLUMN_NAME_CHECK_OUT).toLocalDate());
        requestDto.setProcessed(resultSet.getBoolean(COLUMN_NAME_IS_PROCESSED));
        return requestDto;
    }
}

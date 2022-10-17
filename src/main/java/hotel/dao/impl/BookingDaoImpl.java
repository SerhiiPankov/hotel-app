package hotel.dao.impl;

import hotel.dao.BookingDao;
import hotel.dto.BookingDto;
import hotel.dto.list.BookingsDto;
import hotel.exception.DataProcessingException;
import hotel.lib.Dao;
import hotel.model.Booking;
import hotel.model.enums.BookingStatus;
import hotel.model.enums.PaymentStatus;
import hotel.util.ConnectionUtil;
import hotel.util.Constant;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Dao
public class BookingDaoImpl implements BookingDao, Constant {
    private static final Logger logger = LogManager.getLogger(BookingDaoImpl.class);

    @Override
    public BookingsDto getAll(int startRecord, int recordsPerPage, PaymentStatus paymentStatus)
            throws DataProcessingException {
        String query = "SELECT SQL_CALC_FOUND_ROWS "
                + "b.id AS id, date, hr.number AS number, ac.name AS class_name, "
                + "check_in, check_out, b.number_of_guests AS number_of_guests, "
                + "total_price, payment_status, u.email AS email "
                + "FROM booking b "
                + "JOIN hotel_rooms hr ON hr.id = b.hotel_room_id "
                + "JOIN hotel_room_classes ac on ac.id = hr.hotel_room_class_id "
                + "JOIN users u ON customer_id = u.id "
                + "WHERE payment_status = ? "
                + "ORDER BY date DESC, id "
                + "LIMIT ?, ?";
        String queryCount = "SELECT FOUND_ROWS()";
        List<BookingDto> bookings = new ArrayList<>();
        int numberOfRecords = 0;
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement statement =
                         connection.prepareStatement(query);
                 PreparedStatement statementCount =
                         connection.prepareStatement(queryCount)) {
            connection.setAutoCommit(false);
            statement.setString(1, paymentStatus.name());
            statement.setInt(2, startRecord);
            statement.setInt(3, recordsPerPage);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                bookings.add(parseBookingDtoFromResultSet(resultSet));
            }
            ResultSet resultSetCount = statementCount.executeQuery();
            if (resultSetCount.next()) {
                numberOfRecords = resultSetCount.getInt(1);
            }
            BookingsDto bookingsDto = new BookingsDto();
            bookingsDto.setBookings(bookings);
            bookingsDto.setNumberOfBookings(numberOfRecords);
            logger.info("Request to the database to get a list of all booking"
                    + " was successful");
            connection.setAutoCommit(true);
            return bookingsDto;
        } catch (SQLException e) {
            logger.warn("Request to the database to get a list of all booking"
                    + " failed " + e);
            throw new DataProcessingException("Couldn't get a list of booking from DB.", e);
        }
    }

    @Override
    public BookingsDto getAllByCustomerId(Long customerId, int startRecord,
                                          int recordsPerPage, PaymentStatus paymentStatus)
            throws DataProcessingException {
        String query = "SELECT SQL_CALC_FOUND_ROWS "
                + "b.id AS id, date, hr.number AS number, hrc.name AS class_name, "
                + "check_in, check_out, b.number_of_guests AS number_of_guests, "
                + "total_price, payment_status, u.email AS email "
                + "FROM booking b "
                + "JOIN hotel_rooms hr ON hr.id = b.hotel_room_id "
                + "JOIN hotel_room_classes hrc on hrc.id = hr.hotel_room_class_id "
                + "JOIN users u ON customer_id = u.id "
                + "WHERE customer_id = ? AND payment_status = ? "
                + "ORDER BY date DESC, id "
                + "LIMIT ?, ?";
        String queryCount = "SELECT FOUND_ROWS()";
        List<BookingDto> bookings = new ArrayList<>();
        int numberOfRecords = 0;
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement statement =
                         connection.prepareStatement(query);
                 PreparedStatement statementCount =
                         connection.prepareStatement(queryCount)) {
            statement.setLong(1, customerId);
            statement.setString(2, paymentStatus.name());
            statement.setInt(3, startRecord);
            statement.setInt(4, recordsPerPage);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                bookings.add(parseBookingDtoFromResultSet(resultSet));
            }
            ResultSet resultSetCount = statementCount.executeQuery();
            if (resultSetCount.next()) {
                numberOfRecords = resultSetCount.getInt(1);
            }
            BookingsDto bookingsDto = new BookingsDto();
            bookingsDto.setBookings(bookings);
            bookingsDto.setNumberOfBookings(numberOfRecords);
            logger.info("Request to the database to get a list of all booking by customer id "
                    + customerId + " was successful");
            connection.setAutoCommit(true);
            return bookingsDto;
        } catch (SQLException e) {
            logger.warn("Request to the database to get a list of all booking by customer id "
                    + customerId + " failed " + e);
            throw new DataProcessingException("Couldn't get a list of all booking from DB.", e);
        }
    }

    @Override
    public List<Booking> getAllByDate(LocalDate date) throws DataProcessingException {
        String query = "SELECT * FROM booking "
                + "WHERE (date <= ? AND payment_status = ?) "
                + "OR (date <= ? AND payment_status = ?)";
        List<Booking> overdueBooking = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement preparedStatement =
                         connection.prepareStatement(query)) {
            preparedStatement.setDate(1, Date.valueOf(date));
            preparedStatement.setString(2, PaymentStatus.INVOICE.name());
            preparedStatement.setDate(3,
                    Date.valueOf(date.plusDays(SUBTRACT_DAY_AUTO_DELETE_PROPOSAL)));
            preparedStatement.setString(4, PaymentStatus.PROPOSAL.name());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                overdueBooking.add(parseBookingFromResultSet(resultSet));
            }
            logger.info("Request to the database to get a list of all booking by date "
                    + date + " was successful");
            return overdueBooking;
        } catch (SQLException e) {
            logger.warn("Request to the database to get a list of all booking by date "
                    + date + " failed " + e);
            throw new DataProcessingException("Couldn't get a list of booking from DB.", e);
        }
    }

    @Override
    public Booking getById(Long bookingId) throws DataProcessingException {
        String query = "SELECT * FROM booking WHERE id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement preparedStatement =
                         connection.prepareStatement(query)) {
            preparedStatement.setLong(1, bookingId);
            ResultSet resultSet = preparedStatement.executeQuery();
            Booking booking = null;
            if (resultSet.next()) {
                booking = parseBookingFromResultSet(resultSet);
            }
            logger.info("Request to the database to get a booking by id "
                    + bookingId + " was successful");
            return booking;
        } catch (SQLException e) {
            logger.warn("Request to the database to get a booking by id "
                    + bookingId + " failed " + e);
            throw new DataProcessingException("Couldn't get a booking from DB by id: " + bookingId,
                    e);
        }
    }

    @Override
    public BookingDto getBookingDtoById(Long bookingId) throws DataProcessingException {
        String query = "SELECT b.id AS id, date, hr.number AS number, ac.name AS class_name, "
                + "check_in, check_out, b.number_of_guests AS number_of_guests, "
                + "total_price, payment_status, customer_id "
                + "FROM booking b "
                + "JOIN hotel_rooms hr ON hr.id = b.hotel_room_id "
                + "JOIN hotel_room_classes ac on ac.id = hr.hotel_room_class_id "
                + "JOIN users u ON customer_id = u.id "
                + "WHERE b.id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement preparedStatement =
                         connection.prepareStatement(query)) {
            preparedStatement.setLong(1, bookingId);
            ResultSet resultSet = preparedStatement.executeQuery();
            BookingDto bookingDto = null;
            if (resultSet.next()) {
                bookingDto = parseBookingDtoFromResultSet(resultSet);
            }
            return bookingDto;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get a booking from DB by id: " + bookingId,
                    e);
        }
    }

    @Override
    public BigDecimal getTotalPrice(Long hotelRoomId, LocalDate fromDate, LocalDate toDate)
            throws DataProcessingException {
        String query = "SELECT SUM(s.price) AS total "
                + "FROM schedules s "
                + "WHERE hotel_room_id = ? AND day BETWEEN ? AND ?";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            setScheduleParameterToStatement(preparedStatement, hotelRoomId, fromDate, toDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            BigDecimal total = null;
            if (resultSet.next()) {
                total = resultSet.getBigDecimal("total");
            }
            return total;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get a total sum from DB.",
                    e);
        }
    }

    @Override
    public int getNumberOfBooking(PaymentStatus paymentStatus) throws DataProcessingException {
        String query = "SELECT COUNT(*) AS number_of_booking "
                + "FROM booking "
                + "WHERE payment_status = ? AND check_out >= ?";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement preparedStatement =
                         connection.prepareStatement(query)) {
            preparedStatement.setString(1, paymentStatus.name());
            preparedStatement.setDate(2, Date.valueOf(LocalDate.now()));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(COLUMN_NAME_NUMBER_OF_BOOKING);
            }
            return 0;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't count booking ", e);
        }
    }

    @Override
    public int getNumberOfBookingPerCustomer(Long customerId, PaymentStatus paymentStatus)
            throws DataProcessingException {
        String query = "SELECT COUNT(*) AS number_of_booking "
                + "FROM booking "
                + "WHERE customer_id = ? AND payment_status = ? "
                + "AND check_out >= ?";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement preparedStatement =
                         connection.prepareStatement(query)) {
            preparedStatement.setLong(1, customerId);
            preparedStatement.setString(2, paymentStatus.name());
            preparedStatement.setDate(3, Date.valueOf(LocalDate.now()));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(COLUMN_NAME_NUMBER_OF_BOOKING);
            }
            return 0;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't count booking for user with id "
                    + customerId, e);
        }
    }

    @Override
    public Booking createBooking(Booking booking) throws DataProcessingException {
        String querySchedule = "UPDATE schedules SET booking_status = 'OFFER' "
                + "WHERE hotel_room_id = ? AND day BETWEEN ? AND ?";
        String queryBooking = "INSERT INTO booking "
                + "(date, hotel_room_id, check_in, check_out, number_of_guests, "
                + "total_price, customer_id, manager_id, payment_status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection connection = ConnectionUtil.getConnection();
        try (PreparedStatement statementSchedule =
                     connection.prepareStatement(querySchedule);
                 PreparedStatement statementBooking =
                         connection.prepareStatement(queryBooking,
                             Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            setScheduleParameterToStatement(statementSchedule, booking.getHotelRoomId(),
                    booking.getCheckin(), booking.getCheckout());
            setBookingParameterToStatement(statementBooking, booking);
            statementSchedule.executeUpdate();
            statementBooking.executeUpdate();
            ResultSet resultSet = statementBooking.getGeneratedKeys();
            if (resultSet.next()) {
                booking.setId(resultSet.getObject(1, Long.class));
            }
            connection.commit();
            return booking;
        } catch (SQLException e) {
            try {
                connection.rollback();
                throw new DataProcessingException("Couldn't create booking " + booking, e);
            } catch (SQLException ex) {
                throw new DataProcessingException("Couldn't rollback transaction " + booking, ex);
            }
        } finally {
            try {
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException e) {
                logger.warn("Couldn't set auto commit " + booking, e);
                throw new DataProcessingException("Couldn't set auto commit " + booking, e);
            }
        }
    }

    @Override
    public Booking createReserve(Booking booking, Long bookingRequestId)
            throws DataProcessingException {
        String queryBookingRequest = "UPDATE booking_requests SET is_processed = true "
                + "WHERE id = ?";
        String querySchedule = "UPDATE schedules SET booking_status = 'OFFER' "
                + "WHERE hotel_room_id = ? AND day BETWEEN ? AND ?";
        String queryBooking = "INSERT INTO booking "
                + "(date, hotel_room_id, check_in, check_out, number_of_guests, "
                + "total_price, customer_id, manager_id, payment_status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection connection = ConnectionUtil.getConnection();
        try (PreparedStatement statementBookingRequest =
                     connection.prepareStatement(queryBookingRequest);
                PreparedStatement statementSchedule =
                         connection.prepareStatement(querySchedule);
                PreparedStatement statementBooking =
                         connection.prepareStatement(queryBooking,
                             Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            statementBookingRequest.setLong(1, bookingRequestId);
            setScheduleParameterToStatement(statementSchedule, booking.getHotelRoomId(),
                    booking.getCheckin(), booking.getCheckout());
            setBookingParameterToStatement(statementBooking, booking);
            statementBookingRequest.executeUpdate();
            statementSchedule.executeUpdate();
            statementBooking.executeUpdate();
            ResultSet resultSet = statementBooking.getGeneratedKeys();
            if (resultSet.next()) {
                booking.setId(resultSet.getObject(1, Long.class));
            }
            connection.commit();
            return booking;
        } catch (SQLException e) {
            try {
                connection.rollback();
                throw new DataProcessingException("Couldn't create booking " + booking, e);
            } catch (SQLException ex) {
                throw new DataProcessingException("Couldn't rollback transaction " + booking, ex);
            }
        } finally {
            try {
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException e) {
                throw new DataProcessingException("Couldn't set auto commit " + booking, e);
            }
        }
    }

    @Override
    public void updateReserve(Booking booking, PaymentStatus paymentStatus,
                              BookingStatus bookingStatus, LocalDate... dates)
            throws DataProcessingException {
        String date = "";
        if (dates.length > 0) {
            date = " ,date = ? ";
        }
        String queryBooking = "UPDATE booking SET payment_status = ? " + date + " WHERE id = ?";
        String querySchedule = "UPDATE schedules SET booking_status = ? "
                + "WHERE hotel_room_id = ? AND day BETWEEN ? AND ?";
        Connection connection = ConnectionUtil.getConnection();
        try (PreparedStatement statementBooking =
                         connection.prepareStatement(queryBooking);
                 PreparedStatement statementSchedule =
                         connection.prepareStatement(querySchedule)) {
            connection.setAutoCommit(false);
            statementBooking.setString(1, paymentStatus.name());
            if (dates.length > 0) {
                statementBooking.setDate(2, Date.valueOf(dates[0]));
                statementBooking.setLong(3, booking.getId());
            } else {
                statementBooking.setLong(2, booking.getId());
            }
            statementBooking.execute();
            statementSchedule.setString(1, bookingStatus.name());
            statementSchedule.setLong(2, booking.getHotelRoomId());
            statementSchedule.setDate(3, Date.valueOf(booking.getCheckin()));
            statementSchedule.setDate(4, Date.valueOf(booking.getCheckout()));
            statementSchedule.execute();
        } catch (SQLException e) {
            try {
                connection.rollback();
                throw new DataProcessingException("Couldn't update booking with id "
                        + booking.getId(), e);
            } catch (SQLException ex) {
                throw new DataProcessingException("Couldn't rollback transaction " + booking, ex);
            }
        } finally {
            try {
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException e) {
                throw new DataProcessingException("Couldn't set auto commit " + booking, e);
            }
        }
    }

    @Override
    public int deleteBooking(Booking booking) throws DataProcessingException {
        String querySchedule = "UPDATE schedules SET booking_status = 'FREE' "
                + "WHERE hotel_room_id = ? AND day BETWEEN ? AND ?";
        String queryBooking = "UPDATE booking SET payment_status = 'DELETE'"
                + "WHERE id = ?";
        Connection connection = ConnectionUtil.getConnection();
        try (PreparedStatement statementSchedule =
                     connection.prepareStatement(querySchedule);
                 PreparedStatement statementBooking =
                         connection.prepareStatement(queryBooking)) {
            connection.setAutoCommit(false);
            setScheduleParameterToStatement(statementSchedule,
                    booking.getHotelRoomId(), booking.getCheckin(),
                    booking.getCheckout());
            statementBooking.setLong(1, booking.getId());
            int numberOfFreedDays = statementSchedule.executeUpdate();
            int numberOfCanceledBooking = statementBooking.executeUpdate();
            connection.commit();
            logger.info("Request to the database to delete booking "
                    + booking + " was successful. " + numberOfFreedDays + " free days");
            return numberOfCanceledBooking;
        } catch (SQLException e) {
            logger.warn("Request to the database to delete booking "
                    + booking + " failed " + e);
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new DataProcessingException("Couldn't rollback transaction " + booking, ex);
            }
            throw new DataProcessingException("Couldn't create booking " + booking, e);
        } finally {
            try {
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException e) {
                throw new DataProcessingException("Couldn't set auto commit " + booking, e);
            }
        }
    }

    private void setBookingParameterToStatement(PreparedStatement preparedStatement,
                                                Booking booking) throws SQLException {
        preparedStatement.setDate(1, Date.valueOf(booking.getDate()));
        preparedStatement.setLong(2, booking.getHotelRoomId());
        preparedStatement.setDate(3, Date.valueOf(booking.getCheckin()));
        preparedStatement.setDate(4, Date.valueOf(booking.getCheckout()));
        preparedStatement.setInt(5, booking.getNumberOfGuests());
        preparedStatement.setBigDecimal(6, booking.getTotal());
        preparedStatement.setLong(7, booking.getCustomerId());
        preparedStatement.setLong(8, booking.getManagerId());
        preparedStatement.setString(9, booking.getPaymentStatus().name());
    }

    private void setScheduleParameterToStatement(PreparedStatement preparedStatement,
                                                 Long apartmentId, LocalDate fromDate,
                                                 LocalDate toDate) throws SQLException {
        preparedStatement.setLong(1, apartmentId);
        preparedStatement.setDate(2, Date.valueOf(fromDate));
        preparedStatement.setDate(3, Date.valueOf(toDate));

    }

    private Booking parseBookingFromResultSet(ResultSet resultSet) throws SQLException {
        Booking booking = new Booking();
        booking.setId(resultSet.getLong(COLUMN_NAME_ID));
        booking.setDate(resultSet.getDate(COLUMN_NAME_DATE).toLocalDate());
        booking.setHotelRoomId(resultSet.getLong(COLUMN_NAME_HOTEL_ROOM_ID));
        booking.setCheckin(resultSet.getDate(COLUMN_NAME_CHECK_IN).toLocalDate());
        booking.setCheckout(resultSet.getDate(COLUMN_NAME_CHECK_OUT).toLocalDate());
        booking.setNumberOfGuests(resultSet.getInt(COLUMN_NAME_NUMBER_OF_GUESTS));
        booking.setTotal(resultSet.getBigDecimal(COLUMN_NAME_TOTAL_PRICE));
        booking.setCustomerId(resultSet.getLong(COLUMN_NAME_CUSTOMER_ID));
        booking.setManagerId(resultSet.getLong(COLUMN_NAME_MANAGER_ID));
        booking.setPaymentStatus(PaymentStatus.valueOf(
                resultSet.getNString(COLUMN_NAME_PAYMENT_STATUS)));
        return booking;
    }

    private BookingDto parseBookingDtoFromResultSet(ResultSet resultSet) throws SQLException {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(resultSet.getObject(COLUMN_NAME_ID, Long.class));
        bookingDto.setDate(resultSet.getDate(COLUMN_NAME_DATE).toLocalDate());
        bookingDto.setNumber(resultSet.getNString(COLUMN_NAME_NUMBER));
        bookingDto.setApartmentClassName(resultSet.getNString(COLUMN_NAME_CLASS_NAME));
        bookingDto.setCheckin(resultSet.getDate(COLUMN_NAME_CHECK_IN).toLocalDate());
        bookingDto.setCheckout(resultSet.getDate(COLUMN_NAME_CHECK_OUT).toLocalDate());
        bookingDto.setNumberOfGuests(resultSet.getInt(COLUMN_NAME_NUMBER_OF_GUESTS));
        bookingDto.setTotal(resultSet.getBigDecimal(COLUMN_NAME_TOTAL_PRICE));
        bookingDto.setPaymentStatus(PaymentStatus.valueOf(
                resultSet.getNString(COLUMN_NAME_PAYMENT_STATUS)));
        bookingDto.setEmail(resultSet.getNString(COLUMN_NAME_EMAIL));
        return bookingDto;
    }
}

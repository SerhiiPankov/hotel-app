package hotel.service;

import hotel.dto.BookingDto;
import hotel.dto.list.BookingsDto;
import hotel.exception.DataProcessingException;
import hotel.model.Booking;
import hotel.model.enums.BookingStatus;
import hotel.model.enums.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface BookingService {
    BookingsDto getAll(int page, int recordsPerPage, PaymentStatus paymentStatus)
            throws DataProcessingException;

    BookingsDto getAllByCustomerId(Long customerId, int page,
                                   int recordsPerPage, PaymentStatus paymentStatus)
            throws DataProcessingException;

    List<Booking> getAllByDate(LocalDate date) throws DataProcessingException;

    Booking getById(Long bookingId) throws DataProcessingException;

    BookingDto getBookingDtoById(long bookingId) throws DataProcessingException;

    BigDecimal getTotalPrice(Long apartmentId, LocalDate fromDate, LocalDate toDate)
            throws DataProcessingException;

    int getNumberOfBookingPerCustomer(Long customerId, PaymentStatus paymentStatus)
            throws DataProcessingException;

    int getNumberOfBooking(PaymentStatus paymentStatus) throws DataProcessingException;

    long getCustomerIdByBookingId(long bookingId) throws DataProcessingException;

    Booking createBooking(Booking booking) throws DataProcessingException;

    Booking createReserve(Booking booking, Long bookingRequestId)
            throws DataProcessingException;

    void updateReserve(Booking booking, PaymentStatus paymentStatus,
                          BookingStatus bookingStatus, LocalDate... dates)
            throws DataProcessingException;

    int deleteBooking(Booking booking) throws DataProcessingException;
}

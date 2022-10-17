package hotel.service.impl;

import hotel.dao.BookingDao;
import hotel.dto.list.BookingsDto;
import hotel.exception.DataProcessingException;
import hotel.lib.Inject;
import hotel.lib.Service;
import hotel.model.Booking;
import hotel.model.enums.BookingStatus;
import hotel.model.enums.PaymentStatus;
import hotel.service.BookingService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    @Inject
    private BookingDao bookingDao;

    @Override
    public BookingsDto getAll(int page, int recordsPerPage, PaymentStatus paymentStatus)
            throws DataProcessingException {
        int startRecord = (page - 1) * recordsPerPage;
        return bookingDao.getAll(startRecord, recordsPerPage, paymentStatus);
    }

    @Override
    public BookingsDto getAllByCustomerId(Long customerId, int page,
                                          int recordsPerPage, PaymentStatus paymentStatus)
            throws DataProcessingException {
        int startRecord = (page - 1) * recordsPerPage;
        return bookingDao.getAllByCustomerId(
                customerId, startRecord, recordsPerPage, paymentStatus);
    }

    @Override
    public List<Booking> getAllByDate(LocalDate date) throws DataProcessingException {
        return bookingDao.getAllByDate(date);
    }

    @Override
    public Booking getById(Long bookingId) throws DataProcessingException {
        return bookingDao.getById(bookingId);
    }

    @Override
    public BigDecimal getTotalPrice(Long apartmentId, LocalDate fromDate, LocalDate toDate)
            throws DataProcessingException {
        return bookingDao.getTotalPrice(apartmentId, fromDate, toDate);
    }

    @Override
    public int getNumberOfBookingPerCustomer(Long customerId, PaymentStatus paymentStatus)
            throws DataProcessingException {
        return bookingDao.getNumberOfBookingPerCustomer(customerId, paymentStatus);
    }

    @Override
    public int getNumberOfBooking(PaymentStatus paymentStatus) throws DataProcessingException {
        return bookingDao.getNumberOfBooking(paymentStatus);
    }

    @Override
    public Booking createBooking(Booking booking) throws DataProcessingException {
        return bookingDao.createBooking(booking);
    }

    @Override
    public Booking createReserve(Booking booking, Long bookingRequestId)
            throws DataProcessingException {
        return bookingDao.createReserve(booking, bookingRequestId);
    }

    @Override
    public void updateReserve(Booking booking, PaymentStatus paymentStatus,
                              BookingStatus bookingStatus, LocalDate... dates)
            throws DataProcessingException {
        bookingDao.updateReserve(booking, paymentStatus, bookingStatus, dates);
    }

    @Override
    public int deleteBooking(Booking booking) throws DataProcessingException {
        return bookingDao.deleteBooking(booking);
    }
}

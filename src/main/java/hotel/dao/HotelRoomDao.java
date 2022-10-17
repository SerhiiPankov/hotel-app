package hotel.dao;

import hotel.dto.HotelRoomDto;
import hotel.dto.list.SelectedHotelRoomsDto;
import hotel.exception.DataProcessingException;
import hotel.model.HotelRoom;
import hotel.model.enums.BookingStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HotelRoomDao {
    HotelRoom create(HotelRoom hotelRoom) throws DataProcessingException;

    Optional<HotelRoom> get(Long hotelRoomId) throws DataProcessingException;

    List<HotelRoom> getAll() throws DataProcessingException;

    List<HotelRoomDto> getAllDto() throws DataProcessingException;

    HotelRoom update(HotelRoom hotelRoom) throws DataProcessingException;

    boolean delete(Long hotelRoomId) throws DataProcessingException;

    Optional<HotelRoom> getHotelRoomByNumber(String number) throws DataProcessingException;

    Optional<HotelRoomDto> getHotelRoomDtoById(Long hotelRoomId) throws DataProcessingException;

    List<HotelRoomDto> getAllHotelRoomDtoByDateAndByNumberOfGuestAndByHotelRoomClass(
            long hotelRoomClassId, int numberOfGuests,
            LocalDate fromDate, LocalDate toDate,
            BookingStatus bookingStatus, Long interval) throws DataProcessingException;

    SelectedHotelRoomsDto getAllHotelRoomDtoByDateRangeAndBookingStatus(
            LocalDate fromDate, LocalDate toDate, int numberOfGuests,
            long interval, BookingStatus bookingStatus,
            int startRecord, int recordsPerPage, String sort)
            throws DataProcessingException;
}

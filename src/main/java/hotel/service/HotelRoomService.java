package hotel.service;

import hotel.dto.HotelRoomDto;
import hotel.dto.list.SelectedHotelRoomsDto;
import hotel.exception.DataProcessingException;
import hotel.exception.WrongDataException;
import hotel.model.HotelRoom;
import hotel.model.Request;
import hotel.model.enums.BookingStatus;
import java.time.LocalDate;
import java.util.List;

public interface HotelRoomService {
    HotelRoom create(HotelRoom hotelRoom) throws DataProcessingException;

    HotelRoom get(Long id) throws DataProcessingException, WrongDataException;

    boolean delete(Long id) throws DataProcessingException;

    HotelRoomDto getHotelRoomDtoById(Long id) throws DataProcessingException, WrongDataException;

    List<HotelRoomDto> getAllDto() throws DataProcessingException;

    List<HotelRoomDto> getAllHotelRoomDtoByRequest(
            Request request, BookingStatus bookingStatus)
            throws DataProcessingException;

    SelectedHotelRoomsDto getAllHotelRoomDtoByDateRangeAndBookingStatus(
            LocalDate fromDate, LocalDate toDate,
            int numberOfGuests, BookingStatus bookingStatus,
            int page, int recordsPerPage, String sort)
            throws DataProcessingException;
}

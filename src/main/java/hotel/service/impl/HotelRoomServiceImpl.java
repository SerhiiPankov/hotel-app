package hotel.service.impl;

import hotel.dao.HotelRoomDao;
import hotel.dto.HotelRoomDto;
import hotel.dto.list.SelectedHotelRoomsDto;
import hotel.exception.DataProcessingException;
import hotel.exception.WrongDataException;
import hotel.lib.Inject;
import hotel.lib.Service;
import hotel.model.HotelRoom;
import hotel.model.Request;
import hotel.model.enums.BookingStatus;
import hotel.service.HotelRoomService;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class HotelRoomServiceImpl implements HotelRoomService {
    @Inject
    private HotelRoomDao hotelRoomDao;

    @Override
    public HotelRoom create(HotelRoom hotelRoom) throws DataProcessingException {
        Optional<HotelRoom> hotelRoomByNumber =
                hotelRoomDao.getHotelRoomByNumber(hotelRoom.getNumber());
        if (hotelRoomByNumber.isPresent()) {
            HotelRoom hotelRoomFromDb = hotelRoomByNumber.get();
            hotelRoomFromDb.setNumberOfGuests(hotelRoom.getNumberOfGuests());
            hotelRoomFromDb.setHotelRoomClassId(hotelRoom.getHotelRoomClassId());
            hotelRoomFromDb.setDescription(hotelRoom.getDescription());
            return hotelRoomDao.update(hotelRoomFromDb);
        }
        return hotelRoomDao.create(hotelRoom);
    }

    @Override
    public HotelRoom get(Long hotelRoomId) throws DataProcessingException, WrongDataException {
        return hotelRoomDao.get(hotelRoomId)
                .orElseThrow(() ->
                        new WrongDataException("Can't get hotel room with id: " + hotelRoomId));
    }

    @Override
    public boolean delete(Long id) throws DataProcessingException {
        return hotelRoomDao.delete(id);
    }

    @Override
    public HotelRoomDto getHotelRoomDtoById(Long hotelRoomId)
            throws DataProcessingException, WrongDataException {
        return hotelRoomDao.getHotelRoomDtoById(hotelRoomId)
                .orElseThrow(() ->
                        new WrongDataException("Can't get hotel room with id: " + hotelRoomId));
    }

    @Override
    public List<HotelRoomDto> getAllDto() throws DataProcessingException {
        return hotelRoomDao.getAllDto();
    }

    @Override
    public List<HotelRoomDto> getAllHotelRoomDtoByRequest(
            Request request, BookingStatus bookingStatus)
            throws DataProcessingException {
        long interval = ChronoUnit.DAYS.between(
                request.getCheckIn(),
                request.getCheckOut().plusDays(1L));
        return hotelRoomDao.getAllHotelRoomDtoByDateAndByNumberOfGuestAndByHotelRoomClass(
                request.getHotelRoomClassId(), request.getNumberOfGuests(),
                request.getCheckIn(), request.getCheckOut(),
                bookingStatus, interval);
    }

    @Override
    public SelectedHotelRoomsDto getAllHotelRoomDtoByDateRangeAndBookingStatus(
            LocalDate fromDate, LocalDate toDate,
            int numberOfGuests, BookingStatus bookingStatus,
            int page, int recordsPerPage, String sort)
            throws DataProcessingException {
        long interval = ChronoUnit.DAYS.between(
                fromDate,
                toDate);
        int startRecord = (page - 1) * recordsPerPage;
        return hotelRoomDao.getAllHotelRoomDtoByDateRangeAndBookingStatus(
                fromDate, toDate, numberOfGuests, interval, bookingStatus,
                startRecord, recordsPerPage, sort);
    }

}

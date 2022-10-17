package hotel.service;

import hotel.exception.DataProcessingException;
import hotel.exception.WrongDataException;
import hotel.model.HotelRoomClass;
import java.util.List;

public interface HotelRoomClassService {
    HotelRoomClass create(HotelRoomClass hotelRoomClass) throws DataProcessingException;

    HotelRoomClass get(Long id) throws DataProcessingException, WrongDataException;

    List<HotelRoomClass> getAll() throws DataProcessingException;

    boolean delete(Long id) throws DataProcessingException;

    List<HotelRoomClass> getAllWithoutDeleted() throws DataProcessingException;
}

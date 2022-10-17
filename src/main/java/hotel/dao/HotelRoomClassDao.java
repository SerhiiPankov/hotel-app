package hotel.dao;

import hotel.exception.DataProcessingException;
import hotel.model.HotelRoomClass;
import java.util.List;
import java.util.Optional;

public interface HotelRoomClassDao {
    HotelRoomClass create(HotelRoomClass hotelRoomClass) throws DataProcessingException;

    Optional<HotelRoomClass> get(Long apartmentClassId) throws DataProcessingException;

    List<HotelRoomClass> getAll() throws DataProcessingException;

    List<HotelRoomClass> getAllWithoutDeleted() throws DataProcessingException;

    HotelRoomClass update(HotelRoomClass hotelRoomClass) throws DataProcessingException;

    boolean delete(Long apartmentClassId) throws DataProcessingException;

    Optional<HotelRoomClass> getByName(String name) throws DataProcessingException;
}

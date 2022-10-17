package hotel.service.impl;

import hotel.dao.HotelRoomClassDao;
import hotel.exception.DataProcessingException;
import hotel.exception.WrongDataException;
import hotel.lib.Inject;
import hotel.lib.Service;
import hotel.model.HotelRoomClass;
import hotel.service.HotelRoomClassService;
import java.util.List;
import java.util.Optional;

@Service
public class HotelRoomClassServiceImpl implements HotelRoomClassService {
    @Inject
    private HotelRoomClassDao hotelRoomClassDao;

    @Override
    public HotelRoomClass create(HotelRoomClass hotelRoomClass) throws DataProcessingException {
        Optional<HotelRoomClass> apartmentClassByName =
                hotelRoomClassDao.getByName(hotelRoomClass.getName());
        if (apartmentClassByName.isPresent()) {
            HotelRoomClass hotelRoomClassFromDb = apartmentClassByName.get();
            hotelRoomClassFromDb.setDescription(hotelRoomClass.getDescription());
            return hotelRoomClassDao.update(hotelRoomClassFromDb);
        }
        return hotelRoomClassDao.create(hotelRoomClass);
    }

    @Override
    public HotelRoomClass get(Long id) throws DataProcessingException, WrongDataException {
        return hotelRoomClassDao.get(id)
                .orElseThrow(() ->
                        new WrongDataException("Can't get apartment class with id: " + id));
    }

    @Override
    public List<HotelRoomClass> getAll() throws DataProcessingException {
        return hotelRoomClassDao.getAll();
    }

    //@Override
    //public ApartmentClass update(ApartmentClass apartmentClass) throws DataProcessingException {
    //    return apartmentClassDao.update(apartmentClass);
    //}

    @Override
    public boolean delete(Long id) throws DataProcessingException {
        return hotelRoomClassDao.delete(id);
    }

    @Override
    public List<HotelRoomClass> getAllWithoutDeleted() throws DataProcessingException {
        return hotelRoomClassDao.getAllWithoutDeleted();
    }
}

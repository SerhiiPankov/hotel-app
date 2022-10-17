package hotel.dao;

import hotel.dto.ScheduleDto;
import hotel.dto.list.SchedulesDto;
import hotel.exception.DataProcessingException;
import hotel.model.Schedule;
import java.time.LocalDate;
import java.util.List;

public interface ScheduleDao {
    Schedule add(Schedule schedule) throws DataProcessingException;

    List<ScheduleDto> getAllByHotelRoomIdAndDateInterval(
            long hotelRoomId, LocalDate fromDate, LocalDate toDate) throws DataProcessingException;

    int countFreeDaysByInterval(
            long hotelRoomId, LocalDate fromDate, LocalDate toDate) throws DataProcessingException;

    SchedulesDto getAllByHotelRoomId(long hotelRoomId, int startRecord, int recordsPerPage)
            throws DataProcessingException;

    Schedule update(Schedule schedule) throws DataProcessingException;

    int deleteExpiredSchedule(LocalDate date) throws DataProcessingException;
}

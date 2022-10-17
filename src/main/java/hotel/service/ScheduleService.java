package hotel.service;

import hotel.dto.list.SchedulesDto;
import hotel.exception.DataProcessingException;
import java.math.BigDecimal;
import java.time.LocalDate;

public interface ScheduleService {
    boolean addSchedules(long hotelRoomId, LocalDate fromDate, LocalDate toDate,
                         BigDecimal weekdayPrice, BigDecimal holidayPrice)
            throws DataProcessingException;

    SchedulesDto getAllByHotelRoomId(long hotelRoomId, int page, int recordsPerPage)
            throws DataProcessingException;

    int countFreeDaysByInterval(
            long hotelRoomId, LocalDate fromDate, LocalDate toDate) throws DataProcessingException;

    int deleteExpiredSchedule(LocalDate date) throws DataProcessingException;
}

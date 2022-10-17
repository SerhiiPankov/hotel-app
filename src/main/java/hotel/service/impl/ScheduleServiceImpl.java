package hotel.service.impl;

import hotel.dao.ScheduleDao;
import hotel.dto.ScheduleDto;
import hotel.dto.list.SchedulesDto;
import hotel.exception.DataProcessingException;
import hotel.lib.Inject;
import hotel.lib.Service;
import hotel.model.Schedule;
import hotel.model.enums.BookingStatus;
import hotel.service.ScheduleService;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    private static final long PLUS_DAY = 1L;
    @Inject
    private ScheduleDao scheduleDao;

    @Override
    public boolean addSchedules(long hotelRoomId, LocalDate fromDate, LocalDate toDate,
                                BigDecimal weekdayPrice, BigDecimal holidayPrice)
            throws DataProcessingException {
        List<LocalDate> dateList = scheduleDao
                .getAllByHotelRoomIdAndDateInterval(hotelRoomId, fromDate, toDate)
                .stream().map(ScheduleDto::getDay)
                .collect(Collectors.toList());
        BigDecimal price = weekdayPrice;
        Schedule schedule = new Schedule();
        do {
            if (fromDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)
                    || fromDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
                price = holidayPrice;
            }
            schedule.setHotelRoomId(hotelRoomId);
            schedule.setDay(fromDate);
            schedule.setPrice(price);
            schedule.setBookingStatus(BookingStatus.FREE);
            if (dateList.contains(fromDate)) {
                scheduleDao.update(schedule);
            } else {
                scheduleDao.add(schedule);
            }
            price = weekdayPrice;
            fromDate = fromDate.plusDays(PLUS_DAY);
        } while (fromDate.isBefore(toDate.plusDays(PLUS_DAY)));
        return true;
    }

    @Override
    public SchedulesDto getAllByHotelRoomId(long hotelRoomId, int page, int recordsPerPage)
            throws DataProcessingException {
        int startRecord = (page - 1) * recordsPerPage;
        return scheduleDao.getAllByHotelRoomId(hotelRoomId, startRecord, recordsPerPage);
    }

    @Override
    public int countFreeDaysByInterval(long hotelRoomId, LocalDate fromDate, LocalDate toDate)
            throws DataProcessingException {
        return scheduleDao.countFreeDaysByInterval(hotelRoomId, fromDate, toDate);
    }

    @Override
    public int deleteExpiredSchedule(LocalDate date) throws DataProcessingException {
        return scheduleDao.deleteExpiredSchedule(date);
    }
}

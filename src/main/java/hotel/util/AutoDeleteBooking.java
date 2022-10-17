package hotel.util;

import hotel.exception.DataProcessingException;
import hotel.lib.Injector;
import hotel.model.Booking;
import hotel.service.BookingService;
import hotel.service.ScheduleService;
import java.time.LocalDate;
import java.util.List;
import java.util.TimerTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AutoDeleteBooking extends TimerTask implements Constant {
    private static final long SUBTRACT_DAY = 2L;
    private static final Logger logger = LogManager.getLogger(AutoDeleteBooking.class);
    private static final Injector injector = Injector.getInstance(MAIN_PACKAGE_NAME);
    private final BookingService bookingService = (BookingService) injector
            .getInstance(BookingService.class);
    private final ScheduleService scheduleService = (ScheduleService) injector
            .getInstance(ScheduleService.class);

    @Override
    public void run() {
        try {
            LocalDate localDate = LocalDate.now().minusDays(SUBTRACT_DAY);
            List<Booking> allByDate = bookingService.getAllByDate(localDate);
            int sumOfClosedBooking = 0;
            for (Booking booking : allByDate) {
                sumOfClosedBooking += bookingService.deleteBooking(booking);
            }
            logger.info("The task to automatically delete reserves has been completed, "
                    + "the number of closed reserves " + sumOfClosedBooking);
            int sumOfDeletedSchedule = scheduleService.deleteExpiredSchedule(LocalDate.now());
            logger.info("The task to automatically delete expired schedule has been completed, "
                    + "number of deleted schedule lines " + sumOfDeletedSchedule);
        } catch (DataProcessingException e) {
            logger.info("The task to automatically delete expired schedule and reserves failed");
        }
    }
}

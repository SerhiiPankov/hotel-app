package hotel.web.servlet.room;

import static hotel.util.Constant.MAPPING_SET_SCHEDULE;

import hotel.exception.DataProcessingException;
import hotel.exception.WrongDataException;
import hotel.lib.Injector;
import hotel.service.ScheduleService;
import hotel.util.Constant;
import hotel.validation.Validator;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet(MAPPING_SET_SCHEDULE)
public class SetScheduleServlet extends HttpServlet implements Constant {
    private static final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern(PATTERN_DATE);
    private static final Logger logger = LogManager.getLogger(SetScheduleServlet.class);
    private static final Injector injector = Injector.getInstance(MAIN_PACKAGE_NAME);
    private final ScheduleService scheduleService =
            (ScheduleService) injector.getInstance(ScheduleService.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        long hotelRoomId = Long.parseLong(
                (String) req.getSession().getAttribute(SESSION_ATTRIBUTE_HOTEL_ROOM_ID));
        String dateRangeParameter;
        String weekdayPriceParameter;
        String holidayPriceParameter;
        try {
            dateRangeParameter = Validator.validate(req.getParameter(PARAMETER_DATE));
            weekdayPriceParameter = Validator.validate(req.getParameter(PARAMETER_WEEKDAY_PRICE));
            holidayPriceParameter = Validator.validate(req.getParameter(PARAMETER_HOLIDAY_PRICE));
        } catch (WrongDataException e) {
            logger.warn("Referencing a page " + MAPPING_HOTEL_ROOMS_SCHEDULE
                    + " without parameters" + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_ERROR_PAGE + "?"
                    + PARAMETER_MESSAGE + "=" + NO_PARAMETER_ERROR_MSG);
            return;
        }
        String[] dateRange = dateRangeParameter.split(SPLIT_REGEXP);
        LocalDate fromDate = LocalDate.parse(dateRange[INDEX_FROM_DATE], dateTimeFormatter);
        LocalDate toDate = LocalDate.parse(dateRange[INDEX_TO_DATE], dateTimeFormatter);
        BigDecimal weekdayPrice = BigDecimal.valueOf(
                Double.parseDouble(weekdayPriceParameter));
        BigDecimal holidayPrice = BigDecimal.valueOf(
                Double.parseDouble(holidayPriceParameter));
        try {
            scheduleService.addSchedules(hotelRoomId, fromDate, toDate, weekdayPrice, holidayPrice);
            logger.info("Schedule for hotel room with id " + hotelRoomId + " was added "
                    + "for date from " + fromDate + " to " + toDate);
            resp.sendRedirect(req.getContextPath() + MAPPING_HOTEL_ROOMS_SCHEDULE);
        } catch (DataProcessingException e) {
            logger.warn("Create schedule for hotel room with id " + hotelRoomId + " failed");
            resp.sendRedirect(req.getContextPath() + MAPPING_HOTEL_ROOMS_SCHEDULE + "?"
                    + PARAMETER_MESSAGE + "=" + CREATE_ERROR_MSG);
        }
    }
}

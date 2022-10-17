package hotel.web.servlet.room;

import static hotel.util.Constant.MAPPING_HOTEL_ROOMS_SCHEDULE;

import hotel.dto.HotelRoomDto;
import hotel.dto.ScheduleDto;
import hotel.dto.list.SchedulesDto;
import hotel.exception.DataProcessingException;
import hotel.exception.WrongDataException;
import hotel.lib.Injector;
import hotel.service.HotelRoomService;
import hotel.service.ScheduleService;
import hotel.util.Constant;
import hotel.validation.Validator;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet(MAPPING_HOTEL_ROOMS_SCHEDULE)
public class ScheduleHotelRoomServlet extends HttpServlet implements Constant {
    private static final int START_PAGE = 1;
    private static final int RECORDS_PER_PAGE = 14;
    private static final long ADD_DAY = 1L;
    private static final Logger logger = LogManager.getLogger(ScheduleHotelRoomServlet.class);
    private static final Injector injector = Injector.getInstance(MAIN_PACKAGE_NAME);
    private final HotelRoomService hotelRoomService =
            (HotelRoomService) injector.getInstance(HotelRoomService.class);
    private final ScheduleService scheduleService =
            (ScheduleService) injector.getInstance(ScheduleService.class);
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (req.getParameter(PARAMETER_HOTEL_ROOM_ID) != null
                && !req.getParameter(PARAMETER_HOTEL_ROOM_ID).equals("")) {
            req.getSession().setAttribute(SESSION_ATTRIBUTE_HOTEL_ROOM_ID,
                    req.getParameter(PARAMETER_HOTEL_ROOM_ID));
        }
        String hotelRoomIdParameter;
        try {
            hotelRoomIdParameter = Validator.validate(
                    (String) req.getSession().getAttribute(SESSION_ATTRIBUTE_HOTEL_ROOM_ID));
        } catch (WrongDataException e) {
            logger.warn("Referencing a page " + MAPPING_HOTEL_ROOMS_SCHEDULE
                    + "without parameters");
            resp.sendRedirect(req.getContextPath() + MAPPING_ERROR_PAGE + "?"
                    + PARAMETER_MESSAGE + "=" + NO_PARAMETER_ERROR_MSG);
            return;
        }
        long hotelRoomId = Long.parseLong(hotelRoomIdParameter);
        int page = START_PAGE;
        if (req.getParameter(PARAMETER_PAGE) != null
                && !req.getParameter(PARAMETER_PAGE).equals("")) {
            page = Integer.parseInt(req.getParameter(PARAMETER_PAGE));
        }
        try {
            SchedulesDto schedulesDto =
                    scheduleService.getAllByHotelRoomId(hotelRoomId, page, RECORDS_PER_PAGE);
            List<ScheduleDto> schedules = schedulesDto.getSchedules();
            HotelRoomDto hotelRoom = hotelRoomService.getHotelRoomDtoById(hotelRoomId);
            int numberOfPages = (int)
                    Math.ceil(schedulesDto.getNumberOfSchedules() * 1.0 / RECORDS_PER_PAGE);
            setAttributeToRequest(req, numberOfPages, page, schedules, hotelRoom);
            req.getRequestDispatcher(JSP_HOTEL_ROOMS_SCHEDULE).forward(req, resp);
        } catch (DataProcessingException | WrongDataException e) {
            logger.warn("Load schedule for hotel room with id " + hotelRoomId + " failed " + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_ERROR_PAGE + "?"
                    + PARAMETER_MESSAGE + "=" + ACCESS_ERROR_MSG);
        }
    }

    private void setAttributeToRequest(
            HttpServletRequest req, int numberOfPages,
            int page, List<ScheduleDto> schedules, HotelRoomDto hotelRoom) {
        req.setAttribute(ATTRIBUTE_SCHEDULE_NUMBER_OF_PAGES, numberOfPages);
        req.setAttribute(ATTRIBUTE_SCHEDULE_CURRENT_PAGE, page);
        req.setAttribute(ATTRIBUTE_MIN_ALLOWED_DATE, LocalDate.now().plusDays(ADD_DAY));
        req.setAttribute(ATTRIBUTE_SCHEDULES, schedules);
        req.setAttribute(ATTRIBUTE_HOTEL_ROOM, hotelRoom);
    }
}

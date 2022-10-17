package hotel.web.servlet.room;

import static hotel.util.Constant.MAPPING_HOTEL_ROOMS;

import hotel.dto.HotelRoomDto;
import hotel.exception.DataProcessingException;
import hotel.exception.WrongDataException;
import hotel.lib.Injector;
import hotel.model.HotelRoom;
import hotel.model.HotelRoomClass;
import hotel.service.HotelRoomClassService;
import hotel.service.HotelRoomService;
import hotel.util.Constant;
import hotel.validation.Validator;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet(MAPPING_HOTEL_ROOMS)
public class ManageHotelRoomServlet extends HttpServlet implements Constant {
    private static final Logger logger = LogManager.getLogger(ManageHotelRoomServlet.class);
    private static final Injector injector = Injector.getInstance(MAIN_PACKAGE_NAME);
    private final HotelRoomService hotelRoomService = (HotelRoomService) injector
            .getInstance(HotelRoomService.class);
    private final HotelRoomClassService hotelRoomClassService = (HotelRoomClassService) injector
            .getInstance(HotelRoomClassService.class);

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            List<HotelRoomClass> hotelRoomClasses = hotelRoomClassService.getAllWithoutDeleted();
            req.setAttribute(ATTRIBUTE_HOTEL_ROOM_CLASSES, hotelRoomClasses);
            List<HotelRoomDto> hotelRooms = hotelRoomService.getAllDto();
            req.setAttribute(ATTRIBUTE_HOTEL_ROOMS, hotelRooms);
            req.getRequestDispatcher(JSP_HOTEL_ROOMS_HOTEL_ROOM)
                    .forward(req, resp);
        } catch (DataProcessingException e) {
            logger.warn("Load list of hotel room failed" + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_ERROR_PAGE + "?"
                    + PARAMETER_MESSAGE + "=" + ACCESS_ERROR_MSG);
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        String hotelRoomsNumber;
        String hotelRoomsClassIdParameter;
        String maxNumberOfGuestsParameter;
        String description;
        try {
            hotelRoomsNumber = Validator.validate(
                    req.getParameter(PARAMETER_HOTEL_ROOM_NUMBER));
            hotelRoomsClassIdParameter = Validator.validate(
                    req.getParameter(PARAMETER_HOTEL_ROOM_CLASS_ID));
            maxNumberOfGuestsParameter = Validator.validate(
                    req.getParameter(PARAMETER_HOTEL_ROOM_MAX_NUMBER_GUESTS));
            description = Validator.validate(
                    req.getParameter(PARAMETER_HOTEL_ROOM_DESCRIPTION));
        } catch (WrongDataException e) {
            logger.warn("Referencing a page " + MAPPING_HOTEL_ROOMS
                    + " for create/update hotel room without parameter" + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_ERROR_PAGE + "?"
                    + PARAMETER_MESSAGE + "=" + NO_PARAMETER_ERROR_MSG);
            return;
        }
        long hotelRoomsClassId = Long.parseLong(hotelRoomsClassIdParameter);
        int maxNumberOfGuests = Integer.parseInt(maxNumberOfGuestsParameter);
        HotelRoom hotelRoom =
                parseHotelRoom(hotelRoomsNumber, hotelRoomsClassId,
                        maxNumberOfGuests, description);
        try {
            hotelRoomService.create(hotelRoom);
            logger.info("Hotel room " + hotelRoomsNumber + " was created/updated");
            resp.sendRedirect(req.getContextPath() + MAPPING_HOTEL_ROOMS);
        } catch (DataProcessingException e) {
            logger.warn("Create/update hotel room " + hotelRoomsNumber + " failed");
            resp.sendRedirect(req.getContextPath() + MAPPING_HOTEL_ROOMS + "?"
                    + PARAMETER_MESSAGE + "=" + CREATE_ERROR_MSG);
        }
    }

    private HotelRoom parseHotelRoom(
            String hotelRoomsNumber, long hotelRoomsClassId,
            int maxNumberOfGuests, String description) {
        HotelRoom hotelRoom = new HotelRoom();
        hotelRoom.setNumber(hotelRoomsNumber);
        hotelRoom.setHotelRoomClassId(hotelRoomsClassId);
        hotelRoom.setNumberOfGuests(maxNumberOfGuests);
        hotelRoom.setDescription(description);
        return hotelRoom;
    }
}

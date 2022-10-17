package hotel.web.servlet.room;

import static hotel.util.Constant.MAPPING_HOTEL_ROOMS_DELETE;

import hotel.exception.DataProcessingException;
import hotel.exception.WrongDataException;
import hotel.lib.Injector;
import hotel.service.HotelRoomService;
import hotel.util.Constant;
import hotel.validation.Validator;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet(MAPPING_HOTEL_ROOMS_DELETE)
public class DeleteHotelRoomServlet extends HttpServlet implements Constant {
    private static final Logger logger = LogManager.getLogger(DeleteHotelRoomServlet.class);
    private static final Injector injector = Injector.getInstance(MAIN_PACKAGE_NAME);
    private final HotelRoomService hotelRoomService;

    public DeleteHotelRoomServlet() {
        hotelRoomService =
                (HotelRoomService) injector.getInstance(HotelRoomService.class);
    }

    public DeleteHotelRoomServlet(HotelRoomService hotelRoomService) {
        this.hotelRoomService = hotelRoomService;
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String hotelRoomIdParameter;
        try {
            hotelRoomIdParameter = Validator.validate(req.getParameter(PARAMETER_HOTEL_ROOM_ID));
        } catch (WrongDataException e) {
            logger.warn("Referencing a page " + MAPPING_HOTEL_ROOMS_DELETE
                    + " without parameter" + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_ERROR_PAGE + "?"
                    + PARAMETER_MESSAGE + "=" + NO_PARAMETER_ERROR_MSG);
            return;
        }
        long hotelRoomId = Long.parseLong(hotelRoomIdParameter);
        try {
            hotelRoomService.delete(hotelRoomId);
            logger.info("Hotel room with id:" + hotelRoomId + "was deleted");
            resp.sendRedirect(req.getContextPath() + MAPPING_HOTEL_ROOMS);
        } catch (DataProcessingException e) {
            logger.warn("Delete hotel room with id" + hotelRoomId + " failed" + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_HOTEL_ROOMS + "?"
                    + PARAMETER_MESSAGE + "=" + DELETE_ERROR_MSG);
        }
    }
}

package hotel.web.servlet.room;

import static hotel.util.Constant.MAPPING_HOTEL_ROOMS_CLASS_DELETE;

import hotel.exception.DataProcessingException;
import hotel.exception.WrongDataException;
import hotel.lib.Injector;
import hotel.service.HotelRoomClassService;
import hotel.util.Constant;
import hotel.validation.Validator;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet(MAPPING_HOTEL_ROOMS_CLASS_DELETE)
public class DeleteHotelRoomClassServlet extends HttpServlet implements Constant {
    private static final Logger logger = LogManager.getLogger(DeleteHotelRoomClassServlet.class);
    private static final Injector injector = Injector.getInstance(MAIN_PACKAGE_NAME);
    private final HotelRoomClassService hotelRoomClassService;

    public DeleteHotelRoomClassServlet() {
        hotelRoomClassService =
                (HotelRoomClassService) injector.getInstance(HotelRoomClassService.class);
    }

    public DeleteHotelRoomClassServlet(HotelRoomClassService hotelRoomClassService) {
        this.hotelRoomClassService = hotelRoomClassService;
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        long hotelRoomClassId;
        try {
            hotelRoomClassId =
                    Long.parseLong(Validator.validate(
                            req.getParameter(PARAMETER_HOTEL_ROOM_CLASS_ID)));
        } catch (WrongDataException e) {
            logger.warn("Referencing a page " + MAPPING_HOTEL_ROOMS_CLASS_DELETE
                    + " without parameter" + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_ERROR_PAGE + "?"
                    + PARAMETER_MESSAGE + "=" + NO_PARAMETER_ERROR_MSG);
            return;
        }
        try {
            hotelRoomClassService.delete(hotelRoomClassId);
            logger.info("Hotel room class with id: " + hotelRoomClassId + " was deleted");
            resp.sendRedirect(req.getContextPath() + MAPPING_HOTEL_ROOMS_CLASS);
        } catch (DataProcessingException e) {
            logger.warn("Delete hotel room class with id" + hotelRoomClassId + " failed" + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_HOTEL_ROOMS_CLASS + "?"
                    + PARAMETER_MESSAGE + "=" + DELETE_ERROR_MSG);
        }
    }
}

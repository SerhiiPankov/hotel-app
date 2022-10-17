package hotel.web.servlet.room;

import static hotel.util.Constant.MAPPING_HOTEL_ROOMS_CLASS;

import hotel.exception.DataProcessingException;
import hotel.exception.WrongDataException;
import hotel.lib.Injector;
import hotel.model.HotelRoomClass;
import hotel.service.HotelRoomClassService;
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

@WebServlet(MAPPING_HOTEL_ROOMS_CLASS)
public class ManageHotelRoomClassServlet extends HttpServlet implements Constant {
    private static final Logger logger =
            LogManager.getLogger(ManageHotelRoomClassServlet.class);
    private static final Injector injector = Injector.getInstance(MAIN_PACKAGE_NAME);
    private final HotelRoomClassService hotelRoomClassService = (HotelRoomClassService) injector
            .getInstance(HotelRoomClassService.class);

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            List<HotelRoomClass> hotelRoomClasses = hotelRoomClassService.getAll();
            req.setAttribute(ATTRIBUTE_HOTEL_ROOM_CLASSES, hotelRoomClasses);
            req.getRequestDispatcher(JSP_HOTEL_ROOMS_HOTEL_ROOM_CLASS)
                    .forward(req, resp);
        } catch (DataProcessingException e) {
            logger.warn("Load list of hotel room classes failed" + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_ERROR_PAGE + "?"
                    + PARAMETER_MESSAGE + "=" + ACCESS_ERROR_MSG);
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        String hotelRoomClassName;
        String hotelRoomClassDescription;
        try {
            hotelRoomClassName = Validator.validate(
                    req.getParameter(PARAMETER_HOTEL_ROOM_CLASS_NAME));
            hotelRoomClassDescription = Validator.validate(
                    req.getParameter(PARAMETER_HOTEL_ROOM_CLASS_DESCRIPTION));
        } catch (WrongDataException e) {
            logger.warn("Referencing a page " + MAPPING_HOTEL_ROOMS_CLASS
                    + " for create hotel room class without parameter" + e);
            resp.sendRedirect(req.getContextPath() + MAPPING_ERROR_PAGE + "?"
                    + PARAMETER_MESSAGE + "=" + NO_PARAMETER_ERROR_MSG);
            return;
        }
        HotelRoomClass hotelRoomClass =
                parseHotelRoomClass(hotelRoomClassName, hotelRoomClassDescription);
        try {
            hotelRoomClassService.create(hotelRoomClass);
            logger.info("Hotel room class " + hotelRoomClassName + " was created/updated");
            resp.sendRedirect(req.getContextPath() + MAPPING_HOTEL_ROOMS_CLASS);
        } catch (DataProcessingException e) {
            logger.warn("Create/update hotel room class " + hotelRoomClassName + " failed");
            resp.sendRedirect(req.getContextPath() + MAPPING_HOTEL_ROOMS_CLASS + "?"
                    + PARAMETER_MESSAGE + "=" + CREATE_ERROR_MSG);
        }
    }

    private HotelRoomClass parseHotelRoomClass(String hotelRoomClassName,
                                               String hotelRoomClassDescription) {
        HotelRoomClass hotelRoomClass = new HotelRoomClass();
        hotelRoomClass.setName(hotelRoomClassName);
        hotelRoomClass.setDescription(hotelRoomClassDescription);
        return hotelRoomClass;
    }
}

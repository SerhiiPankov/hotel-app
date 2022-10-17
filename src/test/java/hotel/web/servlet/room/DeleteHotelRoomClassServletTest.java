package hotel.web.servlet.room;

import hotel.exception.DataProcessingException;
import hotel.service.HotelRoomClassService;
import hotel.util.Constant;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

class DeleteHotelRoomClassServletTest implements Constant {

    @Test
    void doPost_Ok() throws DataProcessingException, IOException {
        HotelRoomClassService hotelRoomClassService = mock(HotelRoomClassService.class);
        when(hotelRoomClassService.delete(1L)).thenReturn(true);
        DeleteHotelRoomClassServlet controller = new DeleteHotelRoomClassServlet(hotelRoomClassService);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter(PARAMETER_HOTEL_ROOM_CLASS_ID)).thenReturn("1");
        when(request.getContextPath())
                .thenReturn("localhost:8080");
        HttpServletResponse response = mock(HttpServletResponse.class);
        controller.doPost(request, response);
        verify(request).getParameter(PARAMETER_HOTEL_ROOM_CLASS_ID);
        verify(hotelRoomClassService).delete(1L);
        verify(response).sendRedirect("localhost:8080" + MAPPING_HOTEL_ROOMS_CLASS);
    }

    @Test
    void doPost_WithoutParameter_NotOk() throws IOException {
        DeleteHotelRoomClassServlet controller = new DeleteHotelRoomClassServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter(PARAMETER_HOTEL_ROOM_CLASS_ID)).thenReturn("");
        when(request.getContextPath())
                .thenReturn("localhost:8080");
        HttpServletResponse response = mock(HttpServletResponse.class);
        controller.doPost(request, response);
        verify(response).sendRedirect("localhost:8080" + MAPPING_ERROR_PAGE + "?"
                + PARAMETER_MESSAGE + "=" + NO_PARAMETER_ERROR_MSG);
    }

    @Test
    void doPost_DBExceptionHotelRoomClassService_NotOk() throws DataProcessingException, IOException {
        HotelRoomClassService hotelRoomClassService = mock(HotelRoomClassService.class);
        when(hotelRoomClassService.delete(1L))
                .thenThrow(new DataProcessingException("Can't get room class", new SQLException()));
        DeleteHotelRoomClassServlet controller = new DeleteHotelRoomClassServlet(hotelRoomClassService);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter(PARAMETER_HOTEL_ROOM_CLASS_ID)).thenReturn("1");
        when(request.getContextPath())
                .thenReturn("localhost:8080");
        HttpServletResponse response = mock(HttpServletResponse.class);
        controller.doPost(request, response);
        verify(response).sendRedirect("localhost:8080" + MAPPING_HOTEL_ROOMS_CLASS + "?"
                + PARAMETER_MESSAGE + "=" + DELETE_ERROR_MSG);
    }
}
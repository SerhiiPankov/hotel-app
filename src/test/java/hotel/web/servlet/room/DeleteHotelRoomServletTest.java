package hotel.web.servlet.room;

import hotel.exception.DataProcessingException;
import hotel.service.HotelRoomService;
import hotel.util.Constant;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class DeleteHotelRoomServletTest implements Constant {

    @Test
    void doPost_Ok() throws DataProcessingException, IOException {
        HotelRoomService hotelRoomService = mock(HotelRoomService.class);
        when(hotelRoomService.delete(1L)).thenReturn(true);
        DeleteHotelRoomServlet controller = new DeleteHotelRoomServlet(hotelRoomService);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter(PARAMETER_HOTEL_ROOM_ID)).thenReturn("1");
        when(request.getContextPath())
                .thenReturn("localhost:8080");
        HttpServletResponse response = mock(HttpServletResponse.class);
        controller.doPost(request, response);
        verify(request).getParameter(PARAMETER_HOTEL_ROOM_ID);
        verify(hotelRoomService).delete(1L);
        verify(response).sendRedirect("localhost:8080" + MAPPING_HOTEL_ROOMS);

    }

    @Test
    void doPost_WithoutParameter_NotOk() throws IOException {
        DeleteHotelRoomServlet controller = new DeleteHotelRoomServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter(PARAMETER_HOTEL_ROOM_ID)).thenReturn("");
        when(request.getContextPath())
                .thenReturn("localhost:8080");
        HttpServletResponse response = mock(HttpServletResponse.class);
        controller.doPost(request, response);
        verify(response).sendRedirect("localhost:8080" + MAPPING_ERROR_PAGE + "?"
                + PARAMETER_MESSAGE + "=" + NO_PARAMETER_ERROR_MSG);
    }

    @Test
    void doPost_DBExceptionHotelRoomClassService_NotOk() throws DataProcessingException, IOException {
        HotelRoomService hotelRoomService = mock(HotelRoomService.class);
        when(hotelRoomService.delete(1L))
                .thenThrow(new DataProcessingException("Can't get room ", new SQLException()));
        DeleteHotelRoomServlet controller = new DeleteHotelRoomServlet(hotelRoomService);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter(PARAMETER_HOTEL_ROOM_ID)).thenReturn("1");
        when(request.getContextPath())
                .thenReturn("localhost:8080");
        HttpServletResponse response = mock(HttpServletResponse.class);
        controller.doPost(request, response);
        verify(response).sendRedirect("localhost:8080" + MAPPING_HOTEL_ROOMS + "?"
                + PARAMETER_MESSAGE + "=" + DELETE_ERROR_MSG);
    }
}
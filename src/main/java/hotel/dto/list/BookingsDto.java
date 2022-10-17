package hotel.dto.list;

import hotel.dto.BookingDto;
import java.util.ArrayList;
import java.util.List;

public class BookingsDto {
    private List<BookingDto> bookings;
    private int numberOfBookings;

    public BookingsDto() {
        bookings = new ArrayList<>();
    }

    public List<BookingDto> getBookings() {
        return bookings;
    }

    public void setBookings(List<BookingDto> bookings) {
        this.bookings = bookings;
    }

    public int getNumberOfBookings() {
        return numberOfBookings;
    }

    public void setNumberOfBookings(int numberOfBookings) {
        this.numberOfBookings = numberOfBookings;
    }
}

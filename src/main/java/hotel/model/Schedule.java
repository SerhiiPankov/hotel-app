package hotel.model;

import hotel.model.enums.BookingStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Schedule {
    private long hotelRoomId;
    private LocalDate day;
    private BigDecimal price;
    private BookingStatus bookingStatus;

    public long getHotelRoomId() {
        return hotelRoomId;
    }

    public void setHotelRoomId(long hotelRoomId) {
        this.hotelRoomId = hotelRoomId;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    @Override
    public String toString() {
        return "Schedule{"
                + "hotelRoomId=" + hotelRoomId
                + ", day=" + day
                + ", price=" + price
                + ", bookingStatus=" + bookingStatus
                + '}';
    }
}

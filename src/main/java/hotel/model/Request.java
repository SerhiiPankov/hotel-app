package hotel.model;

import java.time.LocalDate;

public class Request {
    private long id;
    private LocalDate date;
    private long customerId;
    private long hotelRoomClassId;
    private int numberOfGuests;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private boolean isProcessed;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getHotelRoomClassId() {
        return hotelRoomClassId;
    }

    public void setHotelRoomClassId(long hotelRoomClassId) {
        this.hotelRoomClassId = hotelRoomClassId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public boolean isProcessed() {
        return isProcessed;
    }

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }

    @Override
    public String toString() {
        return "Request{"
                + "id=" + id
                + ", date=" + date
                + '}';
    }
}

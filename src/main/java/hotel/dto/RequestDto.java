package hotel.dto;

import java.time.LocalDate;

public class RequestDto {
    private Long requestId;
    private String email;
    private LocalDate date;
    private String hotelRoomClassName;
    private int numberOfGuests;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private boolean isProcessed;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getHotelRoomClassName() {
        return hotelRoomClassName;
    }

    public void setHotelRoomClassName(String hotelRoomClassName) {
        this.hotelRoomClassName = hotelRoomClassName;
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

    public boolean getIsProcessed() {
        return isProcessed;
    }

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }
}

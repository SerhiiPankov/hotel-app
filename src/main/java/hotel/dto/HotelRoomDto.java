package hotel.dto;

public class HotelRoomDto {
    private long id;
    private String number;
    private String hotelRoomClassName;
    private int numberOfGuests;
    private String description;
    private boolean isDeleted;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}

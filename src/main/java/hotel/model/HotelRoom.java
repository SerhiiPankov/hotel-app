package hotel.model;

import hotel.model.enums.BookingStatus;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

public class HotelRoom {
    private long id;
    private String number;
    private long hotelRoomClassId;
    private int numberOfGuests;
    private String description;
    private Map<LocalDate, BookingStatus> schedule;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<LocalDate, BookingStatus> getSchedule() {
        return schedule;
    }

    public void setSchedule(Map<LocalDate, BookingStatus> schedule) {
        this.schedule = schedule;
    }

    @Override
    public String toString() {
        return "HotelRoom{"
                + "number='" + number + '\''
                + ", numberOfGuests=" + numberOfGuests
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HotelRoom hotelRoom = (HotelRoom) o;
        return id == hotelRoom.id && Objects.equals(number, hotelRoom.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number);
    }
}

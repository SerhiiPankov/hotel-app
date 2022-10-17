package hotel.dto.list;

import hotel.dto.SelectedHotelRoom;
import java.util.ArrayList;
import java.util.List;

public class SelectedHotelRoomsDto {
    private List<SelectedHotelRoom> rooms;
    private int numberOfRooms;

    public SelectedHotelRoomsDto() {
        rooms = new ArrayList<>();
    }

    public List<SelectedHotelRoom> getRooms() {
        return rooms;
    }

    public void setRooms(List<SelectedHotelRoom> rooms) {
        this.rooms = rooms;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }
}

package hotel.dto.list;

import hotel.dto.UserDto;
import java.util.ArrayList;
import java.util.List;

public class UsersDto {
    private List<UserDto> users;
    private int numberOfUsers;

    public UsersDto() {
        users = new ArrayList<>();
    }

    public List<UserDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserDto> users) {
        this.users = users;
    }

    public int getNumberOfUsers() {
        return numberOfUsers;
    }

    public void setNumberOfUsers(int numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
    }
}

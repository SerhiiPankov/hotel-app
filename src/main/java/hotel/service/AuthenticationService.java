package hotel.service;

import hotel.exception.AuthenticationException;
import hotel.exception.DataProcessingException;
import hotel.exception.WrongDataException;
import hotel.model.User;

public interface AuthenticationService {
    User login(String login, String password)
            throws AuthenticationException, DataProcessingException, WrongDataException;

    User register(User user) throws DataProcessingException;

    boolean checkPassword(Long userId, String rawPassword)
            throws DataProcessingException, WrongDataException;
}

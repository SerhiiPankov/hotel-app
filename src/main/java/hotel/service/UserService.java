package hotel.service;

import hotel.dto.list.UsersDto;
import hotel.exception.DataProcessingException;
import hotel.exception.WrongDataException;
import hotel.model.User;
import hotel.model.enums.Role;

public interface UserService {
    UsersDto getAllByRole(String orderBy, int page, int recordsPerPage, Role... roles)
            throws DataProcessingException;

    User get(Long userId) throws DataProcessingException, WrongDataException;

    User findByEmail(String email) throws DataProcessingException, WrongDataException;

    User create(User user) throws DataProcessingException;

    User update(User user) throws DataProcessingException;

    void delete(Long id) throws DataProcessingException;

    void restore(long userId) throws DataProcessingException;

    void setLanguage(Long userId, String language) throws DataProcessingException;
}

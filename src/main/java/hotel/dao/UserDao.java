package hotel.dao;

import hotel.dto.list.UsersDto;
import hotel.exception.DataProcessingException;
import hotel.model.User;
import hotel.model.enums.Role;
import java.util.List;
import java.util.Optional;

public interface UserDao {
    List<User> getAll() throws DataProcessingException;

    UsersDto getAllByRole(String orderBy, int startRecord,
                          int recordsPerPage, Role... roles) throws DataProcessingException;

    Optional<User> get(Long userId) throws DataProcessingException;

    Optional<User> findByEmail(String email) throws DataProcessingException;

    User create(User user) throws DataProcessingException;

    User update(User user) throws DataProcessingException;

    void delete(Long id) throws DataProcessingException;

    void setLanguage(Long userId, String language) throws DataProcessingException;

    void restore(long userId) throws DataProcessingException;
}

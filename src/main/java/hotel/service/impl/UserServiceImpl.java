package hotel.service.impl;

import hotel.dao.UserDao;
import hotel.dto.list.UsersDto;
import hotel.exception.DataProcessingException;
import hotel.exception.WrongDataException;
import hotel.lib.Inject;
import hotel.lib.Service;
import hotel.model.User;
import hotel.model.enums.Role;
import hotel.service.UserService;
import hotel.util.HashUtil;

@Service
public class UserServiceImpl implements UserService {
    @Inject
    private UserDao userDao;

    public UserServiceImpl() {
    }

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UsersDto getAllByRole(String orderBy, int page, int recordsPerPage, Role... roles)
            throws DataProcessingException {
        int startRecord = (page - 1) * recordsPerPage;
        return userDao.getAllByRole(orderBy, startRecord, recordsPerPage, roles);
    }

    @Override
    public User get(Long userId) throws DataProcessingException, WrongDataException {
        return userDao.get(userId).orElseThrow(() ->
                new WrongDataException("Can't get user with id: " + userId));
    }

    @Override
    public User findByEmail(String email) throws DataProcessingException, WrongDataException {
        return userDao.findByEmail(email).orElseThrow(() ->
                new WrongDataException("Can't get user with email " + email
                        + " from DB"));
    }

    @Override
    public User create(User user) throws DataProcessingException {
        return userDao.create(user);
    }

    @Override
    public User update(User user) throws DataProcessingException {
        byte[] salt = HashUtil.getSalt();
        user.setSalt(salt);
        user.setPassword(HashUtil.hashPassword(user.getPassword(), salt));
        return userDao.update(user);
    }

    @Override
    public void delete(Long id) throws DataProcessingException {
        userDao.delete(id);
    }

    @Override
    public void restore(long userId) throws DataProcessingException {
        userDao.restore(userId);
    }

    @Override
    public void setLanguage(Long userId, String language) throws DataProcessingException {
        userDao.setLanguage(userId, language);
    }
}

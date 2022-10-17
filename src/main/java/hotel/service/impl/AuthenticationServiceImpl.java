package hotel.service.impl;

import hotel.exception.AuthenticationException;
import hotel.exception.DataProcessingException;
import hotel.exception.WrongDataException;
import hotel.lib.Inject;
import hotel.lib.Service;
import hotel.model.User;
import hotel.service.AuthenticationService;
import hotel.service.UserService;
import hotel.util.HashUtil;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Inject
    private UserService userService;

    public AuthenticationServiceImpl() {
    }

    public AuthenticationServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User login(String email, String password)
            throws AuthenticationException, DataProcessingException, WrongDataException {
        User userFromDb = userService.findByEmail(email);
        if (matchPasswords(password, userFromDb)) {
            return userFromDb;
        }
        throw new AuthenticationException("Incorrect email or password!");
    }

    @Override
    public User register(User user) throws DataProcessingException {
        byte[] salt = HashUtil.getSalt();
        user.setSalt(salt);
        user.setPassword(HashUtil.hashPassword(user.getPassword(), salt));
        userService.create(user);
        return user;
    }

    @Override
    public boolean checkPassword(Long userId, String rawPassword)
            throws DataProcessingException, WrongDataException {
        User userFromDb = userService.get(userId);
        return matchPasswords(rawPassword, userFromDb);
    }

    private boolean matchPasswords(String rawPassword, User userFromDb) {
        String hashedPassword = HashUtil.hashPassword(rawPassword, userFromDb.getSalt());
        return hashedPassword.equals(userFromDb.getPassword());
    }
}

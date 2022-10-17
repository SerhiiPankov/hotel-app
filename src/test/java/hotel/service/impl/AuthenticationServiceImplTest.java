package hotel.service.impl;

import hotel.exception.AuthenticationException;
import hotel.exception.DataProcessingException;
import hotel.exception.WrongDataException;
import hotel.model.User;
import hotel.model.enums.Role;
import hotel.service.AuthenticationService;
import hotel.service.UserService;
import hotel.util.HashUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import util.TestHashUtil;

import static org.mockito.Mockito.*;

class AuthenticationServiceImplTest {
    private final User user = new User.Builder("alice@gmail.com")
            .setPassword("4321")
            .setName("Alice")
            .setPhone("+380 (67) 777-77-77")
            .setRole(Role.CUSTOMER)
            .build();
    private final User alice = new User.Builder("alice@gmail.com")
            .setPassword("1234hash")
            .setSalt(new byte[]{5, 4, 3, 2, 1})
            .setName("Alice")
            .setPhone("+380 (67) 777-77-77")
            .setRole(Role.CUSTOMER)
            .build();
    private final User bob = new User.Builder("bob@gmail.com")
            .setId(1L)
            .setSalt(new byte[]{1, 2, 3, 4, 5})
            .setPassword("1234hash")
            .setName("Bob")
            .setPhone("+380 (66) 666-66-66")
            .setRole(Role.MANAGER)
            .setLanguage("en")
            .build();

    @Test
    void login_Ok() throws AuthenticationException, WrongDataException, DataProcessingException {
        UserService userService = mock(UserService.class);
        when(userService.findByEmail("bob@gmail.com"))
                .thenReturn(bob);
        MockedStatic<HashUtil> utilities = TestHashUtil.getMockHashUtil();
        utilities.when(() -> HashUtil.hashPassword("4321", new byte[]{1, 2, 3, 4, 5}))
                .thenReturn("1234hash");
        AuthenticationService authenticationService = new AuthenticationServiceImpl(userService);
        User actual = authenticationService.login("bob@gmail.com", "4321");
        Assertions.assertEquals(bob, actual);
    }

    @Test
    void login_NotOk() throws WrongDataException, DataProcessingException {
        UserService userService = mock(UserService.class);
        when(userService.findByEmail("bob@gmail.com"))
                .thenReturn(bob);
        AuthenticationService authenticationService = new AuthenticationServiceImpl(userService);
        MockedStatic<HashUtil> utilities = TestHashUtil.getMockHashUtil();
        utilities.when(() -> HashUtil.hashPassword("1234", new byte[]{1, 2, 3, 4, 5}))
                .thenReturn("4321hash");
        Assertions.assertThrows(AuthenticationException.class,
                () -> authenticationService.login("bob@gmail.com", "1234"));
    }

    @Test
    void checkPassword_Ok() throws WrongDataException, DataProcessingException {
        UserService userService = mock(UserService.class);
        when(userService.get(1L)).thenReturn(bob);
        MockedStatic<HashUtil> utilities = TestHashUtil.getMockHashUtil();
        utilities.when(() -> HashUtil.hashPassword("1234", new byte[]{1, 2, 3, 4, 5}))
                .thenReturn("1234hash");
        AuthenticationService authenticationService = new AuthenticationServiceImpl(userService);
        Assertions.assertTrue(authenticationService.checkPassword(1L, "1234"));
    }


    @Test
    void register_Ok() throws DataProcessingException {
        UserService userService = mock(UserService.class);
        AuthenticationService authenticationService = new AuthenticationServiceImpl(userService);
        MockedStatic<HashUtil> utilities = TestHashUtil.getMockHashUtil();
        utilities.when(HashUtil::getSalt).thenReturn(new byte[]{5, 4, 3, 2, 1});
        utilities.when(() -> HashUtil.hashPassword("4321", new byte[]{5, 4, 3, 2, 1}))
                .thenReturn("1234hash");
        when(userService.create(user)).thenReturn(alice);
        User actual = authenticationService.register(user);
        Assertions.assertEquals(alice, actual);

    }
}
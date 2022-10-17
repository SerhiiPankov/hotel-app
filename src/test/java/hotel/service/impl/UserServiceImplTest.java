package hotel.service.impl;

import hotel.dao.UserDao;
import hotel.dto.UserDto;
import hotel.dto.list.UsersDto;
import hotel.exception.DataProcessingException;
import hotel.exception.WrongDataException;
import hotel.model.User;
import hotel.model.enums.Role;
import hotel.service.UserService;
import hotel.util.Constant;
import hotel.util.HashUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import util.TestHashUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest implements Constant {

    @Test
    void getAllByRole_Ok() throws DataProcessingException {
        UserDao userDao = mock(UserDao.class);
        UserService userService = new UserServiceImpl(userDao);
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("expected@email.com");
        userDto.setName("Bob");
        userDto.setRole(Role.MANAGER);
        userDto.setPhone("7777777");
        userDto.setDeleted(false);
        List<UserDto> listUsersDto = new ArrayList<>();
        listUsersDto.add(userDto);
        UsersDto expected = new UsersDto();
        expected.setUsers(listUsersDto);
        expected.setNumberOfUsers(5);
        when(userDao.getAllByRole(
                COLUMN_NAME_NAME, 10, 5, Role.CUSTOMER))
                .thenReturn(expected);
        UsersDto actual = userService.getAllByRole(
                COLUMN_NAME_NAME, 3, 5, Role.CUSTOMER);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void get_Ok() throws DataProcessingException, WrongDataException {
        UserDao userDao = mock(UserDao.class);
        UserService userService = new UserServiceImpl(userDao);
        User expected = new User.Builder("expected@email.com").build();
        expected.setId(1L);
        when(userDao.get(1L)).thenReturn(Optional.of(expected));
        Assertions.assertEquals(expected, userService.get(1L));
    }

    @Test
    void get_WrongDataException_NotOk() throws DataProcessingException {
        UserDao userDao = mock(UserDao.class);
        UserService userService = new UserServiceImpl(userDao);
        when(userDao.get(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(WrongDataException.class, () ->
                userService.get(1L));
    }

    @Test
    void findByEmail_Ok() throws DataProcessingException, WrongDataException {
        UserDao userDao = mock(UserDao.class);
        UserService userService = new UserServiceImpl(userDao);
        User expected = new User.Builder("expected@email.com").build();
        when(userDao.findByEmail("expected@email.com"))
                .thenReturn(Optional.of(expected));
        Assertions.assertEquals(expected,
                userService.findByEmail("expected@email.com"));
    }

    @Test
    void findByEmail_WrongDataException_NotOk() throws DataProcessingException {
        UserDao userDao = mock(UserDao.class);
        UserService userService = new UserServiceImpl(userDao);
        when(userDao.findByEmail("expected@email.com"))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(WrongDataException.class, () ->
                userService.findByEmail("expected@email.com"));
    }

    @Test
    void create_Ok() throws DataProcessingException {
        UserDao userDao = mock(UserDao.class);
        UserService userService = new UserServiceImpl(userDao);
        User expected = new User.Builder("expected@email.com").setId(1L).build();
        when(userDao.create(new User.Builder("expected@email.com").build()))
                .thenReturn(expected);
        Assertions.assertEquals(expected,
                userService.create(new User.Builder("expected@email.com").build()));
    }

    @Test
    void update() throws DataProcessingException {
        UserDao userDao = mock(UserDao.class);
        UserService userService = new UserServiceImpl(userDao);
        MockedStatic<HashUtil> utilities = TestHashUtil.getMockHashUtil();
        utilities.when(() ->
                HashUtil.hashPassword("1234", new byte[]{1, 2, 3, 4, 5}))
                .thenReturn("1234hash");
        utilities.when(HashUtil::getSalt)
                .thenReturn(new byte[]{1, 2, 3, 4, 5});
        User expected = new User.Builder("expected@email.com")
                .setSalt(new byte[]{1, 2, 3, 4, 5}).setPassword("1234hash").build();
        when(userDao.update(new User.Builder("expected@email.com")
                .setPassword("1234hash").setSalt(new byte[]{1, 2, 3, 4, 5}).build())).thenReturn(expected);
        Assertions.assertEquals(expected,
                userService.update(
                        new User.Builder("expected@email.com")
                                .setPassword("1234").build()));
    }

    @Test
    void delete() {
    }

    @Test
    void restore() {
    }

    @Test
    void setLanguage() {
    }
}
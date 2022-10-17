package hotel.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordValidatorTest {

    @Test
    void isEqualPassword() {
        String password = "12345678";
        String repeatPassword = "12345678";
        String wrongRepeatPassword = "1234567";
        Assertions.assertTrue(PasswordValidator.isEqual(password, repeatPassword));
        Assertions.assertFalse(PasswordValidator.isEqual(password, wrongRepeatPassword));
    }
}
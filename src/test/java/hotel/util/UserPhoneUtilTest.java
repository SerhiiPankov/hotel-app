package hotel.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserPhoneUtilTest implements UserPhoneUtil{

    @Test
    void getPhone_Ok() {
        String countryCode = "380";
        String phoneNumber = "678889900";
        String phone = getPhone(phoneNumber, countryCode);
        String expected = "+380 (67) 888-99-00";
        Assertions.assertEquals(expected, phone);
    }

    @Test
    void getPhone_NotOk() {
        String countryCode = "380";
        String phoneNumber = "678889900";
        String phone = getPhone(phoneNumber, countryCode);
        String expected = "+380(67)888-99-00";
        Assertions.assertNotEquals(expected, phone);
    }

    @Test
    void getCountryCode() {
        String phone = "+380 (67) 888-99-00";
        String countryCode = getCountryCode(phone);
        String expected = "380";
        Assertions.assertEquals(expected, countryCode);
    }

    @Test
    void getPhoneNumber() {
        String phone = "+380 (67) 888-99-00";
        String phoneNumber = getPhoneNumber(phone);
        String expected = "678889900";
        Assertions.assertEquals(expected, phoneNumber);
    }
}
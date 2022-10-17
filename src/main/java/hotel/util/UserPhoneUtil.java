package hotel.util;

public interface UserPhoneUtil {
    default String getPhone(String phone, String countryCode) {
        return "+" + countryCode + " ("
                + phone.substring(0, 2)
                + ") "
                + phone.substring(2, 5)
                + "-"
                + phone.substring(5, 7)
                + "-"
                + phone.substring(7);
    }

    default String getCountryCode(String phoneFromDB) {
        return phoneFromDB.substring(1, phoneFromDB.indexOf("(")).trim();
    }

    default String getPhoneNumber(String phoneFromDB) {
        return phoneFromDB.substring(phoneFromDB.indexOf("(") + 1, phoneFromDB.indexOf(")")).trim()
                + phoneFromDB.substring(phoneFromDB.indexOf(")") + 1).trim()
                .replaceAll("-", "");
    }
}

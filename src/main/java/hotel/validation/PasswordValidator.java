package hotel.validation;

public class PasswordValidator {
    private PasswordValidator() {
    }

    public static boolean isEqual(String password, String repeatPassword) {
        return password.equals(repeatPassword);
    }
}

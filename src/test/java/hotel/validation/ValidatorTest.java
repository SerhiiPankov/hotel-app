package hotel.validation;

import hotel.exception.WrongDataException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

    @Test
    void validateStringParameter() throws WrongDataException {
        String parameterFirst = null;
        Assertions.assertThrows(WrongDataException.class, () -> Validator.validate(parameterFirst));
        String parameterSecond = "";
        Assertions.assertThrows(WrongDataException.class, () -> Validator.validate(parameterSecond));
        String parameterThird = "page";
        Assertions.assertDoesNotThrow(() -> Validator.validate(parameterThird));
    }

    @Test
    void validateLongParameter() {
        Long parameterFirst = null;
        Assertions.assertThrows(WrongDataException.class, () -> Validator.validate(parameterFirst));
        Long parameterSecond = 5L;
        Assertions.assertDoesNotThrow(() -> Validator.validate(parameterSecond));
    }

    @Test
    void validateIntegerParameter() {
        Integer parameterFirst = null;
        Assertions.assertThrows(WrongDataException.class, () -> Validator.validate(parameterFirst));
        Integer parameterSecond = 128;
        Assertions.assertDoesNotThrow(() -> Validator.validate(parameterSecond));
    }
}
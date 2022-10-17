package hotel.validation;

import hotel.exception.WrongDataException;

public class Validator {

    private Validator(){
    }

    public static String validate(String parameter) throws WrongDataException {
        if (parameter == null || parameter.equals("")) {
            throw new WrongDataException("Parameter is empty or does not exist");
        }
        return parameter;
    }

    public static Long validate(Long attribute) throws WrongDataException {
        if (attribute == null) {
            throw new WrongDataException("Parameter is empty or does not exist");
        }
        return attribute;
    }

    public static Integer validate(Integer attribute) throws WrongDataException {
        if (attribute == null) {
            throw new WrongDataException("Parameter is empty or does not exist");
        }
        return attribute;
    }
}

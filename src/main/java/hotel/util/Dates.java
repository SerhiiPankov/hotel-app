package hotel.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class Dates {
    private Dates() {
    }

    public static String formatLocalDate(LocalDate localDate, String pattern) {
        return localDate.format(DateTimeFormatter.ofPattern(pattern));
    }
}

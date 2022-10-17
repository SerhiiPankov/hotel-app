package hotel.util.currency;

import hotel.util.Constant;
import java.util.HashMap;
import java.util.Map;

public class CurrencyUtil implements Constant {
    private static final Map<Integer, String> currencies = new HashMap<>();

    static {
        currencies.put(CODE_UAH, UAH);
        currencies.put(CODE_EUR, EUR);
        currencies.put(CODE_USD, USD);
    }

    private CurrencyUtil() {
    }

    public static String getCurrencySign(int currencyCode) {
        return currencies.get(currencyCode);
    }
}

package hotel.util.currency;

import hotel.exception.DataProcessingException;
import hotel.lib.Injector;
import hotel.model.Currency;
import hotel.service.CurrencyService;
import hotel.util.Constant;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CurrencyReader extends TimerTask implements Constant {
    private static final Logger logger = LogManager.getLogger(CurrencyReader.class);
    private static final Injector injector =
            Injector.getInstance(MAIN_PACKAGE_NAME);
    private final CurrencyService currencyService = (CurrencyService) injector
            .getInstance(CurrencyService.class);

    @Override
    public void run() {
        try {
            refreshCurrency();
            logger.info("Loading the exchange rates was successful");
        } catch (IOException | DataProcessingException e) {
            logger.warn("Can not load currencies to DB. " + e);
        }
    }

    private void refreshCurrency() throws IOException, JSONException, DataProcessingException {
        String json = readJsonFromUrl();
        JSONArray jsonArray = new JSONArray(json);
        List<Currency> currencies = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (jsonObject.length() == NUMBER_OF_JSON_OBJECT) {
                Currency currency = new Currency();
                currency.setDateTime(jsonObject.getLong(JSON_FIELD_DATE));
                currency.setCurrencyCodeA(jsonObject.getInt(JSON_FIELD_CURRENCY_CODE_A));
                currency.setCurrencyCodeB(jsonObject.getInt(JSON_FIELD_CURRENCY_CODE_B));
                currency.setRateBuy(jsonObject.getBigDecimal(JSON_FIELD_RATE_BUY));
                currency.setRateSell(jsonObject.getBigDecimal(JSON_FIELD_RATE_SELL));
                currencies.add(currency);
            }
        }
        currencyService.refreshCurrencies(currencies);
        logger.info("Currencies was loaded: ");
        currencies.forEach(logger::info);
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private String readJsonFromUrl() throws IOException, JSONException {
        try (InputStream is = new URL(CURRENCY_API_URL).openStream()) {
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8));
            return readAll(rd);
        }
    }
}

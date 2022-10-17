package hotel.service;

import hotel.exception.DataProcessingException;
import hotel.model.Currency;
import java.util.List;

public interface CurrencyService {
    void refreshCurrencies(List<Currency> currencies) throws DataProcessingException;

    List<Currency> getAllCurrencies() throws DataProcessingException;
}

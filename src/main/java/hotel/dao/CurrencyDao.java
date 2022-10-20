package hotel.dao;

import hotel.exception.DataProcessingException;
import hotel.model.Currency;
import java.util.List;

public interface CurrencyDao {
    void addCurrency(Currency currency) throws DataProcessingException;

    boolean isPresenceCurrency(Currency currency) throws DataProcessingException;

    void refreshCurrencies(Currency currency) throws DataProcessingException;

    List<Currency> getAllCurrencies() throws DataProcessingException;
}

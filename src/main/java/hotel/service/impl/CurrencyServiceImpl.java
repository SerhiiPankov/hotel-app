package hotel.service.impl;

import hotel.dao.CurrencyDao;
import hotel.exception.DataProcessingException;
import hotel.lib.Inject;
import hotel.lib.Service;
import hotel.model.Currency;
import hotel.service.CurrencyService;
import java.util.List;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    @Inject
    private CurrencyDao currencyDao;

    @Override
    public void refreshCurrencies(List<Currency> currencies) throws DataProcessingException {
        for (Currency currency: currencies) {
            if (currencyDao.isPresenceCurrency(currency)) {
                currencyDao.refreshCurrencies(currency);
            } else {
                currencyDao.addCurrency(currency);
            }
        }
    }

    @Override
    public List<Currency> getAllCurrencies() throws DataProcessingException {
        return currencyDao.getAllCurrencies();
    }
}

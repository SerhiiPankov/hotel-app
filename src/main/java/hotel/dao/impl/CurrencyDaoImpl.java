package hotel.dao.impl;

import hotel.dao.CurrencyDao;
import hotel.exception.DataProcessingException;
import hotel.lib.Dao;
import hotel.model.Currency;
import hotel.util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Dao
public class CurrencyDaoImpl implements CurrencyDao {
    private static final Logger logger = LogManager.getLogger(CurrencyDaoImpl.class);

    @Override
    public void addCurrency(Currency currency) throws DataProcessingException {
        String query = "INSERT INTO currencies "
                + "(currency_code_a, currency_code_b, date_time, rate_buy, rate_sell) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1,currency.getCurrencyCodeA());
            statement.setInt(2,currency.getCurrencyCodeB());
            statement.setLong(3, currency.getDateTime());
            statement.setBigDecimal(4, currency.getRateBuy());
            statement.setBigDecimal(5, currency.getRateSell());
            statement.executeUpdate();
            logger.info("Request to the database to add currency " + currency
                    + " was successful");
        } catch (SQLException e) {
            logger.info("Request to the database to add currency " + currency
                    + " failed");
            throw new DataProcessingException("Couldn't add currency "
                    + currency + ". ", e);
        }
    }

    @Override
    public boolean isPresenceCurrency(Currency currency) throws DataProcessingException {
        String query = "SELECT EXISTS (SELECT * FROM currencies "
                + "WHERE currency_code_a = ? AND currency_code_b = ?)";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1,currency.getCurrencyCodeA());
            statement.setInt(2,currency.getCurrencyCodeB());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                logger.info("Request to the database to check currency " + currency
                        + " was successful - present");
                return resultSet.getBoolean(1);
            }
            logger.info("Request to the database to check currency " + currency
                    + " was successful - not present");
            return false;
        } catch (SQLException e) {
            logger.info("Request to the database to check currency " + currency
                    + " failed");
            throw new DataProcessingException("Couldn't add currency "
                    + currency + ". ", e);
        }
    }

    @Override
    public void refreshCurrencies(Currency currency) throws DataProcessingException {
        String query = "UPDATE currencies SET date_time = ?, rate_buy = ?, rate_sell = ? "
                + "WHERE currency_code_a = ? AND currency_code_b = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, currency.getDateTime());
            statement.setBigDecimal(2, currency.getRateBuy());
            statement.setBigDecimal(3, currency.getRateSell());
            statement.setInt(4,currency.getCurrencyCodeA());
            statement.setInt(5,currency.getCurrencyCodeB());
            statement.executeUpdate();
            logger.info("Request to the database to refresh currency " + currency
                    + " was successful");
        } catch (SQLException e) {
            logger.info("Request to the database to refresh currency " + currency
                    + " failed");
            throw new DataProcessingException("Couldn't refresh currency "
                    + currency + ". ", e);
        }
    }

    @Override
    public List<Currency> getAllCurrencies() throws DataProcessingException {
        String query = "SELECT * FROM currencies";
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            List<Currency> currencies = new ArrayList<>();
            while (resultSet.next()) {
                currencies.add(parseCurrencyFromResultSet(resultSet));
            }
            logger.info("Request to the database to get list of all currency "
                    + " was successful");
            return currencies;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get list of currencies ", e);
        }
    }

    private Currency parseCurrencyFromResultSet(ResultSet resultSet) throws SQLException {
        Currency currency = new Currency();
        currency.setCurrencyCodeA(resultSet.getInt("currency_code_a"));
        currency.setCurrencyCodeB(resultSet.getInt("currency_code_b"));
        currency.setDateTime(resultSet.getLong("date_time"));
        currency.setRateBuy(resultSet.getBigDecimal("rate_buy"));
        currency.setRateSell(resultSet.getBigDecimal("rate_sell"));
        return currency;
    }
}

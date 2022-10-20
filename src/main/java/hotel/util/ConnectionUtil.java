package hotel.util;

import hotel.exception.DataProcessingException;
import hotel.exception.DbConnectionException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectionUtil implements Constant {
    private static final int INITIAL_SIZE = 10;
    private static final int MAX_IDLE = 20;
    private static final int MIN_IDLE = 10;
    private static final int MAX_OPENED_PREPARED_STATEMENT = 100;
    private static final Logger logger = LogManager.getLogger(ConnectionUtil.class);
    private static final BasicDataSource basicDataSource = new BasicDataSource();
    private static final Properties dbProperties = new Properties();

    static {
        try (InputStream inputStream = ConnectionUtil.class.getClassLoader()
                .getResourceAsStream(PATH_TO_DB_PROPERTIES)) {
            dbProperties.load(inputStream);
        } catch (IOException e) {
            logger.warn("Loading DB properties failed");
            throw new DbConnectionException("Can't get data for DB connection", e);
        }
        basicDataSource.setUrl(dbProperties.getProperty(PROPERTIES_DB_URL));
        basicDataSource.setUsername(dbProperties.getProperty(PROPERTIES_DB_USERNAME));
        basicDataSource.setPassword(dbProperties.getProperty(PROPERTIES_DB_PASSWORD));
        basicDataSource.setDriverClassName(dbProperties.getProperty(PROPERTIES_DB_DRIVER));
        basicDataSource.setInitialSize(INITIAL_SIZE);
        basicDataSource.setMinIdle(MIN_IDLE);
        basicDataSource.setMaxIdle(MAX_IDLE);
        basicDataSource.setMaxOpenPreparedStatements(MAX_OPENED_PREPARED_STATEMENT);
    }

    private ConnectionUtil() {
    }

    public static Connection getConnection() throws DataProcessingException {
        try {
            return basicDataSource.getConnection();
        } catch (SQLException e) {
            logger.error("Can't get DB connection");
            throw new DataProcessingException("Can't get DB connection", e);
        }
    }
}

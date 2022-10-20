package util;

import hotel.util.ConnectionUtil;
import org.apache.commons.dbcp2.BasicDataSource;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.mockStatic;

public class TestConnectionUtil implements TestConstant {
    private static final MockedStatic<ConnectionUtil> CONNECTION_UTIL =
            mockStatic(ConnectionUtil.class);
    private static final BasicDataSource basicDataSource = new BasicDataSource();

    static {
        basicDataSource.setUrl(CONNECTION_URL);
        basicDataSource.setInitialSize(10);
        basicDataSource.setMinIdle(10);
        basicDataSource.setMaxIdle(20);
    }

    private TestConnectionUtil(){
    }

    public static MockedStatic<ConnectionUtil> getConnectionUtil() {
        return CONNECTION_UTIL;
    }

    public static Connection getConnection() throws SQLException {
        Connection connection = basicDataSource.getConnection();
        TestConnectionUtil.getConnectionUtil().when(ConnectionUtil::getConnection).thenReturn(connection);
        return connection;
    }
}

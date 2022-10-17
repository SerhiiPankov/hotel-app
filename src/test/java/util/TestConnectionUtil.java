package util;

import hotel.util.ConnectionUtil;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.mockStatic;

public class TestConnectionUtil {
    private static final MockedStatic<ConnectionUtil> UTILITIES =
            mockStatic(ConnectionUtil.class);

    private TestConnectionUtil(){
    }

    public static MockedStatic<ConnectionUtil> getMockConnectionUtil() {
        return UTILITIES;
    }
}

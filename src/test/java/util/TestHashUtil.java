package util;

import hotel.util.HashUtil;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.mockStatic;

public class TestHashUtil {
    private static final MockedStatic<HashUtil> UTILITIES =
            mockStatic(HashUtil.class);

    private TestHashUtil() {
    }

    public static MockedStatic<HashUtil> getMockHashUtil() {
        return UTILITIES;
    }
}

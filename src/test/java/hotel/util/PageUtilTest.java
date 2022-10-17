package hotel.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PageUtilTest implements Constant, PageUtil {

    @Test
    void getPage_Ok() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter(PARAMETER_PAGE))
                .thenReturn("3");
        Assertions.assertEquals(3, getPage(req));
    }

    @Test
    void getPage_NotOk() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter(PARAMETER_PAGE))
                .thenReturn(null);
        Assertions.assertEquals(1, getPage(req));
        when(req.getParameter(""))
                .thenReturn("");
        Assertions.assertEquals(1, getPage(req));
    }
}
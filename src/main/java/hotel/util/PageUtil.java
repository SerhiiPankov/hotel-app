package hotel.util;

import javax.servlet.http.HttpServletRequest;

public interface PageUtil extends Constant {
    default int getPage(HttpServletRequest req) {
        int page = START_PAGE;
        if (req.getParameter(PARAMETER_PAGE) != null
                && !req.getParameter(PARAMETER_PAGE).equals("")) {
            page = Integer.parseInt(req.getParameter(PARAMETER_PAGE));
        }
        return page;
    }
}

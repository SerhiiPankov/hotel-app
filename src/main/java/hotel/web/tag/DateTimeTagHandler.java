package hotel.web.tag;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Tag for output actual date-time
 *
 * @author Serhii Pankov
 * @version 1.0
 */
public class DateTimeTagHandler extends SimpleTagSupport {
    private static final Logger logger = LogManager.getLogger(DateTimeTagHandler.class);
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");

    public void doTag() {
        JspWriter out = getJspContext().getOut();
        try {
            out.print(LocalDateTime.now().format(formatter));
        } catch (IOException e) {
            logger.warn("Error in custom tag", e);
        }
    }
}

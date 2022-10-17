package hotel.web.listener;

import static hotel.util.Constant.MAPPING_ALL_PAGES;

import hotel.exception.DataProcessingException;
import hotel.lib.Injector;
import hotel.model.User;
import hotel.model.enums.Role;
import hotel.service.AuthenticationService;
import hotel.service.HotelRoomClassService;
import hotel.service.HotelRoomService;
import hotel.service.UserService;
import hotel.util.AutoDeleteBooking;
import hotel.util.Constant;
import hotel.util.UserPhoneUtil;
import hotel.util.currency.CurrencyReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebListener(MAPPING_ALL_PAGES)
public class InitListener implements ServletContextListener, Constant, UserPhoneUtil {
    private static final Logger logger = LogManager.getLogger(InitListener.class);
    private static final Injector injector = Injector.getInstance(MAIN_PACKAGE_NAME);
    private final UserService userService = (UserService) injector
            .getInstance(UserService.class);
    private final AuthenticationService authenticationService = (AuthenticationService) injector
            .getInstance(AuthenticationService.class);
    private final HotelRoomClassService hotelRoomClassService = (HotelRoomClassService) injector
            .getInstance(HotelRoomClassService.class);
    private final HotelRoomService hotelRoomService = (HotelRoomService) injector
            .getInstance(HotelRoomService.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        startTaskAutoDeleteBooking();
        startTaskRefreshCurrencies();
        try {
            //injectApartments();
            injectAdmin();
        } catch (DataProcessingException e) {
            logger.warn("Failed to inject input data");
        }
        sce.getServletContext().setAttribute(SESSION_ATTRIBUTE_SYSTEM_CURRENCY, CODE_USD);
    }

    private void startTaskRefreshCurrencies() {
        TimerTask timerTaskCurrency = new CurrencyReader();
        timerTaskCurrency.run();
        Timer timer = new Timer(false);
        long millisecondsDelay = MINUTES_DELAY_REFRESH_CURRENCIES * SECONDS * MILLISECONDS;
        timer.schedule(timerTaskCurrency,
                TIME_TO_START_REFRESH_CURRENCIES * SECONDS * MILLISECONDS,
                millisecondsDelay);

        logger.info("Auto refresh currencies task started");
    }

    private void startTaskAutoDeleteBooking() {
        TimerTask timerTaskAutoDeleteBooking = new AutoDeleteBooking();
        timerTaskAutoDeleteBooking.run();
        Timer timer = new Timer(false);
        long timeToStart = ChronoUnit.MILLIS.between(
                LocalDateTime.now(),
                LocalDate.now().atStartOfDay().plusDays(DAYS_TO_START_AUTO_DELETE_BOOKING)
                        .plusHours(HOURS_TO_START_AUTO_DELETE_BOOKING));
        long millisecondsPerDay = HOURS * MINUTES * SECONDS * MILLISECONDS;
        timer.schedule(timerTaskAutoDeleteBooking, timeToStart, millisecondsPerDay);
        logger.info("Auto delete task started");
    }

    private void injectAdmin() throws DataProcessingException {
        if (userService.getAllByRole(
                COLUMN_NAME_ID, START_PAGE,USER_RECORDS_PER_PAGE,
                Role.ADMIN).getUsers().size() == 0) {
            authenticationService.register(new User.Builder(ADMIN_EMAIL)
                    .setPassword(ADMIN_PASSWORD)
                    .setName(ADMIN_NAME)
                    .setPhone(getPhone(ADMIN_PHONE, ADMIN_COUNTRY_CODE))
                    .setRole(Role.ADMIN)
                    .build());
        }
    }
    /*
    private void injectApartments() throws DataProcessingException {
        // Inject Apartments Class
        HotelRoomClass standard = new HotelRoomClass();
        standard.setName("Standard");
        standard.setDescription("Standard room class");
        standard = hotelRoomClassService.create(standard);
        logger.info("Room class " + standard + " was injected");
        HotelRoomClass lux = new HotelRoomClass();
        lux.setName("Lux");
        lux.setDescription("Lux room class");
        lux = hotelRoomClassService.create(lux);
        logger.info("Room class " + lux + " was injected");
        HotelRoomClass superLux = new HotelRoomClass();
        superLux.setName("Super Lux");
        superLux.setDescription("Super Lux room class");
        superLux = hotelRoomClassService.create(superLux);
        logger.info("Room class " + superLux + " was injected");
        // Inject Apartments
        HotelRoom standard101 = new HotelRoom();
        standard101.setHotelRoomClassId(standard.getId());
        standard101.setNumber("101");
        standard101.setNumberOfGuests(2);
        standard101.setDescription("Mountain view room");
        standard101 = hotelRoomService.create(standard101);
        logger.info("Room " + standard101 + " was injected");
        HotelRoom standard102 = new HotelRoom();
        standard102.setHotelRoomClassId(standard.getId());
        standard102.setNumber("102");
        standard102.setNumberOfGuests(3);
        standard102.setDescription("Pool view room");
        standard102 = hotelRoomService.create(standard102);
        logger.info("Room " + standard102 + " was injected");
        HotelRoom standard103 = new HotelRoom();
        standard103.setHotelRoomClassId(standard.getId());
        standard103.setNumber("103");
        standard103.setNumberOfGuests(4);
        standard103.setDescription("Sea view room");
        standard103 = hotelRoomService.create(standard103);
        logger.info("Room " + standard103 + " was injected");

        HotelRoom lux201 = new HotelRoom();
        lux201.setHotelRoomClassId(lux.getId());
        lux201.setNumber("201");
        lux201.setNumberOfGuests(2);
        lux201.setDescription("Mountain view room");
        lux201 = hotelRoomService.create(lux201);
        logger.info("Room " + lux201 + " was injected");
        HotelRoom lux202 = new HotelRoom();
        lux202.setHotelRoomClassId(lux.getId());
        lux202.setNumber("202");
        lux202.setNumberOfGuests(3);
        lux202.setDescription("Pool view room");
        lux202 = hotelRoomService.create(lux202);
        logger.info("Room " + lux202 + " was injected");
        HotelRoom lux203 = new HotelRoom();
        lux203.setHotelRoomClassId(lux.getId());
        lux203.setNumber("203");
        lux203.setNumberOfGuests(4);
        lux203.setDescription("Sea view room");
        lux203 = hotelRoomService.create(lux203);
        logger.info("Room " + lux203 + " was injected");

        HotelRoom superLux301 = new HotelRoom();
        superLux301.setHotelRoomClassId(superLux.getId());
        superLux301.setNumber("301");
        superLux301.setNumberOfGuests(2);
        superLux301.setDescription("Mountain view room");
        superLux301 = hotelRoomService.create(superLux301);
        logger.info("Room " + superLux301 + " was injected");
        HotelRoom superLux302 = new HotelRoom();
        superLux302.setHotelRoomClassId(superLux.getId());
        superLux302.setNumber("302");
        superLux302.setNumberOfGuests(3);
        superLux302.setDescription("Pool view room");
        superLux302 = hotelRoomService.create(superLux302);
        logger.info("Room " + superLux302 + " was injected");
        HotelRoom superLux303 = new HotelRoom();
        superLux303.setHotelRoomClassId(superLux.getId());
        superLux303.setNumber("303");
        superLux303.setNumberOfGuests(4);
        superLux303.setDescription("Sea view room");
        superLux303 = hotelRoomService.create(superLux303);
        logger.info("Room " + superLux303 + " was injected");
    }*/
}

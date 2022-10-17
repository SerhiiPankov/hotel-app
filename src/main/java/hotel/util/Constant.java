package hotel.util;

public interface Constant {
    String MAIN_PACKAGE_NAME = "hotel";
    // Encoding
    String CHARACTER_ENCODING = "UTF-8";
    // Inject Admin Properties
    String ADMIN_EMAIL = "admin@gmail.com";
    String ADMIN_PASSWORD = "1234";
    String ADMIN_NAME = "Admin";
    String ADMIN_PHONE = "888888888";
    String ADMIN_COUNTRY_CODE = "380";
    // App constants
    String CURRENCY_API_URL = "https://api.monobank.ua/bank/currency";
    String JSON_FIELD_DATE = "date";
    String JSON_FIELD_CURRENCY_CODE_A = "currencyCodeA";
    String JSON_FIELD_CURRENCY_CODE_B = "currencyCodeB";
    String JSON_FIELD_RATE_BUY = "rateBuy";
    String JSON_FIELD_RATE_SELL = "rateSell";
    short NUMBER_OF_JSON_OBJECT = 5;
    int CODE_UAH = 980;
    int CODE_EUR = 978;
    int CODE_USD = 840;
    String UAH = "₴";
    String EUR = "€";
    String USD = "$";
    String PATTERN_DATE = "dd.MM.yyyy";
    String CONTENT_TYPE_PDF = "application/pdf";
    String HEADER_CONTENT_DISPOSITION = "Content-Disposition";
    String HEADER_ATTACHMENT_FILENAME = "attachment; filename=";
    String FILE_NAME = "-invoice.pdf";
    long TIME_TO_START_REFRESH_CURRENCIES = 5L;
    long MINUTES_DELAY_REFRESH_CURRENCIES = 10L;
    long DAYS_TO_START_AUTO_DELETE_BOOKING = 1L;
    long HOURS_TO_START_AUTO_DELETE_BOOKING = 4L;
    long HOURS = 24L;
    long MINUTES = 60L;
    long SECONDS = 60L;
    long MILLISECONDS = 1000L;
    long SUBTRACT_DAY_AUTO_DELETE_PROPOSAL = 1L;
    // Pagination
    int START_PAGE = 1;
    int REQUEST_RECORDS_PER_PAGE = 5;
    int BOOKING_RECORDS_PER_PAGE = 5;
    int ROOM_RECORDS_PER_PAGE = 5;
    int USER_RECORDS_PER_PAGE = 10;
    // Date range picker
    String SPLIT_REGEXP = " - ";
    int INDEX_FROM_DATE = 0;
    int INDEX_TO_DATE = 1;
    // Mail properties
    String PATH_TO_MAIL_PROPERTIES = "mail.properties";
    // DB properties
    String PATH_TO_DB_PROPERTIES = "db_sql.properties";
    String PROPERTIES_DB_DRIVER = "db.driver.class";
    String PROPERTIES_DB_URL = "db.url";
    String PROPERTIES_DB_USERNAME = "db.username";
    String PROPERTIES_DB_PASSWORD = "db.password";
    // Session attribute
    String SESSION_ATTRIBUTE_USER_ID = "userId";
    String SESSION_ATTRIBUTE_USER_ROLE = "userRole";
    String SESSION_ATTRIBUTE_USER_NAME = "userName";
    String SESSION_ATTRIBUTE_LANGUAGE = "language";
    String SESSION_ATTRIBUTE_FROM_DATE_SELECT = "fromDateSelect";
    String SESSION_ATTRIBUTE_TO_DATE_SELECT = "toDateSelect";
    String SESSION_ATTRIBUTE_NUMBER_OF_GUESTS_SELECT = "numberOfGuestsSelect";
    String SESSION_ATTRIBUTE_FILTER_REQUEST = "filterRequest";
    String SESSION_ATTRIBUTE_FILTER_BOOKING = "filterBooking";
    String SESSION_ATTRIBUTE_SORT_USER = "sortUser";
    String SESSION_ATTRIBUTE_SORT_ROOM = "sortRoom";
    String SESSION_ATTRIBUTE_HOTEL_ROOM_ID = "hotelRoomId";
    String SESSION_ATTRIBUTE_REQUEST_ID = "requestId";
    String SESSION_ATTRIBUTE_CURRENCY = "currency";
    String SESSION_ATTRIBUTE_SYSTEM_CURRENCY = "system_currency";
    String SESSION_ATTRIBUTE_NUMBER_OF_REQUEST = "numberOfRequests";
    String SESSION_ATTRIBUTE_NUMBER_OF_BOOKING = "numberOfBooking";
    String SESSION_ATTRIBUTE_NUMBER_OF_BOOKING_PROPOSAL = "numberOfBookingProposal";
    String SESSION_ATTRIBUTE_NUMBER_OF_BOOKING_INVOICE = "numberOfBookingInvoice";
    // Attribute
    String ATTRIBUTE_ALL_USERS = "allUsers";
    String ATTRIBUTE_ACCOUNT_USER_EMAIL = "accountUserEmail";
    String ATTRIBUTE_ACCOUNT_USER_NAME = "accountUserName";
    String ATTRIBUTE_ACCOUNT_USER_COUNTRY_CODE = "accountUserCountryCode";
    String ATTRIBUTE_ACCOUNT_USER_PHONE_NUMBER = "accountUserPhoneNumber";
    String ATTRIBUTE_HOTEL_ROOM = "hotelRoom";
    String ATTRIBUTE_HOTEL_ROOM_CLASSES = "hotelRoomClasses";
    String ATTRIBUTE_HOTEL_ROOMS = "hotelRooms";
    String ATTRIBUTE_HOTEL_ROOM_CLASS_NAME = "hotelRoomClassName";
    String ATTRIBUTE_SCHEDULE_NUMBER_OF_PAGES = "numberOfPages";
    String ATTRIBUTE_SCHEDULE_CURRENT_PAGE = "currentPage";
    String ATTRIBUTE_BOOKING_NUMBER_OF_PAGES = "bookingNumberOfPages";
    String ATTRIBUTE_BOOKING_CURRENT_PAGE = "bookingCurrentPage";
    String ATTRIBUTE_ROOM_NUMBER_OF_PAGES = "roomNumberOfPages";
    String ATTRIBUTE_ROOM_CURRENT_PAGE = "roomCurrentPage";
    String ATTRIBUTE_REQUEST_NUMBER_OF_PAGES = "requestNumberOfPages";
    String ATTRIBUTE_REQUEST_CURRENT_PAGE = "requestCurrentPage";
    String ATTRIBUTE_USER_NUMBER_OF_PAGES = "userNumberOfPages";
    String ATTRIBUTE_USER_CURRENT_PAGE = "userCurrentPage";
    String ATTRIBUTE_ALL_REQUESTS = "allRequests";
    String ATTRIBUTE_ALL_BOOKINGS = "allBookings";
    String ATTRIBUTE_SCHEDULES = "schedules";
    String ATTRIBUTE_MIN_ALLOWED_DATE = "minAllowedDate";
    String ATTRIBUTE_FROM_DATE_SELECT = "fromDateSelect";
    String ATTRIBUTE_TO_DATE_SELECT = "toDateSelect";
    String ATTRIBUTE_NUMBER_OF_GUESTS_SELECT = "numberOfGuestsSelect";
    String ATTRIBUTE_REQUEST = "request";
    String ATTRIBUTE_NO_AVAILABLE_ROOM = "noAvailableRoom";
    String ATTRIBUTE_MESSAGE = "msg";
    // Message Error
    String PARAMETER_MESSAGE = "msg";
    String AUTH_ERROR_MSG = "authError";
    String CREATE_ERROR_MSG = "createError";
    String DELETE_ERROR_MSG = "deleteError";
    String ACCESS_ERROR_MSG = "accessError";
    String REGISTER_ERROR_MSG = "registerError";
    String REGISTER_OK_MSG = "registerOk";
    String NO_PARAMETER_ERROR_MSG = "noParameterError";
    String WRONG_PARAMETER_ERROR_MSG = "wrongParameterError";
    String NO_MATCH_PASSWORDS_ERROR_MSG = "noMatchPasswords";
    String NO_VALID_PASSWORD_ERROR_MSG = "noValidPassword";
    String BUSY_HOTEL_ROOM_ERROR_MSG = "busy";
    String SELECTION_HOTEL_ROOM_ERROR_MSG = "selection";
    String FAILED_TO_SEND_NOTIFICATION = "failedSendNotification";
    String CSRF_ERROR = "csrf";
    // Parameter
    String PARAMETER_EMAIL = "email";
    String PARAMETER_PASSWORD = "password";
    String PARAMETER_USER_EMAIL = "userEmail";
    String PARAMETER_USER_NAME = "userName";
    String PARAMETER_USER_PHONE_NUMBER = "userPhoneNumber";
    String PARAMETER_USER_COUNTRY_CODE = "userCountryCode";
    String PARAMETER_UPDATE_FORM_PASSWORD = "updateFormPassword";
    String PARAMETER_NEW_PASSWORD_UPDATE = "userNewPasswordUpdate";
    String PARAMETER_NEW_PASSWORD_REPEAT_UPDATE =
            "userNewPasswordRepeatUpdate";
    String PARAMETER_USER_PASSWORD_DELETE = "userPasswordDelete";
    String PARAMETER_PAGE = "page";
    String PARAMETER_LANGUAGE = "language";
    String PARAMETER_DATE = "dateRange";

    String PARAMETER_NUMBER_OF_GUESTS = "numberOfGuests";
    String PARAMETER_HOTEL_ROOM_CLASS_ID = "hotelRoomClassId";
    String PARAMETER_HOTEL_ROOM_CLASS_NAME = "hotelRoomClassName";
    String PARAMETER_HOTEL_ROOM_CLASS_DESCRIPTION = "hotelRoomClassDescription";
    String PARAMETER_HOTEL_ROOM_ID = "hotelRoomId";
    String PARAMETER_HOTEL_ROOM_DESCRIPTION = "hotelRoomDescription";
    String PARAMETER_HOTEL_ROOM_NUMBER = "hotelRoomNumber";
    String PARAMETER_HOTEL_ROOM_MAX_NUMBER_GUESTS = "maxNumberOfGuests";
    String PARAMETER_BOOKING_ID = "bookingId";
    String PARAMETER_REQUEST_ID = "requestId";
    String PARAMETER_FILTER_REQUEST = "filterRequest";
    String PARAMETER_FILTER_BOOKING = "filterBooking";
    String PARAMETER_SORT_USER = "sortUser";
    String PARAMETER_SORT_ROOM = "sortRoom";
    String PARAMETER_WEEKDAY_PRICE = "weekdayPrice";
    String PARAMETER_HOLIDAY_PRICE = "holidayPrice";
    String PARAMETER_USER_ID = "userId";
    String PARAMETER_NEW_USER_EMAIL = "newUserEmail";
    String PARAMETER_NEW_USER_PASSWORD = "newUserPassword";
    String PARAMETER_NEW_USER_PASSWORD_REPEAT = "newUserPasswordRepeat";
    String PARAMETER_NEW_USER_NAME = "newUserName";
    String PARAMETER_NEW_USER_COUNTRY_CODE = "newUserCountryCode";
    String PARAMETER_NEW_USER_PHONE_NUMBER = "newUserPhoneNumber";
    // JSP
    String JSP_HOTEL_ROOMS_HOTEL_ROOM_CLASS = "/WEB-INF/views/rooms/room-class.jsp";
    String JSP_HOTEL_ROOMS_HOTEL_ROOM = "/WEB-INF/views/rooms/room.jsp";
    String JSP_HOTEL_ROOMS_SCHEDULE = "/WEB-INF/views/rooms/schedule.jsp";
    String JSP_BOOKINGS_ALL_BOOKING = "/WEB-INF/views/bookings/all-booking.jsp";
    String JSP_BOOKINGS_ALL_REQUEST = "/WEB-INF/views/bookings/all-requests.jsp";
    String JSP_BOOKINGS_REQUEST = "/WEB-INF/views/bookings/request.jsp";
    String JSP_BOOKINGS_SELECTED = "/WEB-INF/views/bookings/selection.jsp";
    String JSP_BOOKINGS_SELECT = "/WEB-INF/views/bookings/select.jsp";
    String JSP_USERS_ACCOUNT = "/WEB-INF/views/users/account.jsp";
    String JSP_USERS_ALL_USERS = "/WEB-INF/views/users/all-users.jsp";
    String JSP_ERROR_PAGE = "/WEB-INF/views/error.jsp";
    String JSP_INDEX = "/WEB-INF/views/index.jsp";
    String JSP_LOGIN = "/WEB-INF/views/login.jsp";
    String JSP_REGISTER = "/WEB-INF/views/register.jsp";
    // Mapping
    String MAPPING_ALL_PAGES = "/*";
    String MAPPING_ENTRY_POINT = "/";
    String MAPPING_ERROR_PAGE = "/error";
    String MAPPING_REGISTER = "/register";
    String MAPPING_DO_REGISTER = "/register/do";
    String MAPPING_LOGIN = "/login";
    String MAPPING_AUTHORIZATION = "/authorization";
    String MAPPING_LOGOUT = "/logout";
    String MAPPING_USERS = "/users";
    String MAPPING_USERS_DELETE = "/users/delete";
    String MAPPING_USERS_RESTORE = "/users/restore";
    String MAPPING_USERS_ACCOUNT = "/users/account";
    String MAPPING_USERS_ACCOUNT_UPDATE = "/users/account/update";
    String MAPPING_USERS_ACCOUNT_DELETE = "/users/account/delete";
    String MAPPING_DOWNLOAD = "/download";
    String MAPPING_BOOKING_ALL = "/booking/all";
    String MAPPING_BOOKING_SELECTION = "/booking/selection";
    String MAPPING_BOOKING_INVOICE = "/booking/invoice";
    String MAPPING_BOOKING_ACCEPT = "/booking/accept";
    String MAPPING_BOOKING_PAY = "/booking/pay";
    String MAPPING_BOOKING_SELECT = "/booking/select";
    String MAPPING_BOOKING_SELECT_SORT = "/booking/select/sort";
    String MAPPING_BOOKING_CREATE = "/booking/create";
    String MAPPING_BOOKING_NOT_POSSIBLE = "/booking/not-possible";
    String MAPPING_BOOKING_SELECT_PARAMETER = "/booking/select/parameter";
    String MAPPING_BOOKING_REQUEST_CREATE = "/booking/request/create";
    String MAPPING_BOOKING_REQUEST_DELETE = "/booking/request/delete";
    String MAPPING_BOOKING_REQUEST_ALL = "/booking/request/all";
    String MAPPING_HOTEL_ROOMS = "/rooms";
    String MAPPING_HOTEL_ROOMS_DELETE = "/rooms/delete";
    String MAPPING_HOTEL_ROOMS_SCHEDULE = "/rooms/schedule";
    String MAPPING_SET_SCHEDULE = "/set/schedule";
    String MAPPING_HOTEL_ROOMS_CLASS = "/rooms/class";
    String MAPPING_HOTEL_ROOMS_CLASS_DELETE = "/rooms/class/delete";
    // DB column name
    String COLUMN_NAME_ID = "id";
    String COLUMN_NAME_NUMBER = "number";
    String COLUMN_NAME_HOTEL_ROOM_CLASS_ID = "hotel_room_class_id";
    String COLUMN_NAME_NUMBER_OF_GUESTS = "number_of_guests";
    String COLUMN_NAME_DESCRIPTION = "description";
    String COLUMN_NAME_HOTEL_ROOM_CLASS_NAME = "ac_name";
    String COLUMN_NAME_IS_DELETED = "is_deleted";
    String COLUMN_NAME_HOTEL_ROOM_ID = "hotel_room_id";
    String COLUMN_NAME_DAY = "day";
    String COLUMN_NAME_PRICE = "price";
    String COLUMN_NAME_BOOKING_STATUS = "booking_status";
    String COLUMN_NAME_DATE = "date";
    String COLUMN_NAME_CHECK_IN = "check_in";
    String COLUMN_NAME_CHECK_OUT = "check_out";
    String COLUMN_NAME_TOTAL_PRICE = "total_price";
    String COLUMN_NAME_CUSTOMER_ID = "customer_id";
    String COLUMN_NAME_MANAGER_ID = "manager_id";
    String COLUMN_NAME_PAYMENT_STATUS = "payment_status";
    String COLUMN_NAME_CLASS_NAME = "class_name";
    String COLUMN_NAME_NUMBER_OF_BOOKING = "number_of_booking";
    String COLUMN_NAME_EMAIL = "email";
    String COLUMN_NAME_NAME = "name";
    String COLUMN_NAME_IS_PROCESSED = "is_processed";
    String COLUMN_NAME_PASSWORD = "password";
    String COLUMN_NAME_SALT = "salt";
    String COLUMN_NAME_PHONE = "phone";
    String COLUMN_NAME_ROLE = "role";
    String COLUMN_NAME_LANGUAGE = "language";
    String ORDER_DESC = "DESC";









}

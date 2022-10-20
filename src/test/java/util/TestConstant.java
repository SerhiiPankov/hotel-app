package util;

import hotel.model.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface TestConstant {
    String CONNECTION_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";

    String CREATE_ROOM_CLASSES_TABLE =
        "CREATE TABLE hotel_room_classes ("
                + "id bigint NOT NULL AUTO_INCREMENT, "
                + "name varchar(255) NOT NULL, "
                + "description text NOT NULL, "
                + "is_deleted bit NOT NULL DEFAULT FALSE, "
                + "PRIMARY KEY (id), "
                + "CONSTRAINT name UNIQUE (name) "
                + "); ";
    String DROP_ROOM_CLASSES_TABLE = "DROP TABLE IF EXISTS hotel_room_classes";

    long ROOM_CLASS_ID_STANDARD = 1L;
    String ROOM_CLASS_NAME_STANDARD = "Standard";
    String ROOM_CLASS_DESCRIPTION_STANDARD = "Standard class description";
    String ROOM_CLASS_INSERT_QUERY_STANDARD = "INSERT INTO hotel_room_classes (name, description) "
            + "VALUES ('" + ROOM_CLASS_NAME_STANDARD + "', '"+ ROOM_CLASS_DESCRIPTION_STANDARD + "')";

    String ROOM_CLASS_NAME_LUX = "Lux";
    String ROOM_CLASS_DESCRIPTION_LUX = "Lux class description";
    String ROOM_CLASS_INSERT_QUERY_LUX = "INSERT INTO hotel_room_classes (name, description) "
            + "VALUES ('" + ROOM_CLASS_NAME_LUX + "', '"+ ROOM_CLASS_DESCRIPTION_LUX + "')";

    String SELECT_QUERY_ROOM_CLASS_ALL = "SELECT * FROM hotel_room_classes";
    String SELECT_QUERY_ROOM_CLASS_ID = "SELECT * FROM hotel_room_classes WHERE id = 1";
    String UPDATE_QUERY_ROOM_CLASS_ID = "UPDATE hotel_room_classes SET is_deleted = true WHERE id = 1";

    String CREATE_ROOM_TABLE =
            "CREATE TABLE hotel_rooms ("
                    + "id bigint NOT NULL AUTO_INCREMENT, "
                    + "number varchar(255) NOT NULL, "
                    + "hotel_room_class_id bigint NOT NULL, "
                    + "number_of_guests tinyint NOT NULL,"
                    + "description text NOT NULL, "
                    + "is_deleted bit NOT NULL DEFAULT FALSE, "
                    + "PRIMARY KEY (id), "
                    + "CONSTRAINT number UNIQUE (number), "
                    + "CONSTRAINT fk_hotel_rooms_hotel_room_class_id "
                    + "FOREIGN KEY (hotel_room_class_id) REFERENCES hotel_room_classes (id)"
                    + "ON DELETE NO ACTION ON UPDATE NO ACTION"
                    + "); ";
    String DROP_ROOM_TABLE = "DROP TABLE IF EXISTS hotel_rooms";

    long ROOM_ID_101 = 1L;
    String ROOM_NUMBER_101 = "101";
    long ROOM_ROOM_CLASS_ID_101 = 1L;
    int ROOM_NUMBER_OF_GUESTS_101 = 3;
    String ROOM_DESCRIPTION_101 = "101 number";
    String ROOM_INSERT_QUERY_101 = "INSERT INTO hotel_rooms "
            + "(number, hotel_room_class_id, number_of_guests, description) "
            + "VALUES ('" + ROOM_NUMBER_101 + "', '" + ROOM_ROOM_CLASS_ID_101 + "', '"
            + ROOM_NUMBER_OF_GUESTS_101 + "', '" + ROOM_DESCRIPTION_101 + "' )";

    long ROOM_ID_102 = 2L;
    String ROOM_NUMBER_102 = "102";
    long ROOM_ROOM_CLASS_ID_102 = 2L;
    int ROOM_NUMBER_OF_GUESTS_102 = 2;
    String ROOM_DESCRIPTION_102 = "102 number";
    String ROOM_INSERT_QUERY_102 = "INSERT INTO hotel_rooms "
            + "(number, hotel_room_class_id, number_of_guests, description) "
            + "VALUES ('" + ROOM_NUMBER_102 + "', '" + ROOM_ROOM_CLASS_ID_102 + "', '"
            + ROOM_NUMBER_OF_GUESTS_102 + "', '" + ROOM_DESCRIPTION_102 + "' )";

    String ROOM_SELECT_QUERY_ALL = "SELECT * FROM hotel_rooms";
    String ROOM_SELECT_QUERY_ID = "SELECT * FROM hotel_rooms WHERE id = 1";

    String CREATE_SCHEDULE_TABLE =
            "CREATE TABLE schedules ("
                    + "hotel_room_id bigint NOT NULL, "
                    + "day_schedule date NOT NULL, "
                    + "price decimal(10,2) NOT NULL, "
                    + "booking_status enum('FREE','BOOK','BUSY','UNAVAILABLE', 'OFFER', 'ACCEPT') NOT NULL, "
                    + "PRIMARY KEY (hotel_room_id, day_schedule), "
                    + "CONSTRAINT fk_schedules_hotel_room_id FOREIGN KEY (hotel_room_id) REFERENCES hotel_rooms (id) "
                    + "ON DELETE NO ACTION ON UPDATE NO ACTION"
                    + ")";
    String DROP_SCHEDULE_TABLE = "DROP TABLE IF EXISTS schedules";
    LocalDate SCHEDULE_DAY_1 = LocalDate.of(2022, 10, 10);
    BigDecimal SCHEDULE_PRICE_1 = new BigDecimal("999.99");
    BookingStatus SCHEDULE_BOOKING_STATUS_1 = BookingStatus.FREE;

    BigDecimal SCHEDULE_PRICE_2 = new BigDecimal("9999.99");
    BookingStatus SCHEDULE_BOOKING_STATUS_2 = BookingStatus.BOOK;

    String SCHEDULE_INSERT_QUERY_1 = "INSERT INTO schedules "
            + "(hotel_room_id, day_schedule, price, booking_status) "
            + "VALUES (?, ?, ?, ?)";

    String SCHEDULE_SELECT_QUERY_ROOM_DAY = "SELECT * FROM schedules "
            + "WHERE hotel_room_id = ? AND day_schedule = ?";
}

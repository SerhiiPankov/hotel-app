CREATE SCHEMA IF NOT EXISTS `climatem_hotel` DEFAULT CHARACTER SET utf8;
USE `climatem_hotel`;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
        `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
        `email` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
        `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
        `salt` blob NOT NULL,
        `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
        `phone` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
        `role` enum('ADMIN','MANAGER','CUSTOMER') NOT NULL,
        `is_deleted` bit(1) NOT NULL DEFAULT b'0',
        `language` varchar(2) NOT NULL DEFAULT 'en',
        PRIMARY KEY (`id`) USING BTREE,
        CONSTRAINT `email` UNIQUE (`email`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;
-- ----------------------------
-- Table structure for hotel_room_classes
-- ----------------------------
DROP TABLE IF EXISTS `hotel_room_classes`;
CREATE TABLE `hotel_room_classes` (
        `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
        `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
        `description` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
        `is_deleted` bit(1) NOT NULL DEFAULT b'0',
        PRIMARY KEY (`id`) USING BTREE,
        CONSTRAINT `name` UNIQUE (`name`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;
-- ----------------------------
-- Table structure for hotelRooms
-- ----------------------------
DROP TABLE IF EXISTS `hotel_rooms`;
CREATE TABLE `hotel_rooms` (
        `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
        `number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
        `hotel_room_class_id` bigint(20) unsigned NOT NULL,
        `number_of_guests` tinyint(3) unsigned NOT NULL,
        `description` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
        `is_deleted` bit(1) NOT NULL DEFAULT b'0',
        PRIMARY KEY (`id`) USING BTREE,
        CONSTRAINT `number` UNIQUE (`number`),
        INDEX `fk_hotel_room_class_id`(`hotel_room_class_id`) USING BTREE,
        CONSTRAINT `fk_hotel_rooms_hotel_room_class_id` FOREIGN KEY (`hotel_room_class_id`) REFERENCES `hotel_room_classes` (`id`)
        ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;
-- ----------------------------
-- Table structure for booking_requests
-- ----------------------------
DROP TABLE IF EXISTS `booking_requests`;
CREATE TABLE `booking_requests` (
        `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
        `date` date NOT NULL,
        `customer_id` bigint(20) UNSIGNED NOT NULL,
        `hotel_room_class_id` bigint(20) UNSIGNED NOT NULL,
        `number_of_guests` tinyint(3) UNSIGNED NOT NULL,
        `check_in` date NOT NULL,
        `check_out` date NOT NULL,
        `is_processed` bit(1) NOT NULL DEFAULT b'0',
        PRIMARY KEY (`id`) USING BTREE,
        INDEX `fk_customer_id`(`customer_id`) USING BTREE,
        INDEX `fk_hotel_room_class_id`(`hotel_room_class_id`) USING BTREE,
        CONSTRAINT `fk_booking_requests_customer_id` FOREIGN KEY (`customer_id`) REFERENCES `users` (`id`)
        ON DELETE NO ACTION ON UPDATE NO ACTION,
        CONSTRAINT `fk_booking_requests_hotel_room_class_id` FOREIGN KEY (`hotel_room_class_id`) REFERENCES `hotel_room_classes` (`id`)
        ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;
-- ----------------------------
-- Table structure for schedules
-- ----------------------------
DROP TABLE IF EXISTS `schedules`;
CREATE TABLE `schedules` (
        `hotel_room_id` bigint(20) UNSIGNED NOT NULL,
        `day_schedule` date NOT NULL,
        `price` decimal(10,2) NOT NULL,
        `booking_status` enum('FREE','BOOK','BUSY','UNAVAILABLE', 'OFFER', 'ACCEPT') NOT NULL,
        PRIMARY KEY (`hotel_room_id`, `day_schedule`) USING BTREE,
        CONSTRAINT `fk_schedules_hotel_room_id` FOREIGN KEY (`hotel_room_id`) REFERENCES `hotel_rooms` (`id`)
        ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;
-- ----------------------------
-- Table structure for booking
-- ----------------------------
DROP TABLE IF EXISTS `booking`;
CREATE TABLE `booking` (
        `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
        `date` date NOT NULL,
        `hotel_room_id` bigint(20) UNSIGNED NOT NULL,
        `check_in` date NOT NULL,
        `check_out` date NOT NULL,
        `number_of_guests` tinyint(3) UNSIGNED NOT NULL,
        `total_price` decimal(10,2) NOT NULL,
        `customer_id` bigint(20) UNSIGNED NOT NULL,
        `manager_id` bigint(20) UNSIGNED NOT NULL,
        `payment_status` enum('WAIT','PAID','DELETE', 'PROPOSAL', 'INVOICE') NOT NULL,
        PRIMARY KEY (`id`) USING BTREE ,
        INDEX `fk_hotel_room_id` (`hotel_room_id`) USING BTREE,
        INDEX `fk_customer_id` (`customer_id`) USING BTREE,
        INDEX `fk_manager_id` (`manager_id`) USING BTREE,
        CONSTRAINT `fk_booking_hotel_room_id` FOREIGN KEY (`hotel_room_id`) REFERENCES `hotel_rooms` (`id`)
        ON DELETE NO ACTION ON UPDATE NO ACTION,
        CONSTRAINT `fk_booking_customer_id` FOREIGN KEY (`customer_id`) REFERENCES `users` (`id`)
        ON DELETE NO ACTION ON UPDATE NO ACTION,
        CONSTRAINT `fk_booking_manager_id` FOREIGN KEY (`manager_id`) REFERENCES `users` (`id`)
        ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;
-- ----------------------------
-- Table structure for currencies
-- ----------------------------
DROP TABLE IF EXISTS `currencies`;
CREATE TABLE `currencies` (
        `currency_code_a` int(3) UNSIGNED NOT NULL,
        `currency_code_b` int(3) UNSIGNED NOT NULL,
        `date_time` bigint(20) UNSIGNED NOT NULL,
        `rate_buy` decimal(10,4) UNSIGNED NOT NULL,
        `rate_sell` decimal(10,4) UNSIGNED NOT NULL,
        PRIMARY KEY (`currency_code_a`, `currency_code_b`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;

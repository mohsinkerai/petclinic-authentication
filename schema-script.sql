-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.7.20 - MySQL Community Server (GPL)
-- Server OS:                    Linux
-- HeidiSQL Version:             9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for darbarmanagement
CREATE DATABASE IF NOT EXISTS `darbarmanagement` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `darbarmanagement`;

-- Dumping structure for table darbarmanagement.BATCH_JOB_EXECUTION
CREATE TABLE IF NOT EXISTS `BATCH_JOB_EXECUTION` (
  `JOB_EXECUTION_ID` bigint(20) NOT NULL,
  `VERSION` bigint(20) DEFAULT NULL,
  `JOB_INSTANCE_ID` bigint(20) NOT NULL,
  `CREATE_TIME` datetime NOT NULL,
  `START_TIME` datetime DEFAULT NULL,
  `END_TIME` datetime DEFAULT NULL,
  `STATUS` varchar(10) DEFAULT NULL,
  `EXIT_CODE` varchar(2500) DEFAULT NULL,
  `EXIT_MESSAGE` varchar(2500) DEFAULT NULL,
  `LAST_UPDATED` datetime DEFAULT NULL,
  `JOB_CONFIGURATION_LOCATION` varchar(2500) DEFAULT NULL,
  PRIMARY KEY (`JOB_EXECUTION_ID`),
  KEY `JOB_INST_EXEC_FK` (`JOB_INSTANCE_ID`),
  CONSTRAINT `JOB_INST_EXEC_FK` FOREIGN KEY (`JOB_INSTANCE_ID`) REFERENCES `BATCH_JOB_INSTANCE` (`JOB_INSTANCE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.
-- Dumping structure for table darbarmanagement.BATCH_JOB_EXECUTION_CONTEXT
CREATE TABLE IF NOT EXISTS `BATCH_JOB_EXECUTION_CONTEXT` (
  `JOB_EXECUTION_ID` bigint(20) NOT NULL,
  `SHORT_CONTEXT` varchar(2500) NOT NULL,
  `SERIALIZED_CONTEXT` text,
  PRIMARY KEY (`JOB_EXECUTION_ID`),
  CONSTRAINT `JOB_EXEC_CTX_FK` FOREIGN KEY (`JOB_EXECUTION_ID`) REFERENCES `BATCH_JOB_EXECUTION` (`JOB_EXECUTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.
-- Dumping structure for table darbarmanagement.BATCH_JOB_EXECUTION_PARAMS
CREATE TABLE IF NOT EXISTS `BATCH_JOB_EXECUTION_PARAMS` (
  `JOB_EXECUTION_ID` bigint(20) NOT NULL,
  `TYPE_CD` varchar(6) NOT NULL,
  `KEY_NAME` varchar(100) NOT NULL,
  `STRING_VAL` varchar(250) DEFAULT NULL,
  `DATE_VAL` datetime DEFAULT NULL,
  `LONG_VAL` bigint(20) DEFAULT NULL,
  `DOUBLE_VAL` double DEFAULT NULL,
  `IDENTIFYING` char(1) NOT NULL,
  KEY `JOB_EXEC_PARAMS_FK` (`JOB_EXECUTION_ID`),
  CONSTRAINT `JOB_EXEC_PARAMS_FK` FOREIGN KEY (`JOB_EXECUTION_ID`) REFERENCES `BATCH_JOB_EXECUTION` (`JOB_EXECUTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.
-- Dumping structure for table darbarmanagement.BATCH_JOB_EXECUTION_SEQ
CREATE TABLE IF NOT EXISTS `BATCH_JOB_EXECUTION_SEQ` (
  `ID` bigint(20) NOT NULL,
  `UNIQUE_KEY` char(1) NOT NULL,
  UNIQUE KEY `UNIQUE_KEY_UN` (`UNIQUE_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.
-- Dumping structure for table darbarmanagement.BATCH_JOB_INSTANCE
CREATE TABLE IF NOT EXISTS `BATCH_JOB_INSTANCE` (
  `JOB_INSTANCE_ID` bigint(20) NOT NULL,
  `VERSION` bigint(20) DEFAULT NULL,
  `JOB_NAME` varchar(100) NOT NULL,
  `JOB_KEY` varchar(32) NOT NULL,
  PRIMARY KEY (`JOB_INSTANCE_ID`),
  UNIQUE KEY `JOB_INST_UN` (`JOB_NAME`,`JOB_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.
-- Dumping structure for table darbarmanagement.BATCH_JOB_SEQ
CREATE TABLE IF NOT EXISTS `BATCH_JOB_SEQ` (
  `ID` bigint(20) NOT NULL,
  `UNIQUE_KEY` char(1) NOT NULL,
  UNIQUE KEY `UNIQUE_KEY_UN` (`UNIQUE_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.
-- Dumping structure for table darbarmanagement.BATCH_STEP_EXECUTION
CREATE TABLE IF NOT EXISTS `BATCH_STEP_EXECUTION` (
  `STEP_EXECUTION_ID` bigint(20) NOT NULL,
  `VERSION` bigint(20) NOT NULL,
  `STEP_NAME` varchar(4000) NOT NULL,
  `JOB_EXECUTION_ID` bigint(20) NOT NULL,
  `START_TIME` datetime NOT NULL,
  `END_TIME` datetime DEFAULT NULL,
  `STATUS` varchar(10) DEFAULT NULL,
  `COMMIT_COUNT` bigint(20) DEFAULT NULL,
  `READ_COUNT` bigint(20) DEFAULT NULL,
  `FILTER_COUNT` bigint(20) DEFAULT NULL,
  `WRITE_COUNT` bigint(20) DEFAULT NULL,
  `READ_SKIP_COUNT` bigint(20) DEFAULT NULL,
  `WRITE_SKIP_COUNT` bigint(20) DEFAULT NULL,
  `PROCESS_SKIP_COUNT` bigint(20) DEFAULT NULL,
  `ROLLBACK_COUNT` bigint(20) DEFAULT NULL,
  `EXIT_CODE` varchar(2500) DEFAULT NULL,
  `EXIT_MESSAGE` varchar(2500) DEFAULT NULL,
  `LAST_UPDATED` datetime DEFAULT NULL,
  PRIMARY KEY (`STEP_EXECUTION_ID`),
  KEY `JOB_EXEC_STEP_FK` (`JOB_EXECUTION_ID`),
  CONSTRAINT `JOB_EXEC_STEP_FK` FOREIGN KEY (`JOB_EXECUTION_ID`) REFERENCES `BATCH_JOB_EXECUTION` (`JOB_EXECUTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.
-- Dumping structure for table darbarmanagement.BATCH_STEP_EXECUTION_CONTEXT
CREATE TABLE IF NOT EXISTS `BATCH_STEP_EXECUTION_CONTEXT` (
  `STEP_EXECUTION_ID` bigint(20) NOT NULL,
  `SHORT_CONTEXT` varchar(2500) NOT NULL,
  `SERIALIZED_CONTEXT` text,
  PRIMARY KEY (`STEP_EXECUTION_ID`),
  CONSTRAINT `STEP_EXEC_CTX_FK` FOREIGN KEY (`STEP_EXECUTION_ID`) REFERENCES `BATCH_STEP_EXECUTION` (`STEP_EXECUTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.
-- Dumping structure for table darbarmanagement.BATCH_STEP_EXECUTION_SEQ
CREATE TABLE IF NOT EXISTS `BATCH_STEP_EXECUTION_SEQ` (
  `ID` bigint(20) NOT NULL,
  `UNIQUE_KEY` char(1) NOT NULL,
  UNIQUE KEY `UNIQUE_KEY_UN` (`UNIQUE_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.
-- Dumping structure for table darbarmanagement.hibernate_sequence
CREATE TABLE IF NOT EXISTS `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.
-- Dumping structure for table darbarmanagement.job_failure_items
CREATE TABLE IF NOT EXISTS `job_failure_items` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `failed_item` varchar(255) DEFAULT NULL,
  `failure_reason` varchar(255) DEFAULT NULL,
  `user_job_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_job_id` (`user_job_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.
-- Dumping structure for table darbarmanagement.user
CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `enabled` bit(1) NOT NULL,
  `isExpired` bit(1) NOT NULL,
  `isLocked` bit(1) NOT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`),
  KEY `enabled` (`enabled`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.
-- Dumping structure for table darbarmanagement.user_authority
CREATE TABLE IF NOT EXISTS `user_authority` (
  `user_id` bigint(20) NOT NULL,
  `authority` varchar(255) DEFAULT NULL,
  KEY `FKpqlsjpkybgos9w2svcri7j8xy` (`user_id`),
  KEY `authority` (`authority`),
  FULLTEXT KEY `authorityFT` (`authority`),
  CONSTRAINT `FKpqlsjpkybgos9w2svcri7j8xy` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.
-- Dumping structure for table darbarmanagement.user_job_data
CREATE TABLE IF NOT EXISTS `user_job_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_id` bigint(20) DEFAULT NULL,
  `job_name` varchar(255) DEFAULT NULL,
  `job_status` varchar(255) DEFAULT NULL,
  `job_type` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `job_id` (`job_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.
-- Dumping structure for table darbarmanagement.vehicle
CREATE TABLE IF NOT EXISTS `vehicle` (
  `id` bigint(20) NOT NULL,
  `chassisNumber` varchar(255) DEFAULT NULL,
  `color` varchar(255) NOT NULL,
  `driverCnic` varchar(255) NOT NULL,
  `driverCurrentAddress` varchar(255) NOT NULL,
  `driverMobile` varchar(255) NOT NULL,
  `driverName` varchar(255) NOT NULL,
  `driverPermanentAddress` varchar(255) DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  `engineNumber` varchar(255) DEFAULT NULL,
  `isOwnerDriver` bit(1) NOT NULL,
  `license` varchar(255) NOT NULL,
  `licenseExpiry` datetime NOT NULL,
  `licenseIssue` datetime NOT NULL,
  `make` varchar(255) NOT NULL,
  `model` varchar(255) DEFAULT NULL,
  `ownerCnic` varchar(255) NOT NULL,
  `ownerCurrentAddress` varchar(255) NOT NULL,
  `ownerMobile` varchar(255) NOT NULL,
  `ownerName` varchar(255) NOT NULL,
  `ownerPermanentAddress` varchar(255) DEFAULT NULL,
  `registration` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_e6kki0uvqil5mlrhakhtfpdxw` (`driverCnic`),
  UNIQUE KEY `UK_8yonp90jw51m90y0hhsxqe94r` (`ownerCnic`),
  FULLTEXT KEY `driverMobile` (`driverMobile`),
  FULLTEXT KEY `ownerMobile` (`ownerMobile`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.
-- Dumping structure for table darbarmanagement.volunteers
CREATE TABLE IF NOT EXISTS `volunteers` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `volunteer_age` varchar(255) DEFAULT NULL,
  `volunteer_category` int(11) DEFAULT NULL,
  `volunteer_cell_phone` varchar(255) DEFAULT NULL,
  `volunteer_designation` varchar(255) DEFAULT NULL,
  `volunteer_duty_day` varchar(255) DEFAULT NULL,
  `volunteer_duty_shift` varchar(255) DEFAULT NULL,
  `volunteer_duty_zone` varchar(255) DEFAULT NULL,
  `volunteer_email` varchar(255) DEFAULT NULL,
  `volunteer_home_phone` varchar(255) DEFAULT NULL,
  `volunteer_institution` varchar(255) DEFAULT NULL,
  `volunteer_jamatkhanna` varchar(255) DEFAULT NULL,
  `local_council` varchar(3000) DEFAULT NULL,
  `regional_council` varchar(3000) DEFAULT NULL,
  `volunteer_registration_date` varchar(255) DEFAULT NULL,
  `volunteer_reports_to` varchar(255) DEFAULT NULL,
  `volunteer_residential_address` varchar(255) DEFAULT NULL,
  `volunteer_cnic` varchar(255) NOT NULL,
  `volunteer_image` varchar(255) DEFAULT NULL,
  `volunteer_name` varchar(255) DEFAULT NULL,
  `isEnabled` bit(1) DEFAULT b'1',
  `isPictureAvailable` bit(1) DEFAULT NULL,
  `volunteer_committee` varchar(255) DEFAULT NULL,
  `volunteer_form_no` varchar(255) DEFAULT NULL,
  `volunteer_isprinted` bit(1) DEFAULT NULL,
  `volunteer_jurisdiction` varchar(255) DEFAULT NULL,
  `volunteer_memberof` varchar(255) DEFAULT NULL,
  `volunteer_site` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `volunteer_cnic` (`volunteer_cnic`),
  KEY `volunteer_isprinted` (`volunteer_isprinted`),
  KEY `isPictureAvailable` (`isPictureAvailable`),
  KEY `isEnabled` (`isEnabled`),
  KEY `volunteer_duty_zone` (`volunteer_duty_zone`),
  FULLTEXT KEY `regional_council` (`regional_council`),
  FULLTEXT KEY `volunteer_jamatkhanna` (`volunteer_jamatkhanna`),
  FULLTEXT KEY `local_council` (`local_council`),
  FULLTEXT KEY `volunteer_duty_zone_full_text` (`volunteer_duty_zone`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

-- Data exporting was unselected.
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;

INSERT INTO `user` (`id`, `enabled`, `isExpired`, `isLocked`, `password`, `username`) VALUES (1, b'1', b'0', b'0', '$2a$10$irvXlKRH0UjSf3.okoAuB.vuHqgSF4iX2zi0uhWuswt5m8mjL1reC', 'foo');
INSERT INTO `user` (`id`, `enabled`, `isExpired`, `isLocked`, `password`, `username`) VALUES (2, b'1', b'0', b'0', '$2a$10$kw/.fZAqYqqvDPEx/0hJiOqIhs2ESZCYdSjFpooRhYZgE6Yot3bLK', 'admin');
INSERT INTO `user` (`id`, `enabled`, `isExpired`, `isLocked`, `password`, `username`) VALUES (3, b'1', b'0', b'0', '$2a$10$TnfNzqSKZjNwY2Kvv18PVed0K0MfIVzeR40TadOgTE8tMQcNnwnse', 'registrar');
INSERT INTO `user` (`id`, `enabled`, `isExpired`, `isLocked`, `password`, `username`) VALUES (4, b'1', b'0', b'0', '$2a$10$zb2C3ddNE6razm4XmqQwv.ynRnCb92uZM4VX1m7DJCMQgmpeN4.Je', 'authorizer');

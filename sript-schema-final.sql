/*
SQLyog Community
MySQL - 5.7.14 : Database - darbarmanagement
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`darbarmanagement` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `darbarmanagement`;

/*Table structure for table `batch_job_execution` */

CREATE TABLE `batch_job_execution` (
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
  CONSTRAINT `JOB_INST_EXEC_FK` FOREIGN KEY (`JOB_INSTANCE_ID`) REFERENCES `batch_job_instance` (`JOB_INSTANCE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `batch_job_execution_context` */

CREATE TABLE `batch_job_execution_context` (
  `JOB_EXECUTION_ID` bigint(20) NOT NULL,
  `SHORT_CONTEXT` varchar(2500) NOT NULL,
  `SERIALIZED_CONTEXT` text,
  PRIMARY KEY (`JOB_EXECUTION_ID`),
  CONSTRAINT `JOB_EXEC_CTX_FK` FOREIGN KEY (`JOB_EXECUTION_ID`) REFERENCES `batch_job_execution` (`JOB_EXECUTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `batch_job_execution_params` */

CREATE TABLE `batch_job_execution_params` (
  `JOB_EXECUTION_ID` bigint(20) NOT NULL,
  `TYPE_CD` varchar(6) NOT NULL,
  `KEY_NAME` varchar(100) NOT NULL,
  `STRING_VAL` varchar(250) DEFAULT NULL,
  `DATE_VAL` datetime DEFAULT NULL,
  `LONG_VAL` bigint(20) DEFAULT NULL,
  `DOUBLE_VAL` double DEFAULT NULL,
  `IDENTIFYING` char(1) NOT NULL,
  KEY `JOB_EXEC_PARAMS_FK` (`JOB_EXECUTION_ID`),
  CONSTRAINT `JOB_EXEC_PARAMS_FK` FOREIGN KEY (`JOB_EXECUTION_ID`) REFERENCES `batch_job_execution` (`JOB_EXECUTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `batch_job_execution_seq` */

CREATE TABLE `batch_job_execution_seq` (
  `ID` bigint(20) NOT NULL,
  `UNIQUE_KEY` char(1) NOT NULL,
  UNIQUE KEY `UNIQUE_KEY_UN` (`UNIQUE_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `batch_job_instance` */

CREATE TABLE `batch_job_instance` (
  `JOB_INSTANCE_ID` bigint(20) NOT NULL,
  `VERSION` bigint(20) DEFAULT NULL,
  `JOB_NAME` varchar(100) NOT NULL,
  `JOB_KEY` varchar(32) NOT NULL,
  PRIMARY KEY (`JOB_INSTANCE_ID`),
  UNIQUE KEY `JOB_INST_UN` (`JOB_NAME`,`JOB_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `batch_job_seq` */

CREATE TABLE `batch_job_seq` (
  `ID` bigint(20) NOT NULL,
  `UNIQUE_KEY` char(1) NOT NULL,
  UNIQUE KEY `UNIQUE_KEY_UN` (`UNIQUE_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `batch_step_execution` */

CREATE TABLE `batch_step_execution` (
  `STEP_EXECUTION_ID` bigint(20) NOT NULL,
  `VERSION` bigint(20) NOT NULL,
  `STEP_NAME` varchar(100) NOT NULL,
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
  CONSTRAINT `JOB_EXEC_STEP_FK` FOREIGN KEY (`JOB_EXECUTION_ID`) REFERENCES `batch_job_execution` (`JOB_EXECUTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `batch_step_execution_context` */

CREATE TABLE `batch_step_execution_context` (
  `STEP_EXECUTION_ID` bigint(20) NOT NULL,
  `SHORT_CONTEXT` varchar(2500) NOT NULL,
  `SERIALIZED_CONTEXT` text,
  PRIMARY KEY (`STEP_EXECUTION_ID`),
  CONSTRAINT `STEP_EXEC_CTX_FK` FOREIGN KEY (`STEP_EXECUTION_ID`) REFERENCES `batch_step_execution` (`STEP_EXECUTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `batch_step_execution_seq` */

CREATE TABLE `batch_step_execution_seq` (
  `ID` bigint(20) NOT NULL,
  `UNIQUE_KEY` char(1) NOT NULL,
  UNIQUE KEY `UNIQUE_KEY_UN` (`UNIQUE_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `hibernate_sequence` */

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*Table structure for table `job_failure_items` */

CREATE TABLE `job_failure_items` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `failed_item_formNo` varchar(50) NOT NULL,
  `failed_item_CNIC` varchar(255) DEFAULT NULL,
  `failure_reason` varchar(255) DEFAULT NULL,
  `user_job_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1699 DEFAULT CHARSET=latin1;

/*Table structure for table `user` */

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `enabled` bit(1) NOT NULL,
  `isExpired` bit(1) NOT NULL,
  `isLocked` bit(1) NOT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*Table structure for table `user_authority` */

CREATE TABLE `user_authority` (
  `user_id` bigint(20) NOT NULL,
  `authority` varchar(255) DEFAULT NULL,
  KEY `FKpqlsjpkybgos9w2svcri7j8xy` (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*Table structure for table `user_job_data` */

CREATE TABLE `user_job_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_id` bigint(20) DEFAULT NULL,
  `job_name` varchar(255) DEFAULT NULL,
  `job_status` varchar(255) DEFAULT NULL,
  `job_type` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `job_total_items` varchar(55) DEFAULT NULL,
  `job_total_failure_items` varchar(55) DEFAULT NULL,
  `job_total_success_items` varchar(55) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

/*Table structure for table `vehicle` */

CREATE TABLE `vehicle` (
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
  UNIQUE KEY `UK_8yonp90jw51m90y0hhsxqe94r` (`ownerCnic`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*Table structure for table `volunteers` */

CREATE TABLE `volunteers` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`volunteer_age` VARCHAR(255) NULL DEFAULT NULL,
	`volunteer_category` INT(11) NULL DEFAULT NULL,
	`volunteer_cell_phone` VARCHAR(255) NULL DEFAULT NULL,
	`volunteer_designation` VARCHAR(255) NULL DEFAULT NULL,
	`volunteer_duty_day` VARCHAR(255) NULL DEFAULT NULL,
	`volunteer_duty_shift` VARCHAR(255) NULL DEFAULT NULL,
	`volunteer_duty_zone` VARCHAR(255) NULL DEFAULT NULL,
	`volunteer_email` VARCHAR(255) NULL DEFAULT NULL,
	`volunteer_home_phone` VARCHAR(255) NULL DEFAULT NULL,
	`volunteer_institution` VARCHAR(255) NULL DEFAULT NULL,
	`volunteer_jamatkhanna` VARCHAR(255) NULL DEFAULT NULL,
	`local_council` VARCHAR(3000) NULL DEFAULT NULL,
	`regional_council` VARCHAR(3000) NULL DEFAULT NULL,
	`volunteer_registration_date` VARCHAR(255) NULL DEFAULT NULL,
	`volunteer_reports_to` VARCHAR(255) NULL DEFAULT NULL,
	`volunteer_residential_address` VARCHAR(255) NULL DEFAULT NULL,
	`volunteer_cnic` VARCHAR(255) NOT NULL,
	`volunteer_image` VARCHAR(255) NULL DEFAULT NULL,
	`volunteer_name` VARCHAR(255) NULL DEFAULT NULL,
	`isEnabled` BIT(1) NULL DEFAULT b'1',
	`isPictureAvailable` BIT(1) NULL DEFAULT NULL,
	`volunteer_committee` VARCHAR(255) NULL DEFAULT NULL,
	`volunteer_form_no` VARCHAR(255) NULL DEFAULT NULL,
	`volunteer_isprinted` BIT(1) NULL DEFAULT NULL,
	`volunteer_jurisdiction` VARCHAR(255) NULL DEFAULT NULL,
	`volunteer_memberof` VARCHAR(255) NULL DEFAULT NULL,
	`volunteer_site` VARCHAR(255) NULL DEFAULT NULL,
	`volunteer_isprinteddate` DATE NULL DEFAULT NULL,
	PRIMARY KEY (`id`),
	UNIQUE INDEX `volunteer_cnic` (`volunteer_cnic`),
	INDEX `volunteer_isprinted` (`volunteer_isprinted`),
	INDEX `isPictureAvailable` (`isPictureAvailable`),
	INDEX `isEnabled` (`isEnabled`),
	INDEX `volunteer_duty_zone` (`volunteer_duty_zone`),
	FULLTEXT INDEX `regional_council` (`regional_council`),
	FULLTEXT INDEX `volunteer_jamatkhanna` (`volunteer_jamatkhanna`),
	FULLTEXT INDEX `local_council` (`local_council`),
	FULLTEXT INDEX `volunteer_duty_zone_full_text` (`volunteer_duty_zone`)
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1
;

INSERT INTO `user` (`id`, `enabled`, `isExpired`, `isLocked`, `password`, `username`) VALUES (1, b'1', b'0', b'0', '$2a$10$irvXlKRH0UjSf3.okoAuB.vuHqgSF4iX2zi0uhWuswt5m8mjL1reC', 'foo');
INSERT INTO `user` (`id`, `enabled`, `isExpired`, `isLocked`, `password`, `username`) VALUES (2, b'1', b'0', b'0', '$2a$10$kw/.fZAqYqqvDPEx/0hJiOqIhs2ESZCYdSjFpooRhYZgE6Yot3bLK', 'admin');
INSERT INTO `user` (`id`, `enabled`, `isExpired`, `isLocked`, `password`, `username`) VALUES (3, b'1', b'0', b'0', '$2a$10$TnfNzqSKZjNwY2Kvv18PVed0K0MfIVzeR40TadOgTE8tMQcNnwnse', 'registrar');
INSERT INTO `user` (`id`, `enabled`, `isExpired`, `isLocked`, `password`, `username`) VALUES (4, b'1', b'0', b'0', '$2a$10$zb2C3ddNE6razm4XmqQwv.ynRnCb92uZM4VX1m7DJCMQgmpeN4.Je', 'authorizer');

INSERT INTO `user_authority` (`user_id`, `authority`) VALUES (3, 'REGISTRAR');
INSERT INTO `user_authority` (`user_id`, `authority`) VALUES (4, 'REGISTRAR');
INSERT INTO `user_authority` (`user_id`, `authority`) VALUES (4, 'AUTHORIZER');
INSERT INTO `user_authority` (`user_id`, `authority`) VALUES (1, 'ADMIN');
INSERT INTO `user_authority` (`user_id`, `authority`) VALUES (1, 'REGISTRAR');
INSERT INTO `user_authority` (`user_id`, `authority`) VALUES (1, 'AUTHORIZER');
INSERT INTO `user_authority` (`user_id`, `authority`) VALUES (1, 'CARDISSUER');
INSERT INTO `user_authority` (`user_id`, `authority`) VALUES (2, 'ADMIN');
INSERT INTO `user_authority` (`user_id`, `authority`) VALUES (2, 'REGISTRAR');
INSERT INTO `user_authority` (`user_id`, `authority`) VALUES (2, 'AUTHORIZER');
INSERT INTO `user_authority` (`user_id`, `authority`) VALUES (2, 'CARDISSUER');


/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

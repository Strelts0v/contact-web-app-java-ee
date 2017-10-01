DROP DATABASE IF EXISTS `contact_db`;

CREATE DATABASE `contact_db`
  CHARACTER SET 'utf8'
  COLLATE 'utf8_general_ci';

USE `contact_db`;

CREATE TABLE `nationalities`(
  `id_nationality` INTEGER(11) UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
  `nationality` varchar(40) NOT NULL,
  PRIMARY KEY(`id_nationality`)
)ENGINE=InnoDB;

CREATE TABLE `companies`(
  `id_company` INTEGER(11) UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
  `company` VARCHAR(40) NOT NULL,
  PRIMARY KEY(`id_company`)
)ENGINE=InnoDB;

CREATE TABLE `photos` (
  `id_photo` INTEGER(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `photo` MEDIUMBLOB,
  PRIMARY KEY(`id_photo`)
)ENGINE=InnoDB;

CREATE TABLE `contacts` (
  `id_contact` INTEGER(11) UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
  `first_name` VARCHAR(50) NOT NULL,
  `surname` VARCHAR(50) NOT NULL,
  `patronymic` VARCHAR(50) NOT NULL,
  `birthday` DATE NOT NULL,
  `website` VARCHAR(50),
  `email` VARCHAR(50) NOT NULL,
  `country` VARCHAR(50) NOT NULL,
  `city` VARCHAR(50) NOT NULL,
  `address` VARCHAR(50) NOT NULL,
  `index_number` VARCHAR(50) NOT NULL,
  `gender` ENUM('Male', 'Female'),
  `marital_status` ENUM('Single', 'In a relationship', 'Married'),
  `id_nationality` INTEGER(11) UNSIGNED NOT NULL,
  `id_company` INTEGER(11) UNSIGNED NOT NULL,
  `id_photo` INTEGER(11) UNSIGNED NOT NULL,
  PRIMARY KEY(id_contact),
  FOREIGN KEY(id_company) REFERENCES companies(id_company),
  FOREIGN KEY(id_nationality) REFERENCES nationalities(id_nationality),
  FOREIGN KEY(id_photo) REFERENCES photos(id_photo)
)ENGINE=InnoDB;

CREATE INDEX `idx_id_nationality` ON `contacts` (`id_nationality` ASC);
CREATE INDEX `idx_id_photo` ON `contacts` (`id_photo` ASC);
CREATE INDEX `idx_id_company` ON `contacts` (`id_company` ASC);

CREATE TABLE `phones`(
  `id_phone` INTEGER(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `id_contact` INTEGER(11) UNSIGNED NOT NULL,
  `phone_number` VARCHAR(30) NOT NULL,
  `phone_type` ENUM('Mobile', 'Home', 'Working') NOT NULL,
  `commentary` TEXT,
  PRIMARY KEY(`id_phone`),
  FOREIGN KEY(`id_contact`) REFERENCES contacts(`id_contact`)
)ENGINE=InnoDB;

CREATE INDEX `idx_id_contact` ON `phones` (`id_contact` ASC);

CREATE TABLE `attachments`(
  `id_attachment` INTEGER(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `id_contact` INTEGER(11) UNSIGNED NOT NULL,
  `name` VARCHAR(50) NOT NULL,
  `download_date` DATE NOT NULL,
  `commentary` TEXT,
  `attachment` LONGBLOB,
  PRIMARY KEY (`id_attachment`),
  FOREIGN KEY(`id_contact`) REFERENCES contacts(`id_contact`)
)ENGINE=InnoDB;

CREATE INDEX `idx_id_contact` ON `attachments` (`id_contact` ASC);
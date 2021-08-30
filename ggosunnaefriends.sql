CREATE TABLE `account` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(45) NOT NULL,
  `password` CHAR(60) NOT NULL,
  `age` INT NOT NULL,
  `is_experience_raising_pet` BIT(1) NOT NULL,
  `experience` VARCHAR(100),
  `is_location_confirm` BIT(1) NOT NULL,
  `latitude` DECIMAL(19,2) NOT NULL,
  `longitude` DECIMAL(19,2) NOT NULL,
  `nickname` VARCHAR(10) NOT NULL,
  `sex` VARCHAR(6) NOT NULL,
  `administration_division` VARCHAR(10),
  `is_blocked` BIT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY(`id`),
  UNIQUE(`email`, `nickname`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

CREATE TABLE `post` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(30) NOT NULL,
  `description` VARCHAR(255) NOT NULL,
  `created_at` DATETIME NOT NULL,
  `application_end_date` DATE NOT NULL,
  `protection_start_date` DATE NOT NULL,
  `protection_end_date` DATE NOT NULL,
  `is_updated` BIT(1) NOT NULL DEFAULT 0,
  `contact_info` VARCHAR(100) NOT NULL,
  `is_application_end` BIT(1) NOT NULL DEFAULT 0,
  `account_id` INT NOT NULL,
  PRIMARY KEY(`id`),
  FOREIGN KEY(`account_id`) REFERENCES `account`(`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

CREATE TABLE `application` (
  `id` INT NOT NULL AUTO_INCREMENT, 
  `is_accepted` BIT(1) NOT NULL DEFAULT 0,
  `created_at` DATETIME NOT NULL,
  `account_id` INT NOT NULL,
  `post_id` INT NOT NULL,
  PRIMARY KEY(`id`),
  FOREIGN KEY(`account_id`) REFERENCES `account`(`id`),
  FOREIGN KEY(`post_id`) REFERENCES `post`(`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

CREATE TABLE `pet_image` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `path` VARCHAR(255) NOT NULL,
  `post_id` INT NOT NULL,
  PRIMARY KEY(`id`),
  FOREIGN KEY(`post_id`) REFERENCES `post`(`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

CREATE TABLE `pet_info` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `pet_name` VARCHAR(30) NOT NULL,
  `pet_sex` VARCHAR(6) NOT NULL,
  `pet_species` VARCHAR(30) NOT NULL,
  `post_id` INT NOT NULL,
  PRIMARY KEY(`id`),
  FOREIGN KEY(`post_id`) REFERENCES `post`(`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

CREATE TABLE `review` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `created_at` DATETIME NOT NULL,
  `comment` VARCHAR(255) NOT NULL,
  `grade` DECIMAL(2,1),
  `application_id` INT NOT NULL,
  `target_id` INT NOT NULL,
  `writer_id` INT NOT NULL,
  PRIMARY KEY(`id`),
  FOREIGN KEY(`application_id`) REFERENCES `application`(`id`),
  FOREIGN KEY(`target_id`) REFERENCES `account`(`id`),
  FOREIGN KEY(`writer_id`) REFERENCES `account`(`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

CREATE TABLE `report` (
  `id` INT NOT NULL AUTO_INCREMENT,
  PRIMARY KEY(`id`),
  FOREIGN KEY(`reporter_id`) REFERENCES `account`(`id`),
  FOREIGN KEY(`target_id`) REFERENCES `account`(`id`),
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

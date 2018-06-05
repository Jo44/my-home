-- My Home --

USE myhomedb;

-- DROP

DROP TABLE `myhomedb`.`job`;
DROP TABLE `myhomedb`.`user`;
DROP TABLE `myhomedb`.`file`;
DROP TABLE `myhomedb`.`note`;
DROP TABLE `myhomedb`.`youtube_video`;
DROP TABLE `myhomedb`.`youtube_playlist`;

-- CREATE

CREATE TABLE `myhomedb`.`job` (
  `job_id` INT PRIMARY KEY NOT NULL,
  `job_name` VARCHAR(30) NOT NULL,
  `job_processed` INT NOT NULL,
  `job_finished` TINYINT(1) NOT NULL,
  `job_status` TINYINT(1) NULL,
  `job_start_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `job_finish_time` TIMESTAMP NULL DEFAULT NULL
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE `myhomedb`.`user` (
  `user_id` INT PRIMARY KEY NOT NULL,
  `user_name` VARCHAR(30) NOT NULL UNIQUE,
  `user_pass` VARCHAR(50) NOT NULL,
  `user_email` VARCHAR(50) NOT NULL UNIQUE,
  `user_remember_me_token` VARCHAR(50) NULL,
  `user_validation_token` VARCHAR(50) NOT NULL,
  `user_active` TINYINT(1) NOT NULL,
  `user_reinit_token` VARCHAR(50) NULL,
  `user_reinit_date` TIMESTAMP NULL,
  `user_inscription_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `user_update` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE `myhomedb`.`file` (
  `file_id` INT PRIMARY KEY NOT NULL,
  `file_id_user` INT NOT NULL,
  `file_weight` BIGINT(13) NOT NULL,
  `file_name` VARCHAR(100) NOT NULL UNIQUE,
  `file_upload_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `file_update` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE `myhomedb`.`note` (
  `note_id` INT PRIMARY KEY NOT NULL,
  `note_id_user` INT NOT NULL,
  `note_title` VARCHAR(100) NOT NULL UNIQUE,
  `note_message` LONGTEXT NOT NULL,
  `note_save_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `note_update` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE `myhomedb`.`youtube_playlist` (
  `yt_playlist_id` INT PRIMARY KEY NOT NULL,
  `yt_playlist_id_user` INT NOT NULL,
  `yt_playlist_title` VARCHAR(50) NOT NULL,
  `yt_playlist_type` VARCHAR(50) NOT NULL,
  `yt_playlist_active` TINYINT(1) NOT NULL,
  `yt_playlist_create_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `yt_playlist_update` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE `myhomedb`.`youtube_video` (
  `yt_video_id` INT PRIMARY KEY NOT NULL,
  `yt_video_id_playlist` INT NOT NULL,
  `yt_video_id_url` VARCHAR(11) NOT NULL,
  `yt_video_artist` VARCHAR(50) NOT NULL,
  `yt_video_title` VARCHAR(50) NOT NULL,
  `yt_video_duration` VARCHAR(12) NOT NULL,
  `yt_video_save_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `yt_video_update` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- SELECT

SELECT * FROM `myhomedb`.`job`;
SELECT * FROM `myhomedb`.`user`;
SELECT * FROM `myhomedb`.`file`;
SELECT * FROM `myhomedb`.`note`;
SELECT * FROM `myhomedb`.`youtube_video`;
SELECT * FROM `myhomedb`.`youtube_playlist`;


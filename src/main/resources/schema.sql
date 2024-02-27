CREATE DATABASE IF NOT EXISTS `otp_auth`;   #

CREATE TABLE IF NOT EXISTS `user`
(
    `password` varchar(255) DEFAULT NULL,
    `username` varchar(255) NOT NULL,
    PRIMARY KEY (`username`)
);

CREATE TABLE IF NOT EXISTS `otp`
(
    `code`     varchar(255) DEFAULT NULL,
    `username` varchar(255) NOT NULL,
    PRIMARY KEY (`username`)
);

DELETE
FROM `otp_auth`.`user`;

DELETE
FROM `otp_auth`.`otp`;
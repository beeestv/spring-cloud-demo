CREATE DATABASE IF NOT EXISTS zuul DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
use zuul;

DROP TABLE IF EXISTS `route_group`;
CREATE TABLE `route_group`
(
    `id`          bigint(11)  NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name`        varchar(50) NOT NULL,
    `online`      tinyint(1)  NOT NULL,
    `description` text        NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


DROP TABLE IF EXISTS `route`;
CREATE TABLE `route`
(
    `id`           bigint(11)   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name`         varchar(50)  NOT NULL,
    `path`         varchar(255) NOT NULL,
    `service_id`   varchar(255) DEFAULT NULL,
    `url`          varchar(255) DEFAULT NULL,
    `strip_prefix` tinyint(1)   NOT NULL,
    UNIQUE KEY (`name`),
    UNIQUE KEY (`path`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


DROP TABLE IF EXISTS `request`;
CREATE TABLE `route`
(
    `id`           bigint(11)   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `route_id`     varchar(50)  NOT NULL,
    `host`         varchar(50)  NOT NULL,
    `method`       varchar(10)  NOT NULL,
    `url`          varchar(100) NOT NULL,
    `uri`          varchar(100) NOT NULL,
    `service_id`   varchar(100) DEFAULT NULL,
    `route_host`   varchar(100) DEFAULT NULL,
    `request_time` bigint(20)   NOT NULL,
    KEY (`host`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
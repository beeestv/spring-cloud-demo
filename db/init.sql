CREATE DATABASE IF NOT EXISTS zuul DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

use zuul;

DROP TABLE IF EXISTS `route_group`;
CREATE TABLE `route_group`
(
  `id`          bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `name`        varchar(50)         NOT NULL,
  `online`      tinyint(1)          NOT NULL,
  `description` text                NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


DROP TABLE IF EXISTS `route`;
CREATE TABLE `route`
(
  `id`           bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `name`         varchar(50)         NOT NULL,
  `path`         varchar(255)        NOT NULL,
  `service_id`   varchar(255)        DEFAULT NULL,
  `url`          varchar(255)        DEFAULT NULL,
  `strip_prefix` tinyint(1)          NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`name`),
  UNIQUE KEY (`path`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
/* Convert Old Data */

DROP PROCEDURE IF EXISTS `_ConvertOldData`;
-- Cutting Line --
CREATE PROCEDURE `_ConvertOldData` ()
  BEGIN
    IF EXISTS (
        SELECT * FROM `INFORMATION_SCHEMA`.`TABLES`
        WHERE (`TABLE_NAME` = "NuclearData" AND `TABLE_SCHEMA`=(SELECT database())))
    THEN
      RENAME TABLE `NuclearData` TO `NuclearLogin`;
    END IF;
    DROP VIEW IF EXISTS `vNuclearData`;
  END;
-- Cutting Line --
CALL `_ConvertOldData`();
DROP PROCEDURE `_ConvertOldData`;

/* Standard mysql database format for NuclearLogin  */

-- Cutting Line --
CREATE TABLE IF NOT EXISTS `NuclearLogin` (
  `name` VARCHAR(16) PRIMARY KEY,
  `hash` BINARY(64),
  `registerdate` DATETIME,
  `logindate` DATETIME,
  `lastip` INT(10) UNSIGNED,
  `lastuuid` BINARY(16)
);

DROP FUNCTION IF EXISTS `UUIDToBinary`;
DROP FUNCTION IF EXISTS `BinaryToUUID`;
DROP PROCEDURE IF EXISTS `NuclearPlayerCheck`;
DROP PROCEDURE IF EXISTS `NuclearPlayerRegister`;
DROP PROCEDURE IF EXISTS `NuclearPlayerUnregister`;
DROP PROCEDURE IF EXISTS `NuclearPlayerLogin`;
DROP PROCEDURE IF EXISTS `NuclearPlayerLogout`;
DROP VIEW IF EXISTS `vNuclearLogin`;

-- Cutting Line --
CREATE FUNCTION `UUIDToBinary`(cUUID char(36)) RETURNS binary(16)
  BEGIN
    RETURN CONCAT(UNHEX(LEFT(cUUID,8)),UNHEX(MID(cUUID,10,4)),UNHEX(MID(cUUID,15,4)),UNHEX(MID(cUUID,20,4)),UNHEX(RIGHT(cUUID,12)));
  END;

-- Cutting Line --
CREATE FUNCTION `BinaryToUUID`(uuid BINARY(16)) RETURNS char(36)
  BEGIN
    RETURN CONCAT(HEX(LEFT(uuid,4)),'-', HEX(MID(uuid,5,2)),'-', HEX(MID(uuid,7,2)),'-',HEX(MID(uuid,9,2)),'-',HEX(RIGHT(uuid,6)));
  END;

-- Cutting Line --
CREATE PROCEDURE `NuclearPlayerCheck`(
  `_name` VARCHAR(16),
  `_ip` VARCHAR(64),
  `_uuid` CHAR(36)
)
  BEGIN
    DECLARE `_exist` BOOLEAN DEFAULT FALSE;
    DECLARE `_matchip` BOOLEAN DEFAULT FALSE;
    DECLARE `_matchuuid` BOOLEAN DEFAULT FALSE;
    DECLARE `_countip` INT(11) DEFAULT 0;
    DECLARE `_countuuid` INT(11) DEFAULT 0;
    SET `_exist` = EXISTS (SELECT `name` FROM `NuclearLogin` WHERE `name`=`_name`);
    IF `_exist` THEN
      IF inet_aton(`_ip`) = (SELECT `lastip` FROM `NuclearLogin` WHERE `name`=`_name`) THEN
        SET `_matchip` = TRUE;
      END IF;
      IF `UUIDToBinary`(`_uuid`) = (SELECT `lastuuid` FROM `NuclearLogin` WHERE `name`=`_name`) THEN
        SET `_matchuuid` = TRUE;
      END IF;
    END IF;
    SET `_countip` = (SELECT count(*) FROM `NuclearLogin` WHERE `lastip`=inet_aton(`_ip`));
    SET `_countuuid` = (SELECT count(*) FROM `NuclearLogin` WHERE `lastuuid`=`UUIDToBinary`(`_uuid`));


    CREATE TABLE IF NOT EXISTS `_result_check` (
      `name` VARCHAR(16) PRIMARY KEY NOT NULL,
      `exist` BOOLEAN NOT NULL,
      `matchip` BOOLEAN NOT NULL,
      `matchuuid` BOOLEAN NOT NULL,
      `countip` INT NOT NULL,
      `countuuid` INT NOT NULL
    ) ENGINE = MEMORY;
    INSERT INTO `_result_check` (`name`, `exist`, `matchip`, `matchuuid`, `countip`, `countuuid`)
    VALUES (`_name`, `_exist`, `_matchip`, `_matchuuid`, `_countip`, `_countuuid`);
    SELECT * FROM `_result_check`;
    DROP TABLE IF EXISTS `_result_check`;
  END;

-- Cutting Line --
CREATE PROCEDURE `NuclearPlayerRegister`(
  `_name` VARCHAR(16),
  `_hash` CHAR(128),
  `_ip` VARCHAR(64),
  `_uuid` CHAR(36)
)
  BEGIN
    IF NOT EXISTS (SELECT `name` FROM `NuclearLogin` WHERE `name`=`_name`) THEN
      INSERT INTO `NuclearLogin` (`name`, `hash`, `registerdate`, `logindate`, `lastip`, `lastuuid`)
      VALUES (`_name`, unhex(`_hash`), now(), now(), inet_aton(`_ip`), `UUIDToBinary`(`_uuid`));
    END IF;
  END;

-- Cutting Line --
CREATE PROCEDURE `NuclearPlayerUnregister`(
  `_name` VARCHAR(16),
  `_hash` CHAR(128)
)
  BEGIN

    DECLARE `_exist` BOOLEAN DEFAULT FALSE;
    DECLARE `_hash_correct` BOOLEAN DEFAULT FALSE;
    SET `_exist` = EXISTS (SELECT `name` FROM `NuclearLogin` WHERE `name`=`_name`);
    IF `_exist` THEN
      IF unhex(`_hash`) = (SELECT `hash` FROM `NuclearLogin` WHERE `name`=`_name`) THEN
        SET `_hash_correct` = TRUE;
        DELETE FROM `NuclearLogin` WHERE `name`= `_name`;
      ELSE
        SET `_hash_correct` = FALSE;
      END IF;
    END IF;

    CREATE TABLE IF NOT EXISTS `_result_unregister` (
      `name` VARCHAR(16) PRIMARY KEY NOT NULL,
      `exist` BOOLEAN NOT NULL,
      `hash` BOOLEAN NOT NULL
    ) ENGINE = MEMORY;
    INSERT INTO `_result_unregister` (`name`, `exist`, `hash`)
    VALUES (`_name`, `_exist`, `_hash_correct`);
    SELECT * FROM `_result_unregister`;
    DROP TABLE IF EXISTS `_result_unregister`;
  END;

-- Cutting Line --
CREATE PROCEDURE `NuclearPlayerLogin`(
  `_name` VARCHAR(16),
  `_hash` CHAR(128),
  `_ip` VARCHAR(64),
  `_uuid` CHAR(36)
)
  BEGIN
    DECLARE `_exist` BOOLEAN DEFAULT FALSE;
    DECLARE `_success` BOOLEAN DEFAULT FALSE;
    SET `_exist` = EXISTS (SELECT `name` FROM `NuclearLogin` WHERE `name`=`_name`);
    IF `_exist` THEN
      SET `_success` = unhex(`_hash`) = (SELECT `hash` FROM `NuclearLogin` WHERE `name`=`_name`);
      IF `_success` THEN
        UPDATE `NuclearLogin`
        SET
          `logindate` = now(),
          `lastip` = inet_aton(`_ip`),
          `lastuuid` = `UUIDToBinary`(`_uuid`)
        WHERE `name` = `_name`;
      END IF;
    END IF;

    CREATE TABLE IF NOT EXISTS `_result_login` (
      `name` VARCHAR(16) PRIMARY KEY NOT NULL,
      `exist` BOOLEAN NOT NULL,
      `success` BOOLEAN NOT NULL
    ) ENGINE = MEMORY;
    INSERT INTO `_result_login` (`name`, `exist`, `success`)
    VALUES (`_name`, `_exist`, `_success`);
    SELECT * FROM `_result_login`;
    DROP TABLE IF EXISTS `_result_login`;
  END;

-- Cutting Line --
CREATE PROCEDURE `NuclearPlayerLogout`(
  `_name` VARCHAR(16),
  `_ip` VARCHAR(64),
  `_uuid` CHAR(36)
)
  BEGIN
    DECLARE `_exist` BOOLEAN DEFAULT FALSE;
    SET `_exist` = EXISTS (SELECT `name` FROM `NuclearLogin` WHERE `name`=`_name`);
    IF `_exist` THEN
      UPDATE `NuclearLogin`
      SET
        `logindate` = now(),
        `lastip` = inet_aton(`_ip`),
        `lastuuid` = `UUIDToBinary`(`_uuid`)
      WHERE `name` = `_name`;
    END IF;
  END;

-- Cutting Line --
CREATE VIEW `vNuclearLogin` AS
  SELECT
    `NuclearLogin`.`name` AS `name`,
    HEX(`NuclearLogin`.`hash`) AS `hash`,
    `NuclearLogin`.`registerdate` AS `registerdate`,
    `NuclearLogin`.`logindate` AS `logindate`,
    INET_NTOA(`NuclearLogin`.`lastip`) AS `lastip`,
    BINARYTOUUID(`NuclearLogin`.`lastuuid`) AS `lastuuid`
  FROM
    `NuclearLogin`
;
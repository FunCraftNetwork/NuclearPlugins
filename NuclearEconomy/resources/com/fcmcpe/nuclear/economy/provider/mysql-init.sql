CREATE TABLE IF NOT EXISTS `NuclearEconomy` (
  `name` VARCHAR(16) PRIMARY KEY,
  `money` BIGINT UNSIGNED
);

DROP PROCEDURE IF EXISTS `NuclearMoneyRegister`;
DROP PROCEDURE IF EXISTS `NuclearMoneyCheck`;
DROP PROCEDURE IF EXISTS `NuclearMoneyPay`;

-- Cutting Line --
CREATE PROCEDURE `NuclearMoneyRegister` (
  `_name` VARCHAR(16)
)
  BEGIN
    DECLARE `_exist` BOOLEAN DEFAULT FALSE;
    IF EXISTS(SELECT `money` FROM `NuclearEconomy` WHERE `name`=`_name`) THEN
      SET `_exist`=TRUE;
    ELSE
      INSERT INTO `NuclearEconomy` (`name`, `money`)
      VALUES (`_name`, 0);
    END IF;
    CREATE TABLE IF NOT EXISTS `_result_money_register` (
      `name` VARCHAR(16) PRIMARY KEY NOT NULL,
      `exist` BOOLEAN NOT NULL
    ) ENGINE = MEMORY;
    INSERT INTO `_result_money_register` (`name`, `exist`)
    VALUES (`_name`, `_exist`);
    SELECT * FROM `_result_money_register`;
    DROP TABLE IF EXISTS `_result_money_register`;
  END;

-- Cutting Line --
CREATE PROCEDURE `NuclearMoneyCheck` (
  `_name` VARCHAR(16)
)
  BEGIN
    DECLARE `_exist` BOOLEAN DEFAULT FALSE;
    DECLARE `_money` BIGINT UNSIGNED DEFAULT 0;
    IF EXISTS(SELECT `money` FROM `NuclearEconomy` WHERE `name`=`_name`) THEN
      SET `_exist`=TRUE;
      SET `_money`=(SELECT `money` FROM `NuclearEconomy` WHERE `name`=`_name`);
    END IF;
    CREATE TABLE IF NOT EXISTS `_result_money_check` (
      `name` VARCHAR(16) PRIMARY KEY NOT NULL,
      `exist` BOOLEAN NOT NULL,
      `money` BIGINT UNSIGNED NOT NULL
    ) ENGINE = MEMORY;
    INSERT INTO `_result_money_check` (`name`, `exist`, `money`)
    VALUES (`_name`, `_exist`, `_money`);
    SELECT * FROM `_result_money_check`;
    DROP TABLE IF EXISTS `_result_money_check`;
  END;

-- Cutting Line --
CREATE PROCEDURE `NuclearMoneyPay` (
  `_from` VARCHAR(16),
  `_to` VARCHAR(16),
  `_money` BIGINT UNSIGNED
)
  BEGIN
    DECLARE `_from_enough` BOOLEAN DEFAULT FALSE;
    DECLARE `_from_exist` BOOLEAN DEFAULT FALSE;
    DECLARE `_to_exist` BOOLEAN DEFAULT FALSE;
    DECLARE `_success` BOOLEAN DEFAULT FALSE;
    IF EXISTS (SELECT `money` FROM `NuclearEconomy` WHERE `name`=`_from`) THEN
      SET `_from_exist` = TRUE;
    END IF;
    IF EXISTS (SELECT `money` FROM `NuclearEconomy` WHERE `name`=`_to`) THEN
      SET `_to_exist` = TRUE;
    END IF;
    IF (SELECT `money` FROM `NuclearEconomy` WHERE `name`=`_from`)>=`_money` THEN
      SET `_from_enough` = TRUE;
    END IF;
    IF `_from_exist`=TRUE AND `_to_exist`=TRUE AND `_from_enough` THEN
      UPDATE `NuclearEconomy` SET `money`=`money`-`_money` WHERE `name`=`_from`;
      UPDATE `NuclearEconomy` SET `money`=`money`+`_money` WHERE `name`=`_to`;
      SET `_success`=TRUE;
    END IF;
    CREATE TABLE IF NOT EXISTS `_result_money_pay` (
      `from` VARCHAR(16) NOT NULL,
      `to` VARCHAR(16) NOT NULL,
      `fromexist` BOOLEAN NOT NULL,
      `toexist` BOOLEAN NOT NULL,
      `money` BIGINT UNSIGNED NOT NULL,
      `enough` BOOLEAN NOT NULL,
      `success` BOOLEAN NOT NULL
    ) ENGINE = MEMORY;
    INSERT INTO `_result_money_pay` (`from`, `to`, `fromexist`, `toexist`, `money`, `enough`, `success`)
    VALUES (`_from`, `_to`, `_from_exist`, `_to_exist`, `_money`, `_from_enough`, `_success`);
    SELECT * FROM `_result_money_pay`;
    DROP TABLE IF EXISTS `_result_money_pay`;
  END;

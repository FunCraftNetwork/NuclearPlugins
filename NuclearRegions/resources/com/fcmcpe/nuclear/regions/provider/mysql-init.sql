CREATE TABLE IF NOT EXISTS `NuclearRegions-Location` (
  `idRegion` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `world` VARCHAR(16),
  `fromX` INT NOT NULL,
  `deltaX` INT NOT NULL,
  `fromY` TINYINT UNSIGNED NOT NULL,
  `deltaY` TINYINT NOT NULL,
  `fromZ` INT NOT NULL,
  `deltaZ` INT NOT NULL,
  UNIQUE INDEX `idRegion_UNIQUE` (`idRegion` ASC)
);

CREATE TABLE IF NOT EXISTS `NuclearRegions-Permission` (
  `idRegion` INT UNSIGNED NOT NULL,
  `name` VARCHAR(16) COMMENT 'Player Name',
  `perm` BINARY(4)
);

DROP FUNCTION IF EXISTS `IsCoordinateIn`;
DROP FUNCTION IF EXISTS `IsRegionIn`;
DROP PROCEDURE IF EXISTS `NuclearRegionAdd`;
DROP PROCEDURE IF EXISTS `NuclearRegionRemove`;
DROP PROCEDURE IF EXISTS `NuclearRegionPermUpdate`;

-- Cutting Line --
CREATE FUNCTION `IsCoordinateIn` (
  `_cx` INT, `_cy` TINYINT UNSIGNED, `_cz` INT,
  `_fromX` INT, `_deltaX` INT,
  `_fromY` TINYINT UNSIGNED, `_deltaY` TINYINT,
  `_fromZ` INT, `_deltaZ` INT
) RETURNS BOOLEAN
  BEGIN
    DECLARE `_pos1x` INT;
    DECLARE `_pos1y` TINYINT UNSIGNED;
    DECLARE `_pos1z` INT;
    DECLARE `_pos2x` INT;
    DECLARE `_pos2y` TINYINT UNSIGNED;
    DECLARE `_pos2z` INT;
    IF `_deltaX` >= 0 THEN
      SET `_pos1x` = `_fromX`;
      SET `_pos2x` = `_fromX` + `_deltaX`;
    ELSE
      SET `_pos1x` = `_fromX` + `_deltaX`;
      SET `_pos2x` = `_fromX`;
    END IF;
    IF `_deltaY` >= 0 THEN
      SET `_pos1y` = `_fromY`;
      SET `_pos2y` = `_fromY` + `_deltaY`;
    ELSE
      SET `_pos1y` = `_fromY` + `_deltaY`;
      SET `_pos2y` = `_fromY`;
    END IF;
    IF `_deltaZ` >= 0 THEN
      SET `_pos1z` = `_fromZ`;
      SET `_pos2z` = `_fromZ` + `_deltaZ`;
    ELSE
      SET `_pos1z` = `_fromZ` + `_deltaZ`;
      SET `_pos2z` = `_fromZ`;
    END IF;

    IF (`_cx` >= `_pos1x` AND `_cx` <= `_pos2x`) AND
       (`_cy` >= `_pos1y` AND `_cy` <= `_pos2y`) AND
       (`_cz` >= `_pos1z` AND `_cz` <= `_pos2z`)
    THEN
      RETURN TRUE;
    ELSE
      RETURN FALSE;
    END IF;
  END;

-- Cutting Line --
CREATE FUNCTION `IsRegionIn` (
  `_cx` INT,`_cdeltaX` INT,
  `_cy` TINYINT UNSIGNED, `_cdeltaY` TINYINT UNSIGNED,
  `_cz` INT, `_cdeltaZ` INT,
  `_fromX` INT, `_deltaX` INT,
  `_fromY` TINYINT UNSIGNED, `_deltaY` TINYINT,
  `_fromZ` INT, `_deltaZ` INT
) RETURNS BOOLEAN
  BEGIN
    DECLARE `_c1x` INT;
    DECLARE `_c1y` TINYINT UNSIGNED;
    DECLARE `_c1z` INT;
    DECLARE `_c2x` INT;
    DECLARE `_c2y` TINYINT UNSIGNED;
    DECLARE `_c2z` INT;
    IF `_cdeltaX` >= 0 THEN
      SET `_c1x` = `_cx`;
      SET `_c2x` = `_cx` + `_cdeltaX`;
    ELSE
      SET `_c1x` = `_cx` + `_cdeltaX`;
      SET `_c2x` = `_cx`;
    END IF;
    IF `_cdeltaY` >= 0 THEN
      SET `_c1y` = `_cy`;
      SET `_c2y` = `_cy` + `_cdeltaY`;
    ELSE
      SET `_c1y` = `_cy` + `_cdeltaY`;
      SET `_c2y` = `_cy`;
    END IF;
    IF `_cdeltaZ` >= 0 THEN
      SET `_c1z` = `_cz`;
      SET `_c2z` = `_cz` + `_cdeltaZ`;
    ELSE
      SET `_c1z` = `_cz` + `_cdeltaZ`;
      SET `_c2z` = `_cz`;
    END IF;

    IF (SELECT `IsCoordinateIn` (
        `_c1x`, `_c1y`, `_c1z`,
        `_fromX`, `_deltaX`,
        `_fromY`, `_deltaY`,
        `_fromZ`, `_deltaZ`)
    ) OR (SELECT `IsCoordinateIn` (
        `_c2x`, `_c2y`, `_c2z`,
        `_fromX`, `_deltaX`,
        `_fromY`, `_deltaY`,
        `_fromZ`, `_deltaZ`) )
    THEN
      RETURN TRUE;
    ELSE
      RETURN FALSE;
    END IF;
  END;

-- Cutting Line --
CREATE PROCEDURE `NuclearRegionAdd` (
  `_world` VARCHAR(16),
  `_fromX` INT, `_deltaX` INT,
  `_fromY` TINYINT UNSIGNED, `_deltaY` TINYINT,
  `_fromZ` INT, `_deltaZ` INT
)
  BEGIN
    DECLARE `_currentID` INT;
    DECLARE `_cur_world` VARCHAR(16);
    DECLARE `_cur_fromX` INT;
    DECLARE `_cur_deltaX` INT;
    DECLARE `_cur_fromY` TINYINT UNSIGNED;
    DECLARE `_cur_deltaY` TINYINT;
    DECLARE `_cur_fromZ` INT;
    DECLARE `_cur_deltaZ` INT;
    DECLARE `_done` BOOLEAN DEFAULT FALSE;
    DECLARE `_conflict` BOOLEAN DEFAULT FALSE;
    DECLARE `_id` INT DEFAULT -1;
    DECLARE `rs` CURSOR FOR
      SELECT `idRegion`, `world`, `fromX`, `deltaX`, `fromY`, `deltaY`, `fromZ`, `deltaZ`
      FROM `NuclearRegions-Location`;
    DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET `_done` = TRUE;

    OPEN `rs`;
    FETCH `rs` INTO `_currentID`, `_cur_world`, `_cur_fromX`, `_cur_deltaX`, `_cur_fromY`, `_cur_deltaY`, `_cur_fromZ`, `_cur_deltaZ`;
    REPEAT
      IF NOT `_done` THEN
        IF (SELECT `IsRegionIn`(
            `_fromX`, `_deltaX`,
            `_fromY`, `_deltaY`,
            `_fromZ`, `_deltaZ`,
            `_cur_fromX`, `_cur_deltaX`,
            `_cur_fromY`, `_cur_deltaY`,
            `_cur_fromZ`, `_cur_deltaZ`
        ) AND (`_cur_world` = `_world`)) THEN
          SET `_conflict` = TRUE;
        END IF;
      END IF;
      FETCH `rs` INTO `_currentID`, `_cur_world`, `_cur_fromX`, `_cur_deltaX`, `_cur_fromY`, `_cur_deltaY`, `_cur_fromZ`, `_cur_deltaZ`;
    UNTIL `_done` END REPEAT;
    CLOSE `rs`;

    IF NOT `_conflict` THEN
      INSERT INTO `NuclearRegions-Location` (`world`, `fromX`, `deltaX`, `fromY`, `deltaY`, `fromZ`, `deltaZ`)
      VALUES (`_cur_world`, `_fromX`, `_deltaX`, `_fromY`, `_deltaY`, `_fromZ`, `_deltaZ`);
      -- todo: INSERT INTO `NuclearRegions-Permission`
      SET `_id` = (SELECT `idRegion` FROM `NuclearRegions-Location` WHERE
        `fromX` = `_fromX` AND
        `deltaX` = `_deltaX` AND
        `fromY` = `_fromY` AND
        `deltaY` = `_deltaY` AND
        `fromZ` = `_fromZ` AND
        `deltaZ` = `_deltaZ`
      );
    END IF;

    CREATE TABLE IF NOT EXISTS `_result_region_add` (
      `idRegion` INT NOT NULL,
      `conflict` BOOLEAN NOT NULL
    ) ENGINE = MEMORY;
    INSERT INTO `_result_region_add` (`idRegion`, `conflict`)
    VALUES (`_id`, `_conflict`);
    SELECT * FROM `_result_region_add`;
    DROP TABLE IF EXISTS `_result_region_add`;
  END;

-- Cutting Line --
CREATE PROCEDURE `NuclearRegionRemove`(
  `_id` INT UNSIGNED
)
  BEGIN
    DECLARE `_exist` BOOLEAN DEFAULT FALSE;
    SET `_exist` = EXISTS(SELECT `idRegion` FROM `NuclearRegions-Location` WHERE `idRegion` = `_id`);
    IF `_exist` THEN
      DELETE FROM `NuclearRegions-Location` WHERE `idRegion` = `_id`;
    END IF;

    CREATE TABLE IF NOT EXISTS `_result_region_remove` (
      `idRegion` INT NOT NULL,
      `exist` BOOLEAN NOT NULL
    ) ENGINE = MEMORY;
    INSERT INTO `_result_region_remove` (`idRegion`, `exist`)
    VALUES (`_id`, `_exist`);
    SELECT * FROM `_result_region_remove`;
    DROP TABLE IF EXISTS `_result_region_remove`;
  END;

-- Cutting Line --
CREATE PROCEDURE `NuclearRegionPermUpdate` (
  `_id` INT UNSIGNED,
  `_name` VARCHAR(16),
  `_perm` BINARY(4)
)
  BEGIN
    DECLARE `_exist` BOOLEAN DEFAULT FALSE;
    SET `_exist` = EXISTS(SELECT `idRegion` FROM `NuclearRegions-Permission` WHERE `idRegion` = `_id`);
    REPLACE INTO `NuclearRegions-Permission` (`idRegion`, `name`, `perm`)
    VALUES (`_id`, `_name`, `_perm`);
    CREATE TABLE IF NOT EXISTS `_result_region_perm_upd` (
      `idRegion` INT NOT NULL,
      `exist` BOOLEAN NOT NULL
    ) ENGINE = MEMORY;
    INSERT INTO `_result_region_perm_upd` (`idRegion`, `exist`)
    VALUES (`_id`, `_exist`);
    SELECT * FROM `_result_region_perm_upd`;
    DROP TABLE IF EXISTS `_result_region_perm_upd`;
  END;
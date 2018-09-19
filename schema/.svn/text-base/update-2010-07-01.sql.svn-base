-- Add affectingInventory
ALTER TABLE `inventory`.`QuantityUpdateBulk` ADD COLUMN `affectingInventory` BIT  DEFAULT 1;
ALTER TABLE `inventory`.`Lot` ADD COLUMN `initialQuantity` DOUBLE, ADD COLUMN `initialScale` VARCHAR(10), ADD COLUMN `locationDetail` VARCHAR(200)  DEFAULT NULL AFTER `location`, MODIFY COLUMN `container` BIGINT(20)  DEFAULT NULL;

--
-- Lot lists
create table LotList (
    id bigint not null auto_increment,
    createdBy varchar(255),
    createdDate datetime,
    lastUpdated datetime,
    lastUpdatedBy varchar(255),
    name varchar(200) not null,
    owner_id bigint,
    primary key (id)
) type=InnoDB;

create table LotListLots (
    LotList_id bigint not null,
    element bigint
) type=InnoDB;

alter table LotList add index FK784A4F6FCBDFED0E (owner_id), add constraint FK784A4F6FCBDFED0E foreign key (owner_id) references User (id);
alter table LotListLots add index FKA2CB3B5167D2C3F4 (LotList_id), add constraint FKA2CB3B5167D2C3F4 foreign key (LotList_id) references LotList (id);

create table FieldItem (
    id bigint not null,
    primary key (id)
) type=InnoDB;

create table FieldLot (
    geoposDate datetime,
    latitude double precision,
    longitude double precision,
    plantingDate datetime,
    id bigint not null,
    primary key (id)
) type=InnoDB;


alter table FieldItem 
    add index FKC2D6A30D71B11CFC (id), 
    add constraint FKC2D6A30D71B11CFC 
    foreign key (id) 
    references Item (id);

alter table FieldLot 
    add index FKCC7A94172CF549DE (id), 
    add constraint FKCC7A94172CF549DE 
    foreign key (id) 
    references Lot (id);

ALTER TABLE `inventory`.`QuantityUpdate` ADD COLUMN `originalQty` DOUBLE  DEFAULT NULL AFTER `description_id`;


--
-- Lot trials
create table Trait (
    id bigint not null auto_increment,
    createdBy varchar(255),
    createdDate datetime,
    lastUpdated datetime,
    lastUpdatedBy varchar(255),
    version integer not null,
    description longtext,
    title varchar(255),
    uom varchar(20),
    var varchar(50),
    primary key (id)
) type=InnoDB;

create table TraitCoding (
    id bigint not null auto_increment,
    coding varchar(255),
    value double precision not null,
    trait_id bigint not null,
    primary key (id)
) type=InnoDB;

create table TraitGroup (
    id bigint not null auto_increment,
    createdBy varchar(255),
    createdDate datetime,
    lastUpdated datetime,
    lastUpdatedBy varchar(255),
    version integer not null,
    description varchar(255),
    shortName varchar(255),
    title varchar(255),
    primary key (id)
) type=InnoDB;

create table TraitGroupTraits (
    TraitGroup_id bigint not null,
    traits_id bigint not null,
    position integer not null,
    primary key (TraitGroup_id, position)
) type=InnoDB;

    
create table LotTraitLastValue (
    id bigint not null auto_increment,
    createdBy varchar(255),
    createdDate datetime,
    lastUpdated datetime,
    lastUpdatedBy varchar(255),
    date datetime,
    value double precision,
    entity_id bigint not null,
    trait_id bigint not null,
    trial_id bigint,
    primary key (id)
) type=InnoDB;

create table LotTraitValue (
    id bigint not null auto_increment,
    createdBy varchar(255),
    createdDate datetime,
    lastUpdated datetime,
    lastUpdatedBy varchar(255),
    date datetime,
    value double precision,
    entity_id bigint not null,
    trait_id bigint not null,
    trial_id bigint,
    primary key (id)
) type=InnoDB;

create table LotTrial (
    id bigint not null auto_increment,
    createdBy varchar(255),
    createdDate datetime,
    lastUpdated datetime,
    lastUpdatedBy varchar(255),
    version integer not null,
    date datetime,
    description longtext,
    name varchar(200) not null,
    status varchar(255),
    traitGroup_id bigint,
    primary key (id)
) type=InnoDB;

alter table LotTraitLastValue 
    add index FK6787E79ED11BBA (entity_id), 
    add constraint FK6787E79ED11BBA 
    foreign key (entity_id) 
    references Lot (id);

alter table LotTraitLastValue 
    add index FK6787E79EB1CB66F7 (trial_id), 
    add constraint FK6787E79EB1CB66F7 
    foreign key (trial_id) 
    references LotTrial (id);

alter table LotTraitLastValue 
    add index FK6787E79E8A71FF26 (trait_id), 
    add constraint FK6787E79E8A71FF26 
    foreign key (trait_id) 
    references Trait (id);

alter table LotTraitValue 
    add index FKA4BC4614D11BBA (entity_id), 
    add constraint FKA4BC4614D11BBA 
    foreign key (entity_id) 
    references Lot (id);

alter table LotTraitValue 
    add index FKA4BC4614B1CB66F7 (trial_id), 
    add constraint FKA4BC4614B1CB66F7 
    foreign key (trial_id) 
    references LotTrial (id);

alter table LotTraitValue 
    add index FKA4BC46148A71FF26 (trait_id), 
    add constraint FKA4BC46148A71FF26 
    foreign key (trait_id) 
    references Trait (id);

alter table LotTrial 
    add index FK91744A65F177734E (traitGroup_id), 
    add constraint FK91744A65F177734E 
    foreign key (traitGroup_id) 
    references TraitGroup (id);

alter table TraitCoding 
    add index FKDDFB46F88A71FF26 (trait_id), 
    add constraint FKDDFB46F88A71FF26 
    foreign key (trait_id) 
    references Trait (id);

alter table TraitGroupTraits 
    add index FK7F14DCB674A955AF (traits_id), 
    add constraint FK7F14DCB674A955AF 
    foreign key (traits_id) 
    references Trait (id);

alter table TraitGroupTraits 
    add index FK7F14DCB6F177734E (TraitGroup_id), 
    add constraint FK7F14DCB6F177734E 
    foreign key (TraitGroup_id) 
    references TraitGroup (id);

ALTER TABLE `inventory`.`LotTraitLastValue` ADD UNIQUE INDEX `FK_TRAITLASTVALUE` (`entity_id`, `trait_id`);
ALTER TABLE `inventory`.`LotTraitValue` ADD UNIQUE INDEX `FK_TRAITVALUE` (`entity_id`, `trial_id`, `trait_id`);

INSERT INTO Trait (createdBy, createdDate, lastUpdated, lastUpdatedBy, version, description, title, uom, var) 
	VALUES ('MObreza', now(), now(), 'MObreza', 1, 'Seed lot germination taken 6 days after sowing', 'Germination 6d', '%', 'GERM6');
INSERT INTO Trait (createdBy, createdDate, lastUpdated, lastUpdatedBy, version, description, title, uom, var) 
	VALUES ('MObreza', now(), now(), 'MObreza', 1, 'Seed lot viability', 'Viability', '%', 'VIABILITY');
	
INSERT INTO TraitGroup (createdBy, createdDate, lastUpdated, lastUpdatedBy, version, description, shortName, title) 
	VALUES ('MObreza', now(), now(), 'MObreza', 1, 'Seed bank viability testing', 'seed-viability', 'Viability test');
INSERT INTO TraitGroupTraits (TraitGroup_id, traits_id, position) VALUES (1,1,0);
INSERT INTO TraitGroupTraits (TraitGroup_id, traits_id, position) VALUES (1,2,1);


DROP TRIGGER IF EXISTS T_LotTraitValue_AU;
DROP TRIGGER IF EXISTS T_LotTraitValue_AI;
DROP TRIGGER IF EXISTS T_LotTraitValue_AD;
DELIMITER $$


CREATE DEFINER='root'@'localhost' TRIGGER T_LotTraitValue_AU AFTER UPDATE ON LotTraitValue FOR EACH ROW
BEGIN
    DECLARE lastDate DATETIME;
    SELECT LotTraitLastValue.`date` INTO lastDate FROM LotTraitLastValue WHERE entity_id=NEW.entity_id and trait_id=NEW.trait_id;
    IF lastDate IS NULL THEN
        INSERT INTO LotTraitLastValue 
            (createdBy, createdDate, lastUpdated, lastUpdatedBy, `value`, entity_id, trait_id, trial_id, `date`)
            VALUES (NEW.createdBy, NEW.createdDate, NEW.lastUpdated, NEW.lastUpdatedBy, NEW.`value`, NEW.entity_id, NEW.trait_id, NEW.trial_id, NEW.`date`);
    ELSEIF lastDate <= NEW.`date` THEN
        INSERT INTO LotTraitLastValue 
            (createdBy, createdDate, lastUpdated, lastUpdatedBy, `value`, entity_id, trait_id, trial_id, `date`)
            VALUES (NEW.createdBy, NEW.createdDate, NEW.lastUpdated, NEW.lastUpdatedBy, NEW.`value`, NEW.entity_id, NEW.trait_id, NEW.trial_id, NEW.`date`)
        ON DUPLICATE KEY UPDATE 
            createdBy=NEW.createdBy, createdDate=NEW.createdDate, lastUpdated=NEW.lastUpdated,
            lastUpdatedBy=NEW.lastUpdatedBy, `value`=NEW.`value`, entity_id=NEW.entity_id, 
            trait_id=NEW.trait_id, trial_id=NEW.trial_id, `date`=NEW.`date`;
    END IF;
END$$


CREATE DEFINER='root'@'localhost' TRIGGER T_LotTraitValue_AI AFTER INSERT ON LotTraitValue FOR EACH ROW
BEGIN
    DECLARE lastDate DATETIME;
    SELECT LotTraitLastValue.`date` INTO lastDate FROM LotTraitLastValue WHERE entity_id=NEW.entity_id and trait_id=NEW.trait_id;
    IF lastDate IS NULL THEN
        INSERT INTO LotTraitLastValue 
            (createdBy, createdDate, lastUpdated, lastUpdatedBy, `value`, entity_id, trait_id, trial_id, `date`)
            VALUES (NEW.createdBy, NEW.createdDate, NEW.lastUpdated, NEW.lastUpdatedBy, NEW.`value`, NEW.entity_id, NEW.trait_id, NEW.trial_id, NEW.`date`)
        ON DUPLICATE KEY UPDATE 
            createdBy=NEW.createdBy, createdDate=NEW.createdDate, lastUpdated=NEW.lastUpdated,
            lastUpdatedBy=NEW.lastUpdatedBy, `value`=NEW.`value`, entity_id=NEW.entity_id, 
            trait_id=NEW.trait_id, trial_id=NEW.trial_id, `date`=NEW.`date`;    
    ELSEIF lastDate <= NEW.`date` THEN
        INSERT INTO LotTraitLastValue 
            (createdBy, createdDate, lastUpdated, lastUpdatedBy, `value`, entity_id, trait_id, trial_id, `date`)
            VALUES (NEW.createdBy, NEW.createdDate, NEW.lastUpdated, NEW.lastUpdatedBy, NEW.`value`, NEW.entity_id, NEW.trait_id, NEW.trial_id, NEW.`date`)
        ON DUPLICATE KEY UPDATE 
            createdBy=NEW.createdBy, createdDate=NEW.createdDate, lastUpdated=NEW.lastUpdated,
            lastUpdatedBy=NEW.lastUpdatedBy, `value`=NEW.`value`, entity_id=NEW.entity_id, 
            trait_id=NEW.trait_id, trial_id=NEW.trial_id, `date`=NEW.`date`;        
    END IF;
END$$


CREATE DEFINER='root'@'localhost' TRIGGER T_LotTraitValue_AD AFTER DELETE ON LotTraitValue FOR EACH ROW
BEGIN
    INSERT INTO LotTraitLastValue 
        (createdBy, createdDate, lastUpdated, lastUpdatedBy, `value`, entity_id, trait_id, trial_id, `date`)
        SELECT createdBy, createdDate, lastUpdated, lastUpdatedBy, `value`, entity_id, trait_id, trial_id, `date` FROM LotTraitValue XX
            WHERE entity_id=OLD.entity_id and trait_id=OLD.trait_id ORDER BY `date` desc LIMIT 0, 1
    ON DUPLICATE KEY UPDATE 
            createdBy=XX.createdBy, createdDate=XX.createdDate, lastUpdated=XX.lastUpdated,
            lastUpdatedBy=XX.lastUpdatedBy, `value`=XX.`value`, entity_id=XX.entity_id, 
            trait_id=XX.trait_id, trial_id=XX.trial_id, `date`=XX.`date`;
    -- need to delete hanging values
    DELETE FROM LotTraitLastValue WHERE entity_id=OLD.entity_id and trait_id=OLD.trait_id and trial_id=OLD.trial_id;
END$$


DELIMITER ;

--
-- MIGRATE VIABILITY

DROP PROCEDURE IF EXISTS migrateViability;

DELIMITER $$

CREATE PROCEDURE migrateViability()
BEGIN


DECLARE done INT DEFAULT 0;
DECLARE new_id, TRAITGROUP_ID, TRAIT_ID BIGINT DEFAULT NULL;
DECLARE trialDate DATETIME DEFAULT NULL;
DECLARE cur1 CURSOR FOR SELECT distinct V.testDate from Viability V ORDER BY V.testDate;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

SELECT id into TRAITGROUP_ID FROM TraitGroup WHERE shortName='seed-viability';
IF TRAITGROUP_ID IS NULL THEN
	INSERT INTO TraitGroup (createdBy, createdDate, lastUpdated, lastUpdatedBy, version, description, shortName, title) 
		VALUES ('MObreza', now(), now(), 'MObreza', 1, 'Seed bank viability testing', 'seed-viability', 'Viability test');
	SELECT LAST_INSERT_ID() INTO TRAITGROUP_ID;
END IF;

SELECT id into TRAIT_ID FROM Trait WHERE var='VIABILITY';
IF TRAIT_ID IS NULL THEN
	INSERT INTO Trait (createdBy, createdDate, lastUpdated, lastUpdatedBy, version, description, title, uom, var) 
		VALUES ('MObreza', now(), now(), 'MObreza', 1, 'Seed lot viability', 'Viability', '%', 'VIABILITY');
	SELECT LAST_INSERT_ID() INTO TRAIT_ID;
END IF;


OPEN cur1;

REPEAT
	FETCH cur1 INTO trialDate;
	IF NOT done THEN
		INSERT INTO LotTrial (createdBy, createdDate, lastUpdated, lastUpdatedBy, version, date, description, name, traitGroup_id, status)
			values ('MIGRATION', now(), now(), 'MIGRATION', 1, trialDate, 'Migrated viability results', concat('Viability test ', date_format(trialDate, '%e/%c/%Y')), TRAITGROUP_ID, 'CLOSED');
		SELECT LAST_INSERT_ID() INTO new_id;
		INSERT INTO LotTraitValue (createdBy, createdDate, lastUpdated, lastUpdatedBy, date, value, trait_id, trial_id, entity_id)
			SELECT createdBy, createdDate, lastUpdated, lastUpdatedBy, testDate, viability, TRAIT_ID, new_id, lot_id from Viability where testDate=trialDate; 
	END IF;
UNTIL done END REPEAT;

CLOSE cur1;


END$$

DELIMITER ;

call migrateViability();

DROP PROCEDURE IF EXISTS migrateViability;

--
-- Change transactionType from ORDINAL
ALTER TABLE `inventory`.`QuantityUpdateBulk` MODIFY COLUMN `transactionType` VARCHAR(10)  NOT NULL DEFAULT 'OUT';
UPDATE QuantityUpdateBulk set transactionType='OUT' where transactionType ='0';
UPDATE QuantityUpdateBulk set transactionType='IN' where transactionType ='1';
ALTER TABLE `inventory`.`QuantityUpdateBulk` ADD INDEX `IDX_QUB_TRNTYPE`(`id`, `transactionType`);
UPDATE QuantityUpdateBulk SET transactionSubtype='REGENERATION' where transactionType='OUT' and (title like '%regene%' or description like '%regene%')

 --
 -- Change transaction source from ORDINAL
ALTER TABLE `inventory`.`Transaction2` MODIFY COLUMN `source` VARCHAR(10)  NOT NULL DEFAULT 'SYSTEM';
UPDATE Transaction2 set source='SYSTEM' where source ='0';
UPDATE Transaction2 set source='BULK' where source ='1';


--
--
-- Remove *Item, keeping only Item
alter table FieldItem drop foreign key FKC2D6A30D71B11CFC;
drop table FieldItem;
alter table InVitroItem drop foreign key FK9838B68CE4510817;
drop table InVitroItem;
alter table SeedItem drop foreign key FK3ED48144E4510817;
drop table SeedItem;

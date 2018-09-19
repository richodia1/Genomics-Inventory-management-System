-- Viability testing
CREATE TABLE  `inventory`.`Viability` (
  `id` bigint(20) NOT NULL auto_increment,
  `createdBy` varchar(255) default NULL,
  `createdDate` datetime default NULL,
  `lastUpdated` datetime default NULL,
  `lastUpdatedBy` varchar(255) default NULL,
  `testDate` datetime NOT NULL,
  `viability` double NOT NULL,
  `lot_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FK22CA66F7EB92FD8C` (`lot_id`),
  CONSTRAINT `FK22CA66F7EB92FD8C` FOREIGN KEY (`lot_id`) REFERENCES `Lot` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8;

-- Lot becomes Auditable
ALTER TABLE `inventory`.`Lot` 
ADD COLUMN  `createdBy` varchar(255) default NULL,
ADD COLUMN  `createdDate` datetime default NULL,
ADD COLUMN `lastUpdated` datetime default NULL,
ADD COLUMN `lastUpdatedBy` varchar(255) default NULL;

-- LotVariable Lot alteration 10/08/2011 By KEN --
ALTER TABLE `inventory`.`Lot` ADD COLUMN  `lotVariable` bigint default NULL;
alter table Lot add index FK12B31BC53952C (lotVariable), add constraint FK12B31BC53952C foreign key (lotVariable) references LotVariable (id);
alter table Lot 
        add index FK12B311E4F9856 (parent1_id), 
        add constraint FK12B311E4F9856 
        foreign key (parent1_id) 
        references Lot (id);

    alter table Lot 
        add index FK12B31AD8BBD38 (location), 
        add constraint FK12B31AD8BBD38 
        foreign key (location) 
        references Location (id);

    alter table Lot 
        add index FK12B31329E66CE (container), 
        add constraint FK12B31329E66CE 
        foreign key (container) 
        references ContainerType (id);

    alter table Lot 
        add index FK12B3171E28AF4 (item), 
        add constraint FK12B3171E28AF4 
        foreign key (item) 
        references Item (id);

alter table LotVariable 
        add index FK81EA0CD433D8BE5 (variable), 
        add constraint FK81EA0CD433D8BE5 
        foreign key (variable) 
        references Variables (id);

    alter table LotVariable 
        add index FK81EA0CD2CF6E014 (lot), 
        add constraint FK81EA0CD2CF6E014 
        foreign key (lot) 
        references Lot (id);

UPDATE `inventory`.`Lot` set createdBy='SYSTEM', lastUpdatedBy='SYSTEM', createdDate=dateLastModified, lastUpdated=dateLastModified;

ALTER TABLE `inventory`.`Lot` MODIFY COLUMN `createdBy` VARCHAR(255)  CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
 MODIFY COLUMN `createdDate` DATETIME  NOT NULL,
 MODIFY COLUMN `lastUpdated` DATETIME  NOT NULL,
 MODIFY COLUMN `lastUpdatedBy` VARCHAR(255)  CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL;

ALTER TABLE `inventory`.`Lot` DROP COLUMN `dateLastModified`;


-- QuantityUpdateBulk becomes Auditable
ALTER TABLE `inventory`.`QuantityUpdateBulk` 
ADD COLUMN  `createdBy` varchar(255) default NULL,
ADD COLUMN  `createdDate` datetime default NULL,
ADD COLUMN `lastUpdated` datetime default NULL,
ADD COLUMN `lastUpdatedBy` varchar(255) default NULL;

UPDATE `inventory`.`QuantityUpdateBulk` set createdBy='SYSTEM', lastUpdatedBy='SYSTEM', createdDate=`date`, lastUpdated=`date`;

ALTER TABLE `inventory`.`QuantityUpdateBulk` MODIFY COLUMN `createdBy` VARCHAR(255)  CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
 MODIFY COLUMN `createdDate` DATETIME  NOT NULL,
 MODIFY COLUMN `lastUpdated` DATETIME  NOT NULL,
 MODIFY COLUMN `lastUpdatedBy` VARCHAR(255)  CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL;

-- Migration becomes Auditable 
ALTER TABLE `inventory`.`Migration` 
ADD COLUMN  `createdBy` varchar(255) default NULL,
ADD COLUMN  `createdDate` datetime default NULL,
ADD COLUMN `lastUpdated` datetime default NULL,
ADD COLUMN `lastUpdatedBy` varchar(255) default NULL;

UPDATE `inventory`.`Migration` set createdBy='SYSTEM', lastUpdatedBy='SYSTEM', createdDate=`migrationDate`, lastUpdated=`migrationDate`;

ALTER TABLE `inventory`.`Migration` MODIFY COLUMN `createdBy` VARCHAR(255)  CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
 MODIFY COLUMN `createdDate` DATETIME  NOT NULL,
 MODIFY COLUMN `lastUpdated` DATETIME  NOT NULL,
 MODIFY COLUMN `lastUpdatedBy` VARCHAR(255)  CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL;

ALTER TABLE `inventory`.`Transaction2` 
ADD COLUMN  `createdBy` varchar(255) not null default 'SYSTEM',
ADD COLUMN  `createdDate` datetime default NULL,
ADD COLUMN `lastUpdated` datetime default NULL,
ADD COLUMN `lastUpdatedBy` varchar(255) not null default 'SYSTEM';

create table Variables (
        id bigint not null auto_increment,
        createdBy varchar(255),
        createdDate datetime,
        lastUpdated datetime,
        lastUpdatedBy varchar(255),
        name varchar(255),
        primary key (id)
    ) type=InnoDB;
create table LotVariable (
        id bigint not null auto_increment,
        createdBy varchar(255),
        createdDate datetime,
        lastUpdated datetime,
        lastUpdatedBy varchar(255),
        version integer not null,
        quantity double precision,
        variabledate datetime,
        lot bigint not null,
        variable bigint not null,
        primary key (id)
    ) type=InnoDB;
create table LotVariableUpdate (
        id bigint not null auto_increment,
        originalQty double precision,
        quantity double precision not null,
        variableDate datetime,
        description_id bigint not null,
        lotId bigint not null,
        lotVariableId bigint not null,
        variableId bigint not null,
        primary key (id)
    ) type=InnoDB;

    create table LotVariableUpdateBulk (
        id bigint not null auto_increment,
        createdBy varchar(255),
        createdDate datetime,
        lastUpdated datetime,
        lastUpdatedBy varchar(255),
        version integer not null,
        affectingInventory BIT DEFAULT 1,
        date datetime not null,
        description longtext,
        status integer not null,
        title varchar(250) not null,
        transactionSubtype varchar(255),
        transactionType varchar(255),
        primary key (id)
    ) type=InnoDB;
create table Transaction3 (
        id bigint not null auto_increment,
        createdBy varchar(255),
        createdDate datetime,
        lastUpdated datetime,
        lastUpdatedBy varchar(255),
        date datetime,
        quantity double precision not null,
        rel bigint,
        source varchar(255),
        subtype varchar(50) not null,
        lotVariable bigint not null,
        primary key (id)
    ) type=InnoDB;
alter table LotVariable 
        add index FK81EA0CD433D8BE5 (variable), 
        add constraint FK81EA0CD433D8BE5 
        foreign key (variable) 
        references Variables (id);
    alter table LotVariable 
        add index FK81EA0CD2CF6E014 (lot), 
        add constraint FK81EA0CD2CF6E014 
        foreign key (lot) 
        references Lot (id);

    alter table LotVariableUpdate 
        add index FKD26937F6F001A307 (lotVariableId), 
        add constraint FKD26937F6F001A307 
        foreign key (lotVariableId) 
        references LotVariable (id);

    alter table LotVariableUpdate 
        add index FKD26937F6F552EA00 (variableId), 
        add constraint FKD26937F6F552EA00 
        foreign key (variableId) 
        references Variables (id);

    alter table LotVariableUpdate 
        add index FKD26937F698E93F78 (description_id), 
        add constraint FKD26937F698E93F78 
        foreign key (description_id) 
        references LotVariableUpdateBulk (id);

    alter table LotVariableUpdate 
        add index FKD26937F6331B5CEF (lotId), 
        add constraint FKD26937F6331B5CEF 
        foreign key (lotId) 
        references Lot (id);

alter table Transaction3 
        add index FK7E44DD35BC53952C (lotVariable), 
        add constraint FK7E44DD35BC53952C 
        foreign key (lotVariable) 
        references LotVariable (id);

ALTER TABLE `Transaction3
 DROP FOREIGN KEY `FK7E44DD35BC53952C`;

ALTER TABLE Transaction3 ADD CONSTRAINT `FK7E44DD35BC53952C` FOREIGN KEY `FK7E44DD35BC53952C` (`lotVariable`)
    REFERENCES `LotVariable` (`id`)
    ON DELETE CASCADE
    ON UPDATE RESTRICT;

create table FieldVariables (
        id bigint not null auto_increment,
        createdBy varchar(255),
        createdDate datetime,
        lastUpdated datetime,
        lastUpdatedBy varchar(255),
        version integer not null,
        date datetime,
        qty double precision,
        var varchar(255),
        lot_id bigint not null,
        primary key (id)
    ) type=InnoDB;

alter table FieldVariables 
        add index FK4F2C41BDEB92FD8C (lot_id), 
        add constraint FK4F2C41BDEB92FD8C 
        foreign key (lot_id) 
        references Lot (id);

create table FieldVariablesList (
        id bigint not null auto_increment,
        createdBy varchar(255),
        createdDate datetime,
        lastUpdated datetime,
        lastUpdatedBy varchar(255),
        version integer not null,
        var varchar(255),
        primary key (id)
    ) type=InnoDB;

INSERT INTO FieldVariablesList(createdBy, createdDate, version, var) VALUES('KOraegbunam','2011-09-26 11:09:00',0,'Sprouted');
INSERT INTO FieldVariablesList(createdBy, createdDate, version, var) VALUES('KOraegbunam','2011-09-26 11:09:00',0,'Flower');
INSERT INTO FieldVariablesList(createdBy, createdDate, version, var) VALUES('KOraegbunam','2011-09-26 11:09:00',0,'Bulbils');
INSERT INTO FieldVariablesList(createdBy, createdDate, version, var) VALUES('KOraegbunam','2011-09-26 11:09:00',0,'Weight');
INSERT INTO FieldVariablesList(createdBy, createdDate, version, var) VALUES('KOraegbunam','2011-09-26 11:09:00',0,'Harvested');

-- 02/02/2012 Updates --
create table LotSubtypeUpdate (
        id bigint not null auto_increment,
        date datetime,
        originalQty double precision,
        quantity double precision not null,
        description_id bigint not null,
        lotId bigint not null,
        primary key (id)
    ) type=InnoDB;

    create table LotSubtypeUpdateBulk (
        id bigint not null auto_increment,
        createdBy varchar(255),
        createdDate datetime,
        lastUpdated datetime,
        lastUpdatedBy varchar(255),
        version integer not null,
        affectingInventory BIT DEFAULT 1,
        date datetime not null,
        description longtext,
        status integer not null,
        title varchar(250) not null,
        transactionSubtype varchar(255),
        transactionType varchar(255),
        primary key (id)
    ) type=InnoDB;

alter table LotSubtypeUpdate 
        add index FKCC864B32FCB7C850 (description_id), 
        add constraint FKCC864B32FCB7C850 
        foreign key (description_id) 
        references LotSubtypeUpdateBulk (id);

    alter table LotSubtypeUpdate 
        add index FKCC864B32331B5CEF (lotId), 
        add constraint FKCC864B32331B5CEF 
        foreign key (lotId) 
        references Lot (id);
	
	drop table if exists MigrationLotLocationUpdate;
    drop table if exists MigrationLotLocationUpdateBulk;

	create table MigrationLotLocationUpdate (
        id bigint not null auto_increment,
        fromLocation bigint,
        toLocation varchar(255),
        toLocationId bigint,
        description_id bigint not null,
        lotId bigint not null,
        primary key (id)
    ) type=InnoDB;

    create table MigrationLotLocationUpdateBulk (
        id bigint not null auto_increment,
        createdBy varchar(255),
        createdDate datetime,
        lastUpdated datetime,
        lastUpdatedBy varchar(255),
        version integer not null,
        affectingInventory BIT DEFAULT 1,
        date datetime not null,
        status integer not null,
        primary key (id)
    ) type=InnoDB;

	alter table MigrationLotLocationUpdate 
        add index FKBB3934818A3F09F (description_id), 
        add constraint FKBB3934818A3F09F 
        foreign key (description_id) 
        references MigrationLotLocationUpdateBulk (id);

    alter table MigrationLotLocationUpdate 
        add index FKBB393481331B5CEF (lotId), 
        add constraint FKBB393481331B5CEF 
        foreign key (lotId) 
        references Lot (id);




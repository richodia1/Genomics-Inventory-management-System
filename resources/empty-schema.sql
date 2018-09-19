alter table BarCode 
    drop 
    foreign key FK4F47F480331B5CEF;

alter table InVitroItem 
    drop 
    foreign key FK9838B68C71B11CFC;

alter table InVitroLot 
    drop 
    foreign key FKC2D894B82CF549DE;

alter table Item 
    drop 
    foreign key FK22EF33575A8388;

alter table Location 
    drop 
    foreign key FK752A03D58247B0C8;

alter table Lot 
    drop 
    foreign key FK12B311E4F9856;

alter table Lot 
    drop 
    foreign key FK12B31AD8BBD38;

alter table Lot 
    drop 
    foreign key FK12B31329E66CE;

alter table Lot 
    drop 
    foreign key FK12B3171E28AF4;

alter table MRIAction 
    drop 
    foreign key FK68F1129A332AA855;

alter table MRIAction 
    drop 
    foreign key FK68F1129AEB92FD8C;

alter table MRIAction 
    drop 
    foreign key FK68F1129AB00EE8C3;

alter table MRIRequest 
    drop 
    foreign key FK3BBFEAABEFD3C9A8;

alter table MRIRequest 
    drop 
    foreign key FK3BBFEAAB5651D4C0;

alter table Migration 
    drop 
    foreign key FK44A853AEEB92FD8C;

alter table PrinterInfo 
    drop 
    foreign key FK6E14ABC891BF09EA;

alter table QuantityUpdate 
    drop 
    foreign key FK893B8154CE7670F2;

alter table QuantityUpdate 
    drop 
    foreign key FK893B8154331B5CEF;

alter table SeedItem 
    drop 
    foreign key FK3ED4814471B11CFC;

alter table SeedLot 
    drop 
    foreign key FKD8BC93002CF549DE;

alter table SystemPrinterInfo 
    drop 
    foreign key FK497E961984B39C53;

alter table Transaction2 
    drop 
    foreign key FK7E44DD342CF6E014;

alter table UserDelegation 
    drop 
    foreign key FK44143F73707D4D21;

alter table UserDelegation 
    drop 
    foreign key FK44143F7329E06DF5;

alter table UserLookup 
    drop 
    foreign key FKC97E194536E9810D;

alter table UserPasswordRequest 
    drop 
    foreign key FK8175E0095FF93CF6;

alter table UserRole 
    drop 
    foreign key FKF3F7670136E9810D;

alter table UserSupervisor 
    drop 
    foreign key FK8C15E5B3A74D756A;

alter table UserSupervisor 
    drop 
    foreign key FK8C15E5B336E9810D;

alter table Viability 
    drop 
    foreign key FK22CA66F7EB92FD8C;

drop table if exists BarCode;

drop table if exists ContainerType;

drop table if exists InVitroItem;

drop table if exists InVitroLot;

drop table if exists Item;

drop table if exists ItemType;

drop table if exists LabelInfo;

drop table if exists Location;

drop table if exists LocationFlat;

drop table if exists Lot;

drop table if exists MRI;

drop table if exists MRIAction;

drop table if exists MRIRequest;

drop table if exists Migration;

drop table if exists PrinterInfo;

drop table if exists QuantityUpdate;

drop table if exists QuantityUpdateBulk;

drop table if exists SeedItem;

drop table if exists SeedLot;

drop table if exists SystemPrinterInfo;

drop table if exists Transaction2;

drop table if exists User;

drop table if exists UserDelegation;

drop table if exists UserLookup;

drop table if exists UserPasswordRequest;

drop table if exists UserRole;

drop table if exists UserSupervisor;

drop table if exists Viability;

create table BarCode (
    id bigint not null auto_increment,
    dateAssigned datetime,
    lotId bigint unique,
    primary key (id)
) type=InnoDB;

create table ContainerType (
    id bigint not null auto_increment,
    name varchar(100) not null unique,
    version integer,
    primary key (id)
) type=InnoDB;

create table InVitroItem (
    id bigint not null,
    primary key (id)
) type=InnoDB;

create table InVitroLot (
    introductionDate datetime,
    lineNumber integer,
    germplasmOrigin varchar(20),
    regenerationDate datetime,
    virusFree bit,
    id bigint not null,
    primary key (id)
) type=InnoDB;

create table Item (
    id bigint not null auto_increment,
    accessionIdentifier bigint,
    alternativeIdentifier varchar(255),
    dateLastModified datetime not null,
    name varchar(100) not null,
    notes longtext,
    prefix varchar(10),
    version integer,
    itemType bigint not null,
    primary key (id),
    unique (itemType, name)
) type=InnoDB;

create table ItemType (
    id bigint not null auto_increment,
    name varchar(100) not null unique,
    shortName varchar(20) not null unique,
    version integer,
    primary key (id)
) type=InnoDB;

create table LabelInfo (
    id integer not null auto_increment,
    columns integer not null,
    labelHeight integer not null,
    labelWidth integer not null,
    marginHorizontal integer not null,
    marginVertical integer not null,
    name varchar(200) not null,
    shortName varchar(20),
    version integer,
    primary key (id)
) type=InnoDB;

create table Location (
    id bigint not null auto_increment,
    locationType varchar(25),
    name varchar(100) not null,
    version integer,
    parentId bigint,
    primary key (id)
) type=InnoDB;

create table Lot (
    id bigint not null auto_increment,
    createdBy varchar(255),
    createdDate datetime,
    lastUpdated datetime,
    lastUpdatedBy varchar(255),
    version integer not null,
    notes longtext,
    parent1type varchar(255),
    quantity double precision not null,
    scale varchar(10) not null,
    status integer not null,
    container bigint,
    item bigint not null,
    location bigint,
    parent1_id bigint,
    primary key (id)
) type=InnoDB;

create table MRI (
    id bigint not null auto_increment,
    createdBy varchar(255),
    createdDate datetime,
    lastUpdated datetime,
    lastUpdatedBy varchar(255),
    approvedBy varchar(255),
    date datetime,
    deliveredTo varchar(255),
    issuedBy varchar(255),
    orderedBy varchar(255),
    storeName varchar(255),
    primary key (id)
) type=InnoDB;

create table MRIAction (
    id bigint not null auto_increment,
    createdBy varchar(255),
    createdDate datetime,
    lastUpdated datetime,
    lastUpdatedBy varchar(255),
    quantityIssued integer not null,
    lot_id bigint,
    requestId bigint,
    mriId bigint,
    primary key (id)
) type=InnoDB;

create table MRIRequest (
    id bigint not null auto_increment,
    createdBy varchar(255),
    createdDate datetime,
    lastUpdated datetime,
    lastUpdatedBy varchar(255),
    quantity double precision not null,
    unit varchar(20) not null,
    item_id bigint,
    requestId bigint,
    primary key (id)
) type=InnoDB;

create table Migration (
    id bigint not null auto_increment,
    createdBy varchar(255),
    createdDate datetime,
    lastUpdated datetime,
    lastUpdatedBy varchar(255),
    migrationDate datetime not null,
    newLocationId bigint not null,
    newLocationName varchar(255) not null,
    oldLocationId bigint,
    oldLocationName varchar(255),
    reason longtext,
    lot_id bigint not null,
    primary key (id)
) type=InnoDB;

create table PrinterInfo (
    id integer not null auto_increment,
    allowedIPaddresses longtext,
    location varchar(200) not null,
    name varchar(200) not null,
    version integer,
    labelInfo_id integer,
    primary key (id)
) type=InnoDB;

create table QuantityUpdate (
    id bigint not null auto_increment,
    quantity double precision not null,
    scale varchar(255) not null,
    description_id bigint not null,
    lotId bigint not null,
    primary key (id)
) type=InnoDB;

create table QuantityUpdateBulk (
    id bigint not null auto_increment,
    createdBy varchar(255),
    createdDate datetime,
    lastUpdated datetime,
    lastUpdatedBy varchar(255),
    version integer not null,
    date datetime not null,
    description longtext,
    status integer not null,
    title varchar(250) not null,
    transactionSubtype varchar(255),
    transactionType integer,
    primary key (id)
) type=InnoDB;

create table SeedItem (
    id bigint not null,
    primary key (id)
) type=InnoDB;

create table SeedLot (
    fieldLocation varchar(255),
    germination double precision,
    germinationDate datetime,
    harvestDate datetime,
    moistureContent double precision,
    plantingDate datetime,
    pqsCertified bit,
    seedTested bit,
    storageType varchar(255),
    virusFree bit,
    weight double precision,
    weight100 double precision,
    yearProcessed bigint,
    id bigint not null,
    primary key (id)
) type=InnoDB;

create table SystemPrinterInfo (
    printServiceName varchar(200) not null,
    id integer not null,
    primary key (id)
) type=InnoDB;

create table Transaction2 (
    id bigint not null auto_increment,
    createdBy varchar(255),
    createdDate datetime,
    lastUpdated datetime,
    lastUpdatedBy varchar(255),
    date datetime,
    quantity double precision not null,
    rel bigint,
    scale varchar(20) not null,
    source integer,
    subtype varchar(50) not null,
    lot bigint not null,
    primary key (id)
) type=InnoDB;

create table User (
    id bigint not null auto_increment,
    authenticationType integer,
    department varchar(255),
    description varchar(255),
    displayName varchar(255),
    firstName varchar(255),
    lastLogin datetime,
    lastLoginFailed datetime,
    lastName varchar(255),
    mail varchar(255),
    password varchar(255),
    preferences varchar(255),
    staffId varchar(255),
    status integer,
    username varchar(255),
    primary key (id)
) type=InnoDB;

create table UserDelegation (
    id bigint not null auto_increment,
    application varchar(50) not null,
    delegatedTo bigint not null,
    ownerId bigint not null,
    primary key (id)
) type=InnoDB;

create table UserLookup (
    ID bigint not null auto_increment,
    identifier varchar(200) not null,
    userid bigint not null,
    primary key (ID)
) type=InnoDB;

create table UserPasswordRequest (
    id bigint not null auto_increment,
    dateGenerated datetime,
    dateUsed datetime,
    `key` varchar(255),
    status integer not null,
    user_id bigint,
    primary key (id)
) type=InnoDB;

create table UserRole (
    ID bigint not null auto_increment,
    application varchar(50) not null,
    role varchar(50) not null,
    userId bigint not null,
    primary key (ID)
) type=InnoDB;

create table UserSupervisor (
    id bigint not null auto_increment,
    application varchar(100),
    supervisorId bigint,
    userId bigint,
    primary key (id)
) type=InnoDB;

create table Viability (
    id bigint not null auto_increment,
    createdBy varchar(255),
    createdDate datetime,
    lastUpdated datetime,
    lastUpdatedBy varchar(255),
    testDate datetime not null,
    viability double precision not null,
    lot_id bigint not null,
    primary key (id)
) type=InnoDB;

alter table BarCode 
    add index FK4F47F480331B5CEF (lotId), 
    add constraint FK4F47F480331B5CEF 
    foreign key (lotId) 
    references Lot (id);

alter table InVitroItem 
    add index FK9838B68C71B11CFC (id), 
    add constraint FK9838B68C71B11CFC 
    foreign key (id) 
    references Item (id);

alter table InVitroLot 
    add index FKC2D894B82CF549DE (id), 
    add constraint FKC2D894B82CF549DE 
    foreign key (id) 
    references Lot (id);

alter table Item 
    add index FK22EF33575A8388 (itemType), 
    add constraint FK22EF33575A8388 
    foreign key (itemType) 
    references ItemType (id);

alter table Location 
    add index FK752A03D58247B0C8 (parentId), 
    add constraint FK752A03D58247B0C8 
    foreign key (parentId) 
    references Location (id);

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

alter table MRIAction 
    add index FK68F1129A332AA855 (mriId), 
    add constraint FK68F1129A332AA855 
    foreign key (mriId) 
    references MRI (id);

alter table MRIAction 
    add index FK68F1129AEB92FD8C (lot_id), 
    add constraint FK68F1129AEB92FD8C 
    foreign key (lot_id) 
    references Lot (id);

alter table MRIAction 
    add index FK68F1129AB00EE8C3 (requestId), 
    add constraint FK68F1129AB00EE8C3 
    foreign key (requestId) 
    references MRIRequest (id);

alter table MRIRequest 
    add index FK3BBFEAABEFD3C9A8 (item_id), 
    add constraint FK3BBFEAABEFD3C9A8 
    foreign key (item_id) 
    references Item (id);

alter table MRIRequest 
    add index FK3BBFEAAB5651D4C0 (requestId), 
    add constraint FK3BBFEAAB5651D4C0 
    foreign key (requestId) 
    references MRI (id);

alter table Migration 
    add index FK44A853AEEB92FD8C (lot_id), 
    add constraint FK44A853AEEB92FD8C 
    foreign key (lot_id) 
    references Lot (id);

alter table PrinterInfo 
    add index FK6E14ABC891BF09EA (labelInfo_id), 
    add constraint FK6E14ABC891BF09EA 
    foreign key (labelInfo_id) 
    references LabelInfo (id);

alter table QuantityUpdate 
    add index FK893B8154CE7670F2 (description_id), 
    add constraint FK893B8154CE7670F2 
    foreign key (description_id) 
    references QuantityUpdateBulk (id);

alter table QuantityUpdate 
    add index FK893B8154331B5CEF (lotId), 
    add constraint FK893B8154331B5CEF 
    foreign key (lotId) 
    references Lot (id);

alter table SeedItem 
    add index FK3ED4814471B11CFC (id), 
    add constraint FK3ED4814471B11CFC 
    foreign key (id) 
    references Item (id);

alter table SeedLot 
    add index FKD8BC93002CF549DE (id), 
    add constraint FKD8BC93002CF549DE 
    foreign key (id) 
    references Lot (id);

alter table SystemPrinterInfo 
    add index FK497E961984B39C53 (id), 
    add constraint FK497E961984B39C53 
    foreign key (id) 
    references PrinterInfo (id);

alter table Transaction2 
    add index FK7E44DD342CF6E014 (lot), 
    add constraint FK7E44DD342CF6E014 
    foreign key (lot) 
    references Lot (id);

alter table UserDelegation 
    add index FK44143F73707D4D21 (delegatedTo), 
    add constraint FK44143F73707D4D21 
    foreign key (delegatedTo) 
    references User (id);

alter table UserDelegation 
    add index FK44143F7329E06DF5 (ownerId), 
    add constraint FK44143F7329E06DF5 
    foreign key (ownerId) 
    references User (id);

alter table UserLookup 
    add index FKC97E194536E9810D (userid), 
    add constraint FKC97E194536E9810D 
    foreign key (userid) 
    references User (id);

alter table UserPasswordRequest 
    add index FK8175E0095FF93CF6 (user_id), 
    add constraint FK8175E0095FF93CF6 
    foreign key (user_id) 
    references User (id);

alter table UserRole 
    add index FKF3F7670136E9810D (userId), 
    add constraint FKF3F7670136E9810D 
    foreign key (userId) 
    references User (id);

alter table UserSupervisor 
    add index FK8C15E5B3A74D756A (supervisorId), 
    add constraint FK8C15E5B3A74D756A 
    foreign key (supervisorId) 
    references User (id);

alter table UserSupervisor 
    add index FK8C15E5B336E9810D (userId), 
    add constraint FK8C15E5B336E9810D 
    foreign key (userId) 
    references User (id);

alter table Viability 
    add index FK22CA66F7EB92FD8C (lot_id), 
    add constraint FK22CA66F7EB92FD8C 
    foreign key (lot_id) 
    references Lot (id);



-- LocationFlat table contains the data of all relations between parent location and all its sub-sub-sub locations
-- Contents of this table are updated with triggers (see below)
CREATE TABLE `LocationFlat` (
  `parentId` bigint(20) default NULL,
  `id` bigint(20) NOT NULL,
  KEY `IX_LocationFlat_id` (`id`),
  KEY `IX_LocationFlat_parentId` (`parentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Add triggers for LocationFlat table
-- WARNING: need to use the DELIMITER command!
DELIMITER $$

CREATE TRIGGER T_Location_AU AFTER UPDATE ON Location FOR EACH ROW 
BEGIN 
  DECLARE parent BIGINT; 
  SET parent=NEW.parentId; 
  DELETE FROM LocationFlat WHERE id=NEW.id; 
  REPEAT 
    INSERT INTO LocationFlat (parentId, id) VALUES (parent, NEW.id); 
    SELECT parentId INTO parent FROM Location WHERE id=parent; 
    IF parent IS NULL and NEW.parentId IS NOT NULL THEN 
      INSERT INTO LocationFlat (parentId, id) VALUES (parent, NEW.id); 
    END IF; 
  UNTIL parent is null 
  END REPEAT; 
END$$

CREATE TRIGGER T_Location_AI AFTER INSERT ON Location FOR EACH ROW 
BEGIN 
  DECLARE parent BIGINT; 
  SET parent=NEW.parentId; 
  DELETE FROM LocationFlat WHERE id=NEW.id; 
  REPEAT 
    INSERT INTO LocationFlat (parentId, id) VALUES (parent, NEW.id); 
    SELECT parentId INTO parent FROM Location WHERE id=parent; 
    IF parent IS NULL and NEW.parentId IS NOT NULL THEN 
      INSERT INTO LocationFlat (parentId, id) VALUES (parent, NEW.id); 
    END IF; 
  UNTIL parent is null 
  END REPEAT; 
END$$

CREATE TRIGGER T_Location_AD AFTER DELETE ON Location FOR EACH ROW 
BEGIN 
  DELETE FROM LocationFlat WHERE id=OLD.id; 
END$$

DELIMITER ;

CREATE TABLE  `GenebankOrder` (
  `id` bigint(20) NOT NULL auto_increment,
  `createdBy` varchar(255) default NULL,
  `createdDate` datetime default NULL,
  `lastUpdated` datetime default NULL,
  `lastUpdatedBy` varchar(255) default NULL,
  `version` int(11) NOT NULL,
  `firstName` varchar(100) default NULL,
  `lastName` varchar(100) default NULL,
  `organization` varchar(100) default NULL,
  `shippingAddress` longtext,
  `state` varchar(255) default NULL,
  `title` longtext,
  `description` longtext,
  `purpose` varchar(200) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE  `GenebankOrderItem` (
  `id` bigint(20) NOT NULL auto_increment,
  `createdBy` varchar(255) default NULL,
  `createdDate` datetime default NULL,
  `lastUpdated` datetime default NULL,
  `lastUpdatedBy` varchar(255) default NULL,
  `version` int(11) NOT NULL,
  `quantity` double default NULL,
  `scale` varchar(20) default NULL,
  `item_id` bigint(20) NOT NULL,
  `lot_id` bigint(20) default NULL,
  `order_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FKC3F6CB90EB92FD8C` (`lot_id`),
  KEY `FKC3F6CB90EFD3C9A8` (`item_id`),
  KEY `FKC3F6CB90F15559B` (`order_id`),
  CONSTRAINT `FKC3F6CB90F15559B` FOREIGN KEY (`order_id`) REFERENCES `GenebankOrder` (`id`),
  CONSTRAINT `FKC3F6CB90EB92FD8C` FOREIGN KEY (`lot_id`) REFERENCES `Lot` (`id`),
  CONSTRAINT `FKC3F6CB90EFD3C9A8` FOREIGN KEY (`item_id`) REFERENCES `Item` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


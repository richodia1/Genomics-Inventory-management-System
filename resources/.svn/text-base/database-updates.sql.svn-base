-- Update necessary with Hibernate's hbm2dll=validate
ALTER TABLE `inventory`.`QuantityUpdate` MODIFY COLUMN `quantity` DOUBLE  NOT NULL;

-- LocationFlat table contains the data of all relations between parent location and all its sub-sub-sub locations
-- Contents of this table are updated with triggers (see below)
CREATE TABLE `LocationFlat` (
  `parentId` bigint(20) default NULL,
  `id` bigint(20) NOT NULL,
  KEY `IX_LocationFlat_id` (`id`),
  KEY `IX_LocationFlat_parentId` (`parentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

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



--AUTOMATIC, 
update QuantityUpdateBulk set transactionSubtype='SYSTEM', transactionType=0 where transactionType=0;
--REWEIGHED, 
--update QuantityUpdateBulk set transactionSubtype='REWEIGHED' where transactionType=1;
--NECROSIS, 
update QuantityUpdateBulk set transactionSubtype='NECROSIS', transactionType=0 where transactionType=2;
--CONTAMINATION, 
update QuantityUpdateBulk set transactionSubtype='CONTAMINATION', transactionType=0 where transactionType=3;
--DISTRIBUTION, 
update QuantityUpdateBulk set transactionSubtype='DISTRIBUTION', transactionType=0 where transactionType=4;
--LOSS, 
update QuantityUpdateBulk set transactionSubtype='LOSS', transactionType=0 where transactionType=5;
--REGENERATION, 
update QuantityUpdateBulk set transactionSubtype='REGENERATION', transactionType=1 where transactionType=6;
--INTERNALDISTRIBUTION, 
update QuantityUpdateBulk set transactionSubtype='INTERNALDISTRIBUTION', transactionType=0 where transactionType=7;
--SUBCULTURING, 
update QuantityUpdateBulk set transactionSubtype='SUBCULTURING', transactionType=0 where transactionType=9;
--SUBCULTURED
update QuantityUpdateBulk set transactionSubtype='SUBCULTURED', transactionType=1 where transactionType=10;


INSERT INTO Transaction2 (date, quantity, scale, source, subtype, lot) (SELECT transactionDate, -oldQuantity, oldScale, 0, 'SYSTEM', lot FROM `Transaction`)
INSERT INTO Transaction2 (date, quantity, scale, source, subtype, lot) (SELECT transactionDate, newQuantity, newScale, 0, 'SYSTEM', lot FROM `Transaction`)


--
-- 
-- Migrating introductionDate and germplasmOrigin from InVitroItem to InVitroLot 
--
-- 0) Run this query first and validate after data has been moved around
select I.name NAME, I.prefix PREFIX, I.accessionIdentifier ACCID, I.id ITEMID, SI.id SEEDITEM, II.id INVITROITEM, L.id LOT, L.quantity QUANTITY, L.scale SCALE, IL.introductionDate INTRODUCTION, IL.germplasmOrigin ORIGIN, SL.yearProcessed YEAR, SL.plantingDate PLANTINGDATE from Lot L left outer join InVitroLot IL on IL.id=L.id left outer join SeedLot SL on SL.id=L.id left outer join Item I on I.id=L.item left outer join InVitroItem II on II.id=I.id left outer join SeedItem SI on SI.id=I.id order by L.id;
-- 1) Add columns
ALTER TABLE `inventory`.`InVitroLot` ADD COLUMN `introductionDate` DATE  DEFAULT NULL AFTER `id`, ADD COLUMN `germplasmOrigin` VARCHAR(20)  DEFAULT NULL AFTER `introductionDate`;
-- 2) Copy data to InVitroLot from InVitroItem
update InVitroItem II inner join Item I on I.id=II.id left outer join Lot L on L.item=II.id inner join InVitroLot IL on IL.id=L.id set IL.germplasmOrigin=II.germplasmOrigin, IL.introductionDate=II.introductionDate;
-- Could drop those columns, leaving there for now
-- ALTER TABLE `inventory`.`InVitroItem` DROP COLUMN `introductionDate`, DROP COLUMN `germplasmOrigin`;

-- DELETE Items with no names and no lots
delete II from Item I inner join InVitroItem II on II.id=I.id left outer join Lot L on L.item=I.id where (I.name is null or length(I.name)=0) and L.id is null;
delete I from Item I inner join InVitroItem II on II.id=I.id left outer join Lot L on L.item=I.id where (I.name is null or length(I.name)=0) and L.id is null;
delete SI from Item I inner join SeedItem SI on SI.id=I.id left outer join Lot L on L.item=I.id where (I.name is null or length(I.name)=0) and L.id is null;
delete I from Item I inner join SeedItem SI on SI.id=I.id left outer join Lot L on L.item=I.id where (I.name is null or length(I.name)=0) and L.id is null;
-- Generate names where appropriate
update Item I set I.name=concat(I.prefix, '-', I.accessionIdentifier) where (I.name is null or length(I.name)=0) and I.prefix is not null and I.accessionIdentifier is not null;
-- Update lots to use the min item ID
update Item I inner join (select itemType, name, min(id) minid from Item group by ItemType, name having count(*)>1) X on X.itemType=I.itemType and X.name=I.name left outer join Lot L on L.item=I.id set L.item=X.minid;
-- DELETE Items with no lots
delete II from Item I inner join InVitroItem II on II.id=I.id left outer join Lot L on L.item=I.id where L.id is null;
delete SI from Item I inner join SeedItem SI on SI.id=I.id left outer join Lot L on L.item=I.id where L.id is null;
delete I from Item I left outer join Lot L on L.item=I.id where L.id is null;
-- ADD unique index
ALTER TABLE `inventory`.`Item` DROP INDEX `IX_Item_ItemTypeName`;
ALTER TABLE `inventory`.`Item` ADD UNIQUE INDEX `UIX_ITEM_ITEMTYPENAME`(`itemType`, `name`);

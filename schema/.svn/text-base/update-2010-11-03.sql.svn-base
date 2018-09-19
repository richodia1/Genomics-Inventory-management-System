-- Update database and delete all lot information under “Musa” location
create temporary table TT_Musa (select id from Lot where Lot.location in (select id from Location where id=20271 or id in (select id from LocationFlat where parentId=20271)));
select count(id) from Lot;
update BarCode set BarCode.lotId=null where BarCode.lotId in (select id from TT_Musa);
update Lot set status=-100 where id in (select id from TT_Musa);
delete from Transaction2 where lot in (select id from TT_Musa);
delete from Migration where lot_id in (select id from TT_Musa);
delete from QuantityUpdate where lotId in (select id from TT_Musa);
delete from LotTraitValue where entity_id in (select id from TT_Musa);
delete from LotTraitLastValue where entity_id in (select id from TT_Musa);
update Lot l set l.parent1_id=null where l.parent1_id in (select id from TT_Musa);
delete from Lot where id in (select id from TT_Musa);
select count(id) from Lot;
drop table TT_Musa;
-- Drop Musa location
update Location set Location.name='Musa **deleted**' where id=20271;
update Location set Location.name='Musa' where id=32951;
create temporary table TT_Loc (select id from LocationFlat where parentId=20271);
update Location set parentId=null where id in (select id from TT_Loc);
delete from Location where id in (select id from TT_Loc);
delete from Location where id=20271;
drop table TT_Loc;

-- set barcode.lotId to null when lot deleted
ALTER TABLE `inventory`.`BarCode` DROP FOREIGN KEY `FK4F47F4804F942274`;
ALTER TABLE `inventory`.`BarCode` ADD CONSTRAINT `FK4F47F4804F942274` FOREIGN KEY `FK4F47F4804F942274` (`lotId`) REFERENCES `Lot` (`id`) ON DELETE SET NULL    ON UPDATE RESTRICT;

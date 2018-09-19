ALTER TABLE `inventory`.`GenebankOrder` ADD COLUMN  `requester_id` bigint after `smta611`;

alter table GenebankOrder 
        add index FK1F4292DDB6C027A5 (requester_id), 
        add constraint FK1F4292DDB6C027A5 
        foreign key (requester_id) 
        references User (id);

SELECT id, CONCAT(IF(notes<>'',CONCAT('DEFAULT: ', notes, '||'),''),'QUANTITY: ', quantity,'||STATUS: ', status) AS notes, notes, quantity, status, lastUpdated FROM inventory.Lot WHERE YEAR(lastUpdated)<2013 LIMIT 10000000;
UPDATE inventory.Lot set notes=CONCAT(IF(notes<>'',CONCAT('DEFAULT: ', notes, '||'),''),'QUANTITY: ', quantity,'||STATUS: ', status, '||Deleted by setting status=0 manually'), quantity=0, status=0 WHERE YEAR(lastUpdated)<2013;

--ZEROING ALL LOTs FROM 2012 and BELOW THOUGH MYSQL IS LOOSING CONNECTION
SET innodb_lock_wait_timeout=500000;
SET @@global.max_allowed_packet = 1048576*10;
UPDATE Lot INNER JOIN BarCode INNER JOIN Viability INNER JOIN LotTraitLastValue INNER JOIN LotTraitValue INNER JOIN Transaction2 INNER JOIN QuantityUpdate INNER JOIN DistributionLot INNER JOIN GenebankOrderItem INNER JOIN LotVariable INNER JOIN FieldLot INNER JOIN FieldVariables INNER JOIN OtherLot SET Lot.notes=CONCAT(IF(Lot.notes<>'',CONCAT('DEFAULT: ', Lot.notes, '||'),''),'QUANTITY: ', Lot.quantity,'||STATUS: ', Lot.status, '||Deleted by setting status=0 manually'), Lot.quantity=0, Lot.status=0 WHERE YEAR(Lot.lastUpdated)<2013
-- ZEROING STOPS HERE

-- DELETING ALL LOTs from 2012 AND BELOW
-- DELETE FROM BarCode, Viability, LotTraitLastValue, Transaction2 USING BarCode INNER JOIN Viability INNER JOIN LotTraitLastValue INNER JOIN Transaction2 INNER JOIN Lot WHERE YEAR(Lot.lastUpdated)<2013 AND BarCode.lotId = Lot.id AND Viability.lot_id = Lot.id AND LotTraitLastValue.entity_id = Lot.id AND Transaction2.lot = Lot.id;

DELETE FROM BarCode USING BarCode INNER JOIN Lot WHERE YEAR(Lot.lastUpdated)<2013 AND BarCode.lotId = Lot.id;
DELETE FROM Viability USING Viability INNER JOIN Lot WHERE YEAR(Lot.lastUpdated)<2013 AND Viability.lot_id = Lot.id;
DELETE FROM LotTraitLastValue USING LotTraitLastValue INNER JOIN Lot WHERE YEAR(Lot.lastUpdated)<2013 AND LotTraitLastValue.entity_id = Lot.id;
DELETE FROM Transaction2 USING Transaction2 INNER JOIN Lot WHERE YEAR(Lot.lastUpdated)<2013 AND Transaction2.lot = Lot.id;
DELETE FROM LotTraitValue USING LotTraitValue INNER JOIN Lot WHERE YEAR(Lot.lastUpdated)<2013 AND LotTraitValue.entity_id = Lot.id;
DELETE FROM QuantityUpdate USING QuantityUpdate INNER JOIN Lot WHERE YEAR(Lot.lastUpdated)<2013 AND QuantityUpdate.lotId = Lot.id;
DELETE FROM DistributionLot USING DistributionLot INNER JOIN Lot WHERE YEAR(Lot.lastUpdated)<2013 AND DistributionLot.lot = Lot.id;
DELETE FROM GenebankOrderItem USING GenebankOrderItem INNER JOIN Lot WHERE YEAR(Lot.lastUpdated)<2013 AND GenebankOrderItem.lot_id = Lot.id;
DELETE FROM LotVariableUpdate USING LotVariableUpdate INNER JOIN LotVariable INNER JOIN Lot WHERE YEAR(Lot.lastUpdated)<2013 AND LotVariable.lot = Lot.id AND LotVariableUpdate.lotVariableId = LotVariable.id;
DELETE FROM LotVariable USING LotVariable INNER JOIN Lot WHERE YEAR(Lot.lastUpdated)<2013 AND LotVariable.lot = Lot.id;
DELETE FROM FieldLot USING FieldLot INNER JOIN Lot WHERE YEAR(Lot.lastUpdated)<2013 AND FieldLot.id = Lot.id;
DELETE FROM FieldVariables USING FieldVariables INNER JOIN Lot WHERE YEAR(Lot.lastUpdated)<2013 AND FieldVariables.lot_id = Lot.id;
DELETE FROM OtherLot USING OtherLot INNER JOIN Lot WHERE YEAR(Lot.lastUpdated)<2013 AND OtherLot.id = Lot.id;
DELETE FROM Lot WHERE YEAR(Lot.lastUpdated)<2013
--- DELETING STOPS HERE
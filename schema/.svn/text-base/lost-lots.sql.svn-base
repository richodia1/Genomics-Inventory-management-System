
DELIMITER $$

CREATE PROCEDURE lostLots1()
BEGIN

DECLARE x, ownerid bigint default null;
SELECT id INTO ownerid FROM User where username='OpOyatomi';

-- UNCOMMENT the three SQL lines below
 
--insert into LotList (createdBy, lastUpdatedBy, createdDate, lastUpdated, name, owner_id) values ('MObreza', 'MObreza', now(), now(), 'Lost lots 2010-07-26', ownerid);
--SELECT LAST_INSERT_ID() INTO x;
--insert into LotListLots (LotList_id, element) select x, Lot.id from Lot where createdBy='OpOyatomi' and createdDate>='2010-07-26';

END$$

DELIMITER ;

call lostLots1();
DROP PROCEDURE IF EXISTS lostLots1;

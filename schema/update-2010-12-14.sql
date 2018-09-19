-- Add generation count
alter table Lot add column generation bigint;
alter table Lot add column line varchar(20);

-- Move InVitroLot.lineNumber to Lot.line
update Lot L inner join InVitroLot IL on IL.id=L.id set L.line=IL.lineNumber;
alter table InVitroLot drop column lineNumber;

-- Other lot
create table OtherLot (
    id bigint not null,
    primary key (id)
) type=InnoDB;

alter table OtherLot 
    add index FKBE0BF6412CF549DE (id), 
    add constraint FKBE0BF6412CF549DE 
    foreign key (id) 
    references Lot (id);

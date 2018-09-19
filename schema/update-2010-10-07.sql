-- 07/10/2010: 
-- Genebank Order schema changes
-- 
drop table GenebankOrderItem;
drop table GenebankOrder;

alter table QuantityUpdateBulk add column order_id bigint;


create table GenebankOrder (
    id bigint not null auto_increment,
    createdBy varchar(255),
    createdDate datetime,
    lastUpdated datetime,
    lastUpdatedBy varchar(255),
    version integer not null,
    country varchar(255),
    firstName varchar(100),
    lastName varchar(100),
    mail varchar(255),
    orderDate datetime,
    organization varchar(100),
    shippingAddress longtext,
    state varchar(255),
    title longtext,
    description longtext,
    organizationCategory varchar(200),
    purpose varchar(200) not null,
    remoteOrderId bigint,
    primary key (id)
) type=InnoDB;

create table GenebankOrderItem (
    id bigint not null auto_increment,
    createdBy varchar(255),
    createdDate datetime,
    lastUpdated datetime,
    lastUpdatedBy varchar(255),
    version integer not null,
    itemName varchar(200) not null,
    quantity double precision,
    scale varchar(20),
    status varchar(255),
    item_id bigint,
    lot_id bigint,
    order_id bigint not null,
    primary key (id)
) type=InnoDB;

alter table GenebankOrderItem 
    add index FKC3F6CB90EB92FD8C (lot_id), 
    add constraint FKC3F6CB90EB92FD8C 
    foreign key (lot_id) 
    references Lot (id);

alter table GenebankOrderItem 
    add index FKC3F6CB90EFD3C9A8 (item_id), 
    add constraint FKC3F6CB90EFD3C9A8 
    foreign key (item_id) 
    references Item (id);

alter table GenebankOrderItem 
    add index FKC3F6CB90F15559B (order_id), 
    add constraint FKC3F6CB90F15559B 
    foreign key (order_id) 
    references GenebankOrder (id);

alter table QuantityUpdateBulk 
    add index FK92527C86F15559B (order_id), 
    add constraint FK92527C86F15559B 
    foreign key (order_id) 
    references GenebankOrder (id);
    
alter table GenebankOrder add column contactInfo varchar(200), add shippingCost double;

create table OrderTag (
    percentage double precision,
    id bigint not null,
    entity_id bigint not null,
    primary key (id)
) type=InnoDB;

create table Tag (
    id bigint not null auto_increment,
    tag varchar(200) not null,
    primary key (id)
) type=InnoDB;


alter table OrderTag 
    add index FK4D6C398C995F34E6 (entity_id), 
    add constraint FK4D6C398C995F34E6 
    foreign key (entity_id) 
    references GenebankOrder (id);

alter table OrderTag 
    add index FK4D6C398C20DBF6E9 (id), 
    add constraint FK4D6C398C20DBF6E9 
    foreign key (id) 
    references Tag (id);

alter table Item add column latinName varchar(200);

create table Template (
    id bigint not null auto_increment,
    createdBy varchar(255),
    createdDate datetime,
    lastUpdated datetime,
    lastUpdatedBy varchar(255),
    footer longtext,
    header longtext,
    shortName varchar(100) not null,
    template longtext,
    title varchar(250) not null,
    primary key (id),
    unique (shortName)
) type=InnoDB;

alter table GenebankOrder add agreementOption varchar(50), add providerName varchar(200), add providerOrganization varchar(200);
alter table GenebankOrder add smta611 bit not null default 0;


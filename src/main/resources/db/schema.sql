create table tag_gift_certificate
(
    id_tag int unique,
    id_gift_certificate int unique
);

create table gift_certificate
(
    id_gift_certificate serial not null,
    name                varchar(25),
    description         varchar(100),
    price               numeric(10, 2),
    duration            int,
    create_date         timestamp without time zone,
    last_update_date    timestamp without time zone,
    primary key (id_gift_certificate)
);

create table tag
(
    id_tag              serial not null,
    name                varchar(25) unique,
    id_gift_certificate int not null,
    primary key (id_tag),
    foreign key (id_gift_certificate) references gift_certificate(id_gift_certificate)
);
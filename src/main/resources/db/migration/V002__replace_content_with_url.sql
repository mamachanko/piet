drop table image;

create table image
(
    id     varchar(255) primary key,
    status integer not null,
    url    text,
    text   text
);

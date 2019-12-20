create table image
(
    id      varchar(255) primary key,
    status  integer,
    content bytea,
    text    text
);

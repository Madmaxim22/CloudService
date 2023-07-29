-- liquibase formatted sql

-- changeset liquibase:1
create table users
(
    id        bigint auto_increment primary key,
    email     varchar(255) unique not null,
    firstname varchar(255) not null,
    lastname  varchar(255) not null,
    password  varchar(255) not null,
    role      enum ('ADMIN', 'USER') null
);

create table tokens
(
    expired    bit not null,
    revoker    bit not null,
    id         bigint auto_increment primary key,
    user_id    bigint not null,
    token      varchar(255) not null,
    token_type enum ('BEARER') not null,
    foreign key (user_id) references users (id)
);

create table files
(
    id      bigint auto_increment primary key,
    size    bigint not null,
    user_id bigint not null,
    name    varchar(255) not null,
    type    varchar(255) not null,
    data    tinyblob not null,
    foreign key (user_id) references users (id)
)
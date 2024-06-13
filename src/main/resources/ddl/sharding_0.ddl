create table city
(
    id        bigint auto_increment
        primary key,
    city_name varchar(32) null
);

create table complex_user_0
(
    id       int not null
        primary key,
    order_id int null
);

create table complex_user_1
(
    id       int not null
        primary key,
    order_id int null
);

create table orders
(
    id      bigint auto_increment
        primary key,
    user_id int not null
);

create table product_0
(
    id           bigint auto_increment
        primary key,
    product_name varchar(64) default '' not null
);


create table product_1
(
    id           bigint auto_increment
        primary key,
    product_name varchar(64) default '' not null
);

create table product_item_0
(
    id         bigint auto_increment
        primary key,
    product_id bigint null,
    city_id    bigint null
);

create table product_item_1
(
    id         bigint auto_increment
        primary key,
    product_id bigint null,
    city_id    bigint null
);

create table users_0
(
    id   bigint auto_increment
        primary key,
    name varchar(64) not null
);

create table users_1
(
    id   bigint auto_increment
        primary key,
    name varchar(64) not null
);

create table users_2
(
    id   bigint auto_increment
        primary key,
    name varchar(64) not null
);

create table users_3
(
    id   bigint auto_increment
        primary key,
    name varchar(64) not null
);


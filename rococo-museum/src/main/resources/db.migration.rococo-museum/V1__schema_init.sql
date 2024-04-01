create extension if not exists "uuid-ossp";

create table if not exists "museum"
(
    id          UUID unique        not null default uuid_generate_v1() primary key,
    title       varchar(255) unique not null,
    description varchar(2000)       not null,
    photo       bytea              not null,
    city        varchar(255)        not null,
    country_id  UUID               not null
);

alter table "museum" owner to postgres;

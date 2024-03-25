create extension if not exists "uuid-ossp";

create table if not exists "museum"
(
    id          UUID unique        not null default uuid_generate_v1() primary key,
    title       varchar(50) unique not null,
    description varchar(255)       not null,
    photo       bytea              not null,
    city        varchar(50)        not null,
    country_id  UUID               not null
);

alter table "museum" owner to postgres;

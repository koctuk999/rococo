create extension if not exists "uuid-ossp";

create table if not exists "painting"
(
    id          UUID unique         not null default uuid_generate_v1() primary key,
    title       varchar(255) unique not null,
    description varchar(2000)       not null,
    content     bytea               not null,
    museum_id   UUID                not null,
    artist_id   UUID                not null
);

alter table "painting"
    owner to postgres;

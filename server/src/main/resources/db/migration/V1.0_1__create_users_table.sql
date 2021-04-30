create table if not exists users
(
    id                  bigint primary key       not null,
    login_id            varchar(255)             not null,
    first_name          varchar(255)             not null,
    last_name           varchar(255)             not null,
    auth_provider       varchar(64)              not null,
    email               varchar(255)             not null,
    phone               varchar(255),
    profile_picture_url varchar(1024),
    is_dummy            boolean                  not null,
    created             timestamp with time zone not null,
    modified            timestamp with time zone not null
);
create sequence if not exists users_seq increment by 50;
create unique index if not exists uidx_users_login_id on users (login_id);
create index if not exists idx_users_modified on users (modified);

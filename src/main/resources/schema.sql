create table if not exists users
(
    user_id  uuid  not null
        constraint users_pk
            primary key,
    name     varchar not null,
    username varchar not null,
    password varchar not null
);

create unique index if not exists users_user_id_uindex
    on users (user_id);

create unique index if not exists users_username_uindex
    on users (username);





create table if not exists boards
(
    board_id uuid  not null
        constraint boards_pk
            primary key,
    name     varchar not null,
    user_id  uuid not null
);

create unique index if not exists boards_board_id_uindex
    on boards (board_id);





create table if not exists board_columns
(
    column_id    uuid not null
        constraint board_columns_pk
            primary key,
    name         varchar not null,
    column_order integer not null,
    board_id     uuid not null
);

create unique index if not exists board_columns_column_id_uindex
    on board_columns (column_id);





create table if not exists cards
(
    card_id   uuid not null
        constraint cards_pk
            primary key,
    name      varchar not null,
    column_id uuid not null
);


create unique index if not exists cards_card_id_uindex
    on cards (card_id);





create table if not exists users_boards
(
    user_id  uuid not null
        constraint user_id
            references users,
    board_id uuid not null
        constraint board_id
            references boards
);

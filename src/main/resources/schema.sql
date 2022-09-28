drop table if exists book_case;
drop table if exists book;
drop table if exists member;

create table book
(
    book_id    bigint      not null auto_increment,
    created_at datetime    not null,
    updated_at datetime    not null,
    created_by bigint      not null,
    updated_by bigint      not null,
    day        bigint      not null,
    income     bigint      not null,
    is_delete  bit,
    memo       varchar(256),
    month      bigint      not null,
    outcome    bigint      not null,
    title      varchar(32) not null,
    year       bigint      not null,
    member_id  bigint,
    primary key (book_id)
) engine=InnoDB;

create table book_case
(
    book_case_id bigint       not null auto_increment,
    created_at   datetime     not null,
    updated_at   datetime     not null,
    created_by   bigint       not null,
    updated_by   bigint       not null,
    income       bigint       not null,
    is_delete    bit,
    memo         varchar(256),
    outcome      bigint       not null,
    place        varchar(128) not null,
    title        varchar(32)  not null,
    book_id      bigint,
    primary key (book_case_id)
) engine=InnoDB;

create table member
(
    member_id     bigint       not null auto_increment,
    access_token  varchar(200),
    email         varchar(320) not null,
    member_role   varchar(16)  not null,
    nickname      varchar(16)  not null,
    password      varchar(60)  not null,
    refresh_token varchar(200),
    primary key (member_id)
) engine=InnoDB;

alter table member
    add constraint UK_f3bto5pwgpap696kls4vl7bjx unique (access_token);

alter table member
    add constraint UK_mbmcqelty0fbrvxp1q58dn57t unique (email);

alter table member
    add constraint UK_hh9kg6jti4n1eoiertn2k6qsc unique (nickname);

alter table member
    add constraint UK_56ymicgdb3uysana9eq1qmyjf unique (refresh_token);

alter table book
    add constraint FKpdmglejuicm0m2wwgvosuv0nq
        foreign key (member_id)
            references member (member_id);

alter table book_case
    add constraint FKro5l1vjdm5ylkd3bqye6ap6cj
        foreign key (book_id)
            references book (book_id);
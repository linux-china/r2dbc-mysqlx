create table people (
    id         int          not null auto_increment,
    nick       varchar(255) not null,
    created_at timestamp,
    primary key (id)
);

insert into people (nick, created_at)
values ('linux_china', now());
insert into people (nick, created_at)
values ('joe', now());
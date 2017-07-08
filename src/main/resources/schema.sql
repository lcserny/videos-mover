drop table VIDEOS if exists;

create table VIDEOS (
    id bigint identity primary key,
    name varchar(250) not null,
    file_path varchar(500) not null,
    tv_output varchar(500),
    movie_output varchar(500)
);
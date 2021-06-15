drop table person if exists;
create table person (
    timestr varchar(128),
    door_id varchar(128),
    person_name varchar(128),
    direction varchar(8),
    picture varchar(128),
    is_correct boolean,
    wrong_result varchar(12),
    primary key(door_id, timestr)
);
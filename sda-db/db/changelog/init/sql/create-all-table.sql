
create schema if not exists model;

create table if not exists model.schedules(
	id serial primary key,
	name varchar(10),
	date date,
	hash int
);

create table if not exists model.disciplines(
	id serial primary key,
	name text
);

create table if not exists model.teacher_posts(
	id serial primary key,
	name text
);

create table if not exists model.teachers(
	id serial primary key,
	post_id int,
	name varchar(100)
);

create type model.pair_pos as enum('FIRST', 'SECOND', 'THIRD', 'FOURTH', 'FIFTH', 'SIXTH', 'SEVENTH', 'EIGHTH');

create type model.lesson_format as enum('LEFT', 'RIGHT', 'FULL', 'BOTH');

create table if not exists model.lessons(
	id serial primary key,
	schedule_id int not null,
	discipline_id int not null,
	teacher_id int not null,
	auditorium varchar(20) not null,
	position int not null, -- after show in exam replace on enum
	format varchar(5) default 'FULL', -- after show in exam replace on enum
	unique (position, format)
);

create table if not exists model.schedule_updates(
	id serial primary key,
	schedule_id int not null,
    uploaded timestamp not null,
	last_update timestamp
);

create schema if not exists model;

create table if not exists model.schedules(
	id serial,
	name varchar(10),
	date date,
	hash int
);

create table if not exists model.disciplines(
	id serial,
	name text
);

create table if not exists model.teacher_posts(
	id serial,
	name text
);

create table if not exists model.teachers(
	id serial,
	post_id int,
	name varchar(100)
);

create type model.pair_pos as enum('1', '2', '3', '4', '5', '6', '7', '8');

create type model.lesson_format as enum('left', 'right', 'full');

create table if not exists model.lessons(
	id serial,
	schedule_id int not null,
	discipline_id int not null,
	teacher_id int not null,
	auditorium varchar(20) not null,
	position model.pair_pos not null,
	format model.lesson_format not null,
	unique (position, format)
);

create table if not exists model.schedule_updates(
	id serial,
	schedule_id int not null,
    uploaded timestamp not null,
	last_update timestamp
);
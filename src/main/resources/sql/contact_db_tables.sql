drop database if exists contact_db;

create database contact_db;

use contact_db;

create table genders(
  id_gender int not null auto_increment,
  gender varchar(15) not null,
  primary key(id_gender)
);

create table nationalities(
  id_nationality int not null auto_increment,
  nationality varchar(40) not null,
  primary key(id_nationality)
);

create table marital_status(
  id_marital_status int not null auto_increment,
  marital_status_name varchar(40) not null,
  primary key(id_marital_status)
);

create table addresses(
  id_address int not null auto_increment,
  country varchar(40) not null,
  city varchar(40),
  address varchar(50) not null,
  index_number varchar(40),
  primary key(id_address)
);

create table companies(
  id_company int not null auto_increment,
  company varchar(50) not null,
  primary key(id_company)
);

create table contacts (
  id_contact int not null auto_increment,
  first_name varchar(50) not null,
  surname varchar(50) not null,
  patronymic varchar(50) not null,
  birthday date not null,
  website varchar(70),
  email varchar(50) not null,
  id_address int not null,
  id_company int not null,
  id_gender int not null,
  id_nationality int not null,
  id_marital_status int not null,
  primary key(id_contact),
  foreign key (id_address) references addresses(id_address),
  foreign key (id_company) references companies(id_company),
  foreign key(id_gender) references genders(id_gender),
  foreign key(id_nationality) references nationalities(id_nationality),
  foreign key(id_marital_status) references marital_status(id_marital_status)
);

create table photos (
  id_photo int not null auto_increment,
  id_contact int not null,
  photo mediumblob,
  primary key(id_photo),
  foreign key(id_contact) references contacts(id_contact)
);

create table phone_types(
  id_phone_type int not null auto_increment,
  phone_type varchar(30) not null,
  primary key(id_phone_type)
);

create table phones(
  id_phone int not null auto_increment,
  id_contact int not null,
  phone_number varchar(30) not null,
  id_phone_type int not null,
  commentary varchar(200),
  primary key(id_phone),
  foreign key(id_contact) references contacts(id_contact),
  foreign key(id_phone_type) references phone_types(id_phone_type)

);

create table attachments(
  id_attachment int not null auto_increment,
  id_contact int not null,
  name varchar(50) not null,
  download_date date not null,
  commentary varchar(200),
  attachment longblob,
  primary key (id_attachment),
  foreign key(id_contact) references contacts(id_contact)
);
use contact_db;

insert into addresses(
country, city, address, index_number) values
("Belarus", "Minsk", "Prushinskih 5", "234001");

insert into companies(company) values
("iTechArt");

insert into genders(gender) values
("Male"),
("Female");

insert into nationalities(nationality) values
("Belarus");

insert into marital_status(marital_status_name) values
("Single"),
("In a relationship"),
("Married");

insert into contacts(
first_name, surname, patronymic, birthday, website,
email, id_address, id_company, id_gender,
id_nationality, id_marital_status) values
("test first_name", "test surname", "test patronymic",
"2017-07-04", "test website", "test email", 1, 1, 1, 1, 1);

insert into phone_types(phone_type) values
("Mobile"),
("Home"),
("Working");

insert into photos(id_contact) values
(1);

insert into attachments(id_contact, name, download_date,
commentary) values
(1, "XXX", "2017-07-04", "test comment");

insert into phones(id_contact, phone_number,
id_phone_type, commentary) values
(1, "+375294444444", 1, "test comment");
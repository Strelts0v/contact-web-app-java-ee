USE `contact_db`;

INSERT INTO nationalities(nationality) VALUES
("Russian"),
("Belarus"),
("Ukrainian");

INSERT INTO companies(company) VALUES
("iTechArt"),
("Epam"),
("ITransition");

INSERT INTO photos(photo) VALUES
(NULL);

INSERT INTO contacts(first_name, surname, patronymic,
	birthday, website, email, country, city, address,
	index_number, gender, marital_status, id_nationality,
	id_company, id_photo) VALUES
("Ivan", "Vasilevskiy", "Aleksandr", "1998-04-07", "www.github.com/Vasilevskiy", "ivan.vasilevskiy@gmail.com", "Belarus", "Soligorsk", "Zheleznay 5/44", "220001", "Male", "Single", 1, 1, 1),
("Alexey", "Fedorov", "Aleksandr", "1998-08-11", "www.github.com/Feodorov", "alexey.fedorov@gmail.com", "Belarus", "Smorgon", "Vileyskaya 90/47", "220022", "Male", "Single", 1, 2, 2),
("Sergey", "Pastushenko", "Aleksandr", "1998-12-07", "www.github.com/Pastushenko", "sergey.pastushenko@gmail.com", "Belarus", "Brest", "Zelenaya 14", "220034", "Male", "Single", 1, 3, 3),
("Ivan", "Vasilevskiy", "Aleksandr", "1998-04-07", "www.github.com/Vasilevskiy", "ivan.vasilevskiy@gmail.com", "Belarus", "Soligorsk", "Zeleznay 5/44", "220001", "Male", "Single", 1, 1, 4),
("Ivan", "Vasilevskiy", "Aleksandr", "1998-04-07", "www.github.com/Vasilevskiy", "ivan.vasilevskiy@gmail.com", "Belarus", "Soligorsk", "Zeleznay 5/44", "220001", "Male", "Single", 1, 1, 5),
("Ivan", "Vasilevskiy", "Aleksandr", "1998-04-07", "www.github.com/Vasilevskiy", "ivan.vasilevskiy@gmail.com", "Belarus", "Soligorsk", "Zeleznay 5/44", "220001", "Male", "Single", 1, 1, 6),
("Ivan", "Vasilevskiy", "Aleksandr", "1998-04-07", "www.github.com/Vasilevskiy", "ivan.vasilevskiy@gmail.com", "Belarus", "Soligorsk", "Zeleznay 5/44", "220001", "Male", "Single", 1, 1, 7),
("Ivan", "Vasilevskiy", "Aleksandr", "1998-04-07", "www.github.com/Vasilevskiy", "ivan.vasilevskiy@gmail.com", "Belarus", "Soligorsk", "Zeleznay 5/44", "220001", "Male", "Single", 1, 1, 8),
("Ivan", "Vasilevskiy", "Aleksandr", "1998-04-07", "www.github.com/Vasilevskiy", "ivan.vasilevskiy@gmail.com", "Belarus", "Soligorsk", "Zeleznay 5/44", "220001", "Male", "Single", 1, 1, 9),
("Ivan", "Vasilevskiy", "Aleksandr", "1998-04-07", "www.github.com/Vasilevskiy", "ivan.vasilevskiy@gmail.com", "Belarus", "Soligorsk", "Zeleznay 5/44", "220001", "Male", "Single", 1, 1, 10);

INSERT INTO phones(id_contact, phone_number, phone_type, commentary)
VALUES
(1, "+375291111111", "Mobile", "comment"),
(1, "+375292222222", "Home", "comment"),
(1, "+375293333333", "Working", "comment");

INSERT INTO attachments(id_contact, name, download_date,
commentary, attachment) VALUES
(1, "file1", "2017-07-01", "comment", NULL),
(1, "file2", "2017-07-02", "comment", NULL),
(1, "file3", "2017-07-03", "comment", NULL);
--liquibase formatted sql

--changeset Sergey Tatarinov:20230311_add_drivers_and_lineup_for_2023

insert into driver(name, surname, race_number, country_id) VALUES
('Oscar', 'Piastri', 28, 1),
('Logan', 'Sergeant', 2, 19);

insert into line_up VALUES
(1, 1, 2023),
(1, 15, 2023),
(2, 3, 2023),
(2, 4, 2023),
(3, 6, 2023),
(3, 27, 2023),
(4, 9, 2023),
(4, 12, 2023),
(5, 13, 2023),
(5, 10, 2023),
(6, 7, 2023),
(6, 8, 2023),
(7, 14, 2023),
(7, 26, 2023),
(8, 2, 2023),
(8, 23, 2023),
(9, 24, 2023),
(9, 25, 2023),
(10, 22, 2023),
(10, 28, 2023);
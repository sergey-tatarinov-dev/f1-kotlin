--liquibase formatted sql

--changeset Sergey Tatarinov:20221205_insert_drivers

insert into driver(name, surname, race_number, country_id) VALUES
('Alexander', 'Albon', 23, 28),
('Zhou', 'Guanyu', 24, 3),
('Kevin', 'Magnussen', 20, 30);
--liquibase formatted sql

--changeset Sergey Tatarinov:20221208_insert_drivers_of_2022.sql

insert into driver(name, surname, race_number, country_id) VALUES
('Nico', 'Hulkenberg', 27, 11),
('Nick', 'De Vries', 49, 24);
--liquibase formatted sql

--changeset Sergey Tatarinov:20221205_insert_country

insert into country(name, file_id) VALUES
('Denmark', 42);
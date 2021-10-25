--liquibase formatted sql

--changeset Sergey Tatarinov:20211022_insert_driver

INSERT INTO driver(name, surname, race_number)
VALUES ('Robert', 'Kubica', 88);
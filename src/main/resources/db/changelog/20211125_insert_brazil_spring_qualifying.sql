--liquibase formatted sql

--changeset Sergey Tatarinov:20211125_insert_brazil_spring_qualifying

insert into grand_prix(date, full_name, track_id, sprint)
VALUES ('2021-11-13', 'São Paulo Sprint Qualifying', 20, true);
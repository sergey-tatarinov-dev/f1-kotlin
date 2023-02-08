--liquibase formatted sql

--changeset Sergey Tatarinov:20221205_add_lineup_of_2022

insert into line_up VALUES
(4, 25, 2022),
(10, 26, 2022);
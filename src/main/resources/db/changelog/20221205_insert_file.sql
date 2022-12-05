--liquibase formatted sql

--changeset Sergey Tatarinov:20221205_insert_file

insert into file(name, full_path, extension) VALUES
('denmark', 'denmark.png', 'png');
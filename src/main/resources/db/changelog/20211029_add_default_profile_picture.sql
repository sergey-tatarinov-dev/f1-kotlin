--liquibase formatted sql

--changeset Sergey Tatarinov:20211029_add_default_profile_picture


INSERT INTO file(name, full_path, extension)
VALUES ('default-picture', '/pic/default-picture.png', 'png'),
       ('admin', '/pic/admin.png', 'png');

UPDATE users SET file_id = 30;
UPDATE users SET file_id = 31 where role = 'ADMIN';
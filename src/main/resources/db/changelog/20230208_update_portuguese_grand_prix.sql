--liquibase formatted sql

--changeset Sergey Tatarinov:20230208_update_portuguese_grand_prix.sql

update file
set full_path = 'portugal.png'
where id = 22;
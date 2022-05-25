--liquibase formatted sql

--changeset Sergey Tatarinov:20211025_insert_american_grand_prix_result

insert into grand_prix_result(grand_prix_id, driver_id, position) VALUES
--american gp
(57, 3, 1),
(57, 1, 2),
(57, 4, 3),
(57, 8, 4),
(57, 5, 5),
(57, 2, 6),
(57, 7, 7),
(57, 6, 8),
(57, 14, 9),
(57, 11, 10),
(57, 18, 11),
(57, 12, 12),
(57, 17, 13),
(57, 15, 14),
(57, 16, 15),
(57, 19, 16),
(57, 20, 17),
(57, 9, 18),
(57, 10, 19),
(57, 13, 20);

update grand_prix_result
set set_fastest_lap = true
where (grand_prix_id = 57 and driver_id = 1);
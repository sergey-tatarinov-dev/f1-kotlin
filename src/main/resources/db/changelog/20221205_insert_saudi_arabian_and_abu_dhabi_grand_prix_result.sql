--liquibase formatted sql

--changeset Sergey Tatarinov:20221205_insert_saudi_arabian_and_abu_dhabi_grand_prix_result

insert into grand_prix_result(grand_prix_id, driver_id, position) VALUES
--saudi arabian gp
(61, 1, 1),
(61, 3, 2),
(61, 2, 3),
(61, 10, 4),
(61, 5, 5),
(61, 13, 6),
(61, 8, 7),
(61, 7, 8),
(61, 18, 9),
(61, 6, 10),
(61, 12, 11),
(61, 16, 12),
(61, 9, 13),
(61, 14, 14),
(61, 17, 15),
(61, 11, 16),
(61, 4, 17),
(61, 15, 18),
(61, 20, 19),
(61, 19, 20),

--abu dhabi gp
(62, 3, 1),
(62, 1, 2),
(62, 7, 3),
(62, 14, 4),
(62, 13, 5),
(62, 2, 6),
(62, 6, 7),
(62, 9, 8),
(62, 10, 9),
(62, 8, 10),
(62, 11, 11),
(62, 5, 12),
(62, 12, 13),
(62, 19, 14),
(62, 4, 15),
(62, 16, 16),
(62, 18, 17),
(62, 15, 18),
(62, 17, 19),
(62, 20, 20);

update grand_prix_result
set set_fastest_lap = true
where (grand_prix_id = 61 and driver_id = 1);

update grand_prix_result
set set_fastest_lap = true
where (grand_prix_id = 62 and driver_id = 3);
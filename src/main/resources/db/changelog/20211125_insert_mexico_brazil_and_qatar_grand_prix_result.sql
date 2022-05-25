--liquibase formatted sql

--changeset Sergey Tatarinov:20211125_insert_mexico_brazil_and_qatar_grand_prix_result

insert into grand_prix(date, full_name, track_id, sprint)
VALUES ('2021-11-13', 'São Paulo Sprint Qualifying', 20, true);

insert into grand_prix_result(grand_prix_id, driver_id, position) VALUES
--mexico gp
(58, 3, 1),
(58, 1, 2),
(58, 4, 3),
(58, 13, 4),
(58, 8, 5),
(58, 7, 6),
(58, 11, 7),
(58, 17, 8),
(58, 9, 9),
(58, 6, 10),
(58, 18, 11),
(58, 5, 12),
(58, 10, 13),
(58, 12, 14),
(58, 2, 15),
(58, 15, 16),
(58, 16, 17),
(58, 20, 18),
(58, 19, 19),
(58, 14, 20),

--brazil gp
(59, 1, 1),
(59, 3, 2),
(59, 2, 3),
(59, 4, 4),
(59, 8, 5),
(59, 7, 6),
(59, 13, 7),
(59, 10, 8),
(59, 9, 9),
(59, 6, 10),
(59, 11, 11),
(59, 17, 12),
(59, 15, 13),
(59, 18, 14),
(59, 14, 15),
(59, 16, 16),
(59, 20, 17),
(59, 19, 18),
(59, 5, 19),
(59, 12, 20),

--qatar gp
(60, 1, 1),
(60, 3, 2),
(60, 9, 3),
(60, 4, 4),
(60, 10, 5),
(60, 12, 6),
(60, 7, 7),
(60, 8, 8),
(60, 6, 9),
(60, 11, 10),
(60, 13, 11),
(60, 5, 12),
(60, 14, 13),
(60, 17, 14),
(60, 18, 15),
(60, 19, 16),
(60, 15, 17),
(60, 20, 18),
(60, 16, 19),
(60, 2, 20),

--brazil sprint qualifying
(63, 2, 1),
(63, 3, 2),
(63, 7, 3),
(63, 4, 4),
(63, 1, 5),
(63, 6, 6),
(63, 8, 7),
(63, 13, 8),
(63, 10, 9),
(63, 11, 10),
(63, 5, 11),
(63, 9, 12),
(63, 18, 13),
(63, 12, 14),
(63, 14, 15),
(63, 16, 16),
(63, 15, 17),
(63, 17, 18),
(63, 19, 19),
(63, 20, 20);


update grand_prix_result
set set_fastest_lap = true
where (grand_prix_id = 58 and driver_id = 2);

update grand_prix_result
set set_fastest_lap = true
where (grand_prix_id = 59 and driver_id = 4);

update grand_prix_result
set set_fastest_lap = true
where (grand_prix_id = 60 and driver_id = 3);

update grand_prix_result
set set_fastest_lap = true
where (grand_prix_id = 63 and driver_id = 3);
--liquibase formatted sql

--changeset Sergey Tatarinov:20230214_add_fastest_laps_of_2022_and_sprints.sql

update grand_prix_result
set set_fastest_lap = true
where grand_prix_id = 64 and driver_id = 8;

update grand_prix_result
set set_fastest_lap = true
where grand_prix_id = 65 and driver_id = 8;

update grand_prix_result
set set_fastest_lap = true
where grand_prix_id = 66 and driver_id = 8;

update grand_prix_result
set set_fastest_lap = true
where grand_prix_id = 67 and driver_id = 4;

update grand_prix_result
set set_fastest_lap = true
where grand_prix_id = 68 and driver_id = 3;

update grand_prix_result
set set_fastest_lap = true
where grand_prix_id = 69 and driver_id = 3;

update grand_prix_result
set set_fastest_lap = true
where grand_prix_id = 70 and driver_id = 4;

update grand_prix_result
set set_fastest_lap = true
where grand_prix_id = 71 and driver_id = 6;

update grand_prix_result
set set_fastest_lap = true
where grand_prix_id = 72 and driver_id = 4;

update grand_prix_result
set set_fastest_lap = true
where grand_prix_id = 73 and driver_id = 7;

update grand_prix_result
set set_fastest_lap = true
where grand_prix_id = 74 and driver_id = 1;

update grand_prix_result
set set_fastest_lap = true
where grand_prix_id = 75 and driver_id = 8;

update grand_prix_result
set set_fastest_lap = true
where grand_prix_id = 76 and driver_id = 3;

update grand_prix_result
set set_fastest_lap = true
where grand_prix_id = 77 and driver_id = 7;

update grand_prix_result
set set_fastest_lap = true
where grand_prix_id = 78 and driver_id = 1;

update grand_prix_result
set set_fastest_lap = true
where grand_prix_id = 79 and driver_id = 3;

update grand_prix_result
set set_fastest_lap = true
where grand_prix_id = 80 and driver_id = 3;

update grand_prix_result
set set_fastest_lap = true
where grand_prix_id = 81 and driver_id = 4;

update grand_prix_result
set set_fastest_lap = true
where grand_prix_id = 82 and driver_id = 15;

update grand_prix_result
set set_fastest_lap = true
where grand_prix_id = 83 and driver_id = 23;

update grand_prix_result
set set_fastest_lap = true
where grand_prix_id = 84 and driver_id = 15;

update grand_prix_result
set set_fastest_lap = true
where grand_prix_id = 85 and driver_id = 15;

update grand_prix_result
set set_fastest_lap = true
where grand_prix_id = 86 and driver_id = 15;

update grand_prix_result
set set_fastest_lap = true
where grand_prix_id = 87 and driver_id = 15;

update grand_prix_result
set set_fastest_lap = true
where grand_prix_id = 88 and driver_id = 6;

update grand_prix
set sprint = true
where id in (67, 75, 86);

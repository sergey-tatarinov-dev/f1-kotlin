--liquibase formatted sql

--changeset Sergey Tatarinov:20211025_insert_countries

INSERT INTO country(name, file_id) VALUES
('Australia', 1),
('Bahrain', 2),
('China', 3),
('Azerbaijan', 4),
('Spain', 5),
('Monaco', 6),
('Canada', 7),
('France', 8),
('Austria', 9),
('Great Britain', 10),
('Germany', 11),
('Hungary', 12),
('Belgium', 13),
('Italy', 14),
('Singapore', 15),
('Russia', 16),
('Japan', 17),
('Mexico', 18),
('United States', 19),
('Brazil', 20),
('Arabian Emirates', 21),
('Portugal', 22),
('Turkey', 23),
('Netherlands', 24),
('Qatar', 25),
('Saudi Arabia', 26),
('Poland', 27),
('Thailand', 28),
('Finland', 29);

UPDATE track SET country_id = 1 WHERE id = 1;
UPDATE track SET country_id = 2 WHERE id in (2, 27);
UPDATE track SET country_id = 3 WHERE id = 3;
UPDATE track SET country_id = 4 WHERE id = 4;
UPDATE track SET country_id = 5 WHERE id = 5;
UPDATE track SET country_id = 6 WHERE id = 6;
UPDATE track SET country_id = 7 WHERE id = 7;
UPDATE track SET country_id = 8 WHERE id = 8;
UPDATE track SET country_id = 9 WHERE id = 9;
UPDATE track SET country_id = 10 WHERE id = 10;
UPDATE track SET country_id = 11 WHERE id in (11, 23);
UPDATE track SET country_id = 12 WHERE id = 12;
UPDATE track SET country_id = 13 WHERE id = 13;
UPDATE track SET country_id = 14 WHERE id in (14, 22, 25);
UPDATE track SET country_id = 15 WHERE id = 15;
UPDATE track SET country_id = 16 WHERE id = 16;
UPDATE track SET country_id = 17 WHERE id = 17;
UPDATE track SET country_id = 18 WHERE id = 18;
UPDATE track SET country_id = 19 WHERE id = 19;
UPDATE track SET country_id = 20 WHERE id = 20;
UPDATE track SET country_id = 21 WHERE id = 21;
UPDATE track SET country_id = 22 WHERE id = 24;
UPDATE track SET country_id = 23 WHERE id = 26;
UPDATE track SET country_id = 24 WHERE id = 28;
UPDATE track SET country_id = 25 WHERE id = 29;
UPDATE track SET country_id = 26 WHERE id = 30;

UPDATE driver SET country_id = 10 WHERE id = 1;
UPDATE driver SET country_id = 29 WHERE id = 2;
UPDATE driver SET country_id = 24 WHERE id = 3;
UPDATE driver SET country_id = 18 WHERE id = 4;
UPDATE driver SET country_id = 1 WHERE id = 5;
UPDATE driver SET country_id = 10 WHERE id = 6;
UPDATE driver SET country_id = 5 WHERE id = 7;
UPDATE driver SET country_id = 6 WHERE id = 8;
UPDATE driver SET country_id = 5 WHERE id = 9;
UPDATE driver SET country_id = 8 WHERE id = 10;
UPDATE driver SET country_id = 11 WHERE id = 11;
UPDATE driver SET country_id = 7 WHERE id = 12;
UPDATE driver SET country_id = 8 WHERE id = 13;
UPDATE driver SET country_id = 17 WHERE id = 14;
UPDATE driver SET country_id = 10 WHERE id = 15;
UPDATE driver SET country_id = 7 WHERE id = 16;
UPDATE driver SET country_id = 29 WHERE id = 17;
UPDATE driver SET country_id = 14 WHERE id = 18;
UPDATE driver SET country_id = 11 WHERE id = 19;
UPDATE driver SET country_id = 16 WHERE id = 20;
UPDATE driver SET country_id = 27 WHERE id = 21;

UPDATE team SET country_id = 11 WHERE id = 1;
UPDATE team SET country_id = 9 WHERE id = 2;
UPDATE team SET country_id = 10 WHERE id = 3;
UPDATE team SET country_id = 10 WHERE id = 4;
UPDATE team SET country_id = 8 WHERE id = 5;
UPDATE team SET country_id = 14 WHERE id = 6;
UPDATE team SET country_id = 14 WHERE id = 7;
UPDATE team SET country_id = 14 WHERE id = 8;
UPDATE team SET country_id = 19 WHERE id = 9;
UPDATE team SET country_id = 10 WHERE id = 10;
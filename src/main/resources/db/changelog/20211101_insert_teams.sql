--liquibase formatted sql

--changeset Sergey Tatarinov:20211101_insert_teams

INSERT INTO file (name, full_path, extension) VALUES
('mercedes', 'mercedes.png', 'png'),
('red-bull', 'red-bull.png', 'png'),
('mclaren', 'mclaren.png', 'png'),
('aston-martin', 'aston-martin.png', 'png'),
('alpine', 'alpine.png', 'png'),
('ferrari', 'ferrari.png', 'png'),
('alpha-tauri', 'alpha-tauri.png', 'png'),
('alfa-romeo', 'alfa-romeo.png', 'png'),
('haas', 'haas.png', 'png'),
('williams', 'williams.png', 'png');

ALTER TABLE team ADD COLUMN logo_id BIGINT;
ALTER TABLE team ADD CONSTRAINT fk_team_file_id FOREIGN KEY (logo_id) REFERENCES file (id);

UPDATE team SET logo_id = 32 WHERE id = 1;
UPDATE team SET logo_id = 33 WHERE id = 2;
UPDATE team SET logo_id = 34 WHERE id = 3;
UPDATE team SET logo_id = 35 WHERE id = 4;
UPDATE team SET logo_id = 36 WHERE id = 5;
UPDATE team SET logo_id = 37 WHERE id = 6;
UPDATE team SET logo_id = 38 WHERE id = 7;
UPDATE team SET logo_id = 39 WHERE id = 8;
UPDATE team SET logo_id = 40 WHERE id = 9;
UPDATE team SET logo_id = 41 WHERE id = 10;

ALTER TABLE team ALTER COLUMN logo_id SET NOT NULL;

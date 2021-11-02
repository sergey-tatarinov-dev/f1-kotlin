--liquibase formatted sql

--changeset Sergey Tatarinov:20211101_insert_teams

INSERT INTO file (name, full_path, extension) VALUES
('mercedes', '/pic/mercedes.png', 'png'),
('red-bull', '/pic/red-bull.png', 'png'),
('mclaren', '/pic/mclaren.png', 'png'),
('aston-martin', '/pic/aston-martin.png', 'png'),
('alpine', '/pic/alpine.png', 'png'),
('ferrari', '/pic/ferrari.png', 'png'),
('alpha-tauri', '/pic/alpha-tauri.png', 'png'),
('alfa-romeo', '/pic/alfa-romeo.png', 'png'),
('haas', '/pic/haas.png', 'png'),
('williams', '/pic/williams.png', 'png');

ALTER TABLE team ADD COLUMN logo_id BIGINT;
ALTER TABLE team ADD CONSTRAINT fk_team_file_id FOREIGN KEY (logo_id) REFERENCES file (id);

UPDATE team SET logo_id = 60 WHERE id = 1;
UPDATE team SET logo_id = 61 WHERE id = 2;
UPDATE team SET logo_id = 62 WHERE id = 3;
UPDATE team SET logo_id = 63 WHERE id = 4;
UPDATE team SET logo_id = 64 WHERE id = 5;
UPDATE team SET logo_id = 65 WHERE id = 6;
UPDATE team SET logo_id = 66 WHERE id = 7;
UPDATE team SET logo_id = 67 WHERE id = 8;
UPDATE team SET logo_id = 68 WHERE id = 9;
UPDATE team SET logo_id = 69 WHERE id = 10;

ALTER TABLE team ALTER COLUMN logo_id SET NOT NULL;

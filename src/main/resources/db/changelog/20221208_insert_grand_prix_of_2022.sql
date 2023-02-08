--liquibase formatted sql

--changeset Sergey Tatarinov:20221208_insert_grand_prix_of_2022

insert into grand_prix(date, full_name, track_id) VALUES
('2022-03-20', 'Gulf Air Bahrain Grand Prix', 2),
('2022-03-27', 'Stc Saudi Arabian Grand Prix', 30),
('2022-04-10', 'Heineken Australian Grand Prix', 1),
('2022-04-23', 'Rolex Gran Premio Del Made In Italy E Dell''Emilia-Romagna - SPRINT', 25),
('2022-04-24', 'Rolex Gran Premio Del Made In Italy E Dell''Emilia-Romagna', 25),
('2022-05-08', 'Crypto.com Miami Grand Prix', 31),
('2022-05-22', 'Pirelli Gran Premio De España', 5),
('2022-05-29', 'Grand Prix De Monaco', 6),
('2022-06-12', 'Azerbaijan Grand Prix', 4),
('2022-06-19', 'AWS Grand Prix Du Canada', 7),
('2022-07-03', 'Lenovo British Grand Prix', 10),
('2022-07-09', 'Rolex Grosser Preis Von Österreich - Sprint', 9),
('2022-07-10', 'Rolex Grosser Preis Von Österreich', 9),
('2022-07-24', 'Lenovo Grand Prix De France', 8),
('2022-07-31', 'Aramco Magyar Nagydíj', 12),
('2022-08-28', 'Rolex Belgian Grand Prix', 13),
('2022-09-04', 'Heineken Dutch Grand Prix', 28),
('2022-09-11', 'Pirelli Gran Premio D’Italia', 14),
('2022-10-02', 'Singapore Airlines Singapore Grand Prix', 15),
('2022-10-09', 'Honda Japanese Grand Prix', 17),
('2022-10-23', 'Aramco United States Grand Prix', 19),
('2022-10-30', 'Gran Premio De La Ciudad De México', 18),
('2022-11-12', 'Heineken Grande Premio De Sao Paulo - Sprint', 20),
('2022-11-13', 'Heineken Grande Premio De Sao Paulo', 20),
('2022-11-20', 'Etihad Airways Abu Dhabi Grand Prix', 21);
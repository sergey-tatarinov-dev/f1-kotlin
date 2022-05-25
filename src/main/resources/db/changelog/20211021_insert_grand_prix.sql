--liquibase formatted sql

--changeset Sergey Tatarinov:20211021_insert_grand_prix

insert into grand_prix(date, full_name, track_id) VALUES
       ('2019-03-17', 'Australian Grand Prix', 1),
       ('2019-03-17', 'Bahrain Grand Prix', 2),
       ('2019-04-14', 'Chinese Grand Prix', 3),
       ('2019-04-28', 'Azerbaijan Grand Prix', 4),
       ('2019-05-12', 'Spanish Grand Prix', 5),
       ('2019-05-26', 'Monaco Grand Prix', 6),
       ('2019-06-09', 'Canadian Grand Prix', 7),
       ('2019-06-23', 'French Grand Prix', 8),
       ('2019-06-30', 'Austrian Grand Prix', 9),
       ('2019-07-14', 'British Grand Prix', 10),
       ('2019-07-28', 'German Grand Prix', 11),
       ('2019-08-04', 'Hungarian Grand Prix', 12),
       ('2019-09-01', 'Belgian Grand Prix', 13),
       ('2019-09-08', 'Italian Grand Prix', 14),
       ('2019-09-22', 'Singapore Grand Prix', 15),
       ('2019-09-29', 'Russian Grand Prix', 16),
       ('2019-10-13', 'Japanese Grand Prix', 17),
       ('2019-10-27', 'Mexican Grand Prix', 18),
       ('2019-11-03', 'United States Grand Prix', 19),
       ('2019-11-17', 'Brazilian Grand Prix', 20),
       ('2019-12-01', 'Abu Dhabi Grand Prix', 21),

       ('2020-07-05', 'Austrian Grand Prix', 9),
       ('2020-07-12', 'Styrian Grand Prix', 9),
       ('2020-07-19', 'Hungarian Grand Prix', 12),
       ('2020-08-02', 'British Grand Prix', 10),
       ('2020-08-09', '70th Anniversary Grand Prix', 10),
       ('2020-08-16', 'Spanish Grand Prix', 5),
       ('2020-08-30', 'Belgian Grand Prix', 13),
       ('2020-09-06', 'Italian Grand Prix', 14),
       ('2020-09-13', 'Tuscan Grand Prix', 22),
       ('2020-09-27', 'Russian Grand Prix', 16),
       ('2020-10-11', 'Eifel Grand Prix', 23),
       ('2020-10-25', 'Portuguese Grand Prix', 24),
       ('2020-11-01', 'Emilia Romagna Grand Prix', 25),
       ('2020-11-15', 'Turkish Grand Prix', 26),
       ('2020-11-29', 'Bahrain Grand Prix', 27),
       ('2020-12-06', 'Sakhir Grand Prix', 2),
       ('2020-12-13', 'Abu Dhabi Grand Prix', 21),

       ('2021-03-28', 'Bahrain Grand Prix', 2),
       ('2021-04-18', 'Emilia Romagna Grand Prix', 25),
       ('2021-05-02', 'Portuguese Grand Prix', 24),
       ('2021-05-09', 'Spanish Grand Prix', 5),
       ('2021-05-23', 'Monaco Grand Prix', 6),
       ('2021-06-06', 'Azerbaijan Grand Prix', 4),
       ('2021-06-20', 'French Grand Prix', 8),
       ('2021-06-27', 'Styrian Grand Prix', 9),
       ('2021-07-04', 'Austrian Grand Prix', 9),
       ('2021-07-17', 'British Sprint Qualifying', 10),
       ('2021-07-18', 'British Grand Prix', 10),
       ('2021-08-01', 'Hungarian Grand Prix', 12),
       ('2021-08-29', 'Belgian Grand Prix', 13),
       ('2021-09-05', 'Dutch Grand Prix', 28),
       ('2021-09-11', 'Italian Sprint Qualifying', 14),
       ('2021-09-12', 'Italian Grand Prix', 14),
       ('2021-09-26', 'Russian Grand Prix', 16),
       ('2021-10-10', 'Turkish Grand Prix', 26),
       ('2021-10-24', 'United States Grand Prix', 19),
       ('2021-11-07', 'Mexico City Grand Prix', 18),
       ('2021-11-14', 'São Paulo Grand Prix', 20),
       ('2021-11-21', 'Qatar Grand Prix', 29),
       ('2021-12-05', 'Saudi Arabian Grand Prix', 30),
       ('2021-12-12', 'Abu Dhabi Grand Prix', 21);

update grand_prix
set need_apply_half_points_multiplier = true
where id = 51;

update grand_prix
set sprint = true
where id in (48, 53);
--liquibase formatted sql

--changeset Sergey Tatarinov:20230217_drop_views_and_functions

DROP TRIGGER IF EXISTS create_views_at_update_result ON grand_prix_result;

DROP VIEW IF EXISTS driver_standings_2021;
DROP VIEW IF EXISTS driver_standings_2022;
DROP VIEW IF EXISTS team_standings_2021;
DROP VIEW IF EXISTS team_standings_2022;

DROP FUNCTION IF EXISTS create_views_2019();
DROP FUNCTION IF EXISTS create_views_2020();
DROP FUNCTION IF EXISTS create_views_2021();
DROP FUNCTION IF EXISTS create_views_2022();
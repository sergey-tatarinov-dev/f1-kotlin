--liquibase formatted sql

--changeset Sergey Tatarinov:20221208_insert_miami_circuit.sql

insert into track(circuit_name, length, lap_count, country_id) VALUES
('Miami International Autodrome', 5412, 57 ,19);
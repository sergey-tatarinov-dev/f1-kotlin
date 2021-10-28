--liquibase formatted sql

--changeset Sergey Tatarinov:20211025_insert_files_into_table

INSERT INTO file (name, full_path, extension) VALUES
('australia', '/pic/australia.svg', 'svg'),
('bahrain', '/pic/bahrain.svg', 'svg'),
('china', '/pic/china.svg', 'svg'),
('azerbaijan', '/pic/azerbaijan.svg', 'svg'),
('spain', '/pic/spain.svg', 'svg'),
('monaco', '/pic/monaco.svg', 'svg'),
('canada', '/pic/canada.svg', 'svg'),
('france', '/pic/france.svg', 'svg'),
('austria', '/pic/austria.svg', 'svg'),
('greatbritain', '/pic/greatbritain.svg', 'svg'),
('germany', '/pic/germany.svg', 'svg'),
('hungary', '/pic/hungary.svg', 'svg'),
('belgium', '/pic/belgium.svg', 'svg'),
('italy', '/pic/italy.svg', 'svg'),
('singapore', '/pic/singapore.svg', 'svg'),
('russia', '/pic/russia.svg', 'svg'),
('japan', '/pic/japan.svg', 'svg'),
('mexico', '/pic/mexico.svg', 'svg'),
('unitedstates', '/pic/unitedstates.svg', 'svg'),
('brazil', '/pic/brazil.svg', 'svg'),
('arabianemirates', '/pic/arabianemirates.svg', 'svg'),
('portugal', '/pic/arabianemirates.svg', 'svg'),
('turkey', '/pic/turkey.svg', 'svg'),
('netherlands', '/pic/netherlands.svg', 'svg'),
('qatar', '/pic/qatar.svg', 'svg'),
('saudiarabia', '/pic/saudiarabia.svg', 'svg'),
('poland', '/pic/poland.svg', 'svg'),
('thailand', '/pic/thailand.svg', 'svg'),
('finland', '/pic/finland.svg', 'svg');
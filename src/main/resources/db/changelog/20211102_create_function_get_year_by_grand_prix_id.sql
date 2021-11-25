--liquibase formatted sql

--changeset Sergey Tatarinov:20211102_create_function_get_year_by_grand_prix_id


CREATE OR REPLACE FUNCTION get_year_by_grand_prix_id(grandPrixId BIGINT) RETURNS BIGINT AS '
DECLARE
    result BIGINT;
BEGIN
    SELECT DISTINCT extract(year FROM date)
    INTO result
    FROM grand_prix
             INNER JOIN grand_prix_result gpr ON grand_prix.id = gpr.grand_prix_id
    WHERE grandPrixId = gpr.grand_prix_id;
    RETURN result;
END;
' LANGUAGE plpgsql;
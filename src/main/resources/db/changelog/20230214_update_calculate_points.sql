--liquibase formatted sql

--changeset Sergey Tatarinov:20230214_update_calculate_points

CREATE OR REPLACE FUNCTION calculate_points() RETURNS TRIGGER AS '
    DECLARE
        multiplier                  numeric(2, 1) := 1;
        points_for_fastest_lap      smallint := 0;
        points_per_race             smallint := 0;
    BEGIN
        IF TG_OP = ''INSERT'' OR TG_OP = ''UPDATE'' THEN
            IF is_need_apply_half_points_multiplier(new.grand_prix_id)
            THEN multiplier := 0.5;
            END IF;
            IF new.set_fastest_lap AND NOT is_race_a_sprint(new.grand_prix_id) AND new.position <= 10 THEN
                points_for_fastest_lap := 1;
            END IF;
            IF is_race_a_sprint(new.grand_prix_id) THEN
                IF get_year_by_grand_prix_id(new.grand_prix_id) > 2021 THEN
                    points_per_race :=
                            CASE
                                WHEN new.position = 1 THEN 8
                                WHEN new.position = 2 THEN 7
                                WHEN new.position = 3 THEN 6
                                WHEN new.position = 4 THEN 5
                                WHEN new.position = 5 THEN 4
                                WHEN new.position = 6 THEN 3
                                WHEN new.position = 7 THEN 2
                                WHEN new.position = 8 THEN 1
                                ELSE 0
                            END;
                ELSE
                    points_per_race :=
                            CASE
                                WHEN new.position = 1 THEN 3
                                WHEN new.position = 2 THEN 2
                                WHEN new.position = 3 THEN 1
                                ELSE 0
                            END;
                END IF;
            ELSE
                points_per_race :=
                        CASE
                            WHEN new.position = 1 THEN 25
                            WHEN new.position = 2 THEN 18
                            WHEN new.position = 3 THEN 15
                            WHEN new.position = 4 THEN 12
                            WHEN new.position = 5 THEN 10
                            WHEN new.position = 6 THEN 8
                            WHEN new.position = 7 THEN 6
                            WHEN new.position = 8 THEN 4
                            WHEN new.position = 9 THEN 2
                            WHEN new.position = 10 THEN 1
                            ELSE 0
                            END;
            END IF;

            RAISE NOTICE ''points_per_race %, multiplier %, points_for_fastest_lap %'', points_per_race, multiplier, points_for_fastest_lap;

            UPDATE grand_prix_result
            SET points = points_per_race * multiplier + points_for_fastest_lap
            where new.driver_id = driver_id and new.grand_prix_id = grand_prix_id;

        END IF;
        RETURN null;
    END
' LANGUAGE plpgsql;
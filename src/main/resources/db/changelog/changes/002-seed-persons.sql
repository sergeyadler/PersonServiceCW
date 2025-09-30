-- liquibase formatted sql

-- changeset Sergey:1759223486840-1
INSERT INTO person (id, name, birth_date, city, street, building)
VALUES (1003, 'Mark', '1990-01-01', 'Berlin', 'Lenina', 102),
       (1004, 'Ivan Petrov', '1991-01-01', 'Hamburg', 'Hauptstr', 110),
       (1005, 'Dodon', '1996-01-01', 'Leipzig', 'Kantstr', 10);


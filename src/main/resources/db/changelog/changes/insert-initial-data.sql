--liquibase formatted sql
--changeset deschna:insert_initial_data splitStatements:true endDelimiter:;

INSERT INTO teams (id, name, player_transfer_commission, budget) VALUES
    (1, 'Real Madrid', 0.15, 800000.00),
    (2, 'Barcelona', 0.10, 700000.00),
    (3, 'Manchester United', 0.12, 900000.00);

INSERT INTO players (id, firstname, lastname, birth_date, career_start_date, team_id) VALUES
    (1, 'Cristiano', 'Ronaldo', '1985-02-05', '2002-08-01', 1),
    (2, 'Lionel', 'Messi', '1987-06-24', '2004-10-16', 2),
    (3, 'Neymar', 'Junior', '1992-02-05', '2009-03-01', 1),
    (4, 'Kylian', 'Mbappe', '1998-12-20', '2015-12-01', 3);

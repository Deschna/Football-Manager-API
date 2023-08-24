--liquibase formatted sql
--changeset deschna:create_players_table splitStatements:true endDelimiter:;

CREATE TABLE IF NOT EXISTS players (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    firstname VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    birth_date DATE,
    career_start_date DATE,
    team_id BIGINT,
    CONSTRAINT fk_team FOREIGN KEY (team_id) REFERENCES teams(id)
) ENGINE=InnoDB;

--rollback DROP TABLE players;

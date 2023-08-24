--liquibase formatted sql
--changeset deschna:create_teams_table splitStatements:true endDelimiter:;

CREATE TABLE IF NOT EXISTS teams (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    player_transfer_commission DECIMAL(5, 2),
    budget DECIMAL(15, 2)
) ENGINE=InnoDB;

--rollback DROP TABLE teams;

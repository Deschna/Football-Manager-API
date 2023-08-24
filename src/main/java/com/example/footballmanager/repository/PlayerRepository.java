package com.example.footballmanager.repository;

import com.example.footballmanager.model.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    Page<Player> getAllByTeamId(Pageable pageable, Long teamId);
}

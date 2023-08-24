package com.example.footballmanager.service;

import com.example.footballmanager.model.Player;
import com.example.footballmanager.model.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PlayerService {
    Player getById(Long id);

    Page<Player> getAllByTeamId(Pageable pageable, Long teamId);

    Player create(Player player);

    Player updateById(Long id, Player player);

    void deleteById(Long id);

    Player addUnassignedPlayerToTeam(Player player, Team team);

    Player transferPlayerToTeam(Player player, Team buyingTeam);
}

package com.example.footballmanager.service;

import com.example.footballmanager.model.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TeamService {
    Team getById(Long id);

    Page<Team> getAll(Pageable pageable);

    Team create(Team team);

    Team updateById(Long id, Team team);

    void deleteById(Long id);
}

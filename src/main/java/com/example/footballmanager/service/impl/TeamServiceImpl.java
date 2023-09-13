package com.example.footballmanager.service.impl;

import com.example.footballmanager.exception.BadRequestException;
import com.example.footballmanager.exception.EntityNotFoundException;
import com.example.footballmanager.model.Team;
import com.example.footballmanager.repository.TeamRepository;
import com.example.footballmanager.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;

    @Override
    public Team getById(Long id) {
        return teamRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("No team present with id " + id));
    }

    @Override
    public Page<Team> getAll(Pageable pageable) {
        return teamRepository.findAll(pageable);
    }

    @Override
    public Team create(Team team) {
        if (team.getId() != null) {
            throw new BadRequestException("Can't save a new team with an existing id!");
        }
        return teamRepository.save(team);
    }

    @Override
    public Team updateById(Long id, Team team) {
        if(!teamRepository.existsById(id)) {
            throw new EntityNotFoundException("No team present with id " + id);
        }
        team.setId(id);
        return teamRepository.save(team);
    }

    @Override
    public void deleteById(Long id) {
        if(!teamRepository.existsById(id)) {
            throw new EntityNotFoundException("No team present with id " + id);
        }
        teamRepository.deleteById(id);
    }
}

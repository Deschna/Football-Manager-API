package com.example.footballmanager.service.impl;

import com.example.footballmanager.model.Player;
import com.example.footballmanager.model.Team;
import com.example.footballmanager.repository.PlayerRepository;
import com.example.footballmanager.service.PlayerService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {
    private static final int MONTHS_PER_YEAR = 12;
    private static final BigDecimal BASE_MULTIPLIER = BigDecimal.valueOf(100000L);
    private final PlayerRepository playerRepository;

    @Override
    public Player getById(Long id) {
        return playerRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("No player present with id " + id));
    }

    @Override
    public Page<Player> getAllByTeamId(Pageable pageable, Long teamId) {
        return playerRepository.getAllByTeamId(pageable, teamId);
    }

    @Override
    public Player create(Player player) {
        if (player.getId() != null) {
            throw new IllegalArgumentException("Can't save a new player with an existing id");
        }
        return playerRepository.save(player);
    }

    @Override
    public Player updateById(Long id, Player player) {
        if(!playerRepository.existsById(id)) {
            throw new NoSuchElementException("No player present with id " + id);
        }
        player.setId(id);
        return playerRepository.save(player);
    }

    @Override
    public void deleteById(Long id) {
        if(!playerRepository.existsById(id)) {
            throw new NoSuchElementException("No player present with id " + id);
        }
        playerRepository.deleteById(id);
    }

    @Override
    public Player addUnassignedPlayerToTeam(Player player, Team team) {
        if (player.getTeam() != null) {
            throw new IllegalArgumentException("The player is already on the team");
        }
        player.setTeam(team);
        return updateById(player.getId(), player);
    }

    @Transactional
    @Override
    public Player transferPlayerToTeam(Player player, Team buyingTeam) {
        BigDecimal transferFee = calculateTransferFee(player);

        Team sellingTeam = player.getTeam();
        if (sellingTeam == null) {
            throw new IllegalArgumentException("Player does not belong to any team");
        }
        if (player.getTeam().getId().equals(buyingTeam.getId())) {
            throw new IllegalArgumentException("Can't transfer a player to a team he's already on");
        }
        if (buyingTeam.getBudget().compareTo(transferFee) < 0) {
            throw new IllegalArgumentException("Insufficient funds in the team's budget");
        }
        buyingTeam.setBudget(buyingTeam.getBudget().subtract(transferFee));
        sellingTeam.setBudget(sellingTeam.getBudget().add(transferFee));
        player.setTeam(buyingTeam);

        return updateById(player.getId(), player);
    }

    private BigDecimal calculateTransferFee (Player player) {
        Period playerExperience = Period.between(player.getCareerStartDate(), LocalDate.now());
        int playerExperienceInMonths = MONTHS_PER_YEAR * playerExperience.getYears() + playerExperience.getMonths();
        int playerAgeInYears = Period.between(player.getBirthDate(), LocalDate.now()).getYears();

        BigDecimal transferAmount = BigDecimal.valueOf(playerExperienceInMonths)
                .multiply(BASE_MULTIPLIER)
                .divide(BigDecimal.valueOf(playerAgeInYears), 2, RoundingMode.HALF_UP);
        BigDecimal transferCommission = calculateTransferCommission(player,transferAmount);

        return transferAmount.add(transferCommission);
    }

    private BigDecimal calculateTransferCommission(Player player, BigDecimal transferAmount) {
        return transferAmount
                .multiply(player.getTeam().getPlayerTransferCommission())
                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
    }
}
